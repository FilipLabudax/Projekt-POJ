package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sudoku {

    public static final int PUSTE = 0; // puste komorki = 0
    public static final int ROZMIAR = 9; // rozmiar planszy sudoku

    //Definicja planszy do rozwiazania w tablicy dwuwymiarowej
    public static int[][] org_plansza =
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
    //Definicja rozwiazanej planszy
    public static int[][] rozwiazanie =
            {{9, 5, 7, 6, 1, 3, 2, 8, 4},
            {4, 8, 3, 2, 5, 7, 1, 9, 6},
            {6, 1, 2, 8, 4, 9, 5, 3, 7},
            {1, 7, 8, 3, 6, 4, 9, 5, 2},
            {5, 2, 4, 9, 7, 1, 3, 6, 8},
            {3, 6, 9, 5, 2, 8, 7, 4, 1},
            {8, 4, 5, 7, 9, 2, 6, 1, 3},
            {2, 9, 1, 4, 3, 6, 8, 7, 5},
            {7, 3, 6, 1, 8, 5, 4, 2, 9},
            };


    public static void ustawienia() {
        boolean built = true;
        while (!built)
            rozw_sudoku();

        //Definiowanie ramki 500x500px
        JFrame ramka = new JFrame();
        ramka.setTitle("Sudoku");
        ramka.setSize(500, 500);
        ramka.setResizable(false);
        JPanel panel = new JPanel(new BorderLayout());

        //Definiowanie planszy grywalnej 400x400px
        JPanel plansza = new JPanel();
        plansza.setPreferredSize(new Dimension(400, 400));
        plansza.setLayout(new GridLayout(ROZMIAR, ROZMIAR)); //Tworzenie okien 9x9
        JTextField[][] wolne = new JTextField[ROZMIAR][ROZMIAR];
        for (int i = 0; i < ROZMIAR; i++)
            for (int j = 0; j < ROZMIAR; j++) {
                JTextField field = new JTextField();
                field.setHorizontalAlignment(JTextField.CENTER);
                if (org_plansza[i][j] != 0) {
                    field.setText(Integer.toString(org_plansza[i][j]));
                    field.setEditable(false);
                }
                if (((i + 1) % (int) Math.sqrt(ROZMIAR)) == 0 && ((j + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    field.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                else if (((i + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    field.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                else if (((j + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                else
                    field.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                plansza.add(field);
                wolne[i][j] = field;
            }

        //tworzenie przyciskow
        JPanel panel_sterowania = new JPanel(new FlowLayout());
        JButton wyczysc = new JButton("Wyczysc");
        JButton wykonaj = new JButton("Wykonaj");
        JButton rozwiaz = new JButton("Rozwiaz");
        JButton nowa_gra = new JButton("Nowa Gra");

        //dodawanie fukncji do przycisku wyczysc
        panel_sterowania.add(wyczysc);
        wyczysc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < ROZMIAR; i++)
                    for (int j = 0; j < ROZMIAR; j++)
                        if (org_plansza[i][j] == 0)
                            wolne[i][j].setText("");
            }
        });
        //dodawanie fukncji do przycisku wykonaj
        panel_sterowania.add(wykonaj);
        wykonaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] rozwiazanie = new int[ROZMIAR][ROZMIAR];
                boolean puste = true;
                for (int i = 0; i < ROZMIAR; i++) {
                    for (int j = 0; j < ROZMIAR; j++)
                        if (wolne[i][j].getText().isEmpty()) {
                            puste = false;
                            break;
                        }
                    if (!puste)
                        break;
                }
                if (!puste)
                    JOptionPane.showMessageDialog(new JFrame(), "Wpisz liczby do kazdego pola");
                else {
                    boolean poprawne = true;
                    for (int i = 0; i < ROZMIAR; i++) {
                        for (int j = 0; j < ROZMIAR; j++) {
                            try {
                                rozwiazanie[i][j] = Integer.parseInt(wolne[i][j].getText());
                                if (rozwiazanie[i][j] <= 0 || rozwiazanie[i][j] > ROZMIAR) {
                                    poprawne = false;
                                    break;
                                }
                            } catch (Exception exp) {
                                poprawne = false;
                                break;
                            }
                        }
                        if (!poprawne)
                            break;
                    }
                    if (!poprawne)
                        JOptionPane.showMessageDialog(new JFrame(), "Podales zla wartosc");
                    else {
                        boolean correct = false;
                        for (int i = 0; i < ROZMIAR; i++) {
                            for (int j = 0; j < ROZMIAR; j++) {
                                correct = dostepna();
                                if (!correct)
                                    break;
                            }
                            if (!correct)
                                break;
                        }
                        if (!correct)
                            JOptionPane.showMessageDialog(new JFrame(), "Podales zla odpowiedz, sproboj jeszcze raz");
                        else
                            JOptionPane.showMessageDialog(new JFrame(), "Gratulacje, podales prawidlowa odpowiedz!");

                    }
                }
            }

        });
        //dodawanie fukncji do przycisku rozwiaz
        panel_sterowania.add(rozwiaz);
        rozwiaz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < ROZMIAR; i++)
                    for (int j = 0; j < ROZMIAR; j++)
                        wolne[i][j].setText(Integer.toString(rozwiazanie[i][j]));
            }
        });
        //dodawanie fukncji do przycisku nowa_gra
        panel_sterowania.add(nowa_gra);
        nowa_gra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ramka.dispose();
                ustawienia();
            }
        });

        //Wyswietlanie pola grywalnego i ramki
        panel.add(plansza, BorderLayout.NORTH);
        panel.add(panel_sterowania, BorderLayout.SOUTH);
        ramka.add(panel);
        ramka.setVisible(true);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //wyjscie po nacisnieci X
    }

    private static boolean dostepna() {
        return true;
    }

    //tworzenie planszy 9x9
    public Sudoku(int[][] org_plansza) {
        this.org_plansza = new int[ROZMIAR][ROZMIAR];

        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                this.org_plansza[i][j] = org_plansza[i][j];
            }
        }
    }

    // sprawdzenie czy liczba jest dostepna w wierszu i kolumnie
    private static boolean dostepna(int wiersz, int kolumna, int xy) {
        return !sprWiersz(wiersz, xy) && !sprKolumna(kolumna, xy) && !sprBox(wiersz, kolumna, xy);
    }


    // sprawdzenie czy liczba jest juz dostepna w wierszu
    private static boolean sprWiersz(int wiersz, int x) {
        for (int i = 0; i < ROZMIAR; i++)
            if (org_plansza[wiersz][i] == x) return true;

        return false;
    }

    // sprawdzenie czy liczba jest juz dostepna w kolumnie
    private static boolean sprKolumna(int kolumna, int y) {
        for (int i = 0; i < ROZMIAR; i++)
            if (org_plansza[i][kolumna] == y) return true;

        return false;
    }

    // sprawdzenie czy dostepna liczba jest w siatce 3x3
    private static boolean sprBox(int wiersz, int kolumna, int spr) {
        int w = wiersz - wiersz % 3;
        int k = kolumna - kolumna % 3;

        for (int i = w; i < w + 3; i++)
            for (int j = k; j < k + 3; j++)
                if (org_plansza[i][j] == spr) return true;

        return false;
    }

    // Rozwiazanie sudoku.
    public static boolean rozw_sudoku() {
        for (int wiersz = 0; wiersz < ROZMIAR; wiersz++) {
            for (int kolumna = 0; kolumna < ROZMIAR; kolumna++) {
                // szukanie pustego miejsca (0)
                if (org_plansza[wiersz][kolumna] == PUSTE) {
                    // szukanie dostepnych liczb
                    for (int liczba = 1; liczba <= ROZMIAR; liczba++) {
                        if (dostepna(wiersz, kolumna, liczba)) {
                            // sprawdzenie czy liczba jest dostepna w wierszu i kolumnie
                            org_plansza[wiersz][kolumna] = liczba;

                            if (rozw_sudoku()) { // rekurencja
                                return true;
                            } else { // jesli nie ma rozwiazania, komorka jest zerowana
                                org_plansza[wiersz][kolumna] = PUSTE;
                            }
                        }
                    }
                    return false;
                }
            }
        }

        return true; // sudoku rozwiazanie
    }
}
