package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
//import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;

import main.GameBoard;
import utils.Toolbox;

import utils.BlockColor;

public class NextTetPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -9197521560180289302L;
	
	private int width,height;
	private GameBoard game;
	private Toolbox toolbox = new Toolbox();
	private Queue<Integer> tetIdxQueue, colorIdxQueue;
	private int[][][] tetrominoShapes;
	private int cellSize = 12;
	private BlockColor[] tetColors;
	private EnumMap<BlockColor,Color> colorsList;
	private Graphics2D g2d;
	
	private ArrayList<GameButton> buttons;
	
	public NextTetPanel(GameBoard game, int width, int height) {
		this.setGame(game);
		this.width = width;
		this.height = height;
		
		this.tetColors = new BlockColor[]{BlockColor.RED,BlockColor.ORANGE,BlockColor.YELLOW,BlockColor.GREEN,BlockColor.AQUA,BlockColor.BLUE,BlockColor.PURPLE};
		
		this.tetIdxQueue = game.getTetIdxQueue();
		this.colorIdxQueue = game.getColorIdxQueue();
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
        
        GameButton button = new GameButton(10,500,80,80,true,16,"GITHUB\nLINK",Color.WHITE,new Color(30,30,30),Color.WHITE,"https://github.com/pixelhypercube/Astertris",null);
        this.buttons.add(button);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
	}
	
	public void renderNextBlocks(int x, int y, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// render ids from tetIdxQueue
		int offsetY = y;
		int padding = 40;
		
		// for color tetIdxQueue
		Iterator<Integer> colorIt = this.colorIdxQueue.iterator();
		
		int count = 0;
		for (int idx : this.tetIdxQueue) {
			int[][] tetromino = tetrominoShapes[idx];
			
			// align center
			int tetWidth = tetromino[0].length*cellSize;
			int offsetX = (this.width-tetWidth)/2;
			
			int colorIdx = colorIt.next();
			
			for (int i = 0;i<tetromino.length;i++) {
				for (int j = 0;j<tetromino[0].length;j++) {
					if (tetromino[i][j]==1) {
						BlockColor blockColor = this.tetColors[colorIdx];
						Color color = this.colorsList.get(blockColor);
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
			offsetY += tetromino.length * cellSize + padding + ((count++==0) ? 20 : 0);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D)g;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.YELLOW);
		g2d.fillRect(0,0,2,height);
		
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(
			3.0f,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			10.0f
		));
		int padding = 10;
		g2d.drawRect(padding,padding,this.width-padding*2,this.width-padding*2+10);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(toolbox.getFont(Font.PLAIN, 22));
		g2d.drawString("NEXT", 19, 35);
		
		this.renderNextBlocks(30,50,g2d);
		
		// render buttons
		for (GameButton button : this.buttons) {
			button.paintComponent(g);
		}
	}

	public GameBoard getGame() {
		return game;
	}

	public void setGame(GameBoard game) {
		this.game = game;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseDragged(e);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseMoved(e);
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseClicked(e);
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mousePressed(e);
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseReleased(e);
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseEntered(e);
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		for (GameButton button : this.buttons) {
			button.mouseExited(e);
		}
		repaint();
	}
}
