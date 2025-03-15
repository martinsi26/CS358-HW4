package errorMsg;

public class CompMessage implements Comparable<CompMessage>
{
    // protected sucks in Java, so these are effectively public.
    // buuuut... ya know, appearances.
    protected String message;
    protected int line;
    protected int chr;
    protected String file;

    public CompMessage(String m)
    {
        message = m;
    }

    public int compareTo(CompMessage m)
    {
        // <
        if(line < m.line || line == m.line && chr < m.chr)
        {
            return -1;
        }
        // ==
        if(line == m.line && chr == m.chr)
        {
            return 0;
        }
        // >
        return 1;
    }

    public String toString()
    {
        return message;
    }
}
