package io.bootify.my_app.booking_event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingEventDTO {

    @Size(max = 255)
    @BookingEventIdValid
    private String id;

    @Size(max = 16)
    private String fromStatus;

    @NotNull
    @Size(max = 16)
    private String toStatus;

    @Size(max = 255)
    private String note;

    @NotNull
    private OffsetDateTime at;

    @NotNull
    @Size(max = 255)
    private String booking;

}
