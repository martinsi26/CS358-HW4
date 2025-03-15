package errorMsg;

import syntaxtree.Type;

public class IncompatibleTypeError extends CompError
{
    public IncompatibleTypeError(Type t1, Type t2)
    {
        super("Type-mismatch: "+ t1 + " is not compatible with " + t2);
    }
}
