package com.nb.dev.mini_x.enums;

public enum Status {
    ACTIVE(0),
    DELETED(1);

    final int num;

    Status(int num) {
        this.num = num;
    }
}
