package io.bootify.my_app.train;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrainDTO {

    @Size(max = 5)
    @TrainNumberValid
    private String number;

    @NotNull
    @Size(max = 100)
    private String name;

}
