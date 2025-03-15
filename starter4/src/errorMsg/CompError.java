package errorMsg;

public class CompError extends CompMessage
{
    public CompError(String m)
    {
        super(m);
    }

    public String toString()
    {
        return file + "::" + line + "." + chr + ": " + message;
    }
}
