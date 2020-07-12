import java.math.BigInteger;

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
		sb.append("0x" + this.address.toUpperCase() + ": (" + this.readLength + ")" );
		return sb.toString();
	}
	
	public String listInstruction() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.toString() + "\n");
		sb.append("\tWrite Destination: " + this.getWriteDest());
		sb.append("\tRead Destination:  " + this.getReadDest());
		return sb.toString();
	}
	
	
	public int getRowOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		
		bits = bits.substring(
				cache.getTagSizeBits(), 
				cache.getTagSizeBits() + cache.getIndexSizeBits());
		
		System.out.println("row");
		System.out.println(bits + " = " + this.bitsToInt(bits));
		return 0;
	}
	
	
	public int getTagOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		bits = bits.substring(
				0,
				cache.getTagSizeBits());
		
		System.out.println("tag");
		System.out.println(bits + " = " + this.bitsToInt(bits));
		return 0;
	}
	
	public int getOffsetOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		bits = bits.substring(cache.getTagSizeBits() + cache.getIndexSizeBits());
		System.out.println("Offset");
		System.out.println(bits + " = " + this.bitsToInt(bits));
		return 0;
	}
	
	public String addressToBits(String address)
	{
		return String.format("%32s", new BigInteger(address, 16).toString(2))
					 .replace(' ', '0');
	}
	
	public int bitsToInt(String binaryString)
	{
		return Integer.parseInt(binaryString, 2);
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
