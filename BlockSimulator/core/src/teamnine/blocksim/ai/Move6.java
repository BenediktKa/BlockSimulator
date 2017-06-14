package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class Move6
{
	private ArrayList<Vector3> path;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private ArrayList<Block> floor;
	private int timestep = 0;
	private boolean targetReached = false;
	private boolean intermediateTargetReached = true;
	private Vector3 intermediateTarget = null;
	private int iteration = 0;
	private int failedMoves = 0;
	private boolean done=false;

	public Move6(ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, ArrayList<Block> floor)
	{
		this.obstacles = obstacles;
		this.robots = new ArrayList<RobotBlock>(robots);
		this.floor = floor;
		/*
		 * for (int i = path.size() - 1; i > 0; i--)
		 * decideMove(this.path.get(i));
		 */
	}

	public void startMove3(ArrayList<Vector3> path, Block mt)
	{
		this.path = path;
		System.out.println("poep " + path.size());
		for (int i = 0; i < path.size(); i++)
			System.out.println(this.path.get(i));
		while (!targetReached)
		{
			 System.out.println("reached while failed moves and interation"+failedMoves+" "+iteration);
			iteration++;
			if (intermediateTargetReached)
			{
				System.out.println("reached intermediate target true");
				intermediateTarget = null;
				decideMove(this.path.get(0));
				if (iteration == robots.size())
				{
					// System.out.println("wall check");

					if (failedMoves == (robots.size() * iteration))
					{
						System.out.println("entered");
						RobotBlock closest = null;
						for (int z = 0; z < robots.size(); z++)
						{
							if (closest == null)
							{
								closest = robots.get(z);
							}
							else
							{
								if (closest.getDistanceToPath() > robots.get(z).getDistanceToPath())
								{
									closest = robots.get(z);
								}
							}
						}
						Vector3 pass = findOpening(closest.getPosition());
						intermediateTarget = pass;
						System.out.println(pass);
						for (int z = 0; z < robots.size(); z++)
						{
							robots.get(z).addUnpassableVector(pass);
							robots.get(z).addNumOfpass();
						}
						intermediateTargetReached = false;
					}
					iteration = 0;
					failedMoves = 0;
				}
				// System.out.println("past wall check");
			}
			else
			{
			//	System.out.println("ughhh + intermediate target" + intermediateTarget);
				if(!done){
					
				for (int k = 0; k < robots.size(); k++) 
				{
					if (robots.get(k).getPosition().x + 1 == intermediateTarget.x&& robots.get(k).getPosition().z == intermediateTarget.z) {
						System.out.println("close to target 1");
						boolean bigger = true;
						int attempt = 1;
						int result = 0;
						int currentBest = 0;
						while (bigger) {
							int total = 0;
							for (int j = attempt; j > 0; j--) {
								total = total + j;
							}
							if (total > robots.size()) {
								currentBest = attempt;
								bigger = false;
							} else {
								result = currentBest;
								attempt++;
							}
						}
						
						intermediateTarget = new Vector3(intermediateTarget.x + result, 1, intermediateTarget.z);
						done=true;
					} 
					else if (robots.get(k).getPosition().x - 1 == intermediateTarget.x&& robots.get(k).getPosition().z == intermediateTarget.z) {
						System.out.println("close to target 2");
						boolean bigger = true;
						int attempt = 1;
						int result = 0;
						int currentBest = 0;
						while (bigger) {
							int total = 0;
							for (int j = attempt; j > 0; j--) {
								total = total + j;
							}
							if (total > robots.size()) {
								currentBest = attempt;
								bigger = false;
							} else {
								result = currentBest;
								attempt++;
							}
						}
						
						intermediateTarget = new Vector3(intermediateTarget.x - result, 1, intermediateTarget.z);
						done=true;
					} 
					else if (robots.get(k).getPosition().x == intermediateTarget.x&& robots.get(k).getPosition().z + 1 == intermediateTarget.z) {
						System.out.println("close to target 3 " + robots.get(k).getPosition() + " tar " + intermediateTarget);
						boolean bigger = true;
						int attempt = 1;
						int result = 0;
						int currentBest = 0;
						while (bigger) {
							int total = 0;
							for (int j = attempt; j > 0; j--) {
								total = total + j;
							}
							if (total > robots.size()) {
								currentBest = attempt;
								bigger = false;
							} else {
								result = currentBest;
								attempt++;
							}
						}
						
						System.out.println("result " + result + " inter " + intermediateTarget);
						intermediateTarget = new Vector3(intermediateTarget.x, 1, intermediateTarget.z + result);
						done=true;
						//System.out.println("result " + result + " new inter " + intermediateTarget);
					} 
					else if (robots.get(k).getPosition().x == intermediateTarget.x&& robots.get(k).getPosition().z - 1 == intermediateTarget.z) {
						System.out.println("close to target 4");
						boolean bigger = true;
						int attempt = 1;
						int result = 0;
						int currentBest = 0;
						
						while (bigger) {
							System.out.println("statrt");
							int total = 0;
							for (int j = attempt; j > 0; j--) {
								total = total + j;
								System.out.println("total "+total+" att "+attempt+" cur "+currentBest);
								
							}
							if (total > robots.size()) {
								System.out.println("exit");
								result = currentBest;
								bigger = false;
							} else {
								System.out.println("total set currentBest");
								currentBest = attempt;
								attempt++;
							}
						}
						
						System.out.println("result "+result);
						intermediateTarget = new Vector3(intermediateTarget.x, 1, intermediateTarget.z - result);
						done=true;
					}
				}}

				decideMove(intermediateTarget);
				

				if (iteration == robots.size())
				{
					iteration = 0;
					failedMoves = 0;
					
				}			
			}
		}

		// decideMove(new Vector3(mt.getPosition().x, mt.getPosition().y,
		// mt.getPosition().z));
		System.out.println("Timestep: " + timestep);
		Thread.currentThread().interrupt();
	}

	public Vector3 findOpening(Vector3 start)
	{
		Vector3 lastVector = null;
		Vector3 currentVector = start;

		if (lastVector == null)
		{
			for (int i = 0; i < obstacles.size(); i++)
			{
				if (start.x + 1 == obstacles.get(i).getPosition().x && start.z == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
				}
				else if (start.x - 1 == obstacles.get(i).getPosition().x && start.z == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
				}
				else if (start.x == obstacles.get(i).getPosition().x && start.z + 1 == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
				}
				else if (start.x == obstacles.get(i).getPosition().x && start.z - 1 == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
				}
			}
		}
		System.out.println("current " + currentVector);
		boolean found = false;
		
		while (!found)
		{
			found = true;
			for (int i = 0; i < obstacles.size(); i++)
			{
				if (lastVector.x == obstacles.get(i).getPosition().x && lastVector.z == obstacles.get(i).getPosition().z)
				{
					continue;
				} 

			
				if (currentVector.x + 1 == obstacles.get(i).getPosition().x && currentVector.z == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next1 " + obstacles.get(i).getPosition());
					found = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					break;

				}
				else if (currentVector.x - 1 == obstacles.get(i).getPosition().x && currentVector.z == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next2 " + obstacles.get(i).getPosition());
					found = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					break;

				}
				else if (currentVector.x == obstacles.get(i).getPosition().x && currentVector.z + 1 == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next3 " + obstacles.get(i).getPosition());
					found = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					break;

				}
				else if (currentVector.x == obstacles.get(i).getPosition().x && currentVector.z - 1 == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next4 " + obstacles.get(i).getPosition());
					found = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					break;

				}
			}
			System.out.println("current2 " + currentVector);
		}
		if (lastVector.x < currentVector.x)
		{
			System.out.println("x<x");
			return new Vector3(currentVector.x + 1, 1, currentVector.z);
		}
		else if (lastVector.x > currentVector.x)
		{
			System.out.println("x>x");
			return new Vector3(currentVector.x - 1, 1, currentVector.z);
		}
		else if (lastVector.z < currentVector.z)
		{
			System.out.println("z<z");
			return new Vector3(currentVector.x, 1, currentVector.z + 1);
		}
		else
		{
			System.out.println("z>z");
			return new Vector3(currentVector.x, 1, currentVector.z - 1);
		}
		
	}

	public void decideMove(Vector3 v)
	{

		ArrayList<RobotBlock> orderToMove = new ArrayList<RobotBlock>();
		
		
		for (int i = 0; i < robots.size(); i++)
		{
			orderToMove.add(robots.get(i));
		}
		System.out.println("decide "+orderToMove.size());
		RobotBlock end = new RobotBlock(v, null);
		robots.add(end);
		for (int i = 0; i < robots.size(); i++)
		{
			for (int j = 0; j < robots.size(); j++)
			{
				if (i != j)
				{
					if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y - 1 == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y + 1 == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x + 1 == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x - 1 == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z + 1 == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z - 1 == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
				}
			}
		}

		for (int i = 0; i < robots.size(); i++)
		{
			setDistances(robots.get(i), end);
			for (int j = 0; j < robots.size(); j++)
			{
				robots.get(j).setVisited(false);
				robots.get(j).setCounter(0);
			}
		}

		for (int i = 0; i < robots.size(); i++)
		{
			robots.get(i).setVisited(false);
			robots.get(i).setCounter(0);
		}
		robots.remove(robots.size() - 1);
	
		ArrayList<RobotBlock> newOrderToMove = order(orderToMove);
		for (int i = 0; i < newOrderToMove.size(); i++)
		{
			moving(newOrderToMove.get(i), v);
		}
	}

	public void setDistances(RobotBlock b, RobotBlock end)
	{
		
		if (b.getPosition().x == end.getPosition().x && b.getPosition().z == end.getPosition().z)
		{
			System.out.println("target found"+b.getPosition()+" "+end.getPosition());
			b.setDistanceToPath(0);
		
		}
		else
		{
			Queue<RobotBlock> q = new LinkedList<RobotBlock>();
			q.add(b);
			while (q.peek() != null)
			{
				RobotBlock w = q.poll();

				if (w.getVisited() == false)
				{
					w.setVisited(true);
					ArrayList<RobotBlock> connections = w.getConnections();

					for (int i = 0; i < connections.size(); i++)
					{
						if (connections.get(i).getVisited() == false)
						{

							q.add(connections.get(i));
							if (connections.get(i).getCounter() > (w.getCounter() + 1) || connections.get(i).getCounter() == 0)
								connections.get(i).setCounter(w.getCounter() + 1);
						}
					}
				}
			}
			if (end.getCounter() != 0 && end.getCounter() > (int) ((Math.abs(b.getPosition().x - end.getPosition().x) + Math.abs(b.getPosition().z - end.getPosition().z) + b.getPosition().y) - 1))
				{b.setDistanceToPath(end.getCounter());System.out.println("linked dis");}
			else
				{b.setDistanceToPath((int) ((Math.abs(b.getPosition().x - end.getPosition().x) + Math.abs(b.getPosition().z - end.getPosition().z) + b.getPosition().y) - 1));
				System.out.println("abs dis");
				}System.out.println("distance "+b.getDistanceToPath());
		}
	}

	public ArrayList<RobotBlock> order(ArrayList<RobotBlock> otm)
	{
		System.out.println("order");
		ArrayList<RobotBlock> sorted = new ArrayList<RobotBlock>();
		float maxDistance = 0;
		
		for (int i = 0; i < otm.size(); i++)
		{
			if (otm.get(i).getDistanceToPath() > maxDistance)
			{
				maxDistance = otm.get(i).getDistanceToPath();
			}
		}
		System.out.println("order maxd"+maxDistance);
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		buckets.add(new Bucket());
		for (int i = 0; i < maxDistance; i++)
		{
			buckets.add(new Bucket());
		}

		for (int i = 0; i < otm.size(); i++)
		{
			int d = (int) otm.get(i).getDistanceToPath();
			buckets.get(d).addBlock(otm.get(i));
		}

		for (int i = buckets.size() - 1; i > 0; i--)
		{
			sorted.addAll(buckets.get(i).getBloks());
		}
		System.out.println(sorted.size());
		return sorted;
	}

	public void moving(RobotBlock b, Vector3 v)
	{
		// targetReached = false;
		
		checkIfCanMove(b);
		System.out.println("b " + b.getPosition() + " t " + v);
		ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
		for (int i = 0; i < robots.size(); i++)
		{
			if (robots.get(i).getPosition().x == b.getPosition().x + 1 && robots.get(i).getPosition().z == b.getPosition().z)
			{
				if (robots.get(i).getPosition().y <= b.getPosition().y)
				{
					possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
				}
			}
			if (robots.get(i).getPosition().x == b.getPosition().x && robots.get(i).getPosition().z == b.getPosition().z + 1)
			{
				if (robots.get(i).getPosition().y <= b.getPosition().y)
				{
					possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
				}
			}
			if (robots.get(i).getPosition().x == b.getPosition().x - 1 && robots.get(i).getPosition().z == b.getPosition().z)
			{
				if (robots.get(i).getPosition().y <= b.getPosition().y)
				{
					possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
				}
			}
			if (robots.get(i).getPosition().x == b.getPosition().x && robots.get(i).getPosition().z == b.getPosition().z - 1)
			{
				if (robots.get(i).getPosition().y <= b.getPosition().y)
				{
					possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
				}
			}
		}
		for (int i = 0; i < floor.size(); i++)
		{
			if (floor.get(i).getPosition().x == b.getPosition().x + 1 && floor.get(i).getPosition().z == b.getPosition().z)
			{
				possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
			}
			if (floor.get(i).getPosition().x == b.getPosition().x && floor.get(i).getPosition().z == b.getPosition().z + 1)
			{
				possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
			}
			if (floor.get(i).getPosition().x == b.getPosition().x - 1 && floor.get(i).getPosition().z == b.getPosition().z)
			{
				possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
			}
			if (floor.get(i).getPosition().x == b.getPosition().x && floor.get(i).getPosition().z == b.getPosition().z - 1)
			{
				possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
			}
		}
		removeRobots(possibleMovements);
		removeObstacles(possibleMovements);
		removeImpossibleMovements(possibleMovements, b);
		removeOrPos(possibleMovements, b, v);
		//removePass(possibleMovements, b);
		boolean found=false;
		for(int i=0;i<robots.size();i++)
		{
			if(robots.get(i).getPosition().x == v.x && robots.get(i).getPosition().z == v.z)
			{
				intermediateTargetReached = true;
				if (path.get(0).x == robots.get(i).getPosition().x && path.get(0).z == robots.get(i).getPosition().z)
				{
					targetReached = true;
					System.out.println("done!!!");
				}
					found=true;
				System.out.println("targetReachedxgnf " + targetReached);
				for(int y=0;y<robots.size();y++)
				{
					robots.get(i).setOriginalPos(new Vector3(-1,-1,-1));
				}
			}
		}
		
		if (possibleMovements.size() == 0)
		{
			System.out.println("no moves");
			failedMoves++;
		}
		else
		{
			Vector3 bestMovement = null;
			int bestDistance = -1;
			if (possibleMovements.size() != 0)
			{
				if (possibleMovements.size() == 1)
				{
					bestMovement = possibleMovements.get(0);
				}
				for (int i = 0; i < possibleMovements.size(); i++)
				{
					if (bestDistance == -1)
					{
						bestMovement = possibleMovements.get(i);
						bestDistance = (int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z));
					}
					else
					{
						if (bestDistance > ((int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z))))
						{
							bestMovement = possibleMovements.get(i);
							bestDistance = (int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z));
						}
					}
				}
			}
			if (bestMovement.y > b.getPosition().y)
			{
				for (int p = 0; p < b.getUnpassableVectors().size(); p++)
				{
					if (bestMovement.x == b.getUnpassableVectors().get(p).x)
					{
						b.getNumOfPass().set(p, Integer.valueOf(1));
					}
				}
				boolean possible = true;
				for (int i = 0; i < robots.size(); i++)
					if (b.getPosition().x == robots.get(i).getPosition().x && b.getPosition().z == robots.get(i).getPosition().z && b.getPosition().y + 1 == robots.get(i).getPosition().y)
					{
						possible = false;
					}
				if (possible)
				{
					System.out.println("climb");
					timestep++;
					b.climb();
					checkIfCanMove(b);
				}
				else
				{

				}

			}
			if (bestMovement.x < b.getPosition().x)
			{
				System.out.println("move left");
				timestep++;
				b.moveLeft();
			}
			else if (bestMovement.x > b.getPosition().x)
			{
				System.out.println("move right");
				timestep++;
				b.moveRight();
			}
			else if (bestMovement.z < b.getPosition().z)
			{
				System.out.println("move back");
				timestep++;
				b.moveBackwards();
			}
			else
			{
				System.out.println("move forward");
				timestep++;
				b.moveForward();
			}

		}

		checkIfCanMove(b);
	}

	public void removePass(ArrayList<Vector3> possibleMovements, RobotBlock b)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < possibleMovements.size(); i++)
		{
			for (int j = 0; j < b.getUnpassableVectors().size(); j++)
			{
				if (possibleMovements.get(i).x == b.getUnpassableVectors().get(j).x && possibleMovements.get(i).z == b.getUnpassableVectors().get(j).z)
				{
					if (b.getNumOfPass().get(j) > 1)
					{
						System.out.println("remove pass "+b.getPosition());
						toRemove.add(possibleMovements.get(i));
					}
				}
			}
		}
		possibleMovements.removeAll(toRemove);
	}

	public void removeOrPos(ArrayList<Vector3> possibleMovements, RobotBlock b, Vector3 v)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < possibleMovements.size(); i++)
		{
			if (possibleMovements.get(i).equals(b.getOriginalPos()))
			{
				toRemove.add(possibleMovements.get(i));
			}
			else if (possibleMovements.size() > 1 && Math.abs(b.getPosition().x - v.x) + Math.abs(b.getPosition().z - v.z) < Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z))
			{
				toRemove.add(possibleMovements.get(i));
			}
		}
		possibleMovements.removeAll(toRemove);
	}

	public void removeRobots(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < pm.size(); i++)
		{
			for (int j = 0; j < robots.size(); j++)
			{
				if (pm.get(i).equals(robots.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
			}
		}
		pm.removeAll(toRemove);
	}

	public void removeObstacles(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < pm.size(); i++)
		{
			for (int j = 0; j < obstacles.size(); j++)
			{
				if (pm.get(i).equals(obstacles.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
			}
		}
		pm.removeAll(toRemove);
	}

	public void removeImpossibleMovements(ArrayList<Vector3> pm, RobotBlock b)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		boolean locked = false;
		for (int k = 0; k < robots.size(); k++)
		{
			if (b.getPosition().x == robots.get(k).getPosition().x && b.getPosition().z == robots.get(k).getPosition().z && b.getPosition().y + 1 == robots.get(k).getPosition().y)
			{
				pm.clear();
				locked = true;
			}
		}
		if (!locked)
		{
			for (int i = 0; i < pm.size(); i++)
			{

				if (pm.get(i).x < b.getPosition().x)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}

						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}

						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).x > b.getPosition().x)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).z < b.getPosition().z)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).z > b.getPosition().z)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
							{
								remove = false;
								break;
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
							{
								remove = false;
								break;
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}
					if (remove)
						toRemove.add(pm.get(i));
				}
			}
			pm.removeAll(toRemove);
		}
	}

	public int getTimestep()
	{
		return timestep;
	}

	public void checkIfCanMove(RobotBlock b)
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
				Thread.sleep(250);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
