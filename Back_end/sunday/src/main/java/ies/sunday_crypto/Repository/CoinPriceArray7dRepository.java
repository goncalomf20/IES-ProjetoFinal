package ies.sunday_crypto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.sunday_crypto.Models.CoinPricesArray7d;

public interface CoinPriceArray7dRepository extends JpaRepository<CoinPricesArray7d, String> {
    
}
