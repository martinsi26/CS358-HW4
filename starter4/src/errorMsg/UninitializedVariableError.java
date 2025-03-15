package errorMsg;

public class UninitializedVariableError extends CompError
{
    public UninitializedVariableError(String name)
    {
        super("Reference to uninitialized variable " + name);
    }
}
