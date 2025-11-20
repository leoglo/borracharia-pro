package com.borracharia.borracharia.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProdutoResponseDTO {

    private Long id;
    private String codigoProduto;
    private String codigoFabricante;
    private String descricao;
    private String complemento;
    private String marca;
    private String outrosInfo;
    private String classe;
    private BigDecimal embalagem;
    private String unidade;
    private BigDecimal quantidadeUnidade;
    private String localizacao;
    private BigDecimal precoProduto;
    private BigDecimal frete;
    private BigDecimal custoMedio;
    private LocalDate ultimoReajuste;
    private BigDecimal margemLucro;
    private BigDecimal precoVenda;
    private Integer quantidadeEstoque;
    private Integer quantidadePedido;
    private Integer quantidadeAguardando;
    private Integer quantidadeMinima;
    private String codigoComissao;
    private BigDecimal peso;
    private BigDecimal ipiPercentual;
    private BigDecimal diferencaIcmsPercentual;
    private BigDecimal outrosPercentual;
    private BigDecimal icmsPercentual;
    private BigDecimal reducaoIcmsPercentual;
    private BigDecimal icmsSubstituicaoPercentual;
    private BigDecimal ipiPercentualFinal;
    private String checarEstoque;

    // Construtor vazio
    public ProdutoResponseDTO() {
    }

    // Construtor completo
    public ProdutoResponseDTO(Long id, String codigoProduto, String codigoFabricante, String descricao,
                              String complemento, String marca, String outrosInfo, String classe,
                              BigDecimal embalagem, String unidade, BigDecimal quantidadeUnidade,
                              String localizacao, BigDecimal precoProduto, BigDecimal frete,
                              BigDecimal custoMedio, LocalDate ultimoReajuste, BigDecimal margemLucro,
                              BigDecimal precoVenda, Integer quantidadeEstoque, Integer quantidadePedido,
                              Integer quantidadeAguardando, Integer quantidadeMinima, String codigoComissao,
                              BigDecimal peso, BigDecimal ipiPercentual, BigDecimal diferencaIcmsPercentual,
                              BigDecimal outrosPercentual, BigDecimal icmsPercentual,
                              BigDecimal reducaoIcmsPercentual, BigDecimal icmsSubstituicaoPercentual,
                              BigDecimal ipiPercentualFinal, String checarEstoque) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.codigoFabricante = codigoFabricante;
        this.descricao = descricao;
        this.complemento = complemento;
        this.marca = marca;
        this.outrosInfo = outrosInfo;
        this.classe = classe;
        this.embalagem = embalagem;
        this.unidade = unidade;
        this.quantidadeUnidade = quantidadeUnidade;
        this.localizacao = localizacao;
        this.precoProduto = precoProduto;
        this.frete = frete;
        this.custoMedio = custoMedio;
        this.ultimoReajuste = ultimoReajuste;
        this.margemLucro = margemLucro;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadePedido = quantidadePedido;
        this.quantidadeAguardando = quantidadeAguardando;
        this.quantidadeMinima = quantidadeMinima;
        this.codigoComissao = codigoComissao;
        this.peso = peso;
        this.ipiPercentual = ipiPercentual;
        this.diferencaIcmsPercentual = diferencaIcmsPercentual;
        this.outrosPercentual = outrosPercentual;
        this.icmsPercentual = icmsPercentual;
        this.reducaoIcmsPercentual = reducaoIcmsPercentual;
        this.icmsSubstituicaoPercentual = icmsSubstituicaoPercentual;
        this.ipiPercentualFinal = ipiPercentualFinal;
        this.checarEstoque = checarEstoque;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getCodigoFabricante() {
        return codigoFabricante;
    }

    public void setCodigoFabricante(String codigoFabricante) {
        this.codigoFabricante = codigoFabricante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getOutrosInfo() {
        return outrosInfo;
    }

    public void setOutrosInfo(String outrosInfo) {
        this.outrosInfo = outrosInfo;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public BigDecimal getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(BigDecimal embalagem) {
        this.embalagem = embalagem;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getQuantidadeUnidade() {
        return quantidadeUnidade;
    }

    public void setQuantidadeUnidade(BigDecimal quantidadeUnidade) {
        this.quantidadeUnidade = quantidadeUnidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public BigDecimal getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(BigDecimal precoProduto) {
        this.precoProduto = precoProduto;
    }

    public BigDecimal getFrete() {
        return frete;
    }

    public void setFrete(BigDecimal frete) {
        this.frete = frete;
    }

    public BigDecimal getCustoMedio() {
        return custoMedio;
    }

    public void setCustoMedio(BigDecimal custoMedio) {
        this.custoMedio = custoMedio;
    }

    public LocalDate getUltimoReajuste() {
        return ultimoReajuste;
    }

    public void setUltimoReajuste(LocalDate ultimoReajuste) {
        this.ultimoReajuste = ultimoReajuste;
    }

    public BigDecimal getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(BigDecimal margemLucro) {
        this.margemLucro = margemLucro;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getQuantidadePedido() {
        return quantidadePedido;
    }

    public void setQuantidadePedido(Integer quantidadePedido) {
        this.quantidadePedido = quantidadePedido;
    }

    public Integer getQuantidadeAguardando() {
        return quantidadeAguardando;
    }

    public void setQuantidadeAguardando(Integer quantidadeAguardando) {
        this.quantidadeAguardando = quantidadeAguardando;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public String getCodigoComissao() {
        return codigoComissao;
    }

    public void setCodigoComissao(String codigoComissao) {
        this.codigoComissao = codigoComissao;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getIpiPercentual() {
        return ipiPercentual;
    }

    public void setIpiPercentual(BigDecimal ipiPercentual) {
        this.ipiPercentual = ipiPercentual;
    }

    public BigDecimal getDiferencaIcmsPercentual() {
        return diferencaIcmsPercentual;
    }

    public void setDiferencaIcmsPercentual(BigDecimal diferencaIcmsPercentual) {
        this.diferencaIcmsPercentual = diferencaIcmsPercentual;
    }

    public BigDecimal getOutrosPercentual() {
        return outrosPercentual;
    }

    public void setOutrosPercentual(BigDecimal outrosPercentual) {
        this.outrosPercentual = outrosPercentual;
    }

    public BigDecimal getIcmsPercentual() {
        return icmsPercentual;
    }

    public void setIcmsPercentual(BigDecimal icmsPercentual) {
        this.icmsPercentual = icmsPercentual;
    }

    public BigDecimal getReducaoIcmsPercentual() {
        return reducaoIcmsPercentual;
    }

    public void setReducaoIcmsPercentual(BigDecimal reducaoIcmsPercentual) {
        this.reducaoIcmsPercentual = reducaoIcmsPercentual;
    }

    public BigDecimal getIcmsSubstituicaoPercentual() {
        return icmsSubstituicaoPercentual;
    }

    public void setIcmsSubstituicaoPercentual(BigDecimal icmsSubstituicaoPercentual) {
        this.icmsSubstituicaoPercentual = icmsSubstituicaoPercentual;
    }

    public BigDecimal getIpiPercentualFinal() {
        return ipiPercentualFinal;
    }

    public void setIpiPercentualFinal(BigDecimal ipiPercentualFinal) {
        this.ipiPercentualFinal = ipiPercentualFinal;
    }

    public String getChecarEstoque() {
        return checarEstoque;
    }

    public void setChecarEstoque(String checarEstoque) {
        this.checarEstoque = checarEstoque;
    }
}