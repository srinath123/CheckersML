package activities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JFrame;

import datamodel.Board;
import datamodel.Coin;
import entities.Agent;

public class CheckersGUI extends JFrame implements MouseListener{
	
	private int state;
	private Agent agent;
	int sourceX,sourceY;
	

	private Board checkersBoard;
	private Board intermediateBoard;
	public CheckersGUI()
	{
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
		state=0;
		
		agent=new Agent(Coin.BLACK,false);
		setSize(320,350);
		setResizable(false);
		setVisible(true);
		paint(getGraphics());
		addMouseListener(this);
		state0handler();

	}
	
	void state0handler()
	{
		if(checkersBoard.listLegalTransitions(Coin.BLACK).size()==0)
		{
			System.out.println("Red Wins");
			setTitle("Red Wins.");
			paint(getGraphics());
			state=0;
			return;
		}
		checkersBoard=agent.nextMove(checkersBoard);
		if(checkersBoard.listLegalTransitions(Coin.RED).size()==0)
		{
			System.out.println("Black wins");
			setTitle("Black Wins.");
			paint(getGraphics());
			state=0;
			return;
		}
		paint(getGraphics(),checkersBoard);
		state=1;
	}
	
	void state1handler(int x,int y)
	{
		Coin atXY=checkersBoard.coins[x][y];
		if(atXY==null)
			return;
		if(atXY.getColor()==Coin.RED)
		{
			sourceX=x;
			sourceY=y;
			System.out.println("Highlighted "+x+" "+y);
		highlight(x,y,checkersBoard);
		state=2;
		System.out.println("State:"+state);
		}
	}
	
	void state2handler(int x,int y)
	{
		Coin atXY=checkersBoard.coins[x][y];
		
		if(atXY==null)
		{
			intermediateBoard=new Board(checkersBoard);
			if(Math.abs(x-sourceX)==1&&Math.abs(y-sourceY)==1)
			{
				intermediateBoard.coins[x][y]=intermediateBoard.coins[sourceX][sourceY];
				if(x==0)
					intermediateBoard.coins[x][y].setType(Coin.KING);
				intermediateBoard.coins[sourceX][sourceY]=null;
				
				if(islegalTransition(intermediateBoard, Coin.RED))
				{
					state=0;
					System.out.println("State:"+state);
					checkersBoard=intermediateBoard;
					state0handler();
				}
				else
					intermediateBoard=null;
			}
			else if(Math.abs(x-sourceX)==2&&Math.abs(y-sourceY)==2
					&&checkersBoard.coins[(x+sourceX)/2][(y+sourceY)/2]!=null
					&&checkersBoard.coins[(x+sourceX)/2][(y+sourceY)/2].getColor()==Coin.BLACK)
			{
				intermediateBoard.coins[x][y]=intermediateBoard.coins[sourceX][sourceY];
				intermediateBoard.coins[sourceX][sourceY]=null;
				intermediateBoard.coins[(x+sourceX)/2][(y+sourceY)/2]=null;
				if(x==0)
					intermediateBoard.coins[x][y].setType(Coin.KING);
				
				List<Board> legalMoves=checkersBoard.listLegalTransitions(Coin.RED);
				int intermediate=-1;
				for(Board b:legalMoves)
				{
					if(b.equals(intermediateBoard))
					{
						if(b.isIntermediate())
							intermediate=0;
						else
							intermediate=1;
						break;
					}
				}
				
				if(intermediate==0)
				{
					sourceX=x;
					sourceY=y;
					state=3;
					System.out.println("State:"+state);
					paint(getGraphics(),intermediateBoard);
					highlight(x,y,intermediateBoard);
				}
				else if(intermediate==1)
				{
					state=0;
					System.out.println("State:"+state);
					checkersBoard=intermediateBoard;
					state0handler();
				}
			}
		}
		else if(atXY.getColor()==Coin.RED)
			state1handler(x,y);
		else if(atXY.getColor()==Coin.BLACK)
			return;
	}
	
	void state3handler(int x,int y)
	{
		if(Math.abs(x-sourceX)==2&&Math.abs(y-sourceY)==2
				&&intermediateBoard.coins[(x+sourceX)/2][(y+sourceY)/2]!=null
				&&intermediateBoard.coins[(x+sourceX)/2][(y+sourceY)/2].getColor()==Coin.BLACK
				&&intermediateBoard.coins[x][y]==null)
		{
			intermediateBoard.coins[x][y]=intermediateBoard.coins[sourceX][sourceY];
			intermediateBoard.coins[sourceX][sourceY]=null;
			intermediateBoard.coins[(x+sourceX)/2][(y+sourceY)/2]=null;
			if(x==0)
				intermediateBoard.coins[x][y].setType(Coin.KING);
		
			List<Board> legalMoves=checkersBoard.listLegalTransitions(Coin.RED);
			int intermediate=-1;
			for(Board b:legalMoves)
			{
				if(b.equals(intermediateBoard))
				{
					if(b.isIntermediate())
						intermediate=0;
					else
						intermediate=1;
					break;
				}
			}
			
			if(intermediate==0)
			{
				sourceX=x;
				sourceY=y;
				state=3;
				paint(getGraphics(),intermediateBoard);
				highlight(x,y,intermediateBoard);
			}
			else if(intermediate==1)
			{
				state=0;
				checkersBoard=intermediateBoard;
				state0handler();
			}
		}
	}
	
	void highlight(int x,int y,Board board)
	{
		Graphics g=getGraphics();
		paint(g,board);
		g.setColor(Color.MAGENTA);
		for(int i=0;i<5;i++)
		{
			g.drawRect(y*40+i,x*40+i+28,40-2*i,40-2*i);
		}
	}
	
	private int convert(int x,int y)
	{
		if(y<28||y>348||x<0||x>320)
			return -1;
		return ((y-28)/40)*8+x/40;
	}
	
	public void paint(Graphics g,Board board)
	{
		g.clearRect(0, 0, 350, 350);
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if((i+j)%2!=0)
				{
					g.fillRect(i*40, 28+j*40, 40, 40);
				}
			}
		}
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(board.coins[i][j]==null)
					continue;
				else if(board.coins[i][j].getColor()==Coin.BLACK)
					{
						if(board.coins[i][j].getType()==Coin.KING)
						{
							g.setColor(Color.BLACK);
							g.fillOval(5+j*40, 33+i*40, 30, 30);//j is height,i is width
						}
						else
						{
							g.setColor(Color.BLACK);
							g.fillOval(10+j*40, 38+i*40, 20, 20);
						}
					}
				else if(board.coins[i][j].getColor()==Coin.RED)
				{
					if(board.coins[i][j].getType()==Coin.KING)
					{
						g.setColor(Color.RED);
						g.fillOval(5+j*40, 33+i*40, 30, 30);//j is height,i is width
					}
					else
					{
						g.setColor(Color.RED);
						g.fillOval(10+j*40, 38+i*40, 20, 20);
					}
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent event) {
		
		int temp=convert(event.getX(), event.getY());
		int x=temp/8,y=temp%8;
		
		switch(state)
		{
		case 1:
			state1handler(x,y);
		case 2:
			state2handler(x,y);
		case 3:
			state3handler(x,y);
		}
		
	}
	
	private boolean islegalTransition(Board board,int color)
	{
		for(Board b:checkersBoard.listLegalTransitions(color))
			if(b.equals(board)&&!b.isIntermediate())
				return true;
		return false;
	}
	
	
	public void mouseEntered(MouseEvent arg0) {	
	}

	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
}