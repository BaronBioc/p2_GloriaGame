/** @author Afonso deSousa && Miguel Victorino */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Main {

    private static final String PLAYER = "player";
    private static final String SQUARE = "square";
    private static final String STATUS = "status";
    private static final String DICE = "dice";
    private static final String RANKING = "ranking";
    private static final String EXIT = "exit";
    private static final String CAN_DICE = " can roll the dice";
    private static final String CANNOT_DICE = " cannot roll the dice";
    private static final String INVALID_COMMAND = "Invalid command";
    private static final String INVALID_DICE = "Invalid dice";
    private static final String NOT_OVER = "The cup was not over yet...";
    private static final String CUP_OVER = "The cup is over";
    private static final String INVALID_PLAYER = "Nonexistent player";
    private static final String ELIMINATED_PLAYER = "Eliminated player";


    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        FileReader fileReader = new FileReader("D:\\Programming\\IntelliJ IDEA Community Edition 2022.2.3\\Projects\\GloryGame_v2\\src\\board");
        Scanner reader = new Scanner(fileReader);

        String entry = input.nextLine();
        int board = input.nextInt();

        findBoard(reader, board);

        int tiles = reader.nextInt();

        GameSystem central = new GameSystem(entry, tiles);

        int numFines = reader.nextInt();
        for (int i = 0; i < numFines; i ++) {
            int square = reader.nextInt();
            int num_fines = reader.nextInt();
            central.addFine(square,num_fines);
        }

        int numFalls = reader.nextInt();
        for (int x = 0; x < numFalls; x ++) {
            int square = reader.nextInt();
            String type = reader.nextLine().trim();
            central.addFall(square, type);
        }

        String prompt;
        do {
            prompt = input.next();
            runCommand(prompt, central, input);
        } while (!prompt.equals(EXIT));


        input.close();
    }

    /** @param reader scanner to read the txt file;
     *  @param board number of requested board in the txt file;
     *  skips all other boards' number of tiles, fines and falls until the requested board specs are reached;*/
    private static void findBoard(Scanner reader, int board) {
        for (int i = 1; i < board; i ++) {
            reader.nextInt();
            int foulFine = reader.nextInt();
            for (int x = 0; x < foulFine; x ++) {
                reader.nextInt();
                reader.nextInt();
            }
            int foulFall = reader.nextInt();
            for (int y = 0; y < foulFall; y ++) {
                reader.nextInt();
                reader.next();
            }
        }

    }

    /** @param central class to process the inputs with the other classes;
     *  @param input scanner for user inputs;
     * compare the user input with built commands and run the corresponding with MainSystem.java methods; */
    private static void runCommand(String prompt, GameSystem central, Scanner input) {
        switch (prompt) {
            case PLAYER: runPlayer(central);
                        break;
            case SQUARE: runSquare(central, input);
                        break;
            case STATUS: runStatus(central, input);
                        break;
            case DICE: runDice(central, input);
                        break;
            case RANKING: runRanking(central);
                        break;
            case EXIT: runExit(central);
                        break;
            default:
                input.nextLine();
                System.out.println(INVALID_COMMAND);
        }

    }

    /** @param central class to process the inputs with the other classes;
     * prints the next Player to play if the cup is not yet won; */
    private static void runPlayer(GameSystem central) {
        if (central.isCupOn()) {
            System.out.println("Next to play: " + central.getRunnerUp().getName());
        }
        else {
            System.out.println(CUP_OVER);
        }

    }

    /** @param central class to process the inputs with the other classes;
     * @param input scanner for user inputs;
     * receives the letter of the requested Player and prints the requested Player's position; */
    private static void runSquare(GameSystem central, Scanner input) {
        String target = input.nextLine().trim();
        Player selected = central.selectPlayer(target);

        if (selected != null) {
            if (selected.isEliminated()) {
                System.out.println(ELIMINATED_PLAYER);
            }
            else {
            System.out.println(selected.getName() + " is on square " + selected.getPosition());
            }
        }
        else {
            System.out.println(INVALID_PLAYER);
        }

    }

    /** @param central class to process the inputs with the other classes;
     * @param input scanner for user inputs;
     *receives the letter of a Player and prints whether it has fines or not; */
    private static void runStatus(GameSystem central, Scanner input) {
        String target = input.nextLine().trim();
        Player selected = central.selectPlayer(target);

        if (selected != null) {
            if (central.isCupOn()) {
                if (!selected.isEliminated()) {
                    if (central.getStatus(selected)) {
                        System.out.println(selected.getName() + CAN_DICE);
                    }
                    else {
                        System.out.println(selected.getName() + CANNOT_DICE);
                    }
                }
                else {
                    System.out.println(ELIMINATED_PLAYER);
                }
            }
            else {
                System.out.println(CUP_OVER);
            }
        }
        else {
            System.out.println(INVALID_PLAYER);
        }

    }

    /** @param central class to process the inputs with the other classes;
     * @param input scanner for user inputs;
     * receives, validates and adds both dices' numbers to the Player's position if the cup is not yet won; */
    private static void runDice(GameSystem central, Scanner input) {
        int dice1 = input.nextInt();
        int dice2 = input.nextInt();

        if ((Math.max(dice1, dice2) <= 6) && (Math.min(dice1, dice2) >= 1)) {
            if (central.isCupOn()) {
                central.playRound(dice1, dice2);
            }
            else {
                System.out.println(CUP_OVER);
            }
        }
        else {
            System.out.println(INVALID_DICE);
        }

    }

    /**
     * @param central game system
     * prints a list with the ranking of players by order of games won, position and elimination status; */
    public static void runRanking(GameSystem central) {
        for (int i = 0; i < central.numPlayers(); i ++) {
            Player player = central.getRank(i);
            if (player.isEliminated()) {
                System.out.println(player.getName() + ": " + player.getWins() + " games won; eliminated.");
            }
            else {
                System.out.println(player.getName() + ": " + player.getWins() + " games won; on square " + player.getPosition() + ".");
            }
        }

    }

    /** @param central class to process the inputs with the other classes;
     * ends the cup and prints the winner of the cup if it was won; */
    public static void runExit(GameSystem central) {
        if (central.isCupOn()) {
            System.out.println(NOT_OVER);
        }
        else {
            System.out.println(central.getWinner().getName() + " won the cup!");
        }

    }

}