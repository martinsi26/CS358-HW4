package syntaxtree;

import visitor.Visitor;

/**
 * a statement that consists of a local variable declaration
 */
public class LocalDeclStatement extends Statement
{

    // instance variables filled in by constructor
    public LocalVarDecl localVarDecl; // the actual declaration

    /**
     * constructor
     * @param pos file position
     * @param decl the local variable declaration
     */
    public LocalDeclStatement(int pos, LocalVarDecl decl)
    {
        super(pos);
        localVarDecl = decl;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

}

