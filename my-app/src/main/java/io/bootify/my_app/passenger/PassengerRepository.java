package io.bootify.my_app.passenger;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PassengerRepository extends JpaRepository<Passenger, String> {

    Passenger findFirstByBookingId(String id);

    boolean existsByIdIgnoreCase(String id);

}
