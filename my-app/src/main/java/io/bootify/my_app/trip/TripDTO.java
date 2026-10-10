package io.bootify.my_app.trip;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TripDTO {

    @Size(max = 255)
    @TripIdValid
    private String id;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    @Size(max = 5)
    private String trainNumber;

}
