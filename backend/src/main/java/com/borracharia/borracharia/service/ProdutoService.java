package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.ProdutoRequestDTO;
import com.borracharia.borracharia.dto.Response.ProdutoResponseDTO;
import com.borracharia.borracharia.mapper.ProdutoMapper;
import com.borracharia.borracharia.model.Produto;
import com.borracharia.borracharia.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarParaCriacao(dto);

        String codigo = normalizarString(dto.getCodigoProduto());
        repository.findByCodigoProduto(codigo).ifPresent(p -> {
            throw new IllegalArgumentException("Código de produto já cadastrado (ID: " + p.getId() + ")");
        });

        Produto produto = mapper.toEntity(dto);
        normalizarProduto(produto);
        return mapper.toResponse(repository.save(produto));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));
        return mapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        if (id == null)
            throw new IllegalArgumentException("ID não pode ser nulo");

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com ID: " + id));

        validarParaAtualizacao(dto);

        String codigoNovo = normalizarString(dto.getCodigoProduto());
        String codigoAtual = normalizarString(produto.getCodigoProduto());

        if (codigoNovo != null && !codigoNovo.equals(codigoAtual)) {
            repository.findByCodigoProduto(codigoNovo).ifPresent(p -> {
                throw new IllegalArgumentException("Código de produto já cadastrado (ID: " + p.getId() + ")");
            });
        }

        // ← USE O UPDATE PARCIAL!
        mapper.updateParcial(dto, produto);
        normalizarProduto(produto);

        return mapper.toResponse(repository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // ==================== VALIDAÇÕES SEPARADAS ====================

    private void validarParaCriacao(ProdutoRequestDTO dto) { // ← CORRIGIDO: ProdutoRequestDTO
        validarObrigatorios(dto);
        validarRegrasDeNegocio(dto);
    }

    private void validarParaAtualizacao(ProdutoRequestDTO dto) {
        validarRegrasDeNegocio(dto); // só valida o que vier
    }

    private void validarObrigatorios(ProdutoRequestDTO dto) {
        String codigo = normalizarString(dto.getCodigoProduto());
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("Código do produto é obrigatório");
        }
        String descricao = normalizarString(dto.getDescricao());
        if (descricao == null || descricao.isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória");

        }
    }

    private void validarRegrasDeNegocio(ProdutoRequestDTO dto) {
        validarValorMonetario(dto.getPrecoProduto(), "Preço do produto");
        validarValorMonetario(dto.getFrete(), "Frete");
        validarValorMonetario(dto.getCustoMedio(), "Custo médio");
        validarValorMonetario(dto.getMargemLucro(), "Margem de lucro");
        validarValorMonetario(dto.getPrecoVenda(), "Preço de venda");
        validarValorMonetario(dto.getPeso(), "Peso");

        validarPercentual(dto.getIpiPercentual(), "IPI");
        validarPercentual(dto.getIcmsPercentual(), "ICMS");
        validarPercentual(dto.getDiferencaIcmsPercentual(), "Diferença de ICMS");
        validarPercentual(dto.getReducaoIcmsPercentual(), "Redução de ICMS");
        validarPercentual(dto.getIcmsSubstituicaoPercentual(), "ICMS Substituição");
        validarPercentual(dto.getIpiPercentualFinal(), "IPI Final");
        validarPercentual(dto.getOutrosPercentual(), "Outros percentuais");

        validarQuantidade(dto.getQuantidadeEstoque(), "Quantidade em estoque");
        validarQuantidade(dto.getQuantidadePedido(), "Quantidade em pedido");
        validarQuantidade(dto.getQuantidadeAguardando(), "Quantidade aguardando");
        validarQuantidade(dto.getQuantidadeMinima(), "Quantidade mínima");
    }

    private void validarValorMonetario(BigDecimal valor, String nome) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(nome + " não pode ser negativo");
        }
    }

    private void validarPercentual(BigDecimal percentual, String nome) {
        if (percentual != null
                && (percentual.compareTo(BigDecimal.ZERO) < 0 || percentual.compareTo(new BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException(nome + " deve estar entre 0 e 100");
        }
    }

    private void validarQuantidade(Integer qtd, String nome) {
        if (qtd != null && qtd < 0) {
            throw new IllegalArgumentException(nome + " não pode ser negativa");
        }
    }

    private String normalizarString(String s) {
        return s == null ? null : s.trim().isEmpty() ? null : s.trim();
    }

    private void normalizarProduto(Produto p) {
        p.setCodigoProduto(normalizarString(p.getCodigoProduto()));
        p.setCodigoFabricante(normalizarString(p.getCodigoFabricante()));
        p.setDescricao(normalizarString(p.getDescricao()));
        p.setComplemento(normalizarString(p.getComplemento()));
        p.setMarca(normalizarString(p.getMarca()));
        p.setOutrosInfo(normalizarString(p.getOutrosInfo()));
        p.setClasse(normalizarString(p.getClasse()));
        p.setUnidade(normalizarString(p.getUnidade()));
        p.setLocalizacao(normalizarString(p.getLocalizacao()));
        p.setCodigoComissao(normalizarString(p.getCodigoComissao()));
        p.setChecarEstoque(normalizarString(p.getChecarEstoque()));
    }
}