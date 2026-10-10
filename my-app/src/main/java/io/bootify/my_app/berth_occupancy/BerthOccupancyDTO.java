package io.bootify.my_app.berth_occupancy;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BerthOccupancyDTO {

    @Size(max = 255)
    @BerthOccupancyTripIdValid
    private String tripId;

    @NotNull
    @Size(max = 255)
    private String berthId;

    @NotNull
    @Size(max = 255)
    private String segmentIdx;

    @NotNull
    @Size(max = 255)
    private String allocation;

}
