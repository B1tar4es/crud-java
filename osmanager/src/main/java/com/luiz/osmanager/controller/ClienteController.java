package com.luiz.osmanager.controller;

import org.springframework.web.bind.annotation.*;
import com.luiz.osmanager.model.Cliente;
import com.luiz.osmanager.service.ClienteService;
import com.luiz.osmanager.dto.ClienteResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // LISTAR TODOS
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {

        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CRIAR CLIENTE
    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {

     ClienteResponse response = clienteService.criarCliente(cliente);

        if (response.isCriado()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response.getCliente());
        } else {
            return ResponseEntity.ok(response.getCliente());
        }
    }

    // DELETAR CLIENTE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {

        if (!clienteService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    // ATUALIZAR CLIENTE
    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        return clienteService.atualizarCliente(id, clienteAtualizado);
    }
}