package io.bootify.my_app.coach;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CoachRepository extends JpaRepository<Coach, String> {

    Coach findFirstByTrainNumberNumber(String number);

    Coach findFirstByClassCodeCode(String code);

    boolean existsByIdIgnoreCase(String id);

}
