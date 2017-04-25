package teamnine.blocksim;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public class Move {
	private ArrayList<Vector3> path;
	private ArrayList<Block> robots;
	public Move(ArrayList<Vector3> path, ArrayList<Block> robots)
	{
		this.path=path;
		this.robots= new ArrayList<Block>(robots);
		for(int i=0;i<path.size();i++)
			decideMove(this.path.get(i));
	}
	public void decideMove(Vector3 v)
	{
		ArrayList<Block> orderToMove=new ArrayList<Block>();
		float targetX=v.x;
		
		float targetZ=v.z;
		
		for(int i=0;i<robots.size();i++)
		{
			float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y;
			robots.get(i).setDistanceToPath(distanceToPath);
			orderToMove.add(robots.get(i));
		}
		ArrayList<Block> newOrderToMove=order(orderToMove);
		for(int i=0;i<newOrderToMove.size();i++)
		{
			moving(newOrderToMove.get(i),v);
		}
		
	}
	public ArrayList<Block> order(ArrayList<Block> otm)
	{	
		ArrayList<Block> sorted = new ArrayList<Block>();
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
		
		for(int i=buckets.size();i>1;i--)
		{
			buckets.get(i).getBloks().addAll(sorted);
		}
		return sorted;
	}
	public void moving(Block b, Vector3 v)
	{
		Vector3 bestMovement=new Vector3(0,0,0);
		boolean targetReached=false;
		while(!targetReached)
		{
			
			int bestDistance=-1;
			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			boolean safe =false;
			for(int k=0;k<robots.size();k++)
			{
				if(b.getPosition().x+1==robots.get(k).getPosition().x&&v.y==b.getPosition().y||b.getPosition().z+1==robots.get(k).getPosition().z&&v.y==b.getPosition().y||
				   b.getPosition().x==robots.get(k).getPosition().x&&v.y-1==b.getPosition().y&&b.getPosition().z==robots.get(k).getPosition().z||b.getPosition().x-1==robots.get(k).getPosition().x&&v.y==b.getPosition().y ||
				   b.getPosition().z-1==robots.get(k).getPosition().z&&v.y==b.getPosition().y)
				{
					safe= true;
					break;
				}
			}
			for(int i=0;i<robots.size();i++)
			{
				if(v.x==b.getPosition().x+1&&v.y<=b.getPosition().y)
				{
					
					if(safe)
					{
						bestMovement=v;
						bestDistance=1;
						b.setPosition(bestMovement.x,bestMovement.y,bestMovement.z);
						break;
					}
					
				}
				if(v.x==b.getPosition().x-1&&v.y<=b.getPosition().y)
				{
					if(safe)
					{
						bestMovement=v;
						bestDistance=1;
						b.setPosition(bestMovement.x,bestMovement.y,bestMovement.z);
						break;
					}
				}
				if(v.z==b.getPosition().z+1&&v.y<=b.getPosition().y)
				{
					if(safe)
					{
						bestMovement=v;
						bestDistance=1;
						b.setPosition(bestMovement.x,bestMovement.y,bestMovement.z);
						break;
					}
				}
				if(v.z==b.getPosition().z-1&&v.y<=b.getPosition().y)
				{
					if(safe)
					{
						bestMovement=v;
						bestDistance=1;
						b.setPosition(bestMovement.x,bestMovement.y,bestMovement.z);
						break;
					}
				}
				if(robots.get(i).getPosition().x==b.getPosition().x+1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}					
				}
				if(robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z+1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(robots.get(i).getPosition().x==b.getPosition().x-1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z-1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
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
				b.setPosition(bestMovement.x,bestMovement.y,bestMovement.z);
			}
		}
	}
}
