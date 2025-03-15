package errorMsg;

public class IllegalSuperclassError extends CompError
{
    public IllegalSuperclassError(String name)
    {
        super(name+" cannot be a superclass");
    }
}
