import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class HelloWorld {

    public static void main(String[] args) throws JsonProcessingException {

        String topicName = "products";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:53663");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer
                <>(props);

         ObjectMapper objectMapper = new ObjectMapper();


        for(int i = 0; i < 1000; i++)
            producer.send(new ProducerRecord<>(topicName,
                    Integer.toString(i), objectMapper.writeValueAsString(getRandomProduct())));
        System.out.println("Messages sent successfully");
        producer.close();
    }

    private static Product getRandomProduct() {
        Faker faker = new Faker(new Locale("en-US"));
        LocalDateTime now = LocalDateTime.now();

        Product p1 = new Product(UUID.randomUUID().toString().replace("-", ""),
                faker.name().title(), faker.vehicle().carType(), faker.yoda().quote(), now.toString());
        System.out.println(p1);
        return p1;
    }
}