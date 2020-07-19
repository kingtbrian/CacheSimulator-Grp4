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
		int addr[][] = new int[3][4];
		
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
			
			
			this.simDataAccess(addr[EIP], instruction, instruction.getReadLength());
			
			if (addr[DST][TAG] != 0 && addr[DST][ROW] !=0)
				this.simDataAccess(addr[DST], instruction, 4);
			
			if (addr[SRC][TAG] != 0 && addr[SRC][ROW] !=0)
				this.simDataAccess(addr[SRC], instruction, 4);

		}
	}
	
	public void simDataAccess(int i[], Instruction instruction, int bytesOfOperation)
	{
		int availableBlock = -1; 
		int totalNumRowAccesses = (bytesOfOperation + i[BYTE]) / cache.getBlockSizeBytes();
		System.out.println("totalNumRowAccesses" + totalNumRowAccesses);
		
		for (int cacheRow = i[ROW]; cacheRow < totalNumRowAccesses + cacheRow; cacheRow++)
		{
			for (int block = 0; block < cache.getAssociativity(); block++)
			{
				if(memory[cacheRow][block] == -1) {
					availableBlock = block;
				}
				
				// Cache Hit 
				if (memory[cacheRow][block] == i[TAG])
				{
					// 1 for hit, 2 for instruction
					i[HIT] = 1;
					cache.setHits(cache.getHits() + 3);
					this.updateMemoryTracker(block, cacheRow);
					break;
				}
			}		
			// Cache Miss 
			if (i[HIT] == 0)
			{
				if (availableBlock >= 0)
				{
					memory[cacheRow][availableBlock] = i[TAG];
					// 4 * number reads needed + 2 for instruction
					cache.setCompulsoryMiss(cache.getCompulsoryMiss() + 
							(4 * this.getNumberOfReads()) + 2);
				} 
				else 
				{
					cache.setConflictMiss(cache.getConflictMiss() + 
							(4 * this.getNumberOfReads()) + 2);
					int replaceBlock = getReplacementBlock(cacheRow);
					memory[cacheRow][replaceBlock] = i[TAG];
				}
			}

			// account for new byte offset after operation
			// unneeded if there are no wraps
			i[BYTE] = (i[BYTE] + bytesOfOperation) % cache.getBlockSizeBytes();
				
			availableBlock = -1; 
		}
	}
		
	public int getNumberOfReads() {
		return (int)Math.ceil(cache.getBlockSizeBytes()/4);
	}
	
	//used for LRU replacement policy
	public void updateMemoryTracker(int block, int row)
	{
		int recentlyUsed = block;
		int index = 0; 

		for (int i = 0; i < this.cache.getAssociativity() - 1; i++)
		{
			if (this.memoryTracker[row][i] == block)
			{
				index = i;
			}
		}
		
		for (int i = index; i > 1; i++)
		{
			this.memoryTracker[row][i] = this.memoryTracker[row][i - 1];
		}
		
		this.memoryTracker[row][0] = recentlyUsed;
	}
	public int getRoundRobinReplacement(int row){

		int nextToBeReplaced = -1;
		for (int block = 0; block < cache.getAssociativity(); block++)
		{
			if (this.memoryTracker[row][block] == 1)
			{
				nextToBeReplaced = block;
				
				// set current block in memory tracker to 0
				this.memoryTracker[row][block] = 0;
				
				// set the next block to 1 for the next replacement
				if ( block == cache.getAssociativity() - 1)
				{
					this.memoryTracker[row][0] = 1;
				}
				else 
				{
					this.memoryTracker[row][++block] = 1;
				}
				
				return nextToBeReplaced;
			}
		}
		return -1;
	}
 	
	public int getRandomReplacement(){
		int block = (int)Math.random() * (cache.getAssociativity());
		if ( block == cache.getAssociativity())
		{
			System.out.println("Need to change getRandomReplacement");
		}
		return block;
	}

	public int getLRUReplacement(int row){

		int blockToBeReplaced = this.memoryTracker[row][this.cache.getAssociativity() - 1];
		int buffer;
		// not optimal but to hurry up complete project we are going with it
		for (int i = this.cache.getAssociativity() - 1; i > 1; i--)
		{
			this.memoryTracker[row][i] = this.memoryTracker[row][i - 1];
		}
		// update memoryTracker psuedo-queue
		this.memoryTracker[row][0] = blockToBeReplaced;
		
		return blockToBeReplaced;
	}
	public int getReplacementBlock(int row)
	{
		switch(cache.getReplacementPolicy())
		{
			case "Round Robin":
				return getRoundRobinReplacement(row);
			case "Random":
				return getRandomReplacement();
			case "Least Recently Used":
				return getLRUReplacement(row);
		}
		
		return -1;
		/*
		int min = Integer.MAX_VALUE;
		int blocksTracker[][];
		for (int i = 0; i < cache.getAssociativity(); i++)
		{
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
		*/
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
			}
		}	
		this.configureMemoryTracker();
	}
	
	public void configureMemoryTracker()
	{
		
		switch(cache.getReplacementPolicy())
		{
			case "Round Robin":
				for (int i = 0; i < this.cache.getTotalRows(); i++)
				{
					for (int j = 0; j < this.cache.getAssociativity(); j++) 
					{
						if (j == 0)
						{
							this.memoryTracker[i][j] = 1;
						}
						else 
						{
							this.memoryTracker[i][j] = 0;
						}
					}
				}	
				break;
			case "Random":
				break;
			case "Least Recently Used":
				for (int i = 0; i < this.cache.getTotalRows(); i++)
				{
					for (int j = 0; j < this.cache.getAssociativity(); j++) 
					{
						this.memoryTracker[i][j] = 0;
					}
				}
				break;
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
