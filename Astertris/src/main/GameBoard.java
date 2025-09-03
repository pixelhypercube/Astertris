package main;

import java.util.*;

import objects.*;
import utils.BlockColor;
import utils.BlockState;
import utils.Direction;
import utils.Position;

import java.util.Queue;

import components.NextTetPanel;

import java.util.LinkedList;

public class GameBoard {
	private int cx,cy;
	private int width, height, planetSize;
	private int borderWidth;
	
	private ArrayList<ArrayList<Block>> board;
	
	private Tetromino currTetromino;
	
	private BlockColor[][] placedBlocks;
	
	private int score;
	
	private Random random = new Random();
	
	private boolean gameOver;
	
	private BlockColor[] tetColors;
	private BlockColor currTetColor;
	private Queue<Integer> tetIdxQueue;
	private Queue<Integer> colorIdxQueue;
	
	private NextTetPanel nextTetPanel;
	
	public GameBoard(int width, int height, int planetSize, int score) {
		this.width = width;
		this.height = height;
		this.planetSize = planetSize;
		this.cx = width/2;
		this.cy = height/2;
		
		this.tetColors = new BlockColor[]{BlockColor.RED,BlockColor.ORANGE,BlockColor.YELLOW,BlockColor.GREEN,BlockColor.AQUA,BlockColor.BLUE,BlockColor.PURPLE};
		this.setCurrTetColor(tetColors[random.nextInt(tetColors.length)]);
		
		
		this.score = score;
		
		this.borderWidth = 1;
		
		this.gameOver = false;
		
		this.board = new ArrayList<ArrayList<Block>>();
		
		// set placedBlocks dims
		this.placedBlocks = new BlockColor[height][width];
		
		// INIT TETROMINO
		
		this.tetIdxQueue = new LinkedList<Integer>();
		
		for (int i = 0;i<5;i++) {
			this.tetIdxQueue.add(random.nextInt(0,7));
		}
		
		this.colorIdxQueue = new LinkedList<Integer>();
		
		for (int i = 0;i<5;i++) {
			this.colorIdxQueue.add(random.nextInt(tetColors.length));
		}
		this.genNewTetromino(this.tetIdxQueue.remove(), this.tetColors[this.colorIdxQueue.remove()]);
		
		int tetX = this.currTetromino.getX();
		int tetY = this.currTetromino.getY();
		int tetW = this.currTetromino.getWidth();
		int tetH = this.currTetromino.getHeight();
		
//		generate board
		for (int i = 0;i<height;i++) {
			this.board.add(new ArrayList<Block>());
			for (int j = 0;j<width;j++) {
				BlockState state = BlockState.AIR;
				if (i>=this.cy-this.planetSize && 
					j>=this.cx-this.planetSize && 
					i<=this.cy+this.planetSize && 
					j<=this.cx+this.planetSize)
					state = BlockState.PLANET;
				if (i>=tetY &&
						i<tetY + tetH &&
						j>=tetX &&
						j<tetX + tetW)
				{
					int[][] tetShape = this.currTetromino.getTetrominoShape();
					if (i-tetY>=0 && i-tetY<tetShape.length && j-tetX>=0 && j-tetX<tetShape[0].length) {
						if (tetShape[i-tetY][j-tetX]==1) state = BlockState.TETROMINO_HOVERING;
					}
				}
				if (this.placedBlocks[i][j]!=null) {
					state = BlockState.TETROMINO_PLACED;
				}
				
				this.board.get(i).add(new Block(state));
			}
		}
	}
	
	public void setNextTetPanel(NextTetPanel nextTetPanel) {
		this.nextTetPanel = nextTetPanel;
	}
	
	public void restartGame() {
		this.gameOver = false;
		this.placedBlocks = new BlockColor[height][width];
		this.tetIdxQueue.clear();
		
		for (int i = 0;i<5;i++) {
			this.tetIdxQueue.add(random.nextInt(0,7));
		}
		
		this.colorIdxQueue.clear();
		
		for (int i = 0;i<5;i++) {
			this.colorIdxQueue.add(random.nextInt(tetColors.length));
		}
		this.genNewTetromino(this.tetIdxQueue.remove(), this.tetColors[this.colorIdxQueue.remove()]);
		
	}
	
	public BlockState getCellBlockState(int x, int y) {
		return this.board.get(y).get(x).getBlockState();
	}
	
