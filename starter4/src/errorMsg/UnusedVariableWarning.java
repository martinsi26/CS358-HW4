package errorMsg;

public class UnusedVariableWarning extends CompWarning
{
    public UnusedVariableWarning(String name)
    {
        super("unused variable "+name);
    }
}
