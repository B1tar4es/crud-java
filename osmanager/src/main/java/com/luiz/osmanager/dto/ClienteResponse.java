package com.luiz.osmanager.dto;

import com.luiz.osmanager.model.Cliente;

public class ClienteResponse {

    private Cliente cliente;
    private boolean criado;

    public ClienteResponse(Cliente cliente, boolean criado) {
        this.cliente = cliente;
        this.criado = criado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isCriado() {
        return criado;
    }
}