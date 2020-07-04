

public class Main {

	
	public static void main(String[] args) {
		
		
		FileOps fileOps = new FileOps(args);
		Cache cache = fileOps.argParseCacheConstructor();
		
		// Milestone #1
		CacheSim cacheSim = new CacheSim(cache, fileOps.loadInstructions());
		fileOps.saveSimulation(cache, cacheSim.getInstructions());
		
	}
	


}
