package ies.sunday_crypto.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.sunday_crypto.Models.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
   List<Portfolio> findByInvestorId(Long investorId);
   Optional<Portfolio> findByPortfolioKey(String portfolioKey);
}
