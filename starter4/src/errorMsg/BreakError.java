package errorMsg;

public class BreakError extends CompError
{
    public BreakError()
    {
        super("Break statement not in loop or switch");
    }
}
