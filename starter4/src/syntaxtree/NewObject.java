package syntaxtree;

import visitor.Visitor;

/**
 * a expression that creates a new object, as in "new Tree()"
 */
public class NewObject extends Exp
{

    // instance variables filled in by constructor
    public IdentifierType objType; // the type of the object being created

    /**
     * constructor
     * @param pos file position
     * @param atype the type of object being created
     */
    public NewObject(int pos, IdentifierType atype)
    {
        super(pos);
        objType=atype;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }


}
