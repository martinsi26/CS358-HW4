package errorMsg;

public class OverrideError extends CompError
{
    public static final int RETURN = 0;
    public static final int NUM_ARGS = 1;
    public static final int ARG_TYPES = 2;
    private OverrideError()
    {
        super("Method does not correctly override superclass's method.");
    }
    public OverrideError(int err)
    {
        super("");
        switch(err)
        {
            case RETURN:
              message = "Method has different return type than superclass method.";
              break;
            case NUM_ARGS:
              message = "Method has different number of arguments than superclass method.";
              break;
            case ARG_TYPES:
              message = "Method has different argument types than superclass method.";
              break;
            default:
              message = "Method does not correctly override superclass's method.";
        }
    }
}
