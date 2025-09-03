package objects;

import java.util.ArrayList;

import utils.BlockColor;
import utils.Direction;
import utils.Position;

import utils.Toolbox;

public class Tetromino {
//	anchored to CENTER
	int x,y,w,h,tetrominoId;
	BlockColor color;
	Toolbox toolbox;
	
	private int[][][] tetrominoShapes;
	private int[][] selectedTetrominoShape;
	
	private ArrayList<int[]> solidBlocksPos = new ArrayList<int[]>();
	
	public Tetromino(int x, int y, int tetrominoId, BlockColor color) {
		this.x = x;
		this.y = y;
		this.tetrominoId = tetrominoId;
		this.color = color;
		
		this.toolbox = new Toolbox();
		this.tetrominoShapes = toolbox.getTetrominoShapes();
		
		this.selectedTetrominoShape = this.tetrominoShapes[this.tetrominoId];
		this.h = this.selectedTetrominoShape.length;
		this.w = this.selectedTetrominoShape[0].length;
		
		// solid blocks pos
		this.updateSolidBlocksPos();
	}
	
	public void updateSolidBlocksPos() {
		solidBlocksPos = new ArrayList<int[]>();
		// solid blocks pos
		for (int i = 0;i<this.h;i++) {
			for (int j = 0;j<this.w;j++) {
				if (this.selectedTetrominoShape[i][j]==1) {
					int[] pair = new int[2];
					pair[0] = i;
					pair[1] = j;
					solidBlocksPos.add(pair);
				}
			}
		}
	}
	
	public BlockColor getColor() {
		return color;
	}

	public void setColor(BlockColor color) {
		this.color = color;
	}
	
	public int[][] getRotatedArr(Direction dir) {
		int n = this.selectedTetrominoShape.length;
		int m = this.selectedTetrominoShape[0].length;
		int[][] res = new int[m][n];
		if (dir==Direction.CLOCKWISE) {
			for (int i = 0;i<n;i++) {
				for (int j = 0;j<m;j++) res[j][n-1-i] = this.selectedTetrominoShape[i][j];
			}
		} else if (dir==Direction.COUNTERCLOCKWISE) {
			for (int i = 0;i<n;i++) {
				for (int j = 0;j<m;j++) res[m-1-j][i] = this.selectedTetrominoShape[i][j];
			}
		}
		return res;
	}

	public void rotate(Direction dir) {
		int[][] newArr = this.getRotatedArr(dir);
		this.selectedTetrominoShape = newArr;
		
		this.h = newArr.length;
	    this.w = newArr[0].length;
	    
	    this.updateSolidBlocksPos();
	}
	
	public Position getNearestSide(int n, int m) {
		Position pos = null;
		int minDir = Math.min(Math.min(n-this.y-1, m-this.x-1), Math.min(this.y, this.x));
		if (minDir==n-this.y-1) pos = Position.BOTTOM;
		else if (minDir==this.y) pos = Position.TOP;
		else if (minDir==m-this.x-1) pos = Position.RIGHT;
		else if (minDir==this.x) pos = Position.LEFT;
		return pos;
	}
	
	public Position getNearestSide(int n, int m, int x, int y) {
		Position pos = null;
		int minDir = Math.min(Math.min(n-y-1, m-x-1), Math.min(y, x));
		if (minDir==n-y-1) pos = Position.BOTTOM;
		else if (minDir==y) pos = Position.TOP;
		else if (minDir==m-x-1) pos = Position.RIGHT;
		else if (minDir==x) pos = Position.LEFT;
		return pos;
	}
	
	public int[] getPlayerMoveDelta(Direction dir, int n, int m) {
		int dy = 0, dx = 0;
		Position pos = this.getNearestSide(n, m);
		 
		if (dir==Direction.LEFT) {
			if (pos==Position.TOP) dx--;
			else if (pos==Position.RIGHT) dy--;
			else if (pos==Position.BOTTOM) dx++;
			else if (pos==Position.LEFT) dy++;
		} else if (dir==Direction.RIGHT) {
			if (pos==Position.TOP) dx++;
			else if (pos==Position.RIGHT) dy++;
			else if (pos==Position.BOTTOM) dx--;
			else if (pos==Position.LEFT) dy--;
		}
		
		int[] res = new int[2];
		res[0] = dy;
		res[1] = dx;
		return res;
	}
	
	// board (n * m)
	public void playerMove(Direction dir, int n, int m) {
//		get closest side the tetromino is on
		
		Position pos = this.getNearestSide(n, m);
		 
		if (dir==Direction.LEFT) {
			if (pos==Position.TOP) this.x--;
			else if (pos==Position.RIGHT) this.y--;
			else if (pos==Position.BOTTOM) this.x++;
			else if (pos==Position.LEFT) this.y++;
		} else if (dir==Direction.RIGHT) {
			if (pos==Position.TOP) this.x++;
			else if (pos==Position.RIGHT) this.y++;
			else if (pos==Position.BOTTOM) this.x--;
			else if (pos==Position.LEFT) this.y--;
		}
	}
	
	public int[] calcNextPos(int cx, int cy, int m, int n, int planetSize) {
		int[] res = new int[2];
		
		Position pos = this.getNearestSide(n, m);
		
		switch (pos) {
			case BOTTOM -> {
				if (this.x<cx-planetSize) res[1] = 1;
				else if (this.x>=cx+planetSize) res[1] = -1;
				res[0] = -1;
			}
			case TOP -> {
				if (this.x<cx-planetSize) res[1] = 1;
				else if (this.x>=cx+planetSize) res[1] = -1;
				res[0] = 1;
			}
			case LEFT -> {
				if (this.y<cy-planetSize) res[0] = 1;
				else if (this.y>=cy+planetSize) res[0] = -1;
				res[1] = 1;
			}
			case RIGHT -> {
				if (this.y<cy-planetSize) res[0] = 1;
				else if (this.y>=cy+planetSize) res[0] = -1;
				res[1] = -1;
			}
		}
		return res;
	}
	
	public void updatePos(int[] delta) {
		this.x += delta[1];
		this.y += delta[0];
	}
	
	public double[] calcVelMag(int nx, int ny) {
		double angle = Math.atan2(ny-this.y, nx-this.x);
		return new double[] {Math.sin(angle),Math.cos(angle)};
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public ArrayList<int[]> getSolidBlocksPos() {
		return solidBlocksPos;
	}
	
	public ArrayList<int[]> getSolidBlocksPosFromArr(int[][] arr) {
		ArrayList<int[]> res = new ArrayList<int[]>();
		for (int i = 0;i<arr.length;i++) {
			for (int j = 0;j<arr[i].length;j++) {
				int[] pair = new int[2];
				pair[0] = i;
				pair[1] = j;
				res.add(pair);
			}
		}
		return res;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTetrominoId() {
		return tetrominoId;
	}
	
	public int[][] getTetrominoShape() {
		return this.selectedTetrominoShape;
	}
	
	public int getWidth() {
		return this.w;
	}
	
	public int getHeight() {
		return this.h;
	}

	public void setTetrominoId(int tetrominoId) {
		this.tetrominoId = tetrominoId;
//		change tetromino afterwards
		this.selectedTetrominoShape = this.tetrominoShapes[this.tetrominoId];
		this.w = this.selectedTetrominoShape.length;
		this.h = this.selectedTetrominoShape[0].length;
	}
}
