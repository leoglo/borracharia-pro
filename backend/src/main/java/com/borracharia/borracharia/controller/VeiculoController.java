package com.borracharia.borracharia.controller;

import com.borracharia.borracharia.model.Cliente;
import com.borracharia.borracharia.model.Veiculo;
import com.borracharia.borracharia.repository.ClienteRepository;
import com.borracharia.borracharia.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Veiculo> buscarPorCliente(@PathVariable Long clienteId) {
        return veiculoRepository.findByClienteId(clienteId);
    }

    @PostMapping
    public ResponseEntity<Veiculo> criar(@RequestBody Veiculo veiculo) {
        if (veiculo.getCliente() != null && veiculo.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(veiculo.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            veiculo.setCliente(cliente);
        }
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        return ResponseEntity.ok(veiculoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculoAtualizado) {
        return veiculoRepository.findById(id)
                .map(veiculo -> {
                    veiculo.setPlaca(veiculoAtualizado.getPlaca());
                    veiculo.setModelo(veiculoAtualizado.getModelo());
                    veiculo.setMarca(veiculoAtualizado.getMarca());
                    veiculo.setAno(veiculoAtualizado.getAno());
                    veiculo.setCor(veiculoAtualizado.getCor());
                    veiculo.setObservacoes(veiculoAtualizado.getObservacoes());
                    
                    if (veiculoAtualizado.getCliente() != null && veiculoAtualizado.getCliente().getId() != null) {
                        Cliente cliente = clienteRepository.findById(veiculoAtualizado.getCliente().getId())
                                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                        veiculo.setCliente(cliente);
                    }
                    
                    return ResponseEntity.ok(veiculoRepository.save(veiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<Void> deletarPorCliente(@PathVariable Long clienteId) {
        veiculoRepository.deleteByClienteId(clienteId);
        return ResponseEntity.noContent().build();
    }
}