package com.projeto.songSystem.specifications;

import com.projeto.songSystem.dto.FiltroCustomizadoDTO;
import com.projeto.songSystem.models.BandaModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BandaSpecification {

    private static final Set<String> CAMPOS_PERMITIDOS = Set.of(
            "bandaId", "bandaNome", "bandaGenero", "bandaPais", "bandaAnoFormacao", "musicas", "albuns"
    );

    private static final Set<String> CAMPOS_COLECAO = Set.of("musicas", "albuns");
    private static final Set<String> CAMPOS_NUMERICOS = Set.of("bandaId", "bandaAnoFormacao");

    public static Specification<BandaModel> comFiltros(List<FiltroCustomizadoDTO> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (FiltroCustomizadoDTO filtro : filtros) {
                if (filtro.getCampo() == null || !CAMPOS_PERMITIDOS.contains(filtro.getCampo())) {
                    continue;
                }

                Predicate predicate = CAMPOS_COLECAO.contains(filtro.getCampo())
                        ? construirPredicateColecao(filtro, root, cb)
                        : construirPredicateEscalar(filtro, root, cb);

                if (predicate != null) {
                    predicates.add(predicate);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate construirPredicateColecao(FiltroCustomizadoDTO filtro, Root<BandaModel> root, CriteriaBuilder cb) {
        Expression<Integer> tamanho = cb.size(root.get(filtro.getCampo()));

        Integer valor = tentarConverterInteiro(filtro.getValor());
        if (valor == null) return null;

        return switch (filtro.getOperador()) {
            case "eq" -> cb.equal(tamanho, valor);
            case "gt" -> cb.greaterThan(tamanho, valor);
            case "gte" -> cb.greaterThanOrEqualTo(tamanho, valor);
            case "lt" -> cb.lessThan(tamanho, valor);
            case "lte" -> cb.lessThanOrEqualTo(tamanho, valor);
            default -> null;
        };
    }

    private static Predicate construirPredicateEscalar(FiltroCustomizadoDTO filtro, Root<BandaModel> root, CriteriaBuilder cb) {
        String campo = filtro.getCampo();
        String operador = filtro.getOperador();
        String valor = filtro.getValor();

        if (valor == null) return null;

        if (CAMPOS_NUMERICOS.contains(campo)) {
            return construirPredicateNumerico(campo, operador, valor, root, cb);
        }

        String valorLower = valor.toLowerCase();
        return switch (operador) {
            case "eq" -> cb.equal(cb.lower(root.get(campo)), valorLower);
            case "contains" -> cb.like(cb.lower(root.get(campo)), "%" + valorLower + "%");
            case "startsWith" -> cb.like(cb.lower(root.get(campo)), valorLower + "%");
            case "endsWith" -> cb.like(cb.lower(root.get(campo)), "%" + valorLower);
            default -> null;
        };
    }

    private static Predicate construirPredicateNumerico(String campo, String operador, String valor, Root<BandaModel> root, CriteriaBuilder cb) {
        Number numero = campo.equals("bandaId")
                ? tentarConverterLong(valor)
                : tentarConverterInteiro(valor);

        if (numero == null) return null;

        return switch (operador) {
            case "eq" -> cb.equal(root.get(campo), numero);
            case "gt" -> cb.gt(root.get(campo), numero);
            case "gte" -> cb.ge(root.get(campo), numero);
            case "lt" -> cb.lt(root.get(campo), numero);
            case "lte" -> cb.le(root.get(campo), numero);
            default -> null;
        };
    }

    private static Integer tentarConverterInteiro(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    private static Long tentarConverterLong(String valor) {
        try {
            return Long.parseLong(valor.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}