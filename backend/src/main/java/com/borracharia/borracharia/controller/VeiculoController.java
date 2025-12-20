package com.borracharia.borracharia.controller;

import com.borracharia.borracharia.dto.Request.VeiculoRequestDTO;
import com.borracharia.borracharia.dto.Response.VeiculoResponseDTO;
import com.borracharia.borracharia.exception.BusinessException;
import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.service.VeiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodos() {
        return ResponseEntity.ok(veiculoService.listarTodosCompleto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return veiculoService.buscarPorIdCompleto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Veiculo>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(veiculoService.buscarPorClienteCompleto(clienteId));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody VeiculoRequestDTO dto) {
        System.out.println("POST /veiculos - Recebido via DTO");
        System.out.println("   Placa: " + dto.placa());
        System.out.println("   Modelo: " + dto.modelo());

        try {
            VeiculoResponseDTO veiculoSalvo = veiculoService.salvar(dto); // ← MÉTODO SEGURO E VALIDADO
            System.out.println("Veículo criado com sucesso");
            return ResponseEntity.ok(veiculoSalvo);
        } catch (BusinessException e) {
            System.out.println("BusinessException capturada no Controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.out.println("Exception inesperada: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro interno ao salvar veículo"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo) {
        try {
            return veiculoService.atualizarCompleto(id, veiculo)
                    .map(v -> ResponseEntity.ok((Object) v))
                    .orElse(ResponseEntity.notFound().build());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (veiculoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<Void> deletarPorCliente(@PathVariable Long clienteId) {
        veiculoService.deletarPorCliente(clienteId);
        return ResponseEntity.noContent().build();
    }

    private record ErrorResponse(String message) {
    }
}