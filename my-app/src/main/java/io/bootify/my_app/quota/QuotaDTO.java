package io.bootify.my_app.quota;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuotaDTO {

    @Size(max = 16)
    @QuotaCodeValid
    private String code;

    @NotNull
    @Size(max = 40)
    private String name;

}
