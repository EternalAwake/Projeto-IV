package com.projeto.songSystem.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * Utilitário central para upload e processamento de imagens.
 *
 * Responsabilidades:
 *  - Validar que o arquivo é realmente uma imagem (tipo MIME + conteúdo decodificável).
 *  - Validar tamanho máximo do arquivo.
 *  - Redimensionar mantendo proporção até uma resolução máxima.
 *  - Padronizar o formato de saída para JPEG (capas/avatares não precisam de transparência).
 *  - Gerar nome único e gravar no diretório configurado.
 *
 * Usa apenas javax.imageio / java.awt (módulo java.desktop do JDK) — sem dependências externas.
 */
public final class ImageUploadUtil {

    /** Tipos MIME aceitos no upload. */
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );

    /** Tamanho máximo do arquivo enviado: 5 MB. */
    private static final long TAMANHO_MAX_BYTES = 5L * 1024 * 1024;

    /** Resolução máxima (lado maior). Imagens maiores são reduzidas proporcionalmente. */
    private static final int RESOLUCAO_MAX = 800;

    /** Qualidade não se aplica diretamente ao ImageIO JPEG padrão; mantemos saída consistente. */
    private static final String FORMATO_SAIDA = "jpg";

    private ImageUploadUtil() {}

    /**
     * Processa e salva uma imagem enviada.
     *
     * @param file     arquivo enviado pelo formulário
     * @param uploadDir diretório base de uploads (ex.: configurado em app.upload.dir)
     * @param subPasta  subpasta de destino (ex.: "bandas", "albuns")
     * @return caminho relativo para gravar no banco (ex.: "/uploads/bandas/uuid.jpg")
     * @throws IOException              em falha de leitura/gravação
     * @throws IllegalArgumentException se o arquivo for inválido (tipo, tamanho, corrompido)
     */
    public static String processarESalvar(MultipartFile file, String uploadDir, String subPasta)
            throws IOException {

        validar(file);

        // Decodificar a imagem; se vier null, não é uma imagem válida
        BufferedImage original;
        try (ByteArrayInputStream in = new ByteArrayInputStream(file.getBytes())) {
            original = ImageIO.read(in);
        }
        if (original == null) {
            throw new IllegalArgumentException(
                    "Arquivo enviado não é uma imagem válida ou está corrompido.");
        }

        // Redimensionar mantendo proporção se exceder a resolução máxima
        BufferedImage processada = redimensionar(original, RESOLUCAO_MAX);

        // Achatar transparência para fundo branco (JPEG não suporta canal alfa)
        BufferedImage semAlpha = removerTransparencia(processada);

        // Montar caminho de destino de forma robusta (Paths.get cuida do separador)
        Path pastaDestino = Paths.get(uploadDir, subPasta);
        Files.createDirectories(pastaDestino);

        String nomeArquivo = UUID.randomUUID() + "." + FORMATO_SAIDA;
        Path destino = pastaDestino.resolve(nomeArquivo);

        // Gravar como JPEG padronizado
        boolean escreveu = ImageIO.write(semAlpha, FORMATO_SAIDA, destino.toFile());
        if (!escreveu) {
            throw new IOException("Não foi possível gravar a imagem no formato " + FORMATO_SAIDA);
        }

        // Caminho relativo servido por /uploads/** (ver WebConfig)
        return "/uploads/" + subPasta + "/" + nomeArquivo;
    }

    /** Valida tipo MIME e tamanho do arquivo antes de processar. */
    private static void validar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo de imagem foi enviado.");
        }
        if (file.getSize() > TAMANHO_MAX_BYTES) {
            throw new IllegalArgumentException(
                    "A imagem excede o tamanho máximo de 5 MB.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Formato inválido. Envie uma imagem JPG, PNG ou WEBP.");
        }
    }

    /**
     * Redimensiona a imagem mantendo a proporção, de modo que o maior lado
     * não ultrapasse {@code maxLado}. Imagens menores são mantidas como estão.
     */
    private static BufferedImage redimensionar(BufferedImage original, int maxLado) {
        int largura = original.getWidth();
        int altura = original.getHeight();

        if (largura <= maxLado && altura <= maxLado) {
            return original; // já está dentro do limite
        }

        double escala = (largura >= altura)
                ? (double) maxLado / largura
                : (double) maxLado / altura;

        int novaLargura = Math.max(1, (int) Math.round(largura * escala));
        int novaAltura  = Math.max(1, (int) Math.round(altura * escala));

        BufferedImage redimensionada =
                new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = redimensionada.createGraphics();
        // Qualidade de reamostragem
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, novaLargura, novaAltura, null);
        g.dispose();
        return redimensionada;
    }

    /**
     * Garante uma imagem RGB sem canal alfa (necessário para salvar como JPEG).
     * PNGs transparentes recebem fundo branco.
     */
    private static BufferedImage removerTransparencia(BufferedImage img) {
        if (img.getType() == BufferedImage.TYPE_INT_RGB) {
            return img;
        }
        BufferedImage rgb = new BufferedImage(
                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return rgb;
    }
}
