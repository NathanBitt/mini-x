package com.nb.dev.mini_x.enums;

public enum Values {
    ADMIN(1L),
    BASIC(2L);

   Long id;

    Values(Long id) {
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }
}
