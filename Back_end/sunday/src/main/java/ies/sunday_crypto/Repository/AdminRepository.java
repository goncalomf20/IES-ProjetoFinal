package ies.sunday_crypto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.sunday_crypto.Models.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByToken(String token);

    Admin findByUsername(String username);

    Optional<Admin> findById(Long id);

    
}