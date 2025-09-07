package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.JPanel;

import main.Astertris;
import main.GameBoard;
import utils.BlockColor;
import utils.Toolbox;

public class StatPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -2619725640328042122L;
	
	private int width,height;
	private GameBoard game;
	private Toolbox toolbox = new Toolbox();
	
	private int[][][] tetrominoShapes;
	private int cellSize = 9;
//	private BlockColor[] tetColors;
	private EnumMap<BlockColor,Color> colorsList;
	
//	private Graphics2D g2d;
	
	private ArrayList<GameButton> buttons;
	
	private GameStateListener listener;
	private int[][] stats;
	
	public void setGameStateListener(GameStateListener listener) {
		this.listener = listener;
	}
	
	public StatPanel(GameBoard game, int width, int height, Astertris mainProg) {
		this.setGame(game);
		
		this.game = game;
		this.width = width;
		this.height = height;
		
		this.stats = game.getStats();
		
		
		// TET COLORS
		
//		this.tetColors = new BlockColor[]{BlockColor.RED,BlockColor.ORANGE,BlockColor.YELLOW,BlockColor.GREEN,BlockColor.AQUA,BlockColor.BLUE,BlockColor.PURPLE};
		
		this.tetrominoShapes = toolbox.getTetrominoShapes();
		
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
        
        this.buttons = new ArrayList<GameButton>();
        
//        GameButton helpButton = new GameButton(10,540,100,40,true,16,"Help",Color.WHITE,new Color(30,30,30),Color.WHITE,null,"help_game");
//        this.buttons.add(helpButton);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setColor(Color.YELLOW);
		g2d.fillRect(width-2,0,2,height);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(toolbox.getFont(Font.PLAIN, 20));
		g2d.drawString("STATS",20,30);
		
		int offsetX = 10;
		int offsetY = 50;
		int padding = 30;
		
		int index = 0;
		for (int[][] tetShape : this.tetrominoShapes) {
			int n = tetShape.length, m = tetShape[0].length;
			for (int i = 0;i<n;i++) {
				for (int j = 0;j<m;j++) {
					if (tetShape[i][j]==1) {
//						BlockColor blockColor = BlockColor.ORANGE;
//						Color color = this.colorsList.get(blockColor);
						Color color = new Color(200,150,0);
						Color bevelTopColor = this.toolbox.blendColors(color, Color.WHITE, 0.35);
				        Color bevelBottomColor = this.toolbox.blendColors(color, Color.BLACK, 0.35);
						
						int drawX = offsetX+j*cellSize;
	                    int drawY = offsetY+i*cellSize;
	                    
	                    g2d.setColor(color);
	                    g2d.fillRect(drawX,drawY,cellSize,cellSize);
	                    
						// top 
			            g2d.setColor(bevelTopColor);
			            g2d.fillRect(drawX,drawY,cellSize,2);

			            // left
			            g2d.fillRect(drawX,drawY,2,cellSize);

			            // bottom
			            g2d.setColor(bevelBottomColor);
			            g2d.fillRect(drawX,drawY+cellSize-2,cellSize,2);

			            // right
			            g2d.fillRect(drawX+cellSize-2,drawY,2,cellSize);
					}
				}
			}
			
			g2d.setColor(Color.WHITE);
			g2d.setFont(toolbox.getFont(Font.PLAIN, 12));
			g2d.drawString("x "+stats[index++][1], offsetX+50, offsetY+5+cellSize*tetShape.length/2);
			
			offsetY += tetShape.length * cellSize + padding;
		}
		
		// render buttons
		for (GameButton button : this.buttons) {
			button.paintComponent(g);
		}
	}
	
	public void setGame(GameBoard game) {
		this.game = game;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.getVisibility()) {
				button.mouseDragged(e);
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible()) {
				button.mouseMoved(e);
			}
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible() && button.getButtonBounds().contains(e.getPoint())) {
				button.mouseClicked(e);
				if (button.getNextGameState()!=null && listener != null) {
					listener.onGameStateChange(button.getNextGameState());
				}
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible()) {
				button.mousePressed(e);
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible()) {
				button.mouseReleased(e);
			}
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible()) {
				button.mouseEntered(e);
			}
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			if (button.isVisible()) {
				button.mouseExited(e);
			}
		}
		repaint();
	}
}
