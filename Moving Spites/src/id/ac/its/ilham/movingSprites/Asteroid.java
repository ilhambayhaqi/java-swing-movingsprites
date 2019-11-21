package id.ac.its.ilham.movingSprites;

import java.util.Random;

public class Asteroid extends Sprite {
	
	private Random random = new Random();
	private int ASTEROID_SPEED;
	
	public Asteroid(int x, int y) {	
		super(x, y);
    	initAsteroid();
    }
    
    private void initAsteroid() {
    	ASTEROID_SPEED = random.nextInt(2)+1;
        loadImage("src/resources/meteor-sheet0.png");  
        getImageDimensions();
    }
    
    public void move() {
        
        x -= ASTEROID_SPEED;
        
        if (x < -100) {
            visible = false;
        }
    }
       
}

