/*
 * RPNCalculator.java
 */

package RPN;
import java.util.Deque;
import java.util.LinkedList;

/**
 * File Name RPNCalculator.java
 * Description This class performs all the calculations of the reverse polish
 * notation calculator and save the answer into the stack.
 * @author Branavan Nagendiram
 * Date: 5/8/2017
 * Platform: Windows 10, jdk 1.8.0_66, NetBeans 8.2 i7 cpu
 */
public class RPNCalculator
{
    public static final int ROWS = 4;
    public static final int COLS = 8;
    public static final int NUMBER_OF_REGISTERS = 10;
    protected Deque<Double> theStack;   
    protected LinkedList<String> instructions;
    protected double[] registers;
    
    /**
     * Creates a new instance of RPNCalculator
     */
    public RPNCalculator()
    {
      theStack = new LinkedList(); 
      registers = new double[NUMBER_OF_REGISTERS];
      instructions = new LinkedList();
    }
    /**
     * Description This method takes in a string containing the button that
     * was pressed by the user then it will take the inputs and do the 
     * calculations in a post fix notation.
     * @param button String containing which button was pressed
     * @return pressed returns true if operation is completed false if not
     */
    public boolean buttonPressed(String button)
    {
        boolean pressed = true;
        if(theStack.size() < 2)
        {
            pressed = false;
            return pressed;
        }
        if(button.equals("Up"))
        {
         if(theStack.size() < 2)
         {
             return pressed;
         }
         else
         {
             theStack.addLast(theStack.pop());
             return pressed;
         }
        }
        if(button.equals("Down"))
        {
         if(theStack.size() < 2)
         {
             return pressed;
         }
         else
         {
             theStack.push(theStack.pollLast());
             return pressed;
         }
        }
        
        double secondInput = theStack.pop();
        double firstInput = theStack.pop();
       
        if(button.equals("+"))
        {
            
            theStack.push((firstInput + secondInput));
        }
        else if(button.equals("-"))
        {
            theStack.push((firstInput - secondInput));
        }
        else if(button.equals("x"))
        {
            theStack.push((firstInput * secondInput));
        }
        else if(button.equals("/"))
        {
            
            if(secondInput == 0.0)
            {
                pressed = false;
            }
            else
            {
                theStack.push(firstInput / secondInput);
            }
        }
        else if(button.equals("^"))
        {
            
             theStack.push(Math.pow(firstInput, secondInput));
            
            
        }
        else if(button.equals("%"))
        {
            theStack.push(firstInput % secondInput);
        }
        return pressed;
    }
     
}
