package errorMsg;

import syntaxtree.Type;

public class UndefinedFieldError extends CompError
{
    public UndefinedFieldError(String fieldName, Type t)
    {
        super("Field "+fieldName+" not defined for "+t);
    }
}
