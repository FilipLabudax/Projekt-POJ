package com.company;
public class Main extends Sudoku {
    public Main(int[][] plansza) {
        super(plansza);
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(org_plansza);
        Sudoku.ustawienia();


    }
}
