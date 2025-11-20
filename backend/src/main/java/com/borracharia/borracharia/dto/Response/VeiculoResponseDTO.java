package com.borracharia.borracharia.dto.Response;

public class VeiculoResponseDTO {

    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private Integer ano;
    private Integer km;
    private Long clienteId;
    private String clienteNome;

    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
            Integer ano, Integer km, Long clienteId, String clienteNome) {

        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.km = km;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public Integer getAno() {
        return ano;
    }

    public Integer getKm() {
        return km;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }
}
