package io.bootify.my_app.segment_availability;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SegmentAvailabilityDTO {

    @Size(max = 3)
    @SegmentAvailabilityClassCodeValid
    private String classCode;

    @NotNull
    @Size(max = 255)
    private String segmentIdx;

    @NotNull
    private Integer seatsLeft;

    @NotNull
    private Long trip;

    @NotNull
    @Size(max = 16)
    private String quota;

}
