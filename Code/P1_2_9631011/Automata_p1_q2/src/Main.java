import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * this project in epsilon nfa to dfa machine conversion
 * read data from text file and write dfa result data on another text file
 *
 * @author Hosein beheshti
 */
public class Main {
    static String startState = "";
    static String[][] arr = new String[1000][3];
    static String[] finalStates = new String[1000];
    static String[] states = new String[1000];




    static int finalStatesSize = 0;
    static int statesSize = 0;
    static int alphabetSize = 0;
    static int line = 0;
    public static void main(String[] args) throws IOException {
        String[] alphabet = new String[1000];

        //define variables to save dfa result
        String[] dfaStates = new String[1000];
        int dfaStatesSize = 0;
        String dfaStartState = "";
        String[] dfaFinalStates = new String[1000];
        int dfaFinalStatesSize = 0;
        String[][] dfaArr = new String[1000][3];
        int dfaArrSize = 0;

        /**
         * read from txt file line by line and do operations on it
         */
        try {
            File myObj = new File("NFA_Input_2.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                line++;
                String data = myReader.nextLine();

                if(line == 1)
                {
                    data = data.substring(1);
                    String[] arrOfStr = data.split(" ", -1);
                    alphabetSize = arrOfStr.length;

                    //System.out.println(arrOfStr[0].equals("a"));
                    for(int i = 0 ; i < alphabetSize ; i++)
                    {
                        alphabet[i] = arrOfStr[i];
                    }
                    //System.out.println(alphabet[0]);
                }
                if(line == 2)
                {
                    String[] arrOfStr = data.split(" ", -1);


                    for (String a : arrOfStr) {
                        //System.out.println(a);
                        states[statesSize] = a;
                        statesSize++;
                    }
                }
                if(line == 3)
                    startState = data;
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        //convert epsilon nfa to dfa***********************************************
        int i = 0;
        ArrayList<ArrayList<String>> tmpArray = new ArrayList<>();
        while (true)
        {
            //define temp clouser array for pass to clouser func
            ArrayList<String> clouserTemp = new ArrayList<String>();
            //define  level 1 clouser array
            ArrayList<String> clouserlvl1 = new ArrayList<String>();
            //System.out.println("state is = " + states[i]);

            if(i == 0)
            clouserlvl1 = λ_clouser(states[i] , clouserTemp);
            else {
                if(tmpArray.size() == 0)
                    break;
                clouserlvl1 = tmpArray.get(0);
                System.out.println("********");
                System.out.println(tmpArray);
                tmpArray.remove(0);
            }
            i++;
            //sort array
            for(int a = 0; a < clouserlvl1.size(); a++)
            {
                for(int b = a + 1; b < clouserlvl1.size(); b++)
                {
                    if(clouserlvl1.get(a).compareTo(clouserlvl1.get(b)) > 0)
                    {
                        String t = "";
                        t = clouserlvl1.get(a);
                        clouserlvl1.set(a ,clouserlvl1.get(b));
                        clouserlvl1.set(b ,t);
                    }
                }
            }
            System.out.println("clouser level 1" + clouserlvl1);
            String dfa_state = "";
            boolean isFinal = false;
            for(int t = 0 ; t < clouserlvl1.size() ; t++)
            {
                for(int i1 = 0 ; i1 < finalStatesSize ; i1++)
                {
                    if(clouserlvl1.get(t).equals(finalStates[i1]))
                        isFinal = true;
                }
                dfa_state += clouserlvl1.get(t);
            }
            //add to dfa states
            boolean stateExist = false;
            for(int t = 0 ; t < dfaStatesSize ; t++) {
                if(dfa_state == dfaStates[t])
                    stateExist = true;
            }
            //add new states into dfa states
            if(!stateExist) {
                dfaStates[dfaStatesSize] = dfa_state;
                dfaStatesSize++;
            }
            if(i == 1)
                dfaStartState = dfa_state;
            if(isFinal) {
                dfaFinalStates[dfaFinalStatesSize] = dfa_state;
                dfaFinalStatesSize++;
            }


            for(int j = 0 ; j < alphabetSize ; j++)
            {
                System.out.println(alphabet[j]);
                ArrayList<String> desStates = new ArrayList<String>();
                desStates = calculateDesStates(clouserlvl1, alphabet[j]);
                System.out.println("des states = " + desStates);
                String dfa_state1 = "";
                ArrayList<String> clouser2 = new ArrayList<String>();
                for(int k = 0 ; k < desStates.size() ; k++)
                {
                    λ_clouser(desStates.get(k), clouser2);
                }
                System.out.println("re = " + clouser2);
                //sort array
                for(int a = 0; a < clouser2.size(); a++)
                {
                    for(int b = a + 1; b < clouser2.size(); b++)
                    {
                        if(clouser2.get(a).compareTo(clouser2.get(b)) > 0)
                        {
                            String t = "";
                            t = clouser2.get(a);
                            clouser2.set(a ,clouser2.get(b));
                            clouser2.set(b ,t);
                        }
                    }
                }
                for(int t = 0 ; t < clouser2.size() ; t++)
                {
                    dfa_state1 += clouser2.get(t);
                }
                /**
                 * add states relations into array
                 */
                    dfaArr[dfaArrSize][0] = dfa_state;
                    dfaArr[dfaArrSize][1] = alphabet[j];
                    dfaArr[dfaArrSize][2] = dfa_state1;
                    dfaArrSize++;

                boolean loop = false;
                //check this is new state in table or not
                for(int t = 0 ; t < dfaArrSize ; t++)
                {
                   // System.out.println("dfa state is = " + dfa_state1 + " dfaArr[0] is " + dfaArr[t][0]);
                    if(dfa_state1.equals(dfaArr[t][0])) {
                        loop = true;
                    }
                }
                //check this state exist in array or not
                for(int t = 0 ; t < tmpArray.size() ; t++)
                {
                    String dfa_state2 = "";
                    for(int t1 = 0 ; t1 < tmpArray.get(t).size() ; t1++)
                    {
                        dfa_state2 += tmpArray.get(t).get(t1);
                    }

                    if(dfa_state1.equals(dfa_state2)) {
                        loop = true;
                    }
                }
                //if it's a new state add it into transition table
                if(loop == false && clouser2.size() > 0)
                {
                    tmpArray.add(clouser2);
                }
            }

        }
        //make a string includes dfa alphabet
        String dfaAlphabetString = "";
        for(int j = 0 ; j < alphabetSize ; j++)
        {
            dfaAlphabetString += alphabet[j];
            if(j < alphabetSize - 1)
            dfaAlphabetString += " ";
        }
        System.out.println(dfaAlphabetString);

        //make a string includes dfa states
        String dfaStatesString = "";
        for(int j = 0 ; j < dfaStatesSize ; j++)
        {
            dfaStatesString += dfaStates[j];
            if(j < dfaStatesSize - 1)
                dfaStatesString += " ";
        }
        System.out.println(dfaStatesString);

        System.out.println(dfaStartState);

        //make a string includes dfa final states
        String dfaFinalStatesString = "";
        for(int j = 0 ; j < dfaFinalStatesSize ; j++)
        {
            dfaFinalStatesString += dfaFinalStates[j];
            if(j < dfaFinalStatesSize - 1)
                dfaFinalStatesString += " ";
        }
        System.out.println(dfaFinalStatesString);

        for(int i3 = 0 ; i3 < dfaArrSize ; i3++)
        {
            System.out.print(dfaArr[i3][0] + " " + dfaArr[i3][1] + " " +dfaArr[i3][2]);
            System.out.println("");
        }

        /**
         * write dfa machine specification into txt fale
         */
        File fout = new File("DFA_Output_2.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(dfaAlphabetString);
        bw.newLine();
        bw.write(dfaStatesString);
        bw.newLine();
        bw.write(dfaStartState);
        bw.newLine();
        bw.write(dfaFinalStatesString);
        bw.newLine();
        for(int i3 = 0 ; i3 < dfaArrSize ; i3++) {
            if(dfaArr[i3][0].length() != 0 && dfaArr[i3][1].length() != 0 && dfaArr[i3][2].length() != 0) {
                String tmp = dfaArr[i3][0] + " " + dfaArr[i3][1] + " " + dfaArr[i3][2];
                bw.write(tmp);
                if (i3 < dfaArrSize - 1)
                    bw.newLine();
            }
        }
        bw.close();

    }

    /**
     * Calculate λ_clouser for a special state
     *
     * @param state
     * @param array
     * @return an arraylist includes λ_clouser
     */
    public static ArrayList<String> λ_clouser(String state, ArrayList<String> array)
    {
        boolean checkExist = false;
        for(int k = 0 ; k < array.size() ; k++)
        {
            if(array.get(k).equals(state))
                checkExist = true;
        }
        // add if not exist in our array
        if(!checkExist)
        array.add(state);
        for(int i = 0 ; i < line - 4 ; i++)
        {

                if(arr[i][0].equals(state) && arr[i][1].equals("λ"))
                {
                    λ_clouser(arr[i][2], array);
                }
        }

        return array;
    }

    /**
     * calculate destionation states with alphabet
     * @param array
     * @param alphabet1
     * @return and array includes all possible destination to other states
     */
    public static ArrayList<String> calculateDesStates(ArrayList<String> array , String alphabet1)
    {
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0 ; i < array.size() ; i++)
        {
            for(int j = 0 ; j < line - 4 ; j++)
            {
//                System.out.println(arr[j][1] + " " + alphabet1);
//                    if(alphabet1.equals(arr[j][1]))
//                        System.out.println("yes");
                    if(arr[j][0].equals(array.get(i)) && arr[j][1].equals(alphabet1)) {
                    boolean checkExist = false;
                    for(int k = 0 ; k < temp.size() ; k++)
                    {
                        if(temp.get(k).equals(arr[j][2]))
                            checkExist = true;
                    }
                    if(!checkExist)
                        temp.add(arr[j][2]);

                }
            }
        }

        return temp;
    }
}
