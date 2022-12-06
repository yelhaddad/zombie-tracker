package eu.heetch.technicaltest.entrypoint.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.heetch.technicaltest.service.ZombieService;
import eu.heetch.technicaltest.valueobject.ZombieCaptured;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ZombieCapturedListener {

    private final ZombieService zombieService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.capturedZombie}")
    public void consume(String zombieCaptured, Acknowledgment acknowledgment) {
        try {
            log.info("Start consuming zombie captured");
            zombieService.upsertZombieCaptured(map(zombieCaptured));
        } catch (Exception e) {
            log.error("Zombies captured listener failed : ", e);
            throw new RuntimeException(e);
        }

        acknowledgment.acknowledge();
        log.info("Zombie captured consumed successfully");
    }

    private ZombieCaptured map(String zombieCaptured) {
        try {
            return objectMapper.readValue(zombieCaptured, ZombieCaptured.class);
        } catch (JsonProcessingException e) {
            log.error("Error while mapping zombie captured record", e);
            throw new RuntimeException(e);
        }
    }
}
