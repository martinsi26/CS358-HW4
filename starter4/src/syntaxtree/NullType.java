package syntaxtree;

import visitor.Visitor;

/**
 * the type of the expression 'null'
 */
public class NullType extends Type
{

    /**
     * constructor
     * @param pos file position
     */
    public NullType(int pos)
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
        return obj instanceof NullType;
    }

    public String name()       { return "NullType";} 
    public String vtableName() { return "NULL"; }
    public String typeName()   { return "N"; }
    public boolean isNull()    { return true; }
    public String toString()   { return "NULL"; }

    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        return 7326834;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

}
