package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;

import javax.swing.JPanel;

import utils.Toolbox;

public class GameButton extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -7900222527749194438L;
	
	private int x,y,w,h,fontSize;
	private Rectangle buttonBounds;
	private String label, url, nextGameState;
	Color borderColor, fillColor, currFillColor, textColor;
	
	Graphics2D g2D;
	Toolbox toolbox = new Toolbox();
	private boolean visibility;
	private boolean willRestartGame;
	private boolean isPressed;
	
	public GameButton(int x, int y, int w, int h, boolean visibility, int fontSize, String label, Color borderColor, Color fillColor, Color textColor, String url, String nextGameState) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.fontSize = fontSize;
		this.buttonBounds = new Rectangle(x,y,w,h);
		this.label = label;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		
		this.currFillColor = fillColor;
		this.url = url;
		
		this.visibility = visibility;
		
		this.nextGameState = nextGameState;
		
		this.isPressed = false;
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public GameButton(int x, int y, int w, int h, boolean visibility, int fontSize, String label, Color borderColor, Color fillColor, Color textColor, String url, String nextGameState, boolean willRestartGame) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.fontSize = fontSize;
		this.buttonBounds = new Rectangle(x,y,w,h);
		this.label = label;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		
		this.currFillColor = fillColor;
		this.url = url;
		
		this.visibility = visibility;
		
		this.nextGameState = nextGameState;
		
		this.isPressed = false;
		
		this.setWillRestartGame(willRestartGame);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		if (visibility) {
			super.paintComponent(g);
			Graphics2D g2D = (Graphics2D)g;
			
			g2D.setColor(this.currFillColor);
			g2D.fillRect(this.x,this.y,this.w,this.h);
			
			g2D.setColor(this.borderColor);
			g2D.setStroke(new BasicStroke(
				2.0f,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND,
				10.0f
			));
			g2D.drawRect(this.x,this.y,this.w,this.h);
			
			
			g2D.setFont(toolbox.getFont(Font.PLAIN, this.fontSize));
			g2D.setColor(Color.WHITE);

			
			FontMetrics fm = g2D.getFontMetrics();
			String[] lines = this.label.split("\n");
			int lineHeight = fm.getHeight();

			int totalTextHeight = lineHeight*lines.length;
			int startY = this.y + (this.h-totalTextHeight)/2 + fm.getAscent();

			for (int i = 0; i < lines.length; i++) {
			    String line = lines[i];
			    int textWidth = fm.stringWidth(line);
			    int textX = this.x + (this.w-textWidth)/2;
			    int textY = startY + i*lineHeight;
			    g2D.drawString(line, textX, textY);
			}
		}
	}
	
	public Rectangle getButtonBounds() {
		return this.buttonBounds;
	}
	
	public boolean getVisibility() {
		return visibility;
	}
	
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if (this.visibility && this.buttonBounds.contains(e.getPoint())) {
			this.currFillColor = toolbox.blendColors(this.fillColor, Color.BLACK, 0.25);
			repaint();
			if (this.url!=null) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public String getNextGameState() {
		return nextGameState;
	}

	public void setNextGameState(String nextGameState) {
		this.nextGameState = nextGameState;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    if (this.visibility && this.buttonBounds.contains(e.getPoint())) {
	        this.currFillColor = toolbox.blendColors(this.fillColor, Color.BLACK, 0.25);
	        this.isPressed = true;
	        repaint();
	    }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    if (this.visibility) {
	        this.currFillColor = this.fillColor;
	        this.isPressed = false;
	        repaint();
	    }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	    if (this.visibility && this.buttonBounds.contains(e.getPoint())) {
	        this.currFillColor = toolbox.blendColors(this.fillColor, Color.WHITE, 0.25);
	        repaint();
	    }
	}

	@Override
	public void mouseExited(MouseEvent e) {
	    if (this.visibility) {
	        // Always reset when leaving
	        this.currFillColor = this.fillColor;
	        repaint();
	    }
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	    if (this.visibility) {
	        if (this.buttonBounds.contains(e.getPoint())) {
	            this.currFillColor = toolbox.blendColors(this.fillColor, Color.BLACK, 0.25);
	        } else {
	            this.currFillColor = this.fillColor;
	        }
	        repaint();
	    }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	    if (this.visibility) {
	        if (this.buttonBounds.contains(e.getPoint())) {
	            this.currFillColor = toolbox.blendColors(this.fillColor, Color.WHITE, 0.25);
	        } else {
	            this.currFillColor = this.fillColor;
	        }
	        repaint();
	    }
	}

	public boolean isWillRestartGame() {
		return willRestartGame;
	}

	public void setWillRestartGame(boolean willRestartGame) {
		this.willRestartGame = willRestartGame;
	}
	
}
