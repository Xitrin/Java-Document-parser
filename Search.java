import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

/**
 * Created by Ni2 on 10/22/2016.
 */


public class Search {

    public ArrayList<Double> w = new ArrayList();
    public ArrayList<Double> termfs = new ArrayList<>();
    public ArrayList<Double> cos = new ArrayList<>();

    public String word = "";
    public String docTermFreq = "";
    public String documentsOccured = "";


    public String cos()
    {
        String list = "";
        for (double temp: cos)
        {
            list = list + " " + temp;
        }
        return "" + list + "";
    }
    public String term()
    {
        String list = "";
        for (double temp: termfs)
        {
            list = list + " " + temp;
        }
        return "" + list + "";
    }
    public String weightPrint()
    {
        String list = "";
        for (double temp: w)
        {
            list = list + " " + temp;
        }
        return "" + list + "";
    }
    public String toString()
    {
        return /**documentsOccured + "\n" + docTermFreq+ "\n" + term()  +   "\n" +   weightPrint()  + **/ "\n" +cos() ;
    }




}