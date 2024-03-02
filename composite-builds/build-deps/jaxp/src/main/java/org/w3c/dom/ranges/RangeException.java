package org.w3c.dom.ranges;

public class RangeException extends RuntimeException {

    public RangeException(short code, String message) {
        super(message);
        this.code = code;
    }

    public short code;

    public static final short BAD_BOUNDARYPOINTS_ERR    = 1;
    public static final short INVALID_NODE_TYPE_ERR     = 2;
}
