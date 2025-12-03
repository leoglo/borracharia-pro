package com.borracharia.borracharia.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8)
    private String placa;

    @Column(length = 50)
    private String modelo;

    @Column(length = 30)
    private String marca;

    @Min(value = 1900, message = "Ano mínimo é 1900")
    @Max(value = 2100, message = "Ano máximo é 2100")
    private Integer ano;

    @Column(length = 30)
    private String cor;
 
    private int km;

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    @Column(length = 500)
    private String observacoes;

    // ✅ MUDANÇA AQUI: JsonBackReference ao invés de JsonIgnoreProperties
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    // Getters e Setters (mantenha todos)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Construtores
    public Veiculo() {
    }

    public Veiculo(String placa, String modelo, String marca, Integer ano, 
                   String cor, String observacoes,Integer km, Cliente cliente) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.cor = cor;
        this.observacoes = observacoes;
        this.km = km;
        this.cliente = cliente;
    }
}