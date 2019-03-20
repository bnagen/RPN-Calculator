/*  RPNForm.java */

package RPN;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.swing.*;
/**
 * File Name RPNForm.java
 * Description This is the driver class for the program which will create
 * the GUI for the Reverse Polish Notation calculator allowing the user to
 * select which numbers and operations they wish to perform.
 * @author Branavan Nagendiram
 * Date: 5/8/2017
 * Platform: Windows 10, jdk 1.8.0_66, NetBeans 8.2 i7 cpu
 */
public class RPNForm extends JFrame
{
    public static final int FRAME_WIDTH = 660;
    public static final int FRAME_HEIGHT = 330;
    public static final String MACROFILE = "macroFile.txt";
    public static final Color DIGIT_BACKGROUND = new Color(0xf0, 0xf6, 0xff);
    
    private Container contentPane;
    private JPanel displayPanel;
    private JLabel RPNLabel; 
    private JTextField displayTextField;
    private JPanel buttonPanel;
    private JButton[][] buttonGrid;
    
    private RPNCalculator calc;
    private boolean helpMode = false;
    private boolean recordMode = false;
    private boolean msgOn = false;
    private boolean commandPerformed = false;
    private boolean getOn = false;
    private boolean setOn = false;
    private String inString = "";
    private String displayString = "";
        
    /**
     * Creates and displays a window of the class RPNClaculator.
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        RPNForm gui = new RPNForm();
        gui.setVisible(true);
    }
    
    /**
     * constructor -- set up the form, and gui size
     */
    public RPNForm()
    {  
        calc = new RPNCalculator();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("  RPN Calculator");
        setLocation(40, 40);
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        setDisplayPanel();
    }
    
