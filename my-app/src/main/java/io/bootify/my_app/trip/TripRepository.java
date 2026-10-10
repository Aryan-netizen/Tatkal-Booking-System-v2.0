package io.bootify.my_app.trip;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TripRepository extends JpaRepository<Trip, String> {

    Trip findFirstByTrainNumberNumber(String number);

    boolean existsByIdIgnoreCase(String id);

}
