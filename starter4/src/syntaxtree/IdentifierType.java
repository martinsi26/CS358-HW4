package syntaxtree;

import visitor.Visitor;

/**
 * a named type (i.e., a class name)
 */
public class IdentifierType extends Type
{

    // instance variables filled in by constructor
    public String name; // the name of the type

    // instance variables filled in during later phases
    public ClassDecl link; // the type declaration

    /**
     * constructor
     * @param pos file position
     * @param aname the name of the type
     */
    public IdentifierType(int pos, String aname)
    {
        super(pos);
        name = aname;
        link = null;
    }



    /**
     * type equality
     * @param the object tested for being equal to me
     */
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof IdentifierType &&
               this.link != null &&
               this.link == ((IdentifierType)obj).link;
    }

    public String name()       { return "IdentifierType"; }
    public String vtableName() { return name; }
    public String typeName()   { return name; }
    public boolean isID()      { return true; }

    public boolean isObject()  { return name.equals("Object"); }
    public String toString()   { return name; }

    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        if (name == null)
        {
            return 23977;
        }
        else return 826427*name.hashCode()+83473;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
