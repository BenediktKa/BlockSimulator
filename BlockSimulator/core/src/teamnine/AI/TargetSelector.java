package teamnine.AI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.Block;
import teamnine.blocksim.Grid;

public class TargetSelector {
	private ArrayList<Block> targetBlockList;
	private int pointer;

	public TargetSelector(ArrayList<Block> targetBlockList, Grid grid) {
		this.targetBlockList = targetBlockList;
		QuickSort<Block> sorter = new QuickSort<Block>();
		BlockComparator comparator = new BlockComparator();
		sorter.sort(this.targetBlockList, comparator); //Maybe the original robotBlockList stays untouched
		pointer = 0;
	}

	public Vector3 getLocation(Block selectedBlock) {
		// Has to find a suitable place for the selected block, this can be specified with the ID, otherwise, science will have to figure it out
		
		double IDToFind = selectedBlock.getID();
		
		if ((Double)IDToFind != null){
			
			//Find the target position with the same ID
			for( int i=0; i<targetBlockList.size(); i++){
				if(targetBlockList.get(i).getID()==IDToFind) return targetBlockList.get(i).getPosition();
			}
			
		}
		
		else { 
			
			// return the best suitable position, this should not have a target inside, for now it returns the lowest available position
			while(pointer<targetBlockList.size()){
				if((Double) targetBlockList.get(pointer).getID() == null) pointer++; 
				else {
					pointer++;
					return targetBlockList.get(pointer).getPosition();
				}
			}
		}
		
		// TODO: FIND THE BEST SUITABLE POSITION
		
		return null; //Something went wrong
	}

}
