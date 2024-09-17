import java.util.Scanner;

class Player {
    private String name;
    private char symbol;  // 'X' or 'O'

    public Player(String name, char symbol) { // player constructor
        this.name = name;
        this.symbol = symbol;
    }

    public int[] makeMove(GameBoard board) {
        Scanner scanner = new Scanner(System.in);
        int[] move = new int[2]; // row and column
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("> " + name + " (" + symbol + "), enter your move coordinates :: [row] and [column] separated by space: ");
                move[0] = Integer.parseInt(scanner.next());
                move[1] = Integer.parseInt(scanner.next());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("\n:: Invalid input : Please enter two numbers ::");
                board.printBoard();
            }
        }
        return move;
    }

    public String getName() { return name; }

    public char getSymbol() { return symbol; }
}

class GameBoard {
    private char[][] board = new char[3][3]; // 3x3 game board

    public GameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public void printBoard() {
        System.out.println("\nn 0 1 2");

        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method to change the state of the game board
    public boolean changeBoard(int row, int col, char symbol) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != '-') {
            System.out.println("\n:: Invalid move : Try again ::");
            printBoard();
            return false;
        }
        board[row][col] = symbol;
        return true;
    }

    public boolean checkWin(char symbol) {
        // check rows + columns + diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }
        // check diagonals
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }
        return false;
    }

    // to check if the board is full (draw)
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}

public class Lab3_TicTacToeGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println();
        System.out.println("[1] :: Enter the name of the first player (X):");
        String player1Name = scanner.nextLine();
        Player player1 = new Player(player1Name, 'X');
        
        System.out.println("\n[2] :: Enter the name of the second player (O):");
        String player2Name = scanner.nextLine();
        Player player2 = new Player(player2Name, 'O');
        
        GameBoard board = new GameBoard();
        Player currentPlayer = player1;
        boolean gameWon = false;

        // main game loop
        while (!board.isFull() && !gameWon) {
            board.printBoard();
            int[] move;
            boolean validMove;

            // current player's move
            do {
                move = currentPlayer.makeMove(board);
                validMove = board.changeBoard(move[0], move[1], currentPlayer.getSymbol());
            } while (!validMove);

            // check for a win
            gameWon = board.checkWin(currentPlayer.getSymbol());

            if (gameWon) {
                board.printBoard();
                System.out.println("[!] Congrats, " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")" + " :: You have won [!]");
            } else {
                // switch players
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }
        }

        if (!gameWon && board.isFull()) {
            board.printBoard();
            System.out.println("[!] The game ended in a draw [!]");
        }
    }
}