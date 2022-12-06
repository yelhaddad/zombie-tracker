package eu.heetch.technicaltest.repository;

import eu.heetch.technicaltest.model.Zombie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ZombieRepository extends CrudRepository<Zombie, UUID> {

    @Query(value = """
                SELECT id,
                       latitude,
                       longitude,
                       captured,
                       updated_at
                FROM zombie
                where captured = false
                order by
                ST_DistanceSphere(
                           ST_MakePoint(longitude,latitude),
                           ST_MakePoint(:longitude,:latitude)
                ) ASC
                LIMIT :limit
               """, nativeQuery = true)
    List<Zombie> getNotCapturedZombiesNearTo(Double longitude, Double latitude, int limit);
}
