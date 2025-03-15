package errorMsg;

import syntaxtree.Type;

public class UndefinedMethodError extends CompError
{
    public UndefinedMethodError(String methName, Type t)
    {
        super("Method "+methName+" not defined for "+t);
    }
}
