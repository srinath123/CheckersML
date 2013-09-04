package activities;

import datamodel.Board;
import datamodel.Coin;
import entities.Player;

public class NewCheckersGame {
	
	Board checkersBoard;
	Player player1;
	Player player2;
	public NewCheckersGame(Player player1,Player player2)
	{
		this.player1=player1;
		this.player2=player2;
		checkersBoard=new Board();
		checkersBoard.setBlackPieces(12);
		checkersBoard.setRedPieces(12);
		int black_y[]={0,2,4,6,1,3,5,7,0,2,4,6},
			black_x[]={0,0,0,0,1,1,1,1,2,2,2,2},
			red_y[]={1,3,5,7,0,2,4,6,1,3,5,7},
			red_x[]={7,7,7,7,6,6,6,6,5,5,5,5};
		for(int i=0;i<12;i++)
			{
			checkersBoard.coins[black_x[i]][black_y[i]]=new Coin(Coin.ORDINARY,Coin.BLACK);
			checkersBoard.coins[red_x[i]][red_y[i]]=new Coin(Coin.ORDINARY,Coin.RED);
			}
		
		if(player1.getColor()==player2.getColor())
			throw new IllegalArgumentException();
		gamehandler();
	}
	
	void gamehandler()
	{
		int neutrality=0;
		while(1>0)
		{
			Board temp=checkersBoard;
			if(checkersBoard.listLegalTransitions(player1.getColor()).size()==0)
			{
				System.out.println("Player 2 wins");
				player1.gameover();
				player2.gameover();
				return;
			}
			while(!islegalTransition(temp,player1))		
				{
					System.out.println("Player 1's move");
					temp=player1.nextMove(checkersBoard);
				}
			if(temp.getRedKings()+temp.getRedPieces()==checkersBoard.getRedKings()+checkersBoard.getRedPieces())
				neutrality++;
			else
				neutrality=0;
			if(neutrality==20)
			{
				System.out.println("Draw");
				player1.gameover();
				player2.gameover();
				return;
			}
				
			checkersBoard=temp;
			
			if(checkersBoard.listLegalTransitions(player2.getColor()).size()==0)
			{
				System.out.println("Player 1 wins");
				player1.gameover();
				player2.gameover();
				return;
			}
			
			while(!islegalTransition(temp,player2))
			{
				System.out.println("Player 2's move");
				temp=player2.nextMove(checkersBoard);
			}
			
			if(temp.getBlackKings()+temp.getBlackPieces()==checkersBoard.getBlackKings()+checkersBoard.getBlackPieces())
				neutrality++;
			else
				neutrality=0;
			if(neutrality==20)
			{
				System.out.println("Draw");
				player1.gameover();
				player2.gameover();
				return;
			}
			checkersBoard=temp;
		}
	}
	
	private boolean islegalTransition(Board board,Player player)
	{
		for(Board b:checkersBoard.listLegalTransitions(player.getColor()))
			if(b.equals(board)&&!b.isIntermediate())
				return true;
		return false;
	}
}