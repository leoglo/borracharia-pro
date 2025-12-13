package com.borracharia.borracharia.mapper;

import org.springframework.stereotype.Component;

import com.borracharia.borracharia.dto.Request.ProdutoRequestDTO;
import com.borracharia.borracharia.dto.Response.ProdutoResponseDTO;
import com.borracharia.borracharia.model.Produto;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();

        produto.setCodigoProduto(dto.codigoProduto());
        produto.setCodigoFabricante(dto.codigoFabricante());
        produto.setDescricao(dto.descricao());
        produto.setComplemento(dto.complemento());
        produto.setMarca(dto.marca());
        produto.setOutrosInfo(dto.outrosInfo());
        produto.setClasse(dto.classe());
        produto.setEmbalagem(dto.embalagem());
        produto.setUnidade(dto.unidade());
        produto.setQuantidadeUnidade(dto.quantidadeUnidade());
        produto.setLocalizacao(dto.localizacao());
        produto.setPrecoProduto(dto.precoProduto());
        produto.setFrete(dto.frete());
        produto.setCustoMedio(dto.custoMedio());
        produto.setUltimoReajuste(dto.ultimoReajuste());
        produto.setMargemLucro(dto.margemLucro());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setQuantidadePedido(dto.quantidadePedido());
        produto.setQuantidadeAguardando(dto.quantidadeAguardando());
        produto.setQuantidadeMinima(dto.quantidadeMinima());
        produto.setCodigoComissao(dto.codigoComissao());
        produto.setPeso(dto.peso());
        produto.setIpiPercentual(dto.ipiPercentual());
        produto.setDiferencaIcmsPercentual(dto.diferencaIcmsPercentual());
        produto.setOutrosPercentual(dto.outrosPercentual());
        produto.setIcmsPercentual(dto.icmsPercentual());
        produto.setReducaoIcmsPercentual(dto.reducaoIcmsPercentual());
        produto.setIcmsSubstituicaoPercentual(dto.icmsSubstituicaoPercentual());
        produto.setIpiPercentualFinal(dto.ipiPercentualFinal());
        produto.setChecarEstoque(dto.checarEstoque());

        return produto;
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getCodigoProduto(),
            produto.getCodigoFabricante(),
            produto.getDescricao(),
            produto.getComplemento(),
            produto.getMarca(),
            produto.getOutrosInfo(),
            produto.getClasse(),
            produto.getEmbalagem(),
            produto.getUnidade(),
            produto.getQuantidadeUnidade(),
            produto.getLocalizacao(),
            produto.getPrecoProduto(),
            produto.getFrete(),
            produto.getCustoMedio(),
            produto.getUltimoReajuste(),
            produto.getMargemLucro(),
            produto.getPrecoVenda(),
            produto.getQuantidadeEstoque(),
            produto.getQuantidadePedido(),
            produto.getQuantidadeAguardando(),
            produto.getQuantidadeMinima(),
            produto.getCodigoComissao(),
            produto.getPeso(),
            produto.getIpiPercentual(),
            produto.getDiferencaIcmsPercentual(),
            produto.getOutrosPercentual(),
            produto.getIcmsPercentual(),
            produto.getReducaoIcmsPercentual(),
            produto.getIcmsSubstituicaoPercentual(),
            produto.getIpiPercentualFinal(),
            produto.getChecarEstoque()
        );
    }

    public void updateEntityFromDto(ProdutoRequestDTO dto, Produto produto) {
        produto.setCodigoProduto(dto.codigoProduto());
        produto.setCodigoFabricante(dto.codigoFabricante());
        produto.setDescricao(dto.descricao());
        produto.setComplemento(dto.complemento());
        produto.setMarca(dto.marca());
        produto.setOutrosInfo(dto.outrosInfo());
        produto.setClasse(dto.classe());
        produto.setEmbalagem(dto.embalagem());
        produto.setUnidade(dto.unidade());
        produto.setQuantidadeUnidade(dto.quantidadeUnidade());
        produto.setLocalizacao(dto.localizacao());
        produto.setPrecoProduto(dto.precoProduto());
        produto.setFrete(dto.frete());
        produto.setCustoMedio(dto.custoMedio());
        produto.setUltimoReajuste(dto.ultimoReajuste());
        produto.setMargemLucro(dto.margemLucro());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setQuantidadePedido(dto.quantidadePedido());
        produto.setQuantidadeAguardando(dto.quantidadeAguardando());
        produto.setQuantidadeMinima(dto.quantidadeMinima());
        produto.setCodigoComissao(dto.codigoComissao());
        produto.setPeso(dto.peso());
        produto.setIpiPercentual(dto.ipiPercentual());
        produto.setDiferencaIcmsPercentual(dto.diferencaIcmsPercentual());
        produto.setOutrosPercentual(dto.outrosPercentual());
        produto.setIcmsPercentual(dto.icmsPercentual());
        produto.setReducaoIcmsPercentual(dto.reducaoIcmsPercentual());
        produto.setIcmsSubstituicaoPercentual(dto.icmsSubstituicaoPercentual());
        produto.setIpiPercentualFinal(dto.ipiPercentualFinal());
        produto.setChecarEstoque(dto.checarEstoque());
    }

    public void updateParcial(ProdutoRequestDTO dto, Produto produto) {
        if (dto.codigoProduto() != null)
            produto.setCodigoProduto(dto.codigoProduto());
        if (dto.codigoFabricante() != null)
            produto.setCodigoFabricante(dto.codigoFabricante());
        if (dto.descricao() != null)
            produto.setDescricao(dto.descricao());
        if (dto.complemento() != null)
            produto.setComplemento(dto.complemento());
        if (dto.marca() != null)
            produto.setMarca(dto.marca());
        if (dto.outrosInfo() != null)
            produto.setOutrosInfo(dto.outrosInfo());
        if (dto.classe() != null)
            produto.setClasse(dto.classe());
        if (dto.embalagem() != null)
            produto.setEmbalagem(dto.embalagem());
        if (dto.unidade() != null)
            produto.setUnidade(dto.unidade());
        if (dto.quantidadeUnidade() != null)
            produto.setQuantidadeUnidade(dto.quantidadeUnidade());
        if (dto.localizacao() != null)
            produto.setLocalizacao(dto.localizacao());
        if (dto.precoProduto() != null)
            produto.setPrecoProduto(dto.precoProduto());
        if (dto.frete() != null)
            produto.setFrete(dto.frete());
        if (dto.custoMedio() != null)
            produto.setCustoMedio(dto.custoMedio());
        if (dto.ultimoReajuste() != null)
            produto.setUltimoReajuste(dto.ultimoReajuste());
        if (dto.margemLucro() != null)
            produto.setMargemLucro(dto.margemLucro());
        if (dto.precoVenda() != null)
            produto.setPrecoVenda(dto.precoVenda());
        if (dto.quantidadeEstoque() != null)
            produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        if (dto.quantidadePedido() != null)
            produto.setQuantidadePedido(dto.quantidadePedido());
        if (dto.quantidadeAguardando() != null)
            produto.setQuantidadeAguardando(dto.quantidadeAguardando());
        if (dto.quantidadeMinima() != null)
            produto.setQuantidadeMinima(dto.quantidadeMinima());
        if (dto.codigoComissao() != null)
            produto.setCodigoComissao(dto.codigoComissao());
        if (dto.peso() != null)
            produto.setPeso(dto.peso());
        if (dto.ipiPercentual() != null)
            produto.setIpiPercentual(dto.ipiPercentual());
        if (dto.diferencaIcmsPercentual() != null)
            produto.setDiferencaIcmsPercentual(dto.diferencaIcmsPercentual());
        if (dto.outrosPercentual() != null)
            produto.setOutrosPercentual(dto.outrosPercentual());
        if (dto.icmsPercentual() != null)
            produto.setIcmsPercentual(dto.icmsPercentual());
        if (dto.reducaoIcmsPercentual() != null)
            produto.setReducaoIcmsPercentual(dto.reducaoIcmsPercentual());
        if (dto.icmsSubstituicaoPercentual() != null)
            produto.setIcmsSubstituicaoPercentual(dto.icmsSubstituicaoPercentual());
        if (dto.ipiPercentualFinal() != null)
            produto.setIpiPercentualFinal(dto.ipiPercentualFinal());
        if (dto.checarEstoque() != null)
            produto.setChecarEstoque(dto.checarEstoque());
    }
}