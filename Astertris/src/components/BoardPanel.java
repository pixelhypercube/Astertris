package components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import decorations.Star;
import decorations.Asteroid;
import main.GameBoard;
import utils.BlockColor;
import utils.BlockState;
import utils.Toolbox;

import java.awt.BasicStroke;
import java.awt.Color;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1226815287986568967L;
	private GameBoard game;
	private int cellSize;
	
	private Toolbox toolbox;
	
	private String gameState;
	
//	HashMapping colors list
	private EnumMap<BlockColor,Color> colorsList;
	
	private ArrayList<Star> stars;
	private ArrayList<Asteroid> asteroids;
	
	private Graphics2D g2D;
	
//	components
	
	private HashMap<String,ArrayList<GameButton>> buttonsHashMap;
	
	private GameStateListener listener;
	
	public void setGameStateListener(GameStateListener listener) {
		this.listener = listener;
	}
	
	// SPECIAL BTNS
	private GameButton restartBtn_GameOver, restartBtn_Paused;
	
	// SPECIAL VARS
	private int screenCx, screenCy;
	
	public BoardPanel(GameBoard game, String gameState) {
		this.game = game;
		this.cellSize = game.getCellSize();
		this.gameState = gameState;
		this.stars = new ArrayList<Star>();
		this.asteroids = new ArrayList<Asteroid>();
		setPreferredSize(new Dimension(
			game.getWidth()*cellSize,
			game.getHeight()*cellSize
		));
		colorsList = new EnumMap<>(BlockColor.class);
        colorsList.put(BlockColor.BLACK, new Color(0, 0, 0));
        colorsList.put(BlockColor.RED, new Color(255, 0, 0));
        colorsList.put(BlockColor.ORANGE, new Color(255, 127, 0));
        colorsList.put(BlockColor.YELLOW, new Color(255, 255, 0));
        colorsList.put(BlockColor.GREEN, new Color(0, 255, 0));
        colorsList.put(BlockColor.AQUA, new Color(0, 255, 255));
        colorsList.put(BlockColor.BLUE, new Color(0, 0, 255));
        colorsList.put(BlockColor.PURPLE, new Color(127, 0, 255));
        colorsList.put(BlockColor.GREY, new Color(100, 100, 100));
        colorsList.put(BlockColor.PINK, new Color(255, 192, 203));
        colorsList.put(BlockColor.WHITE, new Color(255, 255, 255));
        this.toolbox = new Toolbox();
        
        // DETERMINE CENTER SCREEN
        this.screenCx = (game.getWidth()*cellSize)/2;
        this.screenCy = (game.getHeight()*cellSize)/2;
        
        this.buttonsHashMap = new HashMap<String, ArrayList<GameButton>>();
        
        this.buttonsHashMap.put("home", new ArrayList<>());
        GameButton startBtn = new GameButton(screenCx-170,370,340,40,true,20,"START",Color.WHITE,new Color(50,40,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("home").add(startBtn);
        GameButton helpBtn = new GameButton(screenCx-170,425,163,40,true,20,"HELP",Color.WHITE,new Color(50,40,30),Color.WHITE,null,"help_home");
        this.buttonsHashMap.get("home").add(helpBtn);
        GameButton creditsBtn = new GameButton(screenCx+5,425,163,40,true,20,"CREDITS",Color.WHITE,new Color(50,40,30),Color.WHITE,null,"credits");
        this.buttonsHashMap.get("home").add(creditsBtn);
        
        this.buttonsHashMap.put("paused", new ArrayList<>());
        GameButton resumeBtn = new GameButton(140,350,150,75,false,20,"RESUME",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("paused").add(resumeBtn);
        restartBtn_Paused = new GameButton(320,350,150,75,false,20,"RESTART",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("paused").add(restartBtn_Paused);
        
        this.buttonsHashMap.put("gameOver", new ArrayList<>());
        restartBtn_GameOver = new GameButton(230,350,150,75,false,20,"RESTART",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("gameOver").add(restartBtn_GameOver);
        
        this.buttonsHashMap.put("help_home", new ArrayList<>());
        GameButton backBtn = new GameButton(screenCx-75,610,150,40,false,16,"BACK",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"home");
        this.buttonsHashMap.get("help_home").add(backBtn);
        
        this.buttonsHashMap.put("help_game", new ArrayList<>());
        GameButton backGameBtn = new GameButton(screenCx-75,610,150,40,false,12,"BACK TO GAME",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("help_game").add(backGameBtn);
        
        this.buttonsHashMap.put("credits",new ArrayList<>());
        GameButton backBtnCredits = new GameButton(screenCx-75,525,150,35,false,16,"BACK",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"home");
        this.buttonsHashMap.get("credits").add(backBtnCredits);
        GameButton githubLink = new GameButton(screenCx-75,430,150,40,false,12,"View Project\non Github!",Color.WHITE,new Color(30,30,30),Color.WHITE,"https://github.com/pixelhypercube/Astertris",null);
        this.buttonsHashMap.get("credits").add(githubLink);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
	}
	
	// nearer stars appear larger and travel faster
	public void generateStar(int size, Color color) {
		int width = game.getWidth()*this.cellSize, height = game.getHeight()*this.cellSize;
		this.stars.add(new Star(width,(int)Math.round(Math.random()*height),size,size,-size*0.25,0,color));
	}
	
	// nearer asteroids appear larger and travel faster
	public void generateAsteroid(int size) {
		int width = game.getWidth()*this.cellSize, height = game.getHeight()*this.cellSize;
		this.asteroids.add(new Asteroid(width,(int)Math.round(Math.random()*height),size,size,-size*0.1,(double)(Math.random()*0.5-0.25),(int)Math.floor(Math.random()*10+1),Math.random()*0.1-0.05));
	}
	
	public void updateStars() {
		stars.forEach(Star::update);
	    stars.removeIf(star->star.getX()<=0);
	}
	
	public void updateAsteroids() {
		asteroids.forEach(Asteroid::update);
		
		final int screenWidth = game.getWidth()*this.cellSize;
		final int screenHeight = game.getHeight()*this.cellSize;
		
		asteroids.removeIf(asteroid -> 
			(asteroid.getX() + asteroid.getW() <= 0) ||
	        (asteroid.getX() >= screenWidth) ||
	        (asteroid.getY() + asteroid.getH() <= 0) ||
	        (asteroid.getY() >= screenHeight)
		);
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
		
		// change btn visibility based on HashMaps
		for (String keyString : this.buttonsHashMap.keySet()) {
			for (GameButton btn : this.buttonsHashMap.get(keyString)) {
				btn.setVisibility(keyString.equals(gameState));
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mouseDragged(e);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mouseMoved(e);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible() && button.getButtonBounds().contains(e.getPoint())) {
					button.mouseClicked(e);
					if (button.getNextGameState()!=null && listener != null) {
						listener.onGameStateChange(button.getNextGameState());
					}
					
					// SPECIAL BTNS ONLY
					if (button.equals(restartBtn_GameOver) || button.equals(restartBtn_Paused)) {
						game.restartGame();
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mousePressed(e);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mouseReleased(e);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mouseEntered(e);
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
		if (buttonList != null) {
			for (GameButton button : this.buttonsHashMap.get(this.gameState)) {
				if (button.isVisible()) {
					button.mouseExited(e);
				}
			}
		}
		repaint();
	}
	
	// special helper function for help dialog
	public void renderHelpDialog(Graphics g) {
	    g2D = (Graphics2D)g;
	    
	    final int DIALOG_WIDTH = 650;
	    final int DIALOG_HEIGHT = 600;
	    
	    // center the dialog
	    final int DIALOG_X = screenCx - DIALOG_WIDTH/2;
	    final int DIALOG_Y = screenCy - DIALOG_HEIGHT/2;
	    
	    // calculate translation offset
	    final int OFFSET_X = DIALOG_X - 200;
	    final int OFFSET_Y = DIALOG_Y - 100;
	    
	    final int SHIFT_DY = -70;
	    
	    g2D.setStroke(new BasicStroke(
	        2.0f,
	        BasicStroke.CAP_ROUND,
	        BasicStroke.JOIN_ROUND,
	        10.0f
	    ));
	    
	    g2D.setColor(Color.BLACK);
	    g2D.fillRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
	    g2D.setColor(Color.WHITE);
	    g2D.drawRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
	    
	 // TITLE
	    g2D.setColor(Color.WHITE);
	    g2D.setFont(toolbox.getFont(Font.BOLD,30));
	    g2D.drawString("HELP", DIALOG_WIDTH/2+5, 140 + OFFSET_Y);
	    
	    // --- How to play section ---
	    g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    g2D.drawString("How to play:", 275 + OFFSET_X, 180 + OFFSET_Y);
	    
	    ImageIcon gifIcon = toolbox.getGifImage("how_to_play.gif");
	    if (gifIcon != null) {
	    	final int ICON_X = DIALOG_X + 50;
	    	final int ICON_Y = DIALOG_Y + DIALOG_HEIGHT/2 - 150;
	    	gifIcon.paintIcon(this, g, ICON_X, ICON_Y);
	    	// draw border
	    	g2D.drawRect(ICON_X, ICON_Y, gifIcon.getIconWidth(), gifIcon.getIconHeight());
	    }
	    
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Like the original Tetris game, gain", 225 + OFFSET_X, 200 + OFFSET_Y);
	    g2D.drawString("points by clearing lines on one of", 225 + OFFSET_X, 215 + OFFSET_Y);
	    g2D.drawString("the 4 sides of the asteroid!", 225 + OFFSET_X, 230 + OFFSET_Y);
	    
	    g2D.drawString("Once the Tetronino lands outside", 225 + OFFSET_X, 475 + OFFSET_Y);
	    g2D.drawString("the screen, it's game over!", 225 + OFFSET_X, 490 + OFFSET_Y);
	    
	    g2D.drawString("Hint: If one side of the asteroid", 225 + OFFSET_X, 525 + OFFSET_Y);
	    g2D.drawString("is getting really pilled up, one", 225 + OFFSET_X, 540 + OFFSET_Y);
	    g2D.drawString("trick is to move to another side", 225 + OFFSET_X, 555 + OFFSET_Y);
	    g2D.drawString("of the asteroid!", 225 + OFFSET_X, 570 + OFFSET_Y);
	    
	    final int CONTROLS_OFFSET_X = 300;
	    
	 // ** CONTROLS SECTION - X POSITIONS ADJUSTED WITH CONTROLS_OFFSET_X **
	    g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    g2D.drawString("CONTROLS:", 310 + OFFSET_X + CONTROLS_OFFSET_X, 250 + OFFSET_Y + SHIFT_DY);
	    
	    g2D.setColor(Color.WHITE);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    
	    // W / Up Arrow - Rotation CCW
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Rot. CC", 347 + OFFSET_X + CONTROLS_OFFSET_X, 275 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("W", 355 + OFFSET_X + CONTROLS_OFFSET_X, 305 + OFFSET_Y + SHIFT_DY);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("↑", 374 + OFFSET_X + CONTROLS_OFFSET_X, 305 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(350 + OFFSET_X + CONTROLS_OFFSET_X, 280 + OFFSET_Y + SHIFT_DY, 40, 40);
	    
	    // S / Down Arrow - Rotation CW
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Rot. CCW", 345 + OFFSET_X + CONTROLS_OFFSET_X, 380 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("S", 355 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("↓", 374 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(350 + OFFSET_X + CONTROLS_OFFSET_X, 325 + OFFSET_Y + SHIFT_DY, 40, 40);
	    
	    // A / Left Arrow - Orbit Left
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Move Left", 235 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("A", 310 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("←", 324 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(305 + OFFSET_X + CONTROLS_OFFSET_X, 325 + OFFSET_Y + SHIFT_DY, 40, 40);
	    
	    // D / Right Arrow - Orbit Right
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Move Right", 440 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("D", 400 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("→", 414 + OFFSET_X + CONTROLS_OFFSET_X, 350 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(395 + OFFSET_X + CONTROLS_OFFSET_X, 325 + OFFSET_Y + SHIFT_DY, 40, 40);
	    
	    // SPACE - Speed Up
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("SPEED UP", 340 + OFFSET_X + CONTROLS_OFFSET_X, 450 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("SPACE", 340 + OFFSET_X + CONTROLS_OFFSET_X, 420 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(305 + OFFSET_X + CONTROLS_OFFSET_X, 395 + OFFSET_Y + SHIFT_DY, 130, 40);
	    
	    // H - Help Dialog
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Open Help Dialog", 325 + OFFSET_X + CONTROLS_OFFSET_X, 500 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("H", 280 + OFFSET_X + CONTROLS_OFFSET_X, 500 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(265 + OFFSET_X + CONTROLS_OFFSET_X, 475 + OFFSET_Y + SHIFT_DY, 40, 40);
	    
	    // ESC - Pause
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Pause Game", 325 + OFFSET_X + CONTROLS_OFFSET_X, 560 + OFFSET_Y + SHIFT_DY);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,13));
	    g2D.drawString("ESC", 272 + OFFSET_X + CONTROLS_OFFSET_X, 560 + OFFSET_Y + SHIFT_DY);
	    g2D.drawRect(265 + OFFSET_X + CONTROLS_OFFSET_X, 535 + OFFSET_Y + SHIFT_DY, 40, 40);
	}
	
	// special helper function to render credits
	public void renderCredits(Graphics g) {
		final int DIALOG_WIDTH = 300;
		final int DIALOG_HEIGHT = 400;
		
		final int DIALOG_X = screenCx - DIALOG_WIDTH/2;
		final int DIALOG_Y = screenCy - DIALOG_HEIGHT/2;
		
		g2D = (Graphics2D)g;
		
		g2D.setStroke(new BasicStroke(
		    2.0f,
		    BasicStroke.CAP_ROUND,
		    BasicStroke.JOIN_ROUND,
		    10.0f
		));
		
		g2D.setColor(Color.WHITE);
		g2D.drawRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
		g2D.setColor(Color.BLACK);
		g2D.fillRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,30));
		g2D.drawString("CREDITS", screenCx-75, 210);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,16));
		g2D.drawString("Game Programming:", screenCx-105, 250);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,12));
		g2D.drawString("@pixelhypercube", screenCx-70, 273);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,16));
		g2D.drawString("Art & UI Design:", screenCx-105, 300);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,12));
		g2D.drawString("@pixelhypercube", screenCx-70, 323);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,16));
		g2D.drawString("Tools Used:", screenCx-105, 350);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,12));
		g2D.drawString("Java 2D & Swing", screenCx-70, 373);
		
		g2D.setColor(Color.WHITE);
		g2D.setFont(toolbox.getFont(Font.PLAIN,16));
		g2D.drawImage(toolbox.getImage("gh_logo.png"), screenCx+40, 390,24,24,this);
		g2D.drawString("Github Repo", screenCx-105, 410);
		
		g2D.setFont(toolbox.getFont(Font.PLAIN,12));
		g2D.drawString("© 2025 @PIXELHYPERCUBE", screenCx-95, 510);
		
		
		g2D.setStroke(new BasicStroke(
		    1.0f,
		    BasicStroke.CAP_ROUND,
		    BasicStroke.JOIN_ROUND,
		    10.0f
		));
		
		g2D.setColor(Color.WHITE);
		g2D.drawLine(DIALOG_X, DIALOG_Y+DIALOG_HEIGHT-50, DIALOG_X+DIALOG_WIDTH, DIALOG_Y+DIALOG_HEIGHT-50);
	}

	
