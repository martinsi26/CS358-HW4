package errorMsg;

public class UndefinedClassError extends CompError
{
    public UndefinedClassError(String name)
    {
        super("Undefined class name "+name);
    }
}
