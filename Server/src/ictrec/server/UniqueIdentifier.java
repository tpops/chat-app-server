/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ictrec.server;
import java.util.*;
/**
 *
 * @author POPOOLA
 */
public class UniqueIdentifier {
    private static List<Integer> ids =
            new ArrayList<Integer>();
    private static final int Range = 1000;
    private static int index =0;
    // adding a range of numbers to the list
    static {
     for (int i=0;i<Range;i++)
     {
         ids.add(i);
     }
     Collections.shuffle(ids);
    }
    private UniqueIdentifier ()
            {
                
            }
    public static void ReturnIdentifier (int Id)
    {
        ids.add(Id);
    }
    public static int getIdentifier()
    {
      if (index>ids.size()-1) index = 0; 
      return ids.get(index++);
    }

}