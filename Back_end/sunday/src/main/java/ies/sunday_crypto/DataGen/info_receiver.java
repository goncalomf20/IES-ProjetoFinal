package ies.sunday_crypto.DataGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;

@Service
public class info_receiver {


    private static final Logger logger = LoggerFactory.getLogger(info_receiver.class);

    private String coingeckoApiUrl = "https://api.coingecko.com/api/v3";

     // private List coingeckoApiSupply = List.of("CG-VGkTi8SVLMpFH4YQ4hkjhGTb","CG-sEtHA73123jceRpFZxVYFQx1");

    private String coingeckoApiKey = "CG-NZarYrYZmRvh7cbBDDCoxpdj";

    private String coingeckoApiKey2 = "CG-njvjDeTQsbpe6spL41na3mfC";

    private String rabbitmqHost = "rabbitmq";

    private int rabbitmqPort = 15672;

    private String rabbitmqUsername = "guest" ;

    private String rabbitmqPassword = "guest";

    private String rabbitmqQueue = "sunday_crypto";

    @EventListener(ContextRefreshedEvent.class)
    @Async
    public void onApplicationEvent() throws InterruptedException {
        System.out.println("Starting InfoReceiverService");

        List<Map<String, Object>> coingeckoData_inicial = getCoingeckoData("coins/markets?vs_currency=eur&order=market_cap_rank_desc&per_page=15&page=1&sparkline=false&locale=en&precision=4&x_cg_demo_api_key="+coingeckoApiKey);

        for(Map<String, Object> crypto : coingeckoData_inicial){
            String coin_id = crypto.get("id").toString();
            logger.info(coin_id);
            Map<String, Object> timestamps_crypto24 = getCoingeckoTimeStamps("coins/"+coin_id+"/market_chart?vs_currency=eur&days=1&precision=4&x_cg_demo_api_key="+coingeckoApiKey);

            // logger.info(timestamps_crypto24.toString());
            Boolean t24 = sendTimestampstoRabbitMQ(timestamps_crypto24,"crypto_timestamps_routing_key", coin_id + "_24");

            Map<String, Object> timestamps_crypto7 = getCoingeckoTimeStamps("coins/"+coin_id+"/market_chart?vs_currency=eur&days=7&precision=4&x_cg_demo_api_key="+coingeckoApiKey2);

            // logger.info("7 dias horas" + timestamps_crypto7.toString());
            Boolean t7 = sendTimestampstoRabbitMQ(timestamps_crypto7,"crypto_timestamps_routing_key", coin_id + "_7");



                // if (t24){
                //     try {
                //         TimeUnit.MILLISECONDS.sleep(300);
                //     } catch (InterruptedException e) {
                //         e.printStackTrace();
                //     }
                // }

            TimeUnit.MILLISECONDS.sleep(200);
        }
        
        
        while (true) {

            List<Map<String, Object>> coingeckoData = getCoingeckoData("coins/markets?vs_currency=eur&order=market_cap_rank_desc&per_page=15&page=1&sparkline=false&locale=en&precision=4&x_cg_demo_api_key="+coingeckoApiKey);

            if (coingeckoData != null) {
                logger.info("Received data from Coingecko API");
                //logger.info(coingeckoData.toString());
                Boolean t = sendToRabbitMQ(coingeckoData,"all_cryptos");
                if (t){
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                logger.error("Error while receiving data from Coingecko API");
            }
        }
    }


    private List<Map<String, Object>> getCoingeckoData(String specificUrl) {
        String url = coingeckoApiUrl + "/" + specificUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Apikey " + coingeckoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Define a ParameterizedTypeReference for the response type (list of maps)
        ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<List<Map<String, Object>>>() {
        };

        // Make the API request using exchange method
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        // Retrieve the body of the response (list of maps)
        List<Map<String, Object>> responseBody = response.getBody();

        return responseBody;
    }

    private Map<String, Object> getCoingeckoTimeStamps(String specificUrl) {
        String url = coingeckoApiUrl + "/" + specificUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Apikey " + coingeckoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // Define a ParameterizedTypeReference for the response type (list of maps)
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        // Make the API request using exchange method
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        // Retrieve the body of the response (list of maps)
        Map<String, Object> responseBody = response.getBody();

        return responseBody;
    }

    private Boolean sendToRabbitMQ(List<Map<String, Object>> data, String routingKey) {
        logger.info(rabbitmqHost + " " + rabbitmqPort + " " + rabbitmqUsername + " " + rabbitmqPassword + " " + rabbitmqQueue);
    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        factory.setUsername(rabbitmqUsername);
    
        //factory.setPassword(rabbitmqPassword);
        //factory.setPort(rabbitmqPort);
    
        logger.info("Sending data to RabbitMQ");
    
        try (Connection connection = factory.newConnection();) {
    
            logger.info("Connection created");
            logger.info(data.get(0).toString());
            Channel channel = connection.createChannel();
            logger.info("Channel created");     
            String exchangeName = "sunday_crypto";
            String exchangeType = "topic";
    
            channel.exchangeDeclare(exchangeName, exchangeType, true);

            ObjectMapper objectMapper = new ObjectMapper();
            // Convert the map to a JSON string

            List<String> send = new ArrayList<>();
            for (Map<String, Object> coin : data) {
                String json = objectMapper.writeValueAsString(coin);
                send.add(json);
            }
            

            // Convert the map to a JSON string
            channel.basicPublish(exchangeName, routingKey, null, send.toString().getBytes());
    
            System.out.println(" [x] Sent data to RabbitMQ: "+ send.toString());
    
            return true; // Return true if data is sent successfully
    
        } catch (IOException | TimeoutException e) {
            if (e.getCause() instanceof ShutdownSignalException) {
                ShutdownSignalException sse = (ShutdownSignalException) e.getCause();
                System.err.println("Shutdown signal received: " + sse.getReason());
            }
            System.err.println("Exception while sending data to RabbitMQ:");
            e.printStackTrace();
    
            return false; // Return false if an exception occurs
        }
    }

    private Boolean sendTimestampstoRabbitMQ(Map<String, Object> data, String routingKey, String coinid) {
        logger.info(rabbitmqHost + " " + rabbitmqPort + " " + rabbitmqUsername + " " + rabbitmqPassword + " " + "crypto_timestamps");
    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        factory.setUsername(rabbitmqUsername);
    
        //factory.setPassword(rabbitmqPassword);
        //factory.setPort(rabbitmqPort);
    
        logger.info("Sending data to RabbitMQ"+coinid);
    
        try (Connection connection = factory.newConnection();) {
    
            logger.info("Connection created");
            Channel channel = connection.createChannel();
            logger.info("Channel created");     
            String exchangeName = "crypto_timestamps";
            String exchangeType = "topic";
    
            channel.exchangeDeclare(exchangeName, exchangeType, true);

            ObjectMapper objectMapper = new ObjectMapper();
            // Convert the map to a JSON string

            List<List<Object>> priceValue = (List<List<Object>>) data.get("prices");
            List<Object> nova = List.of(coinid);
            priceValue.add(nova);
            
            String json = objectMapper.writeValueAsString(priceValue);
            

            // Convert the map to a JSON string
            channel.basicPublish(exchangeName, routingKey, null, json.toString().getBytes());
    
            System.out.println(" [x] Sent timestamps data to RabbitMQ: "+ json.toString());
    
            return true; // Return true if data is sent successfully
    
        } catch (IOException | TimeoutException e) {
            if (e.getCause() instanceof ShutdownSignalException) {
                ShutdownSignalException sse = (ShutdownSignalException) e.getCause();
                System.err.println("Shutdown signal received: " + sse.getReason());
            }
            System.err.println("Exception while sending data to RabbitMQ:");
            e.printStackTrace();
    
            return false; // Return false if an exception occurs
        }
    }
    
}
