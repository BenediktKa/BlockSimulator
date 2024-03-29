package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.BlockSimulator;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.block.blocklist.BlockListController;

/**
 * Responsible for managing the reconfiguration
 * > Defining the order in which targets should be filled
 * > Selecting the robot module that should be moved to the next target position
 * > Initializing movement of robot to target position
 * Issues:
 * > ID's not fully implemented
 *
 */
public class Reconfiguration
{
	private BlockListController blockListController;
	private ArrayList<RobotBlock> robots;
	private ArrayList<RobotBlock> robot; // robot modules that can be used for reconfiguration
	private ArrayList<Block> target;
	private ArrayList<Block>[] sortedTargets;
	private ArrayList<Block> easySortedTargets;
	private Vector3 targetOrigin;
	private int cntr;
	
	private final SmartMovement reconfigurationMovement;

	private final boolean DEBUG = false;

	/**
	 * Constructor for reconfiguration algorithm Checks if the number of robot
	 * blocks == number of target blocks Not yet implemented: check if the ID's
	 * correspond
	 * 
	 * @param robot list with all robot blocks in the neighborhood (how do we
	 *            know where they are exactly)
	 * @param target list with all target blocks
	 * @param vector3 the target block in the corner, closest to the robot
	 * @param movement 
	 */
	public Reconfiguration(Vector3 vector3, SmartMovement reconfigurationMovement)
	{
		robots = new ArrayList<RobotBlock>();
		

		this.blockListController = BlockListController.getInstance();
		
		for(Block block : blockListController.getBlockList(BlockType.Robot))
		{
			robots.add((RobotBlock)block);
		}
		
		this.target = blockListController.getBlockList(BlockType.Goal);
		targetOrigin = vector3;
		this.reconfigurationMovement = reconfigurationMovement;

		robot = findConnectedRobots();
		
		if (robot.size() < target.size())
		{
			throw new IllegalArgumentException("The number of robot blocks is less than to the number of target blocks!");
		}
		
		prepare();
		if (DEBUG)
		{
			System.out.println("//RECONFIG: STARTED");
		}
		// start(); //Take ID's into account -- Not implemented
		startEasy(); //ID's are not important
		check();
		Thread.currentThread().interrupt();
	}


	/**
	 * 1) Sort the targets in a specific way (how?) to avoid deadlocks 2) Figure
	 * out which robot-blocks do have to go to a specific target, and which ones
	 * doesn't
	 */
	private void prepare()
	{
		sortTarget(); // Sort the targetblocks in a specific way, to fill them
	}

