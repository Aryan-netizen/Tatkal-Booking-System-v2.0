package io.bootify.my_app.travel_class;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TravelClassDTO {

    @Size(max = 3)
    @TravelClassCodeValid
    private String code;

    @NotNull
    @Size(max = 40)
    private String name;

}
