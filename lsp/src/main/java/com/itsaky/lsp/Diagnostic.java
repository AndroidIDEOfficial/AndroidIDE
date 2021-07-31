package com.itsaky.lsp;

import java.util.List;

public class Diagnostic {
    public Range range;
    public Integer severity;
    public String code, source, message;
    public List<Integer> tags;
}
