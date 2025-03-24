class Parent {
    int x;
    Parent p;
}

class Child extends Parent {
    String y;
}

class Main {
    public void main() {
        Child e = new Child();

        int value = e.x; 
    }
}