package io.bootify.my_app.train_class_quota;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrainClassQuotaDTO {

    @Size(max = 255)
    @TrainClassQuotaSeatsValid
    private String seats;

    @NotNull
    @Size(max = 5)
    private String trainNumber;

    @NotNull
    @Size(max = 3)
    private String classCode;

    @NotNull
    @Size(max = 16)
    private String quota;

}
