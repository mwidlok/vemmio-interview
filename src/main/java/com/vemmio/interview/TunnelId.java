package com.vemmio.interview;

public class TunnelId {
    private final String name;
    private final int index;

    public TunnelId(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name + index;
    }
}
