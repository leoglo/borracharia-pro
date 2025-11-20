package com.borracharia.borracharia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.borracharia.borracharia.dto.Request.OrdemServicoRequestDTO;
import com.borracharia.borracharia.dto.Response.OrdemServicoResponseDTO;
import com.borracharia.borracharia.service.OrdemServicoService;

@RestController
@RequestMapping("/api/ordens-servico")
@CrossOrigin(origins = "*")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> criar(@RequestBody OrdemServicoRequestDTO dto) {
        OrdemServicoResponseDTO criada = service.criarOrdem(dto);
        return ResponseEntity.ok(criada);
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarOrdens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarOrdemPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody OrdemServicoRequestDTO dto) {

        return ResponseEntity.ok(service.atualizarOrdem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarOrdem(id);
        return ResponseEntity.noContent().build();
    }
}
