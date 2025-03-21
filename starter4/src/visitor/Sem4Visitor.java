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
// - ensure that for each instance variable access (e.g., abc.foo),
//   there is an instance variable defined for the given class (or
//   in a superclass
//   - sets the 'varDec' link in the InstVarAccess to refer to the
//     method
// - ensure that the RHS expression in each assignment statement is
//   type-compatible with its corresponding LHS
//   - also checks that the LHS an lvalue
// - ensure that if a method with a given name is defined in both
//   a subclass and a superclass, that they have the same parameters
//   (with identical types) and the same return type
// - ensure that the declared return-type of a method is compatible
//   with its "return" expression
// - ensuring that the type of the control expression for an if- or
//   while-statement is boolean
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
    }

    public Object matchingError(int pos, Type t1, Type t2) {
        errorMsg.error(pos, new IncompatibleTypeError(t1, t2));
        return null;
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
        if(!t.isArray()) {
            errorMsg.error(pos, new TypeMismatchError(t, new ArrayType(-1, ObjectType)));
            return false;
        }
        return true;
    }

    public Object compatibilityError(int pos, Type t1, Type t2) {
        if(t1.isID()) {
            if(t2.isID()) {
                IdentifierType originalType = (IdentifierType)t1;
                IdentifierType compatabilityType = (IdentifierType)t2;
                if(!originalType.link.name.equals(compatabilityType.name)) {
                    //error
                    return null;
                }
            }
            // error
            return null;
        }
        return null;
    }

    // Check to see if t1 is a subtype of t2
    public Object checkSubtype(int pos, Type t1, Type t2) {
        if(t1.isID()) {
            if(t2.isID()) {
                IdentifierType originalType = (IdentifierType)t1;
                IdentifierType inheritenceType = (IdentifierType)t2;
                if(originalType.link.name.equals(inheritenceType.name)) {
                    // originalType is subtype of inheritenceType
                } else if(originalType.link.superLink != null) {
                    ClassDecl c = originalType.link;
                    while(c.superLink != null) {
                        if(c.superLink.name.equals(inheritenceType.name)) {
                            // originalType is a subtype of inheritenceType
                        }
                        c = c.superLink;
                    }
                }
            }
        }
        return null;
    }

    public Object expectedError(int pos, Type expected, Type actual) {
        errorMsg.error(pos, new TypeMismatchError(expected, actual));
        return null;
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
        Boolean l = checkBoolean(n.left.pos, t1);
        Boolean r = checkBoolean(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Bool;
        return n.type;
    }

    public Object visit(LessThan n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Bool;
        return n.type;
    }

    public Object visit(GreaterThan n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Bool;
        return n.type; 
    }
    public Object visit(Minus n)       
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Int;
        return n.type;
    }

    public Object visit(Or n)          
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkBoolean(n.left.pos, t1);
        Boolean r = checkBoolean(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Bool;
        return n.type;
    }

    public Object visit(Plus n)        
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Int;
        return n.type;
    }

    public Object visit(Times n)       
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Int;
        return n.type;
    }

    public Object visit(Divide n)      
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Int;
        return n.type;
    }

    public Object visit(Remainder n)   
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        Boolean l = checkInt(n.left.pos, t1);
        Boolean r = checkInt(n.right.pos, t2);
        if(!l || !r) {
            return null;
        }
        n.type = Int;
        return n.type; 
    }

    public Object visit(Equals n) 
    { 
        Type t1 = (Type)n.left.accept(this);
        Type t2 = (Type)n.right.accept(this);
        if(t1.isBoolean()) {
            if(t2.isBoolean()) {
                n.type = Bool;
                return n.type; 
            }
        } else if(t1.isInt()) {
            if(t2.isInt()) {
                n.type = Bool;
                return n.type; 
            }
        } 
        return matchingError(n.pos, t1, t2);
    }

    public Object visit(Not n)         
    { 
        Type t = (Type)n.exp.accept(this);
        if(!checkBoolean(n.exp.pos, t)) {
            return null;
        }
        n.type = Bool;
        return n.type; 
    }

    public Object visit(ArrayLength n) 
    { 
        Type t = (Type)n.exp.accept(this);
        if(!checkArray(n.exp.pos, t)) {
            return null;
        }
        n.type = Int;
        return n.type; 
    }

    public Object visit(ArrayLookup n)
    {
        Type t1 = (Type)n.arrExp.accept(this);
        Type t2 = (Type)n.idxExp.accept(this);
        Boolean isArray = checkArray(n.arrExp.pos, t1);
        Boolean isInt = checkInt(n.idxExp.pos, t2);
        if(!isArray || !isInt) {
            return null;
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
        n.obj.accept(this);

        if(currentClass.methodEnv.containsKey(n.methName)) {
            n.methodLink = currentClass.methodEnv.get(n.methName);
        }
        ClassDecl c = currentClass;
        while(c.superLink != null) {
            if(c.superLink.methodEnv.containsKey(n.methName)) {
                n.methodLink = c.superLink.methodEnv.get(n.methName);
            }
            c = c.superLink;
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
            return null;
        }
        for(int i = 0; i < callFormals.size(); i++) {
            if(!callFormals.get(i).type.equals(methodFormals.get(i).type)) { // Fix to check for all equivalences
                //error
                return null;
            }
        }
        return n.type;
    }

    public Object visit(Cast n)
    {
        Type t1 = (Type)n.castType.accept(this);
        Type t2 = (Type)n.exp.accept(this);
        // if(cast type and expression type) {
            // check if cast type is compatible with expression type
        // }
        n.type = t2;
        return n.type;
    }

    public Object visit(InstVarAccess n)
    {
        Type t = (Type)n.exp.accept(this);
        //n.varDecl // Must link to the instance varDecl that is be accessed

        //n.type = // Find some way to get the type of whatever the class that the instance variable is in
        return n.type;
    }

    public Object visit(InstanceOf n)
    {
        Type t1 = (Type)n.exp.accept(this);
        Type t2 = (Type)n.checkType.accept(this);
        // Check compatability
        n.type = Bool;
        return n.type;
    }

    // Helper method to return the proper type
    // public Type isType(Type t1) {

    // }

    public Object visit(NewArray n)
    {
        Type t1 = (Type)n.objType.accept(this);
        Type t2 = (Type)n.sizeExp.accept(this);
        // if(!t1.) {
        //     return expectedError(n.pos, t1, Int); // Need to figure out what the type is. Maybe use ArrayTypeError
        // }
        if(!checkInt(n.sizeExp.pos, t2)) {
            return null;
        }
        n.type = t1;
        return n.type;
    }

    public Object visit(NewObject n)
    {
        Type t = (Type)n.objType.accept(this);
        // if(!isType(t)) {
        //     return not a type error
        // }
        n.type = n.objType;
        return n.type;
    }

}

