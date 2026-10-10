package io.bootify.my_app.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingDTO {

    @Size(max = 255)
    @BookingIdValid
    private String id;

    @Size(max = 10)
    private String pnr;

    @NotNull
    @Size(max = 255)
    private String userId;

    @NotNull
    @Size(max = 64)
    private String idempotencyKey;

    @NotNull
    @Size(max = 255)
    private String fromSeq;

    @NotNull
    @Size(max = 255)
    private String toSeq;

    @NotNull
    @Size(max = 3)
    private String classCode;

    @NotNull
    @Size(max = 255)
    private String seatCount;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    @Size(max = 255)
    private String farePaise;

    @NotNull
    @Size(max = 255)
    private String tatkalChargePaise;

    @NotNull
    @Size(max = 255)
    private String otherChargesPaise;

    @NotNull
    @Size(max = 255)
    private String gstPaise;

    @Size(max = 255)
    private String amountPaise;

    @NotNull
    private OffsetDateTime createdAt;

    private OffsetDateTime confirmedAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    @Size(max = 255)
    private String trip;

    @NotNull
    @Size(max = 255)
    private String trainNumber;

    @NotNull
    @Size(max = 16)
    private String quota;

}
