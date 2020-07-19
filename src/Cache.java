public class Cache {
	private int cacheSizeKBytes;
	private int blockSizeBytes;
	private int associativity; 
	private String replacementPolicy;
	private int numBlocks;
	private int tagSizeBits;
	private int indexSizeBits;
	private int totalRows;
	private int overheadSizeBytes;
	private int implementationSizeKBytes;
	private int totalAccesses;
	private int hits;
	private int totalMisses;
	private int compulsoryMiss;
	private int conflictMiss;
	private int cycles;
	private int instructionCount;
	private double waste;
	private double cost;
	private double hitRate;
	private double missRate;
	private double cyclesPerInstruction;
	private double unusedSpace; 
	private int unusedBlocks;
	
	public Cache(int cacheSize, int blockSize, int associativity, String replacementPolicy) {
		try {
			this.setCacheSizeKBytes(cacheSize);
			this.setBlockSizeBytes(blockSize);
			this.setAssociativity(associativity);
			this.setReplacementPolicy(replacementPolicy);
		} catch (IllegalArgumentException e) {
			System.out.println("Program Exiting: invalid parameters -> " + e.toString());
			System.exit(-1);
		}
		
		// sets index, tag, #blocks, # rows, overhead Size, implemented memory size, cost.
		this.calculateCache();
	}
	
	public Cache() {}
	
	public String toString() {
		String v; 
		StringBuilder sb = new StringBuilder();
		sb.append("***** Cache Input Parameters *****\n");
		v = String.format("%-30s", "Cache Size:");
		sb.append(v + this.getCacheSizeKBytes() + "\n");
		v = String.format("%-30s", "Block Size:");
		sb.append(v + this.getBlockSizeBytes() + "\n");
		v = String.format("%-30s", "Associativity:");
		sb.append(v + this.getAssociativity() + "\n");
		v = String.format("%-30s", "Replacement Policy:");
		sb.append(v + this.getReplacementPolicy() + "\n\n");
		
		sb.append("***** Cache Calculated Values *****\n");
		v = String.format("%-30s", "Total # Blocks:");
		sb.append(v + this.getNumBlocks() + "\n");
		v = String.format("%-30s", "Tag Size:");
		sb.append(v + this.getTagSizeBits() + "\n");
		v = String.format("%-30s", "Index Size:");
		sb.append(v + this.getIndexSizeBits() + "\n");
		v = String.format("%-30s", "Total # Rows:");
		sb.append(v + this.getTotalRows() + "\n");
		v = String.format("%-30s", "Overhead Size:");
		sb.append(v + this.getOverheadSizeBytes() + "\n");
		v = String.format("%-30s", "Implementation Size:");
		sb.append(v + this.getKb(this.getImplementationSizeKBytes()) + ".00 KB   (" + this.getImplementationSizeKBytes() +" bytes)"+ "\n");
		v = String.format("%-30s", "Cost:");
		sb.append(v);
		v = String.format("%.2f", this.getCost());
		sb.append("$" + v + "\n\n");
		
		sb.append("***** CACHE SIMULATION RESULTS *****\n");
		v = String.format("%-30s", "Total Cache Accesses:");
		sb.append(v + this.getTotalAccesses() + "\n");
		v = String.format("%-30s", "Cache Hits:");
		sb.append(v + this.getHits() + "\n");
		v = String.format("%-30s", "Cache Misses:");
		sb.append(v + this.getTotalMisses() + "\n");
		v = String.format("%-30s", "--- Compulsory Misses:");
		sb.append(v + this.getCompulsoryMiss() + "\n");
		v = String.format("%-30s", "--- Conflict Misses:");
		sb.append(v + this.getConflictMiss() + "\n\n");
		
		sb.append("***** ***** CACHE HIT & MISS RATE: ***** *****\n");
		v = String.format("%-30s", "Hit Rate:");
		sb.append(v);
		v = String.format("%.2f", 100 * this.getHitRate());
		sb.append(v + "%\n");
		v = String.format("%-30s", "Miss Rate:");
		sb.append(v);
		v = String.format("%.2f", 100 * this.getMissRate());
		sb.append(v + "%\n");
		v = String.format("%-30s", "CPI:");
		sb.append(v);
		v = String.format("%.2f Cycles/Instruction\n",  this.getCyclesPerInstruction());
		sb.append(v);
		v = String.format("%-30s", "Unused Cache Space:");
		sb.append(v);
		v = String.format("%.2f KB / %.2f KB = %.2f%% Waste = $%.2f \n", this.getUnusedSpace(), 
														   this.getKb(this.getImplementationSizeKBytes()) * 1.0,  
														   100 * (this.getUnusedSpace() * 1.0/this.getKb(this.getImplementationSizeKBytes()) * 1.0),
														   this.getWaste());
		sb.append(v);
		v = String.format("%-30s", "Unused Cache Blocks:");
		sb.append(v + this.getUnusedBlocks() + "\n");
		
		return sb.toString();
	}
	
	public void calculateCache() {
		this.calculateNumBlocks();
		this.calculateIndex();
		this.calculateTagSize();
		this.calculateNumRows();
		this.calculateOverheadSize();
		this.calculateImplementationSize();
		this.calculateCost();
	}
	
	public int getKb(int n) {
		return (int)(n / (Math.pow(2, 10)) );
	}
	
	public void calculateCost() {
		this.setCost(
				.07 * this.implementationSizeKBytes/(int)(Math.pow(2, 10))
				);
	}
	
	public void calculateImplementationSize() {
		this.setImplementationSizeKBytes( 
				(this.cacheSizeKBytes * (int)(Math.pow(2, 10))) + this.overheadSizeBytes
				);
	}
	
	public void calculateOverheadSize() {
		this.setOverheadSizeBytes( 
				((this.tagSizeBits + 1)) * (this.numBlocks / 8)
				);
	}
	
	public void calculateNumRows() {
		this.setTotalRows(
				(this.cacheSizeKBytes * (int)(Math.pow(2, 10))) / (this.blockSizeBytes * this.associativity)
				);
	}
	
	public void calculateNumBlocks() {
		this.setNumBlocks(
				(this.cacheSizeKBytes * (int)(Math.pow(2, 10))) / this.blockSizeBytes
				);
	}
	
	public void calculateIndex() {
		this.setIndexSizeBits(
				(int)(Math.log((this.cacheSizeKBytes * Math.pow(2, 10)) / (this.blockSizeBytes * this.associativity)) /
					  Math.log(2)
				));	
	}
	
	public void calculateTagSize() {
		this.setTagSizeBits(
				32 - this.indexSizeBits - (int)((Math.log(this.blockSizeBytes))/Math.log(2))
				);
	}
	
	public String getReplacementPolicy() {
		return this.replacementPolicy;
	}
	public void setReplacementPolicy(String replacement) {
		if (replacement.equalsIgnoreCase("rr")) {
			this.replacementPolicy = "Round Robin";
		} else if (replacement.equalsIgnoreCase("rnd")) {
			this.replacementPolicy = "Random";
		} else if (replacement.equalsIgnoreCase("lru")) {
			this.replacementPolicy = "Least Recently Used";
		} else {
			throw new IllegalArgumentException("Unknown Replacement Policy Entered");
		}
	}
	public int getCacheSizeKBytes() {
		return cacheSizeKBytes;
	}
	public void setCacheSizeKBytes(int cacheSizeKBytes)  {
		if (cacheSizeKBytes < 1 || cacheSizeKBytes > 8192 ) {
			throw new IllegalArgumentException("Cache Size must be between 1 KB and 8 MB");
		}
		this.cacheSizeKBytes = cacheSizeKBytes;
	}
	public int getBlockSizeBytes() {
		return blockSizeBytes;
	}
	public void setBlockSizeBytes(int blockSizeBytes) {
		if (blockSizeBytes < 4 || blockSizeBytes > 64) {
			throw new IllegalArgumentException("Block Size must be between 4 bytes and 64 bytes");
		}
		this.blockSizeBytes = blockSizeBytes;
	}
	public int getAssociativity() {
		return associativity;
	}
	public void setAssociativity(int associativity) {
		if (associativity != 1 &&
			associativity != 2 &&
			associativity != 4 &&
			associativity != 8 &&
			associativity != 16 ) {
			throw new IllegalArgumentException("Associativity must equal 1, 2, 4, 8 or 16");
		}
		this.associativity = associativity;
	}
	public int getNumBlocks() {
		return numBlocks;
	}
	public void setNumBlocks(int numBlocks) {
		this.numBlocks = numBlocks;
	}
	public int getTagSizeBits() {
		return tagSizeBits;
	}
	public void setTagSizeBits(int tagSizeBits) {
		this.tagSizeBits = tagSizeBits;
	}
	public int getIndexSizeBits() {
		return indexSizeBits;
	}
	public void setIndexSizeBits(int indexSizeBits) {
		this.indexSizeBits = indexSizeBits;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getOverheadSizeBytes() {
		return overheadSizeBytes;
	}
	public void setOverheadSizeBytes(int overheadSizeBytes) {
		this.overheadSizeBytes = overheadSizeBytes;
	}
	public int getImplementationSizeKBytes() {
		return implementationSizeKBytes;
	}
	public void setImplementationSizeKBytes(int implementationSizeKBytes) {
		this.implementationSizeKBytes = implementationSizeKBytes;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getTotalAccesses() {
		return totalAccesses;
	}

	public void setTotalAccesses() {
		this.totalAccesses = this.getHits() + getTotalMisses();
	}

	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getTotalMisses() {
		return totalMisses;
	}

	public void setTotalMisses() {
		this.totalMisses = this.getCompulsoryMiss() + this.getConflictMiss();
	}

	public int getCompulsoryMiss() {
		return compulsoryMiss;
	}
	public void setCompulsoryMiss(int compulsoryMiss) {
		this.compulsoryMiss = compulsoryMiss;
	}
	public int getConflictMiss() {
		return conflictMiss;
	}
	public void setConflictMiss(int conflictMiss) {
		this.conflictMiss = conflictMiss;
	}
	public double getHitRate() {
		return hitRate;
	}
	public void setHitRate() {
		this.hitRate = this.getHits() * 1.0 / this.getTotalAccesses();
	}
	public double getMissRate() {
		return missRate;
	}
	public void setMissRate() {
		this.missRate = this.getTotalMisses() * 1.0 / this.getTotalAccesses();
	}
	public double getCyclesPerInstruction() {
		return cyclesPerInstruction;
	}
	public void setCyclesPerInstruction() {
		this.cyclesPerInstruction = this.getCycles() * 1.0 / this.getInstructionCount();
	}
	public double getUnusedSpace() {
		return unusedSpace;
	}
	public void setUnusedSpace() {
		this.unusedSpace = this.getKb((this.getAssociativity() * this.getTotalRows() * (this.blockSizeBytes))
				- (this.getAssociativity() * this.getTotalRows() * (this.blockSizeBytes) - (this.getBlockSizeBytes() * this.getUnusedBlocks())));
	}
	public int getUnusedBlocks() {
		return unusedBlocks;
	}
	
	public void setWaste()
	{
		this.waste = .07 * this.getUnusedSpace();
	}
	
	public double getWaste()
	{
		return this.waste;
	}
	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	
	public void setInstructionCount(int instructionCount)
	{
		this.instructionCount = instructionCount;
	}
	
	public int getInstructionCount()
	{
		return this.instructionCount;
	}
	
	public void setUnusedBlocks(int n)
	{
		this.unusedBlocks = n;
	}
	
	public void calculateCacheMetrics()
	{
		this.setTotalMisses();
		this.setTotalAccesses();
		this.setHitRate();
		this.setMissRate();
		this.setCyclesPerInstruction();
		this.setUnusedSpace();
		this.setWaste();
	}
	
}
