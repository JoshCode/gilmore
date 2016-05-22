package nl.codefox.gilmore.util;

public class ArrayUtil 
{

    public static String arrayToString(String[] array, int start, String delimiter)
    {
        String ret = "";
        for(int i = start; i < array.length; i++)
        {
            if(i < array.length - 1)
            {
                ret += array[i] + delimiter;
            }
            else
            {
                ret += array[i];
            }
        }
        return ret;
    }
    
}
