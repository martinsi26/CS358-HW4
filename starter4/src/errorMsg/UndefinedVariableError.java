package errorMsg;

public class UndefinedVariableError extends CompError
{
    public UndefinedVariableError(String name)
    {
        super("Undefined variable name "+name);
    }
}
