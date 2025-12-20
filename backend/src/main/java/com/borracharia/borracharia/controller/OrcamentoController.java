package com.borracharia.borracharia.controller;

import com.borracharia.borracharia.dto.Request.OrcamentoRequestDTO;
import com.borracharia.borracharia.dto.Response.OrcamentoResponseDTO;
import com.borracharia.borracharia.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> criar(@RequestBody OrcamentoRequestDTO dto) {
        try {
            OrcamentoResponseDTO orcamento = orcamentoService.criarOrcamento(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody OrcamentoRequestDTO dto) {
        try {
            OrcamentoResponseDTO orcamento = orcamentoService.atualizarOrcamento(id, dto);
            return ResponseEntity.ok(orcamento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            OrcamentoResponseDTO orcamento = orcamentoService.buscarPorId(id);
            return ResponseEntity.ok(orcamento);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listarTodos() {
        List<OrcamentoResponseDTO> orcamentos = orcamentoService.listarTodos();
        return ResponseEntity.ok(orcamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            orcamentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<OrcamentoResponseDTO>> buscarPorTermo(
            @RequestParam("termo") String termo) {
        try {
            List<OrcamentoResponseDTO> orcamentos = orcamentoService.buscarPorTermo(termo);
            return ResponseEntity.ok(orcamentos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}