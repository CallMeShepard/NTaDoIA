import java.sql.*; 
import java.util.Scanner;

class Db {
    String dbUrl = "jdbc:mysql://localhost:3306/myGame?useSSL=false";
    String user = "root"; 
    String password = "GbQV4!yv5l2!jn4ki";
    Connection con;

    public Db() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
            this.con = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("Connection established successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void close() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isUserExists(String username, String password) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM users WHERE username='" + username + "' AND password='" + password + "';");
            while (rs.next())
                if (rs.getInt(1) == 1) return true;
                else return false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}

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

public class Lab4_TicTacToeGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Db db = new Db();  // Create database connection

        Player player1 = null, player2 = null;

        // Log in the first player
        while (player1 == null) {
            System.out.println("[1] :: Enter the username of the first player (X):");
            String username = scanner.nextLine();
            System.out.println("[1] :: Enter the password of the first player:");
            String password = scanner.nextLine();
            if (db.isUserExists(username, password)) {
                player1 = new Player(username, 'X');
            } else {
                System.out.println("Invalid username or password. Try again.");
            }
        }

        // Log in the second player
        while (player2 == null) {
            System.out.println("[2] :: Enter the username of the second player (O):");
            String username = scanner.nextLine();
            System.out.println("[2] :: Enter the password of the second player:");
            String password = scanner.nextLine();
            if (db.isUserExists(username, password)) {
                player2 = new Player(username, 'O');
            } else {
                System.out.println("Invalid username or password. Try again.");
            }
        }

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
                System.out.println("[!] Congrats, " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ") :: You have won [!]");
            } else {
                // switch players
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
            }
        }

        if (!gameWon && board.isFull()) {
            board.printBoard();
            System.out.println("[!] The game ended in a draw [!]");
        }

        db.close();  // close the database connection
    }
}
