package io.bootify.my_app.trip;

import io.bootify.my_app.berth_allocation.BerthAllocation;
import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.trip_class.TripClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"train_number_id", "travel_date"})
)
@Getter
@Setter
public class Trip {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private LocalDate travelDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_number_id", nullable = false)
    private Train trainNumber;

    @OneToMany(mappedBy = "trip")
    private Set<TripClass> tripTripClasses = new HashSet<>();

    @OneToMany(mappedBy = "trip")
    private Set<Booking> tripBookings = new HashSet<>();

    @OneToMany(mappedBy = "trip")
    private Set<BerthAllocation> tripBerthAllocations = new HashSet<>();

}
