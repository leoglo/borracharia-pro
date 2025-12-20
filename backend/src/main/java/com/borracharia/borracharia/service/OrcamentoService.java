package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.OrcamentoRequestDTO;
import com.borracharia.borracharia.dto.Response.OrcamentoResponseDTO;
import com.borracharia.borracharia.dto.Response.ClienteResponseDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.model.*;
import com.borracharia.borracharia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public OrcamentoResponseDTO criarOrcamento(OrcamentoRequestDTO dto) {
        // Buscar cliente e veículo
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        // Criar orçamento
        Orcamento orcamento = new Orcamento();
        orcamento.setCliente(cliente);
        orcamento.setVeiculo(veiculo);
        orcamento.setDataAbertura(dto.dataAbertura() != null ? dto.dataAbertura() : LocalDate.now());
        orcamento.setDesconto(dto.desconto() != null ? dto.desconto() : 0.0);
        orcamento.setTotal(dto.total());
        orcamento.setStatus("ABERTO");

        // Gerar número do orçamento
        String numeroOrcamento = gerarNumeroOrcamento();
        orcamento.setNumeroOrcamento(numeroOrcamento);

        // Salvar orçamento primeiro
        orcamento = orcamentoRepository.save(orcamento);

        // Criar itens
        final Orcamento orcamentoFinal = orcamento;
        List<ItemOrcamento> itens = dto.itens().stream().map(itemDto -> {
            ItemOrcamento item = new ItemOrcamento();
            item.setOrcamento(orcamentoFinal);
            item.setCodigo(itemDto.codigo());
            item.setDescricao(itemDto.descricao());
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setTotal(itemDto.total());
            return item;
        }).collect(Collectors.toList());

        orcamento.setItens(itens);

        // Salvar novamente com os itens
        orcamento = orcamentoRepository.save(orcamento);

        return converterParaDTO(orcamento);
    }

    @Transactional
    public OrcamentoResponseDTO atualizarOrcamento(Long id, OrcamentoRequestDTO dto) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        // Atualizar cliente e veículo se mudaram
        if (dto.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            orcamento.setCliente(cliente);
        }

        if (dto.veiculoId() != null) {
            Veiculo veiculo = veiculoRepository.findById(dto.veiculoId())
                    .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            orcamento.setVeiculo(veiculo);
        }

        orcamento.setDataAbertura(dto.dataAbertura());
        orcamento.setDesconto(dto.desconto() != null ? dto.desconto() : 0.0);
        orcamento.setTotal(dto.total());

        // Remover itens antigos
        orcamento.getItens().clear();

        // CORREÇÃO: Salvar antes de criar os itens para ter o orcamento persistido
        Orcamento orcamentoAtualizado = orcamentoRepository.save(orcamento);

        // Criar novos itens usando a variável salva (que é efetivamente final)
        List<ItemOrcamento> novosItens = dto.itens().stream().map(itemDto -> {
            ItemOrcamento item = new ItemOrcamento();
            item.setOrcamento(orcamentoAtualizado);
            item.setCodigo(itemDto.codigo());
            item.setDescricao(itemDto.descricao());
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setTotal(itemDto.total());
            return item;
        }).collect(Collectors.toList());

        orcamentoAtualizado.setItens(novosItens);

        // Salvar novamente com os itens
        Orcamento orcamentoFinal = orcamentoRepository.save(orcamentoAtualizado);

        return converterParaDTO(orcamentoFinal);
    }

    public OrcamentoResponseDTO buscarPorId(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        return converterParaDTO(orcamento);
    }

    public List<OrcamentoResponseDTO> listarTodos() {
        return orcamentoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        orcamentoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> buscarPorTermo(String termo) {
        try {
            if (termo == null || termo.trim().isEmpty()) {
                return List.of();
            }

            List<Orcamento> orcamentos = orcamentoRepository.buscarPorClienteNomeOuCpf(termo.trim());

            return orcamentos.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar orçamentos por termo: " + e.getMessage());
            return List.of();
        }
    }

    private String gerarNumeroOrcamento() {
        LocalDate hoje = LocalDate.now();
        String dataFormatada = hoje.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orcamentoRepository.count() + 1;
        return String.format("ORC-%s-%04d", dataFormatada, count);
    }

    private OrcamentoResponseDTO converterParaDTO(Orcamento orcamento) {
        // Converter cliente
        ClienteResponseDTO clienteDto = new ClienteResponseDTO(
                orcamento.getCliente().getId(),
                orcamento.getCliente().getNome(),
                orcamento.getCliente().getCpf(),
                orcamento.getCliente().getTelefone(),
                orcamento.getCliente().getEmail(),
                orcamento.getCliente().getRua(),
                orcamento.getCliente().getNumero(),
                orcamento.getCliente().getBairro(),
                orcamento.getCliente().getCidade(),
                orcamento.getCliente().getEstado(),
                orcamento.getCliente().getCep());

        // Converter veículo (ATUALIZADO com todos os campos do record)
        VeiculoResponseDTO veiculoDto = new VeiculoResponseDTO(
                orcamento.getVeiculo().getId(),
                orcamento.getVeiculo().getPlaca(),
                orcamento.getVeiculo().getModelo(),
                orcamento.getVeiculo().getMarca(),
                orcamento.getVeiculo().getAno(),
                orcamento.getVeiculo().getKm(), // Adicione se existir no model
                orcamento.getVeiculo().getCliente().getId(),
                orcamento.getVeiculo().getCliente().getNome(),
                orcamento.getVeiculo().getCor());

        // Converter itens
        List<OrcamentoResponseDTO.ItemDTO> itensDto = orcamento.getItens().stream()
                .map(item -> new OrcamentoResponseDTO.ItemDTO(
                        item.getId(),
                        item.getCodigo(),
                        item.getDescricao(),
                        item.getQuantidade(),
                        item.getValorUnitario(),
                        item.getTotal()))
                .collect(Collectors.toList());

        return new OrcamentoResponseDTO(
                orcamento.getId(),
                orcamento.getNumeroOrcamento(),
                clienteDto,
                veiculoDto,
                itensDto,
                orcamento.getDataAbertura(),
                orcamento.getDesconto(),
                orcamento.getTotal(),
                orcamento.getStatus());
    }
}