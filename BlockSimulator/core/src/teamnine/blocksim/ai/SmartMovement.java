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
 * Is not so smart
 * Only useful for reconfiguration purposes, when a robotblock is first moved to the targetOrigin by other movement method
 * @author jurri
 *
 */
public class SmartMovement 
{
	private BlockListController blockListController;
	private final boolean DEBUG = true;
	private int timestep;
	//private ArrayList<RobotBlock> robotBlockList;
	//private ArrayList<Block> obstacleBlockList;
	
	public SmartMovement(int timestep)
	{
		this.blockListController = BlockListController.getInstance();
		this.timestep = timestep;
		//this.robotBlockList = blockListController.getRobotBlockList();
		//this.obstacleBlockList = blockListController.getBlockList(BlockType.Obstacle);
		
	}
	
	public boolean newSmartMove(RobotBlock movingBlock, Vector3 targetPosition)
	{
		//first move it to targetOrigin by other movement, then get rid of the block stored in that list
		
		//keep track of performed movements
		ArrayList<Vector3> possibleMovements = getPossibleMovements(movingBlock);
		
		while(!movingBlock.getPosition().equals(targetPosition)&&possibleMovements.size()>0)
		{
			Vector3 moveTo = bestMovement(possibleMovements,targetPosition,movingBlock.getPosition());
			
			performMovement(moveTo, movingBlock);
			
			if (DEBUG) System.out.println("//S-MOVE: Block to position: "+movingBlock.getPosition()+" "+moveTo);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//TODO: POSSIBLE MOVEMENTS INVALID
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
	
	private ArrayList<Vector3> getPossibleMovements(RobotBlock movingBlock)
	{
		ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
		//TODO: WHAT ABOUT y-1
		
		boolean climbPossible = false;
		Vector3 aboveBlock = new Vector3(movingBlock.getPosition().x, movingBlock.getPosition().y+1, movingBlock.getPosition().z);
		if (blockListController.getBlockAtPointIgnoreType(aboveBlock, BlockType.Goal) == null) climbPossible=true;
		
		if(movingBlock.getPosition().x>0)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y,movingBlock.getPosition().z);
			Vector3 climb = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y+1,movingBlock.getPosition().z);
			Vector3 fall = new Vector3(movingBlock.getPosition().x-1,movingBlock.getPosition().y-1,movingBlock.getPosition().z);
			
			if(blockListController.getBlockAtPointIgnoreType(shift, BlockType.Goal)==null && /*blockListController.getBlockAtPoint(fall)!=null &&*/(surroundedByRobot(shift, movingBlock.getPosition())||surroundedByRobot(fall, movingBlock.getPosition())))
			{
				//check if there is a robot block surrounding
				
				//x-1 is possible
				possibleMovements.add(shift);
			}
			else if(climbPossible && blockListController.getBlockAtPointIgnoreType(climb, BlockType.Goal)==null && surroundedByRobot(climb, movingBlock.getPosition()))
			{
				//check if there is a robot block surrounding
				//x-1 y+1 is possible
				possibleMovements.add(climb);
			}
			
		}
		
