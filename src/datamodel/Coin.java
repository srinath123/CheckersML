package datamodel;

public class Coin {
	public static final int BLACK=0;
	public static final int RED=1;
	public static final int ORDINARY=0;
	public static final int KING=1;
	
	private int type;
	private int color;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public Coin(int type, int color) {
		super();
		this.type = type;
		this.color = color;
	}
}