package com.borracharia.borracharia.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EstoqueMovimentacaoRequestDTO {

    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade mínima é 1")
    private Integer quantidade;

    // Opcional: motivo da movimentação (COMPRA, USO_OS, CANCELAMENTO_OS...)
    private String motivo;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
