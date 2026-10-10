package io.bootify.my_app.events;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BeforeDeleteQuota {

    private String code;

}
