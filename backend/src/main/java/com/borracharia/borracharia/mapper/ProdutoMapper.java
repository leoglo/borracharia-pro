package com.borracharia.borracharia.mapper;

import org.springframework.stereotype.Component;

import com.borracharia.borracharia.dto.Request.ProdutoRequestDTO;
import com.borracharia.borracharia.dto.Response.ProdutoResponseDTO;
import com.borracharia.borracharia.model.Produto;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();

        produto.setCodigoProduto(dto.getCodigoProduto());
        produto.setCodigoFabricante(dto.getCodigoFabricante());
        produto.setDescricao(dto.getDescricao());
        produto.setComplemento(dto.getComplemento());
        produto.setMarca(dto.getMarca());
        produto.setOutrosInfo(dto.getOutrosInfo());
        produto.setClasse(dto.getClasse());
        produto.setEmbalagem(dto.getEmbalagem());
        produto.setUnidade(dto.getUnidade());
        produto.setQuantidadeUnidade(dto.getQuantidadeUnidade());
        produto.setLocalizacao(dto.getLocalizacao());
        produto.setPrecoProduto(dto.getPrecoProduto());
        produto.setFrete(dto.getFrete());
        produto.setCustoMedio(dto.getCustoMedio());
        produto.setUltimoReajuste(dto.getUltimoReajuste());
        produto.setMargemLucro(dto.getMargemLucro());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setQuantidadePedido(dto.getQuantidadePedido());
        produto.setQuantidadeAguardando(dto.getQuantidadeAguardando());
        produto.setQuantidadeMinima(dto.getQuantidadeMinima());
        produto.setCodigoComissao(dto.getCodigoComissao());
        produto.setPeso(dto.getPeso());
        produto.setIpiPercentual(dto.getIpiPercentual());
        produto.setDiferencaIcmsPercentual(dto.getDiferencaIcmsPercentual());
        produto.setOutrosPercentual(dto.getOutrosPercentual());
        produto.setIcmsPercentual(dto.getIcmsPercentual());
        produto.setReducaoIcmsPercentual(dto.getReducaoIcmsPercentual());
        produto.setIcmsSubstituicaoPercentual(dto.getIcmsSubstituicaoPercentual());
        produto.setIpiPercentualFinal(dto.getIpiPercentualFinal());
        produto.setChecarEstoque(dto.getChecarEstoque());

        return produto;
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();

        dto.setId(produto.getId());
        dto.setCodigoProduto(produto.getCodigoProduto());
        dto.setCodigoFabricante(produto.getCodigoFabricante());
        dto.setDescricao(produto.getDescricao());
        dto.setComplemento(produto.getComplemento());
        dto.setMarca(produto.getMarca());
        dto.setOutrosInfo(produto.getOutrosInfo());
        dto.setClasse(produto.getClasse());
        dto.setEmbalagem(produto.getEmbalagem());
        dto.setUnidade(produto.getUnidade());
        dto.setQuantidadeUnidade(produto.getQuantidadeUnidade());
        dto.setLocalizacao(produto.getLocalizacao());
        dto.setPrecoProduto(produto.getPrecoProduto());
        dto.setFrete(produto.getFrete());
        dto.setCustoMedio(produto.getCustoMedio());
        dto.setUltimoReajuste(produto.getUltimoReajuste());
        dto.setMargemLucro(produto.getMargemLucro());
        dto.setPrecoVenda(produto.getPrecoVenda());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        dto.setQuantidadePedido(produto.getQuantidadePedido());
        dto.setQuantidadeAguardando(produto.getQuantidadeAguardando());
        dto.setQuantidadeMinima(produto.getQuantidadeMinima());
        dto.setCodigoComissao(produto.getCodigoComissao());
        dto.setPeso(produto.getPeso());
        dto.setIpiPercentual(produto.getIpiPercentual());
        dto.setDiferencaIcmsPercentual(produto.getDiferencaIcmsPercentual());
        dto.setOutrosPercentual(produto.getOutrosPercentual());
        dto.setIcmsPercentual(produto.getIcmsPercentual());
        dto.setReducaoIcmsPercentual(produto.getReducaoIcmsPercentual());
        dto.setIcmsSubstituicaoPercentual(produto.getIcmsSubstituicaoPercentual());
        dto.setIpiPercentualFinal(produto.getIpiPercentualFinal());
        dto.setChecarEstoque(produto.getChecarEstoque());

        return dto;
    }

    public void updateEntityFromDto(ProdutoRequestDTO dto, Produto produto) {
        produto.setCodigoProduto(dto.getCodigoProduto());
        produto.setCodigoFabricante(dto.getCodigoFabricante());
        produto.setDescricao(dto.getDescricao());
        produto.setComplemento(dto.getComplemento());
        produto.setMarca(dto.getMarca());
        produto.setOutrosInfo(dto.getOutrosInfo());
        produto.setClasse(dto.getClasse());
        produto.setEmbalagem(dto.getEmbalagem());
        produto.setUnidade(dto.getUnidade());
        produto.setQuantidadeUnidade(dto.getQuantidadeUnidade());
        produto.setLocalizacao(dto.getLocalizacao());
        produto.setPrecoProduto(dto.getPrecoProduto());
        produto.setFrete(dto.getFrete());
        produto.setCustoMedio(dto.getCustoMedio());
        produto.setUltimoReajuste(dto.getUltimoReajuste());
        produto.setMargemLucro(dto.getMargemLucro());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setQuantidadePedido(dto.getQuantidadePedido());
        produto.setQuantidadeAguardando(dto.getQuantidadeAguardando());
        produto.setQuantidadeMinima(dto.getQuantidadeMinima());
        produto.setCodigoComissao(dto.getCodigoComissao());
        produto.setPeso(dto.getPeso());
        produto.setIpiPercentual(dto.getIpiPercentual());
        produto.setDiferencaIcmsPercentual(dto.getDiferencaIcmsPercentual());
        produto.setOutrosPercentual(dto.getOutrosPercentual());
        produto.setIcmsPercentual(dto.getIcmsPercentual());
        produto.setReducaoIcmsPercentual(dto.getReducaoIcmsPercentual());
        produto.setIcmsSubstituicaoPercentual(dto.getIcmsSubstituicaoPercentual());
        produto.setIpiPercentualFinal(dto.getIpiPercentualFinal());
        produto.setChecarEstoque(dto.getChecarEstoque());
    }

    public void updateParcial(ProdutoRequestDTO dto, Produto produto) {
        if (dto.getCodigoProduto() != null)
            produto.setCodigoProduto(dto.getCodigoProduto());
        if (dto.getCodigoFabricante() != null)
            produto.setCodigoFabricante(dto.getCodigoFabricante());
        if (dto.getDescricao() != null)
            produto.setDescricao(dto.getDescricao());
        if (dto.getComplemento() != null)
            produto.setComplemento(dto.getComplemento());
        if (dto.getMarca() != null)
            produto.setMarca(dto.getMarca());
        if (dto.getOutrosInfo() != null)
            produto.setOutrosInfo(dto.getOutrosInfo());
        if (dto.getClasse() != null)
            produto.setClasse(dto.getClasse());
        if (dto.getEmbalagem() != null)
            produto.setEmbalagem(dto.getEmbalagem());
        if (dto.getUnidade() != null)
            produto.setUnidade(dto.getUnidade());
        if (dto.getQuantidadeUnidade() != null)
            produto.setQuantidadeUnidade(dto.getQuantidadeUnidade());
        if (dto.getLocalizacao() != null)
            produto.setLocalizacao(dto.getLocalizacao());
        if (dto.getPrecoProduto() != null)
            produto.setPrecoProduto(dto.getPrecoProduto());
        if (dto.getFrete() != null)
            produto.setFrete(dto.getFrete());
        if (dto.getCustoMedio() != null)
            produto.setCustoMedio(dto.getCustoMedio());
        if (dto.getUltimoReajuste() != null)
            produto.setUltimoReajuste(dto.getUltimoReajuste());
        if (dto.getMargemLucro() != null)
            produto.setMargemLucro(dto.getMargemLucro());
        if (dto.getPrecoVenda() != null)
            produto.setPrecoVenda(dto.getPrecoVenda());
        if (dto.getQuantidadeEstoque() != null)
            produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        if (dto.getQuantidadePedido() != null)
            produto.setQuantidadePedido(dto.getQuantidadePedido());
        if (dto.getQuantidadeAguardando() != null)
            produto.setQuantidadeAguardando(dto.getQuantidadeAguardando());
        if (dto.getQuantidadeMinima() != null)
            produto.setQuantidadeMinima(dto.getQuantidadeMinima());
        if (dto.getCodigoComissao() != null)
            produto.setCodigoComissao(dto.getCodigoComissao());
        if (dto.getPeso() != null)
            produto.setPeso(dto.getPeso());
        if (dto.getIpiPercentual() != null)
            produto.setIpiPercentual(dto.getIpiPercentual());
        if (dto.getDiferencaIcmsPercentual() != null)
            produto.setDiferencaIcmsPercentual(dto.getDiferencaIcmsPercentual());
        if (dto.getOutrosPercentual() != null)
            produto.setOutrosPercentual(dto.getOutrosPercentual());
        if (dto.getIcmsPercentual() != null)
            produto.setIcmsPercentual(dto.getIcmsPercentual());
        if (dto.getReducaoIcmsPercentual() != null)
            produto.setReducaoIcmsPercentual(dto.getReducaoIcmsPercentual());
        if (dto.getIcmsSubstituicaoPercentual() != null)
            produto.setIcmsSubstituicaoPercentual(dto.getIcmsSubstituicaoPercentual());
        if (dto.getIpiPercentualFinal() != null)
            produto.setIpiPercentualFinal(dto.getIpiPercentualFinal());
        if (dto.getChecarEstoque() != null)
            produto.setChecarEstoque(dto.getChecarEstoque());
    }
}