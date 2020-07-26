import java.math.BigInteger;

public class Instruction {
	private int readLength;
	private String address;
	
	public Instruction() {
	}
	
	public Instruction(int readLength, String address) {
		this.setReadLength(readLength);
		this.setAddress(address);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("0x" + this.address.toUpperCase() + ": (" + this.readLength + ")" );
		return sb.toString();
	}
	
	public String listInstruction() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.toString() + "\n");
		return sb.toString();
	}
	
	
	public int getRowOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		
		bits = bits.substring(
				cache.getTagSizeBits(), 
				cache.getTagSizeBits() + cache.getIndexSizeBits());
		
		return bitsToInt(bits);
	}
	
	
	public int getTagOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		bits = bits.substring(
				0,
				cache.getTagSizeBits());
		
		return bitsToInt(bits);
	}
	
	public int getOffsetOfAddress(Cache cache, String address)
	{
		String bits = this.addressToBits(address);
		bits = bits.substring(cache.getTagSizeBits() + cache.getIndexSizeBits());
		return bitsToInt(bits);
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
}
