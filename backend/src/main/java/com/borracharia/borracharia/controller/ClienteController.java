package com.borracharia.borracharia.controller;

import com.borracharia.borracharia.dto.Request.ClienteRequestDTO;
import com.borracharia.borracharia.dto.Response.ClienteResponseDTO;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.service.ClienteService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteService service, ClienteRepository clienteRepository) {
        this.service = service;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        try {
            ClienteResponseDTO response = service.salvar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = service.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        try {
            return service.atualizar(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // NOVO ENDPOINT DE BUSCA
@GetMapping("/buscar")
public ResponseEntity<List<ClienteResponseDTO>> buscarClientes(@RequestParam String termo) {
    Page<Cliente> clientes = clienteRepository
        .findByNomeContainingIgnoreCaseOrCpfContainingOrTelefoneContaining(
            termo, termo, termo, PageRequest.of(0, 10)
        );
    
    List<ClienteResponseDTO> response = clientes.stream()
        .map(ClienteResponseDTO::new)
        .collect(Collectors.toList());
        
    return ResponseEntity.ok(response);
}
}