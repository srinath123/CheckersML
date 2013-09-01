package entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import datamodel.Board;

public class HumanPlayer implements Player {
	int color;

	public int getColor() {
		return color;
	}

	public HumanPlayer(int color) {
		super();
		this.color = color;
	}
	
	public void gameover()
	{
		System.out.println("Yay,I won");
	}

	public Board nextMove(Board b)
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		int i=0;
		b.PrintBoard();
		for(Board board:b.listLegalTransitions(color))
			{
			System.out.println(i);
			i++;
			board.PrintBoard();
			}
		try{
		i=Integer.parseInt(br.readLine());
		}
		catch(IOException e)
		{
			return null;
		}
		System.out.println(i);
		return b.listLegalTransitions(color).get(i);
		
	}
}
