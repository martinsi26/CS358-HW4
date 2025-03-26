package visitor;

import syntaxtree.*;

import java.util.*;

import errorMsg.*;
// The purpose of this class is to do type-checking and related
// actions.  These include:
// - evaluate the type for each expression, 
//   filling in the 'type' link for each
// - ensure that each expression follows MiniJava's type rules (e.g.,
//   that the arguments to '*' are both integer, the argument to
//   '.length' is an array, etc.)

// - ensure that each method-call follows Java's type rules:
//   - there exists a method for the given class (or a superclass)
//     for the receiver's object type
//   - the method has the correct number of parameters
//   - the types of each actual parameter is compatible with that
//     of its corresponding formal parameter
// 
public class Sem4Visitor extends Visitor
{
    ClassDecl currentClass;
    IdentifierType currentType;
    IdentifierType superType;
    ErrorMsg errorMsg;

    // Constants for types we'll need
    BooleanType Bool;
    IntegerType Int;
    VoidType Void;
    NullType Null;
    ErrorType Error;
    IdentifierType ObjectType;
    IdentifierType StringType;

    HashMap<String,ClassDecl> classEnv;

    ConstEvalVisitor ceVisitor;

    public Sem4Visitor(HashMap<String,ClassDecl> env, ErrorMsg e)
    {
        errorMsg = e;
        classEnv = env;
        currentClass = null;

        Bool = new BooleanType(-1);
        Int  = new IntegerType(-1);
        Null = new NullType(-1);
        Void = new VoidType(-1);
        Error = new ErrorType(-1);
        StringType = new IdentifierType(-1, "String");
        ObjectType = new IdentifierType(-1, "Object");
        StringType.link = classEnv.get("String");
        ObjectType.link = classEnv.get("Object");

        ceVisitor = new ConstEvalVisitor();
    }

    public Boolean checkCompatible(int pos, Type t1, Type t2) {
        if(checkSubtype(-1, t1, t2) || checkSubtype(-1, t2, t1)) {
            return true;
        }
        errorMsg.error(pos, new IncompatibleTypeError(t1, t2));
        return false;
    }

    public Boolean checkSubtype(int pos, Type t1, Type t2) {
        if(t1.isVoid() || t2.isVoid()) {
            if(pos >= 0) {
                errorMsg.error(pos, new SubtypeError(t1, t2));
            }
            return false;
        }
        if(t1.equals(t2)) {
            return true;
        }
        if(t1.isArray() && t2.isObject()) {
            return true;
        }
        if(t1.isError()) {
            return true;
        }
        if(t2.isError()) {
            return true;
        }
        if(t1.isID() && t2.isID()) {
            ClassDecl c1 = ((IdentifierType)t1).link;
            ClassDecl c2 = ((IdentifierType)t2).link;
            while(c1.superLink != null) {
                if(c1.equals(c2)) {
                    return true;
                }
                c1 = c1.superLink;
            }
        }
        if(t1.isNull() && (t2.isID() || t2.isArray())) {
            return true;
        }
        if(pos >= 0) {
            errorMsg.error(pos, new SubtypeError(t1, t2));
        }
        return false;
    }

    public Boolean checkInt(int pos, Type t) {
        if(!t.isInt()) {
            errorMsg.error(pos, new TypeMismatchError(t, Int));
            return false;
        }
        return true;
    }

    public Boolean checkBoolean(int pos, Type t) {
        if(!t.isBoolean()) {
            errorMsg.error(pos, new TypeMismatchError(t, Bool));
            return false;
        }
        return true;
    }

    public Boolean checkArray(int pos, Type t) {
        if(t.isArray()) {
            ArrayType array = (ArrayType)t;
            while(array.baseType != null) {
                if(array.baseType.isBoolean() || array.baseType.isInt() || array.baseType.isID()) {
                    return true;
                }
                array = (ArrayType)array.baseType;
            }
        }
        errorMsg.error(pos, new ArrayTypeError());
        return false;
    }

    public Object visit(ClassDecl c)
    {
        currentClass = c;
        currentType = new IdentifierType(c.pos, c.name);
        currentType.link = c;
        if(c.superLink != null) {
            ClassDecl superClass = c.superLink;
            superType = new IdentifierType(superClass.pos, superClass.name);
            superType.link = superClass;
        }
        c.decls.accept(this);
        return null;
    }

    // - ensure that if a method with a given name is defined in both
    //   a subclass and a superclass, that they have the same parameters
    //   (with identical types) and the same return type

    public Object visit(MethodDecl n)
    {
        n.formals.accept(this);
        n.stmts.accept(this);
        ClassDecl c = currentClass;
        while(c.superLink != null) {
            if(c.superLink.methodEnv.containsKey(n.name)) {
                n.superMethod = c.superLink.methodEnv.get(n.name);
                if(n.formals.size() != n.superMethod.formals.size()) {
                    errorMsg.error(n.pos, new OverrideError(1));
                    return null;
                }
                for(int i = 0; i < n.formals.size(); i++) {
                    if(!n.formals.get(i).type.equals(n.superMethod.formals.get(i).type)) {
                        errorMsg.error(n.pos, new OverrideError(2));
                        return null;
                    }
                }
            }
            c = c.superLink;
        }
        return null;
    }

