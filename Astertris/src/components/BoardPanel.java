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
        
        this.buttonsHashMap = new HashMap<String, ArrayList<GameButton>>();
        
        this.buttonsHashMap.put("home", new ArrayList<>());
        GameButton startBtn = new GameButton(130,370,150,75,true,24,"START",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("home").add(startBtn);
        GameButton helpBtn = new GameButton(325,370,150,75,true,24,"HELP",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"help_home");
        this.buttonsHashMap.get("home").add(helpBtn);
        
        this.buttonsHashMap.put("paused", new ArrayList<>());
        GameButton resumeBtn = new GameButton(140,350,150,75,false,24,"RESUME",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("paused").add(resumeBtn);
        restartBtn_Paused = new GameButton(320,350,150,75,false,24,"RESTART",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("paused").add(restartBtn_Paused);
        
        this.buttonsHashMap.put("gameOver", new ArrayList<>());
        restartBtn_GameOver = new GameButton(230,350,150,75,false,24,"RESTART",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("gameOver").add(restartBtn_GameOver);
        
        this.buttonsHashMap.put("help_home", new ArrayList<>());
        GameButton backBtn = new GameButton(225,470,150,40,false,16,"BACK",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"home");
        this.buttonsHashMap.get("help_home").add(backBtn);
        
        this.buttonsHashMap.put("help_game", new ArrayList<>());
        GameButton backGameBtn = new GameButton(215,470,170,40,false,16,"BACK TO GAME",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("help_game").add(backGameBtn);
        
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
		this.asteroids.add(new Asteroid(width,(int)Math.round(Math.random()*height),size,size,-size*0.1,(double)(Math.random()*0.5-0.25),(int)Math.floor(Math.random()*4+1),Math.random()*0.1-0.05));
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
	    g2D.setStroke(new BasicStroke(
	        2.0f,
	        BasicStroke.CAP_ROUND,
	        BasicStroke.JOIN_ROUND,
	        10.0f
	    ));
	    
	    g2D.setColor(Color.BLACK);
	    g2D.fillRect(130, 100, 345, 420);
	    g2D.setColor(Color.WHITE);
	    g2D.drawRect(130, 100, 345, 420);
	    
	    g2D.setColor(Color.WHITE);
	    g2D.setFont(toolbox.getFont(Font.BOLD,30));
	    g2D.drawString("HELP", 260, 140);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    g2D.drawString("CONTROLS:", 240, 180);
	    
	    g2D.setColor(Color.WHITE);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Rot. CC", 277, 205);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("W", 285, 235);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("↑", 304, 235);
	    g2D.drawRect(280, 210, 40, 40);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Rot. CCW", 275, 310);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("S", 285, 280);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("↓", 304, 280);
	    g2D.drawRect(280, 255, 40, 40);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Orbit Left", 155, 280);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("A", 240, 280);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("←", 254, 280);
	    g2D.drawRect(235, 255, 40, 40);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Orbit Right", 370, 280);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("D", 330, 280);
	    g.setFont(new Font("Arial",Font.BOLD,18));
	    g2D.drawString("→", 344, 280);
	    g2D.drawRect(325, 255, 40, 40);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("SPEED UP", 270, 380);
	    g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    g2D.drawString("SPACE", 270, 350);
	    g2D.drawRect(235, 325, 130, 40);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    g2D.drawString("How to play:", 220, 415);
	    
	    g2D.setFont(toolbox.getFont(Font.PLAIN,10));
	    g2D.drawString("Just like Tetris, gain points by forming", 155, 435);
	    g2D.drawString("lines on all 4 sides of the asteroid!", 155, 450);
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
	    
	    g2D.setColor(new Color(30,20,15));
	    
	    // vertical lines
	    g2D.fillRect(game.getCx()*cellSize-game.getPlanetSize()*cellSize, 0, 1, game.getHeight()*cellSize);
	    g2D.fillRect(game.getCx()*cellSize+game.getPlanetSize()*cellSize+cellSize, 0, 1, game.getHeight()*cellSize);
	    
	    // horizontal lines
	    g2D.fillRect(0, game.getCy()*cellSize-game.getPlanetSize()*cellSize, game.getWidth()*cellSize, 1);
	    g2D.fillRect(0, game.getCy()*cellSize+game.getPlanetSize()*cellSize+cellSize, game.getWidth()*cellSize, 1);
		
	    // blocks
		for (int i = 0;i<game.getHeight();i++) {
			for (int j = 0;j<game.getWidth();j++) {
				BlockState state = game.getCellBlockState(i, j);
	            Color color = null;
                
	            switch (state) {
	                case TETROMINO_PLACED:
	                case TETROMINO_HOVERING:
	                	if (this.gameState.equals("home") || this.gameState.equals("help_home")) color = Color.BLACK;
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
	                g2D.setColor(color);
	                g2D.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
	                
	                Color bevelTopColor = this.toolbox.blendColors(color, Color.WHITE, 0.35);
	                Color bevelBottomColor = this.toolbox.blendColors(color, Color.BLACK, 0.35);
	                // bevel
	                
	                if (!(this.gameState.equals("home") || this.gameState.equals("help_home"))) {
	                	// top 
	                    g2D.setColor(bevelTopColor);
	                    g2D.fillRect(j*cellSize,i*cellSize,cellSize,2);

	                    // left
	                    g2D.fillRect(j*cellSize,i*cellSize,2,cellSize);

	                    // bottom
	                    g2D.setColor(bevelBottomColor);
	                    g2D.fillRect(j*cellSize,(i+1)*cellSize-2,cellSize,2);

	                    // right
	                    g2D.fillRect((j+1)*cellSize-2,i*cellSize,2,cellSize);
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
	    if (this.gameState.equals("home")) {
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(90, 130, 425, 220);
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(90, 130, 425, 220);
	    	
	    	
//	    	g2D.setColor(Color.white);
//	    	g2D.setFont(toolbox.getFont(Font.BOLD,45));
//	    	g2D.drawString("Astertris", 150, 240);
//	    	
//	    	g2D.setColor(Color.white);
//	    	g2D.setFont(toolbox.getFont(Font.PLAIN,16));
//	    	g2D.drawString("Tetris, but on an asteroid!", 145, 270);
	    	
	    	g2D.drawImage(toolbox.getImage("logo_asteroid_color.png"), 110, 150, 384, 192, this);
	    	
//	    	g2D.setColor(Color.WHITE);
//	    	g2D.setFont(toolbox.getFont(Font.ITALIC,14));
//	    	g2D.drawString("Made by @pixelhypercube", 185, 310);
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
