package com.company;

public class Main extends Sudoku{
    public Main(int[][] plansza) {
        super(plansza);
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(PLANSZA1);
        System.out.println("Plansza do rozwiazania:");
        sudoku.wstaw();
        sudoku.wyswietl();

        // rozwiazywanie planszy sudoku
        if (sudoku.solve()) {
            System.out.println("Rozwiazanie Sudoku");
            sudoku.wyswietl();
        } else {
            System.out.println("Nie można rozwiazać");
        }
    }
}
