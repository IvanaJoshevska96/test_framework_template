package main.java.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class KafkaProducer {
    public static String produceMessageToKafkaTopic(String jsonFilePath, MessageType messageType, String partitionKey) {
        Properties producerProperties = new Properties();
        try {
            producerProperties.load(Files.newBufferedReader(Paths.get("producer_properties.configuration")));
            producerProperties = prepareProperties(producerProperties);
        } catch (IOException e) {
            System.err.println("Error loading Kafka producer properties:");
            e.printStackTrace();
            return null;
        }

        try (Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(producerProperties)) {

            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            ((ObjectNode) rootNode).put("uniqueId", String.valueOf(System.currentTimeMillis()));

            String updatedJsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

            ProducerRecord<String, String> record = new ProducerRecord<>(messageType.getTopicName(), partitionKey, updatedJsonContent);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.printf("Message sent to topic: %s, partition: %d, offset: %d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    System.err.println("Error sending message:");
                    exception.printStackTrace();
                }
            });

            return String.valueOf(System.currentTimeMillis());
        } catch (IOException e) {
            System.err.println("Error reading JSON file:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error producing message:");
            e.printStackTrace();
        }
        return null;
    }

    private static Properties prepareProperties(Properties props) {
        Properties properties = (Properties) props.clone();
        return properties;
    }
}
