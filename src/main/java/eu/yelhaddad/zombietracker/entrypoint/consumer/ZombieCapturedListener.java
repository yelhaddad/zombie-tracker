package eu.yelhaddad.zombietracker.entrypoint.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.yelhaddad.zombietracker.service.ZombieService;
import eu.yelhaddad.zombietracker.valueobject.ZombieCaptured;
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
    public void consume(String zombieCapturedRecord, Acknowledgment acknowledgment) {
        log.info("Start consuming zombie captured");
        ZombieCaptured zombieCaptured = map(zombieCapturedRecord);
        try {
            log.info("Consuming zombie captured id : {}", zombieCaptured.getId());
            zombieService.upsertZombieCaptured(zombieCaptured);
        } catch (Exception e) {
            log.error("Zombies captured listener failed : ", e);
            throw new RuntimeException(e);
        }

        acknowledgment.acknowledge();
        log.info("Zombie captured id : {} consumed successfully", zombieCaptured.getId());
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
