package com.borracharia.borracharia.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class VeiculoRequestDTO {

    // Para CREATE: obrigatório
    // Para UPDATE: opcional (pode passar só o que quer mudar)
    @Size(min = 7, max = 8, message = "Placa inválida")
    private String placa;

    @Size(min = 2, max = 50, message = "Modelo deve ter entre 2 e 50 caracteres")
    private String modelo;

    @Size(max = 30, message = "Marca deve ter no máximo 30 caracteres")
    private String marca;

    @Min(value = 1900, message = "Ano mínimo é 1900")
    @Max(value = 2026, message = "Ano máximo é 2026")
    private Integer ano;

    @Min(value = 0, message = "KM não pode ser negativo")
    private Integer km;

    private Long clienteId;

    // ========== GETTERS E SETTERS ==========

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}