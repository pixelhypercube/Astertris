package main;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import components.BoardPanel;
import utils.Direction;

public class Astertris extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 5953697399851869782L;
	final static int WIDTH = 51, HEIGHT = 51, PLANET_SIZE = 3; 
	static GameBoard game = new GameBoard(WIDTH,HEIGHT,PLANET_SIZE,0);
	
//	components
	private JLabel titleLabel, scoreLabel;
//	private JLabel boardStrLabel;
	private BoardPanel boardPanel;
//	private String boardStr;
	
	public Astertris() {
		setTitle("Astertris");
		setSize(560,650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setFocusable(true);
		setLayout(null);
		setVisible(true);
		
		
//		components
		
		titleLabel = new JLabel("Astertris (Beta)");
		titleLabel.setBounds(130, 10, 300, 40);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
	    add(titleLabel);
		
	    scoreLabel = new JLabel("Score: " + game.getScore());
	    scoreLabel.setBounds(20, 60, 200, 30);
	    scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
	    add(scoreLabel);
	    
	    titleLabel = new JLabel("Made by @pixelhypercube!");
		titleLabel.setBounds(330, 60, 250, 40);
	    titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	    add(titleLabel);
		
	    boardPanel = new BoardPanel(game);
	    add(boardPanel);
	    boardPanel.setBounds(20, 90, 500, 500);
	    boardPanel.repaint();
	    
//	    update timer for tetromino to sink by gravity
	    Timer timer = new Timer(300, e -> {
	    	game.updateTetromino();
	    	this.updateBoard();
	    });
	    timer.start();
	}
	
	public void updateBoard() {
		game.updateBoard();
    	boardPanel.repaint();
    	scoreLabel.setText("Score: " + game.getScore());
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (key) {
			case KeyEvent.VK_A:
				game.moveCurrTetromino(Direction.RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				game.rotateTetromino(Direction.COUNTERCLOCKWISE);
				break;
			case KeyEvent.VK_D:
				game.moveCurrTetromino(Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				game.rotateTetromino(Direction.CLOCKWISE);
				break;
			case KeyEvent.VK_SPACE:
				game.updateTetromino();
				break;
//			DEBUG ONLY: type 'P' to place
//			case KeyEvent.VK_P:
//				game.placeTetromino();
//				break;
		}
		
		this.updateBoard();
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		new Astertris();
	}
}
