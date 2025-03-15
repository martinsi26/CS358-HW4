package syntaxtree;

import visitor.Visitor;

/**
 * the type 'int'
 */
public class IntegerType extends Type
{

    /**
     * constructor
     * @param pos file position
     */
    public IntegerType(int pos)
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
        return obj instanceof IntegerType;
    }

    public String name()       { return "IntegerType"; }
    public String vtableName() { return "INT"; }
    public String typeName()   { return "I"; }
    public boolean isInt()     { return true; }
    public String toString()   { return "int"; }

    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        return 657643445;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

}
