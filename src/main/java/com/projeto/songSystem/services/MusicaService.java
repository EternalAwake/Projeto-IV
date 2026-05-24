package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.repositories.AlbumRepository;
import com.projeto.songSystem.repositories.BandaRepository;
import com.projeto.songSystem.repositories.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private BandaRepository bandaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public long obterQtdMusicas() {
        return musicaRepository.count();
    }

    public List<MusicaModel> obterMusicasEmDestque() {
        return musicaRepository.findByMusicaDestaqueTrue();
    }

    public List<MusicaModel> listarMusicas() {
        return musicaRepository.findAll();
    }

    @Transactional
    public void cadastrarMusica(MusicaDTO musicaDTO) {
        MusicaModel musica = new MusicaModel();

        // Dados básicos
        musica.setMusicaNome(musicaDTO.getMusicaNome());
        musica.setMusicaTom(musicaDTO.getMusicaTom());
        musica.setMusicaDuracao(musicaDTO.getMusicaDuracao());
        musica.setMusicaAnotacoes(musicaDTO.getMusicaAnotacoes());
        musica.setMusicaAfinacao(musicaDTO.getMusicaAfinacao());

        // Campo destaque
        musica.setMusicaDestaque(musicaDTO.getMusicaDestaque() != null && musicaDTO.getMusicaDestaque());
        musica.setDataCadastro(LocalDateTime.now());

        Long bandaId = musicaDTO.getBandaId();
        Long albumId = musicaDTO.getAlbumId();

        // Caso 1: Usuário selecionou um álbum (com ou sem banda)
        if (albumId != null && albumId > 0) {
            AlbumModel album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

            musica.setAlbum(album);

            // Se o usuário não selecionou banda, usa a banda do álbum
            if (bandaId == null || bandaId == 0) {
                musica.setBanda(album.getBanda());
                System.out.println("Álbum selecionado, banda associada automaticamente: " + album.getBanda().getBandaNome());
            } else {
                // Usuário selecionou banda e álbum - verifica se pertencem
                BandaModel banda = bandaRepository.findById(bandaId)
                        .orElseThrow(() -> new RuntimeException("Banda não encontrada"));

                if (!album.getBanda().getBandaId().equals(bandaId)) {
                    throw new RuntimeException("O álbum selecionado não pertence à banda escolhida");
                }
                musica.setBanda(banda);
            }
        }
        // Caso 2: Apenas banda selecionada (sem álbum)
        else if (bandaId != null && bandaId > 0) {
            BandaModel banda = bandaRepository.findById(bandaId)
                    .orElseThrow(() -> new RuntimeException("Banda não encontrada"));
            musica.setBanda(banda);
            musica.setAlbum(null);
        }
        // Caso 3: Nada selecionado (música avulsa)
        else {
            musica.setBanda(null);
            musica.setAlbum(null);
        }

        musicaRepository.save(musica);
    }

    // Método para buscar músicas em destaque
    public List<MusicaModel> obterMusicasEmDestaque() {
        return musicaRepository.findByMusicaDestaqueTrue();
    }

    public MusicaDTO obterMusicaComDadosCompletos(Long musicaId) {
        Optional<MusicaModel> optionalMusicaModel = musicaRepository.findByIdWithBandaAndAlbum(musicaId);

        if (optionalMusicaModel.isEmpty()) {
            throw new RuntimeException("Música não encontrada com ID: " + musicaId);
        }

        MusicaModel musica = optionalMusicaModel.get();
        MusicaDTO musicaDTO = new MusicaDTO();

        // Dados básicos
        musicaDTO.setMusicaId(musica.getMusicaId());
        musicaDTO.setMusicaNome(musica.getMusicaNome());
        musicaDTO.setMusicaTom(musica.getMusicaTom());
        musicaDTO.setMusicaDuracao(musica.getMusicaDuracao());
        musicaDTO.setMusicaAnotacoes(musica.getMusicaAnotacoes());
        musicaDTO.setMusicaAfinacao(musica.getMusicaAfinacao());
        musicaDTO.setMusicaDestaque(musica.getMusicaDestaque());

        // Objeto Banda completo
        if (musica.getBanda() != null) {
            musicaDTO.setBandaId(musica.getBanda().getBandaId());

            BandaDTO bandaDTO = new BandaDTO();
            bandaDTO.setBandaId(musica.getBanda().getBandaId());
            bandaDTO.setBandaNome(musica.getBanda().getBandaNome());
            musicaDTO.setBandaDTO(bandaDTO);
        }

        // Objeto Álbum completo
        if (musica.getAlbum() != null) {
            musicaDTO.setAlbumId(musica.getAlbum().getAlbumId());

            AlbumDTO albumDTO = new AlbumDTO();
            albumDTO.setAlbumId(musica.getAlbum().getAlbumId());
            albumDTO.setAlbumNome(musica.getAlbum().getAlbumNome());
            albumDTO.setAlbumAnoLancamento(musica.getAlbum().getAlbumAnoLancamento());
            albumDTO.setAlbumDuracao(musica.getAlbum().getAlbumDuracao());
            albumDTO.setAlbumImagem(musica.getAlbum().getAlbumImagem());  // Imagem do álbum
            musicaDTO.setAlbumDTO(albumDTO);
        }

        return musicaDTO;
    }

    @Transactional
    public void alterarMusica(MusicaDTO musicaDTO) {
        // Buscar a música existente
        MusicaModel musica = musicaRepository.findById(musicaDTO.getMusicaId())
                .orElseThrow(() -> new RuntimeException("Música não encontrada com ID: " + musicaDTO.getMusicaId()));

        // Atualizar campos básicos
        musica.setMusicaNome(musicaDTO.getMusicaNome());
        musica.setMusicaTom(musicaDTO.getMusicaTom());
        musica.setMusicaDuracao(musicaDTO.getMusicaDuracao());
        musica.setMusicaAnotacoes(musicaDTO.getMusicaAnotacoes());
        musica.setMusicaAfinacao(musicaDTO.getMusicaAfinacao());
        musica.setMusicaDestaque(musicaDTO.getMusicaDestaque() != null && musicaDTO.getMusicaDestaque());
        musica.setDataAtualizacao(LocalDateTime.now());

        // Atualizar banda
        if (musicaDTO.getBandaId() != null && musicaDTO.getBandaId() > 0) {
            BandaModel banda = bandaRepository.findById(musicaDTO.getBandaId())
                    .orElseThrow(() -> new RuntimeException("Banda não encontrada com ID: " + musicaDTO.getBandaId()));
            musica.setBanda(banda);
        } else {
            musica.setBanda(null);
        }

        // Atualizar álbum
        if (musicaDTO.getAlbumId() != null && musicaDTO.getAlbumId() > 0) {
            AlbumModel album = albumRepository.findById(musicaDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + musicaDTO.getAlbumId()));
            musica.setAlbum(album);
        } else {
            musica.setAlbum(null);
        }

        // Salvar alterações
        musicaRepository.save(musica);

        System.out.println("Música alterada com sucesso! ID: " + musica.getMusicaId());
    }

    @Transactional
    public boolean excluirMusica(Long id) {
        musicaRepository.deleteById(id);
        return true;
    }

}
