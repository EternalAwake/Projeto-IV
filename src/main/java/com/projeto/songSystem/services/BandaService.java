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
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BandaService {

    @Autowired
    private BandaRepository bandaRepository;

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private UploadStorageService uploadStorageService;

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
            uploadStorageService.registrarNovoArquivo(caminhoImagem);
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

    @Transactional(readOnly = true)
    public List<BandaModel> listarBandas() {
        /*
         * Álbum e música são duas coleções List. O Hibernate não permite fazer
         * fetch das duas na mesma consulta (MultipleBagFetchException).
         * Executando uma consulta para cada coleção dentro da mesma transação,
         * as mesmas entidades gerenciadas ficam com ambas inicializadas.
         */
        List<BandaModel> bandas = bandaRepository.findAllWithAlbuns();
        bandaRepository.findAllWithMusicas();
        return bandas;
    }

    public List<BandaModel> listarBandasBasicas() {
        return bandaRepository.findAllByOrderByBandaNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<BandaModel> obterBandasEmDestaque() {
        List<BandaModel> bandas = bandaRepository.findDestaquesWithAlbuns();
        bandaRepository.findDestaquesWithMusicas();
        return bandas;
    }

    @Transactional(readOnly = true)
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
            String imagemAntiga = banda.getBandaImagem();
            String caminhoImagem = salvarImagem(imagem, "bandas");
            uploadStorageService.registrarSubstituicao(imagemAntiga, caminhoImagem);
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

        // Guarda todas as imagens que serão órfãs. A exclusão física só ocorre
        // depois que a transação do banco for confirmada.
        Set<String> imagensParaExcluir = new LinkedHashSet<>();
        imagensParaExcluir.add(banda.getBandaImagem());

        // Músicas ligadas diretamente à banda
        if (banda.getMusicas() != null) {
            for (MusicaModel m : banda.getMusicas()) {
                musicaService.removerDependenciasDeMusica(m.getMusicaId());
            }
        }
        // Músicas e capas dos álbuns ligados à banda
        if (banda.getAlbuns() != null) {
            for (AlbumModel album : banda.getAlbuns()) {
                imagensParaExcluir.add(album.getAlbumImagem());
                if (album.getMusicas() != null) {
                    for (MusicaModel m : album.getMusicas()) {
                        musicaService.removerDependenciasDeMusica(m.getMusicaId());
                    }
                }
            }
        }

        bandaRepository.delete(banda);
        uploadStorageService.agendarExclusaoAposCommit(imagensParaExcluir);
        return true;
    }

    public long obterQtdBandas() {
        return bandaRepository.count();
    }

    public BandaModel obterBandaModelPorId(Long bandaId) {
        return bandaRepository.findById(bandaId)
                .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + bandaId));
    }

}
