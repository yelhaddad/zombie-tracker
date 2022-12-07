package eu.heetch.technicaltest.entrypoint.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.heetch.technicaltest.valueobject.ZombieCaptured;
import eu.heetch.technicaltest.valueobject.ZombieLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class ZombieProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String zombieCapturedTopic;
    private final String zombieLocationsTopic;
    private final ObjectMapper objectMapper;

    public ZombieProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${app.kafka.topics.capturedZombie}") String zombieCapturedTopic,
            @Value("${app.kafka.topics.zombieLocations}") String zombieLocationsTopic,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.zombieCapturedTopic = zombieCapturedTopic;
        this.zombieLocationsTopic = zombieLocationsTopic;
        this.objectMapper = objectMapper;
    }

    // Generate 10000 zombies locations and set 5000 of them to captured
    public void publishZombiesLocations() throws Exception {
        for (int i=0; i < 10000; i++) {
            UUID zombieId = UUID.randomUUID();
            ZombieLocation zombieLocation = ZombieLocation.builder()
                    .id(zombieId)
                    .latitude((Math.random() * 180.0) - 90.0)
                    .longitude((Math.random() * 360.0) - 180.0)
                    .updatedAt(LocalDateTime.now())
                    .build();
            kafkaTemplate.send(zombieLocationsTopic, zombieId.toString(), objectMapper.writeValueAsString(zombieLocation));
            ZombieCaptured zombieCaptured;
            if (i%2 == 0) {
                zombieCaptured = ZombieCaptured.builder()
                        .id(zombieId)
                        .updatedAt(LocalDateTime.now())
                        .build();
            } else {
                zombieCaptured = ZombieCaptured.builder()
                        .id(UUID.randomUUID())
                        .updatedAt(LocalDateTime.now())
                        .build();
            }
            kafkaTemplate.send(zombieCapturedTopic, zombieId.toString(), objectMapper.writeValueAsString(zombieCaptured));
        }
    }
}
