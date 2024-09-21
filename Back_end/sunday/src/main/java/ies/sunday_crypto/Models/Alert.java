
package ies.sunday_crypto.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AlertID;

    @Column(nullable = false, length = 64)
    private String portfolioid;

    @Column(nullable = false, length = 64)
    private String coinid;

     @Column(nullable = false, length = 64)
    private double amount_to_exange;

    @Column(nullable = false, length = 64)
    private double limit_value;

    @Column(nullable = false, length = 64)
    private String sell_buy;

    @Column(nullable = false, length = 64)
    private double coinValueWhenAlert;

    @Column(nullable = false, length = 64)
    private Boolean isDone;

    @Column(nullable = false, length = 64)
    private Boolean IsPossible;

    public Boolean getIsPossible() {
        return IsPossible;
    }

    public void setIsPossible(Boolean isPossible) {
        IsPossible = isPossible;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public double getCoinValueWhenAlert() {
        return coinValueWhenAlert;
    }

    public void setCoinValueWhenAlert(double coinValueWhenAlert) {
        this.coinValueWhenAlert = coinValueWhenAlert;
    }

    public int getAlertID() {
        return AlertID;
    }

    public void setAlertID(int alertID) {
        AlertID = alertID;
    }

    public String getPortfolioid() {
        return portfolioid;
    }

    public void setPortfolioid(String portfolioid) {
        this.portfolioid = portfolioid;
    }

    public String getCoinid() {
        return coinid;
    }

    public void setCoinid(String coinid) {
        this.coinid = coinid;
    }

    public double getAmount_to_exange() {
        return amount_to_exange;
    }

    public void setAmount_to_exange(double d) {
        this.amount_to_exange = d;
    }

    public double getLimit_value() {
        return limit_value;
    }

    public void setLimit_value(double d) {
        this.limit_value = d;
    }

    public String getSell_buy() {
        return sell_buy;
    }

    public void setSell_buy(String sell_buy) {
        this.sell_buy = sell_buy;
    }

    @Override
    public String toString() {
        return "Alert [AlertID=" + AlertID + ", portfolioid=" + portfolioid + ", coinid=" + coinid
                + ", amount_to_exange=" + amount_to_exange + ", limit_value=" + limit_value + ", sell_buy=" + sell_buy
                + ", coinValueWhenAlert=" + coinValueWhenAlert + "]";
    }


    
   
}