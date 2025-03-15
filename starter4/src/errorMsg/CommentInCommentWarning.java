package errorMsg;

public class CommentInCommentWarning extends CompWarning
{
    public CommentInCommentWarning()
    {
        super("found /* inside multi-line comment");
    }
}
