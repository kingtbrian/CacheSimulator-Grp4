

public class CacheSimulator {

	
	public static void main(String[] args) {
		
		
		String params[] = new String[] 
				{
					"-f", "TinyTrace.trc",
					"-s", "512",
					"-b", "16",
					"-a", "8",
					"-r", "rnd"
				};
		
		FileOps fileOps = new FileOps(params);
		Cache cache = fileOps.argParseCacheConstructor();
		
		// Milestone #1
		SimulationRunner cacheSim = new SimulationRunner(cache, fileOps.loadInstructions());
		fileOps.printCacheSimToConsole(cache, cacheSim.getInstructions());
		
		Instruction i = new Instruction(4, "7c809767", "000", "000");
		i.getRowOfAddress(cache);
		i.getTagOfAddress(cache);
		i.getOffsetOfAddress(cache);
	}
	


}
