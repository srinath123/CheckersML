package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import datamodel.Board;
import datamodel.Coin;

public class Agent implements Player{
	int color;
	boolean learn;
	private List<Board> gamehistory;
	static private List<Board> permHistory=new ArrayList<Board>();
	public Agent(int color,boolean learn)
	{
		this.color=color;
		this.learn=learn;
		gamehistory=new ArrayList<Board>();
	}
	public int getColor() {
		return color;
	}
	
	public Board nextMove(Board board)
	{

		gamehistory.add(board);
		double max=-1e200;
		List<Board> legalMoves=board.listLegalTransitions(color);
		for(int i=0;i<legalMoves.size();i++)
		{
			double val=evaluation(legalMoves.get(i));
			if(val>max)
				max=val;
		}
		List<Board> potentialMoves=new ArrayList<Board>();
		for(Board b:legalMoves)
			if(evaluation(b)==max)
				potentialMoves.add(b);
		Random newRandom=new Random();
		Board temp=potentialMoves.get(Math.abs(newRandom.nextInt())%potentialMoves.size());
		
		if(color==Coin.BLACK)
			System.out.print("Black ");
		else
			System.out.print("Red ");
		int x[]=findParameters(temp);
		for(int i=0;i<6;i++)
			System.out.print(x[i]+" ");
		System.out.println();
		System.out.println(evaluation(temp));
		temp.PrintBoard();
		return temp;
	}
	
	int[] findParameters(Board board)
	{
		int x[]=new int[6];
		x[0]=board.getBlackPieces()+board.getBlackKings();
		x[1]=board.getBlackKings();
		List<Board> redLegal=board.listLegalTransitions(Coin.RED);
		int blackPiecesThreatened=0;
		for(Board b:redLegal)
			blackPiecesThreatened+=board.getBlackKings()+board.getBlackPieces()
									-b.getBlackKings()-b.getBlackPieces();
		x[2]=blackPiecesThreatened;
		
		x[3]=board.getRedPieces()+board.getRedKings();
		x[4]=board.getRedKings();
		
		List<Board> blackLegal=board.listLegalTransitions(Coin.BLACK);
		int redPiecesThreatened=0;
		for(Board b:blackLegal)
			redPiecesThreatened+=board.getRedKings()+board.getRedPieces()
									-b.getRedKings()-b.getRedPieces();
		x[5]=redPiecesThreatened;
		
		return x;
	}
	
	private double evaluation(Board board)
	{

		int[] x=findParameters(board);
		double[] W=new double[7];
			
			try{
				
			W=new double[7];
			BufferedReader br=new BufferedReader(new FileReader("weights.txt"));
			String temp[]=br.readLine().split(" ");
			for(int i=0;i<7;i++)
				W[i]=Double.parseDouble(temp[i]);
			br.close();
			}
			catch(IOException e)
			{
				return -1e200;
			}
			
			if(color==Coin.BLACK)
			{
				double result=0;
				result+=W[0]*x[0]+W[1]*x[1]+W[2]*x[2];//allies
				result+=W[3]*x[3]+W[4]*x[4]+W[5]*x[5];//opponents
				result+=W[6];
				return result;
			}
			else
			{
				double result=0;
				result+=W[0]*x[3]+W[1]*x[4]+W[2]*x[5];//allies
				result+=W[3]*x[0]+W[4]*x[1]+W[5]*x[2];//opponents
				result+=W[6];
				return result;
			}
	}
	
	public void gameover()
	{
	if(learn)
	{
		double sensitivity=0.001;
	try{	
	double W[]=new double[8];
	System.out.println("LOL");
	BufferedReader br=new BufferedReader(new FileReader("weights.txt"));
	String temp[]=br.readLine().split(" ");
	for(int i=0;i<7;i++)
		W[i]=Double.parseDouble(temp[i]);
	br.close();

	
	for(int i=0;i<gamehistory.size()-1;i++)
	{
		int[] x=findParameters(gamehistory.get(i));
		int add;
		if(color==Coin.BLACK)
			add=0;
		else
			add=3;
		W[6]=W[6]+sensitivity*(evaluation(gamehistory.get(i+1))-evaluation(gamehistory.get(i)));
		for(int j=0;j<6;j++)
			W[j]=W[j]+sensitivity*(evaluation(gamehistory.get(i+1))-evaluation(gamehistory.get(i)))*x[(j+add)%6];
	}
	BufferedWriter bw=new BufferedWriter(new FileWriter("weights.txt"));
	BufferedWriter bw2=new BufferedWriter(new FileWriter("ErrorPlot.txt",true));


	bw.write(W[0]+" "+W[1]+" "+W[2]+" "+W[3]+" "+W[4]+" "+W[5]+" "+W[6]+"\n");	
	permHistory.addAll(gamehistory);
	bw.close();
	double currError=0;
	
	for(int i=0;i<gamehistory.size()-1;i++)
		currError+=Math.pow(evaluation(gamehistory.get(i+1))-evaluation(gamehistory.get(i)),2);
	
	currError/=gamehistory.size();
	
	bw2.write(currError+" "+W[0]+" "+W[1]+" "+W[2]+" "+W[3]+" "+W[4]+" "+W[5]+" "+W[6]+"\n");
	bw2.close();
	gamehistory.clear();
	}
	catch(IOException e)
	{
		return;
	}
	}
	}
	
	double[] normalize(double[] W,double ceil)
	{
		double newW[]=new double[W.length];
		double max=0;
		for(int i=0;i<W.length;i++)
			{
			newW[i]=Math.abs(W[i]);
			if(max<newW[i])
				max=newW[i];
			}
		for(int i=0;i<newW.length;i++)
			newW[i]=newW[i]*ceil/max;
		for(int i=0;i<newW.length;i++)
			if(W[i]<0)
				newW[i]=-newW[i];
		return newW;
	}

}