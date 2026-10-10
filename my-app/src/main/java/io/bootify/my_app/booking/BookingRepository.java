package io.bootify.my_app.booking;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, String> {

    Booking findFirstByTripId(String id);

    Booking findFirstByTrainNumberSeq(String seq);

    Booking findFirstByQuotaCode(String code);

    boolean existsByIdIgnoreCase(String id);

    boolean existsByPnrIgnoreCase(String pnr);

}
