package io.bootify.my_app.passenger;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PassengerDTO {

    @Size(max = 255)
    @PassengerIdValid
    private String id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String age;

    @NotNull
    @Size(max = 255)
    private String gender;

    @Size(max = 255)
    private String berthPref;

    @NotNull
    @Size(max = 255)
    private String status;

    @Size(max = 255)
    private String queueNo;

    @NotNull
    @Size(max = 255)
    private String booking;

}
