/** @author Afonso deSousa */


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
    private static final String CAN_DICE = "can roll the dice";
    private static final String CANNOT_DICE = "cannot roll the dice";
    private static final String INVALID_COMMAND = "Invalid Command";
    private static final String INVALID_DICE = "Invalid dice";
    private static final String NOT_OVER = "The cup was not over yet...";
    private static final String CUP_OVER = "The cup is over";
    private static final String INVALID_PLAYER = "Nonexistent player";
    private static final String ELIMINATED_PLAYER = "Eliminated player";


    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        FileReader fileReader = new FileReader("boards.txt");
        Scanner reader = new Scanner(fileReader);

        String entry = input.nextLine();
        int board = input.nextInt();

        findBoard(reader, board);

        int tiles = reader.nextInt();

        GameSystem central = new GameSystem(entry, tiles);

        int numFines = reader.nextInt();
        for (int i = 0; i < numFines; i ++) {
            central.addFine(reader.nextInt(), reader.nextInt());
        }

        int numFalls = reader.nextInt();
        for (int x = 0; x < numFalls; x ++) {
            central.addFall(reader.nextInt(), reader.nextLine().trim());
        }

        String prompt;
        do {
            prompt = input.next();
            runCommand(prompt, central, input);
        } while (!prompt.equals(EXIT));


        input.close();
    }

    private static void findBoard(Scanner reader, int board) {
        for (int i = 1; i < board; i ++) {
            reader.nextLine();
            int foulFine = reader.nextInt();
            for (int x = 0; x < foulFine; x ++) {
                reader.nextLine();
            }
            int foulFall = reader.nextInt();
            for (int y = 0; y < foulFall; y ++) {
                reader.nextLine();
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
     * prints the next Player to play if the game is not yet won; */
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
                if (central.getStatus(selected)) {
                    System.out.println(selected.getName() + CAN_DICE);
                }
                else {
                    System.out.println(selected.getName() + CANNOT_DICE);
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
     * receives, validates and adds both dices' numbers to the Player's position if the game is not yet won; */
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

    public static void runRanking(GameSystem central) {
        for (int i = 0; i < central.numPlayers(); i ++) {
            Player player = central.getPlayer(i);
            System.out.print(player.getName() + ": " + player.getWins() + " games won; ");
        }

        for (int i = central.numEliminated(); i >= 0; i --) {
            Player player = central.getEliminated(i);
            System.out.print(player.getName() + ": " + player.getWins() + " games won; eliminated.");
        }


    }

    /** @param central class to process the inputs with the other classes;
     * ends the game and prints if any Player reached the last square; */
    public static void runExit(GameSystem central) {
        if (central.isCupOn()) {
            System.out.println(NOT_OVER);
        }
        else {
            System.out.println(central.getWinner().getName() + " won the cup!");
        }

    }
    

}
