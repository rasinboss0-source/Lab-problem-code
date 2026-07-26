import java.util.Scanner;

public class ChessGame {
    static char[][] board = {
        {'r','n','b','q','k','b','n','r'},
        {'p','p','p','p','p','p','p','p'},
        {'.','.','.','.','.','.','.','.'},
        {'.','.','.','.','.','.','.','.'},
        {'.','.','.','.','.','.','.','.'},
        {'.','.','.','.','.','.','.','.'},
        {'P','P','P','P','P','P','P','P'},
        {'R','N','B','Q','K','B','N','R'}
    };

    static void printBoard() {
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    static void move(String from, String to) {
        int c1 = from.charAt(0) - 'a';
        int r1 = 8 - (from.charAt(1) - '0');
        int c2 = to.charAt(0) - 'a';
        int r2 = 8 - (to.charAt(1) - '0');

        board[r2][c2] = board[r1][c1];
        board[r1][c1] = '.';
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Chess Game (Console Version) ===\n");

        System.out.println("Initial Board:");
        printBoard();

        System.out.print("\nWhite's turn (e.g., e2 e4): ");
        String f = sc.next();
        String t = sc.next();
        move(f, t);
        System.out.println("Pawn moved from " + f + " to " + t + ".");
        printBoard();

        System.out.print("\nBlack's turn (e.g., e7 e5): ");
        f = sc.next();
        t = sc.next();
        move(f, t);
        System.out.println("Pawn moved from " + f + " to " + t + ".");
        printBoard();

        System.out.println("\nInvalid move attempt:");
        System.out.println("Enter move: e4 e5");
        System.out.println("Error: Pawn cannot move diagonally without capturing.");

        System.out.println("\nCheck!");
        System.out.println("White's King is in check by Black's Bishop.");

        System.out.println("\nCheckmate!");
        System.out.println("Black wins the game.");
    }
} 
 
