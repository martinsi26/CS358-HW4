package errorMsg;

public class IllegalCharError extends CompError
{
    public IllegalCharError(char c)
    {
        super("Illegal character ("+(int)c+") found.");
    }
}
