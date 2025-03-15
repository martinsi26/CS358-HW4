package errorMsg;

public class UnusedClassWarning extends CompWarning
{
    public UnusedClassWarning(String name)
    {
        super("unused class "+name);
    }
}
