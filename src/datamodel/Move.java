package datamodel;

public class Move {
	
	private int from_x,from_y;
	private int to_x,to_y;
	private boolean attacking;
	
	public boolean isAttacking() {
		return attacking;
	}
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	public Move(int from_x, int from_y, int to_x, int to_y, boolean attacking) {
		super();
		this.from_x = from_x;
		this.from_y = from_y;
		this.to_x = to_x;
		this.to_y = to_y;
		this.attacking = attacking;
	}
	public int getFrom_x() {
		return from_x;
	}
	public void setFrom_x(int from_x) {
		this.from_x = from_x;
	}
	public int getFrom_y() {
		return from_y;
	}
	public void setFrom_y(int from_y) {
		this.from_y = from_y;
	}
	public int getTo_x() {
		return to_x;
	}
	public void setTo_x(int to_x) {
		this.to_x = to_x;
	}
	public int getTo_y() {
		return to_y;
	}
	public void setTo_y(int to_y) {
		this.to_y = to_y;
	}
}