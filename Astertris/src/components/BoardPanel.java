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
import main.GameBoard;
import utils.BlockColor;
import utils.BlockState;
import utils.Toolbox;

import java.awt.Color;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1226815287986568967L;
	private GameBoard game;
	private final int cellSize = 12;
	
	private Toolbox toolbox;
	
	private String gameState;
	
//	HashMapping colors list
	private EnumMap<BlockColor,Color> colorsList;
	
	private ArrayList<Star> stars;
	
	private Graphics2D g2D;
	
//	components
	
	private HashMap<String,ArrayList<GameButton>> buttonsHashMap;
	
	private GameStateListener listener;
	
	public void setGameStateListener(GameStateListener listener) {
		this.listener = listener;
	}
	
	public BoardPanel(GameBoard game, String gameState) {
		this.game = game;
		this.gameState = gameState;
		this.stars = new ArrayList<Star>();
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
        GameButton startBtn = new GameButton(230,350,150,75,true,24,"START",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("home").add(startBtn);
        
        this.buttonsHashMap.put("paused", new ArrayList<>());
        GameButton resumeBtn = new GameButton(230,350,150,75,false,24,"RESUME",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("paused").add(resumeBtn);
        
        this.buttonsHashMap.put("gameOver", new ArrayList<>());
        GameButton restartBtn = new GameButton(230,350,150,75,false,24,"RESTART",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"game");
        this.buttonsHashMap.get("gameOver").add(restartBtn);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
	}
	
	// nearer stars are bigger and travel faster
	public void generateStar(int size, Color color) {
		int width = game.getWidth()*this.cellSize, height = game.getHeight()*this.cellSize;
		this.stars.add(new Star(width,(int)Math.round(Math.random()*height),size,size,-size*0.25,0,color));
	}
	
	public void updateStars() {
		stars.forEach(Star::update);
	    stars.removeIf(star->star.getX()<=0);
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
		
		// change btn visibility based on HashMaps
		for (String keyString : this.buttonsHashMap.keySet()) {
			for (GameButton btn : this.buttonsHashMap.get(keyString)) {
				btn.setVisible(keyString.equals(gameState));
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
	
//	@Override
	public void paint(Graphics g) {
//		super.paintComponent(g);
		g2D = (Graphics2D)g;
		
		g2D.setColor(Color.BLACK);
//	    g2D.fillRect(0, 0, getWidth(), getHeight());
		g2D.fillRect(0, 0, game.getWidth()*this.cellSize, game.getHeight()*this.cellSize);
	    
	    // decorations
	    
	    // stars
	    for (Star star : stars) {
	    	star.render(g);
	    }
		
	    // blocks
		for (int i = 0;i<game.getHeight();i++) {
			for (int j = 0;j<game.getWidth();j++) {
				BlockState state = game.getCellBlockState(i, j);
	            Color color = null;
                
	            switch (state) {
	                case TETROMINO_PLACED:
	                case TETROMINO_HOVERING:
	                	if (this.gameState.equals("home")) color = Color.BLACK;
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
	                
	                if (!this.gameState.equals("home")) {
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
	    
	    // score
    	g2D.setColor(Color.white);
    	g2D.setFont(toolbox.getFont(Font.PLAIN,20));
    	g2D.drawString("Score: "+toolbox.renderInt(6,game.getScore()), 10, 30);
	    
	    if (this.gameState.equals("home")) {
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(130, 170, 345, 160);
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(130, 170, 345, 160);
	    	
	    	g2D.setColor(Color.white);
	    	g2D.setFont(toolbox.getFont(Font.BOLD,45));
	    	g2D.drawString("Astertris", 150, 240);
	    	
	    	g2D.setColor(Color.white);
	    	g2D.setFont(toolbox.getFont(Font.PLAIN,16));
	    	g2D.drawString("Tetris, but on an asteroid!", 145, 270);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.ITALIC,14));
	    	g2D.drawString("Made by @pixelhypercube", 185, 310);
	    } else if (this.gameState.equals("paused")) {
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(130, 170, 345, 160);
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(130, 170, 345, 160);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.BOLD,35));
	    	g2D.drawString("Game Paused!", 140, 240);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.PLAIN,15));
	    	g2D.drawString("Press 'P' or 'Esc' to unpause!", 147, 280);
	    } else if (this.gameState.equals("gameOver")) {
//	    	gameover window
	    	g2D.setColor(Color.WHITE);
	    	g2D.drawRect(130, 170, 345, 160);
	    	g2D.setColor(Color.BLACK);
	    	g2D.fillRect(130, 170, 345, 160);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.BOLD,40));
	    	g2D.drawString("Game Over!", 150, 240);
	    	
	    	g2D.setColor(Color.WHITE);
	    	g2D.setFont(toolbox.getFont(Font.PLAIN,20));
	    	g2D.drawString("Score: ", 200, 280);
	    	g2D.drawString(toolbox.renderInt(6,game.getScore()),300,280);
//	    	g2D.drawString("High Score: "+game.getScore(), 250, 280);
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
