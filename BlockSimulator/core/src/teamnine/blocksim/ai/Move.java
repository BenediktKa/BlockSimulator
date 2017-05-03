//to do:
//prioritize blocks who have the highest height
//add safe to everything
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
		
		decideMove();
	}
	//does all the prep work with finding and assigning distances
	public void decideMove()
	{	
		ArrayList<RobotBlock> orderToMove=new ArrayList<RobotBlock>();
		//finds distance from the block to the target assigns the value to the block 

		for(int j=0;j<path.size();j++)
		{
			float targetX=path.get(j).x;
			float targetZ=path.get(j).z;
			for(int i=0;i<robots.size();i++)
			{
				float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y;
				robots.get(i).setDistanceToPath(distanceToPath);
				orderToMove.add(robots.get(i));
			}
			ArrayList<RobotBlock> newOrderToMove=order(orderToMove);
			for(int i=0;i<newOrderToMove.size();i++)
				moving(newOrderToMove.get(i),path.get(j));
		}
			
		
		System.out.println("reaced");
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
		
		//System.out.println("moving startblock: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
		Vector3 bestMovement=new Vector3(0,0,0);
		boolean targetReached=false;
		
		//runs until target has been reached or no further movements are possible
		while(!targetReached)
		{	
			int bestDistance=-1;
			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			boolean safe =false;
			
			//checks if movement is possible
			for(int k=0;k<robots.size();k++)
			{
				
				if(b.getPosition().z==robots.get(k).getPosition().z&&robots.get(k).getPosition().y==b.getPosition().y+1&&b.getPosition().x==robots.get(k).getPosition().x)
				{
					safe=false;
					break;
				}
		
				if(b.getPosition().x+1==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z||b.getPosition().x-1==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z
				  ||b.getPosition().x==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z+1||b.getPosition().x==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z-1
				  ||b.getPosition().z==robots.get(k).getPosition().z&&robots.get(k).getPosition().y==b.getPosition().y-1&&b.getPosition().x==robots.get(k).getPosition().x)
				{
					safe= true;
				}
				
				
			}
			
			
			//checks which movements are possible
		//	System.out.println("orpos "+b.getOriginalPos());
			for(int i=0;i<robots.size();i++)
			{
			
				if(b.getOriginalPos().x!=b.getPosition().x+1&&robots.get(i).getPosition().x==b.getPosition().x+1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}					
				}
				if(b.getOriginalPos().z!=b.getPosition().z+1&&robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z+1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(b.getOriginalPos().x!=b.getPosition().x-1&&robots.get(i).getPosition().x==b.getPosition().x-1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(b.getOriginalPos().z!=b.getPosition().z-1&&robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z-1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
			}
			
			for(int i=0;i<possibleMovements.size();i++)
			{
				for(int j=0;j<robots.size();j++)
				{
					if(possibleMovements.get(i).x==robots.get(j).getPosition().x&&possibleMovements.get(i).y==robots.get(j).getPosition().y&&possibleMovements.get(i).z==robots.get(j).getPosition().z)
					{
						possibleMovements.remove(i);
						System.out.println("size "+possibleMovements.size()+" i "+i);
						if(possibleMovements.size()<=i)
							break;
					}
					if(possibleMovements.size()<=i)
						break;
				}
				if(possibleMovements.size()<=i)
					break;
			}
			for(int i=0;i<possibleMovements.size();i++)
			{
				//System.out.println("search");
				if(possibleMovements.get(i).x==b.getOriginalPos().x&&possibleMovements.get(i).y==b.getOriginalPos().y&&possibleMovements.get(i).z==b.getOriginalPos().z)
					{
					possibleMovements.remove(i);
					//System.out.println("last removed");
					}
				
			}
			//checks if there is a movement to be made. if not this part is skipped. if yes movement will be performed
			//System.out.println("possible movements "+possibleMovements.size());
			boolean none=true;
			boolean reallyReached=false;
			if(b.getPosition().x==v.x&&b.getPosition().z==v.z)
			{
				targetReached=true;
				reallyReached=true;
				System.out.println("yaaaay");
				break;
			}
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
					b.climb();
				}
				else if(bestMovement.x<b.getPosition().x)
				{
					none=false;
					System.out.println("move left");
					//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
					b.moveLeft();
				}
				else if(bestMovement.x>b.getPosition().x)
				{
					none=false;
					System.out.println("move right");
					//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
					b.moveRight();
				}
				else if(bestMovement.z<b.getPosition().z)
				{
					none=false;
					System.out.println("move back");
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
					b.moveBackwards();
				}
				else
				{
					none=false;
					System.out.println("move forward");
					b.moveForward();
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);					
				}
			
			}
			
			
			//System.out.println("lol"+"none "+none+" safe "+safe);
			if(none&&safe&&reallyReached==false)
			{
				if(v.x==b.getPosition().x+1&&v.z==b.getPosition().z&&v.y<=b.getPosition().y)
				{
					boolean right=true;
					for(int l =0;l<robots.size();l++)
					{
						if(robots.get(l).getPosition().x==b.getPosition().x+1&&robots.get(l).getPosition().z==b.getPosition().z&&robots.get(l).getPosition().y<=b.getPosition().y)
						{
							right=false;
						}
					}
					if(right)
					{
						System.out.println("right");
						bestMovement=v;
						bestDistance=1;
						b.moveRight();
					
						//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
					
				}
				if(v.x==b.getPosition().x-1&&v.z==b.getPosition().z&&v.y<=b.getPosition().y)
				{	
					boolean left=true;
					for(int l =0;l<robots.size();l++)
					{
						if(robots.get(l).getPosition().x==b.getPosition().x-11&&robots.get(l).getPosition().z==b.getPosition().z&&robots.get(l).getPosition().y<=b.getPosition().y)
						{
							left=false;
						}
					}
					if(left)
					{
						System.out.println("left");
						bestMovement=v;
						bestDistance=1;
						b.moveLeft();
						//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
				}
				if(v.z==b.getPosition().z+1&&v.x==b.getPosition().x&&v.y<=b.getPosition().y)
				{
					boolean forw=true;
					for(int l =0;l<robots.size();l++)
					{
						if(robots.get(l).getPosition().x==b.getPosition().x+1&&robots.get(l).getPosition().z==b.getPosition().z&&robots.get(l).getPosition().y<=b.getPosition().y)
						{
							forw=false;
						}
					}
					if(forw)
					{
						System.out.println("forward");
						bestMovement=v;
						bestDistance=1;
						b.moveForward();
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);
						break;
					}
				}
				if(v.z==b.getPosition().z-1&&v.x==b.getPosition().x&&v.y<=b.getPosition().y)
				{
					boolean back=true;
					for(int l =0;l<robots.size();l++)
					{
						if(robots.get(l).getPosition().x==b.getPosition().x+1&&robots.get(l).getPosition().z==b.getPosition().z&&robots.get(l).getPosition().y<=b.getPosition().y)
						{
							back=false;
						}
					}
					if(back)
					{
						System.out.println("back");
						bestMovement=v;
						bestDistance=1;
						b.moveBackwards();
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
						break;
					}
					
				}
			}
			//System.out.println("moving startblock2: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
			none=true;
			
			
			while(b.getMoving())
			{
				System.out.print(" ");
			}
			
			
			
			
		}
	}
}
