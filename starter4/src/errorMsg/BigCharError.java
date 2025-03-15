package errorMsg;
import java.util.List;

public class BigCharError extends CompError
{
    public BigCharError(char c, List<Character> cs)
    {
        super("found character string " + c + cs.toString());
    }
}
