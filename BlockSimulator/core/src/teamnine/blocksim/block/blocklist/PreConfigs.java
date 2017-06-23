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
		blockListController.createBlock(new Vector3(12, 1, 13), BlockType.Goal);
		blockListController.createBlock(new Vector3(12, 1, 12), BlockType.Goal);
		blockListController.createBlock(new Vector3(5, 1, 13), BlockType.Robot);
		blockListController.createBlock(new Vector3(5, 1, 12), BlockType.Robot);
		blockListController.createBlock(new Vector3(8, 1, 16), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 1, 17), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 16), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 17), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 17), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 16), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 15), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 14), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 13), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 12), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 12), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 1, 12), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 13), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 1, 13), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 11), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 10), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 9), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 3, 8), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 9), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 2, 8), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 1, 8), BlockType.Obstacle);
		blockListController.createBlock(new Vector3(8, 1, 9), BlockType.Obstacle);
	}
}
