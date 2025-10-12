package decorations;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints; 

import utils.Toolbox;

public class Asteroid extends Particle {

	private Image asteroidImage;
	private int asteroidId;
	private double rotAngle = 0.0;
	private double dRotAngle;
	
	public Asteroid(int x, int y, int w, int h, double vx, double vy, int asteroidId, double dRotAngle) {
		super(x, y, w, h, vx, vy);
		this.asteroidId = asteroidId;
		this.dRotAngle = dRotAngle;

		Toolbox toolbox = new Toolbox();
		this.asteroidImage = toolbox.getImage("asteroid_"+asteroidId+".png");
	}
	
	public void update() {
		super.update();
		this.rotAngle += this.dRotAngle;
	}
	
	public void render(Graphics2D g2d) {
		
		java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
	
		double cX = x + w/2.0;
		double cY = y + h/2.0;
		g2d.translate(cX, cY);
		
		g2d.rotate(rotAngle);
	
		g2d.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
	        RenderingHints.VALUE_INTERPOLATION_BILINEAR
	    );
		
		g2d.drawImage(asteroidImage, -w/2, -h/2, w, h, null);
		g2d.setTransform(oldTransform);
		
		//DEBUG
//		System.out.println(x + " " + y);
//		System.out.println(w + " " + h);
//		g2d.setColor(Color.WHITE);
//		g2d.fillRect(x,y,w,h);
	}

}
