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
				
				line = reader.readLine();
				if (line != null) {
					writeDest = line.substring(6, 14);
					readDest = line.substring(33, 41);
				} 
				
				instructionSet.add(new Instruction(length, address, writeDest, readDest));
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
	
	public void saveSimulation(Cache cache, Queue<Instruction> instructions) {
		try {
			
			File directory = new File(new File(".").getAbsolutePath());
			String l[] = directory.list();
			int fileCount = 0;
			for(String word : l) {
				if (word.contains("M#1.txt")) {
					fileCount++;
				}
			}
			
			String outputFileName = "Team_04_Sim_" + ++fileCount + "_M#1.txt";
			
			
			FileWriter simWriter = new FileWriter(new File(outputFileName));
			StringBuilder sb = new StringBuilder();
			
			sb.append("Cache Simulator CS3853 Summer 2020 - Group #04\n\n");
			sb.append("Trace File: " + this.traceFileName + "\n\n");
			sb.append(cache.toString() + "\n\n");
			
			/*
			for(Instruction i : instructions) {
				if (addrCount < 20) {
            		sb.append(i.toString() + "\n");
				}
				addrCount++;
			}
			*/
			
			instructions.stream()
            	.forEach(instruction -> {
            		if (addrCount < 20) {
            			sb.append(instruction.toString() + "\n");
            		}
            		addrCount++;
            });
			
			simWriter.write(sb.toString());
			simWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		addrCount = 0;
	}
	
	public void setTraceFileName(String traceFileName) {
		this.traceFileName = traceFileName;
	}
	
	public String getTraceFileName() {
		return this.traceFileName;
	}

}