//	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		g2D = (Graphics2D)g;
		
		g2D.setColor(Color.BLACK);
//	    g2D.fillRect(0, 0, getWidth(), getHeight());
		g2D.fillRect(0, 0, game.getWidth()*this.cellSize, game.getHeight()*this.cellSize);
	    
	    // decorations
	    
	    // stars
	    for (Star star : stars) {
	    	star.render(g2D);
	    }
	    
	    // asteroids
	    for (Asteroid asteroid : asteroids) {
	    	asteroid.render(g2D);
	    }
	    
	    // lines for working tetris areas
	    
	    g2D.setColor(new Color(100,60,30));
	    
	    // vertical lines
	    g2D.fillRect(game.getCx()*cellSize-game.getPlanetSize()*cellSize, 0, 2, game.getHeight()*cellSize);
	    g2D.fillRect(game.getCx()*cellSize+game.getPlanetSize()*cellSize+cellSize-1, 0, 2, game.getHeight()*cellSize);
	    
	    // horizontal lines
	    g2D.fillRect(0, game.getCy()*cellSize-game.getPlanetSize()*cellSize, game.getWidth()*cellSize, 2);
	    g2D.fillRect(0, game.getCy()*cellSize+game.getPlanetSize()*cellSize+cellSize-1, game.getWidth()*cellSize, 2);
		
	    // blocks
		for (int i = 0;i<game.getHeight();i++) {
			for (int j = 0;j<game.getWidth();j++) {
				BlockState state = game.getCellBlockState(i, j);
	            Color color = null;
                
	            switch (state) {
	                case TETROMINO_PLACED:
	                case TETROMINO_HOVERING:
	                	if (this.gameState.equals("home") || this.gameState.equals("help_home") || this.gameState.equals("credits")) color = Color.BLACK;
	                	else color = colorsList.get(game.getCellColor(i, j));
	                    break;
	                case PLANET:
//	                    color = toolbox.getColorFromHSL(0, 0, (int)(Math.random()*3)+50);
	                	color = Color.gray;
	                    break;
	                default:
	                    continue;
	            }
	            if (color != null) {
	            	if (!(this.gameState.equals("home") || this.gameState.equals("help_home") || this.gameState.equals("credits"))) {
		                g2D.setColor(color);
		                g2D.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
		                
		                Color bevelTopColor = this.toolbox.blendColors(color, Color.WHITE, 0.35);
		                Color bevelBottomColor = this.toolbox.blendColors(color, Color.BLACK, 0.35);
		                
		                int bevelHeight = 3;
		                
		                // bevel

	                	// top 
	                    g2D.setColor(bevelTopColor);
	                    g2D.fillRect(j*cellSize,i*cellSize,cellSize,bevelHeight);

	                    // left
	                    g2D.fillRect(j*cellSize,i*cellSize,bevelHeight,cellSize);

	                    // bottom
	                    g2D.setColor(bevelBottomColor);
	                    g2D.fillRect(j*cellSize,(i+1)*cellSize-bevelHeight,cellSize,bevelHeight);

	                    // right
	                    g2D.fillRect((j+1)*cellSize-bevelHeight,i*cellSize,bevelHeight,cellSize);
	                }
                    
                    // draw border
                	g2D.setColor(Color.BLACK);
                	g2D.drawRect(j*cellSize,i*cellSize,cellSize,cellSize);
	            }
			}
		}
		
