package visitor;

import syntaxtree.*;
import java.util.HashMap;

public class InitPredefined
{

    // returns a dummy symbol table
    static HashMap<String,ClassDecl> initEnv(ClassDeclList classes)
    {
        HashMap<String,ClassDecl> env = new HashMap<String,ClassDecl>();

        ClassDecl obj = createClass("Object", "");
        env.put("Object",  obj);
        classes.add(obj);

        ClassDecl str = createClass("String", "Object");
        str.superLink = obj;
        env.put("String",  str);
        classes.add(str);

        ClassDecl lib = createClass("Lib", "Object");
        lib.superLink = obj;
        env.put("Lib",  lib);
        classes.add(lib);

        ClassDecl run = createClass("RunMain", "Object");
        run.superLink = obj;
        env.put("RunMain",  run);
        classes.add(run);

        addMethod(obj, "hashCode",    "int",     params());
        addMethod(obj, "equals",      "boolean", params("Object"));
        addMethod(obj, "toString",    "String",  params());
        addMethod(lib, "readLine",    "String",  params());
        addMethod(lib, "readInt",     "int",     params());
        addMethod(lib, "readChar",    "int",     params());
        addMethod(lib, "printStr",    "void",    params("String"));
        addMethod(lib, "printBool",   "void",    params("boolean"));
        addMethod(lib, "printInt",    "void",    params("int"));
        addMethod(lib, "intToString", "String",  params("int"));
        addMethod(lib, "intToChar",   "String",  params("int"));
        addMethod(str, "hashCode",    "int",     params());
        addMethod(str, "equals",      "boolean", params("Object"));
        addMethod(str, "toString",    "String",  params());
        addMethod(str, "concat",      "String",  params("String"));
        addMethod(str, "substring",   "String",  params("int","int"));
        addMethod(str, "length",      "int",     params());
        addMethod(str, "charAt",      "int",     params("int"));
        addMethod(str, "compareTo",   "int",     params("String"));

        superMethod(str,obj,"toString");
        superMethod(str,obj,"hashCode");
        superMethod(str,obj,"equals");

        return env;
    }

    private static void superMethod(ClassDecl t, ClassDecl s, String m)
    {
        t.methodEnv.get(m).superMethod = t.methodEnv.get(m);
    }

    private static ClassDecl createClass(String name, String superName)
    {
        return new ClassDecl(-1, name, superName, new DeclList());
    }

    private static VarDeclList params()
    {
        return new VarDeclList();
    }
    private static VarDeclList params(String type)
    {
        VarDeclList params = new VarDeclList();
        params.addElement(new FormalDecl(-1, type(type), "param0"));
        return params;
    }
    private static VarDeclList params(String t1, String t2)
    {
        VarDeclList params = new VarDeclList();
        params.addElement(new FormalDecl(-1, type(t1), "param0"));
        params.addElement(new FormalDecl(-1, type(t2), "param1"));
        return params;
    }

    private static void addMethod(ClassDecl c, String name,
                                  String retType, VarDeclList params)
    {
        StatementList sl = new StatementList();
        MethodDecl m;
        if (retType.equals("void"))
        {
            m = new MethodDeclVoid(-1, name, params, sl);
        }
        else
        {
            Type t = type(retType);
            m = new MethodDeclNonVoid(-1, t, name, params, sl, retExp(t));
        }
        c.decls.addElement(m);
        c.methodEnv.put(name,m);
    }

    private static Type type(String s)
    {
        switch(s)
        {
            case "void":    return new VoidType(-1);
            case "boolean": return new BooleanType(-1);
            case "int":     return new IntegerType(-1);
            default:        return new IdentifierType(-1, s);
        }
    }

    private static Exp retExp(Type t)
    {
        if (t instanceof IntegerType)
            return new IntegerLiteral(-1,0);
        else if (t instanceof BooleanType)
            return new False(-1);
        else
            return new Null(-1);
    }

}
