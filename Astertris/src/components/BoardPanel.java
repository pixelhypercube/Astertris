package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.EnumMap;

import javax.swing.JPanel;

import main.GameBoard;
import utils.BlockColor;
import utils.BlockState;
import utils.Toolbox;

import java.awt.Color;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1226815287986568967L;
	private GameBoard game;
	private final int cellSize = 10;
	
	private Toolbox toolbox;
	
//	mapping colors list
	private EnumMap<BlockColor,Color> colorsList;
	
	public BoardPanel(GameBoard game) {
		this.game = game;
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

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
	    g.fillRect(0, 0, getWidth(), getHeight());
		
		for (int i = 0;i<game.getHeight();i++) {
			for (int j = 0;j<game.getWidth();j++) {
				BlockState state = game.getCellBlockState(i, j);
	            Color color = null;
                
	            switch (state) {
	                case TETROMINO_PLACED:
	                case TETROMINO_HOVERING:
	                    color = colorsList.get(game.getCellColor(i, j));
	                    break;
	                case PLANET:
	                    color = toolbox.getColorFromHSL(0, 0, (int)(Math.random()*3)+50);
	                    break;
	                default:
	                    continue;
	            }
	            if (color != null) {
	                g.setColor(color);
	                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
	            }
			}
		}
		
		// linesss
	    g.setColor(Color.DARK_GRAY);
	    for (int i = 0; i <= game.getHeight(); i++) {
	        g.drawLine(0, i * cellSize, game.getWidth() * cellSize, i * cellSize);
	    }
	    for (int j = 0; j <= game.getWidth(); j++) {
	        g.drawLine(j * cellSize, 0, j * cellSize, game.getHeight() * cellSize);
	    }
	}
}
