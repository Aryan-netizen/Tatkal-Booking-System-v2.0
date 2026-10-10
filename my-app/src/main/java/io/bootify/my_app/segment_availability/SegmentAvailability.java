package io.bootify.my_app.segment_availability;

import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.trip_class.TripClass;
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
public class SegmentAvailability {

    @Id
    @Column(nullable = false, updatable = false, length = 3)
    private String classCode;

    @Column(nullable = false)
    private String segmentIdx;

    @Column(nullable = false)
    private Integer seatsLeft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripClass trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quota_id", nullable = false)
    private Quota quota;

}
