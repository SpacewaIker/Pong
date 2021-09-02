package Pong;

public class Bounce {
    public static void main(String[] args){
        Ball b1 = new Ball(-150);

        int time = 10;

        for (double t = 0; t < time; t += b1.dt){
            b1.update();
            b1.print();
            try {
                Thread.sleep((int)(b1.dt*1000));
            } catch(InterruptedException ex) {}
        }
    }
}

class Ball {
    private double x, v;
    double dt;
    final private double A;
    private String shape;

    Ball(double acc){
        x = 100;
        v = 0;
        A = acc;
        dt = 0.001;
        shape = "O";
    }
    void print(){
        if (x < 2){
            shape = "0";
        } else {
            shape = "O";
        }

        String to_print = "\r";

        for (int i = 0; i < 120; i++){
            if (i == (int) x){
                to_print += shape;
            } else {
                to_print += " ";
            }
        }
        System.out.print(to_print);
    }
    void update(){
        /** Ball.update() method: use kinematics equations
         * to compute/update new values for x, v
         */
        x += (v*dt + 0.5*A*dt*dt);

        if (x < 1){  // if near the ground, invert velocity
            v = -v;
        } else {
            v += (A*dt);  // change velocity normally
        }

    }
}