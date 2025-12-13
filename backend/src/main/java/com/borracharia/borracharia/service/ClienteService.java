package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.ClienteRequestDTO;
import com.borracharia.borracharia.dto.Response.ClienteResponseDTO;
import com.borracharia.borracharia.repository.VeiculoRepository;
import com.borracharia.borracharia.dto.Request.VeiculoRequestDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final VeiculoRepository veiculoRepository;

    public ClienteService(ClienteRepository repository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        String cpfLimpo = limpar(dto.cpf());
        String telefoneLimpo = limpar(dto.telefone());
        String cepLimpo = limpar(dto.cep());

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório!");
        }

        if (cpfLimpo != null && !cpfLimpo.isBlank()) {
            if (!isCpfValido(cpfLimpo)) {
                throw new RuntimeException("CPF inválido!");
            }
            if (repository.existsByCpf(cpfLimpo)) {
                throw new RuntimeException("CPF já cadastrado!");
            }
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome().trim());
        cliente.setCpf(cpfLimpo);
        cliente.setTelefone(telefoneLimpo);
        cliente.setEmail(dto.email() != null ? dto.email().trim() : null);
        cliente.setCep(cepLimpo);
        cliente.setRua(dto.rua() != null ? dto.rua().trim() : null);
        cliente.setNumero(dto.numero());
        cliente.setBairro(dto.bairro() != null ? dto.bairro().trim() : null);
        cliente.setCidade(dto.cidade() != null ? dto.cidade().trim() : null);
        cliente.setEstado(dto.estado() != null ? dto.estado().trim() : null);

        if (dto.veiculo() != null) {
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca(dto.veiculo().placa());
            veiculo.setMarca(dto.veiculo().marca());
            veiculo.setModelo(dto.veiculo().modelo());
            veiculo.setCor(dto.veiculo().cor());
            veiculo = veiculoRepository.save(veiculo);
            cliente.getVeiculos().add(veiculo);
        }

        Cliente salvo = repository.save(cliente);
        return converterParaDTO(salvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Optional<ClienteResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::converterParaDTO);
    }

    @Transactional
    public Optional<ClienteResponseDTO> atualizar(Long id, ClienteRequestDTO dto) {
        return repository.findById(id).map(cliente -> {

            if (dto.nome() != null && !dto.nome().isBlank()) {
                cliente.setNome(dto.nome().trim());
            }

            if (dto.cpf() != null) {
                String cpfLimpo = limpar(dto.cpf());
                if (!isCpfValido(cpfLimpo)) {
                    throw new RuntimeException("CPF inválido!");
                }
                if (!cpfLimpo.equals(cliente.getCpf()) && repository.existsByCpf(cpfLimpo)) {
                    throw new RuntimeException("CPF já cadastrado!");
                }
                cliente.setCpf(cpfLimpo);
            }

            if (dto.telefone() != null) {
                cliente.setTelefone(limpar(dto.telefone()));
            }

            if (dto.email() != null) {
                cliente.setEmail(dto.email().trim());
            }

            if (dto.cep() != null) {
                cliente.setCep(limpar(dto.cep()));
            }

            if (dto.rua() != null) {
                cliente.setRua(dto.rua().trim());
            }

            if (dto.numero() != null) {
                cliente.setNumero(dto.numero());
            }

            if (dto.bairro() != null) {
                cliente.setBairro(dto.bairro().trim());
            }

            if (dto.cidade() != null) {
                cliente.setCidade(dto.cidade().trim());
            }

            if (dto.estado() != null) {
                cliente.setEstado(dto.estado().trim());
            }

            Cliente atualizado = repository.save(cliente);
            return converterParaDTO(atualizado);
        });
    }

    @Transactional
    public boolean deletar(Long id) {
        return repository.findById(id).map(cliente -> {
            repository.delete(cliente);
            return true;
        }).orElse(false);
    }

    /* -----------Funções auxiliares ------------------- */

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                formatarCpf(cliente.getCpf()),
                formatarTelefone(cliente.getTelefone()),
                cliente.getEmail(),
                formatarCep(cliente.getCep()),
                cliente.getRua(),
                cliente.getNumero(),
                cliente.getBairro(),
                cliente.getCidade(),
                cliente.getEstado());
    }

    private String limpar(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11)
            return cpf;
        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9);
    }

    private String formatarTelefone(String tel) {
        if (tel == null)
            return null;
        String only = limpar(tel);
        if (only.length() == 11) {
            return "(" + only.substring(0, 2) + ") " +
                    only.substring(2, 7) + "-" +
                    only.substring(7);
        } else if (only.length() == 10) {
            return "(" + only.substring(0, 2) + ") " +
                    only.substring(2, 6) + "-" +
                    only.substring(6);
        }
        return tel;
    }

    private String formatarCep(String cep) {
        if (cep == null || cep.length() != 8)
            return cep;
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null)
            return false;
        cpf = limpar(cpf);
        if (cpf.length() != 11)
            return false;
        if (cpf.matches("(\\d)\\1{10}"))
            return false;

        try {
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9)
                primeiroDigito = 0;
            if (primeiroDigito != (cpf.charAt(9) - '0'))
                return false;

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9)
                segundoDigito = 0;
            return segundoDigito == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}