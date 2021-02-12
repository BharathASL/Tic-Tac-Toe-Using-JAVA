package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("Enter cells: ");
        final char[] board;
        try (Scanner in = new Scanner(System.in)) {
            String line = in.nextLine();
            if (line.length() < 9) {
                return;
            }
            board = line.toCharArray();
        }
        System.out.println("---------");
        int x = 0, o = 0;
        for (int i = 0; i < board.length; ++i) {
            if (i % 3 == 0) System.out.print("| ");
            System.out.print(board[i] + " ");
            if (i % 3 == 2) System.out.println("|");
            if (board[i] == 'X') ++x;
            if (board[i] == 'O') ++o;
        }
        System.out.println("---------");
        if (x - o > 1 || x - o < -1) {
            System.out.println("Impossible");
            return;
        }
        System.out.println(Main.analyseResult(board));
    }

    private static GameResult analyseResult(char[] board) throws Exception {
        char[] scanResult = new char[8];
        scanResult[0] = Main.scanDirection(board, 0, Direction.RIGHT);
        scanResult[1] = Main.scanDirection(board, 0, Direction.BOTTOM);
        scanResult[2] = Main.scanDirection(board, 0, Direction.BOTTOM_RIGHT);
        scanResult[3] = Main.scanDirection(board, 1, Direction.BOTTOM);
        scanResult[4] = Main.scanDirection(board, 2, Direction.BOTTOM_LEFT);
        scanResult[5] = Main.scanDirection(board, 2, Direction.BOTTOM);
        scanResult[6] = Main.scanDirection(board, 3, Direction.RIGHT);
        scanResult[7] = Main.scanDirection(board, 6, Direction.RIGHT);
        int xWins = 0, oWins = 0;
        boolean draw = true, gameNotFinished = false;
        for (int i = 0; i < 8; i++) {
            switch (scanResult[i]) {
                case 'X':
                    draw = false;
                    ++xWins;
                    break;
                case 'O':
                    draw = false;
                    ++oWins;
                    break;
                case 'x':
                case 'o':
                    gameNotFinished = true;
                case '_':
                    draw = false;
                    break;
            }
        }
        if (xWins + oWins > 1) return GameResult.IMPOSSIBLE;
        if (draw) return GameResult.DRAW;
        if (xWins == 1) return GameResult.X;
        if (oWins == 1) return GameResult.O;
        if (gameNotFinished) return GameResult.NOT_FINISHED;
        return GameResult.IMPOSSIBLE;
    }

    enum GameResult {
        IMPOSSIBLE, DRAW, X, O, NOT_FINISHED;

        @Override
        public String toString() {
            switch (this) {
                case DRAW:
                    return "Draw";
                case O:
                    return "O wins";
                case X:
                    return "X wins";
                case NOT_FINISHED:
                    return "Game not finished";
                default:
                    return "Impossible";
            }
        }
    }

    private static char scanDirection(char[] board, int index, Direction checkDirection) throws Exception {
        int n1, n2 = n1 = index;
        switch (checkDirection) {
            case RIGHT: {
                if (index % 3 != 0) throw new Exception("Invalid index and direction sent for scan");
                ++n1;
                n2 += 2;
                break;
            }
            case BOTTOM: {
                if (index > 2) throw new Exception("Invalid index and direction sent for scan");
                n1 += 3;
                n2 += 6;
                break;
            }
            case BOTTOM_LEFT: {
                if (index != 2) throw new Exception("Invalid index and direction sent for scan");
                n1 += 2;
                n2 += 4;
                break;
            }
            case BOTTOM_RIGHT: {
                if (index != 0) throw new Exception("Invalid index and direction sent for scan");
                n1 += 4;
                n2 += 8;
                break;
            }
        }
        if (board[index] == board[n1] & board[n1] == board[n2]) return board[index];
        if (board[index] != '_' && board[n1] != '_' && board[n2] != '_') return '*';
        if ((board[index] == board[n1] || board[index] == board[n2]))
            return Character.toLowerCase(
                    board[index] == '_'
                            ? board[n1] == '_' ? board[n2] : board[n1]
                            : board[index]
            );

        if (board[n1] == board[n2]) return Character.toLowerCase(board[n1]);
        return '_';
    }

    enum Direction {
        BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, RIGHT
    }
}
