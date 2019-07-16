import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import java.awt.geom.Ellipse2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;


public class Game2048 extends JPanel implements ActionListener, KeyListener
{
 //Variables
 private JLabel[][] grid;
 private int[][] numGrid;
 private JLabel title;
 private int score;
 private Timer timer;
 private Ai computer;
 private static int RIGHT=1, LEFT=2, UP=3,DOWN=4;


 public static void main(String[] args)
 {
  new Game2048();
 }

 public Game2048()
 {
  JFrame window = new JFrame("2048");
  window.setBackground(new Color(255, 206, 122));
  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  window.setTitle("2048");
  window.setSize(500,600);
  window.setLocationRelativeTo(null);
  window.setResizable(false);
  window.addKeyListener(this);

  Font f = new Font("Times Roman", Font.PLAIN, 57);
  score=0;
  //computer = new Ai(100);

  JPanel userInterface = new JPanel();
  userInterface.setLayout(new BorderLayout(20,10));
  userInterface.setBackground(new Color(255, 206, 122));

  JPanel titleBar = new JPanel();
  titleBar.setBackground(new Color(255, 206, 122));
  titleBar.setLayout(new FlowLayout());
  title = new JLabel("Play 2048");
  title.setFont(new Font("Times Roman", Font.PLAIN, 57));
  titleBar.add(title);

  JPanel gridArea = new JPanel();
  gridArea.setLayout(new GridLayout(4,4));
  grid = new JLabel[4][4];
  numGrid = new int[4][4];
  for(int i =0; i<4; i++)
  {
   for(int j = 0; j <4; j++)
   {
    numGrid[i][j]=0;
    grid[i][j]=new JLabel("" + numGrid[i][j], SwingConstants.CENTER);
    grid[i][j].setFont(f);
    grid[i][j].setBackground(Color.ORANGE);
    grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
    grid[i][j].setOpaque(true);
    gridArea.add(grid[i][j]);
   }
  }
  placeNewNumber();



  userInterface.add(gridArea, BorderLayout.CENTER);
  userInterface.add(titleBar, BorderLayout.NORTH);

  window.add(userInterface);
  window.setVisible(true);
  paintLabels();

  timer = new Timer(500, this);
  timer.setInitialDelay(10000);
  timer.start();
 }

 public void actionPerformed(ActionEvent e)
 {
  if(e.getSource()==timer)
  {
   if(notFull())
   {
    int[][] grid = new int[4][4];
    setGrid(grid,numGrid);
    boolean moveMade = makeMove(computer.getBestMove(grid, score));
    if(notFull()&&moveMade)
    {
     placeNewNumber();
    }
    paintLabels();
    System.out.println("Score:" + score);
   }
  }
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
 public void keyTyped(KeyEvent e){}


 public void keyPressed(KeyEvent e)
 {
  boolean moveMade=false;
  if(e.getKeyCode() == KeyEvent.VK_UP)
   moveMade=makeMove(UP);

  else if(e.getKeyCode() == KeyEvent.VK_DOWN)
   moveMade=makeMove(DOWN);

  else if(e.getKeyCode() == KeyEvent.VK_LEFT)
   moveMade=makeMove(LEFT);

  else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
   moveMade=makeMove(RIGHT);

  if(notFull()&&moveMade)
  {
   placeNewNumber();
  }

  paintLabels();
  System.out.println("Score:" + score);
 }


 public void keyReleased(KeyEvent e){}

    public boolean notFull()
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
 private void paintLabels()
 {
  for(int i =0; i<4; i++)
  {
   for(int j = 0; j <4; j++)
   {
    grid[i][j].setText("" + numGrid[i][j]);
    switch(numGrid[i][j])
    {
     case 0: grid[i][j].setBackground(Color.WHITE); break;
     case 2: grid[i][j].setBackground(new Color(88,209,77)); break;
     case 4: grid[i][j].setBackground(new Color(234, 242, 89)); break;
     case 8: grid[i][j].setBackground(new Color(242, 130, 89)); break;
     case 16: grid[i][j].setBackground(new Color(221, 88, 88)); break;
     case 32: grid[i][j].setBackground(new Color(219, 85, 183)); break;
     case 64: grid[i][j].setBackground(new Color(139, 85, 219)); break;
     case 128: grid[i][j].setBackground(new Color(85, 127, 219)); break;
     case 256: grid[i][j].setBackground(new Color(85, 217, 219)); break;
     case 512: grid[i][j].setBackground(new Color(52, 130, 72)); break;
     case 1024: grid[i][j].setBackground(new Color(155, 150, 6)); break;
     case 2048: grid[i][j].setBackground(new Color(155, 90, 6)); break;
    }
   }
  }
 }
 private void placeNewNumber()
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
 private boolean makeMove(int direction)
 {
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
  return moveMade;
 }

}