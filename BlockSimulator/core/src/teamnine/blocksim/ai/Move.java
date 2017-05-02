package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.RobotBlock;

public class Move {
	private ArrayList<Vector3> path;
	private ArrayList<RobotBlock> robots;
	public Move(ArrayList<Vector3> path, ArrayList<RobotBlock> robots)
	{
		this.path=path;
		this.robots= new ArrayList<RobotBlock>(robots);
		for(int i=path.size()-1;i>0;i--)
			decideMove(this.path.get(i));
	}
	//does all the prep work with finding and assigning distances
	public void decideMove(Vector3 v)
	{	
		
		ArrayList<RobotBlock> orderToMove=new ArrayList<RobotBlock>();
		float targetX=v.x;
		
		float targetZ=v.z;
		//finds distance from the block to the target assigns the value to the block 
		
		for(int i=0;i<robots.size();i++)
		{
			float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y;
			robots.get(i).setDistanceToPath(distanceToPath);
			orderToMove.add(robots.get(i));
		}
		ArrayList<RobotBlock> newOrderToMove=order(orderToMove);
		for(int i=0;i<newOrderToMove.size();i++)
		{
			
			moving(newOrderToMove.get(i),v);
		}
		
	}
	//orders the block based on their distance to the target using bucket sort. largest distance first.
	public ArrayList<RobotBlock> order(ArrayList<RobotBlock> otm)
	{	
		ArrayList<RobotBlock> sorted = new ArrayList<RobotBlock>();
		float maxDistance=0;
		for(int i=0; i<otm.size();i++)
		{
			if(otm.get(i).getDistanceToPath()>maxDistance)
			{
				maxDistance=otm.get(i).getDistanceToPath();
			}
		}
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		buckets.add(new Bucket());
		for(int i=0;i<maxDistance;i++)
		{
			buckets.add(new Bucket());
		}
		
		for(int i=0; i<otm.size();i++)
		{
			int d =(int)otm.get(i).getDistanceToPath();
			buckets.get(d).addBlock(otm.get(i));
		}
		
		for(int i=buckets.size()-1;i>0;i--)
		{
			sorted.addAll(buckets.get(i).getBloks());
		}
		
		return sorted;
	}
	//once the order has been made, the movements will go to try and place the block as close to the first step of 
	//the path as possible all the way to the target
	public void moving(RobotBlock b, Vector3 v)
	{
		
		System.out.println("moving startblock: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
		Vector3 bestMovement=new Vector3(0,0,0);
		boolean targetReached=false;
		Vector3 lastPosition = new Vector3(0,0,0);
		//runs until target has been reached or no further movements are possible
		while(!targetReached)
		{	
			int bestDistance=-1;
			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			boolean safe =false;
			//checks if movement is possible
			for(int k=0;k<robots.size();k++)
			{
				if(b.getPosition().x==robots.get(k).getPosition().x&&b.getPosition().z==robots.get(k).getPosition().z&&b.getPosition().y==robots.get(k).getPosition().y-1)
				{
					System.out.println("safe dammit");
				}
				if(b.getPosition().x+1==robots.get(k).getPosition().x&&v.y==b.getPosition().y||b.getPosition().z+1==robots.get(k).getPosition().z&&v.y==b.getPosition().y||
				   b.getPosition().x==robots.get(k).getPosition().x&&b.getPosition().z==robots.get(k).getPosition().z&&b.getPosition().y==robots.get(k).getPosition().y-1||b.getPosition().x-1==robots.get(k).getPosition().x&&v.y==b.getPosition().y ||
				   b.getPosition().z-1==robots.get(k).getPosition().z&&v.y==b.getPosition().y)
				{
					safe= true;
					break;
				}
			}
			
			//checks which movements are possible
			for(int i=0;i<robots.size();i++)
			{
			
				if(lastPosition.x!=b.getPosition().x+1&&robots.get(i).getPosition().x==b.getPosition().x+1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}					
				}
				if(lastPosition.z!=b.getPosition().z+1&&robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z+1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(lastPosition.x!=b.getPosition().x-1&&robots.get(i).getPosition().x==b.getPosition().x-1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(lastPosition.z!=b.getPosition().z-1&&robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z-1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
			}
			//checks if there is a movement to be made. if not this part is skipped. if yes movement will be performed
			System.out.println("possible movements "+possibleMovements.size());
			boolean none=true;
			if(possibleMovements.size()==0)
			{
				targetReached=true;
			}
			else
			{
				for(int j=0;j<possibleMovements.size();j++)
				{
					if(bestDistance==-1)
					{
						bestMovement=possibleMovements.get(0);
						bestDistance=(int)(Math.abs(possibleMovements.get(j).x-v.x)+Math.abs(possibleMovements.get(j).z-v.z)+possibleMovements.get(j).y);
					}
					else
					{
						if(Math.abs(possibleMovements.get(j).x-v.x)+Math.abs(possibleMovements.get(j).z-v.z)+possibleMovements.get(j).y<bestDistance)
						{
							bestMovement=possibleMovements.get(j);
							bestDistance=(int)(Math.abs(possibleMovements.get(j).x-v.x)+Math.abs(possibleMovements.get(j).z-v.z)+possibleMovements.get(j).y);
						}
					}
				}
				
				if(bestMovement.y>b.getPosition().y)
				{
					none=false;
					System.out.println("climb");
					//b.setPosition(b.getPosition().x,b.getPosition().y+1,b.getPosition().z);
				}
				else if(bestMovement.x<b.getPosition().x)
				{
					none=false;
					System.out.println("move left");
					//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
				}
				else if(bestMovement.x>b.getPosition().x)
				{
					none=false;
					System.out.println("move right");
					//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
				}
				else if(bestMovement.z<b.getPosition().z)
				{
					none=false;
					System.out.println("move back");
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
				}
				else
				{
					none=false;
					System.out.println("move forward");

					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);					
				}
			
				
			}
			System.out.println("lol"+"none "+none+" safe "+safe);
			if(v.x==b.getPosition().x+1&&v.z==b.getPosition().z&&v.y<=b.getPosition().y)
			{
				
				if(none&&safe)
				{
					System.out.println("right");
					bestMovement=v;
					bestDistance=1;
					b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
					break;
				}
				
			}
			if(v.x==b.getPosition().x-1&&v.z==b.getPosition().z&&v.y<=b.getPosition().y)
			{
				if(none&&safe)
				{
					System.out.println("left");
					bestMovement=v;
					bestDistance=1;
					b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
					break;
				}
			}
			if(v.z==b.getPosition().z+1&&v.x==b.getPosition().x&&v.y<=b.getPosition().y)
			{
				if(none&&safe)
				{
					System.out.println("forward");
					bestMovement=v;
					bestDistance=1;
					b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);
					break;
				}
			}
			if(v.z==b.getPosition().z-1&&v.x==b.getPosition().x&&v.y<=b.getPosition().y)
			{
				if(none&&safe)
				{
					System.out.println("back");
					bestMovement=v;
					bestDistance=1;
					b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
					break;
				}
			}
			none=true;
			System.out.println("moving startblock2: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
			lastPosition=v;
			
		}
	}
}
