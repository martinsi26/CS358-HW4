package syntaxtree;

import visitor.Visitor;

/**
 * an instance-variable access expression, as in "myList.next"
 */
public class InstVarAccess extends Exp
{

    // instance variables filled in by constructor
    public Exp exp; // the expression denoting the object to access
    public String varName; // the name of the instance variable in the object

    // instance variables filled in during later phases
    public InstVarDecl varDec; // declaration of the instance variable being accessed

    /**
     * constructor
     * @param pos file position
     * @param aexp the expression denoting the object
     * @param avarName the name of the instance variable in the object being accessed
     */
    public InstVarAccess(int pos, Exp aexp, String avarName)
    {
        super(pos);
        exp=aexp;
        varName=avarName;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
