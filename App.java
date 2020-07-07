import javax.management.Query;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class App {
    public JPanel panelMain;
    public JButton searchButton;
    public JRadioButton stopWordRemoval;
    public JRadioButton stemming;
    public JTextField searchfield;
    public String query = "";
    public ArrayList<String> in = new ArrayList<String>();
    public static Map<Integer, String> titleList = new HashMap();
    public static Map<Integer, String> authorList = new HashMap();
    public static Map<String, Integer> dictionary = new HashMap();
    public static Map<String, Search>  postList = new HashMap();
    public static Map<String, Double> order = new HashMap();
    public static ArrayList<Double> termFreq = new ArrayList<Double>();

    public App() {
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String value = searchfield.getText();
                ArrayList<String> split = new ArrayList<String>();
                StringTokenizer input = new StringTokenizer(value);
                String fina = " ";
                while(input.hasMoreElements())
                {
                    split.add(input.nextElement().toString());
                }

                String fin = "";
                if(stopWordRemoval.isSelected() && stemming.isSelected())
                {
                    try {
                        for(String s : split) {
                            String test = s.trim();
                            String que = stopWords(test);
                            String stremmed = strem(que);
                            fin += stremmed + " ";
                            query += fin.trim();

                            in = split;
                            for (String t : split) {
                                fin += t + " ";
                            }
                            getTitles();
                            getAuthors();
                            for (String temp : split) {
                                DictionaryHashMap(temp);
                            }

                            for (String temp : split) {
                                PostingHashMap(temp);
                            }

                            termFreq(dictionary, postList);
                            WeightCalc(dictionary, postList);
                            cosSim(postList, split, in);
                            sortSim(postList);


                            fina = outPut(order);

                            System.out.println(fina);
                        }
                    }
                    catch(IOException ioe) {
                        System.out.println(ioe);
                    }

                    /*Re run compilation but include stop words HERE*/
                    JOptionPane.showMessageDialog(null,"Document lists have been changed to contain stop words");
                }else if(stopWordRemoval.isSelected() && !stemming.isSelected())
                {
                    try {
                        for(String s : split) {
                            String test = s.trim();
                            String que = stopWords(test);
                            fin += que + " ";
                            query += fin.trim();

                            in = split;
                            for (String t : split) {
                                fin += t + " ";
                            }
                            getTitles();
                            getAuthors();
                            for (String temp : split) {
                                DictionaryHashMap(temp);
                            }

                            for (String temp : split) {
                                PostingHashMap(temp);
                            }

                            termFreq(dictionary, postList);
                            WeightCalc(dictionary, postList);
                            cosSim(postList, split, in);
                            sortSim(postList);


                            fina = outPut(order);

                            System.out.println(fina);
                        }
                    }
                    catch(IOException ioe) {}

                    /*Re run compilation but remove stop words HERE*/
                    JOptionPane.showMessageDialog(null,"Document lists have been changed to not contain stop words");
                }

                if(stemming.isSelected() && !stopWordRemoval.isSelected())
                {
                    try {
                        for (String s : split) {
                            String test = strem(s);
                            fin += test + " ";
                            query += fin.trim();
                        }
                        in = split;
                        for (String s : split) {
                            fin += s + " ";
                        }
                        getTitles();
                        getAuthors();
                        for (String temp : split) {
                            DictionaryHashMap(temp);
                        }

                        for (String temp : split) {
                            PostingHashMap(temp);
                        }

                        termFreq(dictionary, postList);
                        WeightCalc(dictionary, postList);
                        cosSim(postList, split, in);
                        sortSim(postList);


                        fina = outPut(order);

                        System.out.println(fina);
                    }
                    catch (IOException ioe){
                        System.out.println();
                    }
                    /*Re run compilation but stem the words HERE*/
                    JOptionPane.showMessageDialog(null,"Document lists have had their words stemmed");
                }
                if(!stemming.isSelected() && !stopWordRemoval.isSelected()) {
                    try{
                        in = split;
                        for(String s : split) {
                            fin += s + " ";
                        }
                        getTitles();
                        getAuthors();
                        for(String temp: split){
                            DictionaryHashMap(temp);
                        }

                        for(String temp: split){
                            PostingHashMap(temp);
                        }

                        termFreq(dictionary, postList);
                        WeightCalc(dictionary, postList);
                        cosSim(postList,split,in);
                        sortSim(postList);


                        fina = outPut(order);

                        System.out.println(fina );


                    }
                    catch (IOException ioe)  {
                        System.out.println(ioe);
                    }
                }
                query = searchfield.getText();
                JOptionPane.showMessageDialog(null,"This is the result of your search: "+ "\n" + fin.trim() + " = " + fina + "\n");
            }
        });
    }

    public String stopWords(String list) throws IOException, IndexOutOfBoundsException
    {
        Scanner scan2 = null;
        String ret = "";
        String current = System.getProperty("user.dir");
        try {
            scan2 = new Scanner(new BufferedReader(new FileReader(current + "\\src\\common_words")));
            String st;
            do {
                st = scan2.next();
                if (list.trim().equalsIgnoreCase(st))
                {
                    list = list.replace(st, "");

                }
            } while (scan2.hasNext());
        } finally {
            if (scan2 != null)
            {
                scan2.close();
            }
        }
        return list;
    }

    public static String StepOne(String word)
    {
        String stemFinal = "";
        String temp = "";
        if(word.endsWith("sses"))
        {
            stemFinal = word.substring(0, word.length() - 4) + "ss";
            return stemFinal;
        }
        else if (word.endsWith("ies"))
        {
            stemFinal = word.substring(0, word.length() - 3) + "i";
            return stemFinal;
        }
        else if(word.endsWith("ed"))
        {
            temp = word.substring(0, word.length() - 2);
            if(temp.endsWith("at")) {
                stemFinal = temp.substring(0, temp.length() - 2) + "ate";
                return stemFinal;
            }
            if(temp.endsWith("bl")) {
                stemFinal = temp.substring(0, temp.length() - 2) + "ble";
                return stemFinal;
            }
            if (temp.endsWith("iz")) {
                stemFinal = temp.substring(0, temp.length() - 2) + "ize";
                return stemFinal;
            }
            else return temp;
        }
        else return word;
    }

    public static String StepTwo(String word)
    {
        String StemFinal = "";
        if(word.endsWith("y")){
            StemFinal = word.substring(0, word.length()- 1) + "i";
            return StemFinal;
        }
        else return word;
    }

    public static String StepThree(String word)
    {
        String StemFinal = "";
        if(word.endsWith("ational"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ational", "ate");
            return StemFinal;
        }
        else if(word.endsWith("tional"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("tional", "tion");
            return StemFinal;
        }
        else if(word.endsWith("izer"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("izer", "ize");
            return StemFinal;
        }
        else if(word.endsWith("bli"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("bli", "ble");
            return StemFinal;
        }
        else if(word.endsWith("alli"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("alli", "al");
            return StemFinal;
        }
        else if(word.endsWith("entli"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("entli", "ent");
            return StemFinal;
        }
        else if(word.endsWith("eli"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("eli", "e");
            return StemFinal;
        }
        else if(word.endsWith("ousli"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ousli", "ous");
            return StemFinal;
        }
        else if(word.endsWith("ization"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ization", "ize");
            return StemFinal;
        }
        else if(word.endsWith("ation"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ation", "ate");
            return StemFinal;
        }
        else if(word.endsWith("ator"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ator", "ate");
            return StemFinal;
        }
        else if(word.endsWith("alism"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("alism", "al");
            return StemFinal;
        }
        else if(word.endsWith("iveness"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("iveness", "ive");
            return StemFinal;
        }
        else if(word.endsWith("fulness"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("fulness", "ful");
            return StemFinal;
        }
        else if(word.endsWith("aliti"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("aliti", "al");
            return StemFinal;
        }
        else if(word.endsWith("iviti"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("ivity", "ive");
            return StemFinal;
        }
        else if(word.endsWith("biliti"))
        {
            StemFinal = word;
            StemFinal = StemFinal.replace("biliti", "ble");
            return StemFinal;
        }
        else return word;
    }

    public static String strem(String word)
    {

        String one = "";
        String two = "";
        String three = "";

        one = StepOne(word);
        two =  StepTwo(one);
        three  = StepThree(two);

        return three;
    }

    public static void main(String[] args) throws IOException {
        String query1 = null;
        App gui = new App();
        JFrame frame = new JFrame("App");
        frame.setContentPane(gui.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void getTitles() throws FileNotFoundException {
        Scanner Scan ;
        String current = System.getProperty("user.dir");
        Scan = new Scanner(new BufferedReader(new FileReader(current + "\\Title.txt")));
        while(Scan.hasNextLine())
        {
            int ID = Integer.parseInt(Scan.nextLine());
            String Title = Scan.nextLine();

            titleList.put(ID, Title );

        }
    }

    public static void getAuthors() throws FileNotFoundException {
        Scanner Scan;
        String current = System.getProperty("user.dir");
        Scan = new Scanner(new BufferedReader(new FileReader(current + "\\Author.txt")));
        while(Scan.hasNextLine())
        {
            int ID = Integer.parseInt(Scan.nextLine());
            String author = Scan.nextLine();
            authorList.put(ID, author );
        }
    }

    public static void DictionaryHashMap(String query) throws FileNotFoundException
    {
        Scanner Sc;
        String line = "";
        String current = System.getProperty("user.dir");
        Sc = new Scanner(new BufferedReader(new FileReader(current + "\\Dictionary.txt")));
        while(Sc.hasNext())
        {
            String word = Sc.next();
            if(word.equalsIgnoreCase(query)){
                int freq = Integer.parseInt(Sc.next());
                dictionary.put(word, freq);
                break;
            }
        }
    }

    public static void PostingHashMap(String query) throws FileNotFoundException
    {
        //  System.out.println("hello");
        Scanner sc;
        String docLine = "";
        String current = System.getProperty("user.dir");
        Search val = new Search();
        sc = new Scanner(new BufferedReader(new FileReader(current + "\\PostList.txt")));
        while(sc.hasNext())
        {
            String word = sc.next();
            if(word.equalsIgnoreCase(query)){
                String temp = sc.nextLine();
                docLine = sc.nextLine();
                String IDs = docLine.replaceAll("DocumentID  ", "");
                val.documentsOccured = IDs;


                String termLine = sc.nextLine();
                String freq = termLine.replaceAll("TermFreq ", "");
                val.docTermFreq = freq;
                postList.put(query, val);

            }

        }
    }

    public static void termFreq(Map<String, Integer> doc, Map<String, Search> pos)
    {
        StringTokenizer d;
        StringTokenizer f;

        for(Map.Entry<String,Search> temp : pos.entrySet())
        {
            d = new StringTokenizer(temp.getValue().documentsOccured);
            f = new StringTokenizer(temp.getValue().docTermFreq);
            Search n = new Search();
            String freq = "";
            while(d.hasMoreElements() && f.hasMoreElements()){
                double tem = Double.parseDouble(f.nextElement().toString());
                double termFreq = 1 + Math.log10(tem);
                n.termfs.add(termFreq);
            }
            n.documentsOccured = temp.getValue().documentsOccured;
            n.docTermFreq = temp.getValue().docTermFreq;

            postList.put(temp.getKey(),n);
        }
    }

    public static void WeightCalc(Map<String, Integer> doc, Map<String, Search> pos)
    {
        for(Map.Entry<String, Integer> temp : doc.entrySet())
        {

            double totalDocs = (double) 3204;
            double docFreq = (double) temp.getValue();
            double idf = Math.log10(totalDocs/docFreq);
            Search t = new Search();
            t = postList.get(temp.getKey());
            for(double tf : t.termfs)
            {
                t.w.add(tf*idf);
            }
            postList.put(temp.getKey(), t);

        }
    }

    public void cosSim(Map<String, Search> pos, ArrayList<String> query, ArrayList<String> input)
    {
        ArrayList<Double> docPowerTwo = new ArrayList<Double>();
        ArrayList<Double> queryPowerTwo = new ArrayList<Double>();
        double doc = 0.0;
        double que = 0.0;

        ArrayList<Double> vector = new ArrayList<Double>();
        for(String word: query)
        {
            int num = Collections.frequency(input,word);
            double weight = 1 + Math.log10((double)num);
            vector.add(weight);

            int i = 0;
            for(Map.Entry<String, Search> temp : pos.entrySet()) {
                Search n = temp.getValue();
                int j = 0;
                StringTokenizer line = new StringTokenizer(n.documentsOccured);

                while (line.hasMoreElements()) {
                    if (j == n.w.size()) {
                        break;
                    }
                    String l = line.nextElement().toString();
                    double multiply = n.w.get(j) * vector.get(i);
                    for (Double tem : n.w) {
                        docPowerTwo.add(tem * tem);
                    }
                    for (Double tem : docPowerTwo) {
                        doc += tem;
                    }

                    for (Double tem : vector) {
                        queryPowerTwo.add(tem * tem);
                    }
                    for (Double tem : queryPowerTwo) {
                        que += tem;
                    }
                    double square = doc * que;
                    n.cos.add(multiply / square);
                    j++;
                }
                postList.put(temp.getKey(), n) ;
                i++;
            }

        }
    }

    public static Map<String, Double> arrangedList(Search n){
        Map<String, Double> tem = new TreeMap<String, Double>();
        Search p = n;
        int i = 0;
        StringTokenizer d = new StringTokenizer(p.documentsOccured);

        while(d.hasMoreElements())
        {
            String line = d.nextElement().toString();
            tem.put(line, p.cos.get(i));
            i++;
        }
        return tem;
    }



    public static void sortSim(Map<String, Search> sim)
    {
        Map<String, Double> tem = new TreeMap<String, Double>();
        for(Map.Entry<String, Search> temp : sim.entrySet())
        {
            Search n = temp.getValue();
            if(tem.isEmpty()){
                tem = arrangedList(n);
                order = tem;
            }
            else
            {
                Map<String, Double> next = new TreeMap();
                next = arrangedList(n);
                Set<String> st = tem.keySet();
                Set<String> st2 = next.keySet();
                st.retainAll(st2);
                for(String i : st)
                {
                    tem.put(i, tem.get(i));
                }

            }
        }

    }

    public static String outPut(Map<String,Double> cos) {
        String fin = "";
        String d= "";
        String a= "";
        String tl= "";
        double dv = 0.0;
        int i = 0;
        //int d = 0;
        for (Map.Entry<String, Double> temp : cos.entrySet())
        {
            if(i == 10)
            {
                break;
            }
            d = temp.getKey();

            int key = Integer.parseInt(temp.getKey());
            dv = temp.getValue();

            //fin += "\n" + d + "\n" + dv + "\n";

            if(authorList.containsKey(key) && titleList.containsKey(key))
            {
                System.out.println("hello");
                d = temp.getKey();
                System.out.println(d);

                a = authorList.get(key);
                System.out.println(a);

                tl = titleList.get(key);
                System.out.println(tl);

                dv = temp.getValue();
                System.out.println(dv);

                fin +=  "\nDocument ID: " +  d + "\n" + " Author: " + a  + "\n" + "Title: " + tl + "\n" +  "CosineSim: " +  dv;
                System.out.println(fin);

            }
            i++;
        }
        return fin;
    }

}