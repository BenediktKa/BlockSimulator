package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.block.BlockList;

/**
 * Responsible for managing the reconfiguration
 * > Defining the order in which targets should be filled
 * > Appointing matching robot and target block
 * > Initializing movement of robot to target position
 * Issues:
 * > ID's not fully implemented
 * > Possibly problems with target-blocks that are not on the floor, since the PathFinding is only 2D
 * @author Jurriaan
 *
 */
public class Reconfiguration
{
	private BlockList blockList;
	private ArrayList<RobotBlock> robot;
	private ArrayList<Block> target;
	private ArrayList<Block>[] sortedTargets;
	private ArrayList<Block> easySortedTargets;
	private Block targetOrigin;
	
	private boolean xIncreasing;
	private boolean zIncreasing;
	
	private PathFinder pathFinder;
	private final Move3 movement;
	private final SmartMovement reconfigurationMovement;

	private final boolean DEBUG = true;

	/**
	 * Constructor for reconfiguration algorithm Checks if the number of robot
	 * blocks == number of target blocks Not yet implemented: check if the ID's
	 * correspond
	 * 
	 * @param robot list with all robot blocks in the neighborhood (how do we
	 *            know where they are exactly)
	 * @param target list with all target blocks
	 * @param minTarget the target block in the corner, closest to the robot
	 * @param movement 
	 */
	public Reconfiguration(BlockList blockList, Block minTarget, Move3 movement, SmartMovement reconfigurationMovement)
	{
		

		this.blockList = blockList;
		this.robot = blockList.getRobotBlockList();
		this.target = blockList.getGoalList();
		targetOrigin = minTarget;
		this.movement = movement;
		this.reconfigurationMovement = reconfigurationMovement;

		if (robot.size() != target.size())
		{
			throw new IllegalArgumentException("The number of robot blocks is not equal to the number of target blocks!");
		}
		
		prepare();
		if (DEBUG)
		{
			System.out.println("//RECONFIG: STARTED");
		}
		// start(); //Take ID's into account
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
		//findTarget(); // Check if there is an Target Block with the same ID, not taken into account now
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
		Vector3 layer0Origin = new Vector3(targetOrigin.getPosition().x, 1, targetOrigin.getPosition().z);

		// Figure out what the dimensions of the target objects together are
		int targetWidth = 0;
		int targetLength = 0;
		int targetHeight = 0;

		for (int i = 0; i < target.size(); i++)
		{
			targetWidth = (int) Math.max(targetWidth, Math.abs(target.get(i).getPosition().x - layer0Origin.x + 1)); // +1:if there
			// was only one block, the abs distance would be 0, but should be 1
			targetLength = (int) Math.max(targetLength, Math.abs(target.get(i).getPosition().z - layer0Origin.z + 1));
			targetHeight = (int) Math.max(targetHeight, target.get(i).getPosition().y - 1); 
			// Robot blocks on the first level are not taken into account for height; so -1
		}

		// The size of these arraylists depend on the dimension of the target
		// region (which is seen as one whole cube covering the region
		int floorLevels = targetWidth + targetLength - 1;
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

		// Define origin for other layers: point opposite to layer0 origin
		Vector3 otherOrigin = new Vector3(layer0Origin.x + targetWidth - 1, 1, layer0Origin.z + targetLength - 1);
		// TODO: double check -1

		// Put target blocks in the right bucket
		for (int i = 0; i < target.size(); i++)
		{
			// Blocks on the floor go in the separate list
			if (target.get(i).getPosition().y == 1)
			{
				// the #bucket depends on the absolute distance to the origin
				// point (so: (x+1 & z)-->1, (x & z+1)-->1, (x+1 & z+1)-->2
				int bucket = (int) (Math.abs(target.get(i).getPosition().x - layer0Origin.x) + Math.abs(target.get(i).getPosition().z - layer0Origin.z));
				if (DEBUG)
				{
					System.out.println("// RECONFIG: " + target.get(i).getID() + ": " + bucket);
				}
				sortedTargets[bucket].add(target.get(i));
			}
			else
			{
				int bucket = (int) (Math.abs(otherOrigin.x - target.get(i).getPosition().x) + Math.abs(otherOrigin.y - target.get(i).getPosition().y) + Math.abs(otherOrigin.z - target.get(i).getPosition().z));
				if (DEBUG)
				{
					System.out.println("// RECONFIG: " + target.get(i).getID() + ": " + (bucket + floorLevels -1));
				}
				sortedTargets[bucket + floorLevels - 1].add(target.get(i));
			}
		}
		
		//Easy sorting targets
		easySortedTargets = new ArrayList<Block>();
		for(int i=0; i<sortedTargets.length; i++)
		{
			for(int j=0; j<sortedTargets[i].size(); j++)
			{
				easySortedTargets.add(sortedTargets[i].get(j));
			}
		}
	}