	public BlockColor getCellColor(int x, int y) {
		if (this.placedBlocks[y][x]!=null) return this.placedBlocks[y][x];
		else {
			BlockState cellBlockState = this.getCellBlockState(x,y);
			if (cellBlockState==BlockState.PLANET) return BlockColor.GREY;
			else if (cellBlockState==BlockState.TETROMINO_HOVERING || cellBlockState==BlockState.TETROMINO_PLACED) return this.currTetromino.getColor();
			else if (cellBlockState==BlockState.AIR) return BlockColor.BLACK;
		}
		return null;
	}
	
	//	tetromino management
	
	public void genNewTetromino(int tetId, BlockColor color) {
		Position[] faces = Position.values();
		Position face = faces[random.nextInt(faces.length)];
		
		int tetX = 0,tetY = 0;
		Tetromino tempTetromino = new Tetromino(tetX,tetY,tetId,color);
		int tetH = tempTetromino.getHeight();
		int tetW = tempTetromino.getWidth();
		
		switch (face) {
			case BOTTOM -> {
				tetX = random.nextInt(borderWidth,width-tetW-borderWidth);
				tetY = height-tetH-borderWidth;
			}
			case TOP -> {
				tetX = random.nextInt(borderWidth,width-tetW-borderWidth);
				tetY = borderWidth;
			}
			case LEFT -> {
				tetX = borderWidth;
				tetY = random.nextInt(borderWidth,height-tetH-borderWidth);
			}
			case RIGHT -> {
				tetW = width - tetW - borderWidth;
				tetY = random.nextInt(borderWidth,height-tetH-borderWidth);
			}
		}
		
		this.currTetromino = new Tetromino(tetX,tetY,tetId,color);
		// append new tet
		this.tetIdxQueue.add(random.nextInt(0,7));
		this.colorIdxQueue.add(random.nextInt(this.tetColors.length));
		
		if (nextTetPanel!=null) this.nextTetPanel.repaint();
	}
	
	public void placeTetromino() {
		int tetH = this.currTetromino.getHeight();
		int tetW = this.currTetromino.getWidth();
		
		int tetX = this.currTetromino.getX();
		int tetY = this.currTetromino.getY();
		
		int[][] tetShape = this.currTetromino.getTetrominoShape();
		
		BlockColor tetColor = this.currTetromino.getColor();
		
		for (int i = 0;i<tetH;i++) {
			for (int j = 0;j<tetW;j++) {
				int y = tetY + i, x = tetX + j;
				if (tetShape[i][j]==1) this.placedBlocks[y][x] = tetColor;
			}
		}
		
		this.clearPlanetLayers();
		
//		determine whether it's game over or not lmaoo
		this.gameOver = this.isGameOver(); 
		
		this.genNewTetromino(this.tetIdxQueue.remove(),this.tetColors[this.colorIdxQueue.remove()]);
	}
	
	public void moveCurrTetromino(Direction dir) {
		int[] playerDelta = this.currTetromino.getPlayerMoveDelta(dir, this.height, this.width);
		if (!this.isFutureCollision(playerDelta)) {
			int n = this.board.size(), m = this.board.get(0).size();
			this.currTetromino.playerMove(dir,n,m);
		}
	}
	
//	POS ONLY
	public boolean isFutureCollision(int[] deltaPos) {
//		detect tetromino touched either planet or other tetrominoes
		int tetX = this.currTetromino.getX();
		int tetY = this.currTetromino.getY();
		
//		int[] delta = this.currTetromino.calcNextPos(this.cx,this.cy,this.width,this.height,this.planetSize);
		int dy = deltaPos[0], dx = deltaPos[1];
		int ny = dy + tetY, nx = dx + tetX;
		
		// touched other placed tetrominoes
		// determine adj
		for (int[] blockPos : this.currTetromino.getSolidBlocksPos()) {
			// use new pos for this one
			int i = blockPos[0] + ny;
			int j = blockPos[1] + nx;
						
			
			// OOB collision tingy
			if (i < 0 || i >= placedBlocks.length || j < 0 || j >= placedBlocks[0].length) {
		        return true;
		    }
			
			// detect if it intersects other placed tetrominoes
			if (this.placedBlocks[i][j] != null ||
				this.getCellBlockState(i, j) == BlockState.PLANET) {
				return true;
			}
		}
		return false;
	}
	
//	ROTATION ONLY
	public boolean isFutureCollision(Direction rot) {
		int[][] rotatedArr = this.currTetromino.getRotatedArr(rot);
//		int n = rotatedArr.length, m = rotatedArr[0].length;
		
		int tetX = this.currTetromino.getX();
		int tetY = this.currTetromino.getY();
		
		ArrayList<int[]> blocksPos = this.currTetromino.getSolidBlocksPosFromArr(rotatedArr);
		for (int[] blockPos : blocksPos) {
			// use new pos for this one
			int i = blockPos[0] + tetX;
			int j = blockPos[1] + tetY;
									
			// detect if it intersects other placed tetrominoes
			if (this.placedBlocks[i][j] != null ||
				this.getCellBlockState(i, j) == BlockState.PLANET) {
				return true;
			}
		}
		return false;
	}
	
