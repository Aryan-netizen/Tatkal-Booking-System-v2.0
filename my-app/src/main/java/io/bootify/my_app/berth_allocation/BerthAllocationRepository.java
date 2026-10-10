package io.bootify.my_app.berth_allocation;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BerthAllocationRepository extends JpaRepository<BerthAllocation, String> {

    BerthAllocation findFirstByTripId(String id);

    BerthAllocation findFirstByBerthId(String id);

    BerthAllocation findFirstByPassengerId(String id);

    boolean existsByIdIgnoreCase(String id);

}
