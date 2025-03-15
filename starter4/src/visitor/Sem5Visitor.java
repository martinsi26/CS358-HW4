package visitor;

import syntaxtree.*;
import errorMsg.*;

// The purpose of this class it detect and report unreachable code,
// according to Java's rules.
public class Sem5Visitor extends Visitor
{
    ErrorMsg errorMsg;

    public Sem5Visitor(ErrorMsg e)
    {
        errorMsg = e;
    }
}