//		 linesss
//	    g2D.setColor(new Color(20,20,20));
//	    for (int i = 0; i <= game.getHeight(); i++) {
//	        g2D.drawLine(0, i * cellSize, game.getWidth() * cellSize, i * cellSize);
//	    }
//	    for (int j = 0; j <= game.getWidth(); j++) {
//	        g2D.drawLine(j * cellSize, 0, j * cellSize, game.getHeight() * cellSize);
//	    }
	    // LABELS
	    
//	    // score (PUT IN STATS PANEL)
//    	g2D.setColor(Color.white);
//    	g2D.setFont(toolbox.getFont(Font.PLAIN,20));
//    	g2D.drawString("Score: "+toolbox.renderInt(6,game.getScore()), 10, 30);
    	
    	// H for Help!
//    	g2D.setColor(Color.white);
//    	g2D.setFont(toolbox.getFont(Font.PLAIN,15));
//    	g2D.drawString("Press 'H' for help!", 10, 585);
	    
    	g2D.setStroke(new BasicStroke(
	    	2.0f,
	    	BasicStroke.CAP_ROUND,
	    	BasicStroke.JOIN_ROUND,
	    	10.0f
	    ));
    	
    	// HOME SCREEN
	    if (this.gameState.equals("home")) {
	    	
	    	
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(screenCx-212, screenCy-220, 425, 370);
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(screenCx-212, screenCy-220, 425, 370);
	    	
	    	
	    	g2D.drawImage(toolbox.getImage("logo_asteroid_color.png"), screenCx-192, screenCy-192, 384, 192, this);
	    	
	    // PAUSED SCREEN
	    } else if (this.gameState.equals("paused")) {
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(130, 170, 345, 160);
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(130, 170, 345, 160);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.BOLD,35));
	    	g2D.drawString("Game Paused!", 140, 240);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.PLAIN,15));
	    	g2D.drawString("Press 'P' or 'Esc' to unpause!", 155, 280);
	    // GAME OVER SCREEN
	    } else if (this.gameState.equals("gameOver")) {
//	    	gameover window
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(130, 170, 345, 160);
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(130, 170, 345, 160);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.BOLD,40));
	    	g2D.drawString("Game Over!", 150, 240);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    	g2D.drawString("Score: ", 200, 280);
	    	g2D.drawString(toolbox.renderInt(6,game.getScore()),300,280);
//	    	g2D.drawString("High Score: "+game.getScore(), 250, 280);
	    } else if (this.gameState.equals("help_home") || this.gameState.equals("help_game")) {
	    	this.renderHelpDialog(g);
	    } else if (this.gameState.equals("credits")) {
	    	this.renderCredits(g);
	    }
	    
	    // render btns
	    ArrayList<GameButton> buttonList = this.buttonsHashMap.get(this.gameState);
	    if (buttonList != null) {
	    	for (GameButton btn : this.buttonsHashMap.get(this.gameState)) {
		    	btn.paintComponent(g);
		    }
	    }
	}
}
