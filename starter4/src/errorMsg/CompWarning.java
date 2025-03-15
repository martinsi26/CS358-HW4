package errorMsg;

public class CompWarning extends CompMessage
{
    public CompWarning(String m)
    {
        super(m);
    }

    public String toString()
    {
        return file + "::" + line + "." + chr + "(warning): " + message;
    }
}
