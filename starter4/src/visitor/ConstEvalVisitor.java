package visitor;

import syntaxtree.*;

// The purpose of this class is to evaluate constant expressions at
// compile-time.  A ConstEvalVisitor, say 'ceVisitor', can be used to
// evaluate an expression, 'e' with:
//   Object obj = e.accept(ceVisitor);
// The object returned will be one of:
//   - null, which means that the expression did NOT evaluate to a constant
//   - An object of (wrapper-class) type Boolean or Integer, in which case
//     the evaluated.
public class ConstEvalVisitor extends Visitor
{

    public ConstEvalVisitor() { }


    public Object visit(Not n)
    {
        return not(bool(n.exp));
    }
    public Object visit(And n)
    {
        return and(bool(n.left), bool(n.right));
    }
    public Object visit(Or n)
    {
        return or(bool(n.left), bool(n.right));
    }

    // equals is weird because it's polymorphic
    // We can use == with bools or ints.
    public Object visit(Equals n)
    {
        Object left = n.left.accept(this);
        Object right = n.right.accept(this);
        if (left == null || right == null) return null;
        return left.equals(right);
    }


    public Object visit(GreaterThan n)
    {
        return intBinOp(intt(n.left), intt(n.right),">");
    }

    public Object visit(LessThan n)
    {
        return intBinOp(intt(n.left), intt(n.right),"<");
    }

    public Object visit(Plus n)
    {
        return intBinOp(intt(n.left), intt(n.right),"+");
    }

    public Object visit(Minus n)
    {
        return intBinOp(intt(n.left), intt(n.right),"-");
    }

    public Object visit(Times n)
    {
        return intBinOp(intt(n.left), intt(n.right),"*");
    }

    public Object visit(Divide n)
    {
        return intBinOp(intt(n.left), intt(n.right),"/");
    }

    public Object visit(Remainder n)
    {
        return intBinOp(intt(n.left), intt(n.right),"%");
    }

    public Object visit(IntegerLiteral n)
    {
        return Integer.valueOf(n.val);
    }

    public Object visit(False n)
    {
        return Boolean.FALSE;
    }

    public Object visit(True n)
    {
        return Boolean.TRUE;
    }

    //////////////////////////////////////////////////////////
    //
    // combinator methods to make traversal easier
    //
    //////////////////////////////////////////////////////////
    
    private Boolean bool(Exp e) {return (Boolean)e.accept(this);}
    private Integer intt(Exp e) {return (Integer)e.accept(this);}

    public Object not(Boolean b)
    {
        if(b == null) return null;
        return !b;
    }

    public Object and(Boolean l, Boolean r)
    {
        if(l == null || r == null) return null;
        return l && r;
    }
    public Object or(Boolean l, Boolean r)
    {
        if(l == null || r == null) return null;
        return l || r;
    }

    public Object intBinOp(Integer l, Integer r, String op)
    {
        if(l == null || r == null) return null;
        switch(op)
        {
            case "+": return l + r;
            case "-": return l - r;
            case "*": return l * r;
            case "/": if(r == 0) return null;
                      else       return l / r;
            case "&": if(r == 0) return null;
                      else       return l % r;
            case ">": return l > r;
            case "<": return l < r;
        }
        return null;
    }
}


