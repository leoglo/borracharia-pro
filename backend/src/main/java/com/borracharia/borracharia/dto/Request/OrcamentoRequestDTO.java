package com.borracharia.borracharia.dto.Request;

import java.time.LocalDate;
import java.util.List;

public record OrcamentoRequestDTO(
    Long clienteId,
    Long veiculoId,
    List<ItemOrcamentoDTO> itens,
    LocalDate dataAbertura,
    Double desconto,
    Double total
) {
    public record ItemOrcamentoDTO(
        String codigo,
        String descricao,
        Integer quantidade,
        Double valorUnitario,
        Double total
    ) {}
}