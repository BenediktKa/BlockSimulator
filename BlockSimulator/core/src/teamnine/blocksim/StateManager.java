package teamnine.blocksim;

public class StateManager
{
	public static SimulationState state;
	
	public StateManager(SimulationState state)
	{
		this.state = state;
	}
	
	public static enum SimulationState
	{
		BUILD, MENU, SIMULATION, SIMULATIONFPS, PAUSE;
	}
}
