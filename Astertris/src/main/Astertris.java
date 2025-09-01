package main;

import java.awt.Color;
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
	final static int WIDTH = 51, HEIGHT = 51, PLANET_SIZE = 5; 
	static GameBoard game = new GameBoard(WIDTH,HEIGHT,PLANET_SIZE,0);
	
//	components
	private JLabel titleLabel, scoreLabel, creditLabel, gameOverLabel;
//	private JLabel boardStrLabel;
	private BoardPanel boardPanel;
//	private String boardStr;
	
	private boolean isGameOver = false;
	
	Timer timer = new Timer(300,null);
	
	public Astertris() {
		setTitle("Astertris");
		setSize(560,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setFocusable(true);
		setLayout(null);
		setVisible(true);
		
		
//		components
		
		titleLabel = new JLabel("Astertris (Beta)");
		titleLabel.setBounds(130, 10, 300, 40);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
		
	    scoreLabel = new JLabel("Score: " + game.getScore());
	    scoreLabel.setBounds(20, 60, 200, 30);
	    scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
	    
	    creditLabel = new JLabel("Made by @pixelhypercube!");
		creditLabel.setBounds(330, 620, 250, 40);
	    creditLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	    
	    gameOverLabel = new JLabel("Game Over! Press R to restart!");
	    gameOverLabel.setBounds(140, 585, 560, 40);
	    gameOverLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    gameOverLabel.setForeground(Color.red);
	    gameOverLabel.setVisible(false);
	    
	    
	    boardPanel = new BoardPanel(game);
	    add(boardPanel);
	    boardPanel.setBounds(20, 90, 500, 500);
	    boardPanel.repaint();
		
	    
	    add(titleLabel);
	    add(scoreLabel);
	    add(creditLabel);
	    add(gameOverLabel);
	    
//	    update timer for tetromino to sink by gravity
	    
	    timer.addActionListener(e -> {
	    	game.updateTetromino();
	    	this.updateBoard();
	    	
	    	isGameOver = game.getGameOver();
	    	if (isGameOver) {
	    		timer.stop();
	    		gameOverLabel.setVisible(isGameOver);
	    	}
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
		if (!isGameOver) {
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
//				DEBUG ONLY: type 'P' to place
//				case KeyEvent.VK_P:
//					game.placeTetromino();
//					break;
			}
			
			this.updateBoard();
		} else {
			if (key==KeyEvent.VK_R) {
				game.restartGame();
				isGameOver = game.getGameOver();
				timer.start();
				gameOverLabel.setVisible(isGameOver);
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		new Astertris();
	}
}
