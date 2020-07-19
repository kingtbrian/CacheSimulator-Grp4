

public class CacheSimulator {

	
	public static void main(String[] args) {
		
		
		String params[] = new String[] 
				{
					"-f", "A-10_new_1.5_a.pdf.trc",
					"-s", "512",
					"-b", "16",
					"-a", "8",
					"-r", "rr"
				};
		
		FileOps fileOps = new FileOps(params);
		Cache cache = fileOps.argParseCacheConstructor();
		
		// Milestone #1
		SimulationRunner cacheSim = new SimulationRunner(cache, fileOps.loadInstructions());
		cacheSim.runSimulation();
		fileOps.printCacheSimToConsole(cache, cacheSim.getInstructions());
	}
	


}
