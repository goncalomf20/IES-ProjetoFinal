package ies.sunday_crypto.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coins")
public class Crypto {

    @Override
    public String toString() {
        return "Crypto [id=" + id + ", symbol=" + symbol + ", name=" + name + ", image=" + image + ", current_price="
                + current_price + ", market_cap=" + market_cap + ", market_cap_rank=" + market_cap_rank
                + ", total_volume=" + total_volume + ", low_24h=" + low_24h + ", hight_24h" + high_24h 
                + ", price_change_24h=" + price_change_24h + ", price_change_percentage_24h="
                + price_change_percentage_24h + ", market_cap_change_24h=" + market_cap_change_24h
                + ", market_cap_change_percentage_24h=" + market_cap_change_percentage_24h + ", total_supply="
                + total_supply + ", max_supply=" + max_supply + "]";
    }

    @Id
    private String id;

    
    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String current_price;

    @Column(nullable = false)
    private long market_cap;

    @Column(nullable = false)
    private int market_cap_rank;

    @Column(nullable = false)
    private long total_volume;

    @Column(nullable = false)
    private double high_24h;

    @Column(nullable = false)
    private double low_24h;

    @Column(nullable = false)
    private double price_change_24h;

    @Column(nullable = false)
    private double price_change_percentage_24h;

    @Column(nullable = false)
    private double market_cap_change_24h;

    @Column(nullable = false)
    private double market_cap_change_percentage_24h;

    @Column(nullable = false)
    private double total_supply;

    @Column(nullable = false)
    private double max_supply;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(long market_cap) {
        this.market_cap = market_cap;
    }

    public int getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public long getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(long total_volume) {
        this.total_volume = total_volume;
    }

    public double getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(double high_24h) {
        this.high_24h = high_24h;
    }

    public double getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(double low_24h) {
        this.low_24h = low_24h;
    }

    public double getPrice_change_24h() {
        return price_change_24h;
    }

    public void setPrice_change_24h(double price_change_24h) {
        this.price_change_24h = price_change_24h;
    }

    public double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public double getMarket_cap_change_24h() {
        return market_cap_change_24h;
    }

    public void setMarket_cap_change_24h(double market_cap_change_24h) {
        this.market_cap_change_24h = market_cap_change_24h;
    }

    public double getMarket_cap_change_percentage_24h() {
        return market_cap_change_percentage_24h;
    }

    public void setMarket_cap_change_percentage_24h(double market_cap_change_percentage_24h) {
        this.market_cap_change_percentage_24h = market_cap_change_percentage_24h;
    }

    public double getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(double total_supply) {
        this.total_supply = total_supply;
    }

    public double getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(double max_supply) {
        this.max_supply = max_supply;
    }

  

    // Add more fields as needed

    

    // Add getter and setter methods for other fields

  
}