	private void findTarget()
	{
		for (int i = 0; i < robot.size(); i++)
		{
			try
			{
				double ID = robot.get(i).getID();
				for (int j = 0; j < target.size(); j++)
				{
					try
					{
						if (target.get(j).getID() == ID)
						{
							robot.get(i).setTarget(target.get(j));
						}
					} catch (NullPointerException e)
					{
					}
				}
				// At the end of the for-loop it may occur that the target is
				// still unspecified, then there wasn't a target block assigned
				// to it
			} catch (NullPointerException e)
			{
			} // specifiedTarget stays uninitialized, i.e. there is no target
				// specified for this robotBlock
		}
	}

	/**
	 * 1) Get last a targetBlock in the current level 2) Check if the
	 * targetBlock has an ID 2.1) In case not, check if the last RobotBlock in
	 * the chain is not determined to be at another position (method findTarget
	 * should have been run before) 2.2) In case it did, check if the last
	 * RobotBlock in the chain has this block as an target (method findTarget
	 * should have been run before) 3.1) IF the last Robot block in the chain
	 * meets the criteria above, THEN move it to the target position 3.2) IF
	 * NOT, THEN move it to the other chain 4) Do this WHILE not all target
	 * blocks of the same level have been filled up > this might not be the most
	 * efficient (since blocks are move between chains), however it is the best
	 * way to avoid dead-locks
	 */
	private void start()
	{
		// Get last robotblock from 'chain'
		// Check if it can be moved to one of the next targets (i.e. target ID
		// matches) --> Not done for now
		// IF so THEN move the block to that position
		// IF not THEN move the block to another chain

		int currentLevel;

		for (currentLevel = 0; currentLevel < sortedTargets.length; currentLevel++)
		{
			if (sortedTargets[currentLevel].size() != 0)
			{
				int cntr = 0;

				while (sortedTargets[currentLevel].size() != 0)
				{

					for (int j = 0; j < sortedTargets[currentLevel].size(); j++)
					{
						try
						{
							// The ID for this Target Position IS specified
							sortedTargets[currentLevel].get(j).getID(); 
							try
							{
								if (sortedTargets[currentLevel].get(j) == robot.get(0).getTarget()) // The
								// Robot Block that is moved into the position, has to have the same ID
								{
									// TODO: Move the robotBlock from the end of
									// the chain to its targetPosition, keep
									// track of indexes in Robot!
									sortedTargets[currentLevel].remove(j); // Remove in
									// order to prevent from filling up again
								}
							} catch (NullPointerException f)
							{
								// TODO: Put Robot Block aside, keep track of
								// indexes in Robot!
							}

						} 
						// The ID WAS NOT specified for the Target block, 
						// so if the robot block doesn't have an assigned target, 
						// it can move into it.
						catch (NullPointerException e) 
						{
							try
							{
								robot.get(0).getTarget(); // This should not be
															// there
								// TODO: Put Robot Block aside, keep track of
								// indexes in Robot!
							} 
							catch (NullPointerException g)
							{
								// TODO: Move the robotBlock from the end of the
								// chain to its targetPosition, keep track of
								// indexes in Robot!
								sortedTargets[currentLevel].remove(j); // Remove
								// Remove in order to prevent from filling up again
							}
						}
						if (DEBUG)
							cntr++;
						if (DEBUG && cntr > robot.size())
						{
							System.out.println("// RECONFIG: " + "All robot blocks have been tried, but no one was able to move to the target position since ID's didn't match");
						}
					}
				}
			}
		}
	}
	
