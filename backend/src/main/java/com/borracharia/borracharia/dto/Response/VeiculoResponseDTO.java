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
    private String cor; 

    // Construtor completo com cor
    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
                              Integer ano, Integer km, Long clienteId, String clienteNome, String cor) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.km = km;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.cor = cor;
    }

    // Construtor antigo (mantido por compatibilidade)
    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
                              Integer ano, Integer km, Long clienteId, String clienteNome) {
        this(id, placa, modelo, marca, ano, km, clienteId, clienteNome, null);
    }

    // Construtor sem cliente
    public VeiculoResponseDTO(Long id, String placa, String modelo, String marca,
                              Integer ano, Integer km) {
        this(id, placa, modelo, marca, ano, km, null, null, null);
    }

    // Getters e Setters (incluindo cor)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Integer getKm() { return km; }
    public void setKm(Integer km) { this.km = km; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    // Getter e Setter da cor
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
}