    /**
     * sets up the displayPanel
     */
    public final void setDisplayPanel()
    {
        /**
         * inner class -- listens for any button actions
         */
        class StatusListener implements ActionListener
        {
            /**
            * deal with an action
            * @param event --the actionEvent performed
            */
            @Override
            public void actionPerformed(ActionEvent event)
            { 
                dealWith(event.getActionCommand());
                displayTextField.requestFocusInWindow();
            }  
        }
        
        /**
         * inner class -- listens for any button actions
         */
        class DisplayListener implements KeyListener
        {
            /**
            * not implemented
            * @param event --the actionEvent performed
            */
            @Override
            public void keyPressed(KeyEvent event)
            {}
            /**
            * deal with a keystroke
            * @param event --the actionEvent performed
            */
            @Override
            public void keyReleased(KeyEvent event)
            {
                char c = event.getKeyChar();
                if(event.getKeyCode() == KeyEvent.VK_SHIFT)
                    return;
                if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        event.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    displayString = displayTextField.getText();
                    return;
                }
                
                displayTextField.setText(displayString);
                if(validChar(c))
                    dealWith(String.valueOf(c)); 
                displayTextField.requestFocusInWindow();
            }
            /**
            * not implemented
            * @param event --the actionEvent performed
            */
            @Override
            public void keyTyped(KeyEvent event)
            {}   
        }
        KeyListener displayListener = new DisplayListener();
        ActionListener buttonListener = new StatusListener();

        
        displayPanel = new JPanel( );
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
        displayPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
                new Color(0XCC, 0X99, 0XFF)));   
        RPNLabel = new JLabel(" RPN ");
        RPNLabel.setBackground(new Color(0X33, 0X00, 0X66));
        RPNLabel.setFont(new Font("Courier New", 1, 36));
        RPNLabel.setForeground(new Color(0x66, 0x33, 0x66));      
        displayPanel.add(RPNLabel);

        displayTextField = new JTextField("");
        displayTextField.setFont(new Font("Courier New", 1, 24));
        displayTextField.setHorizontalAlignment(JTextField.RIGHT);
        displayTextField.setActionCommand("Enter");
        displayTextField.addKeyListener(displayListener);
        displayPanel.add(displayTextField);
        contentPane.add(displayPanel, BorderLayout.NORTH);        

        buttonPanel = new JPanel( );
        buttonPanel.setBackground(new Color(0xff, 0xef, 0xdf));
        buttonPanel.setLayout(new GridLayout(RPNCalculator.ROWS,
                RPNCalculator.COLS));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
                new Color(0X99, 0XFF, 0XFF)));
        buttonGrid = new JButton[RPNCalculator.ROWS][RPNCalculator.COLS];

        for(int i = 0; i < RPNCalculator.ROWS; i++)
        {
            for(int j = 0; j < RPNCalculator.COLS; j++)
            {
                buttonGrid[i][j]  = new JButton();
                buttonGrid[i][j].setFont(new Font("Courier New", 1, 16));
                buttonGrid[i][j].addActionListener(buttonListener);
                buttonGrid[i][j].setBorder(BorderFactory.createBevelBorder(1));
                buttonPanel.add(buttonGrid[i][j]);
            }
        }
        buttonGrid[0][0].setText("Exit");
        buttonGrid[0][1].setText("C");
        buttonGrid[0][2].setText("CE");
        buttonGrid[0][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][3].setBackground(DIGIT_BACKGROUND );
        buttonGrid[0][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][3].setText("7");
        buttonGrid[0][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][4].setBackground(DIGIT_BACKGROUND );
        buttonGrid[0][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][4].setText("8");
        buttonGrid[0][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][5].setBackground(DIGIT_BACKGROUND );
        buttonGrid[0][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][5].setText("9");
        buttonGrid[0][6].setText("+");
        buttonGrid[0][7].setText("x");
        buttonGrid[1][0].setText("Set");
        buttonGrid[1][1].setText("Get");
        buttonGrid[1][2].setText("Up");
        buttonGrid[1][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][3].setBackground(DIGIT_BACKGROUND );
        buttonGrid[1][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][3].setText("4");
        buttonGrid[1][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][4].setBackground(DIGIT_BACKGROUND );
        buttonGrid[1][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][4].setText("5");
        buttonGrid[1][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][5].setBackground(DIGIT_BACKGROUND );
        buttonGrid[1][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][5].setText("6");
        buttonGrid[1][6].setText("-");
        buttonGrid[1][7].setText("/");
        buttonGrid[2][0].setText("Load");
        buttonGrid[2][1].setText("Save");
        buttonGrid[2][2].setText("Down");
        buttonGrid[2][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][3].setBackground(DIGIT_BACKGROUND );
        buttonGrid[2][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][3].setText("1");
        buttonGrid[2][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][4].setBackground(DIGIT_BACKGROUND );
        buttonGrid[2][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][4].setText("2");
        buttonGrid[2][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][5].setBackground(DIGIT_BACKGROUND );
        buttonGrid[2][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][5].setText("3");
        buttonGrid[2][6].setText("^");
        buttonGrid[2][7].setText("%");
        buttonGrid[3][0].setText("Rec");
        buttonGrid[3][1].setText("Run");
        buttonGrid[3][2].setText("?");
        buttonGrid[3][3].setText("+/-");
        buttonGrid[3][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[3][4].setBackground(DIGIT_BACKGROUND );
        buttonGrid[3][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[3][4].setText("0");
        buttonGrid[3][5].setText(".");
        buttonGrid[3][6].setText("1/n");
        buttonGrid[3][7].setFont(new Font("Courier New", 1, 15));
        buttonGrid[3][7].setBackground(new Color(0xf6, 0xf0, 0xff));
        buttonGrid[3][7].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[3][7].setText("Enter");

        contentPane.add(buttonPanel, BorderLayout.CENTER);
        disableAlpha(); 
    }
    
    /**
     * Description This method will deal with the action that the user selected and perform
     * the selected action.
     * @param actionCommand --the actionEvent performed by the user
     */
    public void dealWith(String actionCommand)
    {
        if(msgOn)
        {
            msgOn = false;
            displayTextField.setForeground(Color.BLACK);
            if(recordMode)
                displayTextField.setForeground(Color.pink);
            displayTextField.setText("");
        }
        try
        {
            if(helpMode)
            {
                displayHelp(actionCommand);
                helpMode = false;
                return;
            }
            else
                inString = displayTextField.getText();
            if(getOn)
            {
                getOn = false;
                if(Character.isDigit(actionCommand.charAt(0)))
                {
                    int indexOfRegister = Integer.parseInt(actionCommand);
                    displayTextField.setText(Double.toString(calc.registers[indexOfRegister]));
                }
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                return;
            }
            if(setOn)
            {
                setOn = false;
                if(Character.isDigit(actionCommand.charAt(0)))
                {
                    int indexOfRegister = Integer.parseInt(actionCommand);
                    calc.registers[indexOfRegister] = Double.parseDouble(inString);
                
                }
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                return;
            }
            if(actionCommand.equals("?"))
            {
                helpMode = true;
                displayTextField.setForeground(new Color(0, 0X99, 0X66));
                displayTextField.setText(inString = "?");
            }
            else if(actionCommand.equals("Exit"))
            {
                    System.exit(0);
            }
            else if(actionCommand.equals("Save"))
            {
               if(!recordMode)
               {
                   saveFile();
               }
            }
            else if(actionCommand.equals("Load"))
            {
               if(!recordMode)
               {
                   loadFile();
               }
            }
            else if(actionCommand.equals("Rec"))
            {
                if(!recordMode)
                {
                    recordMode = true;
                    calc.instructions.clear();
                    displayTextField.setForeground(Color.PINK);
                    buttonGrid[3][0].setForeground(Color.PINK);
                }
                else
                {
                    recordMode = false;
                    displayTextField.setForeground(Color.BLACK);
                    buttonGrid[3][0].setForeground(Color.BLACK);
                }
            }
            else if(actionCommand.equals("Run"))
            {
                if(recordMode)
                {
                    recordMode = false;
                    displayTextField.setForeground(Color.BLACK);
                    buttonGrid[3][0].setForeground(Color.BLACK);
                }
                runFile();
            }
            else if(actionCommand.equals("C")
                    || actionCommand.equals("c"))
            {  
               
                commandPerformed = true;
                calc.theStack.clear();
                displayTextField.setText("");
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
            }
            else if(actionCommand.equals("CE"))
            {
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                commandPerformed = true;
                if(calc.theStack.isEmpty())
                {
                    displayTextField.setText("");
                }
                else 
                {
                    calc.theStack.removeFirst();
                    displayTextField.setText(Double.toString(calc.theStack.peekFirst()));
                }    
                
            }
            else if(actionCommand.equals("Get"))
            {           
               getOn = true;
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
            }
            else if(actionCommand.equals("Set"))
            {           
               setOn = true;
               if(recordMode)
               {
                  calc.instructions.add(actionCommand);
               }
            }
            else if(actionCommand.equals("Enter")
                    || actionCommand.equals("\n"))
            {     
              if(recordMode)
              {
                calc.instructions.add(actionCommand);
              }
              numberInputted(inString);
              commandPerformed = true;
            }
            else if (actionCommand.equals("1/n"))                 
            {
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                inverseNumber(inString);
            }
            else if(actionCommand.equals("+/-"))
            {
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                if(!inString.equals(""))
                {
                   if(!inString.substring(0,1).equals("-"))
                   {
                       displayTextField.setText("-" + inString);
                   }
                   else
                   {
                       inString = inString.substring(1);
                       displayTextField.setText(inString);
                   }
                }
            }
            else if (actionCommand.equals("."))                 
            {     
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
                if(displayTextField.getText().equals(""))
                {
                    displayTextField.setText("0.");
                }
                else if(!Double.isNaN(Double.parseDouble(displayTextField.getText())))
                {
                    displayTextField.setText(displayTextField.getText() + ".");
                }
            }
            else if(Character.isDigit(actionCommand.charAt(0)))    
            {     
               
               if(commandPerformed)
               {
                   inString = "";
                   commandPerformed = false;
                   displayTextField.setText(inString);
               }
               if(displayTextField.getText().equals("") ||
                !Double.isNaN(Double.parseDouble(displayTextField.getText())))
               {
                   displayTextField.setText(inString + actionCommand);
               }
               else
               {
                   displayTextField.setText(inString);
               }
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
            }
            else
            {      
                if(!commandPerformed)
                {
                    numberInputted(inString);
                }
                if(calc.buttonPressed(actionCommand))
                {
                    displayTextField.setText(calc.theStack.peekFirst().toString());
                }
                else
                {
                    displayTextField.setText("ERROR");
                    displayTextField.setForeground(Color.pink);
                    msgOn = true;
                }
                if(recordMode)
                {
                    calc.instructions.add(actionCommand);
                }
            }
         
        }
        catch(Exception exp)
        {
           displayTextField.setText("");
           
        }
       
        
    } 
    /**
     * Takes in a string containing a double and changes the value to the inverse
     * of said number
     * @param input String containing value
     */
    private void inverseNumber(String input)
    {
        double value = 0.0;
        if(Double.isNaN(Double.parseDouble(input)))
        {
            displayTextField.setText("");
        }
        else if(!displayTextField.getText().isEmpty())
        {
            value = Double.parseDouble(input);
            if(value == 0.0)
            {
                displayTextField.setText("ERROR");
            }
            else
            {
                value = 1 / value;
                displayTextField.setText(Double.toString(value));
            }
        }
    }
    /**
     * Description This method will take in the number the user selected as
     * a String and convert that into a double if it cannot be converted the
     * textField will tell the user there is an error otherwise it will
     * add the value into the stack
     * @param input - String containing the value user inputted
     */
    private void numberInputted(String input)
    {
            double inputValue = 0.0;
            if(Double.isNaN(Double.parseDouble(input)))
            {
                displayTextField.setText("Error");
            }
            if(!displayTextField.getText().equals(""))
            {
                inputValue = Double.parseDouble(input);
                calc.theStack.addFirst(inputValue);
                if(calc.theStack.isEmpty())
                {
                    displayTextField.setText("");
                }
                else
                {
                    displayTextField.setText(calc.theStack.getFirst().toString());
                }
                
            }       
    }
    /**
     * displays the appropriate help information for the button the user is
     * wondering about.
     * @param actionCommand the command from the triggering event
     */
    private void  displayHelp(String actionCommand)
    {
        msgOn = true;
        if(actionCommand.equals("Exit"))
                displayTextField.setText("Exit: Exits program");
        else if(actionCommand.equals("C")
                || actionCommand.equals("c"))
                displayTextField.setText("C: Clears entire stack");
        else if(actionCommand.equals("CE"))
                displayTextField.setText("CE: Clears entry (top element)");
        else if(actionCommand.equals("+"))
                displayTextField.setText("+: adds top  2 elements");
        else if(actionCommand.equals("x")
                || actionCommand.equals("X")
                || actionCommand.equals("*"))
                displayTextField.setText("x: multiplies top 2 elements");
        else if(actionCommand.equals("Set"))
                displayTextField.setText("Set: Sets register (0-9)");
        else if(actionCommand.equals("Get"))
                displayTextField.setText("Get: gets register (0-9)");
        else if(actionCommand.equals("Up"))
                displayTextField.setText("Up: Rotates stack up");
        else if(actionCommand.equals("-"))
                displayTextField.setText("-: subtracts top 2 elements");
        else if(actionCommand.equals("/"))
                displayTextField.setText("/: divides top 2 elements");
        else if(actionCommand.equals("Load"))
                displayTextField.setText("Load: Loads program from file");
        else if(actionCommand.equals("Save"))
                displayTextField.setText("Save: Saves program to file");
        else if(actionCommand.equals("Down"))
                displayTextField.setText("Down: Rotates stack down");
        else if(actionCommand.equals("^"))
                displayTextField.setText("^: exponent");
        else if(actionCommand.equals("%"))
                displayTextField.setText("%: Mods top 2 elements");
        else if(actionCommand.equals("Rec"))
                displayTextField.setText("Rec: Program mode on/off");
        else if(actionCommand.equals("Run"))
                displayTextField.setText("Run: Runs program");
        else if(actionCommand.equals("?"))
                displayTextField.setText("?: press ? then key for help");
        else if(actionCommand.equals("+/-"))
                displayTextField.setText("+/-: changes sign of number");
        else if(actionCommand.equals("1/n"))
                displayTextField.setText("1/n: inverts the number");
        else if(actionCommand.equals("Enter"))
                displayTextField.setText("Enter: element to stack");
        else
                displayTextField.setText("");
    }

   

    /**
     * Description checks to see if the the character is a valid character
     * to be used.
     * @param c the character to test
     * @return true is c is valid, false otherwise
     */
    private boolean validChar(char c)
    {
        if(Character.isDigit(c))
            return true;
        switch(c)
        {
            case '+':
            case '-':
            case '*':
            case 'x':
            case 'X':
            case '/':
            case 'C':
            case 'c':
            case '^':
            case '%':
            case '?':
            case '.':
            case '\r':
            case '\n':
            return true;
        }
        return false;
    }
    
    /**
     * Description This method disables any character that is non-numeric.
     */
    private void disableAlpha()
    {
        for(char c = '\0'; c < '%'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '&'; c < '*'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = ':'; c <= '?'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '@'; c <= 'C'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'D'; c <= 'X'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'Y'; c <= '^'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '_'; c <= 'c'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'd'; c <= 'x'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'y'; c <= '~'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        
        displayTextField.getInputMap().put(KeyStroke.getKeyStroke('/'),
                            "none");
    }
    /**
     * Description This method creates a text file containing the instructions
     * that the user has recorded so that they will be able to run whatever
     * command they recorded.
     */
    private void saveFile()
    {
        try
        {
            File file = new File(MACROFILE);
            FileOutputStream fileStream = new FileOutputStream(file);
            BufferedWriter writeFile = new BufferedWriter(new OutputStreamWriter(fileStream));
            for(int i = 0; i < calc.instructions.size(); i++)
            {
                writeFile.write(calc.instructions.get(i));
                writeFile.newLine();
            }
            writeFile.flush();
            writeFile.close();
        }
        catch(IOException exp)
        {
            displayTextField.setText("File had issues being saved");
        }
    }
    /**
     * Description: This will load the program file the user has created
     * if it exists and adds the instructions to a linked list if the file
     * doesn't exist then in it will display to the user that it cannot
     * be opened or doesn't exist.
     */
    private void loadFile()
    {
        File loadingFile = new File(MACROFILE);
        try
        {
            if(loadingFile.canRead() && loadingFile.exists())
            {
                Scanner scanFile = new Scanner(loadingFile);
                calc.instructions.clear();
                while(scanFile.hasNextLine())
                {
                    String line = scanFile.nextLine();
                    calc.instructions.add(line);
                }
                scanFile.close();
            }
        }
        catch(FileNotFoundException exp)
        {
            displayTextField.setText("File cannot be read or doesnt exist");
        }
    }
    /**
     * This method will run the macrofile and perform the commands in the file.
     */
    private void runFile()
    {
        for(int i = 0; i < calc.instructions.size(); i++)
        {
            dealWith(calc.instructions.get(i));
        }
    }
    
}





