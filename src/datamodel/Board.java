package datamodel;

import java.util.ArrayList;
import java.util.List;

public class Board {
	public Coin[][] coins;
	
	private boolean attacking;
	int blackKings,blackPieces,redKings,redPieces;
	
	public int getBlackKings() {
		return blackKings;
	}

	public int getBlackPieces() {
		return blackPieces;
	}

	public int getRedKings() {
		return redKings;
	}


	public int getRedPieces() {
		return redPieces;
	}


	public Board()
	{
		coins=new Coin[8][8];
		blackKings=blackPieces=redKings=redPieces=0;
		attacking=false;
	}
	
	public Board(Board b)
	{
		coins=new Coin[8][8];
		
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(b.coins[i][j]!=null)
					coins[i][j]=new Coin(b.coins[i][j].getType(),b.coins[i][j].getColor());;
		blackKings=b.blackKings;
		blackPieces=b.blackPieces;
		redKings=b.redKings;
		redPieces=b.redPieces;
	}
	
	public void PrintBoard()
	{
		System.out.println("  0 1 2 3 4 5 6 7");
		System.out.println(" -----------------");
		for(int i=0;i<8;i++)
		{
			System.out.print(i+"|");
			for(int j=0;j<8;j++)
			{
				if(coins[i][j]!=null)
				{
					if(coins[i][j].getColor()==Coin.BLACK)
					{
						if(coins[i][j].getType()==Coin.ORDINARY)
							System.out.print("x|");
						else
							System.out.print("X|");
					}
					else
					{
						if(coins[i][j].getType()==Coin.ORDINARY)
							System.out.print("o|");
						else
							System.out.print("O|");
					}
				}
				else
					System.out.print(" |");
			}
			System.out.println();
			System.out.println(" -----------------");
		}
		
	}
	
