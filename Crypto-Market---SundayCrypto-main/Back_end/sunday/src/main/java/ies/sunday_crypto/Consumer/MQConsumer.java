package ies.sunday_crypto.Consumer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ies.sunday_crypto.Configs.MQConfig;
import ies.sunday_crypto.Models.Alert;
import ies.sunday_crypto.Models.CoinPricesArray;
import ies.sunday_crypto.Models.CoinPricesArray7d;
import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Repository.AlertRepository;
import ies.sunday_crypto.Repository.CoinPriceArray7dRepository;
import ies.sunday_crypto.Repository.CryptoRepository;
import ies.sunday_crypto.Repository.InvestorRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import ies.sunday_crypto.Services.CoinPriceArrayService;

@Service
public class MQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MQConsumer.class);

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CoinPriceArrayService coinpricesarrayservice;

    @Autowired
    private CoinPriceArray7dRepository coinPriceArray7dRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private InvestorRepository investorRepository;



    @RabbitListener(queues = MQConfig.QUEUE)
    // @SendTo("/topic/cryptoUpdates")
    public void listen(List<Crypto> all_data, @Header("amqp_receivedRoutingKey") String routingKey)
            throws JsonProcessingException {
        // logger.info("Received routing key: {}", routingKey);

        Instant currentTimestamp = Instant.now();
        long milliseconds = currentTimestamp.toEpochMilli();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTimestamp, ZoneId.systemDefault());


        logger.info( " hours: " + localDateTime.getHour() + " minutes: " + localDateTime.getMinute());
        
        if ("all_cryptos".equals(routingKey)) {

            messagingTemplate.convertAndSend("/topic/cryptoUpdates", all_data);

            for (Crypto crypto : all_data) {

                logger.info("Received Crypto: {}", crypto);

                cryptoRepository.save(crypto);  

                List<Object> novo_elemento = List.of(milliseconds, crypto.getCurrent_price());


                // --------- 7 dias ---------------- por websockets a mandar  price_list7d

                Optional<CoinPricesArray7d> arr7d = coinPriceArray7dRepository.findById(crypto.getId());

                List<Object> price_list7d = arr7d.get().getStatusJson();
                List<Object> last_element7d = (List<Object>) price_list7d.get(price_list7d.size() - 1);
                long last_el_time7d = (long) last_element7d.get(0);
                Instant instant = Instant.ofEpochMilli(last_el_time7d);
                LocalDateTime localDateTime7d = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                logger.info("last_el_time7d: " + last_el_time7d + " hours_last7d: " + localDateTime7d.getHour() + " minutes_last7d: " + localDateTime7d.getMinute());
                
                if (localDateTime7d.getHour() != localDateTime.getHour() && localDateTime7d.getMinute() == 0){
                    logger.info("Mudança de hora");
                    price_list7d.remove(0);
                    price_list7d.add(novo_elemento);
                } else {
                    logger.info("Mesma hora e minutos diferent");
                    price_list7d.set(price_list7d.size() - 1, novo_elemento);
                }

        
                arr7d.get().setStatusJson(price_list7d.toString());
                CoinPricesArray7d cpa = new CoinPricesArray7d(crypto.getId(), price_list7d);
                coinPriceArray7dRepository.save(cpa);




                // --------- 24 h ----------------
               
                Optional<CoinPricesArray> array_24 = coinpricesarrayservice.getArrayByCoin(crypto.getId());
                logger.info("GETTEDDD:" + array_24.get().getCoin() + " novo valor: " + crypto.getCurrent_price()+ " time: "+ milliseconds);

                List<Object> price_list = array_24.get().getStatusJson();
               
                if (localDateTime.getMinute() % 5 == 0 && localDateTime.getMinute() != 0){
                    logger.info("Outros 5 min");
                    price_list.remove(0);
                    price_list.add(novo_elemento);
                } else {
                    logger.info("Mesmos 5 min");
                    price_list.set(price_list.size() - 1, novo_elemento);
                }
                
                array_24.get().setStatusJson(price_list.toString());

                CoinPricesArray arr = new CoinPricesArray(crypto.getId(), price_list);
                coinpricesarrayservice.save(arr);

                List<Object> nome = List.of(crypto.getId());
                List<Object> fullList = List.of(nome, price_list);

                messagingTemplate.convertAndSend("/topic/array" , fullList);

                logger.info("GETTEDDD:" + price_list.size()); 
                

                // --------- Process Alers ----------------

                logger.info("Processing alerts" + crypto.getId());
               
                List<Alert> alerts = alertRepository.findByCoinid(crypto.getId());
                for (Alert alert : alerts) {
                    
                    logger.info("Crypto " + alert.toString() + " " + Double.parseDouble(crypto.getCurrent_price()));

                    double limit_value = alert.getLimit_value();
                    Portfolio p = portfolioRepository.findByPortfolioKey(alert.getPortfolioid()).get();
                    logger.info("Portfolio " + p.getPortfolioKey() + " " + p.getInvestorId() + " " + p.getName());
                    

                    Investor i = investorRepository.findById(p.getInvestorId()).get();

                    // alert.setIsDone(true);
                    // alertRepository.save(alert);
                    // messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price()));
                    
                    if (p.getAssets() == null || alert.getIsDone()){
                        continue;
                    }

                    if (limit_value < alert.getCoinValueWhenAlert()) {
                        logger.info("Vai ter que descer o preço");


                        if (Double.parseDouble(crypto.getCurrent_price()) <= limit_value){
                            Map<Crypto, Double> assets = p.getAssets();
                            logger.info("--> " + assets.toString() );
                            Map<String , Double> coin_qt = new HashMap<>();
                            
                            for (Crypto crypto2 : assets.keySet()) {
                                coin_qt.put(crypto2.getId(), assets.get(crypto2));
                            }

                            logger.info("------- " + coin_qt.toString() + " " + coin_qt.get(crypto.getId()));

                            if (alert.getSell_buy().equals("sell")) {
                                logger.info("Vai ter que vender");

                                if (coin_qt.get(crypto.getId()) < alert.getAmount_to_exange()){
                                    logger.info("Não tem moeda suficiente");
                                    alert.setIsDone(true);
                                    alertRepository.save(alert);

                                    messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price(), "sell"));
                                    continue;
                                }

                                Double currentAmount = coin_qt.get(crypto.getId());
                                logger.info("currentAmount " + currentAmount);
                                logger.info("Nova quantidade " + (currentAmount - alert.getAmount_to_exange()));
                                logger.info("Novo Assets:-----"+assets.toString());
                                String crypto_id = crypto.getId();
                                for (Crypto crypto2 : assets.keySet()) {
                                    if (crypto2.getId().equals(crypto_id)){
                                        logger.info("Encontrou");
                                        logger.info("currentAmount " + currentAmount);
                                        logger.info("Nova quantidade " + (currentAmount - alert.getAmount_to_exange()));
                                        logger.info("Novo Assets:-----"+assets.toString());
                                        assets.put(crypto2, (currentAmount - alert.getAmount_to_exange()));
                                    }
                                }


                                //assets.put(crypto, (currentAmount + alert.getAmount_to_exange()));
                                
                                alert.setIsPossible(true);
                                alertRepository.save(alert);
                                p.setAssets(assets);

                                portfolioRepository.save(p);

                                i.setBalance(i.getBalance() + alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price()));
                                investorRepository.save(i);
                                logger.info("Novo Assets:-----"+p.getAssets().toString());
                            } else {
                                logger.info("Vai ter que comprar");

                                if (i.getBalance() < alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price())){
                                    alert.setIsDone(true);
                                    alertRepository.save(alert);
                                    logger.info("Não tem dinheiro suficiente");

                                    messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price(), "buy"));
                                    continue;
                                }

                                Double currentAmount = coin_qt.get(crypto.getId());
                                logger.info("currentAmount " + currentAmount);
                                logger.info("Nova quantidade " + (currentAmount + alert.getAmount_to_exange()));
                                logger.info("Novo Assets:-----"+assets.toString());
                                String crypto_id = crypto.getId();
                                for (Crypto crypto2 : assets.keySet()) {
                                    if (crypto2.getId().equals(crypto_id)){
                                        logger.info("Encontrou");
                                        logger.info("currentAmount " + currentAmount);
                                        logger.info("Nova quantidade " + (currentAmount + alert.getAmount_to_exange()));
                                        logger.info("Novo Assets:-----"+assets.toString());
                                        assets.put(crypto2, (currentAmount + alert.getAmount_to_exange()));
                                    }
                                }


                                //assets.put(crypto, (currentAmount + alert.getAmount_to_exange()));
                                
                                alert.setIsPossible(true);
                                alertRepository.save(alert);
                                p.setAssets(assets);

                                portfolioRepository.save(p);

                                i.setBalance(i.getBalance() - alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price()));
                                investorRepository.save(i);
                                logger.info("Novo Assets:-----"+p.getAssets().toString());
                                
                            }

                            // alertRepository.deleteById(alert.getAlertID());
                            alert.setIsDone(true);
                            alertRepository.save(alert);

                            messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price()));
                        }
                    } else if (limit_value > alert.getCoinValueWhenAlert()) {
                        logger.info("Vai ter que subir o preço");

                        if (Double.parseDouble(crypto.getCurrent_price()) >= limit_value){
                            Map<Crypto, Double> assets = p.getAssets();

                            logger.info("--> " + assets.toString() );
                            Map<String , Double> coin_qt = new HashMap<>();
                            
                            for (Crypto crypto2 : assets.keySet()) {
                                coin_qt.put(crypto2.getId(), assets.get(crypto2));
                            }

                            logger.info("------- " + coin_qt.toString() + " " + coin_qt.get(crypto.getId()));

                            if (alert.getSell_buy().equals("sell")) {
                                logger.info("Vai ter que vender");

                                if (coin_qt.get(crypto.getId()) < alert.getAmount_to_exange()){
                                    logger.info("Não tem moeda suficiente");
                                    alert.setIsDone(true);
                                    alertRepository.save(alert);

                                    messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price(), "sell"));
                                    continue;
                                }
                                
                                Double currentAmount = coin_qt.get(crypto.getId());
                                logger.info("currentAmount " + currentAmount);
                                logger.info("Nova quantidade " + (currentAmount - alert.getAmount_to_exange()));
                                logger.info("Novo Assets:-----"+assets.toString());
                                String crypto_id = crypto.getId();
                                for (Crypto crypto2 : assets.keySet()) {
                                    if (crypto2.getId().equals(crypto_id)){
                                        logger.info("Encontrou");
                                        logger.info("currentAmount " + currentAmount);
                                        logger.info("Nova quantidade " + (currentAmount - alert.getAmount_to_exange()));
                                        logger.info("Novo Assets:-----"+assets.toString());
                                        assets.put(crypto2, (currentAmount - alert.getAmount_to_exange()));
                                    }
                                }


                                //assets.put(crypto, (currentAmount + alert.getAmount_to_exange()));
                                
                                alert.setIsPossible(true);
                                alertRepository.save(alert);
                                p.setAssets(assets);

                                portfolioRepository.save(p);

                                i.setBalance(i.getBalance() + alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price()));
                                investorRepository.save(i);
                                logger.info("Novo Assets:-----"+p.getAssets().toString());
                            } else {
                                logger.info("Vai ter que comprar");


                                if (i.getBalance() < alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price())){
                                    logger.info("Não tem dinheiro suficiente");
                                    alert.setIsDone(true);
                                    alertRepository.save(alert);

                                    messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price(), "buy"));
                                    continue;
                                }

                                Double currentAmount = coin_qt.get(crypto.getId());
                                logger.info("currentAmount " + currentAmount);
                                logger.info("Nova quantidade " + (currentAmount + alert.getAmount_to_exange()));
                                logger.info("Novo Assets:-----"+assets.toString());
                                String crypto_id = crypto.getId();
                                for (Crypto crypto2 : assets.keySet()) {
                                    if (crypto2.getId().equals(crypto_id)){
                                        logger.info("Encontrou");
                                        logger.info("currentAmount " + currentAmount);
                                        logger.info("Nova quantidade " + (currentAmount + alert.getAmount_to_exange()));
                                        logger.info("Novo Assets:-----"+assets.toString());
                                        assets.put(crypto2, (currentAmount + alert.getAmount_to_exange()));
                                    }
                                }


                                //assets.put(crypto, (currentAmount + alert.getAmount_to_exange()));
                                
                                alert.setIsPossible(true);
                                alertRepository.save(alert);
                                p.setAssets(assets);

                                portfolioRepository.save(p);

                                i.setBalance(i.getBalance() - alert.getAmount_to_exange() * Double.parseDouble(crypto.getCurrent_price()));
                                investorRepository.save(i);
                                logger.info("Novo Assets:-----"+p.getAssets().toString());
                            }

                            // alertRepository.deleteById(alert.getAlertID());
                            alert.setIsDone(true);
                            alertRepository.save(alert);

                            messagingTemplate.convertAndSend("/topic/notification/" + i.getId(), Arrays.asList( alert, crypto.getCurrent_price()));
                        }

                    } else {
                        logger.info("Não vai fazer nada");
                    }

                }
            }
        }
    }


    @RabbitListener(queues = MQConfig.QUEUE_TIMESTAMPS)
    public void listenTime(List<Object> all_data, @Header("amqp_receivedRoutingKey") String routingKey)
            throws JsonProcessingException {
        try {
            logger.info("Received times routing key: {}", routingKey);

            List<Object> list_name = (List<Object>) all_data.get(all_data.size() - 1);
            String msg_stat = (String) list_name.get(0);
            String[] parts = msg_stat.split("\\_");
            String coin_name = parts[0];
            String type = parts[1];
            logger.info("type  " + type);
            logger.info("all-Data " + all_data.toString());
            all_data.remove(all_data.size() - 1);

            if (type.equals("24")){
                

                CoinPricesArray c = new CoinPricesArray(coin_name, all_data);
                coinpricesarrayservice.save(c);
                logger.info("Data stored in cache");

            } else if (type.equals("7")){
                CoinPricesArray7d c = new CoinPricesArray7d(coin_name, all_data);
                coinPriceArray7dRepository.save(c);
                logger.info("Data stored in sql");
            } else {
                logger.info("Type not valid");
            }

           
        } catch (Exception e) {
            logger.error("Exception in listenTime method", e);
        }
    }
}