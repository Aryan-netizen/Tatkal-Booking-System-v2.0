package io.bootify.my_app.trip_class;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TripClassDTO {

    private Long id;

    @NotNull
    private OffsetDateTime tatkalOpensAt;

    @NotNull
    @Size(max = 255)
    private String trip;

    @NotNull
    @Size(max = 3)
    private String classCode;

}
