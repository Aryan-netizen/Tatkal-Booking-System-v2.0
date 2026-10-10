package io.bootify.my_app.coach;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CoachDTO {

    @Size(max = 255)
    @CoachIdValid
    private String id;

    @NotNull
    @Size(max = 4)
    private String code;

    @NotNull
    @Size(max = 5)
    private String trainNumber;

    @NotNull
    @Size(max = 3)
    private String classCode;

}
