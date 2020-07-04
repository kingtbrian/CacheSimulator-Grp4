

public class Instruction {
	private int readLength;
	private String address;
	private String writeDest;
	private String readDest;
	
	public Instruction() {
	}
	
	public Instruction(int readLength, String address, String writeDest, String readDest) {
		this.setReadLength(readLength);
		this.setAddress(address);
		this.setWriteDest(writeDest);
		this.setReadDest(readDest);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("0x" + this.address + ": (" + this.readLength + ")" );
		return sb.toString();
	}
	
	public String listInstruction() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.toString() + "\n");
		sb.append("\tWrite Destination: " + this.getWriteDest());
		sb.append("\tRead Destination:  " + this.getReadDest());
		return sb.toString();
	}
	
	
	
	public int getReadLength() {
		return readLength;
	}
	public void setReadLength(int readLength) {
		this.readLength = readLength;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWriteDest() {
		return writeDest;
	}
	public void setWriteDest(String writeDest) {
		this.writeDest = writeDest;
	}
	public String getReadDest() {
		return readDest;
	}
	public void setReadDest(String readDest) {
		this.readDest = readDest;
	}
	
	
}
