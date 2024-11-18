package com.rinha.backend.repository;

import com.rinha.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cliente c WHERE c.id = :id")
    Optional<Cliente> findByIdWithLock(Long id);
}