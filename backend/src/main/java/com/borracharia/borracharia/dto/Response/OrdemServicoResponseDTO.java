package com.borracharia.borracharia.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.borracharia.borracharia.model.StatusOS;

public class OrdemServicoResponseDTO {

    private Long id;
    private String clienteNome;
    private String veiculoDescricao;

    private BigDecimal valorMaoDeObra;
    private BigDecimal valorTotal;

    private StatusOS status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    private List<ItemOrdemServicoResponseDTO> itens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getVeiculoDescricao() {
        return veiculoDescricao;
    }

    public void setVeiculoDescricao(String veiculoDescricao) {
        this.veiculoDescricao = veiculoDescricao;
    }

    public BigDecimal getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public void setValorMaoDeObra(BigDecimal valorMaoDeObra) {
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusOS getStatus() {
        return status;
    }

    public void setStatus(StatusOS status) {
        this.status = status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public List<ItemOrdemServicoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrdemServicoResponseDTO> itens) {
        this.itens = itens;
    }
}
