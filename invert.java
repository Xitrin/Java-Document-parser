import java.io.*;
import java.util.*;

public class invert {
    // Farkhad Gapparov 500718721 and Beniam Kebede 500717478
    public static void main(String[] args) throws IOException  {
        String current = System.getProperty("user.dir");
        Map<String, postList> list = new TreeMap<String, postList>();
        Map<String, Integer> mapp = new HashMap();
        Map<Integer,String> authorList = new HashMap();
        Map<Integer, String> titleList = new HashMap();
        Scanner sc = new Scanner(System.in);
        Scanner scan = null;
        Scanner scan2 = null;
        int ID= 0;
        int pos = 0;
        String authorLines = "";
        String titleLines = "";
        String betweenLines = "";
        String poster = "";
        try {
            System.out.println("Do you want to use Stemming?");
            String userInput = sc.nextLine();
            scan = new Scanner(new BufferedReader(new FileReader(current + "\\src\\cacm.all")));
            String next = "";
            do {
                pos = 0;
                next = scan.next();
                if(next.equals(".I")){
                    next = scan.next();
                    pos++;
                    ID = Integer.parseInt(next);
                }
                else if (next.equals(".T")) {
                    next = scan.next();
                    while(!next.matches("\\.[ABCIKNTWX]"))
                    {
                        titleLines = next ;
                        String nexterm = titleLines.replaceAll("[!&--+.'^:,/()$<>;\\[\\]%*\"\\{\\}]", "").toLowerCase().trim();
                        if(userInput == "Yes") {
                            nexterm = strem(titleLines.replaceAll("[!&--+.'^:,/()$<>;\\[\\]%*\"\\{\\}]", "").toLowerCase().trim());
                        }
                        if (nexterm.length() >= 1 && !nexterm.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
                            if (mapp.get(nexterm) == null) {
                                mapp.put(nexterm, 1);
                            }
                            else {
                                mapp.put(nexterm, mapp.get(nexterm) + 1);
                            }
                        }

                        if(titleList.get(ID) == null) {
                            titleList.put(ID, titleLines);
                        }
                        else {
                            titleLines =  titleList.get(ID)  + " " + next;
                            titleList.put(ID, titleLines);
                        }

                        next = scan.next();
                    }
                }
                if (next.equals(".W")) {
                    next = scan.next();
                    pos++;
                    while (!next.matches("\\.[ABCIKNTWX]"))  {
                        betweenLines = next + scan.nextLine();
                        poster = betweenLines.replaceAll("[!&--+.'^:,/()$<>;\\[\\]%*\"\\{\\}]", "").toLowerCase();
                        StringTokenizer st = new StringTokenizer(poster);
                        while((!st.toString().matches("\\.[ABCIKNTWX]")) && st.hasMoreElements()) {
                            String nexterm = st.nextElement().toString();
                            pos++;
                            int counter = 1 ;
                            if(userInput == "Yes") {
                                nexterm = strem(nexterm);
                            }
                            if (nexterm.length() >= 1 && !nexterm.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
                                if (mapp.get(nexterm) == null) {
                                    mapp.put(nexterm, 1);
                                }
                                else {

                                    mapp.put(nexterm, mapp.get(nexterm) + 1);
                                }

                                if (list.get(nexterm) == null) {
                                    postList temp = new postList();
                                    temp.DocumentsOccured.add(ID);
                                    temp.termFreq.add(counter);
                                    temp.pos.add(pos);
                                    list.put(nexterm, temp);
                                }
                                else if (list.containsKey(nexterm)) {
                                    if(list.get(nexterm).DocumentsOccured.contains(ID) == true) {
                                        for(int i = 0; i < list.get(nexterm).DocumentsOccured.size(); i++){
                                            if(list.get(nexterm).DocumentsOccured.get(i) == ID)
                                            {
                                                int temp = list.get(nexterm).termFreq.get(i) + 1;
                                                list.get(nexterm).termFreq.set(i, temp);
                                                list.get(nexterm).pos.add(pos);
                                            }
                                        }
                                    }
                                    else if(list.get(nexterm).DocumentsOccured.contains(ID) == false )
                                    {
                                        int temp = 1;
                                        list.get(nexterm).DocumentsOccured.add(ID);
                                        list.get(nexterm).termFreq.add(temp);
                                        list.get(nexterm).pos.add(pos);
                                    }
                                }
                            }
                        }

                        next = scan.next();
                    }
                }
                else if(next.equals(".A")) {
                    next = scan.next();
                    while ((!next.matches("\\.[ABCIKNTWX]"))){
                        authorLines = next ;
                        if(authorList.get(ID) == null) {
                            authorList.put(ID, authorLines);
                        }
                        else {
                            authorLines =  authorList.get(ID)  + " " + next;
                            authorList.put(ID, authorLines);
                        }
                        next = scan.next();
                    }
                }
            } while (scan.hasNext());
        } finally {
            if (scan != null) {
                scan.close();
            }
        }

        System.out.println("Do you want to use StopWords?");
        String userInput1 = sc.nextLine();
        if(userInput1 == "Yes") {
            stopWords(scan2,current + "\\src\\common_words", mapp);
            stopWords(scan2,current + "\\src\\common_words", list);
        }

        //Dictionary.txt file
        PrintWriter print = new PrintWriter(new FileOutputStream("Dictionary.txt", false), true);
        List<String> listVal = new ArrayList<String>(mapp.keySet());
        Collections.sort(listVal);
        for (int i = 0; i < listVal.size(); i++) {
            print.println(listVal.get(i) + " " + mapp.get(listVal.get(i)));
        }

        //Author.txt file
        PrintWriter printer = new PrintWriter(new FileOutputStream("Author.txt", false), true);
        List<Integer> listVale = new ArrayList<Integer>(authorList.keySet());
        Collections.sort(listVale);
        for (int i = 0; i < listVale.size(); i++) {
            printer.println(listVale.get(i) + "\n" + authorList.get(listVale.get(i)));
        }

        //Title.txt file
        PrintWriter printer2 = new PrintWriter(new FileOutputStream("Title.txt", false), true);
        List<Integer> listValue = new ArrayList<Integer>(titleList.keySet());
        Collections.sort(listValue);
        for (int i = 0; i < listValue.size(); i++) {
                printer2.println(listValue.get(i) + "\n" + titleList.get(listValue.get(i)));
        }

        //PostList.txt file
        PrintWriter print2 = new PrintWriter(new FileOutputStream("PostList.txt", false), true);
        for(Map.Entry<String,postList> entry : list.entrySet()) {
                print2.write(entry.getKey() + entry.getValue().toString());
                print2.write("\n");
                print2.write("\n");
        }
    }

