package pong.org;
import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame{

    GamePanel panel;

    /*
    * htio bih da na pocetku bude izbornik na kojem mozes birati tezinu:
    * easy - no obstacle
    * medium - one obstacle
    * hard - two obstacles
    * good luck - five obstacles
    * */


    GameFrame(){
        panel = new GamePanel();
        this.add(panel);
        this.setTitle("Pong game");
        this.setResizable(false);//so that people can't resize the frame
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //zatvaranje pomoću X-a
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); //frame in the middle of the screen
    }

}
