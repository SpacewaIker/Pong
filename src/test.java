import acm.program.*;
import acm.graphics.*;
import java.awt.Color;

public class test extends GraphicsProgram{
    public static void main(String[] args){
        new test().start();
    }
    public void run(){
        String a = "yes";

        println(a.substring(0, 1).toUpperCase() + a.substring(1));
    }
}