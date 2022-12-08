/** @author Afonso deSousa */

/** class to operate the players according to the Main.java inputs through Player.java and Board.java methods; */
public class GameSystem {
    private final int squares;
    private final Board board;
    private final String NUM_1 = "1";
    private final String NUM_2 = "2";
    private final String NUM_3 = "3";
    private final String NUM_4 = "4";
    private final String BIRD = "BIRD";
    private final String CRAB = "CRAB";
    private final String HELL = "HELL";
    private final String DEATH = "DEATH";

    private Player[] players;
    private Player[] elimination;
    private boolean gameEli;
    private Player[] ranking;
    private int next = 0;



    public GameSystem(String entry, int squares) {
        this.squares = squares;
        board = new Board(squares);
        players = new Player[entry.length()];
        ranking = new Player[entry.length()];
        elimination = new Player[0];
        for (int i = 0; i < entry.length(); i ++) {
            players[i] = new Player(String.valueOf(entry.charAt(i)));
            ranking[i] = players[i];
        }

    }

    public void startNewGame() {
        for (int i = 0; i < players.length; i ++) {
            players[i].setNewGame();
        }

        board.fillDeath();
        if (!gameEli) {
            for (int i = 0; i < players.length; i ++) {

            }
        }
    }

    public int numPlayers() {
        return players.length;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public int numEliminated() {
        return elimination.length;
    }

    public Player getEliminated(int index) {
        return elimination[index];
    }

    public void addFine(int square, int fines) {
        board.addFine(square, fines);
    }

    public void addFall(int square, String type) {
        board.addFall(square, type);

    }

    public Player getWinner() {
        return ranking[0];
    }

    /** check if the number of active players is bigger than 1; */
    public boolean isCupOn() {
        return (players.length != 1);
    }

    /** @param target letter of the player requested;
     * pick the corresponding Player from the letter input; */
    public Player selectPlayer(String target) {
        Player selected = null;

        for (int i = 0; i < players.length; i ++) {
            if (target.equals(players[i].getName())) {
                selected = players[i];
            }
        }

        return selected;
    }

    /** return the next Player to play keeping the order and fines; */
    public Player getRunnerUp() {
        Player runnerUp = null;
        int idx = next;
        int[] fines = new int[players.length];
        for (int i = 0; i < players.length; i ++) {
            fines[i] = players[i].getFines();
        }

        while (runnerUp == null) {
            if (fines[idx] != 0) {
                fines[idx] --;
            }
            else {
                runnerUp = players[idx];
            }

            idx ++;
            if (idx == 3) {
                idx = 0;
            }
        }

        return runnerUp;
    }

    /** checks if the next player can play and if not determines the next player without fines; */
    private void getNextPlayer() {
        while (players[next].hasFines()) {
            players[next].cutFines();
            next ++;
            if (next == players.length) {
                next = 0;
            }
        }

    }

    /** @param player selected player from selectPLayer();
     * check if Player has fines, returns true to 0 fines; */
    public boolean getStatus(Player player) {
        return !player.hasFines();
    }

    /** @param dice1 number of first dice;
     * @param dice2 number of second dice;
     */
    public void playRound(int dice1, int dice2) {
        Player turnPlayer = players[next];
        int sum = dice1 + dice2;

        turnPlayer.moveDice(sum, squares);
        runSquare(turnPlayer, sum);

        if (turnPlayer.getPosition() == squares) {
            turnPlayer.addWin();
            startNewGame();
        }

        next ++;
        if (next == players.length) {
            next = 0;
        }
        getNextPlayer();
        rank();
        
    }

    public void runSquare(Player player, int sum) {
        String action = board.getTile(player.getPosition());
        switch (action) {
            case NUM_1: player.setFines(1); break;
            case NUM_2: player.setFines(2); break;
            case NUM_3: player.setFines(3); break;
            case NUM_4: player.setFines(4); break;
            case BIRD: player.applyBird(player.getPosition()); break;
            case CRAB: player.applyCrab(sum); break;
            case HELL: player.applyHell(); break;
            case DEATH: cutPlayer(player);
                          break;
        }

    }

    private void cutPlayer(Player eli) {
        eli.eliminate();

        int idx = 0;
        for (int i = 0; i < players.length; i ++) {
            if (eli == players[i]) {
                idx = i;
            }
        }

        players = cut(players, idx);
        elimination = grow(elimination);
        elimination[elimination.length - 1] = eli;

        board.cleanDeaths();
        gameEli = true;
    }

    private Player[] grow(Player[] list) {
        Player[] grown = new Player[list.length + 1];
        for (int i = 0; i < list.length; i ++) {
            grown[i] = list[i];
        }

        return grown;
    }

    private Player[] cut(Player[] list, int remove) {
        Player[] cut = new Player[list.length - 1];

        for (int a = 0; a < list.length - 1; a ++) {
            if (a < remove) {
                cut[a] = list[a];
            }
            else {
                cut[a] = list[a + 1];
            }
        }

        return cut;
    }


    public void rank() {
        for (int i = 0; i < ranking.length - 1; i ++) {
            for (int j = i + 1; j < ranking.length; j ++) {
                if (ranking[i].compare(ranking[j]) < 0) {
                    Player plyr = ranking[j];
                    ranking[j] = ranking[i];
                    ranking[i] = plyr;
                }
            }

        }

    }



}





