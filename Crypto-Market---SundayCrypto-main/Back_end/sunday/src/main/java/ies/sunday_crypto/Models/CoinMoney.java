package ies.sunday_crypto.Models;

public class CoinMoney {
   private Crypto Coin;

   private Double money;


   public Crypto getCoin() {
       return Coin;
   }

   public Double getMoney() {

       return money;
   }

   public void setCoin(Crypto coin) {
       Coin = coin;
   }
  
   public void setMoney(Double money) {

       this.money = money;
   }
   
}
