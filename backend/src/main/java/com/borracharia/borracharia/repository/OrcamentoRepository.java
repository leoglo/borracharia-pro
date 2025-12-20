package com.borracharia.borracharia.repository;

import com.borracharia.borracharia.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findByClienteId(Long clienteId);

    List<Orcamento> findByStatus(String status);
    
    @Query("SELECT o FROM Orcamento o WHERE " +
           "LOWER(o.cliente.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "o.cliente.cpf LIKE CONCAT('%', :termo, '%') " +
           "ORDER BY o.dataAbertura DESC")
    List<Orcamento> buscarPorClienteNomeOuCpf(@Param("termo") String termo);
}