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
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FiveCardDraw extends PokerGame {
    private int FIXED_BET = 100;
    private CardDeck deck;
    private int playersAmount;
    private int initialMoney = 1000;
    private int currentPlayerIndex = 0;
    private int currentBet = 0;
    private int lastRaise = 0;
    private int playersInRound = 0;
    private JPanel tablePanel;
    private ArrayList<JPanel> playerPanels;
    private ArrayList<JLabel> playerMoneyLabels;
    private ArrayList<JLabel> playerBetLabels;
    private ArrayList<Integer> playerBets;
    private ArrayList<Boolean> playerFolded;
    private ArrayList<Boolean> playerEliminated;
    private JButton nextButton;
    private JLabel potLabel;
    private JFrame scrollFrame;
    private JScrollPane scrollPane;

    public FiveCardDraw() {
        super();
        requestPlayers();
    }



    /*
    Esta clase sirve para solicitar la cantidad de jugadores que jugaran
     */
    @Override
    public void requestPlayers() {
        JFrame askPlayersWindow = new JFrame("Seleccionar jugadores");
        askPlayersWindow.setSize(400, 220);
        askPlayersWindow.setLayout(null);
        askPlayersWindow.setLocationRelativeTo(null);

        JLabel label = new JLabel("¿Cuántos jugadores? (2-7)");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBounds(60, 20, 300, 30);
        askPlayersWindow.add(label);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(2, 2, 7, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setBounds(160, 60, 60, 30);
        askPlayersWindow.add(spinner);

        JButton okButton = new JButton("Aceptar");
        okButton.setBounds(130, 110, 120, 35);
        okButton.setFont(new Font("Arial", Font.BOLD, 18));
        okButton.setBackground(new Color(0, 128, 0));
        okButton.setForeground(Color.WHITE);
        askPlayersWindow.add(okButton);

        okButton.addActionListener(e -> {
            playersAmount = (int) spinner.getValue();
            askPlayersWindow.dispose();
            createGameWindow(playersAmount);
        });

        askPlayersWindow.setVisible(true);
    }

    /*
    Esta funcion llama diferentes funciones para crear de manera correcta
    */
    @Override
    public void createGameWindow(int playersAmount) {
        this.playersAmount = playersAmount;

        if (scrollFrame != null) scrollFrame.dispose();
        scrollFrame = new JFrame("5 Card Draw");
        scrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scrollFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        deck = new CardDeck(600, 20, 10);
        cardDeck = deck.getCardDeck();
        players = new ArrayList<>();
        playerPanels = new ArrayList<>();
        playerMoneyLabels = new ArrayList<>();
        playerBetLabels = new ArrayList<>();
        playerBets = new ArrayList<>();
        playerFolded = new ArrayList<>();
        playerEliminated = new ArrayList<>();
        pot = 0;
        potLabel = new JLabel();
        for (int i = 0; i < playersAmount; i++) {
            Player player = new Player(initialMoney);
            players.add(player);
            playerBets.add(0);
            playerFolded.add(false);
            playerEliminated.add(false);
        }
        playersInRound = playersAmount;

        players.forEach(p -> {
            if (!playerEliminated.get(players.indexOf(p))) {
                for (int j = 0; j < 5; j++) {
                    p.addCard(getRandomCardFromDeck());
                }
            }
        });

        drawTable();
        betStage();

        scrollFrame.setVisible(true);
    }

    private Card getRandomCardFromDeck() {
        int index = (int) (Math.random() * cardDeck.size());
        return deck.getCard(index);
    }

    private void drawTable() {
        if (tablePanel != null && scrollPane != null) scrollFrame.remove(scrollPane);

        int N = players.size();
        int cols = N <= 4 ? N : 4;
        int rows = (int) Math.ceil(N / 4.0);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBackground(new Color(34, 139, 34));

        // Panel central para el bote y el botón
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        potLabel = new JLabel("Bote: $" + pot);
        potLabel.setFont(new Font("Arial", Font.BOLD, 32));
        potLabel.setForeground(new Color(255, 255, 140));
        potLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(potLabel);

        nextButton = new JButton("Comenzar apuestas");
        nextButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextButton.setBackground(new Color(220, 180, 0));
        nextButton.setEnabled(true);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(e -> {
            nextButton.setEnabled(false);
            startBettingRound();
        });
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(nextButton);

        // Panel para los jugadores en grid flexible
        JPanel playersPanel = new JPanel(new GridLayout(rows, cols, 30, 20));
        playersPanel.setOpaque(false);

        playerPanels.clear();
        playerMoneyLabels.clear();
        playerBetLabels.clear();

        for (int i = 0; i < N; i++) {
            JPanel playerPanel = buildPlayerPanel(i);
            playerPanels.add(playerPanel);
            playersPanel.add(playerPanel);
        }
        // Si son 7 jugadores, agrega un panel vacío para cuadrar el grid
        if (N == 7) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            playersPanel.add(empty);
        }

        tablePanel.add(centerPanel, BorderLayout.NORTH);
        tablePanel.add(playersPanel, BorderLayout.CENTER);

        scrollFrame.setContentPane(tablePanel);
        scrollFrame.revalidate();
        scrollFrame.repaint();
    }

    private JPanel buildPlayerPanel(int i) {
        int numPlayers = players.size();
        int playerPanelWidth, playerPanelHeight, cardW, cardH, gap;
        if (numPlayers <= 3) {
            playerPanelWidth = 480;
            playerPanelHeight = 260;
            cardW = 90;
            cardH = 135;
            gap = 18;
        } else if (numPlayers <= 5) {
            playerPanelWidth = 380;
            playerPanelHeight = 200;
            cardW = 65;
            cardH = 97;
            gap = 12;
        } else {
            playerPanelWidth = 320;
            playerPanelHeight = 160;
            cardW = 65;
            cardH = 97;
            gap = 8;
        }

        int maxHandPanelWidth = playerPanelWidth - 20;
        int maxCardW = (maxHandPanelWidth - (gap * 4)) / 5;
        cardW = Math.min(cardW, maxCardW);

        JPanel playerPanel = new JPanel();
        playerPanel.setPreferredSize(new Dimension(playerPanelWidth, playerPanelHeight));
        playerPanel.setLayout(null);
        playerPanel.setBorder(new LineBorder(Color.BLACK, 2, true));
        playerPanel.setOpaque(true);

        if (playerEliminated.get(i)) {
            playerPanel.setBackground(new Color(50, 50, 50, 220));
        } else {
            playerPanel.setBackground(new Color(39, 174, 96, 210));
        }

        JLabel playerLabel = new JLabel("<html><b>Jugador " + (i + 1) + "</b></html>");
        playerLabel.setFont(new Font("Arial", Font.BOLD, Math.max(16, cardH / 6)));
        playerLabel.setBounds(10, 5, 140, 24);
        playerPanel.add(playerLabel);

        JLabel moneyLabel = new JLabel("Dinero: $" + players.get(i).getMoney());
        moneyLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(15, cardH / 7)));
        moneyLabel.setBounds(10, 30, 160, 22);
        playerPanel.add(moneyLabel);
        playerMoneyLabels.add(moneyLabel);

        JLabel betLabel = new JLabel("Apuesta: $" + playerBets.get(i));
        betLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(14, cardH / 8)));
        betLabel.setBounds(10, 54, 160, 22);
        playerPanel.add(betLabel);
        playerBetLabels.add(betLabel);

        ArrayList<Card> hand = players.get(i).getHand();

        // Panel de las cartas: siempre una sola fila de 5 cartas
        JPanel handPanel = new JPanel(new GridLayout(1, 5, gap, 0));
        handPanel.setOpaque(false);
        handPanel.setBounds(10, 80, playerPanelWidth - 20, cardH + 10);

        if (playerEliminated.get(i)) {
            JLabel fueraLabel = new JLabel("FUERA");
            fueraLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fueraLabel.setFont(new Font("Arial", Font.BOLD, Math.max(22, cardH / 5)));
            fueraLabel.setForeground(Color.RED);
            handPanel.add(fueraLabel);
            // Rellena los espacios restantes para mantener la grilla
            for (int j = 1; j < 5; j++) handPanel.add(Box.createGlue());
        } else {
            for (Card card : hand) {
                JToggleButton cardBtn = card.getCard();
                cardBtn.setPreferredSize(new Dimension(cardW, cardH));
                cardBtn.setMinimumSize(new Dimension(cardW, cardH));
                cardBtn.setMaximumSize(new Dimension(cardW, cardH));
                cardBtn.setFocusable(false);
                cardBtn.setBorderPainted(false);
                cardBtn.setContentAreaFilled(false);
                for (ActionListener al : cardBtn.getActionListeners()) cardBtn.removeActionListener(al);
                handPanel.add(cardBtn);
            }

            for (int j = hand.size(); j < 5; j++) handPanel.add(Box.createGlue());
        }
        playerPanel.add(handPanel);
        return playerPanel;
    }

    // Asigna valores a las cartas con letra
    private String getCardDisplayString(Card card) {
        String value;
        switch (card.getNumber()) {
            case 1: value = "A"; break;
            case 11: value = "J"; break;
            case 12: value = "Q"; break;
            case 13: value = "K"; break;
            default: value = String.valueOf(card.getNumber());
        }
        String suit;
        switch (card.getFigure()) {
            case "Diamante": suit = "♦"; break;
            case "Corazon":  suit = "♥"; break;
            case "Trebol":   suit = "♣"; break;
            case "Pica":     suit = "♠"; break;
            default:         suit = "?";
        }
        return value + " " + suit;
    }
    // Esto actualiza los fatos variantes de la tabla
    private void updateTable() {
        for (int i = 0; i < playerMoneyLabels.size(); i++) {
            playerMoneyLabels.get(i).setText("<html><b>Dinero:</b> $" + players.get(i).getMoney() + "</html>");
            playerBetLabels.get(i).setText("Apuesta: $" + playerBets.get(i));
        }
        potLabel.setText("Bote: $" + pot);
        tablePanel.repaint();
    }

    // La ronda de apuestas para que si todos hacen 'Check', la ronda termina.
    private void startBettingRound() {
        currentBet = 0;
        lastRaise = -1;
        for(int i=0; i<playerBets.size(); i++) playerBets.set(i, 0);
        for(int i=0; i<playerFolded.size(); i++) playerFolded.set(i, false);
        playersInRound = 0;
        for (int i = 0; i < players.size(); i++) {
            if (!playerEliminated.get(i) && players.get(i).getMoney() > 0) playersInRound++;
        }
        runBettingRound();
    }

    private void runBettingRound() {
        int n = players.size();
        int current = 0;
        boolean[] hasActed = new boolean[n];
        Arrays.fill(hasActed, false);
        int checks = 0;
        int raises = 0;
        boolean roundEnded = false;
        int lastToRaise = -1;

        while (!roundEnded) {
            // Saltar jugadores fuera
            if (playerFolded.get(current) || playerEliminated.get(current) || players.get(current).getMoney() <= 0) {
                hasActed[current] = true;
                current = (current + 1) % n;
                continue;
            }
            if (playersInRound == 1) {
                handleOnlyOneLeft();
                return;
            }

            showAllCards(false);
            showPlayerCards(current, true);

            int playerBet = playerBets.get(current);
            int playerMoney = players.get(current).getMoney();
            int diff = currentBet - playerBet;

            String[] options;
            if (diff == 0) {
                options = new String[]{"Check", "Subir", "Retirarse"};
            } else if (playerMoney > diff) {
                options = new String[]{"Igualar", "Subir", "Retirarse"};
            } else {
                options = new String[]{"All-in", "Retirarse"};
            }

            int action = JOptionPane.showOptionDialog(
                    scrollFrame,
                    "Jugador " + (current + 1) + "\nDinero: $" + playerMoney + "\nApuesta actual: $" + currentBet +
                            "\nTu apuesta: $" + playerBet + "\nBote: $" + pot,
                    "Apuesta fija (" + FIXED_BET + ")",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (action == -1) action = 2;

            if (options[action].equals("Check")) {
                hasActed[current] = true;
                checks++;
            } else if (options[action].equals("Igualar") || options[action].equals("All-in")) {
                int toCall = Math.min(diff, playerMoney);
                pot += toCall;
                players.get(current).betMoney(toCall);
                playerBets.set(current, playerBet + toCall);
                hasActed[current] = true;
            } else if (options[action].equals("Subir")) {
                int raiseTo = currentBet + FIXED_BET;
                int toRaise = raiseTo - playerBet;
                if (playerMoney >= toRaise) {
                    pot += toRaise;
                    players.get(current).betMoney(toRaise);
                    playerBets.set(current, raiseTo);
                    currentBet = raiseTo;
                    Arrays.fill(hasActed, false); // Todos deben volver a actuar después de una subida
                    hasActed[current] = true;
                    checks = 0;
                    raises++;
                    lastToRaise = current;
                } else {
                    pot += playerMoney;
                    playerBets.set(current, playerBet + playerMoney);
                    players.get(current).betMoney(playerMoney);
                    hasActed[current] = true;
                }
            } else if (options[action].equals("Retirarse")) {
                playerFolded.set(current, true);
                playersInRound--;
                hasActed[current] = true;
                if (playersInRound == 1) {
                    handleOnlyOneLeft();
                    return;
                }
            }

            updateTable();

            // Avanza al siguiente jugador
            current = (current + 1) % n;

            // Si todos han actuado
            boolean allActed = true;
            for (int i = 0; i < n; i++) {
                if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0 && !hasActed[i]) {
                    allActed = false;
                    break;
                }
            }
            // Si todos han actuado y no hubo subidas, termina la ronda (todos hicieron check o igualaron)
            if (allActed && raises == 0) {
                roundEnded = true;
            }
            // Si todos han igualado la apuesta después de una subida, termina la ronda
            if (raises > 0 && allActed && lastToRaise != -1) {
                // Si el último en subir ya actuó después de su raise
                roundEnded = true;
            }
        }

        showAllCards(false);
        updateTable();
        distributeCardsStage();
    }

    @Override
    public void betStage() {}

    /*
    Esta función es la que se encarga de distribuir cartas aleatorias del mazo.
     */
    @Override
    public void distributeCardsStage() {
        currentPlayerIndex = 0;
        discardForNextPlayer();
    }

    // Esta función es para jugar la ronda de descarte
    private void discardForNextPlayer() {
        while (currentPlayerIndex < players.size() &&
                (playerFolded.get(currentPlayerIndex) || playerEliminated.get(currentPlayerIndex) || players.get(currentPlayerIndex).getMoney() <= 0)) {
            currentPlayerIndex++;
        }
        if (currentPlayerIndex >= players.size()) {
            // Todos los jugadores han hecho su descarte
            showAllCards(true);
            drawTable();
            updateTable();
            startFinalBettingStage();
            return;
        }

        showAllCards(false);
        showPlayerCards(currentPlayerIndex, true);

        Player player = players.get(currentPlayerIndex);
        ChangeCardsDialog changeDialog = new ChangeCardsDialog(scrollFrame, player, currentPlayerIndex);
        changeDialog.setVisible(true); // Esto es modal, así que espera
        drawTable(); // <-- IMPORTANTE: refresca la mesa después del descarte
        updateTable();

        currentPlayerIndex++;
        discardForNextPlayer();
    }

    /*

     */
    private void processNextChangeTurn() {
        while (currentPlayerIndex < players.size() &&
                (playerFolded.get(currentPlayerIndex) || playerEliminated.get(currentPlayerIndex) || players.get(currentPlayerIndex).getMoney() <= 0)) {
            currentPlayerIndex++;
        }
        if (currentPlayerIndex >= players.size()) {
            showAllCards(true);
            drawTable(); // Fuerza que se vean las cartas antes de la ventana final
            updateTable();
            startFinalBettingStage();
            return;
        }

        showAllCards(false);
        showPlayerCards(currentPlayerIndex, true);

        Player player = players.get(currentPlayerIndex);
        ChangeCardsDialog changeDialog = new ChangeCardsDialog(scrollFrame, player, currentPlayerIndex);
        changeDialog.setVisible(true);

        currentPlayerIndex++;
        processNextChangeTurn();
    }

    // Esta función sirve para la ronda final de apuestas
    private void startFinalBettingStage() {
        currentBet = 0;
        lastRaise = 0;
        playerBets.replaceAll(b -> 0);
        playersInRound = 0;
        for (int i = 0; i < players.size(); i++) {
            if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0) playersInRound++;
        }
        bettingFinalRoundLoop(0, -1, false);
    }

    private void bettingFinalRoundLoop(int startPlayer, int lastRaiser, boolean hasRaised) {
        int n = players.size();
        int current = startPlayer;
        int lastToAct = lastRaiser == -1 ? (startPlayer + n - 1) % n : lastRaiser;
        boolean roundDone = false;

        // Seguimiento: asegura que todos actúen
        boolean[] hasActed = new boolean[n];
        Arrays.fill(hasActed, false); // Inicializa todos como no han actuado

        while (!roundDone) {
            // --- Chequeo de jugadores activos ---
            if (playerFolded.get(current) || playerEliminated.get(current) || players.get(current).getMoney() <= 0) {
                hasActed[current] = true; // Marca como "actuado" si no está activo
                current = (current + 1) % n;
                continue;
            }

            showAllCards(false);
            showPlayerCards(current, true);

            int playerBet = playerBets.get(current);
            int playerMoney = players.get(current).getMoney();
            int diff = currentBet - playerBet;

            String[] options;
            if (diff == 0) {
                options = new String[]{"Check", "Subir", "Retirarse"};
            } else if (playerMoney > diff) {
                options = new String[]{"Igualar", "Subir", "Retirarse"};
            } else {
                options = new String[]{"All-in", "Retirarse"};
            }

            int action = JOptionPane.showOptionDialog(
                    scrollFrame,
                    "Jugador " + (current + 1) + "\nDinero: $" + playerMoney + "\nApuesta actual: $" + currentBet +
                            "\nTu apuesta: $" + playerBet + "\nBote: $" + pot,
                    "Apuesta final (" + FIXED_BET + ")",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (action == -1) action = 2;

            if (options[action].equals("Check")) {
                hasActed[current] = true; // Marca como actuado
            } else if (options[action].equals("Igualar") || options[action].equals("All-in")) {
                int toCall = Math.min(diff, playerMoney);
                pot += toCall;
                players.get(current).betMoney(toCall);
                playerBets.set(current, playerBet + toCall);
                hasActed[current] = true; // Marca como actuado
            } else if (options[action].equals("Subir")) {
                int raiseTo = currentBet + FIXED_BET;
                if (playerMoney >= (raiseTo - playerBet)) {
                    int toRaise = raiseTo - playerBet;
                    pot += toRaise;
                    players.get(current).betMoney(toRaise);
                    playerBets.set(current, raiseTo);
                    currentBet = raiseTo;
                    Arrays.fill(hasActed, false); // Reinicia "hasActed" porque hay nueva subida
                    lastToAct = current;
                } else {
                    pot += playerMoney;
                    playerBets.set(current, playerBet + playerMoney);
                    players.get(current).betMoney(playerMoney);
                }
            } else if (options[action].equals("Retirarse")) {
                playerFolded.set(current, true);
                playersInRound--;
                hasActed[current] = true; // Marca como actuado
                if (playersInRound == 1) {
                    handleOnlyOneLeft();
                    return;
                }
            }

            updateTable();

            // Avanza al siguiente jugador
            current = (current + 1) % n;

            // Verifica si todos los jugadores activos han actuado
            roundDone = true;
            for (int i = 0; i < n; i++) {
                if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0 && !hasActed[i]) {
                    roundDone = false;
                    break;
                }
            }
        }

        showAllCards(true);
        drawTable();
        updateTable();
        fightStage(); // Avanza al showdown
    }

    // Este asigna un ganador dependiendo de las cartas de los jugadores
    @Override
    public void fightStage() {
        showAllCards(true);
        drawTable(); // Asegura que se vean las cartas en la mesa antes del mensaje
        updateTable();

        int bestRank = 11;
        ArrayList<Integer> finalists = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tieBreakers = new ArrayList<>();

        // Determina el mejor rango y recopila los finalistas (empate)
        for (int i = 0; i < players.size(); i++) {
            if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0) {
                int rank = players.get(i).analyzeHand();
                if (rank < bestRank) {
                    bestRank = rank;
                }
            }
        }
        // Recolecta finalistas y sus criterios de desempate
        for (int i = 0; i < players.size(); i++) {
            if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0) {
                int rank = players.get(i).analyzeHand();
                if (rank == bestRank) {
                    finalists.add(i);
                    tieBreakers.add(getTieBreakerValues(players.get(i), bestRank));
                }
            }
        }

        int winnerIdx = 0;
        if (finalists.size() > 1) {
            ArrayList<Integer> winnerValues = tieBreakers.get(0);
            for (int i = 1; i < tieBreakers.size(); i++) {
                ArrayList<Integer> challenger = tieBreakers.get(i);
                boolean challengerWins = false;
                for (int j = 0; j < Math.min(winnerValues.size(), challenger.size()); j++) {
                    if (challenger.get(j) > winnerValues.get(j)) {
                        challengerWins = true;
                        break;
                    } else if (challenger.get(j) < winnerValues.get(j)) {
                        break;
                    }
                }
                if (challengerWins) {
                    winnerIdx = i;
                    winnerValues = challenger;
                }
            }
        }

        int winnerPlayerIdx = finalists.get(winnerIdx);
        players.get(winnerPlayerIdx).addMoney(pot);

        String resultMsg = "¡Jugador " + (winnerPlayerIdx + 1) + " gana el bote de $" + pot + "!\n" +
                "Jugada ganadora: " + getHandRankName(bestRank) + "\n" +
                "Su mano: " + getHandDescription(players.get(winnerPlayerIdx));

        pot = 0;
        updateTable();
        JOptionPane.showMessageDialog(scrollFrame, resultMsg, "Ganador", JOptionPane.INFORMATION_MESSAGE);
        finishRound();
    }


    private String getHandRankName(int rank) {
        switch(rank) {
            case 1: return "Escalera Real";
            case 2: return "Escalera de Color";
            case 3: return "Póker";
            case 4: return "Full House";
            case 5: return "Color";
            case 6: return "Escalera";
            case 7: return "Trío";
            case 8: return "Doble Par";
            case 9: return "Un Par";
            case 10: return "Carta Alta";
            default: return "Desconocido";
        }
    }

    // Devuelve valores relevantes para desempate según la mano
    private ArrayList<Integer> getTieBreakerValues(Player player, int handRank) {
        ArrayList<Card> hand = new ArrayList<>(player.getHand());
        ArrayList<Integer> values = new ArrayList<>();
        // Ordenar de mayor a menor
        hand.sort((a, b) -> Integer.compare(cardValueDesc(b.getNumber()), cardValueDesc(a.getNumber())));
        int[] counts = new int[15]; // 1(A) - 14(A) 2-13 naturales
        for (Card c : hand) counts[cardValueDesc(c.getNumber())]++;
        switch (handRank) {
            case 1: // Escalera real
                // Empate total, no hay desempate
                break;
            case 2: // Escalera de color
            case 6: // Escalera
                // Carta más alta de la escalera
                values.add(getStraightHighCard(hand));
                break;
            case 3: // Póker
                int poker = 0, kickerP = 0;
                for (int i = 14; i >= 2; i--) {
                    if (counts[i] == 4) poker = i;
                    if (counts[i] == 1) kickerP = i;
                }
                values.add(poker);
                values.add(kickerP);
                break;
            case 4: // Full house
                int trioF = 0, pairF = 0;
                for (int i = 14; i >= 2; i--) {
                    if (counts[i] == 3) trioF = i;
                    if (counts[i] == 2) pairF = i;
                }
                values.add(trioF);
                values.add(pairF);
                break;
            case 5: // Color (flush)
                for (Card c : hand) values.add(cardValueDesc(c.getNumber()));
                break;
            case 7: // Trío
                int trio = 0;
                ArrayList<Integer> kickersT = new ArrayList<>();
                for (int i = 14; i >= 2; i--) {
                    if (counts[i] == 3) trio = i;
                    if (counts[i] == 1) kickersT.add(i);
                }
                values.add(trio);
                values.addAll(kickersT);
                break;
            case 8: // Doble par
                int parAlto = 0, parBajo = 0, kickerD = 0;
                for (int i = 14; i >= 2; i--) {
                    if (counts[i] == 2) {
                        if (parAlto == 0) parAlto = i;
                        else if (parBajo == 0) parBajo = i;
                    }
                    if (counts[i] == 1) kickerD = i;
                }
                values.add(parAlto);
                values.add(parBajo);
                values.add(kickerD);
                break;
            case 9: // Un par
                int par = 0;
                ArrayList<Integer> kickersP = new ArrayList<>();
                for (int i = 14; i >= 2; i--) {
                    if (counts[i] == 2) par = i;
                    if (counts[i] == 1) kickersP.add(i);
                }
                values.add(par);
                values.addAll(kickersP);
                break;
            case 10: // Carta alta
                for (Card c : hand) values.add(cardValueDesc(c.getNumber()));
                break;
            default:
                // Para cualquier otra jugada, se compara por carta alta
                for (Card c : hand) values.add(cardValueDesc(c.getNumber()));
        }
        return values;
    }

    // Devuelve la carta más alta de una escalera (considerando el caso especial A-2-3-4-5)
    private int getStraightHighCard(ArrayList<Card> hand) {
        ArrayList<Integer> cards = new ArrayList<>();
        for (Card c : hand) cards.add(cardValueDesc(c.getNumber()));
        Collections.sort(cards, Collections.reverseOrder());
        // Caso especial: A-2-3-4-5
        if (cards.equals(Arrays.asList(14, 5, 4, 3, 2))) return 5;
        return cards.get(0);
    }

    private int cardValueDesc(int v) {
        return v == 1 ? 14 : v;
    }


    private String getHandDescription(Player player) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Card> hand = player.getHand();
        hand.forEach(card -> sb.append(getCardDisplayString(card)).append(" "));
        return sb.toString().trim();
    }

    // Este sirve para finalizar la ronda si solo queda un jugador
    private void finishRound() {
        for (int i = 0; i < players.size(); i++) {
            if (!playerEliminated.get(i) && players.get(i).getMoney() <= 0) {
                handleBankruptcy(i);
            }
        }

        // Checa si solo queda un jugador NO eliminado, ese es el ganador absoluto
        int jugadoresVivos = 0;
        int ganadorIdx = -1;
        for (int i = 0; i < players.size(); i++) {
            if (!playerEliminated.get(i)) {
                jugadoresVivos++;
                ganadorIdx = i;
            }
        }
        if (jugadoresVivos == 1) {
            showAllCards(true);
            drawTable();
            updateTable();
            JFrame winnerFrame = new JFrame("Fin del Juego");
            winnerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            winnerFrame.setSize(460, 250);
            winnerFrame.setLayout(null);
            winnerFrame.setLocationRelativeTo(null);

            JLabel winLabel = new JLabel("<html><center>¡Jugador " + (ganadorIdx + 1) + " es el ganador absoluto!<br>No quedan más jugadores activos.</center></html>", SwingConstants.CENTER);
            winLabel.setFont(new Font("Arial", Font.BOLD, 18));
            winLabel.setBounds(25, 20, 400, 70);
            winnerFrame.add(winLabel);

            JButton mainMenuBtn = new JButton("Volver al menú principal");
            mainMenuBtn.setFont(new Font("Arial", Font.BOLD, 16));
            mainMenuBtn.setBounds(60, 110, 320, 40);
            mainMenuBtn.addActionListener(e -> {
                winnerFrame.dispose();
                scrollFrame.dispose();
                showMainMenu();
            });
            winnerFrame.add(mainMenuBtn);

            JButton closeBtn = new JButton("Cerrar juego");
            closeBtn.setFont(new Font("Arial", Font.BOLD, 16));
            closeBtn.setBounds(140, 160, 160, 35);
            closeBtn.addActionListener(e -> {
                winnerFrame.dispose();
                scrollFrame.dispose();
                System.exit(0);
            });
            winnerFrame.add(closeBtn);

            winnerFrame.setVisible(true);
            return;
        }

        // Este pregunta al final de cada ronda si deseas jugar otra ronda
        int option = JOptionPane.showConfirmDialog(
                scrollFrame,
                "¿Jugar otra ronda?",
                "Nueva ronda",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            resetRound();
        } else {
            scrollFrame.dispose();
            showMainMenu();
        }
    }

    //presenta la opción de quedar en bancarrota o depositar dinero
    private void handleBankruptcy(int playerIndex) {
        while (!playerEliminated.get(playerIndex) && players.get(playerIndex).getMoney() <= 0) {
            Object[] options = {"Bancarrota", "Depositar dinero"};
            int choice = JOptionPane.showOptionDialog(
                    scrollFrame,
                    "¡Haz perdido todo tu dinero!",
                    "Bancarrota - Jugador " + (playerIndex + 1),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 1) {
                // Panel personalizado con botones para depositar
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JLabel label = new JLabel("¿Cuánto dinero deseas ingresar?");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(label);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                int[] cantidades = {500, 700, 1000};
                JButton[] botones = new JButton[cantidades.length];
                final int[] seleccionado = {-1};

                for (int i = 0; i < cantidades.length; i++) {
                    int cantidad = cantidades[i];
                    botones[i] = new JButton("$" + cantidad);
                    botones[i].setFocusable(false);
                    botones[i].addActionListener(e -> {
                        seleccionado[0] = cantidad;
                        Window win = SwingUtilities.getWindowAncestor(panel);
                        if (win != null) win.dispose();
                    });
                    buttonPanel.add(botones[i]);
                }
                panel.add(Box.createVerticalStrut(10));
                panel.add(buttonPanel);

                // Muestra el panel en un JDialog modal
                JDialog dialog = new JDialog((Frame) null, "Depositar dinero", true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(panel);
                dialog.pack();
                dialog.setLocationRelativeTo(scrollFrame);
                dialog.setVisible(true);

                if (seleccionado[0] > 0) {
                    players.get(playerIndex).addMoney(seleccionado[0]);
                    updateTable();
                    break;
                } // si no seleccionó nada, vuelve a mostrar el menú principal
            } else {
                JOptionPane.showMessageDialog(
                        scrollFrame,
                        "¡Jugador " + (playerIndex + 1) + " está en bancarrota y no podrá seguir jugando!",
                        "Bancarrota",
                        JOptionPane.INFORMATION_MESSAGE
                );
                players.get(playerIndex).fold();
                playerEliminated.set(playerIndex, true);
                break;
            }
        }
        drawTable();
    }

    // Regresa al Main
    private void showMainMenu() {
        MainGame.main(null);
    }

    // Reinicia la ronda para los jugadores
    private void resetRound() {
        deck = new CardDeck(600, 20, 10);
        cardDeck = deck.getCardDeck();

        Iterator<Player> it = players.iterator();
        int idx = 0;
        while (it.hasNext()) {
            Player player = it.next();
            player.clearHand();
            if (!playerEliminated.get(idx) && player.getMoney() > 0) {
                player.unlockPlayer();
                for (int j = 0; j < 5; j++) {
                    player.addCard(getRandomCardFromDeck());
                }
            } else {
                player.fold();
            }
            idx++;
        }
        playerBets.replaceAll(b -> 0);
        playerFolded.replaceAll(f -> false);
        pot = 0;
        playersInRound = playersAmount;
        drawTable();
        startBettingRound();
    }

    // Este fragmento sirve para mostrar todas las cartas
    private void showAllCards(boolean faceUp) {
        for (int i = 0; i < players.size(); i++) {
            if (playerEliminated.get(i)) continue;
            ArrayList<Card> hand = players.get(i).getHand();
            hand.forEach(card -> {
                if (faceUp) card.turnUpCard();
                else card.turnDownCard();
            });
        }
        updateTable();
    }

    // Este muestra las cartas del jugador que esté en la ronda de apuestas
    private void showPlayerCards(int idx, boolean faceUp) {
        if (playerEliminated.get(idx)) return;
        ArrayList<Card> hand = players.get(idx).getHand();
        hand.forEach(card -> {
            if (faceUp) card.turnUpCard();
            else card.turnDownCard();
        });
        updateTable();
    }

    // Si todos se retiran, el último jugador en la mesa gana en automático
    private void handleOnlyOneLeft() {
        int winner = -1;
        for (int i = 0; i < players.size(); i++) {
            if (!playerFolded.get(i) && !playerEliminated.get(i) && players.get(i).getMoney() > 0) {
                winner = i;
                break;
            }
        }
        if (winner != -1) {
            players.get(winner).addMoney(pot);
            showAllCards(true);
            drawTable();
            updateTable();
            JOptionPane.showMessageDialog(scrollFrame, "¡Jugador " + (winner + 1) + " gana el bote de $" + pot + " por abandono!", "Ronda terminada", JOptionPane.INFORMATION_MESSAGE);
        }
        finishRound();
    }

    // Diálogo para cambio de cartas
    private class ChangeCardsDialog extends JDialog {
        public ChangeCardsDialog(JFrame parent, Player player, int playerIdx) {
            super(parent, "Cambio de cartas - Jugador " + (playerIdx + 1), true);
            setSize(700, 270);
            setLocationRelativeTo(parent);
            setLayout(null);

            if (playerEliminated.get(playerIdx)) {
                JLabel fueraLabel = new JLabel("FUERA");
                fueraLabel.setHorizontalAlignment(SwingConstants.CENTER);
                fueraLabel.setFont(new Font("Arial", Font.BOLD, 36));
                fueraLabel.setForeground(Color.RED);
                fueraLabel.setBounds(0, 60, 700, 100);
                add(fueraLabel);
                JButton okBtn = new JButton("OK");
                okBtn.setBounds(270, 180, 150, 40);
                okBtn.setFont(new Font("Arial", Font.BOLD, 18));
                okBtn.addActionListener(e -> setVisible(false));
                add(okBtn);
                return;
            }

            JLabel instr = new JLabel("Selecciona cartas para cambiar y presiona 'Cambiar'");
            instr.setFont(new Font("Arial", Font.BOLD, 17));
            instr.setBounds(30, 10, 600, 30);
            add(instr);

            ArrayList<Card> hand = player.getHand();
            ArrayList<JToggleButton> cardButtons = new ArrayList<>();
            int gap = 30;

            hand.forEach(Card::turnUpCard);

            int cardW = hand.get(0).getWidth();
            int cardH = hand.get(0).getHeight();
            int totalWidth = hand.size() * cardW + (hand.size()-1) * gap;
            int xStart = (getWidth() - totalWidth) / 2;

            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                JToggleButton btn = card.getCard();
                btn.setEnabled(true);
                btn.setSelected(false);
                btn.setBounds(xStart + i * (cardW + gap), 60, cardW, cardH);
                card.turnUpCard();
                btn.addActionListener(e -> {
                    if (btn.isSelected()) {
                        card.turnDownCard();
                    } else {
                        card.turnUpCard();
                    }
                });
                add(btn);
                cardButtons.add(btn);

                JLabel nameLabel = new JLabel(getCardDisplayString(card));
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                nameLabel.setForeground(Color.BLACK);
                nameLabel.setBounds(xStart + i * (cardW + gap), 60 + cardH, cardW, 25);
                add(nameLabel);
            }

            JButton exchangeBtn = new JButton("Cambiar");
            exchangeBtn.setFont(new Font("Arial", Font.BOLD, 18));
            exchangeBtn.setBounds((getWidth()-180)/2, 180, 180, 40);
            add(exchangeBtn);

            exchangeBtn.addActionListener(e -> {
                int changed = 0;
                for (int i = hand.size() - 1; i >= 0; i--) {
                    if (cardButtons.get(i).isSelected()) {
                        hand.remove(i);
                        changed++;
                    }
                }
                for (int i = 0; i < changed; i++) {
                    Card newCard = getRandomCardFromDeck();
                    player.addCard(newCard);
                }
                setVisible(false);
            });
        }
    }
}