package io.bootify.my_app.booking;

import io.bootify.my_app.booking_event.BookingEvent;
import io.bootify.my_app.booking_hold.BookingHold;
import io.bootify.my_app.passenger.Passenger;
import io.bootify.my_app.payment.Payment;
import io.bootify.my_app.quota.Quota;
import io.bootify.my_app.train_stop.TrainStop;
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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idempotency_key"}),
        indexes = {
                @Index(columnList = "user_id,created_at"),
                @Index(columnList = "trip_id,class_code,status")
        }
)
@Getter
@Setter
public class Booking {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(unique = true, length = 10)
    private String pnr;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 64)
    private String idempotencyKey;

    @Column(nullable = false)
    private String fromSeq;

    @Column(nullable = false)
    private String toSeq;

    @Column(nullable = false, length = 3)
    private String classCode;

    @Column(nullable = false)
    private String seatCount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String farePaise;

    @Column(nullable = false)
    private String tatkalChargePaise;

    @Column(nullable = false)
    private String otherChargesPaise;

    @Column(nullable = false)
    private String gstPaise;

    @Column
    private String amountPaise;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime confirmedAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_number_id", nullable = false)
    private TrainStop trainNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quota_id", nullable = false)
    private Quota quota;

    @OneToMany(mappedBy = "booking")
    private Set<BookingHold> bookingBookingHolds = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    private Set<Passenger> bookingPassengers = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    private Set<Payment> bookingPayments = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    private Set<BookingEvent> bookingBookingEvents = new HashSet<>();

}
