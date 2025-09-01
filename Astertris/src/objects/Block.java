package objects;

import utils.BlockState;

public class Block {
	private BlockState blockState;
	public Block(BlockState blockState) {
		this.blockState = blockState;
	}
	public BlockState getBlockState() {
		return this.blockState;
	}
	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}
}
