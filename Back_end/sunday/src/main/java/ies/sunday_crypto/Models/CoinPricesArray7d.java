package ies.sunday_crypto.Models;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CoinPricesArray7d implements Serializable {
    
    @Id
    private String coin;

    @Column(columnDefinition = "TEXT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusJson;


    public CoinPricesArray7d() {
        // Default constructor
    }

    public CoinPricesArray7d(String coin, List<Object> status) {
        this.coin = coin;
        // Serialize the list to a JSON string
        this.statusJson = convertListToJson(status);
    }

    private String convertListToJson(List<Object> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            // Handle the exception according to your needs
            e.printStackTrace();
            return null;
        }
    }

    public CoinPricesArray7d(String coin, String statusJson) {
        this.coin = coin;
        this.statusJson = statusJson;
    }

    public String getCoin() {
        return this.coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public List<Object> getStatusJson() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(this.statusJson, new TypeReference<List<Object>>() {});
        } catch (IOException e) {
            // Handle the exception according to your needs
            e.printStackTrace();
            return null;
        }
    }

    public void setStatusJson(String statusJson) {
        this.statusJson = statusJson;
    }

    public CoinPricesArray7d coin(String coin) {
        setCoin(coin);
        return this;
    }

    public CoinPricesArray7d statusJson(String statusJson) {
        setStatusJson(statusJson);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CoinPricesArray7d)) {
            return false;
        }
        CoinPricesArray7d coinPricesArray = (CoinPricesArray7d) o;
        return Objects.equals(coin, coinPricesArray.coin) && Objects.equals(statusJson, coinPricesArray.statusJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coin, statusJson);
    }

    @Override
    public String toString() {
        return "{" +
            " coin='" + getCoin() + "'" +
            ", Array='" + getStatusJson() + "'" +
            "}";
    }
}
