

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileOps fileOps = new FileOps(args);
		Cache cache = fileOps.argParseCacheConstructor();
		System.out.println(cache.toString());
		
	}
	
	public Cache argparse() {
		return new Cache();
	}


}
