package errorMsg;

public class OutOfRangeError extends CompError
{
    public OutOfRangeError(String s)
    {
        super("Integer literal value "+s+" is out of range.");
    }
}
