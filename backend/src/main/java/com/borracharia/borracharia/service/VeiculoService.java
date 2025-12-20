package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.VeiculoRequestDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.exception.BusinessException;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
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

    /** Método principal usado pelo controller (POST /veiculos com DTO) */
    @Transactional
    public VeiculoResponseDTO salvar(VeiculoRequestDTO dto) {
        String placaLimpa = limparPlaca(dto.placa());

        if (dto.modelo() == null || dto.modelo().isBlank()) {
            throw new BusinessException("Modelo é obrigatório!");
        }

        validarPlaca(placaLimpa);

        // Verifica placa duplicada
        if (repository.findByPlaca(placaLimpa).isPresent()) {
            throw new BusinessException("Placa já cadastrada!");
        }

        if (dto.ano() != null) {
            validarAno(dto.ano());
        }

        Integer km = dto.km() == null ? 0 : dto.km();
        if (km < 0) {
            throw new BusinessException("KM não pode ser negativo!");
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(placaLimpa);
        veiculo.setModelo(dto.modelo().trim());
        veiculo.setMarca(dto.marca() != null ? dto.marca().trim() : null);
        veiculo.setAno(dto.ano());
        veiculo.setKm(km);
        veiculo.setCor(dto.cor() != null ? dto.cor().trim() : null);

        // Valida e associa cliente
        if (dto.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() -> new BusinessException("Cliente não encontrado!"));
            veiculo.setCliente(cliente);
        }

        try {
            Veiculo salvo = repository.save(veiculo);
            return converterParaDTO(salvo);
        } catch (DataIntegrityViolationException e) {
            // Proteção extra caso haja race condition ou constraint no banco
            throw new BusinessException("Placa já cadastrada!");
        }
    }

    /** Método usado internamente por salvarCompleto (recebe entidade completa) */
    @Transactional
    public Veiculo salvarCompleto(Veiculo veiculo) {
        String placaLimpa = limparPlaca(veiculo.getPlaca());
        validarPlaca(placaLimpa);

        Optional<Veiculo> existente = repository.findByPlaca(placaLimpa);
        if (existente.isPresent()) {
            if (veiculo.getId() != null && veiculo.getId().equals(existente.get().getId())) {
                return atualizarCompleto(veiculo.getId(), veiculo)
                        .orElseThrow(() -> new BusinessException("Erro ao atualizar veículo"));
            }
            throw new BusinessException("Placa já cadastrada!");
        }

        // Validação de cliente
        if (veiculo.getCliente() != null && veiculo.getCliente().getId() != null) {
            if (!clienteRepository.existsById(veiculo.getCliente().getId())) {
                throw new BusinessException("Cliente não encontrado!");
            }
        }

        try {
            VeiculoRequestDTO dto = converterEntidadeParaDTO(veiculo);
            VeiculoResponseDTO salvoDTO = salvar(dto); // Reusa o método principal

            Veiculo veiculoCompleto = repository.findById(salvoDTO.id())
                    .orElseThrow(() -> new BusinessException("Erro ao buscar veículo salvo"));

            formatarPlacaVeiculo(veiculoCompleto);
            return veiculoCompleto;

        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Placa já cadastrada!");
        }
    }

    @Transactional
    public Optional<VeiculoResponseDTO> atualizar(Long id, VeiculoRequestDTO dto) {
        return repository.findById(id).map(veiculo -> {
            if (dto.placa() != null && !dto.placa().isBlank()) {
                String placaLimpa = limparPlaca(dto.placa());
                validarPlaca(placaLimpa);

                if (!placaLimpa.equals(veiculo.getPlaca()) && repository.findByPlaca(placaLimpa).isPresent()) {
                    throw new BusinessException("Placa já cadastrada em outro veículo!");
                }
                veiculo.setPlaca(placaLimpa);
            }

            if (dto.modelo() != null && !dto.modelo().isBlank()) {
                veiculo.setModelo(dto.modelo().trim());
            }
            if (dto.marca() != null)
                veiculo.setMarca(dto.marca().trim());
            if (dto.ano() != null) {
                validarAno(dto.ano());
                veiculo.setAno(dto.ano());
            }
            if (dto.km() != null) {
                if (dto.km() < 0)
                    throw new BusinessException("KM não pode ser negativo!");
                veiculo.setKm(dto.km());
            } else {
                veiculo.setKm(0);
            }
            if (dto.cor() != null)
                veiculo.setCor(dto.cor().trim());

            if (dto.clienteId() != null) {
                Cliente cliente = clienteRepository.findById(dto.clienteId())
                        .orElseThrow(() -> new BusinessException("Cliente não encontrado!"));
                veiculo.setCliente(cliente);
            }

            try {
                Veiculo atualizado = repository.save(veiculo);
                return converterParaDTO(atualizado);
            } catch (DataIntegrityViolationException e) {
                throw new BusinessException("Placa já cadastrada em outro veículo!");
            }
        });
    }

    @Transactional
    public Optional<Veiculo> atualizarCompleto(Long id, Veiculo veiculo) {
        if (veiculo.getPlaca() != null && !veiculo.getPlaca().isBlank()) {
            String placaLimpa = limparPlaca(veiculo.getPlaca());
            validarPlaca(placaLimpa);

            if (repository.findByPlaca(placaLimpa).filter(v -> !v.getId().equals(id)).isPresent()) {
                throw new BusinessException("Placa já cadastrada em outro veículo!");
            }
        }

        if (veiculo.getCliente() != null && veiculo.getCliente().getId() != null) {
            if (!clienteRepository.existsById(veiculo.getCliente().getId())) {
                throw new BusinessException("Cliente não encontrado!");
            }
        }

        VeiculoRequestDTO dto = converterEntidadeParaDTO(veiculo);
        Optional<VeiculoResponseDTO> resultado = atualizar(id, dto);

        if (resultado.isEmpty())
            return Optional.empty();

        Veiculo completo = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Erro ao buscar veículo atualizado"));
        formatarPlacaVeiculo(completo);
        return Optional.of(completo);
    }

    // === MÉTODOS DE BUSCA E DELEÇÃO (inalterados) ===
    public List<VeiculoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::converterParaDTO).toList();
    }

    public List<Veiculo> listarTodosCompleto() {
        List<Veiculo> veiculos = repository.findAll();
        veiculos.forEach(this::formatarPlacaVeiculo);
        return veiculos;
    }

    public Optional<VeiculoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::converterParaDTO);
    }

    public Optional<Veiculo> buscarPorIdCompleto(Long id) {
        return repository.findById(id).map(v -> {
            formatarPlacaVeiculo(v);
            return v;
        });
    }

    public List<VeiculoResponseDTO> buscarPorCliente(Long clienteId) {
        if (clienteId == null)
            throw new BusinessException("ID do cliente é obrigatório");
        return repository.findByClienteId(clienteId).stream().map(this::converterParaDTO).toList();
    }

    public List<Veiculo> buscarPorClienteCompleto(Long clienteId) {
        if (clienteId == null)
            throw new BusinessException("ID do cliente é obrigatório");
        List<Veiculo> veiculos = repository.findByClienteId(clienteId);
        veiculos.forEach(this::formatarPlacaVeiculo);
        return veiculos;
    }

    @Transactional
    public boolean deletar(Long id) {
        return repository.findById(id).map(v -> {
            repository.delete(v);
            return true;
        }).orElse(false);
    }

    @Transactional
    public void deletarPorCliente(Long clienteId) {
        repository.deleteByClienteId(clienteId);
    }

    // === AUXILIARES ===

    private VeiculoResponseDTO converterParaDTO(Veiculo veiculo) {
        Long clienteId = veiculo.getCliente() != null ? veiculo.getCliente().getId() : null;
        String clienteNome = veiculo.getCliente() != null ? veiculo.getCliente().getNome() : null;

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
    }

    private VeiculoRequestDTO converterEntidadeParaDTO(Veiculo veiculo) {
        Long clienteId = veiculo.getCliente() != null ? veiculo.getCliente().getId() : null;
        // Removida validação redundante aqui — já feita nos métodos principais
        return new VeiculoRequestDTO(
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getMarca(),
                veiculo.getAno(),
                veiculo.getKm(),
                clienteId,
                veiculo.getCor());
    }

    private String limparPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new BusinessException("Placa é obrigatória!");
        }
        return placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private void validarPlaca(String placa) {
        if (placa.length() != 7) {
            throw new BusinessException("Placa deve ter 7 caracteres!");
        }
        if (!placa.matches("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$")) {
            throw new BusinessException("Formato de placa inválido! Use ABC1234 ou ABC1D23");
        }
    }

    private void validarAno(Integer ano) {
        int anoAtual = Year.now().getValue();
        if (ano < 1900 || ano > anoAtual + 1) {
            throw new BusinessException("Ano inválido! Deve estar entre 1900 e " + (anoAtual + 1));
        }
    }

    private String formatarPlaca(String placa) {
        if (placa == null || placa.length() != 7)
            return placa;
        return placa.substring(0, 3) + "-" + placa.substring(3);
    }

    private void formatarPlacaVeiculo(Veiculo veiculo) {
        if (veiculo.getPlaca() != null && veiculo.getPlaca().length() == 7 && !veiculo.getPlaca().contains("-")) {
            veiculo.setPlaca(formatarPlaca(veiculo.getPlaca()));
        }
    }
}