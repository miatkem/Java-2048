import java.awt.Point;
import java.util.ArrayList;

public class Ai
{
    private int gameCount;
    private static int RIGHT=1, LEFT=2, UP=3,DOWN=4;
    
    public Ai(int gameCount)
    {
        this.gameCount=gameCount; //amount of simulated games in each direction (trueTotal = 4*gameCount)
    }
    
    public void setGameCount(int gc)
    {
        gameCount=gc;
    }
    
    //RETURNS THE MOST LIKELY BEST DIRECTION TO MOVE
    public int getBestMove(int[][] numGrid, int score)
    {
        int[][] temp = new int[4][4];
        setGrid(temp,numGrid);
        
        //find which direction produces the maximum points
        int maxPoint = 0;
        int direction=0;
        for(int i=1; i<5; i++)
        {
            temp=numGrid.clone();
            int points = runGames(i,temp,score); //simulate games (returns points)
            if(points>maxPoint){
                maxPoint=points;
                direction=i;
            }
        }
        return direction;
    }
    
    //SIMULATE GAME with direction as its first move 
    // and random moves to follow until it loses, GAMECOUNT amount of times
    private int runGames(int direction, int[][] numGrid, int score)
    {
        int totalScore = 0;
        int count=0;  
        
        while(count<gameCount) //loop gameCount iterations
        {
            //make first move in specified direction
            int[][] temp = new int[4][4];
            setGrid(temp,numGrid);
            if(direction == UP)
            {
                for(int i =3; i>0; i--)
                {
                    for(int j = 0; j <4; j++)
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
            else if(direction == DOWN)
            {
                for(int i =0; i<3; i++)
                {
                    for(int j = 0; j <4; j++)
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
            else if(direction == LEFT)
            {
                for(int i =0; i<4; i++)
                {
                    for(int j = 3; j >0; j--)
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
            else if(direction == RIGHT)
            {
                for(int i =0; i<4; i++)
                {
                    for(int j = 0; j<3; j++)
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
            totalScore+=finishGame(temp,score);
            count++;
        }    
        return totalScore;
    }
    
    //PLAY REMAINDER OF GAME MAKING RANDOM MOVES UNTIL GAME OVER
    public int finishGame(int[][] numberGrid, int score)
    {
        int[][] temp = new int[4][4];
        setGrid(temp,numberGrid);
        while(!gameOver(temp))
        {
            int direction = (int)(Math.random()*4)+1; //random directions
            boolean moveMade=false;
            if(direction == UP)
            {
                for(int i =3; i>0; i--)
                {
                    for(int j = 0; j <4; j++)
                    {
                        if(temp[i-1][j] == 0 && temp[i][j] != 0)
                        {
                            temp[i-1][j] = temp[i][j];
                            temp[i][j]=0;
                            moveMade=true;
                            i=4;
                            j=4;
                        }
                        else if(temp[i-1][j] == temp[i][j] && temp[i][j] != 0)
                        {
                            temp[i-1][j] = temp[i][j]*2;
                            score+=temp[i][j]*2;
                            temp[i][j]=0;
                            moveMade=true;
                            i=4;
                            j=4;
                        }
                    }
                }
            }
            else if(direction == DOWN)
            {
                for(int i =0; i<3; i++)
                {
                    for(int j = 0; j <4; j++)
                    {
                        if(temp[i+1][j] == 0 && temp[i][j] != 0)
                        {
                            temp[i+1][j] = temp[i][j];
                            temp[i][j]=0;
                            moveMade=true;
                            i=-1;
                            j=3;
                        }
                        else if(temp[i+1][j] == temp[i][j] && temp[i][j] != 0)
                        {
                            temp[i+1][j] = temp[i][j]*2;
                            score+=temp[i][j]*2;
                            temp[i][j]=0;
                            moveMade=true;
                            i=-1;
                            j=3;
                        }
                    }
                }
            }
            else if(direction == LEFT)
            {
                for(int i =0; i<4; i++)
                {
                    for(int j = 3; j >0; j--)
                    {
                        if(temp[i][j-1] == 0 && temp[i][j] != 0)
                        {
                            temp[i][j-1] = temp[i][j];
                            temp[i][j]=0;
                            moveMade=true;
                            j=4;
                        }
                        else if(temp[i][j-1] == temp[i][j] && temp[i][j] != 0)
                        {
                            temp[i][j-1] = temp[i][j]*2;
                            score+=temp[i][j]*2;
                            temp[i][j]=0;
                            moveMade=true;
                            j=4;
                        }
                    }
                }
            }
            else if(direction == RIGHT)
            {
                for(int i =0; i<4; i++)
                {
                    for(int j = 0; j<3; j++)
                    {
                        if(temp[i][j+1] == 0 && temp[i][j] != 0)
                        {
                            temp[i][j+1] = temp[i][j];
                            temp[i][j]=0;
                            moveMade=true;
                            j=-1;
                        }
                        else if(temp[i][j+1] == temp[i][j] && temp[i][j] != 0)
                        {
                            temp[i][j+1] = temp[i][j]*2;
                            score+=temp[i][j]*2;
                            temp[i][j]=0;
                            moveMade=true;
                            j=-1;
                        }
                    }
                }
            }
            if(notFull(temp)&&moveMade)
                placeNewNumber(temp);
        }
        
        return score;
    }
    
    //COPIES GRID-TWO TO GRID-ONE
    public void setGrid(int[][]gridOne, int[][] gridTwo)
    {
        for(int i =0; i<4; i++)
            for(int j = 0; j <4; j++)
                gridOne[i][j]=gridTwo[i][j];
    }
    
    //USED TO TEST IF GRID IS COMPLETELY FULL OF NONZEROES
    public boolean notFull(int[][] numGrid)
    {
        for(int i =0; i<4; i++)
            for(int j = 0; j <4; j++)
                if(numGrid[i][j]==0)
                    return true;
        return false;
    }
    
    //RETURN IF GAME IS OVER DUE TO NO MOVES BEING ALLOWED
    public boolean gameOver(int[][] numGrid)
    {
        for(int i =0; i<4; i++)
        {
            for(int j = 0; j <4; j++)
            {
                if(numGrid[i][j]==0)
                    return false;
                
                if(i>0 && numGrid[i][j]==numGrid[i-1][j])
                    return false;
                
                if(i<3 && numGrid[i][j]==numGrid[i+1][j])
                    return false;
                
                if(j>0 && numGrid[i][j]==numGrid[i][j-1])
                    return false;
                
                if(j<3 && numGrid[i][j]==numGrid[i][j+1])
                    return false;
            }
        }
        return true;
    }
    
    //PLACE A NEW NUMBER (2) RANDOMLY AT TILE WITH ZERO
    private void placeNewNumber(int[][] numGrid)
    {
        ArrayList<Point> used = new ArrayList<Point>();
        
        for(int i =0; i<4; i++)
            for(int j = 0; j <4; j++)
                if(numGrid[i][j]==0)
                    used.add(new Point(i,j));
        
        int ran = (int) (Math.random() * used.size());
        numGrid[(int)used.get(ran).getX()][(int)used.get(ran).getY()]=2;
    }
}