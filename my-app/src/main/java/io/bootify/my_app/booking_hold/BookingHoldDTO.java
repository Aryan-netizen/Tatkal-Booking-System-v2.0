package io.bootify.my_app.booking_hold;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingHoldDTO {

    private Long id;

    @NotNull
    private OffsetDateTime holdExpiresAt;

    @NotNull
    @Size(max = 255)
    private String booking;

}
