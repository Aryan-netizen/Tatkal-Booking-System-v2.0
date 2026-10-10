package io.bootify.my_app.trip_class;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TripClassRepository extends JpaRepository<TripClass, Long> {

    TripClass findFirstByTripId(String id);

    TripClass findFirstByClassCodeCode(String code);

}
