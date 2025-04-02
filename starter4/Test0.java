// unreachable code: statement following break
 //import lib.*;

 class Main { // class Test885 {
  public void main() {
     new Test885a().exec();
   }
 }
 class Test885a {
   public   void exec() {
     int a = 4;
       while (a > 0) {
          break; a = 0;  
     }
       /**/
   }
   public void foo() {}
 }