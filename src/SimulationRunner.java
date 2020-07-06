import java.util.Queue;

public class SimulationRunner {
	private Cache cache;
	private Queue<Instruction> instructions;
	
	
	public SimulationRunner(Cache cache, Queue<Instruction> instructions) {
		this.setCache(cache);
		this.setInstructions(instructions);
	}


	public Cache getCache() {
		return cache;
	}


	private void setCache(Cache cache) {
		this.cache = cache;
	}


	public Queue<Instruction> getInstructions() {
		return instructions;
	}


	private void setInstructions(Queue<Instruction> instructions) {
		this.instructions = instructions;
	}
}
