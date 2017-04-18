package teamnine.AI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.Block;
import teamnine.blocksim.Grid;

public class AIMaster {
	private BlockSelector blockSelector;
	private TargetSelector targetSelector;
	private PathFinder pathFinder = new PathFinder();
	
	private ArrayList<Block> blockList; 
	private Grid grid;
	
	private ArrayList<Block> robotBlockList;
	private ArrayList<Block> targetBlockList;
	
	
	public AIMaster (ArrayList<Block> blockList, Grid grid){
		this.blockList = blockList;
		this.grid = grid;
		
		
		//Find all start and target positions
		for(int i = 0; i<blockList.size(); i++){
			if (blockList.get(i).getType() == Block.Type.Robot){
				robotBlockList.add(blockList.get(i));
			}else if (blockList.get(i).getType() == Block.Type.Goal){
				targetBlockList.add(blockList.get(i));
			}
		}
		
		//Please take into account that the list will be sorted, based on height
		blockSelector = new BlockSelector(robotBlockList, grid); 
		targetSelector = new TargetSelector(targetBlockList, grid);
		
		//TODO: HOW TO IMPLEMENT FEEDBACK FROM PATHFINDING ALGORITHM, i.e. it was unable to find a path (for example when a target position is not reachable)
		for(int i = 0; i<robotBlockList.size(); i++){
			Block selectedBlock = blockSelector.getNextBlock();
			Vector3 targetPosition = targetSelector.getLocation(selectedBlock);
			pathFinder.moveRobot(selectedBlock, targetPosition);
		}
		
	}
	
	public static void main (String[]args){
		
	}
}
