package io.bootify.my_app.train_stop;

import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.station.Station;
import io.bootify.my_app.train.Train;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"train_number_id", "station_code_id"}),
        indexes = @Index(columnList = "station_code_id,train_number_id")
)
@Getter
@Setter
public class TrainStop {

    @Id
    @Column(nullable = false, updatable = false)
    private String seq;

    @Column
    private LocalTime arrivesAt;

    @Column
    private LocalTime departsAt;

    @Column(nullable = false)
    private String dayOffset;

    @Column(nullable = false)
    private String distanceKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_number_id", nullable = false)
    private Train trainNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_code_id", nullable = false)
    private Station stationCode;

    @OneToMany(mappedBy = "trainNumber")
    private Set<Booking> trainNumberBookings = new HashSet<>();

}
