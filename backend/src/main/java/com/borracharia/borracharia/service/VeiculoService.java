package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.VeiculoRequestDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    public VeiculoService(VeiculoRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public VeiculoResponseDTO salvar(VeiculoRequestDTO dto) {
        // normaliza valores
        String placaLimpa = limparPlaca(dto.getPlaca());

        // valida campos obrigatórios
        if (dto.getModelo() == null || dto.getModelo().isBlank()) {
            throw new RuntimeException("Modelo é obrigatório!");
        }

        // valida placa
        validarPlaca(placaLimpa);

        // verifica se placa já existe
        if (repository.findByPlaca(placaLimpa).isPresent()) {
            throw new RuntimeException("Placa já cadastrada!");
        }

        // valida ano (se informado)
        if (dto.getAno() != null) {
            validarAno(dto.getAno());
        }

        // valida km (se informado)
        if (dto.getKm() != null && dto.getKm() < 0) {
            throw new RuntimeException("KM não pode ser negativo!");
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(placaLimpa);
        veiculo.setModelo(dto.getModelo().trim());
        veiculo.setMarca(dto.getMarca() != null ? dto.getMarca().trim() : null);
        veiculo.setAno(dto.getAno());
        veiculo.setKm(dto.getKm());
        veiculo.setCor(dto.getCor() != null ? dto.getCor().trim() : null);

        // associa cliente (se informado)
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
            veiculo.setCliente(cliente);
        }

        Veiculo salvo = repository.save(veiculo);
        return converterParaDTO(salvo);
    }

    public List<VeiculoResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Optional<VeiculoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::converterParaDTO);
    }

    public List<VeiculoResponseDTO> buscarPorCliente(Long clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório");
        }

        return repository.findByClienteId(clienteId).stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public Optional<VeiculoResponseDTO> atualizar(Long id, VeiculoRequestDTO dto) {
        return repository.findById(id).map(veiculo -> {

            if (dto.getPlaca() != null && !dto.getPlaca().isBlank()) {
                String placaLimpa = limparPlaca(dto.getPlaca());
                validarPlaca(placaLimpa);

                if (!placaLimpa.equals(veiculo.getPlaca()) && repository.findByPlaca(placaLimpa).isPresent()) {
                    throw new RuntimeException("Placa já cadastrada em outro veículo!");
                }
                veiculo.setPlaca(placaLimpa);
            }

            if (dto.getModelo() != null && !dto.getModelo().isBlank()) {
                veiculo.setModelo(dto.getModelo().trim());
            }

            if (dto.getMarca() != null) {
                veiculo.setMarca(dto.getMarca().trim());
            }

            if (dto.getAno() != null) {
                validarAno(dto.getAno());
                veiculo.setAno(dto.getAno());
            }

            if (dto.getKm() != null) {
                if (dto.getKm() < 0) {
                    throw new RuntimeException("KM não pode ser negativo!");
                }
                veiculo.setKm(dto.getKm());
            }

            if (dto.getCor() != null) {
                veiculo.setCor(dto.getCor().trim());
            }

            if (dto.getClienteId() != null) {
                Cliente cliente = clienteRepository.findById(dto.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
                veiculo.setCliente(cliente);
            }

            Veiculo atualizado = repository.save(veiculo);
            return converterParaDTO(atualizado);
        });
    }

    @Transactional
    public boolean deletar(Long id) {
        return repository.findById(id).map(veiculo -> {
            repository.delete(veiculo);
            return true;
        }).orElse(false);
    }

    /* -----------Funções auxiliares ------------------- */

    private VeiculoResponseDTO converterParaDTO(Veiculo veiculo) {
        try {
            Long clienteId = null;
            String clienteNome = null;

            if (veiculo.getCliente() != null) {
                clienteId = veiculo.getCliente().getId();
                clienteNome = veiculo.getCliente().getNome();
            }

            return new VeiculoResponseDTO(
                    veiculo.getId(),
                    formatarPlaca(veiculo.getPlaca()),
                    veiculo.getModelo(),
                    veiculo.getMarca(),
                    veiculo.getAno(),
                    veiculo.getKm(),
                    clienteId,
                    clienteNome,
                    veiculo.getCor());
        } catch (Exception e) {
            // Log do erro para debug
            System.err.println("Erro ao converter veículo para DTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar dados do veículo", e);
        }
    }

    private String limparPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new RuntimeException("Placa é obrigatória!");
        }
        return placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private void validarPlaca(String placa) {
        if (placa.length() != 7) {
            throw new RuntimeException("Placa deve ter 7 caracteres!");
        }

        // Valida formato Mercosul (ABC1D23) ou antigo (ABC1234)
        if (!placa.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$")) {
            throw new RuntimeException("Formato de placa inválido! Use ABC1234 ou ABC1D23");
        }
    }

    private void validarAno(Integer ano) {
        int anoAtual = Year.now().getValue();

        if (ano < 1900 || ano > anoAtual + 1) {
            throw new RuntimeException("Ano inválido! Deve estar entre 1900 e " + (anoAtual + 1));
        }
    }

    private String formatarPlaca(String placa) {
        if (placa == null || placa.length() != 7) {
            return placa;
        }
        return placa.substring(0, 3) + "-" + placa.substring(3);
    }
}