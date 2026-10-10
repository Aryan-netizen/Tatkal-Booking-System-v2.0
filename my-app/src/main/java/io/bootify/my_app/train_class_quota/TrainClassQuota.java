package io.bootify.my_app.train_class_quota;

import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.travel_class.TravelClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class TrainClassQuota {

    @Id
    @Column(nullable = false, updatable = false)
    private String seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_number_id", nullable = false)
    private Train trainNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_code_id", nullable = false)
    private TravelClass classCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quota_id", nullable = false)
    private Quota quota;

}
