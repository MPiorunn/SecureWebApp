package com.piorun.secure.app.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class Salt {

    @Id
    private String id;
    private final String value;

    public Salt(String value) {
        this.value = value;
    }
}
