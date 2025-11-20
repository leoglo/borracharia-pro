package com.borracharia.borracharia.controller;

import com.borracharia.borracharia.dto.Request.EstoqueMovimentacaoRequestDTO;
import com.borracharia.borracharia.dto.Response.EstoqueMovimentacaoResponseDTO;
import com.borracharia.borracharia.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@CrossOrigin("*")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PostMapping("/entrada")
    public ResponseEntity<EstoqueMovimentacaoResponseDTO> entrada(
            @Valid @RequestBody EstoqueMovimentacaoRequestDTO dto) {
        return ResponseEntity.status(201).body(service.registrarEntrada(dto));
    }

    @PostMapping("/saida")
    public ResponseEntity<EstoqueMovimentacaoResponseDTO> saida(@Valid @RequestBody EstoqueMovimentacaoRequestDTO dto) {
        return ResponseEntity.status(201).body(service.registrarSaida(dto));
    }

    @GetMapping("/saldo/{produtoId}")
    public ResponseEntity<Integer> saldo(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.obterSaldoAtual(produtoId));
    }

    @GetMapping("/historico/{produtoId}")
    public ResponseEntity<List<EstoqueMovimentacaoResponseDTO>> historico(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.historico(produtoId));
    }
}
