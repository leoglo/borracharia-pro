package com.borracharia.borracharia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borracharia.borracharia.dto.Request.ItemOrdemServicoRequestDTO;
import com.borracharia.borracharia.dto.Request.OrdemServicoRequestDTO;
import com.borracharia.borracharia.dto.Response.OrdemServicoResponseDTO;
import com.borracharia.borracharia.mapper.OrdemServicoMapper;
import com.borracharia.borracharia.model.ItemOrdemServico;
import com.borracharia.borracharia.model.OrdemServico;
import com.borracharia.borracharia.model.Produto;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.repository.OrdemServicoRepository;
import com.borracharia.borracharia.repository.ProdutoRepository;
import com.borracharia.borracharia.repository.VeiculoRepository;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemRepo;
    private final ProdutoRepository produtoRepo;
    private final ClienteRepository clienteRepo;
    private final VeiculoRepository veiculoRepo;
    private final OrdemServicoMapper mapper;

    public OrdemServicoService(OrdemServicoRepository ordemRepo,
                               ProdutoRepository produtoRepo,
                               ClienteRepository clienteRepo,
                               VeiculoRepository veiculoRepo,
                               OrdemServicoMapper mapper) {
        this.ordemRepo = ordemRepo;
        this.produtoRepo = produtoRepo;
        this.clienteRepo = clienteRepo;
        this.veiculoRepo = veiculoRepo;
        this.mapper = mapper;
    }

    private static BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    // ---------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------
    @Transactional
    public OrdemServicoResponseDTO criarOrdem(OrdemServicoRequestDTO dto) {

        Cliente cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + dto.getClienteId()));

        Veiculo veiculo = veiculoRepo.findById(dto.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + dto.getVeiculoId()));

        // Cria OS sem itens
        OrdemServico os = mapper.toEntity(dto, cliente, veiculo);
        os.setItens(new ArrayList<>());  // <-- ESSENCIAL

        BigDecimal somaItens = BigDecimal.ZERO;

        for (ItemOrdemServicoRequestDTO itemDto : dto.getItens()) {

            Produto produto = produtoRepo.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente: " + produto.getDescricao());
            }

            // Criando o item
            ItemOrdemServico item = new ItemOrdemServico();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());

            BigDecimal precoUnitario =
                    itemDto.getPrecoUnitario() != null && itemDto.getPrecoUnitario().compareTo(BigDecimal.ZERO) > 0
                            ? itemDto.getPrecoUnitario()
                            : safe(produto.getPrecoVenda());

            item.setPrecoUnitario(precoUnitario);

            BigDecimal valorItem = precoUnitario.multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setValorTotal(valorItem);

            somaItens = somaItens.add(valorItem);

            // Debita estoque
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoRepo.save(produto);

            // Adiciona item na OS
            os.getItens().add(item);
            item.setOrdemServico(os);
        }

        // total final
        os.setValorTotal(somaItens.add(safe(os.getValorMaoDeObra())));

        // Salvar OS completa
        ordemRepo.save(os);

        return mapper.toResponse(os);
    }

    // ---------------------------------------------------------------
    // READ
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDTO> listarOrdens() {
        return ordemRepo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponseDTO buscarOrdemPorId(Long id) {
        OrdemServico os = ordemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OS não encontrada: " + id));

        return mapper.toResponse(os);
    }

    // ---------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------
    @Transactional
    public OrdemServicoResponseDTO atualizarOrdem(Long id, OrdemServicoRequestDTO dto) {

        OrdemServico existente = ordemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada: " + id));

        // Estornar estoque atual
        for (ItemOrdemServico item : existente.getItens()) {
            Produto p = item.getProduto();
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() + item.getQuantidade());
            produtoRepo.save(p);
        }

        Cliente cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepo.findById(dto.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        OrdemServico nova = mapper.toEntity(dto, cliente, veiculo);
        nova.setId(existente.getId());
        nova.setItens(new ArrayList<>());

        BigDecimal somaItens = BigDecimal.ZERO;

        for (ItemOrdemServicoRequestDTO itemDto : dto.getItens()) {

            Produto produto = produtoRepo.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

            if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente: " + produto.getDescricao());
            }

            ItemOrdemServico item = new ItemOrdemServico();
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());

            BigDecimal precoUnitario =
                    itemDto.getPrecoUnitario() != null && itemDto.getPrecoUnitario().compareTo(BigDecimal.ZERO) > 0
                            ? itemDto.getPrecoUnitario()
                            : safe(produto.getPrecoVenda());

            item.setPrecoUnitario(precoUnitario);

            BigDecimal valorItem = precoUnitario.multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            item.setValorTotal(valorItem);

            somaItens = somaItens.add(valorItem);

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoRepo.save(produto);

            nova.getItens().add(item);
            item.setOrdemServico(nova);
        }

        nova.setValorTotal(somaItens.add(safe(nova.getValorMaoDeObra())));

        OrdemServico salvo = ordemRepo.save(nova);
        return mapper.toResponse(salvo);
    }

    // ---------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------
    @Transactional
    public void deletarOrdem(Long id) {

        OrdemServico os = ordemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OS não encontrada: " + id));

        for (ItemOrdemServico item : os.getItens()) {
            Produto p = item.getProduto();
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() + item.getQuantidade());
            produtoRepo.save(p);
        }

        ordemRepo.delete(os);
    }
}
