package com.borracharia.borracharia.service;

import com.borracharia.borracharia.dto.Request.ClienteRequestDTO;
import com.borracharia.borracharia.dto.Response.ClienteResponseDTO;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        // normaliza valores
        String cpfLimpo = limpar(dto.getCpf());
        String telefoneLimpo = limpar(dto.getTelefone());

        // valida nome obrigatório
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório!");
        }

        // valida CPF (se informado)
        if (cpfLimpo != null && !cpfLimpo.isBlank()) {
            if (!isCpfValido(cpfLimpo)) {
                throw new RuntimeException("CPF inválido!");
            }
            if (repository.findByCpf(cpfLimpo).isPresent()) {
                throw new RuntimeException("CPF já cadastrado!");
            }
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome().trim());
        cliente.setCpf(cpfLimpo);
        cliente.setTelefone(telefoneLimpo);

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

            if (dto.getNome() != null && !dto.getNome().isBlank()) {
                cliente.setNome(dto.getNome().trim());
            }

            if (dto.getCpf() != null) {
                String cpfLimpo = limpar(dto.getCpf());
                if (!isCpfValido(cpfLimpo)) {
                    throw new RuntimeException("CPF inválido!");
                }
                if (!cpfLimpo.equals(cliente.getCpf()) && repository.findByCpf(cpfLimpo).isPresent()) {
                    throw new RuntimeException("CPF já cadastrado!");
                }
                cliente.setCpf(cpfLimpo);
            }

            if (dto.getTelefone() != null) {
                cliente.setTelefone(limpar(dto.getTelefone()));
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
                formatarTelefone(cliente.getTelefone())
        );
    }

    private String limpar(String valor) {
        return valor == null ? null : valor.replaceAll("\\D", "");
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9);
    }

    private String formatarTelefone(String tel) {
        if (tel == null) return null;
        String only = limpar(tel);
        if (only.length() == 11) {
            return "(" + only.substring(0, 2) + ") " +
                    only.substring(2, 7) + "-" +
                    only.substring(7);
        }
        return tel;
    }

    /** Validação oficial de CPF */
    private boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        cpf = limpar(cpf);
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9) primeiroDigito = 0;
            if (primeiroDigito != (cpf.charAt(9) - '0')) return false;

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9) segundoDigito = 0;
            return segundoDigito == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}
