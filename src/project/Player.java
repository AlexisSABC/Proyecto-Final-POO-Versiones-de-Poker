package project;

import java.util.*;

public class Player{
    private ArrayList<Card> hand; //Guardar mano de 5 o 2 cartas del jugador
    private int money; //Guardar dinero que tiene el jugador

    /**
     * stillInGame determina si un jugador todavia esta en el juego o ha pasado
     * true = Aun esta jugando.
     * false = Ya dejo en juego
     */
    private boolean stillInGame;

    //Inicliazar jugador
    public Player(int money){
        hand = new ArrayList<>();
        this.money = money;
        stillInGame = true;
    }

    //Recuperar la cantidad de dinero (Apto para mostrar en pantalla y mas acciones)
    public int getMoney(){
        return money;
    }

    //Agregar dinero (Dinero obtenido de los botes de la partidas)
    public void addMoney(int money){
        this.money = money;
    }

    //Recuperar el estatus del jugador (Para poder seguir jugando o no)
    public boolean getPlayerStatus(){
        return stillInGame;
    }

    //Recuperar mano de cartas
    public ArrayList<Card> getHand(){
        return hand;
    }

    //Obtener nueva carta
    public void addCard(Card card){
        hand.add(card);
    }

    //Devolver y eliminar carta de mano
    public Card returnCard(int index){
        Card card = hand.get(index);
        hand.remove(index);
        return card;
    }

    //Quita dinero durante las apuestas (Realiza ya sea el bet, call o raise)
    public void betMoney(int money) {
        this.money = this.money - money;
    }

    //Salir del juego
    public void fold(){
        stillInGame = false;
        hand.clear();
    }

    /**
     * Determinar el rango de mano de cartas
     * 1 = Royal flush.
     * 2 = Straight flush.
     * 3 = Four of a kind.
     * 4 = Full house.
     * 5 = Flush.
     * 6 = Straight.
     * 7 = Three of a kind.
     * 8 = Two Pair.
     * 9 = One Pair.
     * 10 = HighCard.
     */
    public int analyzeHand(){
        int handRange = 0; //Guarda el rango de la mano

        //Ordena mano segun el numero de las cartas
        ArrayList<Card> sortedHand = new ArrayList<>(hand);
        Collections.sort(sortedHand, Comparator.comparing(Card::getNumber));

        //Determinar rango de la mano
        if(findRoyalFlush(sortedHand)){
            handRange = 1;

        }else if(findStraightFlush(sortedHand)){
            handRange = 2;

        }else if(findFourKind(sortedHand)){
            handRange = 3;

        }else if(findFullHouse(sortedHand)){
            handRange = 4;

        }else if(findFlush(sortedHand)){
            handRange = 5;

        }else if(findStraight(sortedHand)){
            handRange = 6;

        }else if(findThreeKind(sortedHand)){
            handRange = 7;

        }else if(findTwoPair(sortedHand)){
            handRange = 8;

        }else if(findOnePair(sortedHand)){
            handRange = 9;

        }else{
            handRange = 10;
        }

        return handRange;
    }

    //Determinar existencia de "Escalera Real" en la mano
    private boolean findRoyalFlush(ArrayList<Card> sortedHand){
        boolean existHandType = false;

        //Determianr si existen 5 cartas del mismo palo
        boolean keepAnalyzing = findFiguresAmount(sortedHand, 5);

        //Determinar si se sigue la secuencia
        if(keepAnalyzing){
            if(sortedHand.get(0).getNumber() == 1){
                if(sortedHand.get(1).getNumber() == 10){
                    if(sortedHand.get(2).getNumber() == 11){
                        if(sortedHand.get(3).getNumber() == 12){
                            if(sortedHand.get(4).getNumber() == 13){
                                existHandType = true;
                            }
                        }
                    }
                }
            }
        }

        return existHandType;
    }

    //Determinar existencia de "Escalera de color" en la mano
    private boolean findStraightFlush(ArrayList<Card> sortedHand){
        boolean existHandType = false;

        //Determianr si existen 5 cartas del mismo palo
        boolean keepAnalyzing = findFiguresAmount(sortedHand, 5);

        //Dterminar si existe secuencia
        if(keepAnalyzing){
            if((sortedHand.get(0).getNumber() + 1) == sortedHand.get(1).getNumber()){
                if((sortedHand.get(1).getNumber() + 1) == sortedHand.get(2).getNumber()){
                    if((sortedHand.get(2).getNumber() + 1) == sortedHand.get(3).getNumber()){
                        if((sortedHand.get(3).getNumber() + 1) == sortedHand.get(4).getNumber()){
                            existHandType = true;
                        }
                    }
                }
            }
        }

        return existHandType;
    }

    //Determinar existencia de "Poker" en la mano
    private boolean findFourKind(ArrayList<Card> sortedHand){
        boolean existHandType = findNumbersAmount(sortedHand, 4);
        return existHandType;
    }

