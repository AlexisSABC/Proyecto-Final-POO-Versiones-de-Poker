package project;

/**
 * Proyecto final - Versiones de Poker
 * desarrollado por:
 *
 * Montoya Ruiz Johab Alejandro
 * Samaniego Aguilar Alexis
 *
 * Para la materia de "Programación Orientada a Objetos"
 * Programa educativo: Ingeniería en computación.
 * Universidad: "Universidad Autonoma de Baja California"
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class TexasHoldEm extends PokerGame{
    //Guardar las Cartas comunitarias
    private ArrayList<Card> communityCards;

    //Guardar cartas deshechas
    private ArrayList<Card> discardCards;

    //Guarda el ID del jugador que es el "The Dealer"
    private int dealerID;

    //Guarda el ID del jugador que es la "Small Blind"
    private int smallBlindID;

    //Guarda el ID del jugador que es la "Big Blind"
    private int bigBlindID;

    //Controla la apuesta de cada ronda
    private int actualBet;

    //Control de turnos del juego
    private int gamePhase;

    //Control de turnos de los jugadores y al ganador
    private int playerTurn;

    //Define que jugador inicia
    private int playerStarts;

    //Definir ruta de archivo de guardado de partida
    private String saveFilePath;

    //Definir objeto File para manejar el archivo
    private File saveFile;

    /**
     * Permitir iniciar juego guardado
     * true = Cargar juego
     * false = Iniciar nuevo juego
     */
    private boolean loadGame;

    /**
     * Llevar control de jugadores que ya apostaron en un turno
     * true = ya aposto
     * false = no ha apostado
     */
    private HashMap<Integer, Boolean> playersAlreadyBet;

    //Llevar control de apuestas de los jugadores
    private HashMap<Integer, Integer> playerBets;

    /**
     * Llevar el control de que jugadores pasaron de realizar la primera apuesta
     * true = ya paso
     * false = no ha pasado
     */
    private HashMap<Integer, Boolean> playersChecked;

    //Etiquetas de jugadores accesibles para todos los metodos
    private ArrayList<JLabel> playerLabels;

    //Etiquetas para mostrar dinero de jugadores
    private ArrayList<JLabel> playersMoney;

    //Etiqueta para el "Dealer"
    private JLabel dealerLabel;

    //Etiqueta para el jugador "Small Blind"
    private JLabel smallBlindLabel;

    //Etiqueta para el jugador "Big Blind"
    private JLabel bigBlindLabel;

    //Etiqueta para indicar turno de jugador
    private JLabel playerActonsLabel;

    //Botón para ejecutar la acción "Check"
    private JButton checkButton;

    //Botón para ejecutar la acción "Bet"
    private JButton betButton;

    //Botón para ejecutar la acción "Call"
    private JButton callButton;

    //Botón para ejecutar la acción "Fold"
    private JButton foldButton;

    //Botón para ejecutar la acción "Raise"
    private JButton raiseButton;

    //Slider para apostar dinero
    private JSlider moneySlider;

    //Etiqueta para mostrar dinero a apostar
    private JLabel newBetLabel;

    //Botón de confirmado de apuestas
    private JButton setBetButton;

    //Etiqueta para mostrar bote
    private JLabel potLabel;

    //Etiqueta para motrar apuesta actual
    private JLabel actualBetLabel;

    //Etiqueta para solicitud de reinicio de juego
    private JLabel requestReplayLabel;

    //Botón para confirmar reinicio de juego
    private JButton replayButton;

    //Botón para rechazar reinicio de juego
    private JButton notReplayButton;

    //Botón para confirmar mano
    private JButton confirmCards;

    //Botón para guardar partida
    private JButton saveGameButton;

    public TexasHoldEm(){
        //Inicializar elementos de la superclase
        super();

        //Guardar ruta de archivo de guardado
        saveFilePath = "src/savedGames/texasholdemsave.txt";
        saveFile = new File(saveFilePath);

        //Inicializar atributos de la clase
        communityCards = new ArrayList<>();
        discardCards = new ArrayList<>();
        playersAlreadyBet = new HashMap<>();
        playerLabels = new ArrayList<>();
        playersMoney = new ArrayList<>();
        playerBets = new HashMap<>();
        playersChecked = new HashMap<>();
        actualBet = 0;
        gamePhase = 1;
        playerTurn = 0;
        dealerID = 0;
        smallBlindID = 0;
        bigBlindID = 0;
        playerStarts = 0;
        loadGame = false;

        //Inicializar elementos propios de la clase
        dealerLabel = new JLabel();
        smallBlindLabel = new JLabel();
        bigBlindLabel = new JLabel();
        playerActonsLabel = new JLabel();
        checkButton = new JButton();
        betButton = new JButton();
        callButton = new JButton();
        foldButton = new JButton();
        raiseButton = new JButton();
        moneySlider = new JSlider();
        newBetLabel = new JLabel();
        setBetButton = new JButton();
        potLabel = new JLabel();
        actualBetLabel = new JLabel();
        requestReplayLabel = new JLabel();
        replayButton = new JButton();
        notReplayButton = new JButton();
        confirmCards = new JButton();
        saveGameButton = new JButton();

        //Inicializar juego
        requestPlayers();
    }

    //Pide los jugadores y les asigna una cantida de dinero
    public void requestPlayers(){
        //Definir ancho y alto de la ventana
        int width = 500;
        int height = 500;

        //Crear ventana principal
        JFrame selectWindow = new JFrame("Proyecto Final - Seleccion de jugadores (Texas Hold'em)");
        selectWindow.setSize(width, height);
        selectWindow.setLocation(0, 0);
        selectWindow.setResizable(false);
        selectWindow.setLayout(null);
        selectWindow.setVisible(true);

        //Título
        JLabel texasTitle = new JLabel("Texas Hold'em");
        texasTitle.setSize(350, 60);
        texasTitle.setFont(new Font("Arial", Font.BOLD, 40));
        texasTitle.setLocation((width - texasTitle.getWidth()) / 2, 20);
        texasTitle.setVerticalAlignment(SwingConstants.CENTER);
        texasTitle.setHorizontalAlignment(SwingConstants.CENTER);
        texasTitle.setForeground(Color.BLACK);
        texasTitle.setVisible(true);
        selectWindow.add(texasTitle);

        //Instrucciones
        JLabel instructions = new JLabel("<html> Selecciona cantidad de <br>jugadores a participal </html>");
        instructions.setSize(350, 60);
        instructions.setFont(new Font("Arial", Font.BOLD, 20));
        instructions.setLocation((width - instructions.getWidth()) / 2, 80);
        instructions.setVerticalAlignment(SwingConstants.CENTER);
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        instructions.setForeground(Color.BLACK);
        instructions.setVisible(true);
        selectWindow.add(instructions);

        //Lista de seleccion de jugadores
        JComboBox players = new JComboBox();
        players.setSize(80, 50);
        players.setFont(new Font("Arial", Font.BOLD, 24));
        players.setLocation((width - players.getWidth()) / 2, 160);
        players.setForeground(Color.BLACK);
        players.setVisible(true);
        selectWindow.add(players);

        //Agregar jugadores
        for(int i = 2; i <= 10; i++){
            players.addItem(i);
        }

        //Texto de informacion
        JLabel extraInfo = new JLabel("Todos los jugadores iniciaran con $1000");
        extraInfo.setSize(350, 60);
        extraInfo.setFont(new Font("Arial", Font.BOLD, 18));
        extraInfo.setLocation((width - extraInfo.getWidth()) / 2, 220);
        extraInfo.setVerticalAlignment(SwingConstants.CENTER);
        extraInfo.setHorizontalAlignment(SwingConstants.CENTER);
        extraInfo.setForeground(Color.BLACK);
        extraInfo.setVisible(true);
        selectWindow.add(extraInfo);

        //Botón para jugar
        JButton playButton = new JButton("Iniciar juego");
        playButton.setSize(200, 60);
        playButton.setFont(new Font("Arial", Font.BOLD, 26));
        playButton.setLocation((width - playButton.getWidth()) / 2, 290);
        playButton.setVerticalAlignment(SwingConstants.CENTER);
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.setBorder(new LineBorder(Color.BLACK, 3));
        playButton.setBackground(Color.LIGHT_GRAY);
        playButton.setForeground(Color.BLACK);
        playButton.setVisible(true);
        selectWindow.add(playButton);

        //Botón para cargar juego
        JButton loadGameButton = new JButton("Cargar partida");
        loadGameButton.setSize(playButton.getWidth(), playButton.getHeight());
        loadGameButton.setFont(new Font("Arial", Font.BOLD, 26));
        loadGameButton.setLocation(playButton.getX(), 360);
        loadGameButton.setVerticalAlignment(SwingConstants.CENTER);
        loadGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        loadGameButton.setBorder(new LineBorder(Color.BLACK, 3));
        loadGameButton.setBackground(Color.LIGHT_GRAY);
        loadGameButton.setForeground(Color.BLACK);
        loadGameButton.setVisible(true);
        loadGameButton.setEnabled(false);
        selectWindow.add(loadGameButton);

        //Botón para confirmar nuevo juego
        JButton confirmNewGame = new JButton("Confirmar");
        confirmNewGame.setSize(playButton.getWidth(), playButton.getHeight());
        confirmNewGame.setFont(new Font("Arial", Font.BOLD, 26));
        confirmNewGame.setLocation(playButton.getX(), 360);
        confirmNewGame.setVerticalAlignment(SwingConstants.CENTER);
        confirmNewGame.setHorizontalAlignment(SwingConstants.CENTER);
        confirmNewGame.setBorder(new LineBorder(Color.BLACK, 3));
        confirmNewGame.setBackground(Color.LIGHT_GRAY);
        confirmNewGame.setForeground(Color.BLACK);
        confirmNewGame.setVisible(false);
        selectWindow.add(confirmNewGame);

        //Evento para Botón de juego
        playButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(saveFile.exists() && saveFile.isFile()){
                    //Confirmar nuevo juego
                    confirmNewGame.setVisible(true);
                    loadGameButton.setVisible(false);
                    playButton.setVisible(false);
                    players.setVisible(false);

                    //Mostrar advertencia
                    texasTitle.setText("CUIDADO");
                    instructions.setText("Si inicias una nueva partida");
                    extraInfo.setText("Borraras la partida anterior");

                }else{
                    createGameWindow((Integer) players.getSelectedItem());
                    selectWindow.dispose();
                }
            }
        });

        loadGameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //Iniciar juego pregargado
                loadGame = true;
                createGameWindow(0);
                selectWindow.dispose();
            }
        });

        confirmNewGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //Borar archivo de juego guardado
                saveFile.delete();

                //Iniciar nuevo juego
                createGameWindow((Integer) players.getSelectedItem());
                selectWindow.dispose();
            }
        });

        //Activar botón si existe el archivo "texasholdemsave.txt"
        if(saveFile.exists() && saveFile.isFile()){
            loadGameButton.setEnabled(true);
        }

        //Fondo de color
        Container background = selectWindow.getContentPane();
        background.setBackground(Color.WHITE);
    }

    //Crear ventana de juego
    public void createGameWindow(int playersAmount){
        //Dimensiones de la ventana
        int width = 1300;
        int height = 700;

        //Preparar juego segun se haya elegido
        if(loadGame){
            //Cargar juego
            try(BufferedReader recoverData = new BufferedReader(new FileReader(saveFilePath))){
                //Guardar linea de datos
                String data = "";
                
                //Recuperar fase actual del juego
                data = recoverData.readLine();
                gamePhase = Integer.parseInt(data);

                //Recuperar turno actual de jugador
                data = recoverData.readLine();
                playerTurn = Integer.parseInt(data);

                //Recuperar guardado de jugador que inicia
                data = recoverData.readLine();
                playerStarts = Integer.parseInt(data);

                //Recuperar id's de Dealer, Small Blind y Big Blind
                data = recoverData.readLine();
                dealerID = Integer.parseInt(data);

                data = recoverData.readLine();
                smallBlindID = Integer.parseInt(data);

                data = recoverData.readLine();
                bigBlindID = Integer.parseInt(data);

                //Recuperar apuesta actual
                data = recoverData.readLine();
                actualBet = Integer.parseInt(data);

                //Recuperar bote
                data = recoverData.readLine();
                pot = Integer.parseInt(data);

                //Recuperar mazo de cartas
                data = recoverData.readLine();
                int deckSize = Integer.parseInt(data);

                for(int i = 0; i < deckSize; i++){
                    //Guardar linea de datos
                    data = recoverData.readLine();

                    //Descomponer linea en arreglo
                    String vectorData[] = data.split(",");

                    //Preparar propiedades de las cartas
                    int number = Integer.parseInt(vectorData[0]);
                    String figure = vectorData[1];

                    //Crear ruta de imagen de carta
                    String imagePath = "src/cardImages/Carta " + number + " " + figure + ".jpg";

                    //Crear cartas
                    Card card = new Card(number, figure, imagePath, 25, 55, 9);
                    cardDeck.add(card);
                }

                //Recuperar cartas descartadas
                data = recoverData.readLine();
                int discardSize = Integer.parseInt(data);

                for(int i = 0; i < discardSize; i++){
                    //Guardar linea de datos
                    data = recoverData.readLine();

                    //Descomponer linea en arreglo
                    String vectorData[] = data.split(",");

                    //Preparar propiedades de las cartas
                    int number = Integer.parseInt(vectorData[0]);
                    String figure = vectorData[1];

                    //Crear ruta de imagen de carta
                    String imagePath = "src/cardImages/Carta " + number + " " + figure + ".jpg";

                    //Crear cartas
                    Card card = new Card(number, figure, imagePath, 100, 55, 9);
                    discardCards.add(card);
                }

                //Recuperar cartas comunitarias
                data = recoverData.readLine();
                int communitySize = Integer.parseInt(data);

                for(int i = 0; i < communitySize; i++){
                    //Guardar linea de datos
                    data = recoverData.readLine();

                    //Descomponer linea en arreglo
                    String vectorData[] = data.split(",");

                    //Preparar propiedades de las cartas
                    int number = Integer.parseInt(vectorData[0]);
                    String figure = vectorData[1];

                    //Crear ruta de imagen de carta
                    String imagePath = "src/cardImages/Carta " + number + " " + figure + ".jpg";

                    //Crear cartas
                    Card card = new Card(number, figure, imagePath, 250 + (i * 100), 270, 9);
                    communityCards.add(card);
                }

                //Recuperar numero jugadores
                data = recoverData.readLine();
                playersAmount = Integer.parseInt(data);

                //Recuperar datos basicos de jugadores
                for(int i = 0; i < playersAmount; i++){
                    //Guardar linea de datos
                    data = recoverData.readLine();

                    //Descomponer linea en arreglo
                    String vectorData[] = data.split(",");

                    //Preparar propiedades de los jugadores
                    int money = Integer.parseInt(vectorData[0]);
                    boolean status = Boolean.parseBoolean(vectorData[1]);

                    //Crear jugador
                    Player player = new Player(money);
                    playersAlreadyBet.put(i, false);
                    playerBets.put(i, 0);
                    playersChecked.put(i, false);

                    if(status == false){
                        player.fold();
                    }

                    //Obtener longitud de la mano del jugador
                    data = recoverData.readLine();
                    int handSize = Integer.parseInt(data);

                    //Recuperar mano de jugador
                    for(int j = 0; j < handSize; j++){
                        //Guardar linea de datos
                        data = recoverData.readLine();

                        //Descomponer en arreglo
                        String cardData[] = data.split(",");

                        //Preparar propiedades de la carta
                        int number = Integer.parseInt(cardData[0]);
                        String figure = cardData[1];

                        //Crear ruta de imagen de carta
                        String imagePath = "src/cardImages/Carta " + number + " " + figure + ".jpg";

                        //Crear cartas
                        Card card = new Card(number, figure, imagePath, 0, 0, 9);
                        player.addCard(card);
                    }

                    //Agregar jugador a ArrayList
                    players.add(player);
                }

                //Definir al siguiente Dealer, Small Blind y Big Blind
                if (playersAmount == 2){
                    //Determinar Small Blind
                    smallBlindID = dealerID;

                    //Determinar Big Blind
                    bigBlindID = dealerID + 1;
                    do {
                        if (bigBlindID == playersAmount) {
                            bigBlindID = 0;
                        } else {
                            if (players.get(bigBlindID).getMoney() == 0) {
                                bigBlindID++;
                            }
                        }
                    } while ((bigBlindID == playersAmount) || (players.get(bigBlindID).getMoney() == 0));

                } else {
                    //Determinar Small Blind
                    smallBlindID = dealerID + 1;
                    do {
                        if (smallBlindID == playersAmount) {
                            smallBlindID = 0;
                        } else {
                            if (players.get(smallBlindID).getMoney() == 0) {
                                smallBlindID++;
                            }
                        }
                    } while ((smallBlindID == playersAmount) || (players.get(smallBlindID).getMoney() == 0));

                    //Determinar Big Blind
                    bigBlindID = smallBlindID + 1;
                    do {
                        if (bigBlindID == playersAmount) {
                            bigBlindID = 0;
                        } else {
                            if (players.get(bigBlindID).getMoney() == 0) {
                                bigBlindID++;
                            }
                        }
                    } while ((bigBlindID == playersAmount) || (players.get(bigBlindID).getMoney() == 0));
                }

            }catch(IOException e){
                e.printStackTrace();
            }

        }else{
            //Iniciar nuevo juego
            //Crear jugadores
            for(int i = 0; i < playersAmount; i++){
                Player player = new Player(1000);
                players.add(player);
                playersAlreadyBet.put(i, false);
                playerBets.put(i, 0);
                playersChecked.put(i, false);
            }

            //Definir al Dealer, Small Blind y Big Blind
            if(playersAmount == 2){
                dealerID = 0;
                smallBlindID = 0;
                bigBlindID = 1;
            }else{
                dealerID = 0;
                smallBlindID = dealerID + 1;
                bigBlindID = dealerID + 2;
            }

            //Definir jugador de inicio de turnos
            playerStarts = smallBlindID;
            playerTurn = playerStarts;

            //Crear mazo de cartas
            CardDeck deck = new CardDeck(25, 55, 9);
            cardDeck = deck.getCardDeck();

            //Barajear cartas
            Collections.shuffle(cardDeck);
        }

        //Crear base de ventana
        super.generateBaseWindow(width, height, "Texas Hold'em");

        //Crear etiquetas de los jugadores
        for(int i = 1; i <= playersAmount; i++){
            JLabel playerLabel = new JLabel("Jugador " + i);
            playerLabel.setSize(130, 45);
            playerLabel.setLocation(0, 0);
            playerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            playerLabel.setForeground(Color.BLACK);
            playerLabel.setOpaque(true);
            playerLabel.setBackground(Color.WHITE);
            playerLabel.setBorder(new LineBorder(Color.BLACK, 3));
            playerLabel.setVerticalAlignment(SwingConstants.CENTER);
            playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            playerLabel.setVisible(true);
            gameWindow.add(playerLabel);
            playerLabels.add(playerLabel);
        }

        //Crear etiquetas de dinero de jugadores
        for(int i = 0; i < playersAmount; i++){
            JLabel moneyLabel = new JLabel("$" + players.get(i).getMoney());
            moneyLabel.setSize(130, 30);
            moneyLabel.setLocation(0, 0);
            moneyLabel.setFont(new Font("Arial", Font.BOLD, 18));
            moneyLabel.setForeground(Color.BLACK);
            moneyLabel.setOpaque(true);
            moneyLabel.setBackground(Color.WHITE);
            moneyLabel.setBorder(new LineBorder(Color.BLACK, 3));
            moneyLabel.setVerticalAlignment(SwingConstants.CENTER);
            moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            moneyLabel.setVisible(true);
            gameWindow.add(moneyLabel);
            playersMoney.add(moneyLabel);
        }

        //Pocisionar etiquetas de jugadores
        locatePlayerComponents(playersAmount);

        //Reacomodar cartas de los jugadores en caso de que sea juego cargado
        if(loadGame){
            //Ejecutar metodos
            betStage();
            distributeCardsStage();
            fightStage();

            //Mostrar cartas
            discardCards.forEach(card -> {
                gameWindow.add(card.getCard());
            });

            communityCards.forEach(card -> {
                card.turnUpCard();
                gameWindow.add(card.getCard());
            });

            //Mostrar cartas de jugadores
            if(gamePhase >= 2){
                //Acomodar primera hole card
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).getPlayerStatus()){
                        //Definir pocision en X y Y de cartas y moverlas
                        int xPos = playersMoney.get(i).getX();
                        int yPos = playersMoney.get(i).getY() + 40;
                        players.get(i).getHand().get(0).relocateCard(xPos, yPos);
                        gameWindow.add(players.get(i).getHand().get(0).getCard());
                    }
                }

                //Acomodar segunda hole card
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).getPlayerStatus()){
                        //Definir pocision en X y Y de cartas y moverlas
                        int xPos = playersMoney.get(i).getX() + 80;
                        int yPos = playersMoney.get(i).getY() + 40;
                        players.get(i).getHand().get(1).relocateCard(xPos, yPos);
                        gameWindow.add(players.get(i).getHand().get(1).getCard());
                    }
                }
            }

            //Cambiar etiquetas de jugadore si estan fuera del juego
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getPlayerStatus() == false){
                    playerLabels.get(i).setText("Fuera");
                    playersMoney.get(i).setText("-----");
                }
            }
        }

        //Título de mazo
        JLabel deckLabel = new JLabel("Mazo");
        deckLabel.setLocation(10, 10);
        deckLabel.setFont(new Font("Arial", Font.BOLD, 20));
        deckLabel.setBorder(new LineBorder(Color.BLACK, 3));
        deckLabel.setForeground(Color.BLACK);
        deckLabel.setOpaque(true);
        deckLabel.setBackground(Color.WHITE);
        deckLabel.setVerticalAlignment(SwingConstants.CENTER);
        deckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deckLabel.setSize(deckLabel.getPreferredSize().width + 30, deckLabel.getPreferredSize().height + 10);
        deckLabel.setVisible(true);
        gameWindow.add(deckLabel);

        //Mostrar cartas
        cardDeck.forEach(card -> {
            gameWindow.add(card.getCard());
        });

        //Etiqueta de apuesta actual
        actualBetLabel.setText("$" + actualBet);
        actualBetLabel.setSize(160, 45);
        actualBetLabel.setLocation(770, 10);
        actualBetLabel.setFont(new Font("Arial", Font.BOLD, 18));
        actualBetLabel.setBorder(new LineBorder(Color.BLACK, 3));
        actualBetLabel.setForeground(Color.BLACK);
        actualBetLabel.setOpaque(true);
        actualBetLabel.setBackground(Color.WHITE);
        actualBetLabel.setVerticalAlignment(SwingConstants.CENTER);
        actualBetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actualBetLabel.setVisible(true);
        gameWindow.add(actualBetLabel);

        //Etiqueta de bote
        potLabel.setText("Bote = $" + pot);
        potLabel.setSize(160, 45);
        potLabel.setLocation(actualBetLabel.getX(), 60);
        potLabel.setFont(new Font("Arial", Font.BOLD, 18));
        potLabel.setBorder(new LineBorder(Color.BLACK, 3));
        potLabel.setForeground(Color.BLACK);
        potLabel.setOpaque(true);
        potLabel.setBackground(Color.WHITE);
        potLabel.setVerticalAlignment(SwingConstants.CENTER);
        potLabel.setHorizontalAlignment(SwingConstants.CENTER);
        potLabel.setVisible(true);
        gameWindow.add(potLabel);

        //Mostrar jugador que es el "Dealer"
        dealerLabel.setText("The Dealer: Jugador " + (dealerID + 1) + ".");
        dealerLabel.setLocation(950, 10);
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dealerLabel.setBorder(new LineBorder(Color.BLACK, 3));
        dealerLabel.setForeground(Color.BLACK);
        dealerLabel.setOpaque(true);
        dealerLabel.setBackground(Color.WHITE);
        dealerLabel.setVerticalAlignment(SwingConstants.CENTER);
        dealerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dealerLabel.setSize(dealerLabel.getPreferredSize().width + 80, dealerLabel.getPreferredSize().height + 10);
        dealerLabel.setVisible(true);
        gameWindow.add(dealerLabel);

        //Mostrar jugador que dará la "Small Blind"
        smallBlindLabel.setText("Small Blind: Jugador " + (smallBlindID + 1) + ".");
        smallBlindLabel.setLocation(950, dealerLabel.getHeight() + 20);
        smallBlindLabel.setFont(new Font("Arial", Font.BOLD, 20));
        smallBlindLabel.setBorder(new LineBorder(Color.BLACK, 3));
        smallBlindLabel.setForeground(Color.BLACK);
        smallBlindLabel.setOpaque(true);
        smallBlindLabel.setBackground(Color.WHITE);
        smallBlindLabel.setVerticalAlignment(SwingConstants.CENTER);
        smallBlindLabel.setHorizontalAlignment(SwingConstants.CENTER);
        smallBlindLabel.setSize(dealerLabel.getWidth(), dealerLabel.getHeight());
        smallBlindLabel.setVisible(true);
        gameWindow.add(smallBlindLabel);

        //Mostrar jugador que dará la "Big Blind"
        bigBlindLabel.setText("Big Blind: Jugador " + (bigBlindID + 1) + ".");
        bigBlindLabel.setLocation(950, (dealerLabel.getHeight() * 2) + 30);
        bigBlindLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bigBlindLabel.setBorder(new LineBorder(Color.BLACK, 3));
        bigBlindLabel.setForeground(Color.BLACK);
        bigBlindLabel.setOpaque(true);
        bigBlindLabel.setBackground(Color.WHITE);
        bigBlindLabel.setVerticalAlignment(SwingConstants.CENTER);
        bigBlindLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bigBlindLabel.setSize(dealerLabel.getWidth(), dealerLabel.getHeight());
        bigBlindLabel.setVisible(true);
        gameWindow.add(bigBlindLabel);

        //Título de acciones
        playerActonsLabel.setText("Acciones Jugador 2");
        playerActonsLabel.setLocation(950, (dealerLabel.getHeight() * 3) + 80);
        playerActonsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        playerActonsLabel.setBorder(new LineBorder(Color.BLACK, 5));
        playerActonsLabel.setForeground(Color.BLACK);
        playerActonsLabel.setOpaque(true);
        playerActonsLabel.setBackground(Color.WHITE);
        playerActonsLabel.setVerticalAlignment(SwingConstants.CENTER);
        playerActonsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerActonsLabel.setSize(dealerLabel.getWidth(), dealerLabel.getHeight() + 15);
        playerActonsLabel.setVisible(true);
        gameWindow.add(playerActonsLabel);

        //Botón para ejecutar la acción "Check"
        checkButton.setText("Check");
        checkButton.setFont(new Font("Arial", Font.BOLD, 22));
        checkButton.setBorder(new LineBorder(Color.BLACK, 5));
        checkButton.setForeground(Color.BLACK);
        checkButton.setOpaque(true);
        checkButton.setBackground(Color.decode("#e3e3e3"));
        checkButton.setVerticalAlignment(SwingConstants.CENTER);
        checkButton.setHorizontalAlignment(SwingConstants.CENTER);
        checkButton.setSize(playerActonsLabel.getWidth() - 80, playerActonsLabel.getHeight() - 5);
        checkButton.setLocation(950 + ((playerActonsLabel.getWidth() - checkButton.getWidth()) / 2), 0);
        checkButton.setLocation(checkButton.getX(), playerActonsLabel.getY() + playerActonsLabel.getHeight() + 10);
        checkButton.setVisible(false);
        gameWindow.add(checkButton);

        //Botón para ejecutar la acción "Bet"
        betButton.setText("Bet");
        betButton.setFont(new Font("Arial", Font.BOLD, 22));
        betButton.setBorder(new LineBorder(Color.BLACK, 5));
        betButton.setForeground(Color.BLACK);
        betButton.setOpaque(true);
        betButton.setBackground(Color.decode("#e3e3e3"));
        betButton.setVerticalAlignment(SwingConstants.CENTER);
        betButton.setHorizontalAlignment(SwingConstants.CENTER);
        betButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        betButton.setLocation(checkButton.getX(), checkButton.getY() + checkButton.getHeight() + 10);
        betButton.setVisible(false);
        gameWindow.add(betButton);

        //Botón para ejecutar la acción "Call"
        callButton.setText("Call");
        callButton.setFont(new Font("Arial", Font.BOLD, 22));
        callButton.setBorder(new LineBorder(Color.BLACK, 5));
        callButton.setForeground(Color.BLACK);
        callButton.setOpaque(true);
        callButton.setBackground(Color.decode("#e3e3e3"));
        callButton.setVerticalAlignment(SwingConstants.CENTER);
        callButton.setHorizontalAlignment(SwingConstants.CENTER);
        callButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        callButton.setLocation(checkButton.getX(), betButton.getY() + betButton.getHeight() + 10);
        callButton.setVisible(false);
        gameWindow.add(callButton);

        //Botón para ejecutar la acción "Raise"
        raiseButton.setText("Raise");
        raiseButton.setFont(new Font("Arial", Font.BOLD, 22));
        raiseButton.setBorder(new LineBorder(Color.BLACK, 5));
        raiseButton.setForeground(Color.BLACK);
        raiseButton.setOpaque(true);
        raiseButton.setBackground(Color.decode("#e3e3e3"));
        raiseButton.setVerticalAlignment(SwingConstants.CENTER);
        raiseButton.setHorizontalAlignment(SwingConstants.CENTER);
        raiseButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        raiseButton.setLocation(checkButton.getX(), callButton.getY() + callButton.getHeight() + 10);
        raiseButton.setVisible(false);
        gameWindow.add(raiseButton);

        //Botón para ejecutar la acción "Fold"
        foldButton.setText("Fold");
        foldButton.setFont(new Font("Arial", Font.BOLD, 22));
        foldButton.setBorder(new LineBorder(Color.BLACK, 5));
        foldButton.setForeground(Color.BLACK);
        foldButton.setOpaque(true);
        foldButton.setBackground(Color.decode("#e3e3e3"));
        foldButton.setVerticalAlignment(SwingConstants.CENTER);
        foldButton.setHorizontalAlignment(SwingConstants.CENTER);
        foldButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        foldButton.setLocation(checkButton.getX(), raiseButton.getY() + raiseButton.getHeight() + 10);
        foldButton.setVisible(false);
        gameWindow.add(foldButton);

        //Valores mínimo medio y máximo para moneySlider
        Hashtable<Integer, JLabel> sliderTable = new Hashtable<>();
        sliderTable.put(1, new JLabel("+$1"));
        sliderTable.put(50, new JLabel("+$50"));
        sliderTable.put(100, new JLabel("+$100"));

        //Slider para apostar dinero
        moneySlider.setMinimum(1);
        moneySlider.setMaximum(100);
        moneySlider.setValue(1);
        moneySlider.setFont(new Font("Arial", Font.BOLD, 8));
        moneySlider.setOrientation(JSlider.HORIZONTAL);
        moneySlider.setSize(playerActonsLabel.getWidth(), 60);
        moneySlider.setLabelTable(sliderTable);
        moneySlider.setPaintLabels(true);
        moneySlider.setBackground(Color.WHITE);
        moneySlider.setForeground(Color.BLACK);
        moneySlider.setBorder(new LineBorder(Color.BLACK, 3));
        moneySlider.setLocation(playerActonsLabel.getX(), checkButton.getY());
        moneySlider.setVisible(false);
        gameWindow.add(moneySlider);

        //Etiqueta para mostrar monto final a aposta
        newBetLabel.setText("$" + moneySlider.getValue());
        newBetLabel.setLocation(moneySlider.getX(), moneySlider.getY() + moneySlider.getHeight() + 10);
        newBetLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newBetLabel.setBorder(new LineBorder(Color.BLACK, 3));
        newBetLabel.setForeground(Color.BLACK);
        newBetLabel.setOpaque(true);
        newBetLabel.setBackground(Color.WHITE);
        newBetLabel.setVerticalAlignment(SwingConstants.CENTER);
        newBetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newBetLabel.setSize(playerActonsLabel.getWidth(), dealerLabel.getHeight() + 10);
        newBetLabel.setVisible(false);
        gameWindow.add(newBetLabel);

        //Mostrar valor de apuesta mientras se desliza la barra
        moneySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int valor = moneySlider.getValue();
                newBetLabel.setText("$" + (actualBet + valor) + "");
            }
        });

        //Botón de confirmado de apuesta
        setBetButton.setText("Confirmar");
        setBetButton.setFont(new Font("Arial", Font.BOLD, 22));
        setBetButton.setBorder(new LineBorder(Color.BLACK, 5));
        setBetButton.setForeground(Color.BLACK);
        setBetButton.setOpaque(true);
        setBetButton.setBackground(Color.decode("#e3e3e3"));
        setBetButton.setVerticalAlignment(SwingConstants.CENTER);
        setBetButton.setHorizontalAlignment(SwingConstants.CENTER);
        setBetButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        setBetButton.setLocation(checkButton.getX(), newBetLabel.getY() + newBetLabel.getHeight() + 10);
        setBetButton.setVisible(false);
        gameWindow.add(setBetButton);

        //Ejecutar la acción "check"
        int finalPlayersAmount = playersAmount;
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocultar componentes
                hideButtons();
                saveGameButton.setVisible(false);

                //Pasar turno
                playersChecked.put(playerTurn, true);
                nextPlayerTurn(finalPlayersAmount);
            }
        });

        //Ejecutar la acción "bet"
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocultar componentes
                hideButtons();
                saveGameButton.setVisible(false);

                //Ejecutar acción segun el jugador que es
                if(playerTurn == bigBlindID){
                    if(players.get(playerTurn).getMoney() < (pot * 2)){
                        playerActonsLabel.setText("No se puede apostar mas");
                        foldButton.setVisible(true);

                        //Determinar Small Blind
                        smallBlindID = dealerID + 1;
                        do{
                            if(smallBlindID == finalPlayersAmount){
                                smallBlindID = 0;
                            }else{
                                if(players.get(smallBlindID).getMoney() == 0){
                                    smallBlindID++;
                                }
                            }
                        }while((smallBlindID == finalPlayersAmount) || (players.get(smallBlindID).getMoney() == 0));

                        //Determinar Big Blind
                        bigBlindID = smallBlindID + 1;
                        do{
                            if(bigBlindID == finalPlayersAmount){
                                bigBlindID = 0;
                            }else{
                                if(players.get(bigBlindID).getMoney() == 0){
                                    bigBlindID++;
                                }
                            }
                        }while((bigBlindID == finalPlayersAmount) || (players.get(bigBlindID).getMoney() == 0));

                    }else{
                        //Ejecutar acción si es el Big Blind
                        //Actualizar nuevo bote
                        int newBet = pot * 2;
                        pot = pot + newBet;
                        actualBet = newBet;

                        //Quitar cantidad de dinero a jugador
                        players.get(playerTurn).betMoney(newBet);

                        //Actualizar etiquetas
                        actualBetLabel.setText("$" + newBet);
                        playersMoney.get(playerTurn).setText("$" + players.get(playerTurn).getMoney());
                        potLabel.setText("Bote = $" + pot);

                        //Seguir con turnos de jugadores
                        bigBlindID = -1;
                        playersAlreadyBet.put(playerTurn, true);
                        playerBets.put(playerTurn, newBet);
                        nextPlayerTurn(finalPlayersAmount);
                    }

                }else{
                    //Ejecutar acción normal con cualquier otro jugador
                    //Mostrar componentes
                    moneySlider.setVisible(true);
                    newBetLabel.setVisible(true);
                    setBetButton.setVisible(true);

                    moneySlider.setValue(1);
                    newBetLabel.setText("$" + (moneySlider.getValue() + actualBet));
                }
            }
        });

        //Ejecutar la acción "call"
        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Determinar cuanto dinero se agregara al bote
                //dependiendo de la cantidad dada antes por el jugador
                //en caso de que haya existido un "raise" antes
                int betDifference = actualBet - playerBets.get(playerTurn);
                saveGameButton.setVisible(false);

                //Quitar cantidad de dinero a jugador
                players.get(playerTurn).betMoney(betDifference);

                //Agregar nueva apuesta al bote
                pot = pot + betDifference;

                //Actualizar etiquetas
                playersMoney.get(playerTurn).setText("$" + players.get(playerTurn).getMoney());
                potLabel.setText("Bote = $" + pot);

                //Ocultar componentes
                hideButtons();

                //Seguir con turnos de jugadores
                playersAlreadyBet.put(playerTurn, true);
                playerBets.put(playerTurn, actualBet);
                nextPlayerTurn(finalPlayersAmount);
            }
        });

        //Ejecutar la acción "raise"
        raiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocultar componentes
                hideButtons();
                saveGameButton.setVisible(false);

                //Mostrar componentes
                moneySlider.setVisible(true);
                newBetLabel.setVisible(true);
                setBetButton.setVisible(true);

                moneySlider.setValue(1);
                newBetLabel.setText("$" + (moneySlider.getValue() + actualBet));
            }
        });

        //Ejecutar la acción "fold"
        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocultar componentes
                hideButtons();
                saveGameButton.setVisible(false);

                //Borrar botones
                if(gamePhase >= 2){
                    players.get(playerTurn).getHand().get(0).getCard().setVisible(false);
                    players.get(playerTurn).getHand().get(1).getCard().setVisible(false);
                }

                //Ejecutar acciones
                playerLabels.get(playerTurn).setText("Fuera");
                playersMoney.get(playerTurn).setText("-----");
                players.get(playerTurn).fold();
                nextPlayerTurn(finalPlayersAmount);
            }
        });

        //Confirmar seleccion de apuesta
        setBetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGameButton.setVisible(false);
                if((players.get(playerTurn).getMoney() - (actualBet + moneySlider.getValue())) < 0){
                    playerActonsLabel.setText("No se puede incrementar");
                    foldButton.setVisible(true);

                }else{
                    //Ocultar componentes
                    hideButtons();

                    //Definir nueva apuesta
                    int newBet = actualBet + moneySlider.getValue();
                    actualBet = newBet;

                    //Quitar cantidad de dinero a jugador
                    players.get(playerTurn).betMoney(newBet);

                    //Agregar nueva apuesta al bote
                    pot = pot + newBet;

                    //Actualizar etiquetas
                    playersMoney.get(playerTurn).setText("$" + players.get(playerTurn).getMoney());
                    actualBetLabel.setText("$" + actualBet);
                    potLabel.setText("Bote = $" + pot);

                    //Definir que los demas jugadores no han apostado (Para igualar la apuesta)
                    for(int i = 0; i < playersAlreadyBet.size(); i++){
                        playersAlreadyBet.put(i, false);
                    }

                    //Definir acción si el jugador es o no small blind
                    if(playerTurn == smallBlindID){
                        //Eliminar id de Small Blind
                        if(playerTurn == smallBlindID){
                            smallBlindID = -1;
                        }

                        //Seguir con turnos de jugadores
                        playerBets.put(playerTurn, newBet);
                        nextPlayerTurn(finalPlayersAmount);
                    }else{
                        //Seguir con turnos de jugadores
                        playersAlreadyBet.put(playerTurn, true);
                        playerBets.put(playerTurn, newBet);
                        nextPlayerTurn(finalPlayersAmount);
                    }
                }
            }
        });

        //Etiqueta de solicitud de reinicio de juego
        requestReplayLabel.setText("¿Jugar otra vez?");
        requestReplayLabel.setLocation(950, (playerActonsLabel.getHeight() * 3) + 100);
        requestReplayLabel.setFont(new Font("Arial", Font.BOLD, 22));
        requestReplayLabel.setBorder(new LineBorder(Color.BLACK, 5));
        requestReplayLabel.setForeground(Color.BLACK);
        requestReplayLabel.setOpaque(true);
        requestReplayLabel.setBackground(Color.WHITE);
        requestReplayLabel.setVerticalAlignment(SwingConstants.CENTER);
        requestReplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        requestReplayLabel.setSize(dealerLabel.getWidth(), dealerLabel.getHeight() + 15);
        requestReplayLabel.setVisible(false);
        gameWindow.add(requestReplayLabel);

        //Botón para reiniciar juego
        replayButton.setText("Si");
        replayButton.setFont(new Font("Arial", Font.BOLD, 22));
        replayButton.setBorder(new LineBorder(Color.BLACK, 5));
        replayButton.setForeground(Color.BLACK);
        replayButton.setOpaque(true);
        replayButton.setBackground(Color.decode("#e3e3e3"));
        replayButton.setVerticalAlignment(SwingConstants.CENTER);
        replayButton.setHorizontalAlignment(SwingConstants.CENTER);
        replayButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        replayButton.setLocation(checkButton.getX(), requestReplayLabel.getY() + 70);
        replayButton.setVisible(false);
        gameWindow.add(replayButton);

        //Botón para no reiniciar juego
        notReplayButton.setText("No");
        notReplayButton.setFont(new Font("Arial", Font.BOLD, 22));
        notReplayButton.setBorder(new LineBorder(Color.BLACK, 5));
        notReplayButton.setForeground(Color.BLACK);
        notReplayButton.setOpaque(true);
        notReplayButton.setBackground(Color.decode("#e3e3e3"));
        notReplayButton.setVerticalAlignment(SwingConstants.CENTER);
        notReplayButton.setHorizontalAlignment(SwingConstants.CENTER);
        notReplayButton.setSize(checkButton.getWidth(), checkButton.getHeight());
        notReplayButton.setLocation(checkButton.getX(), replayButton.getY() + 70);
        notReplayButton.setVisible(false);
        gameWindow.add(notReplayButton);

        //Crear botón de confirmacion de mano
        confirmCards.setText("Confirmar cartas");
        confirmCards.setLocation(950, (playerActonsLabel.getHeight() * 3) + 100);
        confirmCards.setFont(new Font("Arial", Font.BOLD, 22));
        confirmCards.setBorder(new LineBorder(Color.BLACK, 5));
        confirmCards.setForeground(Color.BLACK);
        confirmCards.setOpaque(true);
        confirmCards.setBackground(Color.decode("#e3e3e3"));
        confirmCards.setVerticalAlignment(SwingConstants.CENTER);
        confirmCards.setHorizontalAlignment(SwingConstants.CENTER);
        confirmCards.setSize(dealerLabel.getWidth(), dealerLabel.getHeight() + 15);
        confirmCards.setVisible(false);
        gameWindow.add(confirmCards);

        //Crear botón de guardado de partida
        saveGameButton.setText("Guardar partida");
        saveGameButton.setLocation(950, (playerActonsLabel.getHeight() * 3) + 420);
        saveGameButton.setFont(new Font("Arial", Font.BOLD, 22));
        saveGameButton.setBorder(new LineBorder(Color.BLACK, 5));
        saveGameButton.setForeground(Color.BLACK);
        saveGameButton.setOpaque(true);
        saveGameButton.setBackground(Color.decode("#e3e3e3"));
        saveGameButton.setVerticalAlignment(SwingConstants.CENTER);
        saveGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        saveGameButton.setSize(dealerLabel.getWidth(), dealerLabel.getHeight() + 15);
        saveGameButton.setVisible(true);
        gameWindow.add(saveGameButton);

        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Realizar guardado de datos
                saveGameData(finalPlayersAmount);

                //Ocultar botón
                saveGameButton.setVisible(false);
            }
        });

        //Agregar acciones a los botones
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocular botones
                requestReplayLabel.setVisible(false);
                replayButton.setVisible(false);
                notReplayButton.setVisible(false);

                //Reiniciar juego
                restartGame();
            }
        });

        notReplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ocultar componentes
                hideButtons();
                dealerLabel.setVisible(false);
                smallBlindLabel.setVisible(false);
                bigBlindLabel.setVisible(false);
                potLabel.setVisible(false);
                actualBetLabel.setVisible(false);
                requestReplayLabel.setVisible(false);
                replayButton.setVisible(false);
                notReplayButton.setVisible(false);

                //Borar archivo de juego guardado
                saveFile.delete();
                saveGameButton.setVisible(false);
            }
        });

        //Pintar fondo
        Container background = gameWindow.getContentPane();
        background.setBackground(Color.decode("#477148"));

        //Iniciar segun si es nuevo juego o cargado
        if(loadGame){
            //El metodo ejecutara el punto del juego segun sea el caso
            if(gamePhase == 9){
                fightStage();
            }else{
                betStage();
                distributeCardsStage();
            }
        }else{
            betStage();
        }
    }

    //Seguir turnos de jugadores
    public void nextPlayerTurn(int playersAmount){
        //Acumular cuantos jugadores ya aostaron
        int playersBet = 0;

        //Contar cuantos jugadores ya apostaron o salieron
        for(int i = 0; i < players.size(); i++){
            if(playersAlreadyBet.get(i) || (players.get(i).getPlayerStatus() == false)){
                playersBet++;
            }
        }

        //Seguit turno de jugador o juego segun cuantos jugadores hayan apostado
        if(playersBet == playersAmount){
            //Incrementar turno de juego
            gamePhase++;
            saveGameButton.setVisible(true);

            //Definir jugador que iniciara
            do{
                if(players.get(playerStarts).getPlayerStatus() == false){
                    playerStarts++;
                }

                if(playerStarts == playersAmount){
                    playerStarts = 0;
                }
            }while(players.get(playerStarts).getPlayerStatus() == false);
            playerTurn = playerStarts;

            //Reiniciar apuesta
            actualBet = 0;
            actualBetLabel.setText("$" + actualBet);

            //Desbloquear a todos los jugadores
            for(int i = 0; i < playersAlreadyBet.size(); i++){
                playersAlreadyBet.put(i, false);
                playersChecked.put(i, false);
                playerBets.put(i, 0);
            }

        }else{
            //Incrementar turno de jugador en caso de que el jugador
            //ya haya apostado o haya salido del juego
            do{
                if(playerTurn == (playersAmount - 1)){
                    playerTurn = 0;
                }else{
                    playerTurn++;
                }
            }while(playersAlreadyBet.get(playerTurn) || (players.get(playerTurn).getPlayerStatus() == false));
        }

        //Llamar metodos de acciones
        //Los metodos actuaran segun la fase que es
        betStage();
        distributeCardsStage();
    }

    //Etapas de apuestas
    public void betStage(){
        //Realizar acción segun el turno del juego
        if(gamePhase == 1){
            //Actualizar eqtiquetas
            dealerLabel.setText("The Dealer: Jugador " + (dealerID + 1) + ".");
            playerActonsLabel.setText("Acciones jugador " + (playerTurn + 1) + "");

            //Permitir acciones segun el jugador
            if(playerTurn == smallBlindID){
                betButton.setVisible(true);

            }else if(playerTurn == bigBlindID){
                betButton.setVisible(true);

            }else{
                //Determinar cuantos jugadores siguen en pie
                int stillInGame = 0;
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).getPlayerStatus()){
                        stillInGame++;
                    }
                }

                //Terminar o coninuar el juego
                if(stillInGame == 1){
                    finishGame();

                }else{
                    if(players.get(playerTurn).getMoney() < actualBet){
                        //Obligar a retirarse del juego
                        foldButton.setVisible(true);
                    }else{
                        //Jugar normal
                        callButton.setVisible(true);
                        raiseButton.setVisible(true);
                        foldButton.setVisible(true);
                    }
                }
            }

        }else if((gamePhase == 3) || (gamePhase == 5) || (gamePhase == 7)){
            playerActonsLabel.setText("Acciones jugador " + (playerTurn + 1) + "");

            //Ocultar todos los componentes
            hideButtons();

            //Determinar cuantos jugadores siguen en pie
            int stillInGame = 0;
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getPlayerStatus()){
                    stillInGame++;
                }
            }

            //Terminar o coninuar el juego
            if(stillInGame == 1){
                finishGame();

            }else{
                //Permitir acciones segun la apuesta inicial
                if(actualBet == 0){
                    //Determinar rumbo de jugador segun su dinero
                    if(players.get(playerTurn).getMoney() < actualBet){
                        //Obligar a retirarse del juego
                        foldButton.setVisible(true);
                    }else{
                        //Permitir acciones si no hay apuesta
                        if(playersChecked.get(playerTurn) == false){
                            checkButton.setVisible(true);
                        }
                        betButton.setVisible(true);
                        foldButton.setVisible(true);
                    }
                }else{
                    //Determinar rumbo de jugador segun su dinero
                    if(players.get(playerTurn).getMoney() < actualBet){
                        //Obligar a retirarse del juego
                        foldButton.setVisible(true);
                    }else{
                        //Permitir acciones si ya hay apuesta
                        callButton.setVisible(true);
                        raiseButton.setVisible(true);
                        foldButton.setVisible(true);
                    }
                }
            }

        }else{
            //Hacer nada
        }
    }

    //Etapas de disribucion de cartas
    public void distributeCardsStage(){
        if(loadGame == false){
            //Realizar acción segun el turno del juego
            switch(gamePhase){
                case 2:
                    //Repartir primera hole card a los jugadores
                    for(int i = 0; i < players.size(); i++){
                        //Repartir cartas si el jugador aun sigue en juego
                        if(players.get(i).getPlayerStatus()){
                            //Deshacerse de una carta
                            discardCards.add(cardDeck.get(0));
                            cardDeck.remove(0);
                            discardCards.get(discardCards.size() - 1).relocateCard(100, 55);

                            //Mover primera hole card de ArrayList
                            players.get(i).addCard(cardDeck.get(0));

                            //Eliminar carta de mazo
                            cardDeck.remove(0);

                            //Definir pocision en X y Y de cartas y moverlas
                            int xPos = playersMoney.get(i).getX();
                            int yPos = playersMoney.get(i).getY() + 40;
                            players.get(i).getHand().get(0).relocateCard(xPos, yPos);
                        }
                    }

                    //Repartir segunda hole card a los jugadores
                    for(int i = 0; i < players.size(); i++){
                        //Repartir cartas si el jugador aun sigue en juego
                        if(players.get(i).getPlayerStatus()){
                            //Deshacerse de una carta
                            discardCards.add(cardDeck.get(0));
                            cardDeck.remove(0);
                            discardCards.get(discardCards.size() - 1).relocateCard(100, 55);

                            //Mover segunda hole card de ArrayList
                            players.get(i).addCard(cardDeck.get(0));

                            //Eliminar carta de mazo
                            cardDeck.remove(0);

                            //Definir pocision en X y Y de cartas y moverlas
                            int xPos = playersMoney.get(i).getX() + 80;
                            int yPos = playersMoney.get(i).getY() + 40;
                            players.get(i).getHand().get(1).relocateCard(xPos, yPos);
                        }
                    }

                    //Seguir turno
                    gamePhase++;
                    betStage();
                    break;

                case 4:
                    //Deshacerse de una carta
                    discardCards.forEach(card -> {
                        card.getCard().setVisible(false);
                    });

                    discardCards.add(cardDeck.get(0));
                    cardDeck.remove(0);
                    discardCards.get(discardCards.size() - 1).relocateCard(100, 55);

                    //Mover 3 cartas comunitarias de ArrayList
                    communityCards.add(cardDeck.get(0));
                    communityCards.add(cardDeck.get(1));
                    communityCards.add(cardDeck.get(2));

                    //Eliminar cartas de mazo
                    cardDeck.remove(0); //Eliminar primera carta (pocision 0)
                    cardDeck.remove(0); //Eliminar segunda carta (pocision 0)
                    cardDeck.remove(0); //Eliminar tercera carta (pocision 0)

                    //Mover cartas
                    communityCards.get(0).relocateCard(250, 270);
                    communityCards.get(1).relocateCard(350, 270);
                    communityCards.get(2).relocateCard(450, 270);

                    //Seguir turno
                    gamePhase++;
                    betStage();
                    break;

                case 6:
                    //Deshacerse de una carta
                    discardCards.forEach(card -> {
                        card.getCard().setVisible(false);
                    });

                    discardCards.add(cardDeck.get(0));
                    cardDeck.remove(0);
                    discardCards.get(discardCards.size() - 1).relocateCard(100, 55);

                    //Mostrar carta "Turn" de ArrayList
                    communityCards.add(cardDeck.get(0));

                    //Eliminar cartas de mazo
                    cardDeck.remove(0); //Eliminar carta (pocision 0)

                    //Mover cartas y reescalar cartas
                    communityCards.get(3).relocateCard(550, 270);

                    //Seguir turno
                    gamePhase++;
                    betStage();
                    break;

                case 8:
                    //Deshacerse de una carta
                    discardCards.forEach(card -> {
                        card.getCard().setVisible(false);
                    });

                    discardCards.add(cardDeck.get(0));
                    cardDeck.remove(0);
                    discardCards.get(discardCards.size() - 1).relocateCard(100, 55);

                    //Mover carta "River" de ArrayList
                    communityCards.add(cardDeck.get(0));

                    //Eliminar cartas de mazo
                    cardDeck.remove(0); //Eliminar carta (pocision 0)

                    //Mover cartas y reescalar cartas
                    communityCards.get(4).relocateCard(650, 270);

                    //Entrar a enfrentamiento
                    gamePhase++;
                    fightStage();
                    break;

                default:
                    //Hacer nada
                    break;
            }
        }
        loadGame = false;
    }

    //Etapa de enfrentamiento
    public void fightStage(){
        if(gamePhase == 9){
            //Control de jugadores
            playerTurn = 0;
            saveGameButton.setVisible(false);

            //Definir al jugador que iniciara
            do{
                if(players.get(playerTurn).getPlayerStatus() == false){
                    playerTurn++;
                }
            }while(players.get(playerTurn).getPlayerStatus() == false);

            //Cambiar etiqueta de acciones
            dealerLabel.setText("Selecciona");
            smallBlindLabel.setText("las 2 cartas a");
            bigBlindLabel.setText("rechazar");
            playerActonsLabel.setText("Mano de Jugador " + (playerTurn + 1));

            //Mostrar cartas de los jugadores
            players.get(playerTurn).getHand().forEach(card -> {
                card.turnUpCard();
            });

            //Controlar que solo se seleccionen 5 cartas
            ArrayList<Card> selectedCards = new ArrayList<>();

            //Mostrar cartas comunitarias y habilitar efectos
            communityCards.forEach(card -> {
                //Mostrar carta
                card.turnUpCard();

                //Agregar evento
                card.getCard().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(selectedCards.size() < 2){
                            card.getCard().setEnabled(false);
                            selectedCards.add(card);

                            if(selectedCards.size() == 2){
                                confirmCards.setVisible(true);
                            }
                        }
                    }
                });
            });

            //Agregar cartas seleccionadas al jugador y seguir con los demas jugadores
            confirmCards.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    //Ocultar Botón
                    confirmCards.setVisible(false);

                    //Agregar cartas a jugador
                    communityCards.forEach(card -> {
                        if(card.getCard().isEnabled()){
                            players.get(playerTurn).addCard(card);
                        }
                    });

                    //Seguir con el siguiente jugador
                    playerTurn++;
                    if(playerTurn < players.size()){
                        do{
                            if(players.get(playerTurn).getPlayerStatus() == false){
                                playerTurn++;
                            }

                            if(playerTurn == players.size()){
                                playerTurn = 0;
                                analyzeHands();
                            }
                        }while(players.get(playerTurn).getPlayerStatus() == false);

                        playerActonsLabel.setText("Mano de Jugador " + (playerTurn + 1));

                        //Mostrar cartas del jugador
                        for(int i = 0; i < 2; i++){
                            players.get(playerTurn).getHand().get(i).turnUpCard();
                        }

                        //Habilitar todas las cartas
                        communityCards.forEach(card -> {
                            card.getCard().setEnabled(true);
                        });
                        selectedCards.clear();
                    }else{
                        analyzeHands();
                    }
                }
            });
        }
    }

    //Determinar al ganador
    public void analyzeHands(){
        //Guardar valores de las manos de los jugadores
        ArrayList<Integer> handsValues = new ArrayList<>();
        saveGameButton.setVisible(true);

        //Analizar valores de las manos de los jugadores
        players.forEach(player -> {
            if(player.getPlayerStatus() == false){
                handsValues.add(1000);
            }else{
                handsValues.add(player.analyzeHand());
            }
        });

        //Determinar valor de la mano mas fuerte
        int strongestHand = handsValues.get(0);
        for(int i = 0; i < players.size(); i++){
            if(handsValues.get(i) < strongestHand){
                strongestHand = handsValues.get(i);
            }
        }

        //Determinar ganador o empate
        int strongestHandCounter = 0;
        for(int i = 0; i < handsValues.size(); i++){
            if(handsValues.get(i) == strongestHand){
                strongestHandCounter++;
            }
        }

        //Declarar empate o ganador
        if(strongestHandCounter == 1){
            //Determinar ganador
            for(int i = 0; i < handsValues.size(); i++){
                if(handsValues.get(i) == strongestHand){
                    playerTurn = i;
                    finishGame();
                }
            }
        }else{
            //Cambiar textos
            dealerLabel.setText("El Bote se");
            smallBlindLabel.setText("repartira entre");
            bigBlindLabel.setText("los empatados");
            playerActonsLabel.setText("Empate");

            for(int i = 0; i < handsValues.size(); i++){
                if(handsValues.get(i) == strongestHand){
                    playerActonsLabel.setText(playerActonsLabel.getText() + " - " + (i + 1));
                }
            }

            //Determinar empate diviendo el bote
            int splitPot = pot / strongestHandCounter;

            //Actualizar etiquetas
            actualBetLabel.setText("$0");
            potLabel.setText("Bote = $0");
            pot = 0;

            //Agregar dinero a jugadores
            for(int i = 0; i < players.size(); i++){
                if(handsValues.get(i) == strongestHand){
                    players.get(i).addMoney(splitPot);
                    playersMoney.get(i).setText("$" + players.get(i).getMoney());
                }
            }

            //Solicitar reinicio de juego
            requestReplayLabel.setVisible(true);
            replayButton.setVisible(true);
            notReplayButton.setVisible(true);
        }
    }

    //Pocisionar etiquetas de jugadores segun cuantos jugadores son
    public void locatePlayerComponents(int playersAmount){
        //Ordenar etiquetas de jugadores
        switch(playersAmount){
            case 2:
                //Acomodar etiquetas de 2 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                break;

            case 3:
                //Acomodar etiquetas de 3 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                break;

            case 4:
                //Acomodar etiquetas de 4 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                break;

            case 5:
                //Acomodar etiquetas de 5 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                break;

            case 6:
                //Acomodar etiquetas de 6 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                playerLabels.get(5).setLocation(600, 440);
                break;

            case 7:
                //Acomodar etiquetas de 7 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                playerLabels.get(5).setLocation(600, 440);
                playerLabels.get(6).setLocation(400, 440);
                break;

            case 8:
                //Acomodar etiquetas de 8 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                playerLabels.get(5).setLocation(600, 440);
                playerLabels.get(6).setLocation(400, 440);
                playerLabels.get(7).setLocation(200, 440);
                break;

            case 9:
                //Acomodar etiquetas de 9 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                playerLabels.get(5).setLocation(600, 440);
                playerLabels.get(6).setLocation(400, 440);
                playerLabels.get(7).setLocation(200, 440);
                playerLabels.get(8).setLocation(20, 420);
                break;

            case 10:
                //Acomodar etiquetas de 10 jugadores
                playerLabels.get(0).setLocation(200, 10);
                playerLabels.get(1).setLocation(400, 10);
                playerLabels.get(2).setLocation(600, 10);
                playerLabels.get(3).setLocation(780, 200);
                playerLabels.get(4).setLocation(780, 420);
                playerLabels.get(5).setLocation(600, 440);
                playerLabels.get(6).setLocation(400, 440);
                playerLabels.get(7).setLocation(200, 440);
                playerLabels.get(8).setLocation(20, 420);
                playerLabels.get(9).setLocation(20, 200);
                break;

            default:
                //Hacer nada
                break;
        }

        //Ordenar etiquetas de dinero de jugadores
        for(int i = 0; i < playersAmount; i++){
            playersMoney.get(i).setLocation(playerLabels.get(i).getX(), playerLabels.get(i).getY() + 50);
        }
    }

    //Ocultar todos los botones
    public void hideButtons(){
        checkButton.setVisible(false);
        betButton.setVisible(false);
        callButton.setVisible(false);
        foldButton.setVisible(false);
        raiseButton.setVisible(false);
        setBetButton.setVisible(false);
        newBetLabel.setVisible(false);
        moneySlider.setVisible(false);
    }

    //Agregar bote a jugador y finalizar juego
    public void finishGame(){
        //Cambiar texto de etiqueta de acciones
        playerActonsLabel.setText("Jugador " + (playerTurn + 1) + " gana el Bote");

        //Agregar dinero de bote a jugador ganador
        players.get(playerTurn).addMoney(pot);
        pot = 0;

        //Actualizar etiquetas
        actualBetLabel.setText("$0");
        potLabel.setText("Bote = $" + pot);
        playersMoney.get(playerTurn).setText("$" + players.get(playerTurn).getMoney());

        //Solicitar reinicio de juego
        requestReplayLabel.setVisible(true);
        replayButton.setVisible(true);
        notReplayButton.setVisible(true);
        saveGameButton.setVisible(true);
    }

    //Reiniciar juego
    public void restartGame(){
        //Quitar todos los eventos de botones especiales
        for(ActionListener event: confirmCards.getActionListeners()){
            confirmCards.removeActionListener(event);
        }

        for(int i = 0; i < communityCards.size(); i++){
            for(ActionListener event: communityCards.get(i).getCard().getActionListeners()){
                communityCards.get(i).getCard().removeActionListener(event);
            }
        }

        //Recuperar numero de jugadores
        int playersAmount = players.size();

        //Determinar cuantos jugadres aun tienen dinero
        int playersWithMoney = 0;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getMoney() != 0){
                playersWithMoney++;
            }
        }

        //Seguir con nueva partida si aun quedan jugadores con dinero
        if((playersWithMoney == 0) || (playersWithMoney == 1)){
            //Mostrar mensaje de falta de jugadores
            dealerLabel.setVisible(true);
            smallBlindLabel.setVisible(true);
            bigBlindLabel.setVisible(true);

            dealerLabel.setText("No existen");
            smallBlindLabel.setText("suficientes jugadores");
            bigBlindLabel.setText("con dinero");

        }else{
            //Definir al siguiente Dealer, Small Blind y Big Blind
            if(playersAmount == 2){
                //Determinar al dealer
                dealerID++;
                do{
                    if(dealerID == playersAmount){
                        dealerID = 0;
                    }else{
                        if(players.get(dealerID).getMoney() == 0){
                            dealerID++;
                        }
                    }
                }while((dealerID == playersAmount) || (players.get(dealerID).getMoney() == 0));

                //Determinar Small Blind
                smallBlindID = dealerID;

                //Determinar Big Blind
                bigBlindID = dealerID + 1;
                do{
                    if(bigBlindID == playersAmount){
                        bigBlindID = 0;
                    }else{
                        if(players.get(bigBlindID).getMoney() == 0){
                            bigBlindID++;
                        }
                    }
                }while((bigBlindID == playersAmount) || (players.get(bigBlindID).getMoney() == 0));

            }else{
                //Definir cuando existen mas de 2 jugadores
                //Determinar al dealer
                dealerID++;
                do{
                    if(dealerID == playersAmount){
                        dealerID = 0;
                    }else{
                        if(players.get(dealerID).getMoney() == 0){
                            dealerID++;
                        }
                    }
                }while((dealerID == playersAmount) || (players.get(dealerID).getMoney() == 0));

                //Determinar Small Blind
                smallBlindID = dealerID + 1;
                do{
                    if(smallBlindID == playersAmount){
                        smallBlindID = 0;
                    }else{
                        if(players.get(smallBlindID).getMoney() == 0){
                            smallBlindID++;
                        }
                    }
                }while((smallBlindID == playersAmount) || (players.get(smallBlindID).getMoney() == 0));

                //Determinar Big Blind
                bigBlindID = smallBlindID + 1;
                do{
                    if(bigBlindID == playersAmount){
                        bigBlindID = 0;
                    }else{
                        if(players.get(bigBlindID).getMoney() == 0){
                            bigBlindID++;
                        }
                    }
                }while((bigBlindID == playersAmount) || (players.get(bigBlindID).getMoney() == 0));
            }

            //Definir jugador de inicio de turnos
            playerStarts = smallBlindID;
            playerTurn = playerStarts;

            //Limpiar cartas de jugadores (y reactivarlos)
            for(int i = 0; i < players.size(); i++){
                for(int j = 0; j < players.get(i).getHand().size(); j++){
                    players.get(i).getHand().get(j).getCard().setVisible(false);
                }
                players.get(i).clearHand();
            }

            //Limpiar cartas deshechas
            discardCards.forEach(card -> {
                card.getCard().setVisible(false);
            });
            discardCards.clear();

            //Limpiar cartas restantes del mazo
            cardDeck.forEach(card -> {
                card.getCard().setVisible(false);
            });
            cardDeck.clear();

            //Limpiar cartas comunitarias
            communityCards.forEach(card -> {
                card.getCard().setVisible(false);
            });
            communityCards.clear();

            //Limpar HashMaps
            for(int i = 0; i < playersAmount; i++){
                playersAlreadyBet.put(i, false);
                playerBets.put(i, 0);
                playersChecked.put(i, false);
            }

            //Limpiar contadores de apuestas
            pot = 0;
            actualBet = 0;
            gamePhase = 1;

            //Actualizar etiquetas
            dealerLabel.setText("The Dealer: Jugador " + (dealerID + 1) + ".");
            smallBlindLabel.setText("Small Blind: Jugador " + (smallBlindID + 1) + ".");
            bigBlindLabel.setText("Big Blind: Jugador " + (bigBlindID + 1) + ".");

            for(int i = 0; i < playerLabels.size(); i++){
                playerLabels.get(i).setText("Jugador " + (i + 1));
                playersMoney.get(i).setText("$" + players.get(i).getMoney());
            }

            //Desbloquear todos los jugadores
            Iterator<Player> playerIterator = players.iterator();
            while(playerIterator.hasNext()){
                playerIterator.next().unlockPlayer();
            }

            //Bloquear aquellos jugadores que ya no tienen dinero
            for(int i = 0; i < players.size(); i++){
                if(players.get(i).getMoney() == 0){
                    //Ejecutar acciones
                    playerLabels.get(i).setText("Fuera");
                    playersMoney.get(i).setText("-----");
                    players.get(i).fold();
                }
            }

            //Crear mazo de cartas
            CardDeck deck = new CardDeck(25, 55, 9);
            cardDeck = deck.getCardDeck();

            //Barajear cartas
            Collections.shuffle(cardDeck);

            //Mostrar cartas
            cardDeck.forEach(card -> {
                gameWindow.add(card.getCard());
            });

            //Pocisionar etiquetas de jugadores
            locatePlayerComponents(playersAmount);

            //Empezar juego
            playerActonsLabel.setText("Acciones Jugador " + playerTurn);
            betStage();
        }
    }

    //Guardar informacion de variables y colecciones
    public void saveGameData(int playersAmount){
        //Elimnar archivo de guardado de partida
        saveFile.delete();

        //Crear nuevo archivo de guardado de partida
        try{
            FileWriter saveFileWriter = new FileWriter(saveFilePath);
            saveFileWriter.close();
        }catch(IOException expt){
            expt.printStackTrace();
        }

        //Agregar todos los datos
        try(BufferedWriter insertData = new BufferedWriter(new FileWriter(saveFilePath))){

            //Guardar fase actual del juego
            insertData.write("" + gamePhase);
            insertData.newLine();

            //Guardar turno actual de jugador
            insertData.write("" + playerTurn);
            insertData.newLine();

            //Guardar guardado de jugador que inicia
            insertData.write("" + playerStarts);
            insertData.newLine();

            //Guardar id's de Dealer, Small Blind y Big Blind
            insertData.write("" + dealerID);
            insertData.newLine();

            insertData.write("" + smallBlindID);
            insertData.newLine();

            insertData.write("" + bigBlindID);
            insertData.newLine();

            //Guardar apuesta actual
            insertData.write("" + actualBet);
            insertData.newLine();

            //Guardar bote
            insertData.write("" + pot);
            insertData.newLine();

            //Guardar longitud de mazo
            insertData.write("" + cardDeck.size());
            insertData.newLine();

            //Guardar mazo de cartas
            for(int i = 0; i < cardDeck.size(); i++){
                //Guardar atributos de las cartas
                int number = cardDeck.get(i).getNumber();
                String figure = cardDeck.get(i).getFigure();

                //Guardar en archivo
                insertData.write("" + number + "," + figure);
                insertData.newLine();
            }

            //Guardar longitud de cartas descartadas
            insertData.write("" + discardCards.size());
            insertData.newLine();

            //Guardar cartas descartadas
            for(int i = 0; i < discardCards.size(); i++){
                //Guardar atributos de las cartas
                int number = discardCards.get(i).getNumber();
                String figure = discardCards.get(i).getFigure();

                //Guardar en archivo
                insertData.write("" + number + "," + figure);
                insertData.newLine();
            }

            //Guardar longitud de cartas comunitarias
            insertData.write("" + communityCards.size());
            insertData.newLine();

            //Guardar cartas comunitarias
            for(int i = 0; i < communityCards.size(); i++){
                //Guardar atributos de las cartas
                int number = communityCards.get(i).getNumber();
                String figure = communityCards.get(i).getFigure();

                //Guardar en archivo
                insertData.write("" + number + "," + figure);
                insertData.newLine();
            }

            //Guardar numero de jugadores
            insertData.write("" + playersAmount);
            insertData.newLine();

            //Guardar jugadores
            for(int i = 0; i < players.size(); i++){
                //Guardar datos basicos de los jugadores
                int money = players.get(i).getMoney();
                boolean status = players.get(i).getPlayerStatus();

                insertData.write("" + money + "," + status);
                insertData.newLine();

                //Guardar longitud de la mano
                int handSize = players.get(i).getHand().size();
                insertData.write("" + handSize);
                insertData.newLine();

                //Guardar cartas de la mano
                for(int j = 0; j < handSize; j++){
                    //Guardar atributos de las cartas
                    int numberC = players.get(i).getHand().get(j).getNumber();
                    String figureC = players.get(i).getHand().get(j).getFigure();

                    //Guardar en archivo
                    insertData.write("" + numberC + "," + figureC);
                    insertData.newLine();
                }
            }

        }catch(IOException expt){
            expt.printStackTrace();
        }
    }
}