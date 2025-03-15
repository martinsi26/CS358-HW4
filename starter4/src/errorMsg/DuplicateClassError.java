package errorMsg;

public class DuplicateClassError extends CompError
{
    public DuplicateClassError(String name)
    {
        super("duplicate class name " + name);
    }
}
