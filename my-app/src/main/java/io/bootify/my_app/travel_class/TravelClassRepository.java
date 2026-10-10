package io.bootify.my_app.travel_class;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TravelClassRepository extends JpaRepository<TravelClass, String> {

    boolean existsByCodeIgnoreCase(String code);

}
