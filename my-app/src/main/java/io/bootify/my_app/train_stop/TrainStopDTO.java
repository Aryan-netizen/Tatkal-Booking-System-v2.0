package io.bootify.my_app.train_stop;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrainStopDTO {

    @Size(max = 255)
    @TrainStopSeqValid
    private String seq;

    private LocalTime arrivesAt;

    private LocalTime departsAt;

    @NotNull
    @Size(max = 255)
    private String dayOffset;

    @NotNull
    @Size(max = 255)
    private String distanceKm;

    @NotNull
    @Size(max = 5)
    private String trainNumber;

    @NotNull
    @Size(max = 5)
    private String stationCode;

}
