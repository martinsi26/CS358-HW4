package errorMsg;

public class AssignmentError extends CompError
{
    public AssignmentError()
    {
        super("Assignment target not modifiable");
    }
}

