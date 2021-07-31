package com.itsaky.lsp;

public class CompletionItemKind {
    public static final int Text = 1,
            Method = 2,
            Function = 3,
            Constructor = 4,
            Field = 5,
            Variable = 6,
            Class = 7,
            Interface = 8,
            Module = 9,
            Property = 10,
            Unit = 11,
            Value = 12,
            Enum = 13,
            Keyword = 14,
            Snippet = 15,
            Color = 16,
            File = 17,
            Reference = 18,
            Folder = 19,
            EnumMember = 20,
            Constant = 21,
            Struct = 22,
            Event = 23,
            Operator = 24,
            TypeParameter = 25;
            
    public static String asString(int kind) {
        switch(kind) {
            case Text : return "Text";
            case Method : return "Method";
            case Function : return "Function";
            case Constructor : return "Constructo";
            case Field: return "Field";
            case Variable : return "Variable";
            case Class : return "Class";
            case Interface : return "Interface";
            case Module : return "Module";
            case Property : return "Property";
            case Unit : return "Unit";
            case Value : return "Value";
            case Enum : return "Enum";
            case Keyword : return "Keyword";
            case Snippet : return "Snippet";
            case Color : return "Color";
            case File : return "File";
            case Reference : return "Reference";
            case Folder : return "Folder";
            case EnumMember : return "Enum Member";
            case Constant : return "Constant";
            case Struct : return "Struct";
            case Event : return "Event";
            case Operator : return "Operator";
            case TypeParameter : return "Type Parameter";
            default: return "Unknown";
        }
    }
}
