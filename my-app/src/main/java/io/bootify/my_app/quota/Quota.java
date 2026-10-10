package io.bootify.my_app.quota;

import io.bootify.my_app.booking.Booking;
import io.bootify.my_app.segment_availability.SegmentAvailability;
import io.bootify.my_app.train_class_quota.TrainClassQuota;
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
public class Quota {

    @Id
    @Column(nullable = false, updatable = false, length = 16)
    private String code;

    @Column(nullable = false, length = 40)
    private String name;

    @OneToMany(mappedBy = "quota")
    private Set<TrainClassQuota> quotaTrainClassQuotas = new HashSet<>();

    @OneToMany(mappedBy = "quota")
    private Set<SegmentAvailability> quotaSegmentAvailabilities = new HashSet<>();

    @OneToMany(mappedBy = "quota")
    private Set<Booking> quotaBookings = new HashSet<>();

}
