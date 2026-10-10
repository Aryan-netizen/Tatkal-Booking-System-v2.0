package io.bootify.my_app.train_class_quota;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TrainClassQuotaRepository extends JpaRepository<TrainClassQuota, String> {

    TrainClassQuota findFirstByTrainNumberNumber(String number);

    TrainClassQuota findFirstByClassCodeCode(String code);

    TrainClassQuota findFirstByQuotaCode(String code);

    boolean existsBySeatsIgnoreCase(String seats);

}
