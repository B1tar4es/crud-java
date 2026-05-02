package com.luiz.osmanager.controller;

import com.luiz.osmanager.dto.ClienteResponse;
import com.luiz.osmanager.model.Cliente;
import com.luiz.osmanager.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ClienteService service;

        @Test
        void deveListarClientes() throws Exception {

                Cliente cliente = new Cliente();
                cliente.setNome("Luiz");

                when(service.listarClientes()).thenReturn(List.of(cliente));

                mockMvc.perform(get("/clientes"))
                                .andExpect(status().isOk());
        }

        @Test
        void deveCriarCliente() throws Exception {

                Cliente cliente = new Cliente();
                cliente.setNome("Luiz");

                ClienteResponse response = new ClienteResponse(cliente, true);

                when(service.criarCliente(any(Cliente.class))).thenReturn(response);
                mockMvc.perform(post("/clientes")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nome": "Luiz",
                                                  "email": "luiz@email.com"
                                                }
                                                """))
                                .andExpect(status().isOk());
        }

        @Test
        void deveBuscarClientePorId() throws Exception {

                Cliente cliente = new Cliente();
                cliente.setId(1L);
                cliente.setNome("Luiz");

                when(service.findById(1L)).thenReturn(Optional.of(cliente));

                mockMvc.perform(get("/clientes/1"))
                                .andExpect(status().isOk());
        }

        @Test
        void deveRetornarErroQuandoClienteNaoExiste() throws Exception {

                when(service.findById(1L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/clientes/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deveDeletarCliente() throws Exception {

                when(service.existsById(1L)).thenReturn(true);
                doNothing().when(service).deletarCliente(1L);

                mockMvc.perform(delete("/clientes/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void deveRetornar404QuandoClienteNaoExisteNoDelete() throws Exception {

                when(service.existsById(1L)).thenReturn(false);

                mockMvc.perform(delete("/clientes/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deveAtualizarCliente() throws Exception {

                Cliente atualizado = new Cliente();
                atualizado.setNome("Novo Nome");
                atualizado.setEmail("novo@email.com");

                when(service.atualizarCliente(anyLong(), any(Cliente.class)))
                                .thenReturn(atualizado);

                mockMvc.perform(put("/clientes/1")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nome": "Novo Nome",
                                                  "email": "novo@email.com"
                                                }
                                                """))
                                .andExpect(status().isOk());
        }

        @Test
        void deveRetornar400QuandoJsonInvalidoNoCreate() throws Exception {

                mockMvc.perform(post("/clientes")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                                {
                                                  "nome": ""
                                                }
                                                """))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveRetornar404QuandoClienteNaoExisteNoGet() throws Exception {

                when(service.findById(1L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/clientes/1"))
                                .andExpect(status().isNotFound());
        }
}
