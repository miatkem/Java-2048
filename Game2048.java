import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import java.awt.geom.Ellipse2D;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;
import java.util.ArrayList;


public class Game2048 extends JPanel implements ActionListener, KeyListener, ChangeListener
{
    //GUI OBEJECTS
    private JButton aiOnOff, reset;
    private JSlider aiSpeedSlider, aiDepthSlider;
    private JLabel[][] grid;
    private int[][] numGrid;
    private JLabel title;
    //VARIABLES
    private static int RIGHT=1, LEFT=2, UP=3,DOWN=4;
    private int score;
    private Timer timer;
    private Ai computer;
    private boolean aiOn;
    private int aiDepth, aiSpeed;
    
    public static void main(String[] args) { new Game2048(); }
    
    public Game2048()
    {
        //setup jframe window
        JFrame window = new JFrame("2048");
        window.setBackground(new Color(255, 206, 122));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("2048");
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setFocusable(true);
        window.addKeyListener(this); //key listener
        
        //program variables
        score=0;
        aiOn=false;
        aiSpeed=100;
        aiDepth=500;
        computer = new Ai(aiDepth);
        timer = new Timer(aiSpeed, this);
        timer.setInitialDelay(10);
        
        //create main jpanel
        JPanel userInterface = new JPanel();
        userInterface.setLayout(new BorderLayout(20,10));
        userInterface.setBackground(new Color(255, 206, 122));
        
        //create user controls subpanel
        JPanel aiControls = new JPanel();
        JLabel aiSpeedlbl= new JLabel("AI Run Speed:");
        aiControls.add(aiSpeedlbl);
        aiSpeedSlider=new JSlider(JSlider.HORIZONTAL,1, 999, 999-aiSpeed);
        aiSpeedSlider.setFocusable(false);
        aiSpeedSlider.setName("speedSlider");
        aiSpeedSlider.addChangeListener(this); //speed slider
        aiControls.add(aiSpeedSlider);
        aiOnOff=new JButton("Turn AI On");
        aiOnOff.setFocusable(false);
        aiOnOff.addActionListener(this);
        aiControls.add(aiOnOff);
        reset=new JButton("Reset");
        reset.setFocusable(false);
        reset.addActionListener(this);
        aiControls.add(reset);
        JLabel aiDepthlbl = new JLabel("AI Prediction Depth:");
        aiControls.add(aiDepthlbl);
        aiDepthSlider=new JSlider(JSlider.HORIZONTAL,1, 2000, aiDepth);
        aiDepthSlider.setFocusable(false);
        aiDepthSlider.setName("depthSlider");
        aiDepthSlider.addChangeListener(this); //depth slider
        aiControls.add(aiDepthSlider);
        
        //create title subpanel
        JPanel titleBar = new JPanel();
        titleBar.setBackground(new Color(255, 206, 122));
        titleBar.setLayout(new FlowLayout());
        title = new JLabel("Play 2048");
        title.setFont(new Font("Times Roman", Font.PLAIN, 57));
        titleBar.add(title);
        
        //create grid area for tiles
        JPanel gridArea = new JPanel();
        gridArea.setLayout(new GridLayout(4,4));
        
        //setup grid of labels and num
        Font f = new Font("Times Roman", Font.PLAIN, 57);
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
                grid[i][j].setPreferredSize(new Dimension(175,175));
                grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                grid[i][j].setOpaque(true);
                gridArea.add(grid[i][j]);
            }
        }
        
        //add subpanels to main panel
        userInterface.add(gridArea, BorderLayout.CENTER);
        userInterface.add(titleBar, BorderLayout.NORTH);
        userInterface.add(aiControls, BorderLayout.SOUTH);
        
        //finish program variable setup
        placeNewNumber();
        paintLabels();
        
        //finish window setup
        window.add(userInterface);
        window.pack();
        window.setVisible(true);
    }
    
    //EVENT LISTENER (timer and buttons)
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==timer) //ai timer - ticks ai and updates screen
        {
            if(!gameOver(numGrid))
            {
                int[][] grid = new int[4][4];
                setGrid(grid,numGrid);
                boolean moveMade = makeMove(computer.getBestMove(grid, score));
                if(!gameOver(numGrid)&&moveMade)
                    placeNewNumber();
                paintLabels();
            }
        }
        
        if(e.getSource() == aiOnOff) //ai button - turns on and off ai timer
        {
            if(aiOn==false)
            {
                aiOn=true;
                aiOnOff.setText("Turn AI Off");
                timer.start();
            }
            
            else if(aiOn==true)
            {
                aiOn=false;
                aiOnOff.setText("Turn AI On");
                timer.stop();
            }
        }
        
        if(e.getSource() == reset) //reset button - stops ai, resets program variables
        {
            if(aiOn==true)
            {
                aiOn=false;
                aiOnOff.setText("Turn AI On");
                timer.stop();
            }
            computer = new Ai(aiDepth);
            for(int i =0; i<4; i++)
                for(int j = 0; j <4; j++)
                    numGrid[i][j]=0;
            score=0;
            placeNewNumber();
            paintLabels();
        }
    }
    
    //KEY LISTENER (user tile movement) - arrows push tiles in different directions
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
        
        //place new number if move was made
        if(!gameOver(numGrid)&&moveMade)
            placeNewNumber();
        paintLabels();
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    
    //SLIDER LISTENER (ai speed and depth) - adjusts the ai variables
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting())
        {
            //Changes speed of ai timer
            if(source.getName().equals("speedSlider")) 
            {
                aiSpeed=1000-source.getValue();
                timer.stop();
                timer.setDelay(aiSpeed);
                if(aiOn)
                    timer.start();
            }
            //Changes the amt of simulated games
            if(source.getName().equals("depthSlider")) 
            {
                aiDepth=source.getValue();
                computer.setGameCount(aiDepth);
            }
        }
    }
    
    //COPIES GRID- TWO TO GRID-ONE
    public void setGrid(int[][]gridOne, int[][] gridTwo)
    {
        for(int i =0; i<4; i++)
            for(int j = 0; j <4; j++)
                gridOne[i][j]=gridTwo[i][j];
    }
    
    //CHECKS IF MOVES CAN STILL BE MADE
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
    
    //UPDATE LABELS - painted according to number value
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
                    case 4096: grid[i][j].setBackground(new Color(30, 110, 20)); break;
                }
            }
        }
    }
    
    //PLACE A NEW NUMBER (2) RANDOMLY AT TILE WITH ZERO
    private void placeNewNumber()
    {
        ArrayList<Point> used = new ArrayList<Point>();
        
        for(int i =0; i<4; i++)
            for(int j = 0; j <4; j++)
                if(numGrid[i][j]==0)
                    used.add(new Point(i,j));
        
        int ran = (int) (Math.random() * used.size());
        numGrid[(int)used.get(ran).getX()][(int)used.get(ran).getY()]=2;
    }
    
    //MAKE MOVE IN A DIRECTION - update grid in response to directional move
    private boolean makeMove(int direction)
    {
        boolean moveMade=false;
        
        if(direction == UP)
        {
            for(int i =3; i>0; i--)
            {
                for(int j = 0; j <4; j++)
                {
                    if(numGrid[i-1][j] == 0 && numGrid[i][j] != 0) //move tile
                    {
                        numGrid[i-1][j] = numGrid[i][j];
                        numGrid[i][j]=0;
                        moveMade=true;
                        i=4;
                        j=4;
                    }
                    else if(numGrid[i-1][j] == numGrid[i][j] && numGrid[i][j] != 0) //combine tiles
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
        
        else if(direction == DOWN)
        {
            for(int i =0; i<3; i++)
            {
                for(int j = 0; j <4; j++)
                {
                    if(numGrid[i+1][j] == 0 && numGrid[i][j] != 0) //move tile
                    {
                        numGrid[i+1][j] = numGrid[i][j];
                        numGrid[i][j]=0;
                        moveMade=true;
                        i=-1;
                        j=3;
                    }
                    else if(numGrid[i+1][j] == numGrid[i][j] && numGrid[i][j] != 0) //combine tiles
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
        
        else if(direction == LEFT)
        {
            for(int i =0; i<4; i++)
            {
                for(int j = 3; j >0; j--)
                {
                    
                    if(numGrid[i][j-1] == 0 && numGrid[i][j] != 0) //move tile
                    {
                        numGrid[i][j-1] = numGrid[i][j];
                        numGrid[i][j]=0;
                        moveMade=true;
                        j=4;
                    }
                    else if(numGrid[i][j-1] == numGrid[i][j] && numGrid[i][j] != 0) //combine tiles
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
        
        else if(direction == RIGHT)
        {
            for(int i =0; i<4; i++)
            {
                for(int j = 0; j<3; j++)
                {
                    if(numGrid[i][j+1] == 0 && numGrid[i][j] != 0) //move tile
                    {
                        numGrid[i][j+1] = numGrid[i][j];
                        numGrid[i][j]=0;
                        moveMade=true;
                        j=-1;
                    }
                    else if(numGrid[i][j+1] == numGrid[i][j] && numGrid[i][j] != 0) //combine tiles
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
        return moveMade;
    }
}
