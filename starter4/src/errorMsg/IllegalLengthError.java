package errorMsg;

public class IllegalLengthError extends CompError
{
    public IllegalLengthError()
    {
        super("Illegal name, 'length', for an instance variable");
    }
}
