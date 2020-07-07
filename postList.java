import java.io.*;
import java.util.*;

public class postList {
    List<Integer> DocumentsOccured = new ArrayList<>();
    List<Integer> termFreq = new ArrayList<>();
    List<Integer> pos = new ArrayList<>();


    public String toString(){
        return "\nDocumentID " + printID() + "\n" + "TermFreq" + printFreq() + "\n" + "position" + printPos();
    }

    public String printID()
    {
        String s = "";
        for(int value : DocumentsOccured)
        {
            s = s + " " + value;
        }
        return "" + s + "";
    }

    public String printFreq()
    {
        String s = "";
        for(int value : termFreq)
        {
            s = s + " " + value;
        }
        return "" + s + "";
    }

    public String printPos()
    {
        String s = "";
        for(int value : pos)
        {
            s = s + " " + value;
        }
        return "" + s + "";
    }
}