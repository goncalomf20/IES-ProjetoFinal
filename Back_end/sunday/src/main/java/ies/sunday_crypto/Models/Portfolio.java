package ies.sunday_crypto.Models;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "portfolio2")
public class Portfolio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false , length = 64, unique = true)
  private String portfolioKey;

  @Column(nullable = true)
  private Long investorId;

  @Column(nullable = true)
  private String name = "Default";

  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "crypto_name")
  @Column(name = "assets")
  private Map<Crypto, Double> assets = new HashMap<>();

  public void setPortfolioKey(String portfolioKey) {
      this.portfolioKey = portfolioKey;
  }

  public String getPortfolioKey() {
      return portfolioKey;
  }


  public void setAssets(Map<Crypto, Double> assets) {
      this.assets = assets;
  }

  public Map<Crypto, Double> getAssets() {
      return assets;
  }
    
  public String getName() {
        return name;
    
  }

  public void setName(String name) {
      this.name = name;
  }

  public Long getInvestorId() {
      return investorId;
  }

  public void setIdInvestor(Long IdInvestor) {
      investorId = IdInvestor;
  }

}
