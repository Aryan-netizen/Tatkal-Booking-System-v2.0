package io.bootify.my_app.station;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StationRepository extends JpaRepository<Station, String> {

    boolean existsByCodeIgnoreCase(String code);

}
