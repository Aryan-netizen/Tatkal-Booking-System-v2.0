package io.bootify.my_app.booking_event;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingEventRepository extends JpaRepository<BookingEvent, String> {

    BookingEvent findFirstByBookingId(String id);

    boolean existsByIdIgnoreCase(String id);

}
