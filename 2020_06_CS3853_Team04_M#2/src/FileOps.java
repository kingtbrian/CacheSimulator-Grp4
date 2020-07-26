import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class FileOps {
	private String args[];
	private String traceFileName;
	private int addrCount;
	
	private final String simResultsFileName = "SimulationResults.csv";
	
	public FileOps(String args[]) {
		this.args = args;
		this.defaultFileCheck();
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

	public Queue<Instruction> loadInstructions() {
		Queue<Instruction> instructionSet = new LinkedList<Instruction>();
		BufferedReader reader = null;
		String filePath = traceFileName;
		String line;
		int length;
		String address, writeDest, readDest;
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) {
				address = writeDest = readDest = null;
				
				length = Integer.parseInt(line.substring(5, 7));
				address = line.substring(10, 18);
				instructionSet.add(new Instruction(length, address));
				
				line = reader.readLine();
				if (line != null) {
					writeDest = line.substring(6, 14);
					readDest = line.substring(33, 41);
				} 
				
				if (!writeDest.equalsIgnoreCase("00000000")) {
					instructionSet.add(new Instruction(4, writeDest));
				}
				if (!readDest.equalsIgnoreCase("00000000")) {
					instructionSet.add(new Instruction(4, readDest));
				}
					
				line = reader.readLine();
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
	
	public void printCacheSimToConsole(Cache cache, Queue<Instruction> instructions) {
		System.out.println("Cache Simulator CS3853 Summer 2020 - Group #04\n\n");
		System.out.println("Trace File: " + this.traceFileName + "\n\n");
		System.out.println(cache.toString() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		instructions.stream()
		            .forEach(instruction -> {
		            	if (addrCount < 20)
		            		sb.append(instruction.toString() + "\n");
		            	addrCount++;
		            });
		System.out.println(sb.toString());
	}
	
	public void defaultFileCheck() {
		
		File f = new File(this.simResultsFileName);
		if (!f.exists()) {
			try {
				f.createNewFile();
				FileWriter deptWriter = new FileWriter(new File(simResultsFileName));
				StringBuilder sb = new StringBuilder();

				sb.append("Cache Size (KB)," +
						  "Block Size," +
						  "Associativty," +
						  "Replacement Policy," +
						  "Total Blocks," +
						  "Tag Size (bits)," +
						  "Index Size (bits)," +
						  "Total Rows," +
						  "Overhead Size," +
						  "Implementation Size," +
						  "Cost ($)," +
						  "Total Accesses," +
						  "Total Hits," +
						  "Total Misses," +
						  "Compulsory Misses," +
						  "Conflict Misses," +
						  "Hit Rate," +
						  "Miss Rate," +
						  "CPI," +
						  "Unused Space," +
						  "Unused Blocks," + 
						  "Waste ($)," +
						  "Filename\n"
						  );
				
				deptWriter.write(sb.toString());
				deptWriter.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveSimulation(Cache cache) {
		try {
			FileWriter deptWriter = new FileWriter(new File(simResultsFileName), true);
			StringBuilder sb = new StringBuilder();
			String cv = ",";
			String nl = "\n";
			
			String cost = String.format("%.2f", cache.getCost());
			String hitRate = String.format("%.2f%%", 100 * cache.getHitRate());
			String missRate = String.format("%.2f%%", 100 * cache.getMissRate());
			String unusedSpace = String.format("%.2f%%",100 * (cache.getUnusedSpace() * 1.0/cache.getKb(cache.getImplementationSizeKBytes()) * 1.0));
			String cpi = String.format("%.2f", cache.getCyclesPerInstruction());
			String waste = String.format("%.2f", cache.getWaste());
			
			sb.append(cache.getCacheSizeKBytes() + cv
					+ cache.getBlockSizeBytes() + cv
					+ cache.getAssociativity() + cv
					+ cache.getReplacementPolicy() + cv
					+ cache.getNumBlocks() + cv
					+ cache.getTagSizeBits() + cv
					+ cache.getIndexSizeBits() + cv
					+ cache.getTotalRows() + cv
					+ cache.getOverheadSizeBytes() + cv
					+ (cache.getKb(cache.getImplementationSizeKBytes()) * 1.0) + cv
					+ cost + cv
					+ cache.getTotalAccesses() + cv
					+ cache.getHits() + cv
					+ (cache.getCompulsoryMiss() + cache.getConflictMiss())  + cv
					+ cache.getCompulsoryMiss() + cv
					+ cache.getConflictMiss() + cv
					+ hitRate + cv
					+ missRate + cv
					+ cpi + cv
					+ unusedSpace + cv
					+ cache.getUnusedBlocks() + cv
					+ waste + cv 
					+ this.getTraceFileName() + nl
					);
			
			deptWriter.write(sb.toString());
			deptWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTraceFileName(String traceFileName) {
		this.traceFileName = traceFileName;
	}
	
	public String getTraceFileName() {
		return this.traceFileName;
	}

}


