
public class FileOps {
	private String args[];
	private String traceFileName;
	
	public FileOps(String args[]) {
		this.args = args;
	}
	
	public Cache argParseCacheConstructor() 
	{
		int idx = 0;
		int cacheSize = Integer.MIN_VALUE;
		int blockSize = Integer.MIN_VALUE;
		int associativity = Integer.MIN_VALUE;
		String rPolicy = null;

		while (idx < args.length) {
			switch(args[idx]) {
				case "-f":
					this.traceFileName = args[++idx];
					idx++;
					break;
				case "-s":
					cacheSize = Integer.parseInt(args[++idx]);
					idx++;
					break;
				case "-b":
					blockSize = Integer.parseInt(args[++idx]);
					idx++;
					break;
				case "-a":
					associativity = Integer.parseInt(args[++idx]);
					idx++;
					break;
				case "-r":
					rPolicy = args[++idx];
					idx++;
					break;
				default:
					idx++;
					break;
			}
		}
		
		Cache cache = new Cache(cacheSize, blockSize, associativity, rPolicy);
		return cache;
	}

	/*  *****    PLACE HOLDER ****
	// need to create an instruction class
	public List<Instructions> loadInstructions() {
		List<Instructions> instructionSet = new List<Instructions>();
		BufferedReader reader = null;
		String line;

		try {
			reader = new BufferedReader(new FileReader(traceFileName));
			while ((line = reader.readLine()) != null) {
				
				// read line from file, parse line, create new instruction and
				// load into instruction (list, queue, heap)? 
				 
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return instructionSet;
	}
	*/
	
	public void setTraceFileName(String traceFileName) {
		this.traceFileName = traceFileName;
	}
	
	public String getTraceFileName() {
		return this.traceFileName;
	}

}