    // - ensure that the declared return-type of a method is compatible
    //   with its "return" expression

    public Object visit(MethodDeclNonVoid n)
    {
        n.rtnType.accept(this);
        Type rType = n.rtnType;
        visit((MethodDecl)n);
        Type rExp = (Type)n.rtnExp.accept(this);
        if(n.superMethod != null) {
            if(!n.rtnType.equals(((MethodDeclNonVoid)n.superMethod).rtnType)) {
                errorMsg.error(n.pos, new OverrideError(0));
                return null;
            }
        }
        if(!checkCompatible(n.pos, rType, rExp)) {
            return null;
        }
        return null;
    }

    public Object visit(False n)   
    { 
        n.type = Bool;
        return n.type; 
    }

    public Object visit(Null n)    
    { 
        n.type = Null;
        return n.type; 
    }

    public Object visit(Super n)   
    { 
        n.type = superType;
        return n.type; 
    }

    public Object visit(This n)    
    { 
        n.type = currentType;
        return n.type; 
    }

    public Object visit(True n)    
    { 
        n.type = Bool;
        return n.type; 
    }

    public Object visit(IdentifierExp n)  
    { 
        n.type = n.link.type;
        return n.type;
    }

    public Object visit(IntegerLiteral n)
    { 
        n.type = Int;
        return n.type;
    }

    public Object visit(StringLiteral n)  
    { 
        n.type = StringType;
        return n.type;
    }

    public Object visit(And n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkBoolean(n.left.pos, t1);
        checkBoolean(n.right.pos, t2);
        n.type = Bool;
        return n.type;
    }

    public Object visit(LessThan n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Bool;
        return n.type;
    }

    public Object visit(GreaterThan n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Bool;
        return n.type;
    }
    public Object visit(Minus n)       
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Int;
        return n.type;
    }

    public Object visit(Or n)          
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Bool;
        return n.type;
    }

    public Object visit(Plus n)        
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Int;
        return n.type;
    }

    public Object visit(Times n)       
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Int;
        return n.type;
    }

    public Object visit(Divide n)      
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Int;
        return n.type;
    }

    public Object visit(Remainder n)   
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkInt(n.left.pos, t1);
        checkInt(n.right.pos, t2);
        n.type = Int;
        return n.type;
    }

    public Object visit(Equals n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        checkCompatible(n.pos, t1, t2);
        n.type = Bool;
        return n.type;
    }

    public Object visit(Not n)         
    { 
        Type t = (Type)n.exp.accept(this);
        checkBoolean(n.exp.pos, t);
        n.type = Bool;
        return n.type; 
    }

    public Object visit(ArrayLength n) 
    { 
        Type t = (Type)n.exp.accept(this);
        checkArray(n.exp.pos, t);
        n.type = Int;
        return n.type; 
    }

    public Object visit(ArrayLookup n)
    {
        Type t1 = (Type)n.arrExp.accept(this);
        Type t2 = (Type)n.idxExp.accept(this);
        Boolean isArray = checkArray(n.arrExp.pos, t1);
        Boolean isInt = checkInt(n.idxExp.pos, t2);
        if(!isArray) {
            n.type = Error;
            return Error;
        }
        Object obj = n.idxExp.accept(ceVisitor);
        if(obj != null) {
            if((Integer)obj < 0) {
                errorMsg.warning(n.idxExp.pos, new NegativeIndexWarning());
                n.type = Error;
                return Error;
            }
        }
        Type t = ((ArrayType)t1).baseType;
        while(t instanceof ArrayType) {
            t = ((ArrayType)t).baseType;
        }
        n.type = t;
        return n.type;
    }

    // - ensure that each method-call follows Java's type rules:
