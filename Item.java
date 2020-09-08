

/**
 * Items.
 *
 */
public class Item{
	
	private int key;
	private String info;
	
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	public int getKey(){
		return key;
	}
	public String getInfo(){
		return info;
	}
	
//	public String toString( ) {
//		return "" + this.getKey();
//	}
}