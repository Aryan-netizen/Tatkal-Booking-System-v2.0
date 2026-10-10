package io.bootify.my_app.train_stop;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TrainStopRepository extends JpaRepository<TrainStop, String> {

    TrainStop findFirstByTrainNumberNumber(String number);

    TrainStop findFirstByStationCodeCode(String code);

    boolean existsBySeqIgnoreCase(String seq);

}
