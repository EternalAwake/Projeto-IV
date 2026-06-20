package com.projeto.songSystem.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Utilitário centralizado para processamento de imagens de upload.
 *
 * Responsabilidades:
 *  - Validar tipo MIME e extensão (whitelist de formatos seguros).
 *  - Redimensionar a imagem para caber dentro de um limite máximo de pixels,
 *    mantendo a proporção original (sem distorção).
 *  - Padronizar o formato de saída (JPEG para fotografias, PNG para o resto).
 *  - Expor getInputStream() para o chamador salvar onde quiser — sem acoplamento
 *    com o sistema de arquivos.
 *
 * Limites adotados:
 *  - Resolução máxima: 1200 × 1200 px  (capa de álbum/banda em alta qualidade)
 *  - Tamanho de arquivo: validado no application.properties (5 MB via multipart)
 *  - Formatos aceitos: JPEG, PNG, WebP, GIF
 */
public final class ImageUtil {

    /** Resolução máxima em qualquer dimensão (px). Imagens maiores são redimensionadas. */
    public static final int MAX_DIM = 1200;

    /** Resolução alvo para miniaturas (thumbnails), se necessário no futuro. */
    public static final int THUMB_DIM = 300;

    /** Qualidade JPEG de saída (0.0–1.0). 0.85 = bom equilíbrio qualidade/tamanho. */
    private static final float JPEG_QUALITY = 0.85f;

    /** Tipos MIME aceitos (whitelist). */
    private static final Set<String> MIME_ACEITOS = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    /** Extensões aceitas correspondentes (whitelist). */
    private static final Set<String> EXT_ACEITAS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp", ".gif"
    );

    private ImageUtil() {}

    /**
     * Resultado do processamento: InputStream pronto para escrita em disco
     * e a extensão normalizada a ser usada no nome do arquivo.
     */
    public static final class Resultado {
        private final InputStream inputStream;
        private final String extensao; // ex: ".jpg"
        private final int largura;
        private final int altura;

        private Resultado(InputStream is, String ext, int w, int h) {
            this.inputStream = is;
            this.extensao = ext;
            this.largura = w;
            this.altura = h;
        }

        public InputStream getInputStream() { return inputStream; }
        public String getExtensao()         { return extensao; }
        public int getLargura()             { return largura; }
        public int getAltura()              { return altura; }
    }

    /**
     * Processa um MultipartFile de imagem:
     *  1. Valida tipo e extensão.
     *  2. Lê como BufferedImage.
     *  3. Redimensiona se exceder MAX_DIM, preservando proporção.
     *  4. Codifica de volta (JPEG para jpg/webp, PNG para o resto).
     *
     * @param file arquivo recebido do formulário
     * @return Resultado com InputStream processado e extensão normalizada
     * @throws IllegalArgumentException se o tipo não for permitido
     * @throws IOException se a imagem estiver corrompida ou ilegível
     */
    public static Resultado processar(MultipartFile file) throws IOException {
        validar(file);

        String mime = file.getContentType().toLowerCase();
        String extOriginal = extrairExtensao(file.getOriginalFilename());

        // Decidir formato de saída
        // WebP e JPEG → salvar como JPEG (ImageIO não escreve WebP nativo)
        // PNG / GIF   → salvar como PNG (preserva transparência)
        boolean usarJpeg = mime.contains("jpeg") || mime.contains("jpg") || mime.contains("webp");
        String extSaida  = usarJpeg ? ".jpg" : ".png";
        String formatoIO = usarJpeg ? "jpg"  : "png";

        // Decodificar
        BufferedImage original = ImageIO.read(file.getInputStream());
        if (original == null) {
            throw new IOException("Não foi possível decodificar a imagem. Verifique se o arquivo não está corrompido.");
        }

        // Redimensionar se necessário
        BufferedImage processada = redimensionar(original, MAX_DIM);

        // Codificar para bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (usarJpeg) {
            escreverJpeg(processada, baos, JPEG_QUALITY);
        } else {
            ImageIO.write(processada, formatoIO, baos);
        }

        return new Resultado(
                new ByteArrayInputStream(baos.toByteArray()),
                extSaida,
                processada.getWidth(),
                processada.getHeight()
        );
    }

    // ─── Métodos internos ────────────────────────────────────────────────────

    /**
     * Valida tipo MIME e extensão. Lança IllegalArgumentException com mensagem
     * amigável se inválido.
     */
    public static void validar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem enviada.");
        }

        String mime = file.getContentType();
        if (mime == null || !MIME_ACEITOS.contains(mime.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Formato de imagem não permitido: " + mime +
                    ". Use JPEG, PNG, WebP ou GIF.");
        }

        String nome = file.getOriginalFilename();
        if (nome == null || nome.lastIndexOf('.') < 0) {
            throw new IllegalArgumentException("Nome de arquivo inválido.");
        }
        String ext = nome.substring(nome.lastIndexOf('.')).toLowerCase();
        if (!EXT_ACEITAS.contains(ext)) {
            throw new IllegalArgumentException(
                    "Extensão de arquivo não permitida: " + ext +
                    ". Use .jpg, .jpeg, .png, .webp ou .gif.");
        }
    }

    /**
     * Redimensiona a imagem para que nenhuma dimensão exceda maxDim,
     * preservando a proporção. Se já couber dentro do limite, retorna
     * a imagem original sem cópia.
     */
    public static BufferedImage redimensionar(BufferedImage img, int maxDim) {
        int w = img.getWidth();
        int h = img.getHeight();

        if (w <= maxDim && h <= maxDim) {
            // Já está dentro do limite — não reprocessar
            return img;
        }

        // Calcular novo tamanho mantendo proporção
        double escala = Math.min((double) maxDim / w, (double) maxDim / h);
        int novaW = (int) Math.round(w * escala);
        int novaH = (int) Math.round(h * escala);

        // Usar BICUBIC para melhor qualidade de redução
        BufferedImage resultado = new BufferedImage(novaW, novaH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultado.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                               RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, novaW, novaH);
            g.drawImage(img, 0, 0, novaW, novaH, null);
        } finally {
            g.dispose();
        }

        return resultado;
    }

    private static void escreverJpeg(BufferedImage img, ByteArrayOutputStream out,
                                     float qualidade) throws IOException {
        // Garantir RGB (JPEG não suporta canal alfa)
        BufferedImage rgb;
        if (img.getType() != BufferedImage.TYPE_INT_RGB) {
            rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgb.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.drawImage(img, 0, 0, null);
            g.dispose();
        } else {
            rgb = img;
        }

        var writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) throw new IOException("Encoder JPEG não disponível.");

        var writer = writers.next();
        var param  = writer.getDefaultWriteParam();
        param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(qualidade);

        try (var ios = ImageIO.createImageOutputStream(out)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(rgb, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    private static String extrairExtensao(String nome) {
        if (nome == null || nome.lastIndexOf('.') < 0) return "";
        return nome.substring(nome.lastIndexOf('.')).toLowerCase();
    }
}
