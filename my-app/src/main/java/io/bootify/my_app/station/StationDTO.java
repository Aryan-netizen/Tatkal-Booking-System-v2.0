package io.bootify.my_app.station;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StationDTO {

    @Size(max = 5)
    @StationCodeValid
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

}
