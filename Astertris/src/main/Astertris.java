package main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import components.BoardPanel;
import components.GameStateListener;
import components.NextTetPanel;
import utils.Direction;
import utils.Toolbox;

public class Astertris extends JFrame implements KeyListener, GameStateListener {
	
	private static final long serialVersionUID = 5953697399851869782L;
	final static int WIDTH = 51, HEIGHT = 51, PLANET_SIZE = 5; 
	GameBoard game;
	
//	components
//	private JLabel boardStrLabel;
	private BoardPanel boardPanel;
//	private String boardStr;
	private NextTetPanel nextTetPanel;
	
	private Toolbox toolbox;
	
	private String gameState;
	
	private final int nextTetPanelWidth = 100;
	
	Timer timer = new Timer(250,null);
	Timer starTimer = new Timer(20,null);
	
	public Astertris() {
		this.gameState = "home";
		this.toolbox = new Toolbox();
		
		setTitle("Astertris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setFocusable(true);
		setLayout(null);
		setVisible(true);
		setResizable(false);
		
//		components
		
		// init game
	    game = new GameBoard(WIDTH,HEIGHT,PLANET_SIZE,0);
	    
	    nextTetPanel = new NextTetPanel(game,nextTetPanelWidth,600);
	    add(nextTetPanel);
	    nextTetPanel.setBounds(600,0,nextTetPanelWidth,600);
	    nextTetPanel.repaint();
	    
	    game.setNextTetPanel(nextTetPanel);
	    
	    
	    boardPanel = new BoardPanel(game,this.gameState);
	    boardPanel.setGameStateListener(this);
	    add(boardPanel);
	    boardPanel.setBounds(0, 0, 600, 600);
	    boardPanel.repaint();
	   
	    
	    
	    // set resolution
	    setSize(612+nextTetPanelWidth,635);
		
	    
//	    add(gameOverLabel);
//	    add(pausedLabel);
	    
//	    update timer for tetromino to sink by gravity
	    
	    timer.addActionListener(e -> {
	    	game.updateTetromino();
	    	this.updateBoard();
	    	
	    	// generate stars
	    	if (Math.random()<0.5) {
	    		int size = (int)(Math.random()*5)+2;
	    		int hue  = (int)(Math.random()*40+20);
	    		int lum = (int)(Math.random()*50+40);
	    		Color starColor = toolbox.getColorFromHSL(hue,100,lum);
	    		hue = (int)(Math.random()*40+150);
	    		if (Math.random()<0.2) starColor = toolbox.getColorFromHSL(hue, size, lum);
	    		this.boardPanel.generateStar(size,starColor);
	    	}
	    	
	    	if (game.getGameOver()) this.toggleGameState("gameOver");
	    });
	    
	    starTimer.addActionListener(e -> {
	    	boardPanel.updateStars();
	    	boardPanel.repaint();
	    });
	}
	
	@Override
	public void onGameStateChange(String newState) {
		this.toggleGameState(newState);
	}
	
	public void toggleGameState(String gameState) {
		this.gameState = gameState;
		boardPanel.setGameState(this.gameState);
		if (gameState.equals("game")) {
//			pausedLabel.setText("Press 'P' or 'Esc' to pause!");
			timer.start();
			starTimer.start();
			
//			gameOverLabel.setVisible(false);
		} 
		else if (gameState.equals("home")) {
			timer.stop();
			starTimer.stop();
		}
		else if (gameState.equals("paused")) {
//			pausedLabel.setText("Press 'P' or 'Esc' to unpause!");
			timer.stop();
			starTimer.stop();
		}
		else if (gameState.equals("gameOver")) {
			timer.stop();
			starTimer.stop();
//			gameOverLabel.setVisible(true);
		}
		boardPanel.repaint();
	}
	
	public void updateStars() {
		boardPanel.updateStars();
    	boardPanel.repaint();
	}
	
	public void updateBoard() {
		game.updateBoard();
//    	scoreLabel.setText("Score: " + game.getScore());
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (this.gameState.equals("game") || this.gameState.equals("paused")) {
			switch (key) {
				case KeyEvent.VK_A:
					if (!gameState.equals("paused")) game.moveCurrTetromino(Direction.RIGHT);
					break;
				case KeyEvent.VK_LEFT:
					if (!gameState.equals("paused")) game.rotateTetromino(Direction.COUNTERCLOCKWISE);
					break;
				case KeyEvent.VK_D:
					if (!gameState.equals("paused")) game.moveCurrTetromino(Direction.LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					if (!gameState.equals("paused")) game.rotateTetromino(Direction.CLOCKWISE);
					break;
				case KeyEvent.VK_SPACE:
					if (!gameState.equals("paused")) game.updateTetromino();
					break;
				case KeyEvent.VK_P:
				case KeyEvent.VK_ESCAPE: {
					if (this.gameState.equals("paused")) this.toggleGameState("game");
					else this.toggleGameState("paused");
				}
			}
			
			this.updateBoard();
		} else if (this.gameState.equals("home")) {
			if (key==KeyEvent.VK_ENTER) {
				this.toggleGameState("game");
			}
		} else if (this.gameState.equals("gameOver")) {
			if (key==KeyEvent.VK_R) {
				game.restartGame();
				this.toggleGameState("game");
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		new Astertris();
	}
}
