package com.company;

import java.util.Scanner;

public class Sudoku {

    // Definicja planszy do rozwiazania w tablicy dwuwymiarowej
    public static int[][] PLANSZA1 =
            {{0, 0, 0, 0, 0, 0, 2, 0, 0},
             {0, 8, 0, 0, 0, 7, 0, 9, 0},
             {6, 0, 2, 0, 0, 0, 5, 0, 0},
             {0, 7, 0, 0, 6, 0, 0, 0, 0},
             {0, 0, 0, 9, 0, 1, 0, 0, 0},
             {0, 0, 0, 0, 2, 0, 0, 4, 0},
             {0, 0, 5, 0, 0, 0, 6, 0, 3},
             {0, 9, 0, 4, 0, 0, 0, 7, 0},
             {0, 0, 6, 0, 0, 0, 0, 0, 0},
            };

    private int[][] plansza;
    public static final int PUSTE = 0; // puste komorki = 0
    public static final int ROZMIAR = 9; // rozmiar planszy sudoku

    //tworzenie planszy 9x9
    public Sudoku(int[][] plansza) {
        this.plansza = new int[ROZMIAR][ROZMIAR];

        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                this.plansza[i][j] = plansza[i][j];
            }
        }
    }

    // sprawdzenie czy liczba jest juz dostepna w wierszu
    private boolean sprWiersz(int wiersz, int x) {
        for (int i = 0; i < ROZMIAR; i++)
            if (plansza[wiersz][i] == x) return true;

        return false;
    }

    // sprawdzenie czy liczba jest juz dostepna w kolumnie
    private boolean sprKolumna(int kolumna, int y) {
        for (int i = 0; i < ROZMIAR; i++)
            if (plansza[i][kolumna] == y) return true;

        return false;
    }

    // sprawdzenie czy dostepna liczba jest w siatce 3x3
    private boolean sprBox(int wiersz, int kolumna, int spr) {
        int r = wiersz - wiersz % 3;
        int k = kolumna - kolumna % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = k; j < k + 3; j++)
                if (plansza[i][j] == spr) return true;

        return false;
    }

    // sprawdzenie czy liczba jest dostepna w wierszu i kolumnie
    private boolean dostepna(int wiersz, int kolumna, int xy) {
        return !sprWiersz(wiersz, xy) && !sprKolumna(kolumna, xy) && !sprBox(wiersz, kolumna, xy);
    }

    // Rozwiazanie sudoku.
    public boolean solve() {
        for (int wiersz = 0; wiersz < ROZMIAR; wiersz++) {
            for (int kolumna = 0; kolumna < ROZMIAR; kolumna++) {
                // szukanie pustego miejsca (0)
                if (plansza[wiersz][kolumna] == PUSTE) {
                    // szukanie dostepnych liczb
                    for (int liczba = 1; liczba <= ROZMIAR; liczba++) {
                        if (dostepna(wiersz, kolumna, liczba)) {
                            // sprawdzenie czy liczba jest dostepna w wierszu i kolumnie
                            plansza[wiersz][kolumna] = liczba;

                            if (solve()) { // rekurencja
                                return true;
                            } else { // jesli nie ma rozwiazania, komorka jest zerowana
                                plansza[wiersz][kolumna] = PUSTE;
                            }
                        }
                    }
                    return false;
                }
            }
        }

        return true; // sudoku rozwiazanie
    }

    //wyswietlanie planszy
    public void wyswietl() {
        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                System.out.print(" " + plansza[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void wstaw() {
        Scanner s1 = new Scanner(System.in);
        System.out.println("Podaj miejsce w którym chcesz wstawic liczbe:");
        String input = s1.nextLine();
        String[] values = input.split(" ");
        plansza[Integer.parseInt(values[0])][Integer.parseInt(values[1])] = Integer.parseInt(values[2]);
        System.out.println();
        }
    }