	private void startEasy()
	{
		
		int cntr = 0;
		
		while(cntr<robot.size()) 
		{
			
			System.out.println("// RECONFIG: Cntr: "+cntr);
			// 1) Select last robot block to move, i.e. robot.get(0)????!!!!!?????!!!!!?????!!!!!?????!!!!
			final RobotBlock blockToMove = getFurthestRobot();

			
			// 2) Select the target position where it has to go to, there should not be a robot block on it
			Block targetBlock = easySortedTargets.get(cntr);
			cntr++; 
			
			// Check if there is not already a robot block on this position
			//TODO: Improve implementation
			for(int i=0; i<robot.size(); i++)
			{
				if(robot.get(i).getPosition().x==targetBlock.getPosition().x&&robot.get(i).getPosition().y==targetBlock.getPosition().y&&robot.get(i).getPosition().z==targetBlock.getPosition().z)
				{
					targetBlock = easySortedTargets.get(cntr);
					System.out.println("// RECONFIG: skipped");
					cntr++;
					i=0;
				}
			}
			// 3) Find path from position of robotBlock to target position
			// TODO: Check if it works with only 1 block
			final Block targetBlockForMove = targetBlock;
			
			//pathFinder.startPathFinder(blockToMove, targetBlock);
			
			// 4) Perform actual movement
			
			System.out.println("// RECONFIG: Start movement, block: "+blockToMove.getID()+ " to: "+targetOrigin.getPosition());
			
			// FIRST: Move the block to the target origin			
			
			/*
			Thread thread1 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					// TODO: Run & Debug this
					ArrayList<Vector3> fakePath = new ArrayList<Vector3>();
					fakePath.add(targetOrigin.getPosition());
					movement.startMove3(fakePath, targetOrigin);
				}
			});
			thread1.start();
			
			while(thread1.isAlive())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			
			// THIRD: Do Reconfiguration Part
			
			System.out.println("// RECONFIG: Start smartMovement, block: "+blockToMove.getID()+ " to: "+targetBlockForMove.getPosition());
			Thread thread2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					reconfigurationMovement.newSmartMove(blockToMove, targetBlockForMove.getPosition());
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
			
			blockToMove.setInFinalPosition(true);
			
			System.out.println("// RECONFIG: CNTR: "+cntr+" ROBOT SIZE: "+robot.size());
			// TODO: SECOND: Try to get the block out of the list for move3
			
			
		}
		
		System.out.println("// RECONFIG: End of While loop");
		
		//CLOSE MOVING MODE

	}

	private RobotBlock getFurthestRobot() {
		// TODO: Watch out that robot blocks that are already in place are not given
		xIncreasing = true;
		zIncreasing = true;
		
		RobotBlock furthestBlock = null;
		int biggestDistance = 0;
		Vector3 targetOrigin = this.targetOrigin.getPosition();
		
		for(RobotBlock block : robot)
		{
			Vector3 vector = block.getPosition();

			if(!block.isInFinalPosition()){
				System.out.println("true");
				int thisDistance = (int) (Math.abs(vector.x - targetOrigin.x) + Math.abs(vector.z - targetOrigin.z) + Math.abs(vector.y - targetOrigin.y));
				if(thisDistance > biggestDistance)
				{
					biggestDistance = thisDistance;
					furthestBlock = block;
					System.out.println("Found one");
				}
			}
			
			
		}
		
		System.out.println("// RECONFIG: Furthest robot block: "+furthestBlock.getID()+" "+furthestBlock.getPosition());
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
}
