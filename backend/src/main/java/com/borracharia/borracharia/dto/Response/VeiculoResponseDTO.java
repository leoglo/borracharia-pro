package com.borracharia.borracharia.dto.Response;

public record VeiculoResponseDTO(
    Long id,
    String placa,
    String modelo,
    String marca,
    Integer ano,
    Integer km,
    Long clienteId,
    String clienteNome,
    String cor
) {
    public VeiculoResponseDTO {
    }
    
    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
                              Integer ano, Integer km, Long clienteId, String clienteNome) {
        this(id, placa, modelo, marca, ano, km, clienteId, clienteNome, null);
    }
    
    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
                              Integer ano, Integer km) {
        this(id, placa, modelo, marca, ano, km, null, null, null);
    }
}