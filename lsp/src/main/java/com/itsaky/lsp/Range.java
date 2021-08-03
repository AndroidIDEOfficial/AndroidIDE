package com.itsaky.lsp;

import com.google.gson.GsonBuilder;

public class Range {
    public Position start, end;

    public Range() {}

    public Range(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Range) {
            Range that = (Range) obj;
            return this.start.equals(that.start)
            && this.end.equals(that.end);
        }
        return false;
    }

    public static final Range NONE = new Range(Position.NONE, Position.NONE);
}
