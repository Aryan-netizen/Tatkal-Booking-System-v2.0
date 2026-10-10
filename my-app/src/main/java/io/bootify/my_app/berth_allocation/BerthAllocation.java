package io.bootify.my_app.berth_allocation;

import io.bootify.my_app.berth.Berth;
import io.bootify.my_app.berth_occupancy.BerthOccupancy;
import io.bootify.my_app.passenger.Passenger;
import io.bootify.my_app.trip.Trip;
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
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = "passenger_id"),
        indexes = @Index(columnList = "trip_id,berth_id")
)
@Getter
@Setter
public class BerthAllocation {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String fromSeq;

    @Column(nullable = false)
    private String toSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id", nullable = false)
    private Berth berth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @OneToMany(mappedBy = "allocation")
    private Set<BerthOccupancy> allocationBerthOccupancies = new HashSet<>();

}
