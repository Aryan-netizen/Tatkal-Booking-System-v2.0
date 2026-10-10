package io.bootify.my_app.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    @Size(max = 255)
    @PaymentIdValid
    private String id;

    @NotNull
    @Size(max = 64)
    private String gatewayRef;

    @NotNull
    @Size(max = 255)
    private String amountPaise;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    @Size(max = 255)
    private String booking;

}
