package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.BlockSimulator;
import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.block.blocklist.BlockListController;

/**
 * Used to move a robot block from a certain position, to a specified target position
 * Required is that from all the robot modules, at least one is already placed in a target position.
 *
 */
public class SmartMovement 
{
	private BlockListController blockListController;
	private final boolean DEBUG = false;
	private int timestep;
	private boolean climbPossible;
	
	public SmartMovement(int timestep)
	{
		this.blockListController = BlockListController.getInstance();
		this.timestep = timestep;
		
	}
	
	public boolean newSmartMove(RobotBlock movingBlock, Vector3 targetPosition)
	{
		//keep track of performed movements
		ArrayList<Vector3> possibleMovements = getPossibleMovements(movingBlock);
		
		while(!movingBlock.getPosition().equals(targetPosition)&&possibleMovements.size()>0)
		{
			Vector3 moveTo = bestMovement(possibleMovements,targetPosition,movingBlock.getPosition());
			
			performMovement(moveTo, movingBlock);
			
			if (DEBUG) System.out.println("//S-MOVE: Block to position: "+movingBlock.getPosition()+" "+moveTo+"\n");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			possibleMovements = getPossibleMovements(movingBlock);
			
			
		}
		
		if(DEBUG&&possibleMovements.size()==0) System.out.println("//S-MOVE: Block Locked");
		if(DEBUG&&movingBlock.getPosition().equals(targetPosition)) System.out.println("//S-MOVE: Block in position: "+movingBlock.getPosition()+" "+targetPosition);
		
		if(movingBlock.getPosition().equals(targetPosition))
		{
			return true; //Movement has succeeded
		}
		return false; //Movement has failed
	}
	
	public int getTimestep() {return timestep;}
	
