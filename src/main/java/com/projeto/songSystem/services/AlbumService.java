package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.repositories.AlbumRepository;
import com.projeto.songSystem.repositories.BandaRepository;
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
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private BandaRepository bandaRepository;

    public long obterQtdAlbuns() {
        return albumRepository.count();
    }

    public List<AlbumModel> listarAlbuns() {
        return albumRepository.findAll();
    }

    @Transactional
    public void cadastrarAlbum(AlbumDTO albumDTO) throws IOException {
        AlbumModel albumModel = new AlbumModel();

        // Preencher os dados
        albumModel.setAlbumNome(albumDTO.getAlbumNome());
        albumModel.setBanda(albumDTO.getBanda());
        albumModel.setAlbumAnoLancamento(albumDTO.getAlbumAnoLancamento());
        albumModel.setAlbumDestaque(albumDTO.getAlbumDestaque() != null ? albumDTO.getAlbumDestaque() : false);
        albumModel.setAlbumNumeroFaixas(albumDTO.getAlbumNumeroFaixas());
        albumModel.setAlbumDuracao(albumDTO.getAlbumDuracao());
        albumModel.setAlbumObservacoes(albumDTO.getAlbumObservacoes());

        // Processar a imagem
        MultipartFile imagem = albumDTO.getAlbumAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "bandas");
            albumModel.setAlbumImagem(caminhoImagem);
        }

        albumModel.setDataCadastro(LocalDateTime.now());

        albumRepository.save(albumModel);
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

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<AlbumModel> obterAlbunsEmDestaque() {
        return albumRepository.findByAlbumDestaqueTrue();
    }

    @Transactional
    public boolean excluirAlbum(Long id) {
        albumRepository.deleteById(id);
        return true;
    }

    public AlbumDTO obterAlbumCompleto(Long albumId) {
        Optional<AlbumModel> optionalAlbumModel = albumRepository.findById(albumId);

        if (optionalAlbumModel.isEmpty()) {
            throw new RuntimeException("Álbum não encontrado");
        }

        AlbumModel album = optionalAlbumModel.get();

        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setAlbumId(album.getAlbumId());
        albumDTO.setAlbumNome(album.getAlbumNome());
        albumDTO.setAlbumAnoLancamento(album.getAlbumAnoLancamento());
        albumDTO.setAlbumDuracao(album.getAlbumDuracao());
        albumDTO.setAlbumObservacoes(album.getAlbumObservacoes());
        albumDTO.setAlbumImagem(album.getAlbumImagem());
        albumDTO.setAlbumDestaque(album.getAlbumDestaque());
        albumDTO.setAlbumNumeroFaixas(album.getAlbumNumeroFaixas());

        // IMPORTANTE: Setar o bandaId para o select
        if (album.getBanda() != null) {
            albumDTO.setBandaId(album.getBanda().getBandaId());

            BandaDTO bandaDTO = new BandaDTO();
            bandaDTO.setBandaId(album.getBanda().getBandaId());
            bandaDTO.setBandaNome(album.getBanda().getBandaNome());
            albumDTO.setBandaDTO(bandaDTO);
        }

        if (album.getMusicas() != null && !album.getMusicas().isEmpty()) {
            List<MusicaDTO> musicasDTO = album.getMusicas().stream()
                    .map(this::converterMusicaParaDTO)
                    .collect(Collectors.toList());
            albumDTO.setMusicasDto(musicasDTO);
        }

        return albumDTO;
    }

    @Transactional
    public void alterarAlbum(AlbumDTO albumDto) throws IOException {
        Optional<AlbumModel> optionalAlbumModel = albumRepository.findById(albumDto.getAlbumId());

        if (optionalAlbumModel.isEmpty()) {
            throw new RuntimeException("Álbum não encontrado");
        }

        AlbumModel albumModel = optionalAlbumModel.get();

        // Atualizar campos básicos
        albumModel.setAlbumNome(albumDto.getAlbumNome());
        albumModel.setAlbumAnoLancamento(albumDto.getAlbumAnoLancamento());
        albumModel.setAlbumDuracao(albumDto.getAlbumDuracao());
        albumModel.setAlbumObservacoes(albumDto.getAlbumObservacoes());
        albumModel.setAlbumDestaque(albumDto.getAlbumDestaque());
        albumModel.setAlbumNumeroFaixas(albumDto.getAlbumNumeroFaixas());

        // Atualizar a banda usando o bandaId
        if (albumDto.getBandaId() != null) {
            BandaModel banda = bandaRepository.findById(albumDto.getBandaId())
                    .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + albumDto.getBandaId()));
            albumModel.setBanda(banda);
        }

        // Processar imagem (apenas se uma nova for enviada)
        MultipartFile imagem = albumDto.getAlbumAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "albuns");
            albumModel.setAlbumImagem(caminhoImagem);
        }

        albumRepository.save(albumModel);
    }

    private MusicaDTO converterMusicaParaDTO(MusicaModel musica) {
        MusicaDTO dto = new MusicaDTO();
        dto.setMusicaId(musica.getMusicaId());
        dto.setMusicaNome(musica.getMusicaNome());
        dto.setMusicaTom(musica.getMusicaTom());
        dto.setMusicaDuracao(musica.getMusicaDuracao());
        return dto;
    }

    @Transactional
    public void cadastrarAlbumBanda(AlbumDTO albumDto) throws IOException {
        AlbumModel album = new AlbumModel();

        album.setAlbumNome(albumDto.getAlbumNome());
        album.setAlbumAnoLancamento(albumDto.getAlbumAnoLancamento());
        album.setAlbumDuracao(albumDto.getAlbumDuracao());
        album.setAlbumObservacoes(albumDto.getAlbumObservacoes());
        album.setAlbumDestaque(albumDto.getAlbumDestaque());
        album.setAlbumNumeroFaixas(albumDto.getAlbumNumeroFaixas());
        album.setDataCadastro(LocalDateTime.now());

        // Buscar a banda usando o bandaId
        if (albumDto.getBandaId() != null) {
            BandaModel banda = bandaRepository.findById(albumDto.getBandaId())
                    .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + albumDto.getBandaId()));
            album.setBanda(banda);
        } else {
            throw new RuntimeException("Banda não informada");
        }

        // Processar imagem
        MultipartFile imagem = albumDto.getAlbumAvatar();
        if (imagem != null && !imagem.isEmpty()) {
            String caminhoImagem = salvarImagem(imagem, "albuns");
            album.setAlbumImagem(caminhoImagem);
        }

        albumRepository.save(album);
    }

    public List<AlbumModel> listarAlbunsPorBanda(Long bandaId) {
        return albumRepository.findByBandaBandaId(bandaId);
    }

}