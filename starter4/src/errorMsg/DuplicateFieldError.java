package errorMsg;

public class DuplicateFieldError extends CompError
{
    public DuplicateFieldError(String name)
    {
        super("duplicate field name " + name);
    }
}

