package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.dto.FiltroCustomizadoDTO;
import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.repositories.BandaRepository;
import com.projeto.songSystem.specifications.BandaSpecification;
import jakarta.transaction.Transactional;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BandaService {

    @Autowired
    private BandaRepository bandaRepository;

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

    private String salvarImagem(MultipartFile file, String subPasta) throws IOException {
        // Gerar nome único para o arquivo
        String nomeOriginal = file.getOriginalFilename();
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Criar diretório se não existir
        Path caminhoCompleto = Paths.get(uploadDir + subPasta);
        Files.createDirectories(caminhoCompleto);

        // Salvar arquivo
        Path caminhoArquivo = caminhoCompleto.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), caminhoArquivo);

        // Retornar o caminho relativo para salvar no banco
        return "/uploads/" + subPasta + "/" + nomeArquivo;
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
        Optional<BandaModel> optionalBandaModel = bandaRepository.findById(bandaDto.getBandaId());

        optionalBandaModel.get().setBandaId(bandaDto.getBandaId());
        optionalBandaModel.get().setBandaNome(bandaDto.getBandaNome());

        optionalBandaModel.get().setBandaImagem(bandaDto.getBandaImagem());

        optionalBandaModel.get().setBandaBiografia(bandaDto.getBandaBiografia());
        optionalBandaModel.get().setBandaPais(bandaDto.getBandaPais());
        optionalBandaModel.get().setBandaGenero(bandaDto.getBandaGenero());
        optionalBandaModel.get().setBandaAnoFormacao(bandaDto.getBandaAnoFormacao());
        optionalBandaModel.get().setBandaDestaque(bandaDto.getBandaDestaque());
        optionalBandaModel.get().setBandaDataAtualizacao(LocalDateTime.now());

        // Processar a imagem APENAS se uma nova for enviada
        MultipartFile imagem = bandaDto.getBandaAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "bandas");
            optionalBandaModel.get().setBandaImagem(caminhoImagem);
        }

        bandaRepository.save(optionalBandaModel.get());
        return null;
    }

    public List<BandaModel> filtrarBandas(List<FiltroCustomizadoDTO> filtros) {
        if (filtros == null || filtros.isEmpty()) {
            return bandaRepository.findAll();
        }
        return bandaRepository.findAll(BandaSpecification.comFiltros(filtros));
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public boolean excluirBanda(Long id) {
        bandaRepository.deleteById(id);
        return true;
    }

    private void deletarImagem(String caminhoImagem) {
        if (caminhoImagem != null && caminhoImagem.startsWith("/uploads/")) {
            try {
                String caminhoCompleto = uploadDir + caminhoImagem.replace("/uploads/", "");
                Path caminho = Paths.get(caminhoCompleto);
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