		if(movingBlock.getPosition().z>0)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y,movingBlock.getPosition().z-1);
			Vector3 climb = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y+1,movingBlock.getPosition().z-1);
			Vector3 fall = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y-1,movingBlock.getPosition().z-1);
			
			if(blockListController.getBlockAtPointIgnoreType(shift, BlockType.Goal)==null && /*blockListController.getBlockAtPoint(fall)!=null &&*/ (surroundedByRobot(shift, movingBlock.getPosition())||surroundedByRobot(fall, movingBlock.getPosition())))
			{
				//check if there is a robot block surrounding
				
				//z-1 is possible
				possibleMovements.add(shift);
			}
			else if(climbPossible && blockListController.getBlockAtPointIgnoreType(climb, BlockType.Goal)==null && surroundedByRobot(climb, movingBlock.getPosition()))
			{
				//check if there is a robot block surrounding
				//z-1 y+1 is possible
				possibleMovements.add(climb);
			}
		}
		
		if(movingBlock.getPosition().x<BlockSimulator.gridSize-1)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y,movingBlock.getPosition().z);
			Vector3 climb = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y+1,movingBlock.getPosition().z);
			Vector3 fall = new Vector3(movingBlock.getPosition().x+1,movingBlock.getPosition().y-1,movingBlock.getPosition().z);
			
			if(blockListController.getBlockAtPointIgnoreType(shift, BlockType.Goal)==null && /*blockListController.getBlockAtPoint(fall)!=null &&*/ (surroundedByRobot(shift, movingBlock.getPosition())||surroundedByRobot(fall, movingBlock.getPosition())))			{
				//check if there is a robot block surrounding
				
				//x+1 is possible
				possibleMovements.add(shift);
			}
			else if(climbPossible && blockListController.getBlockAtPointIgnoreType(climb, BlockType.Goal)==null && surroundedByRobot(climb, movingBlock.getPosition()))
			{
				//check if there is a robot block surrounding
				//x+1 y+1 is possible
				possibleMovements.add(climb);
			}
		}
		
		if(movingBlock.getPosition().z<BlockSimulator.gridSize-1)
		{
			Vector3 shift = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y,movingBlock.getPosition().z+1);
			Vector3 climb = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y+1,movingBlock.getPosition().z+1);
			Vector3 fall = new Vector3(movingBlock.getPosition().x,movingBlock.getPosition().y-1,movingBlock.getPosition().z+1);
			
			if(blockListController.getBlockAtPointIgnoreType(shift, BlockType.Goal)==null /*&& blockListController.getBlockAtPoint(fall)!=null*/ && (surroundedByRobot(shift, movingBlock.getPosition())||surroundedByRobot(fall, movingBlock.getPosition())))
			{
				//check if there is a robot block surrounding
				
				//z+1 is possible
				possibleMovements.add(shift);
			}
			else if(climbPossible && blockListController.getBlockAtPointIgnoreType(climb, BlockType.Goal)==null && surroundedByRobot(climb, movingBlock.getPosition()))
			{
				//check if there is a robot block surrounding
				//z+1 y+1 is possible
				possibleMovements.add(climb);
			}
		}
		
		return possibleMovements;
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
			
			System.out.println(thisHeight+ " " + bestHeight);
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
			if(thisDistance<=absDistance)
			{
				absDistance = thisDistance;
				bestPosition = vector;
			}
		}
		if(DEBUG) System.out.println("//S-MOVE: best movement shifting/falling: "+bestPosition);
		return bestPosition;
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
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean surroundedByRobot(Vector3 newPosition, Vector3 oldPosition)
	{
		boolean surrounded = false;
		Vector3 position = new Vector3(newPosition.x+1, newPosition.y, newPosition.z);
		if(!position.equals(oldPosition) && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal)!=null && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal).getType().equals(BlockType.Robot))
		{
			surrounded = true;
		}
		
		position = new Vector3(newPosition.x-1, newPosition.y, newPosition.z);
		if(!position.equals(oldPosition) && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal)!=null && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal).getType().equals(BlockType.Robot))
		{
			surrounded = true;
		}
		
		position = new Vector3(newPosition.x, newPosition.y, newPosition.z+1);
		if(!position.equals(oldPosition) && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal)!=null && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal).getType().equals(BlockType.Robot))
		{
			surrounded = true;
		}
		
		position = new Vector3(newPosition.x, newPosition.y, newPosition.z-1);
		if(!position.equals(oldPosition) && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal)!=null && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal).getType().equals(BlockType.Robot))
		{
			surrounded = true;
		}
		
		position = new Vector3(newPosition.x, (newPosition.y)-1, newPosition.z);
		if(!position.equals(oldPosition) && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal)!=null && blockListController.getBlockAtPointIgnoreType(position, BlockType.Goal).getType().equals(BlockType.Robot))
		{
			surrounded = true;
		}
		if(!surrounded)
		{
			System.out.println("//S-MOVE: not surrounded"+newPosition);
		}
		else
		{
			System.out.println("//S-MOVE: surrounded"+newPosition);
		}
		return surrounded;
	}

}