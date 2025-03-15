package errorMsg;

import syntaxtree.Type;

public class SubtypeError extends CompError
{
    public SubtypeError(Type t1, Type t2)
    {
        super("Type-mismatch: " + t1 + " is not a subtype of " + t2);
    }
}
