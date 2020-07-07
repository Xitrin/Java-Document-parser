import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;


public class test {
    //Farkhad Gapparov 500718721 and Beniam Kebede 500717478
    public static void main(String args[]) {
        test T = new test();
        while(true)
        {
            T.looper();
        }
    }

    private void looper() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please provide the term you are looking for: ");
        while (sc.hasNext()) {
            String user_input = sc.nextLine();

            if(user_input.equals("zzend")) {
                System.exit(0);
                System.out.println("Program end!");
            }

            if(!check(user_input)){break;}

            try {
                long start1 = System.currentTimeMillis();
                lookup(user_input);
                long time1 = System.currentTimeMillis() - start1;
                System.out.println("This query took " + time1 + " milliseconds to resolve." + "\n");
            } catch (FileNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }

            System.out.println("Please provide the term you are looking for: ");
        }
    }



    private void lookup(String word) throws FileNotFoundException {
        System.out.println("Searching Dictionary..." + "\n");

        File dictionary = new File("F:\\IntelliJ IDEA\\842A1\\Dictionary.txt");
        Scanner looker = new Scanner(dictionary);

        int totalOccurrence = 0;
        int foundit = 0;

        while(looker.hasNextLine()) {
            String input = looker.nextLine();
            String inputline = input.split( " ")[0];
            int inputstat = Integer.parseInt(input.split( " ")[1]);

            if(inputline.equals(word)) {
                System.out.println("Found!");
                totalOccurrence = inputstat;
                System.out.println("Number of appearances is: " + totalOccurrence + "\n");

                try {
                    lookupPosting(inputline);
                } catch (FileNotFoundException e) {
                    throw new ExceptionInInitializerError(e);
                }
                foundit = 1;
                break;
            }
        }
        if (foundit==0)
          System.out.println("!!!! Not Found !!!!" + "\n");

    }


    private void lookupPosting(String link) throws FileNotFoundException {
        System.out.println("Searching through posting lists..." + "\n");

        File postList = new File("F:\\IntelliJ IDEA\\842A1\\PostList.txt");
        Scanner listLooker = new Scanner(postList);

//        File orig = new File("F:\\IntelliJ IDEA\\842A1\\src\\cacm.all");
//        Scanner origParser = new Scanner(orig);

        while(listLooker.hasNextLine()) {
            String listInput = listLooker.nextLine();
//            while(origParser.hasNextLine()) {
//                String origNext = origParser.nextLine();
//                if (origNext.equals(".T")) {
//                    String title = origParser.nextLine();
//                    System.out.println(title);
//                    break;
//                }
//            }

                if(listInput.equals(link)) {
                System.out.println("Found!");
                System.out.println(listLooker.nextLine());
                System.out.println(listLooker.nextLine());
                System.out.println(listLooker.nextLine());
            }
        }
    }

    private boolean check(String word)
    {
        System.out.println("Checking user input");

        if(word.matches("[!&--+.'^:,/()$<>;\\[\\]%*\"\\{\\}]"))
        {
            System.out.println("Please remove any special characters to continue");
            return false;
        }

        return true;
    }

}