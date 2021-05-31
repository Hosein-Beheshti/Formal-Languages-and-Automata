import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * this project is a dfa machine acceptor that
 * get data from a text file and scan string as input and
 * print result as output
 *
 * @author Hosein Beheshti
 */
public class Main {

    public static void main(String[] args) {
        String currentState = null;  //this is the state we are in at the moment
        String[][] arr = new String[1000][3];
        String[] finalStates = new String[1000];

        int finalStatesSize = 0;
        int line = 0;
        /**
         * read from text file line by line and do some operations on each line
         */
        try {
            File myObj = new File("DFA_Input_1.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                line++;
                String data = myReader.nextLine();
                if(line == 3)
                currentState = data;
                if(line == 4) {
                    String[] arrOfStr = data.split(" ", -1);


                    for (String a : arrOfStr) {
                        //System.out.println(a);
                        finalStates[finalStatesSize] = a;
                        finalStatesSize++;
                    }
                }

                if(line > 4)
                {
                    int rowCounter = line - 5;
                    String[] arrOfStr = data.split(" ", 3);
                    int colCounter = 0;
                    for (String a : arrOfStr) {
                        arr[rowCounter][colCounter] = a;
                        colCounter++;
                    }
                }
            }
            //System.out.println(data);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        String[] alphabetArr = new String[1000];
        /**
         * get string from input and add each alphabet in array
         */
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] arrOfStr = s.split(" ", -1);

        int alphabetSize = 0;
        for (String a : arrOfStr) {
            //System.out.println(a);
            alphabetArr[alphabetSize] = a;
            alphabetSize++;
        }
        boolean acc = false;
        boolean valid = true;
        for(int c = 0 ; c < alphabetSize ; c++) {
            int checker = 0;
            for(int i = 0 ; i < line - 4 ; i++)
            {
                if(arr[i][0].equals(currentState) && arr[i][1].equals(alphabetArr[c])) {
                    //change current state
                    currentState = arr[i][2];
                    break;
                }
                else
                    checker++;
            }
            if(checker >= line - 4)
            {
                valid = false;
                break;
            }
        }

        for(int i = 0 ; i < finalStatesSize ; i++)
        {
            //check we are in final state or not
            if(currentState.equals(finalStates[i]))
            {
                acc = true;
                break;
            }
        }
        if(valid == true && acc == true)
            System.out.println("Accepted");
        else
            System.out.println("Not Accepted");

    }
}
