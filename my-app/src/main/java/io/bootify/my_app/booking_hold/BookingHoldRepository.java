package io.bootify.my_app.booking_hold;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingHoldRepository extends JpaRepository<BookingHold, Long> {

    BookingHold findFirstByBookingId(String id);

}
