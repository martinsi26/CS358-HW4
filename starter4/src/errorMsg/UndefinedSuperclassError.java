package errorMsg;

public class UndefinedSuperclassError extends CompError
{
    public UndefinedSuperclassError(String name)
    {
        super("undefined super class " + name);
    }
}
