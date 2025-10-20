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
	final static int WIDTH = 46, HEIGHT = 46, PLANET_SIZE = 5; 
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
	
	private final int cellSize = 16;
	
	private final int nextTetPanelWidth = 150, statPanelWidth = 170, gameWidth = WIDTH*cellSize;
	private final int topPanelHeight = 40;
	
	Timer tetrisFallTimer = new Timer(800,null);
	Timer starRenderTimer = new Timer(20,null);
	Timer starGenTimer = new Timer(500,null);
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
		setIconImage(toolbox.getImage("icon.png"));
		
		
		
//		components
		
		// init game
	    game = new GameBoard(WIDTH,HEIGHT,PLANET_SIZE,0,cellSize);
	    
	    // top panel
	    topPanel = new TopPanel(game,gameWidth,topPanelHeight);
	    add(topPanel);
	    topPanel.setBounds(statPanelWidth,0,gameWidth,50);
	    topPanel.repaint();
	    
	    
	    nextTetPanel = new NextTetPanel(game,nextTetPanelWidth,gameWidth+topPanelHeight);
	    add(nextTetPanel);
	    nextTetPanel.setBounds(statPanelWidth+gameWidth,0,nextTetPanelWidth,gameWidth+topPanelHeight);
	    nextTetPanel.repaint();
	    
	    statPanel = new StatPanel(game,statPanelWidth,gameWidth+topPanelHeight,this);
	    statPanel.setGameStateListener(this);
	    add(statPanel);
	    statPanel.setBounds(0,0,statPanelWidth,gameWidth+topPanelHeight);
	    statPanel.repaint();
	    
	    game.setStatPanel(statPanel);
	    game.setNextTetPanel(nextTetPanel);
	    
	    
	    boardPanel = new BoardPanel(game,this.gameState);
	    boardPanel.setGameStateListener(this);
	    add(boardPanel);
	    boardPanel.setBounds(statPanelWidth, topPanelHeight, gameWidth, gameWidth);
	    boardPanel.repaint();
	   
	    
	    
	    // set resolution
	    setSize(statPanelWidth+gameWidth+12+nextTetPanelWidth,gameWidth+35+topPanelHeight);
		
	    
//	    add(gameOverLabel);
//	    add(pausedLabel);
	    
//	    update tetrisFallTimer for tetromino to sink by gravity
	    
	    tetrisFallTimer.addActionListener(e -> {
	    	game.updateTetromino();
	    	this.updateBoard();
	    	this.updateTimerDelay();
	    	
	    	if (game.getGameOver()) this.toggleGameState("gameOver");
	    });
	    
	    starGenTimer.addActionListener(e -> {
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
	    });
	    
	    starRenderTimer.addActionListener(e -> {
	    	boardPanel.updateStars();
	    	boardPanel.repaint();
	    });
	    
	    asteroidTimer.addActionListener(e -> {
	    	boardPanel.updateAsteroids();
	    	boardPanel.repaint();
	    });
	    
//	    DEBUG ONLY!!!
//	    this.toggleGameState("paused");
	}
	
	// helper function to set the tetrisFallTimer delay
	public void updateTimerDelay() {
		int linesCleared = game.getLinesCleared();
		int level = linesCleared/10;
		/*
		
		Level	Frames per Gridcell
		00	48
		01	43
		02	38
		03	33
		04	28
		05	23
		06	18
		07	13
		08	8
		09	6
		10–12	5
		13–15	4
		16–18	3
		19–28	2
		29+	1 
		*/
		
		int framesPerGridCell = 48;
		if (level>=0 && level<=8) framesPerGridCell -= level*5;
		else if (level==9) framesPerGridCell = 6;
		else if (level>=10 && level<=12) framesPerGridCell = 5;
		else if (level>=13 && level<=15) framesPerGridCell = 4;
		else if (level>=16 && level<=18) framesPerGridCell = 3;
		else if (level>=19 && level<=28) framesPerGridCell = 2;
		else if (level>=29) framesPerGridCell = 1;
		
		final int MS_PER_FRAME = 17; // 16.67
		int newDelay = framesPerGridCell * MS_PER_FRAME;
		if (tetrisFallTimer.getDelay() != newDelay) {
			tetrisFallTimer.setDelay(newDelay);
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
			tetrisFallTimer.start();
			starRenderTimer.start();
			starGenTimer.start();
			asteroidTimer.start();
			
//			gameOverLabel.setVisible(false);
		} 
		else if (gameState.equals("home")) {
			tetrisFallTimer.stop();
			starRenderTimer.stop();
			starGenTimer.stop();
			asteroidTimer.stop();
		}
		else if (gameState.equals("paused")) {
//			pausedLabel.setText("Press 'P' or 'Esc' to unpause!");
			tetrisFallTimer.stop();
			starRenderTimer.stop();
			starGenTimer.stop();
			asteroidTimer.stop();
		}
		else if (gameState.equals("gameOver")) {
			tetrisFallTimer.stop();
			starRenderTimer.stop();
			starGenTimer.stop();
			asteroidTimer.stop();
//			gameOverLabel.setVisible(true);
		}
		else if (gameState.equals("help_home")) {
			tetrisFallTimer.stop();
			starRenderTimer.stop();
			starGenTimer.stop();
			asteroidTimer.stop();
//			gameOverLabel.setVisible(true);
		}
		else if (gameState.equals("help_game")) {
			tetrisFallTimer.stop();
			starRenderTimer.stop();
			starGenTimer.stop();
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
	    } else if (key == KeyEvent.VK_H && this.gameState.equals("game")) {
	    	this.toggleGameState("help_game");
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
