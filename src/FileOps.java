import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileOps {
	private String traceFileName;
	
	
	public FileOps(String traceFileName) {
		this.setTraceFileName(traceFileName);
	}

	/*  *****    PLACE HOLDER ****
	// need to create an instruction class
	public List<Instructions> loadPersonnel() {
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


