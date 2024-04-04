package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    private Image apple;
    private Image dot;
    private Image head;
    private final int ALL_DOTS = 250000;
    private final int DOT_SIZE = 10;
    private final int [] x = new int[ALL_DOTS];
    private final int [] y = new int[ALL_DOTS];
    private int dots;
    private final int RANDOM_POSITION = 43;
    private int apple_x;
    private int apple_y;
    private Timer timer;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private int[] xPos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450};
    private int[] yPos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450};
    private int enemyX,enemyY;
    private Random random = new Random();
    private int Score = 0;
    Board(){
        addKeyListener(new TAdapter());
        setBackground(new Color(27, 1, 107));
        setPreferredSize(new Dimension(500,500));
        setFocusable(true);
        loadImages();
        initGame();
        ;    }

    public void loadImages(){
        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = img1.getImage();
        ImageIcon img2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = img2.getImage();
        ImageIcon img3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head = img3.getImage();

    }
    public void initGame(){
        dots = 3;
        for(int i=0;i<dots;i++){
            y[i] = 100;
            x[i] = 100 - i * DOT_SIZE;
        }
        locateApple();
        timer = new Timer(100,this);
        timer.start();
    }
    public void locateApple(){
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int)(Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
            setScore(g);

        }else{
            gameOver(g);
        }
    }
    public void setScore(Graphics g){
        g.setFont(new Font("Arial",Font.PLAIN,16));
        g.setColor(Color.WHITE);
        Score = dots-3;
        g.drawString("Score: "+Score,DOT_SIZE - 10 , DOT_SIZE+1);
    }
    public void gameOver(Graphics g){
        String msg = "Game Over!";
        setBackground(new Color(0,0,0));
        Font font = new Font("SAN_SERIF",Font.BOLD,45);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg,(500 - metrics.stringWidth(msg))/2, 215);
        g.setFont(new Font("SAN_SERIF",Font.BOLD,45));
        g.drawString("Your Score: "+ Score,(500 - metrics.stringWidth(msg))/2 - 20, 260);
        g.setFont(new Font("SAN_SERIF",Font.PLAIN,20));
        g.drawString("Press ENTER to Restart",(500 - metrics.stringWidth(msg))/2 + 20, 290);
    }
    public void move(){
        for(int i=dots;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= DOT_SIZE;
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }
    public void checkApple(){
        if((apple_x == x[0] )&&( apple_y == y[0])){
            dots++;
            locateApple();
        }
    }
    public void checkCollision(){
        for(int i=dots;i>=0;i--){
            if((i > 4) && (x[0] == x[i]) && (y[0] == y[i])){
                inGame = false;
            }
            if(y[i] > 500 ){
                y[i] = 0;
                if(y[0] > 500) {
                    upDirection = false;
                    leftDirection = false;
                    rightDirection = false;
                }
            }
            if( y[i] < 0){
                y[i] = 500;
                if(y[0] < 0) {
                    downDirection = false;
                    leftDirection = false;
                    rightDirection = false;
                }
            }
            if(x[i] > 500){
                x[i] = 0;
                if(x[0] > 500) {
                    downDirection = false;
                    leftDirection = false;
                    upDirection = false;
                }
            }
            if(x[i] < 0){
                x[i] = 500;
                if(y[0] < 0) {
                    downDirection = false;
                    upDirection = false;
                    rightDirection = false;
                }
            }
            if(!inGame){
                timer.stop();
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            int key =  e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){
                rightDirection = false;
                upDirection = true;
                leftDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){
                rightDirection = false;
                downDirection = true;
                leftDirection = false;
            }
            if((key == KeyEvent.VK_ENTER) && (!inGame)){
                restart();
            }
        }
    }
    private void restart(){
        inGame = true;
        dots = 3;
        Score = 0;
        rightDirection = true;
        leftDirection = false;
        upDirection = false;
        downDirection = false;
        initGame();
        setBackground(new Color(27, 1, 107));
        repaint();
    }
}

