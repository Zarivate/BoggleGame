import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Project Description:
 * This program simulates a game of Boggle. It prints out a standard Boggle board of characters
 * alongside doing common Boggle mechanics in the process such as shuffling the board and finding
 * possible words in the board. Ultimately printing out all the words discovered in the board in the process.
 *
 *@Author Ivan Zarate
 *CSCI 340
 *Section 001*
 *Assignment 2 Blocky Game
 *Known Bugs: None
 */

public class Boggle {

    // Boggle board object to to be utilized throughout program
    private static boggleDie[][] board = new boggleDie[4][4];

    // Boolean to be used when searching for a word
    private static boolean[][] used = new boolean[4][4];

    // As a TreeSet doesn't allow duplicates, it is chosen as the data structure to hold the words discovered in the board
    private static TreeSet<String> finalWords = new TreeSet<>();

    // Words supplies from the game dictionary
    private static Lexicon dictionary;

    /**
     * Constructor to create a dictionary object for all the possible words
     * @throws FileNotFoundException is what is thrown in the off chance no file is found
     */

    public Boggle() throws FileNotFoundException {

        dictionary = new Lexicon("ospd.txt");
    }


    /**
     * Method to shuffle the board, rearranges the board via the utilization of two
     * temporary positions that are created and swapped for new positions on the board
     */

    public static void shuffle() {

        // Two temporary boggleDie objects to be used later
        boggleDie temp;
        boggleDie temp2;

        // Instance of random class created to be used for a new position on board later
        Random randPosition = new Random();

        // Nested for loop to go through the board by row and columns
        for (int row = 0; row < board.length; row++) {

            for (int column = 0; column < board[0].length; column++) {

                // Sets the first temporary position equal to the current point on the board
                temp = board[row][column];

                // Creates a random number between 0 and 3 to be used as a new row position later
                int randRow = randPosition.nextInt(4);

                // Creates a random number between 0 and 3 to be used as a new column position later
                int randColumn = randPosition.nextInt(4);

                // Sets the second temporary position equal to the random point we created with the two ints above
                temp2 = board[randRow][randColumn];

                // Sets the current board position equal to the second temporary position with the random point
                board[row][column] = temp2;

                // Sets the random position on the board equal to the current position
                board[randRow][randColumn] = temp;

            }
        }
    }

    /**
     * Method to print the board and all it's characters in a neat way
     */

    public static void printBoard() {

        // Nested for loop to go through entire board
        for (int row = 0; row < 4; row++) {

            for (int column = 0; column < 4; column++) {

                // Print statement that prints out every character/position eventually
                System.out.print(board[row][column].getTop() + " ");
            }

            // New line print statement added for formatting reasons
            System.out.println();
        }
    }

    /**
     * Method to find all the words currently on the board. Goes through every possible combination
     * for every position. Words found must be at least 4 characters long and a member of the
     * lexicon dictionary built in the constructor.
     *
     * @param row is the name of the row sent in to the method, accepted as an int
     * @param column is the name of the column position sent to the method, also taken as an int
     * @param prefix
     */

