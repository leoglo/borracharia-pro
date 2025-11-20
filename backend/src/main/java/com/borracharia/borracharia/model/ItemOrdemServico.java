package com.borracharia.borracharia.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_ordem_servico")
public class ItemOrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produto vinculado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Ordem de serviço à qual pertence
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    // Quantidade de produtos usados
    @Column(nullable = false)
    private Integer quantidade;

    // Preço unitário de venda (não do estoque, pode variar)
    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    // Subtotal = quantidade * precoUnitario (persistido para histórico)
    @Column(name = "valor_total", precision = 14, scale = 2)
    private BigDecimal valorTotal;

    // --- Callbacks para garantir valorTotal atualizado ---
    @PrePersist
    @PreUpdate
    private void calcularValorTotal() {
        if (precoUnitario == null || quantidade == null) {
            this.valorTotal = BigDecimal.ZERO;
            return;
        }
        this.valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        // mantém valorTotal consistente caso setado fora do ciclo JPA
        calcularValorTotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        // recalcula para manter coesão em memória antes do persist
        calcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
