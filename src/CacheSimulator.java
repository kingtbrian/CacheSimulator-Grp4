

public class CacheSimulator {

	
	public static void main(String[] args) {
		
		
		FileOps fileOps = new FileOps(args);
		Cache cache = fileOps.argParseCacheConstructor();
		
		// Milestone #1
		SimulationRunner cacheSim = new SimulationRunner(cache, fileOps.loadInstructions());
		fileOps.printCacheSimToConsole(cache, cacheSim.getInstructions());
	}
	


}
