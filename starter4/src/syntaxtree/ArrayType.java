package syntaxtree;

import visitor.Visitor;

/**
 * an array type
 */
public class ArrayType extends Type
{
    public Type baseType;

    /**
     * constructor
     * @param pos file position
     * @param base the base type of the array
     */
    public ArrayType(int pos, Type base)
    {
        super(pos);
        baseType = base;
    }

    /**
     * type equality
     * @param the object tested for being equal to me
     */
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof ArrayType && this.baseType.equals(((ArrayType)obj).baseType);
    }

    public String name()       { return "ArrayType"; }
    public String vtableName() { return "ARRAY_" + baseType.vtableName(); }
    public String typeName()   { return "["+baseType.typeName(); }
    public boolean isArray()   { return true; }
    public String toString()   { return baseType + "[]";}


    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        return 18623 + 274673*this.baseType.hashCode();
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
