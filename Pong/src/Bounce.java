// package Pong;

public class Bounce {
    public static void main(String[] args){
        // String m = "gravity";
        String m = "wall_to_wall";

        Ball b1 = new Ball(m);

        int time = 5;

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
    final private String mode;
    private double A;
    private String shape;

    Ball(String m){
        mode = m;
        switch(mode){
            case "gravity":
                A = -150;
                v = 0;
                x = 100;
            break;
            case "wall_to_wall":
                A = 0;
                v = 80;
                x = 50;
            break;
        }
        dt = 0.001;
        shape = "O";
    }
    void print(){
        String to_print = "\r";

        switch(mode){
        case "gravity":
            if (x < 2){
                shape = "0";
            } else {
                shape = "O";
            }

            for (int i = 0; i < 120; i++){
                if (i == (int) x){
                    to_print += shape;
                } else {
                    to_print += " ";
                }
            }
            System.out.print(to_print);
        break;
        case "wall_to_wall":
            if (x < 2 || x > 98){
                shape = "0";
            } else {
                shape = "O";
            }

            to_print += "|";
            for (int i = 0; i < 99; i++){
                if (i == (int) x){
                    to_print += shape;
                } else {
                    to_print += " ";
                }
            }
            to_print += "|";
            System.out.print(to_print);
        break;
        }
    }
    void update(){
        /** Ball.update() method: use kinematics equations
         * to compute/update new values for x, v
         * Behavior is changed w/r to this.mode
         */
        x += (v*dt + 0.5*A*dt*dt);

        switch(mode){
            case "gravity":
                if (x < 1){  // if near the ground, invert velocity
                    v *= -1;
                } else {
                    v += (A*dt);  // change velocity normally
                }
            break;
            case "wall_to_wall":
                if (x < 1 || x > 99){
                    v *= -1;
                }
            break;
        }
    }
}