

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
		
		
		//produceSimResults();


	}
	
	public static void produceSimResults()
	{
		String fileName[] = {"A-10_new_1.5_a.pdf.trc",
				 "A-9_new_1.5.pdf.trc",
				 "Corruption2.trc"};

		String replacement[] = {"rr", "rnd"};
		
		for (int cacheSize = 1024; cacheSize <= 1024; cacheSize *= 2)
		{
			for (int blockSize = 16; blockSize <= 64; blockSize *= 2)
			{
				for (int assoc = 2; assoc <= 8; assoc *= 2)
				{
					for (int policy = 0; policy <= 1; policy++)
					{
						for (int file = 0; file <= 2; file++)
						{
							String params[] = new String[] 
									{
										"-f", fileName[file],
										"-s", cacheSize + "",
										"-b", blockSize + "",
										"-a", assoc + "",
										"-r", replacement[policy]
									};
							FileOps fileOps = new FileOps(params);
							Cache cache = fileOps.argParseCacheConstructor();
							SimulationRunner cacheSim = new SimulationRunner(cache, fileOps.loadInstructions());
							cacheSim.runSimulation();
							fileOps.saveSimulation(cache);
							System.out.println("Completed: size = " + cacheSize + " block = " + blockSize
									                + " assoc = " + assoc + " policy  = " + replacement[policy]
									                + " file = " + fileName[file]);
						}
					}
				}
			}
		}
	}
	


}
