import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random; 
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
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

        //Pipes
        int pipeX = boardWidth;
        int pipeY = 0; //pipe will start at the top and right side of the screen
        int pipeWidth = 64;
        int pipeHeight = 512;  //scaled by 1/6

        class Pipe{
            int x = pipeX;
            int y = pipeY;
            int width = pipeWidth;
            int height = pipeHeight;
            Image img;
            boolean passed = false;

            Pipe(Image img){
                this.img = img;
            }
        }


        //Game Logic
        Bird bird;
        int velocityX = -4; //move pipes to the left speed(simulates bird moving right)
        int velocityY = 0;// jump velocity in the -y direction (up_)
        int gravity = 1; // gravity effect

        ArrayList<Pipe> pipes; //Store the pipes in a list
        Random random = new Random();
        
        Timer gameLoop;
        Timer placePipesTimer;

        boolean gameOver = false;
        double score = 0;

        FlappyBird(){
            setPreferredSize(new Dimension(boardWidth, boardHeight));
            //setBackground(Color.blue);
            setFocusable(true); //make sure that flappybird class is the one that get the key events
            addKeyListener(this);

            //load images
            backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
            birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
            topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

            //bird 
            bird = new Bird(birdImg);
            pipes = new ArrayList<Pipe>();
            //place pipes timer
            placePipesTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    placePipes();
                }
            });
            placePipesTimer.start();
            //game timer
            gameLoop = new Timer(1000/60, this);
            gameLoop.start();

        }

        public void placePipes(){
            //randomize the pipe height
            //random number between 0-256
            //0 - 128 -(0-256) --> 1/4 pipeHeight -?3/4 pipeHeight
            int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
            int openingSpace = boardHeight/4;



            Pipe topPipe = new Pipe(topPipeImg);
            topPipe.y = randomPipeY;
            pipes.add(topPipe);

            Pipe bottomPipe = new Pipe(bottomPipeImg);
            bottomPipe.y = topPipe.y + + pipeHeight + openingSpace;
            pipes.add(bottomPipe);

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

            //pipes
            for(int i = 0; i<pipes.size();i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            //score
            g.setColor(Color.white);
            g.setFont(new Font ("Arial", Font.PLAIN, 32));
            if(gameOver){
                g.drawString("Game Over: " + String.valueOf((int)score),10,35);
            }else{
                g.drawString(String.valueOf((int)score),10,35);
            }
            
        }

        public void move(){
            //bird 
            velocityY += gravity; // add gravity effect
            bird.y += velocityY;
            bird.y = Math.max(bird.y,0);
           
            //pipes
            for(int i=0;i<pipes.size();i++){
                Pipe pipe = pipes.get(i);
                pipe.x += velocityX;

                if(collision(bird,pipe)){
                    gameOver = true;
                }
                if(!pipe.passed && bird.x > pipe.x + pipe.width){
                    pipe.passed = true;
                    score+=0.5;
                    Toolkit.getDefaultToolkit().beep(); // Play a beep sound
             
                }
            }

            if(bird.y > boardHeight){
                gameOver = true;
            }

  
    
        }

        public boolean collision(Bird a, Pipe b){
            return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y; //collision formula
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            repaint();
            if(gameOver){
                placePipesTimer.stop();
                gameLoop.stop();
               
            }
            
        }

   

        @Override
        public void keyPressed(KeyEvent e) {
           if(e.getKeyCode() == KeyEvent.VK_SPACE){ //jump if space bar is pressed
                velocityY = -13;

                if(gameOver){
                    //reset game
                    pipes.clear();
                    bird.y = birdY;
                    velocityY = 0;
                    score = 0;
                    gameOver = false;
                    gameLoop.start();
                    placePipesTimer.start();
                }
           }
          
        }

        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
        
}
