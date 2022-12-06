package eu.heetch.technicaltest.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZombieLocation {
    private UUID id;
    private Double longitude;
    private Double latitude;
    private LocalDateTime updatedAt;
}
