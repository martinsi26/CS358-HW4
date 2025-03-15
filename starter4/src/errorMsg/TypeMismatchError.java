package errorMsg;

import syntaxtree.Type;

public class TypeMismatchError extends CompError
{
    public TypeMismatchError(Type t1, Type t2)
    {
        super("Type-mismatch: expected " + t2 + " found " + t1);
    }
}
