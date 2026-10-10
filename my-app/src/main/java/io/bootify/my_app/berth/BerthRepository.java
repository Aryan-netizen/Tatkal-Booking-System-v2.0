package io.bootify.my_app.berth;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BerthRepository extends JpaRepository<Berth, String> {

    Berth findFirstByCoachId(String id);

    boolean existsByIdIgnoreCase(String id);

}
