package test;

import activities.NewCheckersGame;
import datamodel.Coin;
import entities.Agent;
import entities.HumanPlayer;
import entities.Player;


public class CheckersTest {
	public static void main(String args[])
	{
		NewCheckersGame newGame;
		for(int i=0;i<100;i++)
			newGame=new NewCheckersGame(new Agent(Coin.BLACK,true),new Agent(Coin.RED,true));
	}
}