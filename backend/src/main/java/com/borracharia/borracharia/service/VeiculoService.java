package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.VeiculoRequestDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    public VeiculoService(VeiculoRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    public VeiculoResponseDTO salvar(VeiculoRequestDTO dto) {
        // VALIDAÇÕES OBRIGATÓRIAS PARA CREATE
        if (dto.getPlaca() == null || dto.getPlaca().trim().isEmpty()) {
            throw new RuntimeException("Placa é obrigatória!");
        }
        if (dto.getModelo() == null || dto.getModelo().trim().isEmpty()) {
            throw new RuntimeException("Modelo é obrigatório!");
        }

        String placaLimpa = limparPlaca(dto.getPlaca());
        validarPlaca(placaLimpa);
        validarAno(dto.getAno());

        repository.findByPlaca(placaLimpa).ifPresent(v -> {
            throw new RuntimeException("Placa já cadastrada!");
        });

        Veiculo v = new Veiculo();
        v.setPlaca(placaLimpa);
        v.setModelo(dto.getModelo());
        v.setMarca(dto.getMarca());
        v.setAno(dto.getAno());
        v.setKm(dto.getKm());

        if (dto.getClienteId() != null) {
            clienteRepository.findById(dto.getClienteId())
                    .ifPresentOrElse(
                            v::setCliente,
                            () -> {
                                throw new RuntimeException("Cliente não encontrado!");
                            });
        }

        Veiculo salvo = repository.save(v);
        return converterParaDTO(salvo);
    }

    public List<VeiculoResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public Optional<VeiculoResponseDTO> buscar(Long id) {
        return repository.findById(id)
                .map(this::converterParaDTO);
    }

    public Optional<VeiculoResponseDTO> atualizar(Long id, VeiculoRequestDTO dto) {
        return repository.findById(id).map(veiculoExistente -> {

            // Atualizar APENAS os campos que foram enviados
            if (dto.getPlaca() != null && !dto.getPlaca().trim().isEmpty()) {
                String placaLimpa = limparPlaca(dto.getPlaca());
                validarPlaca(placaLimpa);

                if (!placaLimpa.equals(veiculoExistente.getPlaca())) {
                    repository.findByPlaca(placaLimpa).ifPresent(v -> {
                        throw new RuntimeException("Placa já cadastrada em outro veículo!");
                    });
                }

                veiculoExistente.setPlaca(placaLimpa);
            }

            if (dto.getModelo() != null && !dto.getModelo().trim().isEmpty()) {
                veiculoExistente.setModelo(dto.getModelo());
            }

            if (dto.getMarca() != null) {
                veiculoExistente.setMarca(dto.getMarca());
            }

            if (dto.getAno() != null) {
                validarAno(dto.getAno());
                veiculoExistente.setAno(dto.getAno());
            }

            if (dto.getKm() != null) {
                if (dto.getKm() < 0) {
                    throw new RuntimeException("KM não pode ser negativo!");
                }
                veiculoExistente.setKm(dto.getKm());
            }

            if (dto.getClienteId() != null) {
                clienteRepository.findById(dto.getClienteId())
                        .ifPresentOrElse(
                                veiculoExistente::setCliente,
                                () -> {
                                    throw new RuntimeException("Cliente não encontrado!");
                                });
            }

            Veiculo atualizado = repository.save(veiculoExistente);
            return converterParaDTO(atualizado);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(v -> {
            repository.delete(v);
            return true;
        }).orElse(false);
    }

    // ============== MÉTODOS AUXILIARES ==============

    private String limparPlaca(String placa) {
        if (placa == null) {
            throw new RuntimeException("Placa não pode ser nula!");
        }
        return placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private void validarPlaca(String placa) {
        if (placa.length() != 7) {
            throw new RuntimeException("Placa deve ter 7 caracteres!");
        }

        if (!placa.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$")) {
            throw new RuntimeException("Formato de placa inválido! Use ABC1234 ou ABC1D23");
        }
    }

    private void validarAno(Integer ano) {
        if (ano == null) {
            return;
        }

        int anoAtual = Year.now().getValue();

        if (ano < 1900 || ano > anoAtual + 1) {
            throw new RuntimeException("Ano inválido! Deve estar entre 1900 e " + (anoAtual + 1));
        }
    }

    private VeiculoResponseDTO converterParaDTO(Veiculo v) {
        return new VeiculoResponseDTO(
                v.getId(),
                formatarPlaca(v.getPlaca()),
                v.getModelo(),
                v.getMarca(),
                v.getAno(),
                v.getKm(),
                v.getCliente() != null ? v.getCliente().getId() : null,
                v.getCliente() != null ? v.getCliente().getNome() : null);
    }

    private String formatarPlaca(String placa) {
        if (placa == null || placa.length() != 7) {
            return placa;
        }
        return placa.substring(0, 3) + "-" + placa.substring(3);
    }
}