

public class Main {

	
	public static void main(String[] args) {
		
		String testParams[] = new String[] {
				"-f", "TinyTrace.trc",
				"-s", "512",
				"-b", "16",
				"-a", "8",
				"-r", "rnd"
		};
		
		
		FileOps fileOps = new FileOps(testParams);
		Cache cache = fileOps.argParseCacheConstructor();
		// Milestone #1
		printSimulationParameters(cache, fileOps);
		
		CacheSim cacheSim = new CacheSim(cache, fileOps.loadInstructions());
		System.out.println(cacheSim.getInstructions());
		
	}
	
	public static void printSimulationParameters(Cache cache, FileOps fileOps)
	{
		System.out.println("Cache Simulator CS3853 Summer 2020 - Group #04\n");
		System.out.println("Trace File: " + fileOps.getTraceFileName() + "\n");
		System.out.println(cache.toString());
	}


}
