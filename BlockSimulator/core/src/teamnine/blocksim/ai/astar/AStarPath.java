package teamnine.blocksim.ai.astar;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.blocklist.BlockListController;

public class AStarPath
{
	private BlockListController blockListController = null;

	public AStarPath()
	{
		blockListController = BlockListController.getInstance();
	}

	public ArrayList<Vector3> initializeAStar()
	{
		int nodeGrid[][][] = constructNodeGrid(blockListController.getFloorGridSize());
		int agentCount = blockListController.getBlockList(BlockType.Robot).size();
		int goalCount = blockListController.getBlockList(BlockType.Goal).size();
		
		if (agentCount < 2)
			return null;
		
		Vector3 startPos = blockListController.getBlockList(BlockType.Robot).get(1).getPosition();

		return buildPath(nodeGrid, startPos, agentCount, goalCount);
	}

	public int[][][] constructNodeGrid(int gridSize)
	{
		int nodeGrid[][][] = new int[gridSize][gridSize][gridSize];

		for (Block obstacle : blockListController.getCombinedBlockList(BlockType.Floor, BlockType.Obstacle))
		{
			Vector3 pos = obstacle.getPosition();
			nodeGrid[(int) pos.x][(int) pos.y][(int) pos.z] = -1;
		}
		return nodeGrid;
	}

	public ArrayList<Vector3> buildPath(int nodeGrid[][][], Vector3 startPos, int agentCount, int goalCount)
	{
		
		return null;
	}
}
