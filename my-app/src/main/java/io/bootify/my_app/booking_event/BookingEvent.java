package io.bootify.my_app.booking_event;

import io.bootify.my_app.booking.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(indexes = @Index(columnList = "booking_id,at"))
@Getter
@Setter
public class BookingEvent {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(length = 16)
    private String fromStatus;

    @Column(nullable = false, length = 16)
    private String toStatus;

    @Column
    private String note;

    @Column(nullable = false)
    private OffsetDateTime at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

}
