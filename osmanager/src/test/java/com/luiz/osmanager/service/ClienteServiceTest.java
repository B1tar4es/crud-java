package com.luiz.osmanager.service;

import com.luiz.osmanager.dto.ClienteResponse;
import com.luiz.osmanager.dto.ClienteResponse;
import com.luiz.osmanager.exception.ResourceNotFoundException;
import com.luiz.osmanager.model.Cliente;
import com.luiz.osmanager.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    void deveSalvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Luiz");

        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponse response = service.criarCliente(cliente);

        assertNotNull(response);
        assertTrue(response.isCriado());
        assertEquals("Luiz", response.getCliente().getNome());

        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Luiz");

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Luiz", resultado.get().getNome());
    }

    @Test
    void deveRetornarVazioQuandoClienteNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = service.findById(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarCliente() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        existente.setNome("Antigo");
        existente.setEmail("antigo@email.com");

        Cliente atualizado = new Cliente();
        atualizado.setNome("Novo");
        atualizado.setEmail("novo@email.com");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Cliente.class))).thenReturn(existente);

        Cliente resultado = service.atualizarCliente(1L, atualizado);

        assertEquals("Novo", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Cliente atualizado = new Cliente();
        atualizado.setNome("Novo");

        assertThrows(ResourceNotFoundException.class,
                () -> service.atualizarCliente(1L, atualizado));
    }

    @Test
    void deveDeletarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).delete(cliente);

        service.deletarCliente(1L);

        verify(repository, times(1)).delete(cliente);
    }

    @Test
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.deletarCliente(1L));
    }

    @Test
    void deveListarClientes() {
        List<Cliente> lista = new ArrayList<>();

        Cliente cliente = new Cliente();
        cliente.setNome("Luiz");

        lista.add(cliente);

        when(repository.findAll()).thenReturn(lista);

        List<Cliente> resultado = service.listarClientes();

        assertEquals(1, resultado.size());
        assertEquals("Luiz", resultado.get(0).getNome());
    }

    @Test
    void deveVerificarExistenciaDoCliente() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean existe = service.existsById(1L);

        assertTrue(existe);
        verify(repository, times(1)).existsById(1L);
    }
}