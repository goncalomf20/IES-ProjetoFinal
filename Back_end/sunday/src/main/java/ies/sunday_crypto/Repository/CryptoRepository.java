package ies.sunday_crypto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.sunday_crypto.Models.Crypto;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, String> {
   
}
