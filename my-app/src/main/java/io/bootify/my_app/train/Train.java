package io.bootify.my_app.train;

import io.bootify.my_app.coach.Coach;
import io.bootify.my_app.train_class_quota.TrainClassQuota;
import io.bootify.my_app.train_stop.TrainStop;
import io.bootify.my_app.trip.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Train {

    @Id
    @Column(nullable = false, updatable = false, length = 5)
    private String number;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "trainNumber")
    private Set<TrainStop> trainNumberTrainStops = new HashSet<>();

    @OneToMany(mappedBy = "trainNumber")
    private Set<Coach> trainNumberCoaches = new HashSet<>();

    @OneToMany(mappedBy = "trainNumber")
    private Set<TrainClassQuota> trainNumberTrainClassQuotas = new HashSet<>();

    @OneToMany(mappedBy = "trainNumber")
    private Set<Trip> trainNumberTrips = new HashSet<>();

}
