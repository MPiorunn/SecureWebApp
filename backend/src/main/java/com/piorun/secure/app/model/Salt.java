package com.piorun.secure.app.model;

import org.springframework.data.annotation.Id;

public class Salt {

    @Id
    private String id;
    private String value;


    public Salt(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
