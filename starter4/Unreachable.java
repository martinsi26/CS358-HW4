class Main {
    public void main() {
    	while (false) { // this open-brace is unreachable
    		int x = 0;
    	}
    	while (true) {
    	}
    	int z = 0; // this statement is unreachable
    }
    public void test(int n) {
	while (n < 0) {
	    if (n > 0) {
		break;
		n++; // this statement is unreachable
		break;
	    }
	    else {
		while (true) {
		}
	    }
	    n--; // this statement is unreachable
	}
    }
}

