//to do:
//prioritize blocks who have the highest height
//add safe to everything
package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class Move {
	private ArrayList<Vector3> path;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private int pauseTime;
	
	public Move(ArrayList<Vector3> path, ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, Block mt)
	{
		this.obstacles=obstacles;
		this.path=path;
		this.robots= new ArrayList<RobotBlock>(robots);
		pauseTime=350*robots.size();
		for(int i=path.size()-1;i>0;i--)
			decideMove(this.path.get(i));
		
		decideMove(new Vector3(mt.getPosition().x,mt.getPosition().y,mt.getPosition().z));
		
		Thread.currentThread().interrupt();
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
			System.out.println("block: "+newOrderToMove.get(i)+"x; "+newOrderToMove.get(i).getPosition().x+" z: "+newOrderToMove.get(i).getPosition().z+" height: "+newOrderToMove.get(i).getPosition().y+" dis "+newOrderToMove.get(i).getDistanceToPath()+" target: "+v);
		}
			System.out.println();
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
		
		//System.out.println("moving startblock: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
		Vector3 bestMovement=new Vector3(0,0,0);
		boolean targetReached=false;
		
		//runs until target has been reached or no further movements are possible
		while(!targetReached)
		{
			while(b.getMoving())
			{
				try 
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			int bestDistance=-1;
			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			boolean safe =false;
			boolean none=true;

			//checks which movements are possible
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
			//System.out.println("lol"+"none "+none+" safe "+safe);
			if(safe)
			{
				if(v.x==b.getPosition().x+1&&v.z==b.getPosition().z)
				{
					boolean right=true;
					for(int l =0;l<robots.size();l++)
					{
						//System.out.println(robots.get(l).getPosition()+" ,  "+b.getPosition());
						if(b.getPosition().x+1==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z==robots.get(l).getPosition().z)
						{	System.out.println("failed");
							right=false;
						}
					}
					if(right)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x+1==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								right=false;
							}
						}
					}
					if(right)
					{
						System.out.println("right"+" BLOCK: "+b);
						bestMovement=v;
						bestDistance=1;
						b.moveRight();
						none=false;
						//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
					
				}
				if(v.x==b.getPosition().x-1&&v.z==b.getPosition().z)
				{	
					boolean left=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x-1==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z==robots.get(l).getPosition().z)
						{System.out.println("failed");
							left=false;
						}
					}
					if(left)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x-1==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								left=false;
							}
						}
					}
					if(left)
					{
						System.out.println("left"+" BLOCK: "+b);
						bestMovement=v;
						bestDistance=1;
						b.moveLeft();
						none=false;
						//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
				}
				if(v.z==b.getPosition().z+1&&v.x==b.getPosition().x)
				{
					boolean forw=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z+1==robots.get(l).getPosition().z)
						{System.out.println("failed");
							forw=false;
						}
					}
					if(forw)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z+1==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								forw=false;
							}
						}
					}
					if(forw)
					{
						System.out.println("forward"+" BLOCK: "+b);
						bestMovement=v;
						bestDistance=1;
						b.moveForward();
						none=false;
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);
						break;
					}
				}
				if(v.z==b.getPosition().z-1&&v.x==b.getPosition().x)
				{
					boolean back=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z-1==robots.get(l).getPosition().z)
						{System.out.println("failed");
							back=false;
						}
					}
					if(back)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z-1==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								back=false;
							}
						}
					}
					if(back)
					{
						System.out.println("back"+" BLOCK: "+b);
						bestMovement=v;
						bestDistance=1;
						b.moveBackwards();
						none=false;
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
						break;
					}
					
				}
			}
			boolean floating =true;
			if(!safe)
			{
				for(int p=0;p<obstacles.size();p++)
				{
					if(b.getPosition().x==obstacles.get(p).getPosition().x&&b.getPosition().z==obstacles.get(p).getPosition().z&&b.getPosition().y-1==obstacles.get(p).getPosition().y)
					{
						floating =false;
						break;
					}
				}
			}
			/////////////////////////////////////////////////////////////
			if(none&&floating){
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
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeOrPos(possibleMovements,b,v);
			
			System.out.println("pms2 "+possibleMovements.size());
				
			if(b.getPosition().x==v.x&&b.getPosition().z==v.z)
			{
				targetReached=true;
				
			//	System.out.println("yaaaay");
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
				System.out.println("bm "+bestMovement);
				if(bestMovement.y>b.getPosition().y)
				{
					
					boolean possible=true;
					for(int i=0;i<robots.size();i++)
						if(b.getPosition().x==robots.get(i).getPosition().x&&b.getPosition().z==robots.get(i).getPosition().z&&b.getPosition().y+1==robots.get(i).getPosition().y)
						{
							possible=false;
						}
					if(possible)
					{
					
					System.out.println("climb"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x,b.getPosition().y+1,b.getPosition().z);
					b.climb();
					}
					else
					{
						targetReached=true;
					}
					
				}
				else if(bestMovement.x<b.getPosition().x)
				{
					
					System.out.println("move left"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
					b.moveLeft();
				}
				else if(bestMovement.x>b.getPosition().x)
				{
					
					System.out.println("move right"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
					b.moveRight();
				}
				else if(bestMovement.z<b.getPosition().z)
				{
					
					System.out.println("move back"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
					b.moveBackwards();
				}
				else
				{
					
					System.out.println("move forward"+" BLOCK: "+b);
					b.moveForward();
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);					
				}
			
			}
			}
			
			none=true;
			
			
		}
		while(b.getMoving())
		{
			try 
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("next block");
	}
	
	public void removeRobots(ArrayList<Vector3> pm)
	{
		for(int i=0;i<pm.size();i++)
		{
			for(int j=0;j<robots.size();j++)
			{
				if(pm.get(i).x==robots.get(j).getPosition().x&&pm.get(i).y==robots.get(j).getPosition().y&&pm.get(i).z==robots.get(j).getPosition().z)
				{
			
					pm.remove(i);
					//System.out.println("size "+possibleMovements.size()+" i "+i);
					if(pm.size()<=i)
						break;
				}
				if(pm.size()<=i)
					break;
			}
			if(pm.size()<=i)
				break;
		}
	}
	public void removeObstacles(ArrayList<Vector3> pm)
	{
		for(int j=0;j<pm.size();j++)
		{
			for(int i =0;i<obstacles.size();i++)
			{
				if(pm.get(j).x==obstacles.get(i).getPosition().x&&pm.get(j).y==obstacles.get(i).getPosition().y&&pm.get(j).z==obstacles.get(i).getPosition().z)
				{
					
					pm.remove(j);
					//System.out.println("size "+possibleMovements.size()+" i "+i);
					if(pm.size()<=j)
						break;
				}
				if(pm.size()<=j)
					break;
			}
			if(pm.size()<=j)
				break;
		}
	}
	public void removeOrPos(ArrayList<Vector3> possibleMovements, RobotBlock b, Vector3 v)
	{
		for(int i=0;i<possibleMovements.size();i++)
		{
			//System.out.println("search");
			if(possibleMovements.get(i).x==b.getOriginalPos().x&&possibleMovements.get(i).y==b.getOriginalPos().y&&possibleMovements.get(i).z==b.getOriginalPos().z)
			{
				possibleMovements.remove(i);
				//System.out.println("last removed");
			}
			 int newDistance=(int) (Math.abs(v.x-possibleMovements.get(i).x)+Math.abs(v.z-possibleMovements.get(i).z)+possibleMovements.get(i).y);
			if(b.getDistanceToPath()<newDistance)
			{
				possibleMovements.remove(i);
			}
			
		}
	}
}