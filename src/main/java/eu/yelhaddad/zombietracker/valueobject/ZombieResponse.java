package eu.yelhaddad.zombietracker.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZombieResponse {
    private UUID zombie_id;
    private Double longitude;
    private Double latitude;
}
