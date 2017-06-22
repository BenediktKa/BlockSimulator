package teamnine.blocksim.block.blocklist;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockType;

public class PreConfigs
{
	BlockListController blockListController;
	public PreConfigs(BlockListController blockListController)
	{
		this.blockListController = blockListController;
	}
	
	public void experiment1()
	{
		blockListController.createBlock(new Vector3(14, 1, 1), BlockType.Goal);
		blockListController.createBlock(new Vector3(14, 1, 0), BlockType.Goal);
		blockListController.createBlock(new Vector3(18, 1, 0), BlockType.Robot);
		blockListController.createBlock(new Vector3(18, 1, 1), BlockType.Robot);
		blockListController.createBlock(new Vector3(16, 1, 0), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 1), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 2), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 3), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 4), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 5), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 6), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 7), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 8), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 9), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 10), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 11), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 12), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 13), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 14), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 15), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 16), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 17), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 19), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(16, 1, 18), BlockType.Obstacle);
	}
}
