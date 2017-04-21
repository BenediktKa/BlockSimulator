package teamnine.blocksim;

import java.util.ArrayList;

public class ConfigurationChecker {
	private BlockList blockList;
    private Grid grid;
    private ArrayList<Block> robotBlockList = new ArrayList<Block>();
	private ArrayList<Block> goalBlockList = new ArrayList<Block>();
	private ArrayList<Block> obstacleBlockList = new ArrayList<Block>();
	
	private ArrayList<Double> seenRobotIDs = new ArrayList<Double>();
	private ArrayList<Double> seenGoalIDs = new ArrayList<Double>();
	
	private String errorMessage;
	
	public ConfigurationChecker(BlockList blockList, Grid grid){
		this.blockList = blockList;
        this.grid = grid;
        
	}
	
	public ArrayList<Block> getRobotBlockList(){
		return robotBlockList;
	}
	
	public ArrayList<Block> getGoalBlockList(){
		return goalBlockList;
	}
	
	public ArrayList<Block> getObstacleBlockList(){
		return obstacleBlockList;
	}
	
	public boolean checkConfiguration(){
    	
    	int robotCntr = 0;
    	int goalCntr = 0;
    	
    	//Check if at least one block was connected to the ground floor
    	boolean robotConnectedToFloor = false;
    	boolean goalConnectedToFloor = false;
    	
    	if(blockList.size()<1){
    		errorMessage = "No blocks specified";
    	}
    	
    	for(int i=0; i<blockList.size(); i++){
    		
    		Block block = blockList.getBlock(i);
    		if(block.getType()== Block.Type.Robot){
    			robotBlockList.add(block);
    			if(block.getPosition().y==1) robotConnectedToFloor = true;
    			
    			//TODO: IMPLEMENT THE NULL VARIANT
    			if(block.getID()==0.0){
    				robotCntr++;
    			}
    			else if(seenRobotIDs.indexOf(block.getID()) == -1) { //The ID was not yet in there
    				robotCntr++;
    				seenRobotIDs.add(block.getID());
    			}
    			else {
    				errorMessage = "Robot Blocks with the same ID found: "+block.getID();
    				return false;
    			}
    			
    			
    		}
    		else if (block.getType() == Block.Type.Goal){
    			goalBlockList.add(block);
    			if(block.getPosition().y==1) goalConnectedToFloor = true;
    			
    			//TODO: IMPLEMENT THE NULL VARIANT
    			
    			if(block.getID() == 0.0){
    				goalCntr++;
    			}
    			else if(seenGoalIDs.indexOf(block.getID()) == -1) { //The ID was not yet in there
    				goalCntr++;
    				seenGoalIDs.add(block.getID());
    			}
    			else{
    				errorMessage = "Goal Blocks with the same ID found"+block.getID();
    				return false;
    			}
    			
    			
    		}
    		else if (block.getType() == Block.Type.Obstacle){
    			obstacleBlockList.add(block);
    			//TODO: RIGHT NOW OBSTACLES CAN FLY, KINDA FUNNY THOUGH
    		}
    		
    		if(!isConnected(block)){
    			errorMessage = "is not connected";
    			return false;
    		}
    	}
    	
    	//Check if there are as much robot blocks as target blocks
    	if(robotCntr!=goalCntr) {
    		errorMessage = "The amount of robot blocks is not equal to the amount of goal blocks: "+ robotCntr +" != "+goalCntr;
    		return false;
    	}
    	
    	//Check if there is a robot ID for every goal ID
    	for( int i=0; i<seenGoalIDs.size(); i++){
    		if(seenRobotIDs.indexOf(seenGoalIDs.get(i))==-1){
    			errorMessage = "There is a goal ID that is not in the start configuration";
    			return false;
    		}
    	}
    	
    	
    	//TODO: DEBUG THIS PART
    	//Check if all robots are connected to each other
    	if(!isOneComponent(robotBlockList)){
    		errorMessage = "Not one robot component";
    		return false;
    	}
    	
    	//Check if all target units are connected each other
    	if(!isOneComponent(goalBlockList)) {
    		errorMessage  = "Not one goal component";
    		return false;
    	}
    	
    	if(!robotConnectedToFloor | !goalConnectedToFloor){ // TODO: Check if it is working
    		errorMessage = "There is no robot modulel and/or no goal module connected to the floor (it flies)";
    		return false;
    	}
    	
    	return true;
    }
    
    private boolean isConnected (Block block){
    	int x = (int) block.getPosition().x;
    	int y = (int) block.getPosition().y;
    	int z = (int) block.getPosition().z;
    	
    	if(block.getPosition().y==1) return true; //There has to be a block underneath or to the side (if it is 1, its already on the floor)
    	
    	boolean allConnected = false;
    	
    	if(x>0 && grid.grid[x-1][y][z]!=null){
    		allConnected = true;
    	}
    	else if(x<grid.grid.length-1 && grid.grid[x+1][y][z]!=null){
    		allConnected = true;
    	}
    	else if (z>0 && grid.grid[x][y][z-1]!=null){
    		allConnected = true;
    	}
    	else if (z<grid.grid[x][y].length-1 && grid.grid[x][y][z+1]!=null){
    		allConnected = true;
    	}
    	else if (y>0 && grid.grid[x][y-1][z]!=null){
    		allConnected = true;
    	}
    	
    	return allConnected;
    }
    
    private boolean isOneComponent (ArrayList<Block> subList){
    	ArrayList<Block> compareBlock = new ArrayList<Block>(); 
    	findAllConnected(compareBlock, subList.get(0));
    	
    	if(subList.size()==compareBlock.size()) return true;
    	return false;
    	
    }
    
    private void findAllConnected(ArrayList<Block> visitedBlocks, Block block){
    	
    	visitedBlocks.add(block);
    	
    	int x = (int) block.getPosition().x;
    	int y = (int) block.getPosition().y;
    	int z = (int) block.getPosition().z;
    	
    	//if <prevent index out of bounds> <check if there is acturally a block> <check if block is already visited>  <check if it is a block of the same type>  
    	if(x>0 && grid.grid[x-1][y][z]!=null && visitedBlocks.indexOf(grid.grid[x-1][y][z])==-1 && grid.grid[x-1][y][z].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x-1][y][z]);
    	}
    	if(x<grid.grid.length-1 && grid.grid[x+1][y][z]!=null && visitedBlocks.indexOf(grid.grid[x+1][y][z])==-1 && grid.grid[x+1][y][z].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x+1][y][z]);
    	}
    	if(z>0 && grid.grid[x][y][z-1]!=null && visitedBlocks.indexOf(grid.grid[x][y][z-1])==-1 && grid.grid[x][y][z-1].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x][y][z-1]);
    	}
    	if(z<grid.grid[x][y].length-1 && grid.grid[x][y][z+1]!=null && visitedBlocks.indexOf(grid.grid[x][y][z+1])==-1 && grid.grid[x][y][z+1].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x][y][z+1]);
    	}
    	if(y>0 && grid.grid[x][y-1][z]!=null && visitedBlocks.indexOf(grid.grid[x][y-1][z])==-1 && grid.grid[x][y-1][z].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x][y-1][z]);
    	}
    	if(y<grid.grid[x].length-1 && grid.grid[x][y+1][z]!=null && visitedBlocks.indexOf(grid.grid[x][y+1][z])==-1 && grid.grid[x][y+1][z].getType()==block.getType()){
    		findAllConnected(visitedBlocks, grid.grid[x][y+1][z]);
    	}
    }

	public String getErrorMessage() {
		return errorMessage;
	}

	
}