	private ArrayList<Vector3> getPossibleMovements(RobotBlock movingBlock)
	{
		ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
		
		climbPossible = false;
		Vector3 aboveBlock = new Vector3(movingBlock.getPosition().x, movingBlock.getPosition().y+1, movingBlock.getPosition().z);
		if (blockListController.getBlockAtPointWithType(aboveBlock, BlockType.Robot) == null) climbPossible=true;
		
		if(movingBlock.getPosition().x>0)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y,movingBlock.getPosition().z);
			Vector3 climb = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y+1,movingBlock.getPosition().z);
			Vector3 fall = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y-1,movingBlock.getPosition().z);
			Vector3 underShift = fall;
			Vector3 underClimb = shift;
			Vector3 underFall = new Vector3 (movingBlock.getPosition().x-1,1,movingBlock.getPosition().z);
			
			doChecks(possibleMovements, shift, climb, fall, underShift, underClimb, underFall);
		}
		
		if(movingBlock.getPosition().z>0)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y,movingBlock.getPosition().z-1);
			Vector3 climb = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y+1,movingBlock.getPosition().z-1);
			Vector3 fall = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y-1,movingBlock.getPosition().z-1);
			Vector3 underShift = fall;
			Vector3 underClimb = shift;
			Vector3 underFall = new Vector3(movingBlock.getPosition().x,1,movingBlock.getPosition().z-1);
			
			doChecks(possibleMovements, shift, climb, fall, underShift, underClimb, underFall);
			
		}
		
		if(movingBlock.getPosition().x<BlockSimulator.gridSize-1)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y,movingBlock.getPosition().z);
			Vector3 climb = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y+1,movingBlock.getPosition().z);
			Vector3 fall = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y-1,movingBlock.getPosition().z);
			Vector3 underShift = fall;
			Vector3 underClimb = shift;
			Vector3 underFall = new Vector3(movingBlock.getPosition().x+1,1,movingBlock.getPosition().z);
			
			doChecks(possibleMovements, shift, climb, fall, underShift, underClimb, underFall);
			
		}
		
		if(movingBlock.getPosition().z<BlockSimulator.gridSize-1)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y,movingBlock.getPosition().z+1);
			Vector3 climb = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y+1,movingBlock.getPosition().z+1);
			Vector3 fall = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y-1,movingBlock.getPosition().z+1);
			Vector3 underShift = fall;
			Vector3 underClimb = shift;
			Vector3 underFall = new Vector3(movingBlock.getPosition().x,1,movingBlock.getPosition().z+1);
			
			doChecks(possibleMovements, shift, climb, fall, underShift, underClimb, underFall);
			
		}
		
		if(DEBUG) System.out.println("//S-MOVE: # possiblilities: "+possibleMovements.size());
		return possibleMovements;
	}
	
	private void doChecks(ArrayList<Vector3> possibleMovements, Vector3 shift, Vector3 climb, Vector3 fall, Vector3 underShift, Vector3 underClimb, Vector3 underFall)
	{
		/*SHIFT IF
		 * No robot or obstacle module is on the new position
		 * Robot module is underneath the new position
		 * A goal block is on the new position, but should be surrounded by at least one robot module
		*/
		
		if(blockListController.getBlockAtPointIgnoreType(shift, BlockType.Goal)==null)
		{
			if(blockListController.getBlockAtPointWithType(underShift, BlockType.Robot)!=null 
				|| blockListController.getBlockAtPointWithType(underFall, BlockType.Robot)!=null)
			{
				possibleMovements.add(shift);
			}
			else if(blockListController.getBlockAtPointWithType(underShift, BlockType.Goal)!=null
					|| blockListController.getBlockAtPointWithType(underFall, BlockType.Goal)!=null)
			{
				possibleMovements.add(shift);
			}
		}
		
		/*CLIMB ALS
		 * No robot module is above the moving robot module
		 * Robot module underneath the new position
		 * No robot/obstacle module on the new position
		 */

		else if(climbPossible && 
				blockListController.getBlockAtPointWithType(underClimb, BlockType.Robot)!=null &&
				blockListController.getBlockAtPointIgnoreType(climb, BlockType.Goal)==null)
		{
			possibleMovements.add(climb);
		}
	}
	
	private Vector3 bestMovement(ArrayList<Vector3> possibleMovements, Vector3 targetPosition, Vector3 oldPosition)
	{
		//int absDistance = Integer.MAX_VALUE;
		int absDistance=(int) (Math.abs(oldPosition.x - targetPosition.x) + Math.abs(oldPosition.z - targetPosition.z) + Math.abs(oldPosition.y - targetPosition.y));
		int bestHeight = (int) oldPosition.y;
		//int oldHeight = 0;
		Vector3 bestPosition = null;
		
		for(Vector3 vector : possibleMovements)
		{
			int thisHeight = (int) vector.y;
			int thisDistance = (int) (Math.abs(vector.x - targetPosition.x) + Math.abs(vector.z - targetPosition.z) + Math.abs(vector.y - targetPosition.y));
			if (DEBUG) System.out.println("//S-MOVE: th: "+ thisHeight + " bh: "+bestHeight+" td: "+thisDistance+ " ad: "+ absDistance);
			if(thisHeight>bestHeight && thisDistance <= absDistance)
			{
				bestHeight = thisHeight;
				absDistance = thisDistance;
				bestPosition = vector;
			}
		}
		if(null != bestPosition)
		{
			if(DEBUG) System.out.println("//S-MOVE: best movement climbing: "+bestPosition);
			return bestPosition;
		}
		
		for(Vector3 vector : possibleMovements)
		{
			int thisDistance = (int) (Math.abs(vector.x - targetPosition.x) + Math.abs(vector.z - targetPosition.z) + Math.abs(vector.y - targetPosition.y));
			if (DEBUG) System.out.println("//S-MOVE: td: "+thisDistance+ " ad: "+ absDistance);
			if(thisDistance<=absDistance)
			{
				absDistance = thisDistance;
				bestPosition = vector;
			}
		}
		if(null != bestPosition)
		{
			if(DEBUG) System.out.println("//S-MOVE: best movement shifting/falling: "+bestPosition);
			return bestPosition;
		}
		if(DEBUG) System.out.println("//S-Move: no good position found");
		return null;
	}
	

	private void performMovement(Vector3 moveTo, RobotBlock movingBlock) 
	{
		checkIfCanMove(movingBlock);
		if (DEBUG) System.out.println("//S-MOVE: moving: "+movingBlock.getID());
		if(movingBlock.getPosition().y < moveTo.y) movingBlock.climb();
		
		if(movingBlock.getPosition().x < moveTo.x) movingBlock.moveRight();
		else if(moveTo.x < movingBlock.getPosition().x) movingBlock.moveLeft();
		else if(movingBlock.getPosition().z < moveTo.z) movingBlock.moveForward();
		else if(moveTo.z < movingBlock.getPosition().z) movingBlock.moveBackwards();
		
		timestep++;
		
	}
	
	private void checkIfCanMove(RobotBlock b)
	{
		if (StateManager.state == SimulationState.MENU || StateManager.state == SimulationState.BUILD)
		{
			try
			{
				Thread.currentThread().join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		while (b.getMoving() || StateManager.state == SimulationState.PAUSE)
		{
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}