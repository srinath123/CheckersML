package entities;

import datamodel.Board;

public interface Player {
	public int getColor();
	
	public Board nextMove(Board b);
	
	public void gameover();

}
