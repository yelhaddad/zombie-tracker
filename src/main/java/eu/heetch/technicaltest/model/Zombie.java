package eu.heetch.technicaltest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Zombie {

    @Id
    private UUID id;
    private Double longitude;
    private Double latitude;
    private boolean captured;
    private LocalDateTime updatedAt;
}
