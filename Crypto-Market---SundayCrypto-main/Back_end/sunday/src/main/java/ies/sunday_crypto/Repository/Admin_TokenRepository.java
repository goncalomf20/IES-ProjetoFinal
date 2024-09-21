package ies.sunday_crypto.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.sunday_crypto.Models.Admin_Token;

public interface Admin_TokenRepository extends JpaRepository<Admin_Token, Long> {
    Optional<Admin_Token> findByTkn(String tkn);
    
}
