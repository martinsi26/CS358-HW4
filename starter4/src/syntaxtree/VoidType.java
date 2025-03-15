package syntaxtree;

import visitor.Visitor;

/**
 * the type, 'void'
 */
public class VoidType extends Type
{

    /**
     * constructor
     * @param pos file position
     */
    public VoidType(int pos)
    {
        super(pos);
    }

    /**
     * type equality
     * @param the object tested for being equal to me
     */
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof VoidType;
    }

    public String name()       { return "VoidType"; }
    public String vtableName() { return "VOID"; }
    public String typeName()   { return "V"; }
    public boolean isVoid()    { return true; }
    public String toString()   { return "void"; }

    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        return 23672;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
