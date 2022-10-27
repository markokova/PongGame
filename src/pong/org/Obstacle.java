package pong.org;

import java.awt.*;
import java.util.Random;

public class Obstacle extends Rectangle {
    Random random;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 3;

    Obstacle(int xVelocity, int yVelocity, int width, int height){
        super(xVelocity,yVelocity,width,height);
        Random random = new Random();
        int randomXDirection = random.nextInt(2);
        if(randomXDirection == 0)
            randomXDirection--;
        setXDirection(randomXDirection*initialSpeed);

        int randomYDirection = random.nextInt(2);
        if(randomYDirection == 0)
            randomYDirection--;
        setYDirection(randomYDirection*initialSpeed);
    }

    public void setXDirection(int randomXDirection){
        xVelocity = randomXDirection;
    }

    public void setYDirection(int randomYDirection){
        yVelocity = randomYDirection;
    }

    public void move(){
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g){
        g.setColor(Color.red);
        g.fillOval(x,y,height,width);
    }
}
