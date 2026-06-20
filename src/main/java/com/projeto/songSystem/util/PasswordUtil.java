package com.projeto.songSystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitário de hash de senhas usando SHA-256 com salt aleatório.
 *
 * Implementado em Java puro (java.security), sem nenhuma dependência externa.
 *
 * Formato do hash armazenado: "SHA256$<salt_base64>$<hash_base64>"
 *   - SHA256  → identificador do algoritmo (permite detectar e migrar no futuro)
 *   - salt    → 16 bytes aleatórios gerados por SecureRandom (único por senha)
 *   - hash    → SHA-256(salt + senha), codificado em Base64
 *
 * Propriedades de segurança:
 *   - Salt aleatório por senha → protege contra rainbow tables e ataques de
 *     dicionário pré-computados; dois hashes da mesma senha são sempre diferentes.
 *   - SecureRandom → gerador criptograficamente seguro (não previsível).
 *   - SHA-256 → função one-way; não é possível recuperar a senha a partir do hash.
 *
 * Migração gradual: senhas antigas em texto puro ainda são aceitas e rehashadas
 * automaticamente no primeiro uso (ver UsuarioService.autenticar).
 */
public final class PasswordUtil {

    /** Prefixo que identifica hashes gerados por este utilitário. */
    private static final String PREFIX = "SHA256$";

    /** Tamanho do salt em bytes. 16 bytes = 128 bits de entropia. */
    private static final int SALT_BYTES = 16;

    private PasswordUtil() {}

    /**
     * Gera o hash de uma senha em texto puro.
     * Cada chamada produz um hash diferente devido ao salt aleatório.
     *
     * @param senhaEmTexto senha em texto puro
     * @return string no formato "SHA256$<salt_base64>$<hash_base64>"
     */
    public static String hash(String senhaEmTexto) {
        byte[] salt = gerarSalt();
        byte[] hashBytes = sha256(salt, senhaEmTexto);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

        return PREFIX + saltBase64 + "$" + hashBase64;
    }

    /**
     * Verifica se uma senha em texto puro corresponde ao hash armazenado.
     *
     * Também suporta senhas legadas em texto puro para migração gradual:
     * se o valor armazenado não começa com "SHA256$", é tratado como texto puro
     * e comparado diretamente.
     *
     * @param senhaEmTexto  senha digitada pelo usuário
     * @param hashArmazenado valor armazenado no banco
     * @return true se a senha confere
     */
    public static boolean verificar(String senhaEmTexto, String hashArmazenado) {
        if (senhaEmTexto == null || hashArmazenado == null || hashArmazenado.isEmpty()) {
            return false;
        }

        // Hash gerado por este utilitário
        if (hashArmazenado.startsWith(PREFIX)) {
            String[] partes = hashArmazenado.split("\\$");
            // partes[0] = "SHA256", partes[1] = salt, partes[2] = hash
            if (partes.length != 3) return false;

            byte[] salt      = Base64.getDecoder().decode(partes[1]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[2]);
            byte[] hashCalculado = sha256(salt, senhaEmTexto);

            return constantTimeEquals(hashCalculado, hashEsperado);
        }

        // Senha legada em texto puro — comparação segura contra timing attacks
        return constantTimeEquals(senhaEmTexto.getBytes(), hashArmazenado.getBytes());
    }

    // ─── Métodos internos ────────────────────────────────────────────────────

    private static byte[] gerarSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] sha256(byte[] salt, String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            md.update(senha.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 é obrigatório na especificação Java — nunca deve ocorrer
            throw new RuntimeException("SHA-256 não disponível", e);
        }
    }

    /**
     * Comparação em tempo constante: evita timing attacks que poderiam deduzir
     * o hash correto medindo o tempo de resposta de comparações byte a byte.
     */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int resultado = 0;
        for (int i = 0; i < a.length; i++) {
            resultado |= (a[i] ^ b[i]);
        }
        return resultado == 0;
    }
}
