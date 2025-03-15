package syntaxtree;

import visitor.Visitor;

/**
 * an array-access expression, as in a[i]
 */
public class ArrayLookup extends Exp
{

    // instance variables filled in by constructor
    public Exp arrExp; // the array-expression
    public Exp idxExp; // the index-expression

    /**
     * constructor
     * @param pos file position
     * @param aarrExp the expression before the brackets
     * @param aidxExp the expression inside the brackets
     */
    public ArrayLookup(int pos, Exp aarrExp, Exp aidxExp)
    {
        super(pos);
        arrExp=aarrExp;
        idxExp=aidxExp;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
