package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.EstoqueMovimentacaoRequestDTO;
import com.borracharia.borracharia.dto.Response.EstoqueMovimentacaoResponseDTO;
import com.borracharia.borracharia.model.MovimentacaoEstoque;
import com.borracharia.borracharia.model.Produto;
import com.borracharia.borracharia.model.TipoMovimentacao;
import com.borracharia.borracharia.repository.MovimentacaoEstoqueRepository;
import com.borracharia.borracharia.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final MovimentacaoEstoqueRepository movRepo;
    private final ProdutoRepository produtoRepo;

    public EstoqueService(MovimentacaoEstoqueRepository movRepo, ProdutoRepository produtoRepo) {
        this.movRepo = movRepo;
        this.produtoRepo = produtoRepo;
    }

    @Transactional
    public EstoqueMovimentacaoResponseDTO registrarEntrada(EstoqueMovimentacaoRequestDTO dto) {
        Produto produto = produtoRepo.findById(dto.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // criar movimentação de ENTRADA
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setQuantidade(dto.getQuantidade());
        mov.setTipo(TipoMovimentacao.ENTRADA);
        mov.setMotivo(dto.getMotivo());

        // atualizar saldo do produto (adiciona)
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + dto.getQuantidade());
        produtoRepo.save(produto);

        MovimentacaoEstoque salvo = movRepo.save(mov);
        return toDTO(salvo);
    }

    @Transactional
    public EstoqueMovimentacaoResponseDTO registrarSaida(EstoqueMovimentacaoRequestDTO dto) {
        Produto produto = produtoRepo.findById(dto.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() < dto.getQuantidade()) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getDescricao());
        }

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setQuantidade(dto.getQuantidade());
        mov.setTipo(TipoMovimentacao.SAIDA);
        mov.setMotivo(dto.getMotivo());

        // reduzir estoque
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - dto.getQuantidade());
        produtoRepo.save(produto);

        MovimentacaoEstoque salvo = movRepo.save(mov);
        return toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public int obterSaldoAtual(Long produtoId) {
        Produto produto = produtoRepo.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        return produto.getQuantidadeEstoque();
    }

    @Transactional(readOnly = true)
    public List<EstoqueMovimentacaoResponseDTO> historico(Long produtoId) {
        return movRepo.findByProdutoIdOrderByDataMovimentacaoDesc(produtoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EstoqueMovimentacaoResponseDTO toDTO(MovimentacaoEstoque mov) {
        return new EstoqueMovimentacaoResponseDTO(
                mov.getId(),
                mov.getProduto() != null ? mov.getProduto().getId() : null,
                mov.getQuantidade(),
                mov.getTipo() != null ? mov.getTipo().name() : null,
                mov.getMotivo(),
                mov.getDataMovimentacao()
        );
    }
}
