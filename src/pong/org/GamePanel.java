package pong.org;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    ArrayList<Obstacle> obstacles;
    Score score;
    GamePanel(){
        newPaddles();
        newBall();
        newObstacle();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true); // what does this mean?
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2) - (BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT)/2,PADDLE_WIDTH, PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT)/2,PADDLE_WIDTH, PADDLE_HEIGHT,2);
    }

    public void newObstacle(){
        obstacles = new ArrayList<Obstacle>();
        for(int i = 0; i < 3; i++){// umjesto do 3 mislim da bih trebao uvesti novu klasu "menu" i na temelju izbora odabrati broj prepreka
            obstacles.add(new Obstacle((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt((GAME_HEIGHT - BALL_DIAMETER)), BALL_DIAMETER, BALL_DIAMETER));
            //obstacles.set(i, new Obstacle((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt((GAME_HEIGHT - BALL_DIAMETER)), BALL_DIAMETER, BALL_DIAMETER));
        }
    }

    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        for(int i = 0; i < 3; i++){ // umjesto do 3 mislim da bih trebao uvesti novu klasu "menu" i na temelju izbora odabrati broj prepreka
            obstacles.get(i).draw(g);
        }
        score.draw(g);
    }

    public void move(){
        paddle1.move();//for smoother moves
        paddle2.move();
        ball.move();
        for(int i = 0; i < 3; i++){ // umjesto do 3 mislim da bih trebao uvesti novu klasu "menu" i na temelju izbora odabrati broj prepreka
            obstacles.get(i).move();
        }
    }

    public void checkCollision(){
        //bounce ball off top & bottom window edges
        if(ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= GAME_HEIGHT - (BALL_DIAMETER)){
            ball.setYDirection(-ball.yVelocity);
        }
        //bounces ball of paddles
        if(ball.intersects(paddle1)){
            ball.setXDirection(-ball.xVelocity);
            ball.xVelocity+=2; //optional: the ball will travel faster after collision with paddel
            if(ball.yVelocity>0) ball.yVelocity++; //optional
            else ball.yVelocity--; //optional
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){
            ball.xVelocity+=2;
            ball.setXDirection(-ball.xVelocity);
            if(ball.yVelocity>0) ball.yVelocity++; //optional
            //else ball.yVelocity--; //optional
            //ball.setXDirection(-ball.xVelocity); //zasto -, radi bolje ali nez zasto
            ball.setYDirection(ball.yVelocity);
        }
        //stops paddles at window edges
        if(paddle1.y<= 0)
            paddle1.y = 0;
        if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if(paddle2.y<= 0)
            paddle2.y = 0;
        if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        //give a player 1 point and creates new paddles & ball
        if(ball.x <= 0){
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("P1=> " + score.player1 + " " + score.player2 + " <=P2");
        }
        if(ball.x >= GAME_WIDTH - BALL_DIAMETER){
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("P1=> " + score.player1 + " " + score.player2 + " <=P2");
        }
        //ball colides with obstacle
        /*
        * ako lopta ide prema lijevo i udari u prepreku znaci da paddle 1 dobiva bod a ako ide prema desno i udari u prepreku paddle 2 dobiva bod
        * ovo bi se moglo mozda izdvojit u metodu jer se ponavljam u gornja 2 ifa i u ovom dolje ifu
        * */
        for(int i = 0; i < 3; i++){
            if(ball.intersects(obstacles.get(i)) && ball.xVelocity > 0 ) {
                score.player2++;
                newPaddles();
                newBall();
                System.out.println("P1=> " + score.player1 + " " + score.player2 + " <=P2");
            }
            if(ball.intersects(obstacles.get(i)) && ball.xVelocity < 0){
                score.player1++;
                newPaddles();
                newBall();
                System.out.println("P1=> " + score.player1 + " " + score.player2 + " <=P2");
            }
            //for obstacle to stay on the game screen
            if(obstacles.get(i).y <= 0){
                obstacles.get(i).yVelocity *= -1;
            }
            if(obstacles.get(i).y >= (GAME_HEIGHT - BALL_DIAMETER)){
                obstacles.get(i).yVelocity *= -1;
            }
            if(obstacles.get(i).x <= 0){
                obstacles.get(i).xVelocity *= -1;
            }
            if(obstacles.get(i).x >= (GAME_WIDTH - BALL_DIAMETER)){
                obstacles.get(i).xVelocity *= -1;
            }
        }
    }

    public void run(){
        //game loop
        long lastTime = System.nanoTime();
        double ammountOfTicks = 60.0;
        double ns = 1000000000 / ammountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    //AL - Action Listener
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);

        }
    }
}
