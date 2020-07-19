import java.util.Queue;

public class SimulationRunner {
	private Cache cache;
	private Queue<Instruction> instructions;
	private int memory[][]; // row and column of cache
	private int memoryTracker[][]; // tracking for replacement policy
	private int instructionCount;

	public static int TAG = 0;
	public static int ROW = 1;
	public static int BYTE = 2; 
	public static int HIT = 3;
	public static int EIP = 0;
	public static int DST = 1;
	public static int SRC = 2;


	public SimulationRunner(Cache cache, Queue<Instruction> instructions) {
		this.setCache(cache);
		this.setInstructions(instructions);
		this.setInstructionCount(instructions.size());
		this.setMemory();
	}

	
	
	public void runSimulation()
	{
		int addr[][] = new int[3][3];
		
		while (!this.instructions.isEmpty()) 
		{
			Instruction instruction = this.instructions.remove();
			
			addr[EIP][TAG] = instruction.getTagOfAddress(cache, instruction.getAddress());
			addr[EIP][ROW] = instruction.getRowOfAddress(cache, instruction.getAddress());
			addr[EIP][BYTE] = instruction.getOffsetOfAddress(cache, instruction.getAddress());
			addr[EIP][HIT] = 0;
			addr[DST][TAG] = instruction.getTagOfAddress(cache, instruction.getWriteDest());
			addr[DST][ROW] = instruction.getRowOfAddress(cache, instruction.getWriteDest());
			addr[DST][BYTE] = instruction.getOffsetOfAddress(cache, instruction.getWriteDest());
			addr[DST][HIT] = 0;
			addr[SRC][TAG] = instruction.getTagOfAddress(cache, instruction.getReadDest());
			addr[SRC][ROW] = instruction.getRowOfAddress(cache, instruction.getReadDest());
			addr[SRC][BYTE] = instruction.getOffsetOfAddress(cache, instruction.getReadDest());
			addr[SRC][HIT] = 0;
			
			
			this.simDataAccess(addr[EIP], instruction);
			this.simDataAccess(addr[DST], instruction);
			this.simDataAccess(addr[SRC], instruction);

		}
	}
	
	public void simDataAccess(int address[], Instruction instruction)
	{
		int bytesOfOperation = instruction.getReadLength();
		int availableBlock = -1; 

		// Operation will not involve wrapping to more rows
		if (bytesOfOperation + address[BYTE] <= cache.getBlockSizeBytes())
		{
			for (int block = 0; block < cache.getAssociativity(); block++)
			{
				if(memory[address[ROW]][block] == -1) {
					availableBlock = block;
				}
				
				// Cache Hit - No Wrap
				if (memory[address[ROW]][block] == address[TAG])
				{
					// 1 for hit, 2 for instruction
					address[HIT] = 1;
					cache.setHits(cache.getHits() + 3);
					this.updateMemoryTracker(block, address[ROW]);
					break;
				}
			}
			
			// Cache Miss - No Wrap
			if (address[HIT] == 0)
			{
				if (availableBlock >= 0)
				{
					memory[address[ROW]][availableBlock] = address[TAG];
					// 4 * number reads needed + 2 for instruction
					cache.setCompulsoryMiss(cache.getCompulsoryMiss() + 
							(4 * this.getNumberOfReads()) + 2);
				} 
				else 
				{
					int replaceBlock = getReplacementBlock(address[ROW]);
					memory[address[ROW]][replaceBlock] = address[TAG];
				}
			}
			
			availableBlock = -1; 
		}
		else 
		{
			int iRows = ((bytesOfOperation + address[BYTE]) / cache.getBlockSizeBytes());
			while(iRows != 0)
			{
				//process 1 row
				iRows--;
			}
		}
		// separate logic in case of multiple row accesses? can it be combined?
		// What are the edge cases for multiple row access? 
		// maybe this:
		// for ( i = 0; 
		//       i <  ((number of bytes for read/write instruction 
		//			  + byte offset in address the instruction is accessing) 
		//			  / cache block size);
		// 		 i++)
		// formula should produce the number of rows needing to be accessed? no more separate logic for wraps?
					 
					
	}
	
	public int getNumberOfReads() {
		return (int)Math.ceil(cache.getBlockSizeBytes()/4);
	}
	
	public void updateMemoryTracker(int index, int row)
	{
		for (int i = 0; i < cache.getAssociativity(); i++)
		{
			for(int j = 0; j < cache.getTotalRows(); j++)
			{
				if(i == index && j == row)
				{
					memory[j][i] = 0;
				}
			}
			// logic to track recently used.
		}
	}
	public int getRoundRobin(int block){
		//last block replaced + 1
		return block + 1;
	}
	public int getRandomReplacement(int block){
		block = (int)Math.random();
		return block;
	}
	public int getLRU(int block){
		//block least recently used
		return block;
	}
	public int getReplacementBlock(int row)
	{
		int min = Integer.MAX_VALUE;
		int blocksTracker[][];
		for (int i = 0; i < cache.getAssociativity(); i++)
		{
			for(int j = 0; j < row; j++){
				if( memoryTracker[j][i] == 0){
					return memoryTracker[j][i];
				}
			}
			// pull from memory tracking array next index to be replaced.
			for(int j = 0; j < row; j++)
			{
				if(memory[j][i] == 0)
				{
					return memory[j][i];
				}
			}
		}
		//return block needing to be replaced
		return -1;
	}
	
	public void setMemory() {
		
		this.memory = new int[this.cache.getTotalRows()][this.cache.getAssociativity()];
		this.memoryTracker = new int[this.cache.getTotalRows()][this.cache.getAssociativity()];
		
		//initialize array with -1 for valid bit
		
		for (int i = 0; i < this.cache.getTotalRows(); i++)
		{
			for (int j = 0; j < this.cache.getAssociativity(); j++) 
			{
				this.memory[i][j] = -1;
				this.memoryTracker[i][j] = -1;
			}
		}
		
	}
	
	public int[][] getMemory()
	{
		return this.memory;
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
	
	public int getInstructionCount() {
		return instructionCount;
	}



	public void setInstructionCount(int instructionCount) {
		this.instructionCount = instructionCount;
	}
}
