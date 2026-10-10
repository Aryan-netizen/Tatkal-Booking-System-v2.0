package io.bootify.my_app.berth_occupancy;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BerthOccupancyRepository extends JpaRepository<BerthOccupancy, String> {

    BerthOccupancy findFirstByAllocationId(String id);

    boolean existsByTripIdIgnoreCase(String tripId);

}
