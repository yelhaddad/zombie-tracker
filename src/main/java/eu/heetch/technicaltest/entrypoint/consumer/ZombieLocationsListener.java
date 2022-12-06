package eu.heetch.technicaltest.entrypoint.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.heetch.technicaltest.service.ZombieService;
import eu.heetch.technicaltest.valueobject.ZombieLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ZombieLocationsListener {

    private final ZombieService zombieService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.zombieLocations}", batch = "true")
    public void consume(List<String> zombieLocationRecords, Acknowledgment acknowledgment) {
        try {
            if (!zombieLocationRecords.isEmpty()) {
                log.info("Start consuming zombies locations, size : {}", zombieLocationRecords.size());
                zombieService.upsertZombieLocations(mapRecords(zombieLocationRecords));
            }
        } catch (Exception e) {
            log.error("Zombie locations listener failed", e);
            throw new RuntimeException(e);
        }

        acknowledgment.acknowledge();
        log.info("Zombies locations consumed successfully : {}", zombieLocationRecords.size());
    }

    List<ZombieLocation> mapRecords(List<String> zombieLocationRecords) {
        List<ZombieLocation> zombieLocations = new ArrayList<>();
        zombieLocationRecords.forEach(zombieLocationRecord -> {
            try {
                zombieLocations.add(objectMapper.readValue(zombieLocationRecord, ZombieLocation.class));
            } catch (JsonProcessingException e) {
                log.error("Error while mapping zombie locations records", e);
                throw new RuntimeException(e);
            }
        });
        return zombieLocations;
    }
}
