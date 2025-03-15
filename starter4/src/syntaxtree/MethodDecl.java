package syntaxtree;

import visitor.Visitor;

/**
 * a method declaration (abstract)
 */
public abstract class MethodDecl extends Decl
{
    // instance variables filled in by constructor
    public VarDeclList formals; // the method's formal parameters
    public StatementList stmts; // the method's body

    // instance variables filled in during later phases
    public MethodDecl superMethod; // the method that this method is overriding, if any
    public int thisPtrOffset; // the offset on the stack of the this-pointer
    public int vtableOffset; // this method's position in the v-table
    public ClassDecl classDecl; // the class in which the method is declared

    /**
     * constructor
     * @param pos file position
     * @param aname the name being declared
     * @param aformals the list of formal parameter declarations
     * @param astmts the statements that are the method's body
     */
    public MethodDecl(int pos, String aname, VarDeclList aformals,
                      StatementList astmts)
    {
        super(pos,aname);
        formals=aformals;
        stmts=astmts;
        superMethod = null;
        vtableOffset = Integer.MIN_VALUE;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
}
