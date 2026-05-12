package com.luiz.osmanager.service;

import com.luiz.osmanager.dto.ClienteResponse;
import com.luiz.osmanager.exception.ResourceNotFoundException;
import com.luiz.osmanager.model.Cliente;
import com.luiz.osmanager.repository.ClienteRepository;
import com.luiz.osmanager.util.CpfValidator;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    public ClienteResponse criarCliente(Cliente cliente) {

        if (!CpfValidator.isValid(cliente.getCpf())) {
            throw new RuntimeException("CPF inválido");
        }

        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(cliente.getEmail());

        return clienteRepository.findByEmail(cliente.getEmail())
                .map(existente -> {
                    existente.setNome(cliente.getNome());
                    existente.setEmail(cliente.getEmail());
                    Cliente atualizado = clienteRepository.save(existente);
                    return new ClienteResponse(atualizado, false); // atualização
                })
                .orElseGet(() -> {
                    Cliente novo = clienteRepository.save(cliente);
                    return new ClienteResponse(novo, true); // criação
                });
    }

    public void deletarCliente(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        clienteRepository.delete(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());

        return clienteRepository.save(cliente);
    }
}