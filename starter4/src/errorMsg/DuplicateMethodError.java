package errorMsg;

public class DuplicateMethodError extends CompError
{
    public DuplicateMethodError(String name)
    {
        super("duplicate method name " + name);
    }
}
