package teamnine.blocksim;

public class StateManager
{
	public static SimulationState state = SimulationState.BUILD;
	
	public static enum SimulationState
	{
		BUILD, MENU, SIMULATION;
	}
}