	/**
	 * Stores the target-blocks in buckets, every bucket representing level with
	 * a set of target positions that should be filled first, before the
	 * positions will be filled (2x2x2 target, origin at left-top-on floor,
	 * level-matrix: floor[0,1;1,2] floor+1[5,4;4,3] 
	 * This does not have to be the most efficient way, since other targets
	 * could be reached as well and blocks for the other positions may be at a
	 * shorter path. Works in linear time
	 */
	private void sortTarget()
	{

		// Define origin for layer 0: point where robot blocks enter the goal
		// region
		Vector3 layer0Origin = new Vector3(targetOrigin.x, 1, targetOrigin.z);
		//System.out.println("// RECONFIG: "+targetOrigin);

		// Figure out what the dimensions of the target objects together are
		int targetWidth = 0;
		int targetLength = 0;
		int targetHeight = 0;
		
		float OppositeX = layer0Origin.x;
		float OppositeZ = layer0Origin.z;

		for (Block target: target)
		{
			float XDist = Math.abs(target.getPosition().x - layer0Origin.x);
			float ZDist = Math.abs(target.getPosition().z - layer0Origin.z);
			
			if(targetWidth < XDist+1)
			{
				targetWidth = (int) XDist+1;
				OppositeX = target.getPosition().x;
			}
			if(targetLength < ZDist+1)
			{
				targetLength = (int) ZDist+1;
				OppositeZ = target.getPosition().z;
			}

			targetHeight = (int) Math.max(targetHeight, target.getPosition().y - 1); 
			
		}
		Vector3 otherOrigin = new Vector3(OppositeX, 1, OppositeZ);
		
		if (DEBUG) System.out.println("// RECONFIG: TW: "+targetWidth+" TL: "+targetLength+" TH: "+targetHeight);
		if (DEBUG) System.out.println("// RECONFIG: Origin: "+layer0Origin+ " Other Origin " +otherOrigin);
		

		// The size of these arraylists depend on the dimension of the target
		// region (which is seen as one whole cube covering the region
		int floorLevels = targetWidth*targetLength;
		int nonFloorLevels = floorLevels*targetHeight;

		sortedTargets = new ArrayList[floorLevels + nonFloorLevels];

		for (int i = 0; i < sortedTargets.length; i++)
		{
			sortedTargets[i] = new ArrayList<Block>();
		}

		if (DEBUG)
		{
			System.out.println("// RECONFIG: " + "floor: " + floorLevels);
			System.out.println("// RECONFIG: " + "nonFloor: " + nonFloorLevels);
		}
		
		// Put target blocks in the right bucket
		for (Block target: target)
		{
			// Blocks on the floor go in the separate list
			if (target.getPosition().y == 1)
			{
				// the #bucket depends on the absolute distance to the origin
				// point (so: (x+1 & z)-->1, (x & z+1)-->1, (x+1 & z+1)-->2
				int bucket = (int) (Math.abs(target.getPosition().x - layer0Origin.x) + Math.abs(target.getPosition().z - layer0Origin.z));
				if (DEBUG)
				{
					System.out.println("// RECONFIG: " + target.getID() + ": " + bucket);
				}
				sortedTargets[bucket].add(target);
			}
			else
			{
				int bucket = (int) (Math.abs(target.getPosition().x - otherOrigin.x) + Math.abs(target.getPosition().z - otherOrigin.z)+Math.abs(target.getPosition().y - otherOrigin.y))+floorLevels-1;
				if (DEBUG)
				{
					System.out.println("// RECONFIG: " + target.getID() + ": " + bucket);
				}
				sortedTargets[bucket].add(target);
			}
		}
		
		//Easy sorting targets - since ID's are not taken into account
		easySortedTargets = new ArrayList<Block>();
		for(int i=0; i<sortedTargets.length; i++)
		{
			for(int j=0; j<sortedTargets[i].size(); j++)
			{
				easySortedTargets.add(sortedTargets[i].get(j));
			}
		}
	}
	
