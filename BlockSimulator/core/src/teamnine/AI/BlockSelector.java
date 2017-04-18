package teamnine.AI;

import java.util.ArrayList;

import teamnine.blocksim.Block;
import teamnine.blocksim.Grid;

public class BlockSelector {
	
	private ArrayList<Block> robotBlockList;
	private int pointer;
	
	
	public BlockSelector(ArrayList<Block> robotBlockList, Grid grid){
		this.robotBlockList = robotBlockList;
		
		//sort the blocks, based on height??
		QuickSort<Block> sorter = new QuickSort<Block>();
		BlockComparator comparator = new BlockComparator();
		sorter.sort(this.robotBlockList, comparator); //Maybe the original robotBlockList stays untouched
		pointer = robotBlockList.size();
	}

	public Block getNextBlock() {
		if(pointer==0) return null;
		
		// has to select a block, without creating an invalid configuration... (not completely invalid (since not all robotblocks have to be attached together anymore)
		// starts with the highest element (last one in the robotBlockList)
		pointer -= 1;
		return robotBlockList.get(pointer);
	}

}
