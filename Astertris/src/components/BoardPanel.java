package components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.JPanel;

import main.GameBoard;
import utils.BlockColor;
import utils.BlockState;
import utils.Toolbox;

import objects.Star;

import java.awt.Color;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1226815287986568967L;
	private GameBoard game;
	private final int cellSize = 12;
	
	private Toolbox toolbox;
	
	private String gameState;
	
//	mapping colors list
	private EnumMap<BlockColor,Color> colorsList;
	
	private ArrayList<Star> stars;
	
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
	}
	
	// nearer stars are bigger and travel faster
	public void generateStar(int size, Color color) {
		int width = game.getWidth()*this.cellSize, height = game.getHeight()*this.cellSize;
		this.stars.add(new Star(width,(int)Math.round(Math.random()*height),size,size,-size*0.25,0,color));
	}
	
	public void updateStars() {
		stars.forEach(Star::update);
	    stars.removeIf(star->star.getX()<0);
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
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
	                g.setColor(color);
	                g.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
	                
	                Color bevelTopColor = this.toolbox.blendColors(color, Color.WHITE, 0.35);
	                Color bevelBottomColor = this.toolbox.blendColors(color, Color.BLACK, 0.35);
	                // bevel-like texture
	                if (!this.gameState.equals("home")) {
	                	// top 
	                    g.setColor(bevelTopColor);
	                    g.fillRect(j*cellSize,i*cellSize,cellSize,2);

	                    // left
	                    g.fillRect(j*cellSize,i*cellSize,2,cellSize);

	                    // bottom
	                    g.setColor(bevelBottomColor);
	                    g.fillRect(j*cellSize,(i+1)*cellSize-2,cellSize,2);

	                    // right
	                    g.fillRect((j+1)*cellSize-2,i*cellSize,2,cellSize);
	                }
	            }
			}
		}
		
		// linesss
	    g.setColor(new Color(20,20,20));
	    for (int i = 0; i <= game.getHeight(); i++) {
	        g.drawLine(0, i * cellSize, game.getWidth() * cellSize, i * cellSize);
	    }
	    for (int j = 0; j <= game.getWidth(); j++) {
	        g.drawLine(j * cellSize, 0, j * cellSize, game.getHeight() * cellSize);
	    }
	    // LABELS
	    
	    // score
//	    g.setColor(Color.WHITE);
//    	g.drawRect(20, 20, 240, 40);
//    	g.setColor(Color.BLACK);
//    	g.fillRect(20, 20, 240, 40);
    	g.setColor(Color.white);
    	g.setFont(new Font("Arial",Font.PLAIN, 20));
    	g.drawString("Score: "+game.getScore(), 10, 30);
	    
	    if (this.gameState.equals("home")) {
	    	g.setColor(Color.WHITE);
	    	g.drawRect(130, 170, 345, 160);
	    	g.setColor(Color.BLACK);
	    	g.fillRect(130, 170, 345, 160);
	    	
	    	g.setColor(Color.white);
	    	g.setFont(new Font("Arial",Font.BOLD, 70));
	    	g.drawString("Astertris", 160, 240);
	    	
	    	g.setColor(Color.white);
	    	g.setFont(new Font("Arial",Font.PLAIN,30));
	    	g.drawString("Press 'Enter' to start!", 165, 280);
	    	
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("Arial",Font.ITALIC,16));
	    	g.drawString("Made by @pixelhypercube", 215, 310);
	    } else if (this.gameState.equals("paused")) {
	    	g.setColor(Color.WHITE);
	    	g.drawRect(130, 170, 345, 160);
	    	g.setColor(Color.BLACK);
	    	g.fillRect(130, 170, 345, 160);
	    	
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("Arial",Font.BOLD, 40));
	    	g.drawString("Game Paused!", 165, 240);
	    	
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("Arial",Font.PLAIN,20));
	    	g.drawString("Press 'P' or 'Esc' to unpause!", 180, 280);
	    } else if (this.gameState.equals("gameOver")) {
//	    	gameover window
	    	g.setColor(Color.WHITE);
	    	g.drawRect(100, 200, 400, 225);
	    	g.setColor(Color.BLACK);
	    	g.fillRect(100, 200, 400, 225);
	    	
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("Arial",Font.BOLD,60));
	    	g.drawString("Game Over!", 130, 300);
	    	
	    	g.setColor(Color.WHITE);
	    	g.setFont(new Font("Arial",Font.BOLD,30));
	    	g.drawString("Press 'R' to restart!", 165, 350);
	    }
	}
}
