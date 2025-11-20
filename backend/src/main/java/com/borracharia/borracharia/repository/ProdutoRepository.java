package com.borracharia.borracharia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.borracharia.borracharia.model.Produto;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByDescricaoContainingIgnoreCase(String descricao);

    Optional<Produto> findByCodigoProduto(String codigoProduto);

    List<Produto> findByCodigoFabricanteContainingIgnoreCase(String codigoFabricante);

    List<Produto> findByMarcaIgnoreCase(String marca);

    List<Produto> findByQuantidadeEstoqueLessThan(int quantidade);

    List<Produto> findByQuantidadeEstoqueLessThanEqual(int quantidade);

    List<Produto> findByClasseIgnoreCase(String classe);

    List<Produto> findByPrecoVendaBetween(BigDecimal precoMin, BigDecimal precoMax);

    List<Produto> findByLocalizacaoContainingIgnoreCase(String localizacao);

    List<Produto> findByMargemLucroLessThan(BigDecimal margem);

    List<Produto> findByQuantidadeAguardandoGreaterThan(int quantidade);
}
