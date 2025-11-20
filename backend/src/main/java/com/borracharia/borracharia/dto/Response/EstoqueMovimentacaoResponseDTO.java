package com.borracharia.borracharia.dto.Response;

import java.time.LocalDateTime;

public class EstoqueMovimentacaoResponseDTO {

    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private String tipo; // "ENTRADA" ou "SAIDA"
    private String motivo;
    private LocalDateTime dataMovimentacao;

    public EstoqueMovimentacaoResponseDTO(Long id, Long produtoId, Integer quantidade,
            String tipo, String motivo, LocalDateTime dataMovimentacao) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.motivo = motivo;
        this.dataMovimentacao = dataMovimentacao;
    }

    public Long getId() {
        return id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
}
