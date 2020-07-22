

public class CacheSimulator {

	
	public static void main(String[] args) {
		
		/*
		String params[] = new String[] 
				{
					"-f", "A-9_new_1.5.pdf.trc",
					"-s", "128",
					"-b", "4",
					"-a", "8",
					"-r", "rr"
				};
		*/
		FileOps fileOps = new FileOps(args);
		Cache cache = fileOps.argParseCacheConstructor();
		
		SimulationRunner cacheSim = new SimulationRunner(cache, fileOps.loadInstructions());
		cacheSim.runSimulation();
		fileOps.printCacheSimToConsole(cache, cacheSim.getInstructions());
	}
	


}
