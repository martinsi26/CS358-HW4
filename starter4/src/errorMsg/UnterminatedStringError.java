package errorMsg;

public class UnterminatedStringError extends CompError
{
    public UnterminatedStringError()
    {
        super("unterminated string literal");
    }
}
