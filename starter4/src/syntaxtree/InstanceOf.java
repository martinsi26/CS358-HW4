package syntaxtree;

import visitor.Visitor;

/**
 * a 'instanceof' expression
 */
public class InstanceOf extends Exp
{

    // instance variables filled in by constructor
    public Exp exp; // the expression being tested
    public Type checkType; // the type being checked against

    /**
     * constructor
     * @param pos file position
     * @param aexp the expression being tested
     * @param atype the type being tested against
     */
    public InstanceOf(int pos, Exp aexp, Type atype)
    {
        super(pos);
        exp=aexp;
        checkType=atype;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
