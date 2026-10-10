package io.bootify.my_app.berth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BerthDTO {

    @Size(max = 255)
    @BerthIdValid
    private String id;

    @NotNull
    @Size(max = 255)
    private String number;

    @NotNull
    @Size(max = 255)
    private String kind;

    @NotNull
    @Size(max = 255)
    private String coach;

}
