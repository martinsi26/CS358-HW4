package syntaxtree;

import visitor.Visitor;

/**
 * a 'while' statement
 */
public class While extends BreakTarget
{

    // instance variables filled in by constructor
    public Exp exp; // the test-expression
    public Statement body; // the while-statement's body

    /**
     * constructor
     * @param pos file position
     * @param aexp the while-statement's test-expression
     * @param abody the while-statement's body
     */
    public While(int pos, Exp aexp, Statement abody)
    {
        super(pos);
        exp=aexp;
        body=abody;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }


}

