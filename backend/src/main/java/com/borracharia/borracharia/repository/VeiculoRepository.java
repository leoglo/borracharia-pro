package com.borracharia.borracharia.repository;

import com.borracharia.borracharia.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByPlaca(String placa);
    List<Veiculo> findByClienteId(Long clienteId);
    void deleteByClienteId(Long clienteId);
    
}
