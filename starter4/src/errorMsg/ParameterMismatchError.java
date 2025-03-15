package errorMsg;

public class ParameterMismatchError extends CompError
{
    public ParameterMismatchError(String methName, int argSize, int paramSize)
    {
        super("Wrong number of parameters in call to '"+
              methName+"': "+ argSize+" ("+ paramSize+" expected)");
    }
}
