package com.luiz.osmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.luiz.osmanager.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}