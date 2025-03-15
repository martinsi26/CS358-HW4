package errorMsg;

public class InheritanceCycleError extends CompError
{
    public InheritanceCycleError(String name)
    {
        super("inheritance cycle detected: "+name);
    }
}
