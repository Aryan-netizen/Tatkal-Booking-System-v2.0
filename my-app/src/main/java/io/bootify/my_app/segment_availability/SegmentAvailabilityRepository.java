package io.bootify.my_app.segment_availability;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SegmentAvailabilityRepository extends JpaRepository<SegmentAvailability, String> {

    SegmentAvailability findFirstByTripId(Long id);

    SegmentAvailability findFirstByQuotaCode(String code);

    boolean existsByClassCodeIgnoreCase(String classCode);

}
