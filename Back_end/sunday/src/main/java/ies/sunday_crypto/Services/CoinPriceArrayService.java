package ies.sunday_crypto.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ies.sunday_crypto.Models.CoinPricesArray;
import ies.sunday_crypto.Repository.CoinPricesArrayRepository;

@Service
public class CoinPriceArrayService {

  @Autowired
  CoinPricesArrayRepository repository;

  @Cacheable(value = "coin", key = "#id")
  public Optional<CoinPricesArray> getArrayByCoin(String id) {
    return repository.findById(id);
  }
 
  @CachePut(cacheNames = "coin", key = "#coin.id")
  public CoinPricesArray updateCoin(CoinPricesArray coinArray) {
    return repository.save(coinArray);
  }

  public void save(CoinPricesArray coinArray) {
    repository.save(coinArray);
  }

  public List<CoinPricesArray> getAllCoinsArrayTime() {
    return (List<CoinPricesArray>) repository.findAll();
  }
}