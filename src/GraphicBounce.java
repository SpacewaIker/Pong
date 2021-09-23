import acm.program.*;
import acm.graphics.*;

public class GraphicBounce extends GraphicsProgram{
    public static void main(String[] args){
        new GraphicBounce().start();
    }
    public void init(){
        setTitle("Bouncing Ball");
    }
    public final int V = 8;
    public int dir = 1;

    public void run(){
        GOval ball = new GOval(400, getHeight() / 2, 20, 20);
        ball.setFilled(true);
        add(ball);

        // for (double t = 0; t < 10; t += 0.01){
        while (true) {
            double x = ball.getBounds().getX();
            double y = ball.getBounds().getY();
            if ((x <= 0) || (x >= getWidth() - 20)){
                dir *= -1;
            }
            ball.move(V*dir, getHeight()/2 - y);

            pause(10);
        }
    }
}