	/*
	 * Performing reconfiguration when ID's are not taken into account
	 */
	private void startEasy()
	{
		
		cntr = 0;
		
		while(cntr<target.size()) 
		{
			
			if (DEBUG) System.out.println("// RECONFIG: Cntr: "+cntr);
			// 1) Select last robot block to move, i.e. robot.get(0)????!!!!!?????!!!!!?????!!!!!?????!!!!
			final RobotBlock blockToMove = getFurthestRobot();
			
			// 2) Select the target position where it has to go to, there should not be a robot block on it
			//TODO: VULNERABLE POINT
			Block targetBlock = easySortedTargets.get(cntr); 
			cntr++; 
			
			// Check if there is not already a robot block on this position
			for(int i=0; i<robot.size(); i++)
			{
				if(robot.get(i).getPosition().x==targetBlock.getPosition().x&&robot.get(i).getPosition().y==targetBlock.getPosition().y&&robot.get(i).getPosition().z==targetBlock.getPosition().z)
				{
					targetBlock = easySortedTargets.get(cntr);
					if (DEBUG) System.out.println("// RECONFIG: target position skipped");
					cntr++;
					i=0;
				}
			}
			// 3) Find path from position of robotBlock to target position
			final Block targetBlockForMove = targetBlock;
			
			// THIRD: Do Reconfiguration Part
			
			if (DEBUG) System.out.println("// RECONFIG: Start smartMovement, block: "+blockToMove.getID()+ " to: "+targetBlockForMove.getPosition());
			Thread thread2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					if(reconfigurationMovement.newSmartMove(blockToMove, targetBlockForMove.getPosition()))
					{
						blockToMove.setInFinalPosition(true);
					}
					else changeCntr();
				}
				
			});
			thread2.start();
			
			while(thread2.isAlive())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
			if (DEBUG) System.out.println("// RECONFIG: CNTR: "+cntr+" ROBOT SIZE: "+robot.size());
		}
		
		if (DEBUG) System.out.println("// RECONFIG: End of While loop");
		
		System.out.println("TIMESTEP: "+reconfigurationMovement.getTimestep());
		
		

	}
	
	private void changeCntr(){
		cntr--;
	}

	private RobotBlock getFurthestRobot() {
		RobotBlock furthestBlock = null;
		int biggestDistance = 0;
		int maxHeight = 0;
		Vector3 targetOrigin = this.targetOrigin;
		System.out.println("ROBOT SIZE FOR RECONFIG: "+robot.size());
		
		for(RobotBlock block : robot)
		{
			Vector3 vector = block.getPosition();

			if(!block.isInFinalPosition()){
				int thisDistance = (int) (Math.abs(vector.x - targetOrigin.x) + Math.abs(vector.z - targetOrigin.z) + Math.abs(vector.y - targetOrigin.y));
				if(DEBUG) System.out.println("// RECONFIG: "+block.getID()+" "+thisDistance);
				
				int thisHeight = (int) vector.y;
				if(thisHeight > maxHeight)
				{
					maxHeight = thisHeight;
					biggestDistance = thisDistance;
					furthestBlock = block;
				}
				else if(thisHeight == maxHeight && thisDistance > biggestDistance)
				{
					if (DEBUG) System.out.println("// RECONFIG: Found one: "+block.getPosition()+" "+thisDistance+">"+biggestDistance);
					biggestDistance = thisDistance;
					furthestBlock = block;
					
				}
			}
			
			
		}
		
		if (DEBUG) System.out.println("// RECONFIG: Furthest robot block: "+furthestBlock.getID()+" "+furthestBlock.getPosition());
		return furthestBlock;
	}


	private boolean check()
	{
		int correctPositions = 0;

		for (Block block: target)
		{
			Vector3 targetPosition = block.getPosition();
			for (Block robotBlock: robot)
			{
				Vector3 robotPosition = robotBlock.getPosition();
				// Find robot & target block on the same position
				if (targetPosition.x == robotPosition.y && targetPosition.y == robotPosition.y && targetPosition.z == robotPosition.z)
				{
					// Check if ID is specified for target
					try
					{
						double targetID = block.getID();
						// Check if ID is specified for robot and matches with
						// target
						try
						{
							double robotID = robotBlock.getID();
							if (targetID == robotID)
							{
								correctPositions++;
							}
						} 
						catch (NullPointerException d)
						{
							return false;
						}
					} catch (NullPointerException e)
					{
						correctPositions++;
					}
				}
			}
		}
		if (correctPositions == target.size())
		{
			return true;
		}
		return false;
	}
	
	public ArrayList<RobotBlock> findConnectedRobots()
	{
		ArrayList<RobotBlock> alreadyFound = new ArrayList<RobotBlock>();
		alreadyFound.add((RobotBlock) blockListController.getBlockAtPointWithType(targetOrigin, BlockType.Robot)); 
		getConnectedRobots(alreadyFound.get(0), alreadyFound);
		return alreadyFound;
	}
	
	private void getConnectedRobots(Block block, ArrayList<RobotBlock> alreadyFound)
	{
		//TODO: ADD FOR INDEX OUT OF BOUNDS
		RobotBlock blockToAdd;
		ArrayList<Vector3> positions = new ArrayList<Vector3>();
		if(block.getPosition().x > 0) positions.add(new Vector3 (block.getPosition().x+1, block.getPosition().y, block.getPosition().z));
		if(block.getPosition().x < BlockSimulator.gridSize) positions.add(new Vector3 (block.getPosition().x, block.getPosition().y, block.getPosition().z-1));
		if(block.getPosition().z > 0) positions.add(new Vector3 (block.getPosition().x-1, block.getPosition().y, block.getPosition().z));
		if(block.getPosition().x < BlockSimulator.gridSize) positions.add(new Vector3 (block.getPosition().x, block.getPosition().y, block.getPosition().z+1));
		positions.add(new Vector3(block.getPosition().x, block.getPosition().y+1, block.getPosition().z));
		
		for(Vector3 position : positions)
		{
			if(null!=blockListController.getBlockAtPointWithType(position, BlockType.Robot))
			{
				blockToAdd = (RobotBlock) blockListController.getBlockAtPointWithType(position, BlockType.Robot);
				//if not added already > add and recurse
				if(!alreadyFound.contains(blockToAdd))
				{
					alreadyFound.add(blockToAdd);
					getConnectedRobots(blockToAdd, alreadyFound);
				}
			}
		}
		System.out.println(alreadyFound.size());
	}
}
