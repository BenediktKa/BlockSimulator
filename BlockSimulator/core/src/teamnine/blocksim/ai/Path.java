package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.blocklist.BlockListController;

public class Path {
	
	private BlockListController blockListController;
	//private Block target;
	private Block intitalPosition;
	private ArrayList<Vector3> bestPath=new ArrayList<Vector3>();
	private int bestDistance=-1;
	private int currentDistance=0;
	private ArrayList<Vector3> currentPath=new ArrayList<Vector3>();

	public Path()
	{
		//System.out.println("Path" +target.getPosition());
		this.blockListController = BlockListController.getInstance(); 
	}
	
	public void findPath(Vector3 startPosition, Block targetPosition)
	{
		Vector3 start = startPosition;
		Block target = targetPosition;
		
		if(start.x==target.getPosition().x&&start.z==target.getPosition().z)
		{
			return;
		}
		currentPath.add(new Vector3(start.x,start.y,start.z));
		currentDistance=currentDistance+(int) ((Math.abs(start.x - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1);
		ArrayList<Vector3> moves = new ArrayList<Vector3>();
		ArrayList<Integer> moveDistances = new ArrayList<Integer>();
		
		moves.add(new Vector3(start.x-1,0,start.z));
		moveDistances.add((int) ((Math.abs(start.x-1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1));
		moves.add(new Vector3(start.x+1,0,start.z));
		moveDistances.add((int) ((Math.abs(start.x+1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1));
		moves.add(new Vector3(start.x,0,start.z-1));
		moveDistances.add((int) ((Math.abs(start.x - target.getPosition().x) + Math.abs(start.z - target.getPosition().z-1) + start.y) - 1));
		moves.add(new Vector3(start.x,0,start.z+1));
		moveDistances.add((int) ((Math.abs(start.x-1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z+1) + start.y) - 1));
		
		System.out.println(moves.size()+" "+moveDistances.size()+" "+moveDistances.get(0));
		int bestMoveD=-1;
		Vector3 bestMove= new Vector3(0,0,0);
		for(int i=0;i<moves.size();i++)
		{
			if(bestMoveD==-1)
			{
				bestMoveD=moveDistances.get(i);
				bestMove=moves.get(i);
			}
			if(moveDistances.get(i)<bestMoveD)
			{
				bestMoveD=moveDistances.get(i);
				bestMove=moves.get(i);
			}
		}
		currentPath.add(bestMove);
		currentDistance=currentDistance+bestMoveD;
		System.out.println(bestMove+" "+bestMoveD);
		findPath(bestMove, targetPosition);
		
	}
	public ArrayList<Vector3> getFinalList()
	{
		
		return currentPath;
	}
}