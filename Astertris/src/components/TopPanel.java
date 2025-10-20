package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import main.GameBoard;
import utils.Toolbox;

public class TopPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 8039501377763181612L;
	
	private int width,height;
	private GameBoard game;
	private Toolbox toolbox = new Toolbox();
	
	public TopPanel(GameBoard game, int width, int height) {
		this.game = game;
		
		this.width = width;
		this.height = height;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setColor(new Color(127,127,127));
		g2d.fillRect(0,height-2,width,2);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(toolbox.getFont(Font.PLAIN, 20));
		g2d.drawString("SCORE: "+toolbox.renderInt(6,game.getScore()),10,25);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(toolbox.getFont(Font.PLAIN, 20));
		g2d.drawString("LINES: "+toolbox.renderInt(3,game.getLinesCleared()),570,25);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