	// update tetromino due to gravity (USING TICKS)
	public void updateTetromino() {
//		detect tetromino touched either planet or other tetrominoes
		
		int[] delta = this.currTetromino.calcNextPos(this.cx,this.cy,this.width,this.height,this.planetSize);
		if (this.isFutureCollision(delta)) this.placeTetromino();
		else this.currTetromino.updatePos(delta);
	}
	
	// rotate tetromino
	public void rotateTetromino(Direction dir) {
		if (!this.isFutureCollision(dir)) this.currTetromino.rotate(dir);
	}
	
	private void clearPlanetLayers() {
	    int startX = this.cx - this.planetSize;
	    int endX = this.cx + this.planetSize;
	    int startY = this.cy - this.planetSize;
	    int endY = this.cy + this.planetSize;

	    // TOP DIR
	    for (int i = startY - 1; i >= 0; i--) {
	        boolean full = true;
	        for (int j = startX; j <= endX; j++) {
	            if (this.placedBlocks[j][i] == null) {
	                full = false;
	                break;
	            }
	        }
	        if (full) {
	            // clear row
	            for (int j = startX; j <= endX; j++) {
	                this.placedBlocks[j][i] = null;
	            }
	            // shift above downward
	            for (int idx = i; idx > 0; idx--) {
	                for (int j = startX; j <= endX; j++) {
	                    this.placedBlocks[j][idx] = this.placedBlocks[j][idx-1];
	                }
	            }
	            // clear top row
	            for (int j = startX; j <= endX; j++) this.placedBlocks[j][0] = null;

	            this.score += 100;
	            i++; // recheck this row
	        }
	    }

	    // BOTTOM DIR
	    for (int i = endY + 1; i < this.placedBlocks[0].length; i++) {
	        boolean full = true;
	        for (int j = startX; j <= endX; j++) {
	            if (this.placedBlocks[j][i] == null) {
	                full = false;
	                break;
	            }
	        }
	        if (full) {
	            // clear row
	            for (int j = startX; j <= endX; j++) this.placedBlocks[j][i] = null;
	            // shift below upward
	            for (int idx = i; idx < this.placedBlocks[0].length - 1; idx++) {
	                for (int j = startX; j <= endX; j++) {
	                    this.placedBlocks[j][idx] = this.placedBlocks[j][idx+1];
	                }
	            }
	            // clear bottom row
	            for (int j = startX; j <= endX; j++) this.placedBlocks[j][this.placedBlocks[0].length - 1] = null;

	            this.score += 100;
	            i--; // recheck this row
	        }
	    }

	    // LEFT DIR
	    for (int j = startX - 1; j >= 0; j--) {
	        boolean full = true;
	        for (int i = startY; i <= endY; i++) {
	            if (this.placedBlocks[j][i] == null) {
	                full = false;
	                break;
	            }
	        }
	        if (full) {
	            // clear column
	            for (int i = startY; i <= endY; i++) this.placedBlocks[j][i] = null;
	            // shift leftward
	            for (int idx = j; idx > 0; idx--) {
	                for (int i = startY; i <= endY; i++) {
	                    this.placedBlocks[idx][i] = this.placedBlocks[idx-1][i];
	                }
	            }
	            // clear leftmost column
	            for (int i = startY; i <= endY; i++) this.placedBlocks[0][i] = null;

	            this.score += 100;
	            j++; // recheck this column
	        }
	    }

	    // RIGHT DIR
	    for (int j = endX + 1; j < this.placedBlocks.length; j++) {
	        boolean full = true;
	        for (int i = startY; i <= endY; i++) {
	            if (this.placedBlocks[j][i] == null) {
	                full = false;
	                break;
	            }
	        }
	        if (full) {
	            // clear column
	            for (int i = startY; i <= endY; i++) this.placedBlocks[j][i] = null;
	            // shift rightward
	            for (int idx = j; idx < this.placedBlocks.length - 1; idx++) {
	                for (int i = startY; i <= endY; i++) {
	                    this.placedBlocks[idx][i] = this.placedBlocks[idx+1][i];
	                }
	            }
	            // clear rightmost column
	            for (int i = startY; i <= endY; i++) this.placedBlocks[this.placedBlocks.length - 1][i] = null;

	            this.score += 100;
	            j--; // recheck this column
	        }
	    }
	}
	
//	GAME OVERRR METHOD
	
