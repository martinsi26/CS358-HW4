// This file has a number of type errors in it.  Hopefully, your
// compiler will catch them.

class Main {
    // error: wrong number of parameters for main method in Main class
    public void main(int k) {
		new Abc().start();
		Abc[] x = new Abc[1 - 3];
		Abc y = x[1 - 3];
    }
}

class Abc {
    int xyz;
    String str;
    boolean b;
    Abc abc;
    public void start() {
	Def def = new Def();
	abc = new Abc();
	int[] z = new int[34];
	// error: created array does not have enough dimensions
	String[][] tooSmall = new String[34];
	// error: created array has too many dimensions
	String[][] tooBig = new String[17][][];
	// error: ints do not have instance variables
	xyz.foo = 4;
	// error: no instance variable named 'jack'
	abc.jack = 5;
	// error: no method named 'where'
	abc.where();
	// error: cannot call a method on a boolean
	b.start(6, "abc");
	// error: wrong # parameters
	def.start(3, "xyz", 4);

	def.start(3, "yes");

	// error: parameter-type mismatch
	def.start(3, true);
	// error: attempt to index non-array value
	def[4] = 4;
	// error: array-index must be int
	z[abc] = 34;
	// error: taking length of non-array
	xyz = abc.length;
	// error: cannot assign to subclass value
	def = new Abc();
	// error parameter-type mismatch
	xyz = false;
	// error: cannot compare void values
	b = start() == start();
	// error: assignment type incompatibility
	b = 45;
	abc = new Def();
	def = null;
	abc = null;
	// error: '+' needs two ints
	xyz = 4 + true;
	// error: '<' needs two ints
	xyz = b < false;
	// error: '&&' needs two booleans
	b = b && 3;
	// error: initializer incompatibilty
	Def def2 = 45;
	// error: cannot initialize from superclass
	Def def3 = new Abc();
	Abc abc2 = new Def();
	// error: condition must be boolean
	if (xyz) {
	    xyz = 3;
	}
	// error: condition must be boolean
	while (def) {
	    xyz = 4;
	}
	// error: condition must be boolean
	for (int i = 0; xyz+4; i++) {
	    xyz = 5;
	}
    }
    public int test() {
	return 4;
    }
}

class Def extends Abc {
    // error: signature mismatch w/superclass
    public void start(int n, String c) {
	// error: array-length must be int
	Abc[] zz = new Abc[b];
	// unrelated "instanceof" operands
	if (c instanceof Lib) {
	    // error: attempt to assign to non-l-value
	    4 = xyz;
	    // unrelated cast (array)
	    Lib lib = (Lib)zz;
	    // error: unrelated cast
	    lib = (Lib)abc;
	    Object o = (Object)"xyz";
	    // error: unrelated instanceof
	    boolean b = "honk" instanceof Lib;
	    boolean b3 = null instanceof Lib;
	    // error: indexing of non-array type
	    boolean b4 = abc[4] instanceof Lib;
	    // error: indexing of non-array type
	    Lib lib3 = (Lib)abc[4];
	}
    }
    // error: signature mismatch with superclass
    public void test(int n) {
    }
    public int myMethod() {
	// error: return-exp mismatches with method return type
	return b;
    }
}
