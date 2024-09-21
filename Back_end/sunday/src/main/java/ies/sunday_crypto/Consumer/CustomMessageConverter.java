package ies.sunday_crypto.Consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

    public CustomMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            return new Message(jsonString.getBytes(), messageProperties);
        } catch (IOException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    @Override
    public Object fromMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), Object.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }
}
