package com.luiz.osmanager.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void deveCriarExceptionComMensagem() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Cliente não encontrado");

        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("erro");
        });
    }
}