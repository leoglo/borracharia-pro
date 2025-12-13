package com.borracharia.borracharia.dto.Request;

public record VeiculoRequestDTO(
    String placa,
    String modelo,
    String marca,
    Integer ano,
    Integer km,
    Long clienteId,
    String cor
) {}