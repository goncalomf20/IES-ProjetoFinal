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
public class CoinPricesArray implements Serializable {

    @Id
    private String coin;

    // Adjusted annotation for serialization
    @Column(columnDefinition = "TEXT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusJson;

    public CoinPricesArray() {
        // Default constructor
    }

    public CoinPricesArray(String coin, List<Object> status) {
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

    public CoinPricesArray(String coin, String statusJson) {
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

    public CoinPricesArray coin(String coin) {
        setCoin(coin);
        return this;
    }

    public CoinPricesArray statusJson(String statusJson) {
        setStatusJson(statusJson);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CoinPricesArray)) {
            return false;
        }
        CoinPricesArray coinPricesArray = (CoinPricesArray) o;
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

