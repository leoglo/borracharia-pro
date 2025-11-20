package com.borracharia.borracharia.dto.Request;

import java.math.BigDecimal;
import java.util.List;

import com.borracharia.borracharia.model.StatusOS;

public class OrdemServicoRequestDTO {

    private Long clienteId;
    private Long veiculoId;

    private BigDecimal valorMaoDeObra;
    private List<ItemOrdemServicoRequestDTO> itens;

    private StatusOS status = StatusOS.ABERTA;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public BigDecimal getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public void setValorMaoDeObra(BigDecimal valorMaoDeObra) {
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public List<ItemOrdemServicoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrdemServicoRequestDTO> itens) {
        this.itens = itens;
    }

    public StatusOS getStatus() {
        return status;
    }

    public void setStatus(StatusOS status) {
        this.status = status;
    }
}
