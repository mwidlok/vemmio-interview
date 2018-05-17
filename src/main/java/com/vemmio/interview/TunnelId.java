package com.vemmio.interview;

import java.util.Objects;

public class TunnelId {
    private final String name;
    private final int index;

    public TunnelId(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name + index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TunnelId tunnelId = (TunnelId)o;
        return index == tunnelId.index && Objects.equals(name, tunnelId.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index);
    }
}
