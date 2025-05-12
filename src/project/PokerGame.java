package project;

import javax.swing.*;
import java.util.ArrayList;

public abstract class PokerGame {
    protected ArrayList<Card> cardDeck; //Guarda el mazo de 52 cartas
    protected ArrayList<Player> players; //Guarda los jugadores de la partida
    protected int pot; //Bote de apuestas

    protected JFrame gameWindow; //Ventana de juego

    //Inicializar juego de poker
    public PokerGame(){
        cardDeck = new ArrayList<>();
        players = new ArrayList<>();
        gameWindow = new JFrame();
        pot = 0;
    }

    //Pedir jugadores por medio de una ventana grafica
    public abstract void requestPlayers();

    //Crear ventana de juego completo (Es llamado cuando ya se seleccione el numero de jugadores)
    public abstract void createGameWindow(int playersAmount);

    //Fase de apuestas, el comportamiento varía según el modo de poker
    public abstract void betStage();

    //Fase de intercambio de cartas, el comportamiento varía según el modo de poker
    public abstract void distributeCardsStage();

    //Fase de enfrentamiento, se compara el rango de las manos de cartas y muestra los resultados
    public abstract void fightStage();

    //Crear base de la ventana de juego definiendo dimensiones y nombre personalizado
    //Se declara al principio dentro de "createWindow"
    protected void generateBaseWindow(int windowWidth, int windowHeight, String pokerGameName){
        //Crear marco de la ventana de juego
        gameWindow = new JFrame("Proyecto Final - " + pokerGameName);
        gameWindow.setSize(windowWidth, windowHeight);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setLocation(0, 0);
        gameWindow.setResizable(false);
        gameWindow.setLayout(null);
        gameWindow.setVisible(true);
    }
}