package io.bootify.my_app.station;

import io.bootify.my_app.train_stop.TrainStop;
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
public class Station {

    @Id
    @Column(nullable = false, updatable = false, length = 5)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "stationCode")
    private Set<TrainStop> stationCodeTrainStops = new HashSet<>();

}
