package com.borracharia.borracharia.dto.Response;

import com.borracharia.borracharia.dto.Response.ClienteResponseDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoResponseDTO(
    Long id,
    String numeroOrcamento,
    ClienteResponseDTO cliente,
    VeiculoResponseDTO veiculo,
    List<ItemDTO> itens,
    LocalDate dataAbertura,
    Double desconto,
    Double total,
    String status
) {
    public record ItemDTO(
        Long id,
        String codigo,
        String descricao,
        Integer quantidade,
        Double valorUnitario,
        Double total
    ) {}
}