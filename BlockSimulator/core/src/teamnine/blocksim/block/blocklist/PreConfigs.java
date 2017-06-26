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
		blockListController.createBlock(new Vector3(6, 1, 6), BlockType.Goal);
		blockListController.createBlock(new Vector3(6, 1, 5), BlockType.Goal);
		blockListController.createBlock(new Vector3(6, 1, 4), BlockType.Goal);
		blockListController.createBlock(new Vector3(7, 1, 5), BlockType.Goal);
		blockListController.createBlock(new Vector3(5, 1, 5), BlockType.Goal);
		blockListController.createBlock(new Vector3(5, 2, 5), BlockType.Goal);
		blockListController.createBlock(new Vector3(6, 2, 4), BlockType.Goal);
		blockListController.createBlock(new Vector3(7, 2, 5), BlockType.Goal);
		blockListController.createBlock(new Vector3(6, 2, 6), BlockType.Goal);
		blockListController.createBlock(new Vector3(6, 1, 22), BlockType.Robot);
		blockListController.createBlock(new Vector3(6, 1, 21), BlockType.Robot);
		blockListController.createBlock(new Vector3(6, 1, 20), BlockType.Robot);
		blockListController.createBlock(new Vector3(5, 1, 21), BlockType.Robot);
		blockListController.createBlock(new Vector3(7, 1, 21), BlockType.Robot);
		blockListController.createBlock(new Vector3(6, 2, 22), BlockType.Robot);
		blockListController.createBlock(new Vector3(6, 2, 20), BlockType.Robot);
		blockListController.createBlock(new Vector3(7, 2, 21), BlockType.Robot);
		blockListController.createBlock(new Vector3(5, 2, 21), BlockType.Robot);
	}
}
