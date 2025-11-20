package com.borracharia.borracharia.repository;

import com.borracharia.borracharia.model.OrdemServico;
import com.borracharia.borracharia.model.StatusOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    
    List<OrdemServico> findByStatus(StatusOS status);
    
    List<OrdemServico> findByClienteId(Long clienteId);
    
    List<OrdemServico> findByVeiculoPlaca(String placa);
}