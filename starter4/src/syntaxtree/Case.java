package syntaxtree;

import visitor.Visitor;

/**
 * a case-label within a switch statement
 */
public class Case extends Label
{

    // instance variables filled in by constructor
    public Exp exp; // the case's label-value

    /**
     * constructor
     * @param pos file position
     * @param aexp the case-expression
     */
    public Case(int pos, Exp aexp)
    {
        super(pos);
        exp=aexp;
    }

    /**
     * the value, if any, that is associated with the label
     * @return the value associated with the label
     */
    @Override
    public Exp labelValue()
    {
        return exp;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

}

