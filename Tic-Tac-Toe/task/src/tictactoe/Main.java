package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        final char[] board = new char[9];
        Arrays.fill(board, '_');
        try (Scanner in = new Scanner(System.in)) {
            boolean xRole = true;
            Main.printBoard(board);
            do {
                int targetIndex, x, y;
                do {
                    System.out.print("Enter the coordinates: ");
                    String[] words = in.nextLine().split(" ");
                    if (words.length != 2) {
                        System.out.println("You should enter numbers!");
                        continue;
                    }
                    try {
                        x = (Integer.parseInt(words[0]) - 1);
                        y = Integer.parseInt(words[1]) - 1;
                        if (x > 2 || y > 2 || x < 0 || y < 0) {
                            System.out.println("Coordinates should be from 1 to 3!");
                            continue;
                        }
                        if (board[targetIndex = x * 3 + y] != '_') {
                            System.out.println("This cell is occupied! Choose another one!");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("You should enter numbers!");
                    }
                } while (true);
                board[targetIndex] = xRole ? 'X' : 'O';
                Main.printBoard(board);
                final GameResult gameResult = Main.analyseResult(board);
                if (gameResult == GameResult.DRAW || gameResult == GameResult.O || gameResult == GameResult.X) {
                    System.out.println(gameResult);
                    return;
                }
                xRole = !xRole;
            } while (true);
        }

    }

    private static void printBoard(char[] board) {
        System.out.println("---------");
        for (int i = 0; i < board.length; ++i) {
            if (i % 3 == 0) System.out.print("| ");
            System.out.print((board[i] == '_' ? " " : board[i]) + " ");
            if (i % 3 == 2) System.out.println("|");
        }
        System.out.println("---------");
    }

    private static GameResult analyseResult(char[] board) throws Exception {
        int x = 0, o = 0;
        for (char c : board) {
            if (c == 'X') ++x;
            if (c == 'O') ++o;
        }
        if (x - o > 1 || x - o < -1) return GameResult.IMPOSSIBLE;
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
