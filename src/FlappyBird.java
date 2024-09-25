import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random; 
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImage;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int height = birdHeight;
        int width = birdWidth;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

        //Game Logic
        Bird bird;
        int velocityY = -9;// jump velocity in the -y direction (up_)
        int gravity = 1; // gravity effect
        
        Timer gameLoop;

        FlappyBird(){
            setPreferredSize(new Dimension(boardWidth, boardHeight));
            //setBackground(Color.blue);

            //load images
            backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
            birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
            topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

            //bird 
            bird = new Bird(birdImg);

            //game timer
            gameLoop = new Timer(1000/60, this);
            gameLoop.start();
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            draw(g);
        }  
        
        public void draw(Graphics g){
            System.out.println("Drawing");
            //background
            g.drawImage(backgroundImage,0,0,boardWidth, boardHeight, null);

            //bird
            g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);
        }

        public void move(){
            //bird 
            velocityY += gravity; // add gravity effect
            bird.y += velocityY;
            bird.y = Math.max(bird.y,0);
           
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            repaint();
            
        }
        
}
