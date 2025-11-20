package com.borracharia.borracharia.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.borracharia.borracharia.dto.Request.ItemOrdemServicoRequestDTO;
import com.borracharia.borracharia.dto.Request.OrdemServicoRequestDTO;
import com.borracharia.borracharia.dto.Response.ItemOrdemServicoResponseDTO;
import com.borracharia.borracharia.dto.Response.OrdemServicoResponseDTO;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.ItemOrdemServico;
import com.borracharia.borracharia.model.OrdemServico;
import com.borracharia.borracharia.model.Produto;
import com.borracharia.borracharia.model.Veiculo;

@Component
public class OrdemServicoMapper {

    /**
     * Converte a OS do DTO para a entidade com cliente e veículo.
     * Aqui já criamos os itens!
     */
    public OrdemServico toEntity(OrdemServicoRequestDTO dto, Cliente cliente, Veiculo veiculo) {

        OrdemServico os = new OrdemServico();
        os.setCliente(cliente);
        os.setVeiculo(veiculo);

        os.setValorMaoDeObra(dto.getValorMaoDeObra());
        os.setStatus(dto.getStatus());

        // CRIA OS ITENS DA ORDEM
        if (dto.getItens() != null) {
            os.setItens(
                dto.getItens().stream()
                   .map(itemDto -> toItemEntity(itemDto))
                   .collect(Collectors.toList())
            );

            // vincula item → os
            os.getItens().forEach(i -> i.setOrdemServico(os));
        }

        return os;
    }

    /**
     * Converte um item do DTO para entidade sem produto por enquanto.
     * O SERVICE define o produto real (consultado no banco).
     */
    public ItemOrdemServico toItemEntity(ItemOrdemServicoRequestDTO dto) {

        ItemOrdemServico item = new ItemOrdemServico();

        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(null);  // definido no service
        item.setProduto(null);        // definido no service

        return item;
    }

    /**
     * Converte entidade → DTO para resposta.
     */
    public OrdemServicoResponseDTO toResponse(OrdemServico os) {

        OrdemServicoResponseDTO dto = new OrdemServicoResponseDTO();

        dto.setId(os.getId());
        dto.setValorMaoDeObra(os.getValorMaoDeObra());
        dto.setValorTotal(os.getValorTotal());
        dto.setStatus(os.getStatus());
        dto.setDataAbertura(os.getDataAbertura());
        dto.setDataFechamento(os.getDataFechamento());

        dto.setClienteNome(os.getCliente() != null ? os.getCliente().getNome() : null);

        if (os.getVeiculo() != null) {
            dto.setVeiculoDescricao(os.getVeiculo().getModelo() + " - " + os.getVeiculo().getPlaca());
        }

        if (os.getItens() != null) {
            dto.setItens(
                os.getItens().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private ItemOrdemServicoResponseDTO toItemResponse(ItemOrdemServico item) {

        ItemOrdemServicoResponseDTO dto = new ItemOrdemServicoResponseDTO();

        Produto p = item.getProduto();

        dto.setProdutoNome(p.getDescricao());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setValorTotal(item.getPrecoUnitario()
                           .multiply(BigDecimal.valueOf(item.getQuantidade())));

        return dto;
    }
}