package components;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.GameBoard;
import objects.BlockColor;
import java.awt.Color;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1226815287986568967L;
	private GameBoard game;
	private final int cellSize = 10;
	
	public BoardPanel(GameBoard game) {
		this.game = game;
		setPreferredSize(new Dimension(
			game.getWidth()*cellSize,
			game.getHeight()*cellSize
		));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int i = 0;i<game.getHeight();i++) {
			for (int j = 0;j<game.getWidth();j++) {
				BlockColor cellBlockColor = game.getCellColor(i, j);
				switch (cellBlockColor) {
					case BlockColor.BLACK:
						g.setColor(new Color(0,0,0));
						break;
					case BlockColor.RED:
						g.setColor(new Color(255,0,0));
						break;
					case BlockColor.ORANGE:
						g.setColor(new Color(255,127,0));
						break;
					case BlockColor.YELLOW:
						g.setColor(new Color(255,255,0));
						break;
					case BlockColor.GREEN:
						g.setColor(new Color(0,255,0));
						break;
					case BlockColor.AQUA:
						g.setColor(new Color(0,255,255));
						break;
					case BlockColor.BLUE:
						g.setColor(new Color(0,0,255));
						break;
					case BlockColor.PURPLE:
						g.setColor(new Color(127,0,255));
						break;
					case BlockColor.GREY:
						g.setColor(new Color(100,100,100));
						break;
					case BlockColor.PINK:
						g.setColor(new Color(0,255,255));
						break;
					case BlockColor.WHITE:
						g.setColor(new Color(255,255,255));
						break;
				}
				g.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
			}
		}
	}
}
