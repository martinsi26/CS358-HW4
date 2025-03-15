package errorMsg;

public class DuplicateVariableError extends CompError
{
    public DuplicateVariableError(String name)
    {
        super("duplicate variable name " + name);
    }
}
