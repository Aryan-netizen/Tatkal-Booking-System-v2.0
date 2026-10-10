package io.bootify.my_app.train;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TrainRepository extends JpaRepository<Train, String> {

    boolean existsByNumberIgnoreCase(String number);

}
