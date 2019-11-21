package id.ac.its.ilham.movingSprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int ICRAFT_X = 40;
    private final int ICRAFT_Y = 60;
    private final int DELAY = 10;
    private final int AS_NUM = 200;
    private final int AS_SPAWN_AREA = 10000;
    private Image bg;
    private Image gameOver;
    private boolean inGame;
    
    private Timer timer;
    private SpaceShip spaceShip;
    private List<Asteroid> asteroids;
    
    private Random random = new Random();
        
    public Board() {
    	
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        //setBackground(Color.BLACK);
        bg = Toolkit.getDefaultToolkit().createImage("src/resources/background-sheet0.png");
        gameOver = Toolkit.getDefaultToolkit().createImage("src/resources/gameover-sheet0.png");
        
        setFocusable(true);
        
        inGame = true;
        spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        asteroids = new ArrayList<>();
        initAsteroid();       
        
        timer = new Timer(DELAY, this);
        timer.start();
        
    }

	private void initAsteroid() {		
		for(int i=0; i<AS_NUM; i++) {
			asteroids.add(new Asteroid(random.nextInt(AS_SPAWN_AREA)+600,random.nextInt(225)));
		}
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(inGame)doDrawing(g);
        else drawGameOver(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
	
	private void drawGameOver(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(bg,0,0,null);
        g2d.drawImage(gameOver,50,50,this);
        
	}

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(bg,0,0,null);
        
        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(),
                spaceShip.getY(), this);

        List<Missile> missiles = spaceShip.getMissiles();      
        
        for (Missile missile : missiles) {
            
            g2d.drawImage(missile.getImage(), missile.getX(),
                    missile.getY(), this);
        }
        
        for (Asteroid asteroid : asteroids) {
			g2d.drawImage(asteroid.getImage(),asteroid.getX(),
					asteroid.getY(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    	checkGameOver();
        updateMissiles();
        updateSpaceShip();
        updateAsteroids();
        collisionChecks();
        
        repaint();
    }

	private void checkGameOver() {
		if(!inGame) {
			timer.stop();
		}
	}

	private void collisionChecks() {
		
		for (Asteroid asteroid : asteroids) {

			if(spaceShip.getX() + 40 >= asteroid.getX()
				&& spaceShip.getX() <= asteroid.getX() +75
				&& spaceShip.getY() + 30 >= asteroid.getY() 
				&& spaceShip.getY() <= asteroid.getY() + 50) {	
				
				spaceShip.setVisible(false);
				asteroid.setVisible(false);
				inGame = false;
			}
			
			 List<Missile> missiles = spaceShip.getMissiles();   
			 
			 for (Missile missile : missiles) {
				 if(missile.getX() + 10 >= asteroid.getX()
					&& missile.getX() <= asteroid.getX() +60
					&& missile.getY() + 15 >= asteroid.getY() 
					&& missile.getY() <= asteroid.getY() + 60) {	
						
					missile.setVisible(false);
					asteroid.setVisible(false);
				}	
			}
		}
	}

	private void updateMissiles() {

        List<Missile> missiles = spaceShip.getMissiles();

        for (int i = 0; i < missiles.size(); i++) {

            Missile missile = missiles.get(i);

            if (missile.isVisible()) {

                missile.move();
            } else {

                missiles.remove(i);
            }
        }
    }
    
    private void updateAsteroids() {
    	    	
    	for(int i = 0; i<asteroids.size(); i++) {
    		Asteroid asteroid = asteroids.get(i);
    		
    		if(asteroid.isVisible()) {
    			
    			asteroid.move();
    		} else {
    		
    			asteroids.remove(i);
    		}
    	}
    }
    

    private void updateSpaceShip() {

        spaceShip.move();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            spaceShip.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            spaceShip.keyPressed(e);
        }
    }
}