package decorations;

import java.awt.Color;
import java.awt.Graphics2D;

public class Star extends Particle {
	public Star(int x, int y, int w, int h, double vx, double vy, Color color) {
		super(x,y,w,h,vx,vy,color);
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(this.color);
		g2d.fillOval((int)this.x, (int)this.y, this.w, this.h);
	}
}
