package errorMsg;

public class SwitchError extends CompError
{
    public static final int FIRST_LABEL_ERROR = 0;
    public static final int LABEL_AFTER_BREAK = 1;
    public static final int END_BREAK_ERROR = 2;
    public static final int NO_DEFAULT_ERROR = 3;
    public static final int DUPLICATE_KEY_ERROR = 4;
    public static final int NONCONSTANT_CASE_ERROR = 5;

    private SwitchError()
    {
        super("Switch incorrectly formatted");
    }
    public SwitchError(int err)
    {
        super("");
        switch(err)
        {
            case FIRST_LABEL_ERROR: 
                message = "First statement of a switch must be a case or default";
                break;
            case LABEL_AFTER_BREAK: 
                message = "The statement following a break must be a case or default";
                break;
            case END_BREAK_ERROR: 
                message = "Last statement of a switch must be a break";
                break;
            case NO_DEFAULT_ERROR: 
                message = "No default found in switch";
                break;
            case DUPLICATE_KEY_ERROR: 
                message = "Same value found in multiple cases";
                break;
            case NONCONSTANT_CASE_ERROR:
                message = "case labels must be constant values.";
                break;
            default:
                message = "Switch incorrectly formatted";
                break;
        }
    }
}

