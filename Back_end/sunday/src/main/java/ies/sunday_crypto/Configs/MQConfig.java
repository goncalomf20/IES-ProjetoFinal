package ies.sunday_crypto.Configs;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import ies.sunday_crypto.Consumer.MQConsumer;


@Configuration
@Service
public class MQConfig {

    public static final String QUEUE = "sunday_crypto";
    public static final String QUEUE_TIMESTAMPS = "crypto_timestamps";
    public static final String EXCHANGE = "sunday_crypto";
    public static final String EXCHANGE_TIMESTAMPS = "crypto_timestamps";
    public static final String ROUTING_KEY = "all_cryptos";
    public static final String ROUTING_KEY_TIMESTAMPS = "crypto_timestamps_routing_key";
    

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public Queue queue_timestamps(){
        return new Queue(QUEUE_TIMESTAMPS);
    }

    
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public TopicExchange exchange_timestamps(){
        return new TopicExchange(EXCHANGE_TIMESTAMPS);
    }

    @Bean
    public Binding binding(Queue queue , TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    
    }

    @Bean
    public Binding binding_timestamps(Queue queue_timestamps , TopicExchange exchange_timestamps){
        return BindingBuilder.bind(queue_timestamps).to(exchange_timestamps).with(ROUTING_KEY_TIMESTAMPS);
    
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public MQConsumer consumer(){
        return new MQConsumer();
    }


}
