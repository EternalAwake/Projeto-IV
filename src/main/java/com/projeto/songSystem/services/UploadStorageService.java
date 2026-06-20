package com.projeto.songSystem.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Gerencia a remoção segura dos arquivos salvos no diretório de uploads.
 *
 * Os arquivos vinculados a registros do banco só são apagados depois que a
 * transação é confirmada. Assim, uma falha de integridade ou rollback não deixa
 * o banco apontando para uma imagem que já foi removida do disco.
 */
@Service
public class UploadStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadStorageService.class);
    private static final String PREFIXO_PUBLICO = "/uploads/";

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Registra um arquivo recém-criado para ser removido caso a transação falhe.
     */
    public void registrarNovoArquivo(String caminhoPublico) {
        if (!isUploadGerenciado(caminhoPublico)) {
            return;
        }

        if (transacaoSincronizada()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status != TransactionSynchronization.STATUS_COMMITTED) {
                        deletarAgora(caminhoPublico);
                    }
                }
            });
        }
    }

    /**
     * Em uma substituição, remove a imagem antiga após o commit. Se houver
     * rollback, remove a imagem nova para não deixar arquivo órfão.
     */
    public void registrarSubstituicao(String caminhoAntigo, String caminhoNovo) {
        if (Objects.equals(caminhoAntigo, caminhoNovo)) {
            return;
        }

        if (transacaoSincronizada()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    deletarAgora(caminhoAntigo);
                }

                @Override
                public void afterCompletion(int status) {
                    if (status != TransactionSynchronization.STATUS_COMMITTED) {
                        deletarAgora(caminhoNovo);
                    }
                }
            });
            return;
        }

        // Fora de uma transação, considera-se que a alteração já foi concluída.
        deletarAgora(caminhoAntigo);
    }

    public void agendarExclusaoAposCommit(String caminhoPublico) {
        agendarExclusaoAposCommit(Set.of(caminhoPublico == null ? "" : caminhoPublico));
    }

    public void agendarExclusaoAposCommit(Collection<String> caminhosPublicos) {
        if (caminhosPublicos == null || caminhosPublicos.isEmpty()) {
            return;
        }

        Set<String> caminhosValidos = new LinkedHashSet<>();
        for (String caminho : caminhosPublicos) {
            if (isUploadGerenciado(caminho)) {
                caminhosValidos.add(caminho);
            }
        }
        if (caminhosValidos.isEmpty()) {
            return;
        }

        Runnable exclusao = () -> caminhosValidos.forEach(this::deletarAgora);

        if (transacaoSincronizada()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    exclusao.run();
                }
            });
        } else {
            exclusao.run();
        }
    }

    public boolean isUploadGerenciado(String caminhoPublico) {
        return caminhoPublico != null && caminhoPublico.startsWith(PREFIXO_PUBLICO);
    }

    private boolean transacaoSincronizada() {
        return TransactionSynchronizationManager.isActualTransactionActive()
                && TransactionSynchronizationManager.isSynchronizationActive();
    }

    private void deletarAgora(String caminhoPublico) {
        if (!isUploadGerenciado(caminhoPublico)) {
            return;
        }

        Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
        String relativo = caminhoPublico.substring(PREFIXO_PUBLICO.length());
        Path destino = base.resolve(relativo).normalize();

        // Impede path traversal e qualquer exclusão fora de app.upload.dir.
        if (!destino.startsWith(base)) {
            LOGGER.warn("Tentativa de exclusão fora do diretório de uploads bloqueada: {}", caminhoPublico);
            return;
        }

        try {
            Files.deleteIfExists(destino);
        } catch (IOException e) {
            // O banco já confirmou a operação. Registra o erro para permitir
            // manutenção posterior sem transformar a exclusão em erro 500.
            LOGGER.error("Não foi possível excluir o arquivo de upload: {}", destino, e);
        }
    }
}