	public boolean isGameOver() {
		int minX = this.borderWidth, maxX = this.width-this.borderWidth-1;
		int minY = this.borderWidth, maxY = this.height-this.borderWidth-1;
		
		for (int i = 0;i<this.height;i++) {
			for (int j = 0;j<this.width;j++) {
				if (i<=minY || i>=maxY || j<=minX || j>=maxX) {
					if (this.placedBlocks[i][j]!=null) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
//	board render function
	
	public void updateBoard() {
		// check if bars are filled on all 4 sides

//		clearPlanetLayers();
		
		int tetH = this.currTetromino.getHeight();
		int tetW = this.currTetromino.getWidth();
		
		int tetX = this.currTetromino.getX();
		int tetY = this.currTetromino.getY();
		
		for (int i = 0;i<height;i++) {
			for (int j = 0;j<width;j++) {
				BlockState state = BlockState.AIR;
				if (i>=this.cy-this.planetSize && 
					j>=this.cx-this.planetSize && 
					i<=this.cy+this.planetSize && 
					j<=this.cx+this.planetSize)
					state = BlockState.PLANET;
				if (i>=tetY && i<tetY + tetH && j>=tetX && j<tetX + tetW)
				{
					int[][] tetShape = this.currTetromino.getTetrominoShape();
					if (tetShape[i-tetY][j-tetX]==1) state = BlockState.TETROMINO_HOVERING;
				}
				if (this.placedBlocks[i][j]!=null) {
					state = BlockState.TETROMINO_PLACED;
				}
				this.board.get(i).get(j).setBlockState(state);
			}
		}
	}
	
	// renders to console
	public void renderBoard() {
		for (int i = 0;i<this.board.size();i++) {
			for (int j = 0;j<this.board.get(i).size();j++) {
				Block block = this.board.get(i).get(j);
				BlockState state = block.getBlockState();
				if (state==BlockState.AIR) System.out.print('⬜');
				else if (state==BlockState.TETROMINO_HOVERING) System.out.print('▦');
				else if (state==BlockState.TETROMINO_PLACED) System.out.print('.');
				else if (state==BlockState.PLANET) System.out.print('■');
			}
			System.out.println();
		}
	}
	
	public String getBoardString() {
		String res = "";
		for (int i = 0;i<this.board.size();i++) {
			for (int j = 0;j<this.board.get(i).size();j++) {
				Block block = this.board.get(i).get(j);
				BlockState state = block.getBlockState();
				if (state==BlockState.AIR) res += "🟫";
				else if (state==BlockState.TETROMINO_HOVERING) res += "🟥";
				else if (state==BlockState.TETROMINO_PLACED) res += "🟥";
				else if (state==BlockState.PLANET) res += "❎";
				res += "";
			}
			res += "\n";
		}
		return res;
	}
	
	public Queue<Integer> getTetIdxQueue() {
		return this.tetIdxQueue;
	}
	
	public Queue<Integer> getColorIdxQueue() {
		return this.colorIdxQueue;
	}
	
	public boolean getGameOver() {
		return this.gameOver;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public int getCx() {
		return cx;
	}

	public void setCx(int cx) {
		this.cx = cx;
	}

	public int getCy() {
		return cy;
	}

	public void setCy(int cy) {
		this.cy = cy;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPlanetSize() {
		return planetSize;
	}

	public void setPlanetSize(int planetSize) {
		this.planetSize = planetSize;
	}

	public ArrayList<ArrayList<Block>> getBoard() {
		return board;
	}

	public void setBoard(ArrayList<ArrayList<Block>> board) {
		this.board = board;
	}

	public BlockColor getCurrTetColor() {
		return currTetColor;
	}

	public void setCurrTetColor(BlockColor currTetColor) {
		this.currTetColor = currTetColor;
	}
}