    public static void findWords(int row, int column, String prefix) {

        // Sets prefix to whatever is at the top
        prefix += board[row][column].getTop();

        // Checks to see if the "prefix" parameter is in the "prefixes" HashSet
        if (!(dictionary.isPrefix(prefix))) {
            return;
        }

        // Checks to see if the "prefix" parameter is in the "words" HashSet, is so adds it to "finalWords" HashSet
         if (dictionary.isWords(prefix)) {
           finalWords.add(prefix);
        }

         // Sets the current position on board as "used"
        used[row] [column] = true;

         // Loops to go through each possible combination for each point, included are try catch
         // blocks in case a maneuver is ever not possible
             try {
                 if (board[row - 1][column] != null && !used[row - 1][column]) {
                     findWords(row - 1, column, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row - 1][column + 1] != null && !used[row - 1][column + 1]) {
                     findWords(row - 1, column + 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row][column + 1] != null && !used[row][column + 1]) {
                     findWords(row, column + 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row + 1][column + 1] != null && !used[row + 1][column + 1]) {
                     findWords(row + 1, column + 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row + 1][column] != null && !used[row + 1][column]) {
                     findWords(row + 1, column, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row + 1][column - 1] != null && !used[row + 1][column - 1]) {
                     findWords(row + 1, column - 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row][column - 1] != null && !used[row][column - 1]) {
                     findWords(row, column - 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             try {
                 if (board[row - 1][column - 1] != null && !used[row - 1][column - 1]) {
                     findWords(row - 1, column - 1, prefix);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored) {
             }

             // Sets the position back to "unused"
                used[row] [column] = false;
         }

    /**
     * Class that handles the populating of the valid words and prefixes in the game. Stores
     * them in separate HashSets while utilizing the same file to fill both.
     */

    static class Lexicon {

        // String HashSet created called "words" to be filled in later
        HashSet<String> words = new HashSet<>();

        // String HashSet created called "prefixes" to be filled in later
        HashSet<String> prefixes = new HashSet<>();

        /**
         * Method that populates the "words" and "prefixes" HashSet using a provided file. Goes through
         * the file and adds the corresponding contexts to each HashSet.
         * @throws FileNotFoundException is what is thrown in the chance the read file is not there when ran
         */

        public Lexicon (String filename) throws FileNotFoundException {

            // New Scanner to go through the file
            Scanner file = new Scanner((new File(filename)));

            // Continues so long as file has content
            while (file.hasNext()) {

                // Create a temporary String to hold the following file content
                String wordLength = file.next();

                // As our game only allows words of length 4 or more checks to see if it is so
                // before being added to the valid "words" HashSet
                if (wordLength.length() >= 4) {
                    words.add(wordLength.trim());
                }
            }

            // Closes the file
            file.close();

            // For loop to go through the words HashSet
            for (String word : words) {

                for (int i = 0; i <= (word).length(); i++) {

                    // Adds all possible prefixes to thr "prefixes" HashSet
                    prefixes.add((word).substring(0, i));
                }
            }
        }

        /**
         * Method to decide whether what was sent in is a valid word in the game
         * @param str is the word sent in that is looked for in the "words" HashSet
         * @return is either true or false
         */

        public boolean isWords (String str) {

            // Single line that returns either "true" or "false" depending if the sent in parameter exists in "words"
            return words.contains(str);

        }

        /**
         * Method to decide whether what was sent in is a valid prefix in the game
         * @param str is the string sent in and looked for in the "prefixes" HashSet
         * @return
         */

        public boolean isPrefix(String str) {

            // Single line that returns either "true" or "false" depending if the sent in parameter exists in "prefixes"
            return prefixes.contains(str);
        }
    }

    /**
     * Class that handles the creation and populating of the die used for the game.
     */

    public static class boggleDie {

        // Int variable to be used later as the "top" of the die
        int top;

        // String array of size 6 to be used later
        String[] die = new String[6];

        /**
         * Method that goes through the side chosen for the die
         * @param sides is the name for the string sent in to the method
         */

        boggleDie(String sides) {

            // Loop to go through string created at top of boggleDie class
            for (int i = 0; i < 6; i++) {

                // Sets the position of the die equal to the
                die[i] = sides.substring(i, i + 1);

            }

            // Calls roll method on now populated die object
            roll();
        }

        /**
         * Method to "roll" the die. Simply chooses one of the random six sides and assigns it as "top"
         * @return in this case is the "top" of the die
         */

        public int roll() {

            // Randomly sets the top equal to one of the six sides
            top = (int) (Math.random() * 6);

            // New top is returned
            return top;
        }

        /**
         * Method to return the top value of the die
         * @return is what actually gets the character/top value
         */

        public String getTop() {

            // Returns the top position of the die
            return die[top];
        }
    }

    /**
     * Method that checks the board for any and all possible words on the board
     */

    private static void checkBoard() {

        // Nested for loop that checks each position on the board
        for (int row = 0; row < board.length; row ++) {

            for (int column = 0; column <board[0].length; column++) {

                findWords(row,column, "");
            }
        }
    }

    /**
     * Method that prints out all the words found in the board
     */

    private static void printWords() {

        // New line for formatting reasons
        System.out.println();

        System.out.println("List of found words in the game are:");

        // Loop to go through the entire TreeSet declared at top and print out the contents
        for (String currentWord : finalWords) {

                System.out.print(currentWord + " ");
        }

        // New line for formatting reasons
        System.out.println();
    }

    /**
     * Main method that utilizes all the methods created above in order to simulate a game of Boggle
     * @param args
     * @throws FileNotFoundException as the file for the dice is read here, an exception is required in the chance
     * the file is not there when ran
     */

    public static void main(String[] args) throws FileNotFoundException {

        Boggle chance = new Boggle();

        // Scanner to read in the "dice.txt" file that hold what characters appears on the dice
        Scanner diceFile = new Scanner(new File("dice.txt "));

        // Nested for loop to go through entire board
        for (int row = 0; row < board.length; row++) {

            for (int column = 0; column < board[0].length; column++) {

                // String to hold in the following contents of the scanner
                String getDie = diceFile.next();

                // boggleDie object that utilizes the string created above
                boggleDie hope = new boggleDie(getDie);

                // Sets the current board position equal to the boggleDie object created above
                board[row][column] = hope;
            }
        }

        System.out.println("Game before shuffling");

        // Prints the board via calling the printBoard method
        printBoard();

        // New line for formatting purposes
        System.out.println();

        System.out.println("Game after shuffling");

        // Shuffles the board
        shuffle();

        // Prints the board again
        printBoard();

        // Checks every position on the board for words
        checkBoard();

        // Prints the words found in the board via calling the "printWords" method
        printWords();
    }
}