package decorations;

import java.awt.Color;
import java.awt.Graphics;

public class Star extends Particle {
	public Star(int x, int y, int w, int h, double vx, double vy, Color color) {
		super(x,y,w,h,vx,vy,color);
	}
	
	public void render(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.x, this.y, this.w, this.h);
	}
}