    public static void stopWords(Scanner scan2, String file, Map list) throws IOException, IndexOutOfBoundsException
    {
        try {
            scan2 = new Scanner(new BufferedReader(new FileReader(file)));
            String st;
            do {
                st = scan2.next();
                if (list.get(st) != null)
                {
                    list.remove(st);
                }
            } while (scan2.hasNext());
        } finally {
            if (scan2 != null)
            {
                scan2.close();
            }
        }

    }

    public static String StremPrep(String word)
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
        else if(word.endsWith("ational"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("ational", "ate");
            return stemFinal;
        }
        else if(word.endsWith("tional"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("tional", "tion");
            return stemFinal;
        }
        else if(word.endsWith("izer"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("izer", "ize");
            return stemFinal;
        }
        else if(word.endsWith("bli"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("bli", "ble");
            return stemFinal;
        }
        else if(word.endsWith("alli"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("alli", "al");
            return stemFinal;
        }
        else if(word.endsWith("entli"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("entli", "ent");
            return stemFinal;
        }
        else if(word.endsWith("eli"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("eli", "e");
            return stemFinal;
        }
        else if(word.endsWith("ousli"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("ousli", "ous");
            return stemFinal;
        }
        else if(word.endsWith("ization"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("ization", "ize");
            return stemFinal;
        }
        else if(word.endsWith("ation"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("ation", "ate");
            return stemFinal;
        }
        else if(word.endsWith("ator"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("ator", "ate");
            return stemFinal;
        }
        else if(word.endsWith("alism"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("alism", "al");
            return stemFinal;
        }
        else if(word.endsWith("iveness"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("iveness", "ive");
            return stemFinal;
        }
        else if(word.endsWith("fulness"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("fulness", "ful");
            return stemFinal;
        }
        else if(word.endsWith("aliti"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("aliti", "al");
            return stemFinal;
        }
        else if(word.endsWith("iviti"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("iviti", "ive");
            return stemFinal;
        }
        else if(word.endsWith("biliti"))
        {
            stemFinal = word;
            stemFinal = stemFinal.replace("biliti", "ble");
            return stemFinal;
        }
        else return word;
    }

    public static String strem(String word) {

        String stremmed = "";
        stremmed = StremPrep(word);

        Stemmer strem = new Stemmer();
        strem.stem();
        return stremmed;
    }


}