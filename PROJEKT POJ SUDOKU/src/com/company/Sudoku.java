package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Scanner;

public class Sudoku{

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

    public Sudoku() {
    }

    public static void run() {
        boolean ustawianie = true;
        while (!ustawianie)
            rozw_sudoku();

        //Definiowanie ramki 500x500px
        JFrame ramka = new JFrame();
        ramka.setTitle("Sudoku");
        ramka.setSize(700, 700);
        ramka.setResizable(false);
        JPanel panel = new JPanel(new BorderLayout());

        //Definiowanie planszy grywalnej 400x400px (D:JComponents)
        JPanel plansza = new JPanel();
        plansza.setPreferredSize(new Dimension(600, 600)); //rozmiar planszy
        plansza.setLayout(new GridLayout(ROZMIAR, ROZMIAR)); //Tworzenie okn 9x9, 1 kol na wiersz
        JTextField[][] wolne = new JTextField[ROZMIAR][ROZMIAR]; //Wprowadzanie tekstu
        for (int i = 0; i < ROZMIAR; i++) //wypelnianie
            for (int j = 0; j < ROZMIAR; j++) {
                JTextField pole = new JTextField();
                pole.setHorizontalAlignment(JTextField.CENTER);//text horizontal, center
                if (org_plansza[i][j] != 0) {
                    pole.setText(Integer.toString(org_plansza[i][j])); //zmiana int na String
                    pole.setEditable(false);
                }
                //ustawianie kolorow tablicy
                if (((i + 1) % (int) Math.sqrt(ROZMIAR)) == 0 && ((j + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    pole.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK));
                else if (((i + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    pole.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK));
                else if (((j + 1) % (int) Math.sqrt(ROZMIAR)) == 0)
                    pole.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK));
                else
                    pole.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
                plansza.add(pole); //dodanie pola do planszy
                wolne[i][j] = pole;
            }

        //tworzenie przyciskow
        JPanel panel_sterowania = new JPanel(new FlowLayout());
        JButton wyczysc = new JButton("Wyczysc");
        JButton wykonaj = new JButton("Wykonaj");
        JButton rozwiaz = new JButton("Rozwiaz");
        JButton nowa_gra = new JButton("Nowa Gra");
        JButton zapisz = new JButton("Zapisz");
        JButton wczytaj = new JButton("Wczytaj");
        JButton ranking = new JButton("Ranking");

        //dodawanie fukncji do przycisku wyczysc
        panel_sterowania.add(wyczysc);
        wyczysc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < ROZMIAR; i++)
                    for (int j = 0; j < ROZMIAR; j++)
                        if (org_plansza[i][j] == 0)
                            wolne[i][j].setText("");
                            wstawianie();
            }
        });
        //dodawanie fukncji do przycisku wykonaj
        panel_sterowania.add(wykonaj);
        wykonaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                                org_plansza[i][j] = Integer.parseInt(wolne[i][j].getText());
                                if (org_plansza[i][j] <= 0 || org_plansza[i][j] > ROZMIAR) {
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
                        boolean odpowiedz = true;
                        odpowiedz = dostepna();
                        if (!odpowiedz)
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
                run();
            }
        });

        //dodawanie fukncji do przycisku zapisz
        panel_sterowania.add(zapisz);
        zapisz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Tworzenie pliku file typu JTextField
                    JTextField file = new JTextField(20);
                    for (int i = 0; i < ROZMIAR; i++) {
                        for (int j = 0; j < ROZMIAR; j++) {
                            file.setText(String.valueOf(wolne[i][j])); //Zbieranie wartosci z planszy
                        }
                    }
                    String destin = "testout.txt"; //Miejsce docelowe zapisu
                    FileWriter fileWriter = new FileWriter(destin); //Zapis do pliku testout.txt
                    file.write(fileWriter);
                    fileWriter.close();
                } catch (Exception exc) { //Obsluga bledu
                    System.out.println(exc);
                }
            }
        });

        //dodawanie fukncji do przycisku wczytaj
        panel_sterowania.add(wczytaj);
        wczytaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Tworzenie pliku file typu StringBuffer
                StringBuffer buffer = new StringBuffer();
                try {
                    FileInputStream fileIn = new FileInputStream("testinput.txt"); //Miejsce docelowe zapisu
                    DataInputStream inputStream = new DataInputStream(fileIn);
                    while (inputStream.available() > 0) {
                        buffer.append(inputStream.readUTF((DataInput) fileIn)); // Zapis do pliku testinput.txt
                    }
                } catch (Exception exc) { //Obsluga bledu
                    System.out.println(exc);
                }
                System.out.println("Content" + buffer.toString()); //Wyswietlanie zawartosci pliku
            }
        });
        //dodawanie fukncji do przycisku ranking
        panel_sterowania.add(ranking);
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Scanner s1 = new Scanner(System.in);
                    String nazwa = s1.nextLine(); //Pobieranie nazwy od uzytkownika z konsoli
                    JTextField ranking = new JTextField(20);
                    ranking.setText(nazwa + '\n'); //Ustawianie textu w ranking
                    String file = "ranking.txt"; //Miejsce docelowe zapisu
                    FileWriter fileWriter = new FileWriter(file, true); //Zapis bez kasowania zawartosci
                    ranking.write(fileWriter); //Zapis do pliku
                    fileWriter.close();
                } catch (Exception exc) { //Obsluga bledu
                    System.out.println(exc);
                }
            }
        });


        //Wyswietlanie pola grywalnego i ramki
        panel.add(plansza, BorderLayout.NORTH);
        panel.add(panel_sterowania, BorderLayout.SOUTH);
        ramka.add(panel);
        ramka.setVisible(true);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //wyjscie po nacisnieciu X
    }

    //tworzenie planszy 9x9
    public void Ustawienia(int[][] org_plansza) {
        this.org_plansza = new int[ROZMIAR][ROZMIAR];

        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                this.org_plansza[i][j] = org_plansza[i][j];
            }
        }
    }

    // sprawdzenie czy aktualna plansza rowna sie rozwiazanej
    private static boolean dostepna() {
        boolean poprawne = true;
        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                if(org_plansza[i][j] != rozwiazanie[i][j]){
                    poprawne = false;
                    break;
                }
            }
        }
        return poprawne;
    }

    // Rozwiazanie sudoku.
    public static boolean rozw_sudoku() {
        for (int wiersz = 0; wiersz < ROZMIAR; wiersz++) {
            for (int kolumna = 0; kolumna < ROZMIAR; kolumna++) {
                // szukanie pustego miejsca (0)
                if (org_plansza[wiersz][kolumna] == PUSTE) {
                    // szukanie dostepnych liczb
                    for (int liczba = 1; liczba <= ROZMIAR; liczba++) {
                        if (dostepna()) {
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

    public static void wstawianie(){
       int [][]plansza = org_plansza;
    }
}
