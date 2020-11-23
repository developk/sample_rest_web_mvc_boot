package com.gru.erpdm.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Greeting {
    private final long id;
    private final String content;

    public Greeting() {
        this.id = -1;
        this.content = "";
    }
}