//   - there exists a method for the given class (or a superclass)
//     for the receiver's object type
//   - the method has the correct number of parameters
//   - the types of each actual parameter is compatible with that
//     of its corresponding formal parameter

    public Object visit(Call n)
    {
        n.parms.accept(this);
        Type t = (Type)n.obj.accept(this);
        if(t.isID()) {
            ClassDecl c = ((IdentifierType)n.obj.type).link;
            if(c.methodEnv.containsKey(n.methName)) {
                n.methodLink = c.methodEnv.get(n.methName);
            } else {
                while(c.superLink != null) {
                    if(c.superLink.methodEnv.containsKey(n.methName)) {
                        n.methodLink = c.superLink.methodEnv.get(n.methName);
                        break;
                    }
                    c = c.superLink;
                }
            }
        }
        if(n.methodLink == null) {
            errorMsg.error(n.pos, new UndefinedMethodError(n.methName, t));
            n.type = Error;
            return n.type;
        }
        if(n.methodLink instanceof MethodDeclVoid) {
            n.type = Void;
        } else {
            n.type = ((MethodDeclNonVoid)n.methodLink).rtnType;
        }
        VarDeclList methodFormals = n.methodLink.formals;
        ExpList callFormals = n.parms;
        if(callFormals.size() != methodFormals.size()) {
            errorMsg.error(n.pos, new ParameterMismatchError(n.methodLink.name, callFormals.size(), methodFormals.size()));
            return n.type;
        }
        for(int i = 0; i < callFormals.size(); i++) {
            checkSubtype(n.pos, callFormals.get(i).type, methodFormals.get(i).type);
        }
        return n.type;
    }

    public Object visit(Cast n)
    {
        n.castType.accept(this);
        Type t1 = n.castType;
        Type t2 = (Type)n.exp.accept(this);
        checkCompatible(n.pos, t1, t2);
        n.type = t2;
        return n.type;
    }

//- ensure that for each instance variable access (e.g., abc.foo),
//   there is an instance variable defined for the given class (or
//   in a superclass
//   - sets the 'varDec' link in the InstVarAccess to refer to the
//     method

// Use undefined field error if obj is not an ID (is an int) or obj is not in current class or any parent class

    public Object visit(InstVarAccess n)
    {
        Type t = (Type)n.exp.accept(this);
        if(!t.isID()) {
            errorMsg.error(n.pos, new UndefinedFieldError(n.varName, t));
            n.type = Error;
            return n.type;
        }
        ClassDecl c = classEnv.get(((IdentifierType)t).name);
        if(c.fieldEnv.containsKey(n.varName)) {
            n.varDec = c.fieldEnv.get(n.varName);
        } else {
            while(c.superLink != null) {
                if(c.superLink.fieldEnv.containsKey(n.varName)) {
                    n.varDec = c.superLink.fieldEnv.get(n.varName);
                    break;
                }
                c = c.superLink;
            }
        }
        if(n.varDec == null) {
            errorMsg.error(n.pos, new UndefinedFieldError(n.varName, t));
            n.type = Error;
            return n.type;
        }
        n.type = n.varDec.type;
        return n.type;
    }

    public Object visit(InstanceOf n)
    {
        Type t1 = (Type)n.exp.accept(this);
        n.checkType.accept(this);
        Type t2 = n.checkType;
        checkCompatible(n.pos, t1, t2);
        n.type = Bool;
        return n.type;
    }

    public Object visit(NewArray n)
    {
        n.objType.accept(this);
        Type t1 = n.objType;
        t1 = new ArrayType(n.pos, t1);
        Type t2 = (Type)n.sizeExp.accept(this);
        Object obj = n.sizeExp.accept(ceVisitor);
        if(obj != null) {
            if((Integer)obj < 0) {
                errorMsg.warning(n.sizeExp.pos, new NegativeLengthWarning());
                n.type = Error;
                return Error;
            }
        }
        checkInt(n.sizeExp.pos, t2);
        n.type = t1;
        return n.type;
    }

    public Object visit(NewObject n)
    {
        n.objType.accept(this);
        Type t = n.objType;
        n.type = t;
        return n.type;
    }

// - ensuring that the type of the control expression for an if- or
//   while-statement is boolean

    public Object visit(If n)
    {
        Type t = (Type)n.exp.accept(this);
        n.trueStmt.accept(this);
        n.falseStmt.accept(this);
        checkBoolean(n.exp.pos, t);
        return null;
    }

    public Object visit(While n)
    {
        Type t = (Type)n.exp.accept(this);
        n.body.accept(this);
        checkBoolean(n.exp.pos, t);
        return null;
    }


    public Object visit(Switch n)
    {
        Type t = (Type)n.exp.accept(this);
        n.stmts.accept(this);
        checkInt(n.exp.pos, t);
        return null;
    }

    public Object visit(Case n)
    {
        Type t = (Type)n.exp.accept(this);
        checkInt(n.exp.pos, t);
        return null;
    }

    // - ensure that the RHS expression in each assignment statement is
//   type-compatible with its corresponding LHS
//   - also checks that the LHS an lvalue (The left side has to be a variable)

    public Object visit(Assign n)
    {
        Type t1 = (Type)n.lhs.accept(this);
        Type t2 = (Type)n.rhs.accept(this);
        checkSubtype(n.rhs.pos, t2, t1);
        if(!(n.lhs instanceof IdentifierExp) && !(n.lhs instanceof InstVarAccess) && !(n.lhs instanceof ArrayLookup)) {
            errorMsg.error(n.pos, new AssignmentError());
        }
        return null;
    }

    public Object visit(LocalVarDecl n)
    {
        Type t = (Type)n.initExp.accept(this);
        checkSubtype(n.initExp.pos, t, n.type);
        return visit((VarDecl)n);
    }

}

