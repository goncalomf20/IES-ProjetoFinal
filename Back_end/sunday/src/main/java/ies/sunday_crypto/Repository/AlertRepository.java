package ies.sunday_crypto.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.sunday_crypto.Models.Alert;


@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
    List<Alert> findByCoinid(String coinid);

    List<Alert> findByPortfolioid(String portfolioKey);
}