    //Determinar existencia de "Casa llena" en la mano
    private boolean findFullHouse(ArrayList<Card> sortedHand){
        boolean existHandType = false;

        //Determinar cuantos numeros existen
        HashMap<Integer, Integer> numbers = new HashMap<>();
        for(int i = 0; i < sortedHand.size(); i++){
            if(numbers.containsKey(sortedHand.get(i).getNumber())){
                numbers.put(sortedHand.get(i).getNumber(), numbers.get(sortedHand.get(i).getNumber()) + 1);
            }else{
                numbers.put(sortedHand.get(i).getNumber(), 1);
            }
        }

        //Validar si existe un trio y un par
        Collection<Integer> numbersAmount = numbers.values();
        List<Integer> listNumbersAmount = new ArrayList<>(numbersAmount);

        if((listNumbersAmount.get(0) == 2) && (listNumbersAmount.get(1) == 3)){
            existHandType = true;
        }else{
            if((listNumbersAmount.get(0) == 3) && (listNumbersAmount.get(1) == 2)) {
                existHandType = true;
            }
        }

        return existHandType;
    }

    //Determinar existencia de "Color" en la mano
    private boolean findFlush(ArrayList<Card> sortedHand){
        boolean existHandType = findFiguresAmount(sortedHand, 5);
        return existHandType;
    }

    //Determinar existencia de "Escalera" en la mano
    private boolean findStraight(ArrayList<Card> sortedHand){
        boolean existHandType = false;

        //Verificar existencia de escalera
        if((sortedHand.get(0).getNumber() + 1) == sortedHand.get(1).getNumber()){
            if((sortedHand.get(1).getNumber() + 1) == sortedHand.get(2).getNumber()){
                if((sortedHand.get(2).getNumber() + 1) == sortedHand.get(3).getNumber()){
                    if((sortedHand.get(3).getNumber() + 1) == sortedHand.get(4).getNumber()){
                        existHandType = true;
                    }
                }
            }
        }

        return existHandType;
    }

    //Determinar existencia de "Trio" en la mano
    private boolean findThreeKind(ArrayList<Card> sortedHand){
        boolean existHandType = findNumbersAmount(sortedHand, 3);
        return existHandType;
    }

    //Determinar existencia de "Doble Par" en la mano
    private boolean findTwoPair(ArrayList<Card> sortedHand){
        boolean existHandType = findPairs(sortedHand, 2);
        return existHandType;
    }

    //Determinar existencia de "Par" en la mano
    private boolean findOnePair(ArrayList<Card> sortedHand){
        boolean existHandType = findPairs(sortedHand, 1);
        return existHandType;
    }

    //Determinar si existe una cantidad de figuras
    private boolean findFiguresAmount(ArrayList<Card> sortedHand, int figureAmount){
        boolean keepAnalyzing = false;

        //Determinar si las n cartas son del mismo palo
        HashMap<String, Integer> figures = new HashMap<>();
        for(int i = 0; i < sortedHand.size(); i++){
            if(figures.containsKey(sortedHand.get(i).getFigure())){
                figures.put(sortedHand.get(i).getFigure(), figures.get(sortedHand.get(i).getFigure()) + 1);
            }else{
                figures.put(sortedHand.get(i).getFigure(), 1);
            }
        }

        Set<String> keys = figures.keySet();
        Iterator<String> keyIterator = keys.iterator();
        while(keyIterator.hasNext()){
            if(figures.get(keyIterator.next()) == figureAmount){
                keepAnalyzing = true;
            }
        }

        return keepAnalyzing;
    }

    //Determinar si existe una cantidad de numeros
    private boolean findNumbersAmount(ArrayList<Card> sortedHand, int numberAmount){
        boolean keepAnalyzing = false;

        //Determinar si las n cartas son tienen el mismo numero
        HashMap<Integer, Integer> numbers = new HashMap<>();
        for(int i = 0; i < sortedHand.size(); i++){
            if(numbers.containsKey(sortedHand.get(i).getNumber())){
                numbers.put(sortedHand.get(i).getNumber(), numbers.get(sortedHand.get(i).getNumber()) + 1);
            }else{
                numbers.put(sortedHand.get(i).getNumber(), 1);
            }
        }

        Set<Integer> keys = numbers.keySet();
        Iterator<Integer> keyIterator = keys.iterator();
        while(keyIterator.hasNext()){
            if(numbers.get(keyIterator.next()) == numberAmount){
                keepAnalyzing = true;
            }
        }

        return keepAnalyzing;
    }

    //Encontrar pares
    private boolean findPairs(ArrayList<Card> sortedHand, int pairsAmount) {
        boolean keepAnalyzing = false;

        //Determinar cuantos numeros existen
        HashMap<Integer, Integer> numbers = new HashMap<>();
        for(int i = 0; i < sortedHand.size(); i++){
            if(numbers.containsKey(sortedHand.get(i).getNumber())){
                numbers.put(sortedHand.get(i).getNumber(), numbers.get(sortedHand.get(i).getNumber()) + 1);
            }else{
                numbers.put(sortedHand.get(i).getNumber(), 1);
            }
        }

        //Verificar que existan 2 pares
        Collection<Integer> numbersAmount = numbers.values();

        int pairsCounter = 0;
        for(Integer value: numbersAmount){
            if(value == 2){
                pairsCounter++;
            }
        }

        if(pairsCounter == pairsAmount){
            keepAnalyzing = true;
        }

        return keepAnalyzing;
    }
}