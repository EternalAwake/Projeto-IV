package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.*;
import com.projeto.songSystem.models.*;
import com.projeto.songSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeoriaService {

    @Autowired
    private TeoriaCampoHarmonicoRepository campoHarmonicoRepository;

    @Autowired
    private TeoriaEscalaRepository escalaRepository;

    @Autowired
    private TeoriaAcordeRepository acordeRepository;

    // ==================== CAMPO HARMÔNICO ====================

    // Buscar campo harmônico por ID
    public TeoriaCampoHarmonicoDTO buscarCampoHarmonicoPorId(Long id) {
        TeoriaCampoHarmonicoModel campo = campoHarmonicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acorde não encontrado"));
        return converterCampoHarmonicoParaDTO(campo);
    }

    // Atualizar campo harmônico
    @Transactional
    public void atualizarCampoHarmonico(TeoriaCampoHarmonicoDTO dto) {
        TeoriaCampoHarmonicoModel campo = campoHarmonicoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Acorde não encontrado"));

        // Verificar conflito com outro registro (mesmo tom/grau mas id diferente)
        List<TeoriaCampoHarmonicoModel> conflitos = campoHarmonicoRepository.findByTomAndGrauAndIdNot(dto.getTom(), dto.getGrau(), dto.getId());
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Já existe um acorde para o " + getGrauRomano(dto.getGrau()) + " grau de " + dto.getTom());
        }

        campo.setGrau(dto.getGrau());
        campo.setAcorde(dto.getAcorde());
        campo.setTipo(dto.getTipo());
        campo.setFuncao(dto.getFuncao());
        campo.setNotas(dto.getNotas());
        campo.setObservacoes(dto.getObservacoes());
        campoHarmonicoRepository.save(campo);
    }

    // Excluir campo por tom
    @Transactional
    public void excluirCampoPorTom(String tom) {
        List<TeoriaCampoHarmonicoModel> campos = campoHarmonicoRepository.findByTomOrderByGrauAsc(tom);
        if (campos.isEmpty()) {
            throw new RuntimeException("Nenhum campo harmônico encontrado para o tom " + tom);
        }
        campoHarmonicoRepository.deleteAll(campos);
    }

    @Transactional
    public void excluirCampoHarmonico(Long id) {
        // Verificar se o acorde existe
        TeoriaCampoHarmonicoModel campo = campoHarmonicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acorde não encontrado"));

        // Excluir o acorde
        campoHarmonicoRepository.delete(campo);
    }

    // Método auxiliar para converter grau para romano
    private String getGrauRomano(Integer grau) {
        switch (grau) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            default: return String.valueOf(grau);
        }
    }

    public List<TeoriaCampoHarmonicoDTO> listarTodosCamposHarmonicos() {
        return campoHarmonicoRepository.findAllByOrderByTomAscGrauAsc()
                .stream()
                .map(this::converterCampoHarmonicoParaDTO)
                .collect(Collectors.toList());
    }

    public List<TeoriaCampoHarmonicoDTO> listarCampoHarmonicoPorTom(String tom) {
        return campoHarmonicoRepository.findByTomOrderByGrauAsc(tom)
                .stream()
                .map(this::converterCampoHarmonicoParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void salvarCampoHarmonico(TeoriaCampoHarmonicoDTO dto) {
        // Verificar se já existe um acorde para este tom e grau
        List<TeoriaCampoHarmonicoModel> existentes = campoHarmonicoRepository.findByTomAndGrau(dto.getTom(), dto.getGrau());
        if (!existentes.isEmpty()) {
            throw new RuntimeException("Já existe um acorde para o " + dto.getGrau() + "º grau de " + dto.getTom());
        }

        TeoriaCampoHarmonicoModel campo = new TeoriaCampoHarmonicoModel();
        campo.setTom(dto.getTom());
        campo.setGrau(dto.getGrau());
        campo.setAcorde(dto.getAcorde());
        campo.setTipo(dto.getTipo());
        campo.setFuncao(dto.getFuncao());
        campo.setNotas(dto.getNotas());
        campo.setObservacoes(dto.getObservacoes());
        campoHarmonicoRepository.save(campo);
    }

    public List<TeoriaCampoHarmonicoDTO> filtrarCamposHarmonicos(String tom, Integer grau, String funcao, String tipo) {
        List<TeoriaCampoHarmonicoModel> campos = campoHarmonicoRepository.findAll();
        return campos.stream()
                .filter(c -> tom == null || tom.isEmpty() || c.getTom().equals(tom))
                .filter(c -> grau == null || c.getGrau().equals(grau))
                .filter(c -> funcao == null || funcao.isEmpty() || c.getFuncao().equals(funcao))
                .filter(c -> tipo == null || tipo.isEmpty() || c.getTipo().equals(tipo))
                .map(this::converterCampoHarmonicoParaDTO)
                .collect(Collectors.toList());
    }

    public List<String> listarTonsDisponiveis() {
        return List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B");
    }

    private TeoriaCampoHarmonicoDTO converterCampoHarmonicoParaDTO(TeoriaCampoHarmonicoModel model) {
        TeoriaCampoHarmonicoDTO dto = new TeoriaCampoHarmonicoDTO();
        dto.setId(model.getId());
        dto.setTom(model.getTom());
        dto.setGrau(model.getGrau());
        dto.setAcorde(model.getAcorde());
        dto.setTipo(model.getTipo());
        dto.setFuncao(model.getFuncao());
        dto.setNotas(model.getNotas());
        return dto;
    }

    // ==================== ESCALAS ====================

    // TeoriaService.java - Adicione os métodos

    public List<TeoriaEscalaDTO> filtrarEscalas(String tipo, String tonica, String busca) {
        List<TeoriaEscalaModel> escalas = escalaRepository.findAll();
        return escalas.stream()
                .filter(e -> tipo == null || tipo.isEmpty() || e.getTipo().equals(tipo))
                .filter(e -> tonica == null || tonica.isEmpty() || (e.getTonica() != null && e.getTonica().equals(tonica)))
                .filter(e -> busca == null || busca.isEmpty() ||
                        (e.getNome() != null && e.getNome().toLowerCase().contains(busca.toLowerCase())))
                .map(this::converterEscalaParaDTO)
                .collect(Collectors.toList());
    }

    public TeoriaEscalaDTO buscarEscalaPorId(Long id) {
        TeoriaEscalaModel escala = escalaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escala não encontrada"));
        return converterEscalaParaDTO(escala);
    }

    public List<TeoriaEscalaDTO> listarTodasEscalas() {
        return escalaRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::converterEscalaParaDTO)
                .collect(Collectors.toList());
    }

    public List<TeoriaEscalaDTO> listarEscalasPorTipo(String tipo) {
        return escalaRepository.findByTipoOrderByNomeAsc(tipo)
                .stream()
                .map(this::converterEscalaParaDTO)
                .collect(Collectors.toList());
    }

    public List<String> listarTiposEscala() {
        return escalaRepository.findAll().stream()
                .map(TeoriaEscalaModel::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void salvarEscala(TeoriaEscalaDTO dto) {
        TeoriaEscalaModel escala = new TeoriaEscalaModel();
        escala.setNome(dto.getNome());
        escala.setTipo(dto.getTipo());
        escala.setTonica(dto.getTonica());
        escala.setNotas(dto.getNotas());
        escala.setFormula(dto.getFormula());
        escala.setDescricao(dto.getDescricao());
        escala.setGraus(dto.getGraus());
        escala.setAcordes(dto.getAcordes());
        escalaRepository.save(escala);
    }

    @Transactional
    public void atualizarEscala(TeoriaEscalaDTO dto) {
        TeoriaEscalaModel escala = escalaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Escala não encontrada"));
        escala.setNome(dto.getNome());
        escala.setTipo(dto.getTipo());
        escala.setTonica(dto.getTonica());
        escala.setNotas(dto.getNotas());
        escala.setFormula(dto.getFormula());
        escala.setDescricao(dto.getDescricao());
        escala.setGraus(dto.getGraus());
        escala.setAcordes(dto.getAcordes());
        escalaRepository.save(escala);
    }

    @Transactional
    public void excluirEscala(Long id) {
        escalaRepository.deleteById(id);
    }

    public List<TeoriaEscalaDTO> filtrarEscalas(String tipo, String tonica) {
        List<TeoriaEscalaModel> escalas = escalaRepository.findAll();
        return escalas.stream()
                .filter(e -> tipo == null || tipo.isEmpty() || e.getTipo().equals(tipo))
                .filter(e -> tonica == null || tonica.isEmpty() || e.getTonica().equals(tonica))
                .map(this::converterEscalaParaDTO)
                .collect(Collectors.toList());
    }

    private TeoriaEscalaDTO converterEscalaParaDTO(TeoriaEscalaModel model) {
        TeoriaEscalaDTO dto = new TeoriaEscalaDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setTipo(model.getTipo());
        dto.setTonica(model.getTonica());
        dto.setNotas(model.getNotas());
        dto.setFormula(model.getFormula());
        dto.setDescricao(model.getDescricao());
        dto.setGraus(model.getGraus());
        dto.setAcordes(model.getAcordes());
        return dto;
    }

    // ==================== ACORDES ====================

    public List<TeoriaAcordeDTO> listarTodosAcordes() {
        return acordeRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::converterAcordeParaDTO)
                .collect(Collectors.toList());
    }

    public List<TeoriaAcordeDTO> listarAcordesPorTipo(String tipo) {
        return acordeRepository.findByTipoOrderByNomeAsc(tipo)
                .stream()
                .map(this::converterAcordeParaDTO)
                .collect(Collectors.toList());
    }

    public List<String> listarTiposAcorde() {
        return acordeRepository.findAll().stream()
                .map(TeoriaAcordeModel::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void salvarAcorde(TeoriaAcordeDTO dto) {
        TeoriaAcordeModel acorde = new TeoriaAcordeModel();
        acorde.setNome(dto.getNome());
        acorde.setTipo(dto.getTipo());
        acorde.setTonica(dto.getTonica());
        acorde.setTerça(dto.getTerça());
        acorde.setQuinta(dto.getQuinta());
        acorde.setSetima(dto.getSetima());
        acorde.setNotas(dto.getNotas());
        acorde.setCifra(dto.getCifra());
        acorde.setDescricao(dto.getDescricao());
        acordeRepository.save(acorde);
    }

    @Transactional
    public void atualizarAcorde(TeoriaAcordeDTO dto) {
        TeoriaAcordeModel acorde = acordeRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Acorde não encontrado"));
        acorde.setNome(dto.getNome());
        acorde.setTipo(dto.getTipo());
        acorde.setTonica(dto.getTonica());
        acorde.setTerça(dto.getTerça());
        acorde.setQuinta(dto.getQuinta());
        acorde.setSetima(dto.getSetima());
        acorde.setNotas(dto.getNotas());
        acorde.setCifra(dto.getCifra());
        acorde.setDescricao(dto.getDescricao());
        acordeRepository.save(acorde);
    }

    @Transactional
    public void excluirAcorde(Long id) {
        acordeRepository.deleteById(id);
    }

    public List<TeoriaAcordeDTO> filtrarAcordes(String tipo) {
        List<TeoriaAcordeModel> acordes = acordeRepository.findAll();
        return acordes.stream()
                .filter(a -> tipo == null || tipo.isEmpty() || a.getTipo().equals(tipo))
                .map(this::converterAcordeParaDTO)
                .collect(Collectors.toList());
    }

    private TeoriaAcordeDTO converterAcordeParaDTO(TeoriaAcordeModel model) {
        TeoriaAcordeDTO dto = new TeoriaAcordeDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setTipo(model.getTipo());
        dto.setTonica(model.getTonica());
        dto.setTerça(model.getTerça());
        dto.setQuinta(model.getQuinta());
        dto.setSetima(model.getSetima());
        dto.setNotas(model.getNotas());
        dto.setCifra(model.getCifra());
        dto.setDescricao(model.getDescricao());
        return dto;
    }

    // TeoriaService.java - Adicione os métodos

    public List<TeoriaAcordeDTO> filtrarAcordes(String tipo, String tonica, String busca) {
        List<TeoriaAcordeModel> acordes = acordeRepository.findAll();
        return acordes.stream()
                .filter(a -> tipo == null || tipo.isEmpty() || a.getTipo().equals(tipo))
                .filter(a -> tonica == null || tonica.isEmpty() || (a.getTonica() != null && a.getTonica().equals(tonica)))
                .filter(a -> busca == null || busca.isEmpty() ||
                        (a.getNome() != null && a.getNome().toLowerCase().contains(busca.toLowerCase())) ||
                        (a.getNotas() != null && a.getNotas().toLowerCase().contains(busca.toLowerCase())))
                .map(this::converterAcordeParaDTO)
                .collect(Collectors.toList());
    }

    public TeoriaAcordeDTO buscarAcordePorId(Long id) {
        TeoriaAcordeModel acorde = acordeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acorde não encontrado com ID: " + id));
        return converterAcordeParaDTO(acorde);
    }

}