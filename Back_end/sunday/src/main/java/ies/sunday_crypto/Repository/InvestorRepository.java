package ies.sunday_crypto.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.sunday_crypto.Models.Investor;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
    Investor findByUsername(String username); 
    Optional<Investor> findByToken(String token);
    Optional<Investor> findById(Long Id);
}
