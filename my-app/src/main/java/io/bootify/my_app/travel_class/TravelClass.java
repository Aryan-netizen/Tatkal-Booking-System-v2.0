package io.bootify.my_app.travel_class;

import io.bootify.my_app.coach.Coach;
import io.bootify.my_app.train_class_quota.TrainClassQuota;
import io.bootify.my_app.trip_class.TripClass;
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
public class TravelClass {

    @Id
    @Column(nullable = false, updatable = false, length = 3)
    private String code;

    @Column(nullable = false, length = 40)
    private String name;

    @OneToMany(mappedBy = "classCode")
    private Set<Coach> classCodeCoaches = new HashSet<>();

    @OneToMany(mappedBy = "classCode")
    private Set<TrainClassQuota> classCodeTrainClassQuotas = new HashSet<>();

    @OneToMany(mappedBy = "classCode")
    private Set<TripClass> classCodeTripClasses = new HashSet<>();

}
