

public class Main {

	public static void main(String[] args) {
		FileOps fileOps = new FileOps(args);
		Cache cache = fileOps.argParseCacheConstructor();
		
		// Milestone #1
		printSimulationParameters(cache, fileOps);
		
	}
	
	public static void printSimulationParameters(Cache cache, FileOps fileOps)
	{
		System.out.println("Cache Simulator CS3853 Summer 2020 - Group #04\n");
		System.out.println("Trace File: " + fileOps.getTraceFileName() + "\n");
		System.out.println(cache.toString());
	}


}
