package main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;

import components.BoardPanel;
import components.GameStateListener;
import components.NextTetPanel;
import components.StatPanel;
import components.TopPanel;
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
	private StatPanel statPanel;
	
	private TopPanel topPanel;
	
	private Toolbox toolbox;
	
	private String gameState;
	
	private final int nextTetPanelWidth = 150, statPanelWidth = 165, gameWidth = 600;
	private final int topPanelHeight = 40;
	
	Timer timer = new Timer(500,null);
	Timer starTimer = new Timer(20,null);
	Timer asteroidTimer = new Timer(30,null);
	
	// list of active keys
	private Set<Integer> activeKeys = new HashSet<>();
	
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
	    
	    // top panel
	    topPanel = new TopPanel(game,gameWidth,topPanelHeight);
	    add(topPanel);
	    topPanel.setBounds(statPanelWidth,0,gameWidth,50);
	    topPanel.repaint();
	    
	    
	    nextTetPanel = new NextTetPanel(game,nextTetPanelWidth,600+topPanelHeight);
	    add(nextTetPanel);
	    nextTetPanel.setBounds(statPanelWidth+gameWidth,0,nextTetPanelWidth,600+topPanelHeight);
	    nextTetPanel.repaint();
	    
	    statPanel = new StatPanel(game,statPanelWidth,600+topPanelHeight,this);
	    statPanel.setGameStateListener(this);
	    add(statPanel);
	    statPanel.setBounds(0,0,statPanelWidth,600+topPanelHeight);
	    statPanel.repaint();
	    
	    game.setStatPanel(statPanel);
	    game.setNextTetPanel(nextTetPanel);
	    
	    
	    boardPanel = new BoardPanel(game,this.gameState);
	    boardPanel.setGameStateListener(this);
	    add(boardPanel);
	    boardPanel.setBounds(statPanelWidth, topPanelHeight, 600, 600);
	    boardPanel.repaint();
	   
	    
	    
	    // set resolution
	    setSize(statPanelWidth+gameWidth+12+nextTetPanelWidth,635+topPanelHeight);
		
	    
//	    add(gameOverLabel);
//	    add(pausedLabel);
	    
//	    update timer for tetromino to sink by gravity
	    
	    timer.addActionListener(e -> {
	    	game.updateTetromino();
	    	this.updateBoard();
	    	this.updateTimerDelay();
	    	
	    	// generate stars / asteroids
	    	if (Math.random()<0.5) {
    			int size = (int)(Math.random()*5)+2;
	    		int hue  = (int)(Math.random()*40+20);
	    		int lum = (int)(Math.random()*50+40);
	    		Color starColor = toolbox.getColorFromHSL(hue,100,lum);
	    		hue = (int)(Math.random()*40+150);
	    		if (Math.random()<0.2) starColor = toolbox.getColorFromHSL(hue, size, lum);
	    		this.boardPanel.generateStar(size,starColor);
    		}
	    	if (Math.random()<0.01) {
    			this.boardPanel.generateAsteroid(12);
    		}
	    	
	    	if (game.getGameOver()) this.toggleGameState("gameOver");
	    });
	    
	    starTimer.addActionListener(e -> {
	    	boardPanel.updateStars();
	    	boardPanel.repaint();
	    });
	    
	    asteroidTimer.addActionListener(e -> {
	    	boardPanel.updateAsteroids();
	    	boardPanel.repaint();
	    });
	}
	
	// helper function to set the timer delay
	public void updateTimerDelay() {
		int linesCleared = game.getLinesCleared();
		
		int baseDelay = 500;
		int minimumDelay = 100;
		int newDelay = Math.max(minimumDelay,baseDelay - ((int)linesCleared/10 * 50));
		
		if (timer.getDelay() != newDelay) {
			timer.setDelay(newDelay);
		}
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
			asteroidTimer.start();
			
//			gameOverLabel.setVisible(false);
		} 
		else if (gameState.equals("home")) {
			timer.stop();
			starTimer.stop();
			asteroidTimer.stop();
		}
		else if (gameState.equals("paused")) {
//			pausedLabel.setText("Press 'P' or 'Esc' to unpause!");
			timer.stop();
			starTimer.stop();
			asteroidTimer.stop();
		}
		else if (gameState.equals("gameOver")) {
			timer.stop();
			starTimer.stop();
			asteroidTimer.stop();
//			gameOverLabel.setVisible(true);
		}
		else if (gameState.equals("help_home")) {
			timer.stop();
			starTimer.stop();
			asteroidTimer.stop();
//			gameOverLabel.setVisible(true);
		}
		else if (gameState.equals("help_game")) {
			timer.stop();
			starTimer.stop();
			asteroidTimer.stop();
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
		if (topPanel!=null) this.topPanel.repaint();
	}
	
	public void handleMultipleInputs() {
		if (!this.gameState.equals("game")) return;
		
		// Rotation (W/UP, S/DOWN)
		if (activeKeys.contains(KeyEvent.VK_W) || activeKeys.contains(KeyEvent.VK_UP)) {
			game.rotateTetromino(Direction.COUNTERCLOCKWISE);
		}
		
		if (activeKeys.contains(KeyEvent.VK_S) || activeKeys.contains(KeyEvent.VK_DOWN)) {
	        game.rotateTetromino(Direction.CLOCKWISE);
	    }
		
		// Movement (A/LEFT, D/RIGHT)
		
		if (activeKeys.contains(KeyEvent.VK_A) || activeKeys.contains(KeyEvent.VK_LEFT)) {
	        game.moveCurrTetromino(Direction.RIGHT);
	    }
	    
	    if (activeKeys.contains(KeyEvent.VK_D) || activeKeys.contains(KeyEvent.VK_RIGHT)) {
	        game.moveCurrTetromino(Direction.LEFT);
	    }
	    
	    // Fast drop
	    if (activeKeys.contains(KeyEvent.VK_SPACE)) {
	        game.updateTetromino(); // Trigger a quick drop
	    }
	    
	    this.updateBoard(); 
	}
	
	public void keyPressed(KeyEvent e) {
		activeKeys.add(e.getKeyCode());
		
		int key = e.getKeyCode();
		
		// handle state transitions
		if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
	        if (this.gameState.equals("paused")) this.toggleGameState("game");
	        else if (this.gameState.equals("game")) this.toggleGameState("paused");
	    } 
		
		this.handleMultipleInputs();
	}
	
	public void keyReleased(KeyEvent e) {
		activeKeys.remove(e.getKeyCode());
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		new Astertris();
	}
}
