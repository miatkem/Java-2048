public class Ai
{
	private int gameCount;
	private static int RIGHT=1, LEFT=2, UP=3,DOWN=4;

	public Ai(int gameCount)
	{
		this.gameCount=gameCount;
		if(gameCount<4)
			gameCount=4;
		gameCount=gameCount/4;
	}

	public int getBestMove(int[][] numGrid, int score)
	{
		int[][] temp = new int[4][4];
		setGrid(temp,numGrid);
		int rightAvg = runGames(RIGHT, temp,score);
		temp = numGrid.clone();
		int leftAvg = runGames(LEFT, temp,score);
		temp = numGrid.clone();
		int upAvg = runGames(UP, temp, score);
		temp = numGrid.clone();
		int downAvg = runGames(DOWN, temp, score);

		if(rightAvg>upAvg && rightAvg>leftAvg && rightAvg>downAvg)
			return RIGHT;
		else if(leftAvg>upAvg && leftAvg>rightAvg && leftAvg>downAvg)
			return LEFT;
		else if(upAvg>rightAvg && upAvg>leftAvg && upAvg>downAvg)
			return UP;
		else
			return DOWN;
	}

	private int runGames(int direction, int[][] numGrid, int score)
	{
		int totalScore = 0;
		int count=0;
		while(count<gameCount)
		{
		int[][] temp = new int[4][4];
		setGrid(temp,numGrid);
		if(direction == UP)
		{
			for(int i =3; i>-1; i--)
			{
				for(int j = 0; j <4; j++)
				{

					if(i>0)
					{
						if(temp[i-1][j] == 0 && temp[i][j] != 0)
						{
							temp[i-1][j] = temp[i][j];
							temp[i][j]=0;
							i=4;
							j=4;
						}
						else if(temp[i-1][j] == temp[i][j] && temp[i][j] != 0)
						{
							temp[i-1][j] = temp[i][j]*2;
							score+=temp[i][j]*2;
							temp[i][j]=0;
							i=4;
							j=4;
						}
					}
				}

			}

			totalScore=finishGame(temp,score);
		}
		else if(direction == DOWN)
		{
			for(int i =0; i<4; i++)
			{
				for(int j = 0; j <4; j++)
				{
					if(i<3)
					{
						if(temp[i+1][j] == 0 && temp[i][j] != 0)
						{
							temp[i+1][j] = temp[i][j];
							temp[i][j]=0;
							i=-1;
							j=3;
						}
						else if(temp[i+1][j] == temp[i][j] && temp[i][j] != 0)
						{
							temp[i+1][j] = temp[i][j]*2;
							score+=temp[i][j]*2;
							temp[i][j]=0;
							i=-1;
							j=3;
						}
					}
				}
			}
			totalScore=finishGame(temp,score);
		}
		else if(direction == LEFT)
		{
			for(int i =0; i<4; i++)
			{
				for(int j = 3; j >-1; j--)
				{
					if(j>0)
					{
						if(temp[i][j-1] == 0 && temp[i][j] != 0)
						{
							temp[i][j-1] = temp[i][j];
							temp[i][j]=0;
							j=4;
						}
						else if(temp[i][j-1] == temp[i][j] && temp[i][j] != 0)
						{
							temp[i][j-1] = temp[i][j]*2;
							score+=temp[i][j]*2;
							temp[i][j]=0;
							j=4;
						}
					}
				}
			}
			totalScore=finishGame(temp,score);
		}
		else if(direction == RIGHT)
		{
			for(int i =0; i<4; i++)
			{
				for(int j = 0; j<4; j++)
				{
					if(j<3)
					{
						if(temp[i][j+1] == 0 && temp[i][j] != 0)
						{
							temp[i][j+1] = temp[i][j];
							temp[i][j]=0;
							j=-1;
						}
						else if(temp[i][j+1] == temp[i][j] && temp[i][j] != 0)
						{
							temp[i][j+1] = temp[i][j]*2;
							score+=temp[i][j]*2;
							temp[i][j]=0;
							j=-1;
						}
					}
				}
			}
			totalScore=finishGame(temp,score);
		}
		count++;
		}//Closes While
		return totalScore;
	}

	public int finishGame(int[][] numberGrid, int score)
	{
		int[][] numGrid = new int[4][4];
		setGrid(numGrid,numberGrid);
		while(notFull(numGrid))
		{
			int direction = (int)(Math.random()*4)+1;
			boolean moveMade=false;
			if(direction == UP)
			{
				for(int i =3; i>-1; i--)
				{
					for(int j = 0; j <4; j++)
					{
						if(i>0)
						{
							if(numGrid[i-1][j] == 0 && numGrid[i][j] != 0)
							{
								numGrid[i-1][j] = numGrid[i][j];
								numGrid[i][j]=0;
								moveMade=true;
								i=4;
								j=4;
							}
							else if(numGrid[i-1][j] == numGrid[i][j] && numGrid[i][j] != 0)
							{
								numGrid[i-1][j] = numGrid[i][j]*2;
								score+=numGrid[i][j]*2;
								numGrid[i][j]=0;
								moveMade=true;
								i=4;
								j=4;
							}
						}
					}
				}
			}
			else if(direction == DOWN)
			{
				for(int i =0; i<4; i++)
				{
					for(int j = 0; j <4; j++)
					{
						if(i<3)
						{
							if(numGrid[i+1][j] == 0 && numGrid[i][j] != 0)
							{
								numGrid[i+1][j] = numGrid[i][j];
								numGrid[i][j]=0;
								moveMade=true;
								i=-1;
								j=3;
							}
							else if(numGrid[i+1][j] == numGrid[i][j] && numGrid[i][j] != 0)
							{
								numGrid[i+1][j] = numGrid[i][j]*2;
								score+=numGrid[i][j]*2;
								numGrid[i][j]=0;
								moveMade=true;
								i=-1;
								j=3;
							}
						}
					}
				}
			}
			else if(direction == LEFT)
			{
				for(int i =0; i<4; i++)
				{
					for(int j = 3; j >-1; j--)
					{
						if(j>0)
						{
							if(numGrid[i][j-1] == 0 && numGrid[i][j] != 0)
							{
								numGrid[i][j-1] = numGrid[i][j];
								numGrid[i][j]=0;
								moveMade=true;
								j=4;
							}
							else if(numGrid[i][j-1] == numGrid[i][j] && numGrid[i][j] != 0)
							{
								numGrid[i][j-1] = numGrid[i][j]*2;
								score+=numGrid[i][j]*2;
								numGrid[i][j]=0;
								moveMade=true;
								j=4;
							}
						}
					}
				}
			}
			else if(direction == RIGHT)
			{
				for(int i =0; i<4; i++)
				{
					for(int j = 0; j<4; j++)
					{
						if(j<3)
						{
							if(numGrid[i][j+1] == 0 && numGrid[i][j] != 0)
							{
								numGrid[i][j+1] = numGrid[i][j];
								numGrid[i][j]=0;
								moveMade=true;
								j=-1;
							}
							else if(numGrid[i][j+1] == numGrid[i][j] && numGrid[i][j] != 0)
							{
								numGrid[i][j+1] = numGrid[i][j]*2;
								score+=numGrid[i][j]*2;
								numGrid[i][j]=0;
								moveMade=true;
								j=-1;
							}
						}
					}
				}
			}
			if(notFull(numGrid)&&moveMade)
			{
						placeNewNumber(numGrid);
			}
		}

		return score;
	}
	public void setGrid(int[][]gridOne, int[][] gridTwo)
	{
		for(int i =0; i<4; i++)
		{
			for(int j = 0; j <4; j++)
			{
				gridOne[i][j]=gridTwo[i][j];
			}
		}
	}

	public boolean notFull(int[][] numGrid)
	{
		for(int i =0; i<4; i++)
		{
			for(int j = 0; j <4; j++)
			{
				if(numGrid[i][j]==0)
				{
					return true;
				}
			}
		}

		return false;
	}
	private void placeNewNumber(int[][] numGrid)
	{
		boolean valuePlaced=false;
		int ranX, ranY;
		while(!valuePlaced)
		{
			ranX = (int) (Math.random() * 4);
			ranY = (int) (Math.random() * 4);


			if(numGrid[ranX][ranY]==0)
			{
				numGrid[ranX][ranY]=2;
				valuePlaced=true;
			}
		}
	}
}