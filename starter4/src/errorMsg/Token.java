package errorMsg;

public class Token extends CompMessage
{
    public Token(String m)
    {
        super(m);
    }

    public String toString()
    {
        return line + "." + chr + ": " + message;
    }
}
