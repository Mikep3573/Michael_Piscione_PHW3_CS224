import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.io.FileReader;

/**
 * Info holds general information pertaining to the algorithm. For the most part, it is meant to
 * help return two values (i.e., a list of integers and the number of inversions).
 */
class Info {
    // Create class fields
    // A and B are Integer ArrayLists meant to hold halves of a larger ArrayList (L)
    ArrayList<Integer> A = new ArrayList<>();
    ArrayList<Integer> B = new ArrayList<>();

    // The sorted list of integers given by MergeAndCount
    ArrayList<Integer> sortedList = new ArrayList<>();

    // Number of inversions
    int invCount = 0;
}

public class Main {
    // Class field for the original list of integers from the file
    static ArrayList<Integer> L = new ArrayList<>();

    /**
     * MergeAndCount implements the Merge and Count algorithm studied in class. It simultaneously sorts
     * two lists of numbers while merging the two lists into one.
     * @param A one half of the original list of integers (L).
     * @param B the other half of the original list of integers.
     * @return Info, the Info object holding the inversion count and the sorted list.
     */
    static Info MergeAndCount(ArrayList<Integer> A, ArrayList<Integer> B) {
        // Create an info variable to place the number of inversions and sorted list into.
        Info info = new Info();

        // Sort the two arrays by placing the elements from both in the auxiliary array one-by-one
        // Note that it only chooses from the front of both arrays and chooses the smaller of the two
        // (as specified by the algorithm studied in class)
        for(int i = 0; i < L.size(); i++) {
            // If either A or B have a size of zero, load the rest of the opposite half into the sorted list
            if(A.size() == 0) {
                info.sortedList.addAll(B);
                B.clear();
                break;
            }
            else if(B.size() == 0) {
                info.sortedList.addAll(A);
                A.clear();
                break;
            }
            // Compare the starts of both lists
            else if (A.get(0) < B.get(0)) {
                info.sortedList.add(A.get(0));
                A.remove(0);
            }
            else {
                info.sortedList.add(B.get(0));
                B.remove(0);
                info.invCount += A.size();
            }
        }

        // Return the inversion count and the sorted list (loaded into an Info object)
        return info;
    }


    /**
     * SortAndCount implements the Sort and Count algorithm studied in class. By recursively calling itself
     * it splits the list of integers given in half continuously. The inversion removal is then done through
     * MergeAndCount (another algorithm studied in class) in a Depth-First-Search order (picture a recursion tree).
     * The algorithm returns the final total number of inversions in the given list of numbers and the
     * sorted list of numbers.
     * @param L, the original list of numbers to have its inversions removed.
     * @return Info, the Info object containing the final list of numbers and the inversion count.
     */
    static Info SortAndCount(ArrayList<Integer> L) {
        // Create an Info object
        Info infoMain = new Info();

        // If the list has one element; Return Zero and the list L
        if(L.size() == 1) {
            infoMain.invCount = 0;
            infoMain.sortedList = L;
            return infoMain;
        }

        // Divide the list into two halves (A and B)
        // If an odd number of elements, have B contain one more than A (as seen in Homework Seven)
        for(int i = 0; i < L.size() / 2; i++) {
            infoMain.A.add(L.get(i));
        }
        for(int i = L.size() / 2; i < L.size(); i++) {
            infoMain.B.add(L.get(i));
        }

        // Call SortAndCount again on A
        Info infoA = SortAndCount(infoMain.A);

        // Call SortAndCount again on B
        Info infoB = SortAndCount(infoMain.B);

        // Call MergeAndCount on A and B
        Info infoSorted = MergeAndCount(infoA.sortedList, infoB.sortedList);

        // Return the sum of the inversions and the sorted list of numbers
        infoMain.invCount = infoSorted.invCount + infoA.invCount + infoB.invCount;
        infoMain.sortedList = infoSorted.sortedList;
        return infoMain;
    }
    public static void main(String[] args)  {
        // Prompt for an input file
        System.out.print("Please enter a filename: ");

        // Get user input file
        Scanner scanner = new Scanner(System.in);
        String userFile = scanner.nextLine();

        // NOTE FOR TESTER -> Please place the testing file in the src directory
        // Load the input file
        File inputFile = new File("src/" + userFile);

        // While there isn't an exception, proceed to
        // load the contents of the file into an ArrayList
        try {
            // Create a new scanner for the file
            Scanner s = new Scanner(new FileReader(inputFile));

            // Run the algorithm on every line of the input file
            // Add contents of a line to an ArrayList (L)
            while(s.hasNextLine()) {
                if(s.hasNext()) {
                    // Clear the ArrayList L
                    L.clear();

                    // Grab one line at a time
                    String line = s.nextLine();

                    // Split the line into numbers by using a white space as the delimiter
                    String[] splitted = line.split("\\s+");

                    // Cast the new strings into integers and add to L
                    for(int i = 0; i < splitted.length; ++i) {
                        L.add(Integer.parseInt(splitted[i]));
                    }
                }

                // Call the algorithm
                Info info = SortAndCount(L);

                // Create formatted output based on the result of the algorithm (found in the Info object)
                // Give the inversion count
                String fOutput = "Inversion Count: " + info.invCount + ". Sorted Array: ";

                // Give the order of the numbers without inversions
                for(int i = 0; i < info.sortedList.size(); i++) {
                    fOutput += info.sortedList.get(i) + " ";
                }

                // Print the formatted output
                System.out.println(fOutput);
            }
        }
        catch(Exception e) {
            System.out.println("File Not Found");
        }
    }
}