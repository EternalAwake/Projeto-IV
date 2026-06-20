package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.repositories.BandaRepository;
import com.projeto.songSystem.util.ImageUploadUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BandaService {

    @Autowired
    private BandaRepository bandaRepository;

    @Autowired
    private MusicaService musicaService;

    @Transactional
    public void cadastrarBanda(BandaDTO bandaDTO) throws IOException {
        BandaModel banda = new BandaModel();

        // Preencher os dados
        banda.setBandaNome(bandaDTO.getBandaNome());
        banda.setBandaAnoFormacao(bandaDTO.getBandaAnoFormacao());
        banda.setBandaGenero(bandaDTO.getBandaGenero());
        banda.setBandaPais(bandaDTO.getBandaPais());
        banda.setBandaBiografia(bandaDTO.getBandaBiografia());
        banda.setBandaDestaque(bandaDTO.getBandaDestaque() != null ? bandaDTO.getBandaDestaque() : false);

        // Processar a imagem
        MultipartFile imagem = bandaDTO.getBandaAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "bandas");
            banda.setBandaImagem(caminhoImagem);
        }

        banda.setBandaDataCadastro(LocalDateTime.now());

        bandaRepository.save(banda);
    }

    /**
     * Processa e salva a imagem da banda usando o utilitário central, que valida
     * tipo/tamanho, redimensiona para a resolução máxima e padroniza o formato.
     */
    private String salvarImagem(MultipartFile file, String subPasta) throws IOException {
        return ImageUploadUtil.processarESalvar(file, uploadDir, subPasta);
    }

    public List<BandaModel> listarBandas() {
        return bandaRepository.findAll();
    }

    public List<BandaModel> obterBandasEmDestaque() {
        return bandaRepository.findByBandaDestaqueTrue();
    }

    public BandaDTO obterBanda(Long bandaId) {
        Optional<BandaModel> optionalBandaModel = bandaRepository.findById(bandaId);
        BandaDTO bandaDTO = new BandaDTO();
        if(optionalBandaModel.isPresent()) {
            bandaDTO.setBandaId(optionalBandaModel.get().getBandaId());
            bandaDTO.setBandaImagem(optionalBandaModel.get().getBandaImagem());
            bandaDTO.setBandaNome(optionalBandaModel.get().getBandaNome());
            bandaDTO.setBandaAnoFormacao(optionalBandaModel.get().getBandaAnoFormacao());
            bandaDTO.setBandaGenero(optionalBandaModel.get().getBandaGenero());
            bandaDTO.setBandaPais(optionalBandaModel.get().getBandaPais());
            bandaDTO.setBandaBiografia(optionalBandaModel.get().getBandaBiografia());
            bandaDTO.setBandaDataCadastro(optionalBandaModel.get().getBandaDataCadastro());
            bandaDTO.setBandaDestaque(optionalBandaModel.get().getBandaDestaque());
            bandaDTO.setBandaDataAtualizacao(LocalDateTime.now());

            // Carregar álbuns
            if (optionalBandaModel.get().getAlbuns() != null && !optionalBandaModel.get().getAlbuns().isEmpty()) {
                List<AlbumDTO> albunsDTO = optionalBandaModel.get().getAlbuns().stream()
                        .map(this::converterAlbumParaDTO)
                        .collect(Collectors.toList());
                bandaDTO.setAlbuns(albunsDTO);
            }

            // Carregar músicas
            if (optionalBandaModel.get().getMusicas() != null && !optionalBandaModel.get().getMusicas().isEmpty()) {
                List<MusicaDTO> musicasDTO = optionalBandaModel.get().getMusicas().stream()
                        .map(this::converterMusicaParaDTO)
                        .collect(Collectors.toList());
                bandaDTO.setMusicasDTO(musicasDTO);
            }
        }
        return bandaDTO;
    }

    private AlbumDTO converterAlbumParaDTO(AlbumModel album) {
        AlbumDTO dto = new AlbumDTO();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumNome(album.getAlbumNome());
        dto.setAlbumAnoLancamento(album.getAlbumAnoLancamento());
        dto.setAlbumDuracao(album.getAlbumDuracao());
        dto.setAlbumImagem(album.getAlbumImagem());

        // Carregar músicas do álbum
        if (album.getMusicas() != null) {
            dto.setMusicas(album.getMusicas());
        }

        return dto;
    }

    private MusicaDTO converterMusicaParaDTO(MusicaModel musica) {
        MusicaDTO musicaDTO = new MusicaDTO();
        musicaDTO.setMusicaId(musica.getMusicaId());
        musicaDTO.setMusicaNome(musica.getMusicaNome());
        musicaDTO.setMusicaTom(musica.getMusicaTom());
        musicaDTO.setMusicaDuracao(musica.getMusicaDuracao());
        musicaDTO.setMusicaAnotacoes(musica.getMusicaAnotacoes());
        musicaDTO.setMusicaAfinacao(musica.getMusicaAfinacao());
        musicaDTO.setMusicaDestaque(musica.getMusicaDestaque());

        // Se tiver álbum associado
        if (musica.getAlbum() != null) {
            AlbumDTO albumDTO = new AlbumDTO();
            albumDTO.setAlbumId(musica.getAlbum().getAlbumId());
            albumDTO.setAlbumNome(musica.getAlbum().getAlbumNome());
            musicaDTO.setAlbumDTO(albumDTO);
        }

        return musicaDTO;
    }

    @Transactional
    public String alterarBanda(BandaDTO bandaDto) throws IOException {
        BandaModel banda = bandaRepository.findById(bandaDto.getBandaId())
                .orElseThrow(() -> new RuntimeException(
                        "Banda não encontrada com ID: " + bandaDto.getBandaId()));

        banda.setBandaNome(bandaDto.getBandaNome());
        banda.setBandaBiografia(bandaDto.getBandaBiografia());
        banda.setBandaPais(bandaDto.getBandaPais());
        banda.setBandaGenero(bandaDto.getBandaGenero());
        banda.setBandaAnoFormacao(bandaDto.getBandaAnoFormacao());
        banda.setBandaDestaque(bandaDto.getBandaDestaque() != null
                ? bandaDto.getBandaDestaque() : false);
        banda.setBandaDataAtualizacao(LocalDateTime.now());

        // Processar nova imagem APENAS se uma for enviada; caso contrário mantém a atual
        MultipartFile imagem = bandaDto.getBandaAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "bandas");
            banda.setBandaImagem(caminhoImagem);
        }
        // Não sobrescreve bandaImagem com o valor do DTO quando não há nova imagem

        bandaRepository.save(banda);
        return null;
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public boolean excluirBanda(Long id) {
        // A banda tem cascade ALL sobre álbuns e músicas: ao excluí-la, o Hibernate
        // apaga em cascata todas as músicas (diretas e dos álbuns). Mas essas músicas
        // podem estar em itens de repertório/setlist (FK NOT NULL), o que violaria a
        // integridade referencial. Por isso, limpamos essas dependências antes.
        BandaModel banda = bandaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + id));

        // Músicas ligadas diretamente à banda
        if (banda.getMusicas() != null) {
            for (MusicaModel m : banda.getMusicas()) {
                musicaService.removerDependenciasDeMusica(m.getMusicaId());
            }
        }
        // Músicas ligadas aos álbuns da banda
        if (banda.getAlbuns() != null) {
            for (AlbumModel album : banda.getAlbuns()) {
                if (album.getMusicas() != null) {
                    for (MusicaModel m : album.getMusicas()) {
                        musicaService.removerDependenciasDeMusica(m.getMusicaId());
                    }
                }
            }
        }

        bandaRepository.deleteById(id);
        return true;
    }

    private void deletarImagem(String caminhoImagem) {
        if (caminhoImagem != null && caminhoImagem.startsWith("/uploads/")) {
            try {
                // Remove o prefixo "/uploads/" e resolve relativo ao diretório base,
                // de forma consistente com o ImageUploadUtil (que usa Paths.get).
                String relativo = caminhoImagem.replaceFirst("^/uploads/", "");
                Path caminho = Paths.get(uploadDir).resolve(relativo).normalize();
                Files.deleteIfExists(caminho);
            } catch (IOException e) {
                System.err.println("Erro ao deletar imagem: " + e.getMessage());
            }
        }
    }

    public long obterQtdBandas() {
        return bandaRepository.count();
    }

    public BandaModel obterBandaModelPorId(Long bandaId) {
        return bandaRepository.findById(bandaId)
                .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + bandaId));
    }

}
