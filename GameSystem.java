/** @author Afonso deSousa */

/** class to operate the players according to the Main.java inputs through Player.java and Board.java methods; */
public class GameSystem {
    private Player[] players;
    private Player[] elimination;
    private Player[] ranking;
    private Player winner;
    private int next = 0;
    private final int squares;
    private final Board board;


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
        return winner;
    }

    /** check if any player has reached the last tile / if any player has won; */
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
            winner = turnPlayer;
            turnPlayer.addWin();
        }

        next ++;
        if (next == players.length) {
            next = 0;
        }
        getNextPlayer();
        
    }

    public void runSquare(Player player, int sum) {
        String action = board.getTile(player.getPosition());
        switch (action) {
            case "1": player.setFines(1); break;
            case "2": player.setFines(2); break;
            case "3": player.setFines(3); break;
            case "4": player.setFines(4); break;
            case "BIRD": player.applyBird(player.getPosition()); break;
            case "CRAB": player.applyCrab(sum); break;
            case "HELL": player.applyHell(); break;
            case "DEATH": player.eliminate();
                          cutPlayer(player);
                          break;

        }

    }

    private void cutPlayer(Player eliminated) {
        int idx = 0;
        for (int i = 0; i < players.length; i ++) {
            if (eliminated == players[i]) {
                idx = i;
            }
        }

        for (int x = idx; x < players.length - 1; x ++) {
            players[x] = players[x + 1];
        }

        addEliminated(eliminated);

    }

    private void addEliminated(Player eliminated) {
        Player[] grown = new Player[elimination.length + 1];

        for (int i = 0; i < elimination.length; i ++) {
            grown[i] = elimination[i];
        }

        elimination = grown;
        elimination[elimination.length - 1] = eliminated;
    }

    public void rank() {

    }



}





