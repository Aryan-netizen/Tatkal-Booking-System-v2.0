package io.bootify.my_app.passenger;

import io.bootify.my_app.berth_allocation.BerthAllocation;
import io.bootify.my_app.booking.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(indexes = @Index(columnList = "booking_id"))
@Getter
@Setter
public class Passenger {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column
    private String berthPref;

    @Column(nullable = false)
    private String status;

    @Column
    private String queueNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @OneToMany(mappedBy = "passenger")
    private Set<BerthAllocation> passengerBerthAllocations = new HashSet<>();

}
