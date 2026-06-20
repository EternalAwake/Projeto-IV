package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.models.enums.Role;
import com.projeto.songSystem.repositories.UsuarioRepository;
import com.projeto.songSystem.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ─── Cadastro ────────────────────────────────────────────────────────────

    @Transactional
    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setSenha(PasswordUtil.hash(usuarioDTO.getSenha()));
        usuario.setAtivo(true);
        usuario.setRole(Role.USER);
        usuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return converterParaDTO(usuario);
    }

    // ─── Leitura ─────────────────────────────────────────────────────────────

    public UsuarioDTO buscarPorId(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public UsuarioDTO buscarPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAllByOrderByDataCadastroDesc().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // ─── Atualização ─────────────────────────────────────────────────────────

    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDataAtualizacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return converterParaDTO(usuario);
    }

    /**
     * Troca a senha do usuário após validações completas.
     * Regras: senha atual correta, nova ≥ 8 chars, confirmação idêntica, nova ≠ atual.
     */
    @Transactional
    public void trocarSenha(Long usuarioId,
                            String senhaAtual,
                            String novaSenha,
                            String confirmarNovaSenha) {

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!PasswordUtil.verificar(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new IllegalArgumentException("A nova senha deve ter no mínimo 8 caracteres.");
        }
        if (!novaSenha.equals(confirmarNovaSenha)) {
            throw new IllegalArgumentException("A confirmação da nova senha não confere.");
        }
        if (PasswordUtil.verificar(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha não pode ser igual à senha atual.");
        }

        usuario.setSenha(PasswordUtil.hash(novaSenha));
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // ─── Pergunta secreta ─────────────────────────────────────────────────────

    /**
     * Salva ou atualiza a pergunta e resposta secreta do usuário.
     * A resposta é armazenada como hash (mesmo esquema da senha).
     */
    @Transactional
    public void salvarPerguntaSecreta(Long usuarioId, String pergunta, String resposta) {
        if (pergunta == null || pergunta.isBlank()) {
            throw new IllegalArgumentException("A pergunta secreta não pode estar vazia.");
        }
        if (resposta == null || resposta.isBlank()) {
            throw new IllegalArgumentException("A resposta não pode estar vazia.");
        }

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setPerguntaSecreta(pergunta.trim());
        usuario.setRespostaSecreta(PasswordUtil.hash(resposta.trim().toLowerCase()));
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    /**
     * Retorna a pergunta secreta do usuário identificado por username ou e-mail.
     * Não revela se o usuário existe: retorna null em vez de lançar exceção.
     */
    public String buscarPerguntaSecreta(String usernameOuEmail) {
        Optional<UsuarioModel> opt = usuarioRepository.findByEmail(usernameOuEmail);
        if (opt.isEmpty()) opt = usuarioRepository.findByUsername(usernameOuEmail);
        if (opt.isEmpty() || opt.get().getPerguntaSecreta() == null) return null;
        return opt.get().getPerguntaSecreta();
    }

    /**
     * Redefine a senha após verificar a resposta secreta.
     * Retorna false se usuário não encontrado, resposta errada ou sem pergunta cadastrada.
     */
    @Transactional
    public boolean redefinirSenhaComResposta(String usernameOuEmail,
                                             String resposta,
                                             String novaSenha,
                                             String confirmar) {
        Optional<UsuarioModel> opt = usuarioRepository.findByEmail(usernameOuEmail);
        if (opt.isEmpty()) opt = usuarioRepository.findByUsername(usernameOuEmail);
        if (opt.isEmpty()) return false;

        UsuarioModel usuario = opt.get();

        if (usuario.getRespostaSecreta() == null) return false;
        if (!PasswordUtil.verificar(resposta.trim().toLowerCase(), usuario.getRespostaSecreta())) return false;

        if (novaSenha == null || novaSenha.length() < 8) {
            throw new IllegalArgumentException("A nova senha deve ter no mínimo 8 caracteres.");
        }
        if (!novaSenha.equals(confirmar)) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }

        usuario.setSenha(PasswordUtil.hash(novaSenha));
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return true;
    }

    // ─── Admin: primeiro acesso ───────────────────────────────────────────────

    /**
     * Conclui o setup inicial do admin: define senha, pergunta e resposta secreta.
     * Só funciona quando o admin ainda não tem senha (isPrimeiroAcesso).
     */
    @Transactional
    public void concluirSetupAdmin(Long adminId,
                                   String novaSenha,
                                   String confirmar,
                                   String pergunta,
                                   String resposta) {
        UsuarioModel admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));

        if (!admin.isPrimeiroAcesso()) {
            throw new IllegalStateException("Setup já foi concluído.");
        }
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
        }
        if (!novaSenha.equals(confirmar)) {
            throw new IllegalArgumentException("As senhas não conferem.");
        }
        if (pergunta == null || pergunta.isBlank()) {
            throw new IllegalArgumentException("A pergunta secreta é obrigatória.");
        }
        if (resposta == null || resposta.isBlank()) {
            throw new IllegalArgumentException("A resposta secreta é obrigatória.");
        }

        admin.setSenha(PasswordUtil.hash(novaSenha));
        admin.setPerguntaSecreta(pergunta.trim());
        admin.setRespostaSecreta(PasswordUtil.hash(resposta.trim().toLowerCase()));
        admin.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(admin);
    }

    // ─── Admin: gerenciamento de usuários ────────────────────────────────────

    /**
     * Permite que um administrador defina uma nova senha para uma conta comum.
     * Contas ADMIN nunca podem ser alteradas por este fluxo.
     */
    @Transactional
    public void redefinirSenhaPorAdmin(Long adminId,
                                       Long usuarioId,
                                       String novaSenha,
                                       String confirmarNovaSenha) {
        UsuarioModel admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado."));

        if (!Role.ADMIN.equals(admin.getRole()) || Boolean.FALSE.equals(admin.getAtivo())) {
            throw new IllegalArgumentException("Você não tem permissão para redefinir senhas.");
        }
        if (adminId.equals(usuarioId)) {
            throw new IllegalArgumentException("Use o perfil para alterar a sua própria senha.");
        }

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (Role.ADMIN.equals(usuario.getRole())) {
            throw new IllegalArgumentException("A senha de outro administrador não pode ser redefinida.");
        }
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new IllegalArgumentException("A nova senha deve ter no mínimo 8 caracteres.");
        }
        if (novaSenha.length() > 128) {
            throw new IllegalArgumentException("A nova senha deve ter no máximo 128 caracteres.");
        }
        if (!novaSenha.equals(confirmarNovaSenha)) {
            throw new IllegalArgumentException("A confirmação da nova senha não confere.");
        }
        if (PasswordUtil.verificar(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual do usuário.");
        }

        usuario.setSenha(PasswordUtil.hash(novaSenha));
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluirUsuario(Long adminId, Long usuarioId) {
        if (adminId.equals(usuarioId)) {
            throw new IllegalArgumentException("O admin não pode excluir a própria conta.");
        }
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (Role.ADMIN.equals(usuario.getRole()) && usuario.getUsername().equals("admin")) {
            throw new IllegalArgumentException("Não é possível excluir o admin principal.");
        }
        usuarioRepository.delete(usuario);
    }

    /**
     * Promove ou rebaixa um usuário.
     * O admin principal ('admin') não pode ser rebaixado.
     * Um admin não pode alterar a própria role.
     */
    @Transactional
    public void alterarRole(Long adminId, Long usuarioId, Role novaRole) {
        if (adminId.equals(usuarioId)) {
            throw new IllegalArgumentException("Você não pode alterar a própria role.");
        }
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (usuario.getUsername().equals("admin") && novaRole == Role.USER) {
            throw new IllegalArgumentException("O admin principal não pode ser rebaixado.");
        }
        usuario.setRole(novaRole);
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // ─── Autenticação ─────────────────────────────────────────────────────────

    /**
     * Autentica por e-mail ou username.
     * Para o admin sem senha definida, retorna o model para que o controller
     * redirecione para o setup inicial (verificado via isPrimeiroAcesso()).
     */
    @Transactional
    public UsuarioModel autenticar(String emailOuUsername, String senha) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(emailOuUsername);
        if (usuarioOpt.isEmpty()) usuarioOpt = usuarioRepository.findByUsername(emailOuUsername);
        if (usuarioOpt.isEmpty()) return null;

        UsuarioModel usuario = usuarioOpt.get();
        if (Boolean.FALSE.equals(usuario.getAtivo())) return null;

        // Admin sem senha: retorna o model sem verificar senha
        // (o controller redireciona para setup)
        if (usuario.isPrimeiroAcesso()) return usuario;

        if (!PasswordUtil.verificar(senha, usuario.getSenha())) return null;

        // Migração gradual: rehashear senhas legadas em texto puro
        if (!usuario.getSenha().startsWith("SHA256$")) {
            usuario.setSenha(PasswordUtil.hash(senha));
            usuario.setDataAtualizacao(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }

        return usuario;
    }

    @Transactional
    public void desativarUsuario(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(false);
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // ─── Inicialização do admin ───────────────────────────────────────────────

    /**
     * Chamado na inicialização da aplicação.
     * Cria o usuário 'admin' sem senha se ele ainda não existir.
     */
    @Transactional
    public void garantirAdminExiste() {
        if (!usuarioRepository.existsByUsername("admin")) {
            UsuarioModel admin = new UsuarioModel();
            admin.setUsername("admin");
            admin.setEmail("admin@songsystem.local");
            admin.setNome("Administrador");
            admin.setSenha(null); // sem senha — primeiro login define
            admin.setAtivo(true);
            admin.setRole(Role.ADMIN);
            admin.setDataCadastro(LocalDateTime.now());
            usuarioRepository.save(admin);
        }
    }

    // ─── Conversão ────────────────────────────────────────────────────────────

    private UsuarioDTO converterParaDTO(UsuarioModel usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setAtivo(usuario.getAtivo());
        dto.setRole(usuario.getRole());
        dto.setPerguntaSecreta(usuario.getPerguntaSecreta());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());
        // senha e respostaSecreta NUNCA saem no DTO
        return dto;
    }
}
