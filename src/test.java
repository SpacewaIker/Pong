import acm.program.*;
import acm.graphics.*;
import java.awt.Color;

public class test extends GraphicsProgram{
    public static void main(String[] args){
        new test().start();
    }
    public void run(){
        number mynum = new number(20);
        mynum.print();
        // mynum.number(10);
    }
}
class number{
    int a;
    int b;
    number(int a){
        this.a = a;
        this.b = 1;
    }
    public void method(){
        this.a = 100;
        this.b = 100;
    }
    public void print(){
        System.out.println(a + " " + b);
    }
}