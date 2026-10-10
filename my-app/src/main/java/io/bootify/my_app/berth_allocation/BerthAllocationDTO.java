package io.bootify.my_app.berth_allocation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BerthAllocationDTO {

    @Size(max = 255)
    @BerthAllocationIdValid
    private String id;

    @NotNull
    @Size(max = 255)
    private String fromSeq;

    @NotNull
    @Size(max = 255)
    private String toSeq;

    @NotNull
    @Size(max = 255)
    private String trip;

    @NotNull
    @Size(max = 255)
    private String berth;

    @NotNull
    @Size(max = 255)
    private String passenger;

}
