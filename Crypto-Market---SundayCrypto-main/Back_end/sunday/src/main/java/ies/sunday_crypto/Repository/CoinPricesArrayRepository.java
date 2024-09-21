package ies.sunday_crypto.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ies.sunday_crypto.Models.CoinPricesArray;

@Repository
public interface CoinPricesArrayRepository extends CrudRepository<CoinPricesArray, String> {
}