	public List<Board> listLegalTransitions(int color)
	{
		List<Board> output=new ArrayList<Board>();
		
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(coins[i][j]!=null&&coins[i][j].getColor()==color&&coins[i][j].getType()==Coin.ORDINARY)
				{
					addOrdinaryMoves(output,this,i,j,color);	
				}
				else if(coins[i][j]!=null&&coins[i][j].getColor()==color&&coins[i][j].getType()==Coin.KING)
				{
					addKingMoves(output,this,i,j,color);
				}
			}
		}
		boolean hasToAttack=false;
		for(Board b:output)
		if(b.attacking)
		{
			hasToAttack=true;
			break;
		}
		if(hasToAttack)
		{
			List<Board> temp=new ArrayList<Board>();
			for(Board b:output)
				if(!b.attacking)
					temp.add(b);
			output.removeAll(temp);
		}
		for(Board b:output)
			b.attacking=false;
		return output;
	}
	
	private void addKingMoves(List<Board> output,Board currentState,int x,int y,int color)
	{
		if(!currentState.attacking)
		{
			int x_arr[]={1,1,-1,-1};
			int y_arr[]={1,-1,1,-1};
			for(int i=0;i<4;i++)
				if(x+x_arr[i]>=0 && x+x_arr[i]<8 && y+y_arr[i]>=0 && y+y_arr[i]<8
					&& currentState.coins[x+x_arr[i]][y+y_arr[i]]==null)
				{
					Board temp=new Board(currentState);
					temp.coins[x+x_arr[i]][y+y_arr[i]]=temp.coins[x][y];
					temp.coins[x][y]=null;
					output.add(temp);
				}
		}
		
		
		int x_arr[]={2,2,-2,-2};
		int y_arr[]={2,-2,2,-2};
		int no_moves=0;
		for(int i=0;i<4;i++)
			if(x+x_arr[i]>=0 && x+x_arr[i]<8 && y+y_arr[i]>=0 && y+y_arr[i]<8
			    && currentState.coins[x+x_arr[i]][y+y_arr[i]]==null
				&& currentState.coins[x+x_arr[i]/2][y+y_arr[i]/2]!=null
				&& currentState.coins[x+x_arr[i]/2][y+y_arr[i]/2].getColor()!=color)
			{
				no_moves++;
				Board temp=new Board(currentState);
				if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getColor()==Coin.BLACK)
				{
					if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getType()==Coin.KING)
						temp.blackKings--;
					else
						temp.blackPieces--;
				}
				else
				{
					if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getType()==Coin.KING)
						temp.redKings--;
					else
						temp.redPieces--;
				}
				temp.coins[x+x_arr[i]/2][y+y_arr[i]/2]=null;
				temp.coins[x+x_arr[i]][y+y_arr[i]]=temp.coins[x][y];
				temp.coins[x][y]=null;
				temp.attacking=true;
				addKingMoves(output,temp,x+x_arr[i],y+y_arr[i],color);
			}
		
		if(no_moves==0&&currentState.attacking)
		{
			output.add(currentState);
		}
		
	}
	
	private void addOrdinaryMoves(List<Board> output,Board currentState,int x,int y,int color)
	{
		if(!currentState.attacking)
		{
			int y_arr[]={1,-1};
			int x_arr[]=new int[2];
			if(color==Coin.BLACK)
				x_arr[0]=x_arr[1]=1;
			else
				x_arr[0]=x_arr[1]=-1;
			
			for(int i=0;i<2;i++)
				if(x+x_arr[i]>=0 && x+x_arr[i]<8 && y+y_arr[i]>=0 && y+y_arr[i]<8
					&& currentState.coins[x+x_arr[i]][y+y_arr[i]]==null)
				{
					Board temp=new Board(currentState);
					temp.coins[x+x_arr[i]][y+y_arr[i]]=temp.coins[x][y];
					temp.coins[x][y]=null;
					
					if(temp.coins[x+x_arr[i]][y+y_arr[i]].getColor()==Coin.BLACK
							&&x+x_arr[i]==7)
					{
						temp.blackKings++;
						temp.blackPieces--;
						temp.coins[x+x_arr[i]][y+y_arr[i]].setType(Coin.KING);
					}
					else if(temp.coins[x+x_arr[i]][y+y_arr[i]].getColor()==Coin.RED
							&&x+x_arr[i]==0)
					{
						temp.redKings++;
						temp.redPieces--;
						temp.coins[x+x_arr[i]][y+y_arr[i]].setType(Coin.KING);
					}
					
					output.add(temp);
				}			
		}
		
		int y_arr[]={2,-2};
		int x_arr[]=new int[2];
		if(color==Coin.BLACK)
			x_arr[0]=x_arr[1]=2;
		else
			x_arr[0]=x_arr[1]=-2;
		
		int no_moves=0;
		for(int i=0;i<2;i++)
			if(x+x_arr[i]>=0 && x+x_arr[i]<8 && y+y_arr[i]>=0 && y+y_arr[i]<8
			    && currentState.coins[x+x_arr[i]][y+y_arr[i]]==null
				&& currentState.coins[x+x_arr[i]/2][y+y_arr[i]/2]!=null
				&& currentState.coins[x+x_arr[i]/2][y+y_arr[i]/2].getColor()!=color)
			{
				no_moves++;
				Board temp=new Board(currentState);
				
				if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getColor()==Coin.BLACK)
				{
					if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getType()==Coin.KING)
						temp.blackKings--;
					else
						temp.blackPieces--;
				}
				else
				{
					if(temp.coins[x+x_arr[i]/2][y+y_arr[i]/2].getType()==Coin.KING)
						temp.redKings--;
					else
						temp.redPieces--;
				}
				
				temp.coins[x+x_arr[i]/2][y+y_arr[i]/2]=null;
				temp.coins[x+x_arr[i]][y+y_arr[i]]=temp.coins[x][y];
				temp.coins[x][y]=null;
				temp.attacking=true;
				
				if(temp.coins[x+x_arr[i]][y+y_arr[i]].getColor()==Coin.BLACK
						&&x+x_arr[i]==7)
					{
					temp.blackKings++;
					temp.blackPieces--;
					temp.coins[x+x_arr[i]][y+y_arr[i]].setType(Coin.KING);
					}
				else if(temp.coins[x+x_arr[i]][y+y_arr[i]].getColor()==Coin.RED
						&&x+x_arr[i]==0)
				{
					temp.redKings++;
					temp.redPieces--;
					temp.coins[x+x_arr[i]][y+y_arr[i]].setType(Coin.KING);
				}
				
				addOrdinaryMoves(output,temp,x+x_arr[i],y+y_arr[i],color);
			}
		
		if(no_moves==0&&currentState.attacking)
			output.add(currentState);		
	}
	
	
	public boolean equals(Object o)
	{
		Board board;
		if(o.getClass()==Board.class)
			board=(Board)o;
		else
			return false;
		
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
			if(coins[i][j]!=null||board.coins[i][j]!=null)
				{
				if((coins[i][j]==null&&board.coins[i][j]!=null)||(coins[i][j]!=null&&board.coins[i][j]==null))
					return false;
				else if(coins[i][j].getColor()!=board.coins[i][j].getColor()||coins[i][j].getType()!=board.coins[i][j].getType())
					return false;
				}
			}
		}
		return true;
	}

	public Coin[][] getCoins() {
		return coins;
	}

	public void setCoins(Coin[][] coins) {
		this.coins = coins;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public void setBlackKings(int blackKings) {
		this.blackKings = blackKings;
	}

	public void setBlackPieces(int blackPieces) {
		this.blackPieces = blackPieces;
	}

	public void setRedKings(int redKings) {
		this.redKings = redKings;
	}

	public void setRedPieces(int redPieces) {
		this.redPieces = redPieces;
	}
}