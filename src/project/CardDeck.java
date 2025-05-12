package project;

import java.util.ArrayList;

public class CardDeck{
    //Guardar las 52 cartas de poker
    private ArrayList<Card> cardDeck;

    //Generar mazo de cartas al crear el objeto (En una pocision espefica)
    public CardDeck(int xPosition, int yPosition, int size){
        //Crear ArrayList para el mazo de cartas
        cardDeck = new ArrayList<>();

        //Crear mazo
        generateDeck(xPosition, yPosition, size);
    }

    //Generar mazo de cartas
    public void generateDeck(int xPosition, int yPosition, int size){
        //Controlar el numero de carta
        for(int i = 1; i <= 13; i++){
            //Controlar la figura de carta
            for(int j = 1; j <= 4; j++){
                //Determinar figura de carta
                String figure = "";
                switch(j){
                    case 1:
                        figure = "Diamante";
                        break;

                    case 2:
                        figure = "Corazon";
                        break;

                    case 3:
                        figure = "Trebol";
                        break;

                    case 4:
                        figure = "Pica";
                        break;

                    default:
                        //Break
                        break;
                }

                //Generar ruta de imagen
                String imagePath = "src/cardImages/Carta " + i + " " + figure + ".jpg";

                //Crear carta
                Card card = new Card(i, figure, imagePath, xPosition, yPosition, size);
                cardDeck.add(card);
            }
        }
    }

    //Regresar carta de mazo y eliminarlo
    public Card getCard(int index){
        Card card = cardDeck.get(index);
        cardDeck.remove(index);
        return card;
    }

    //Regresar mazo de cartas (ArrayList)
    public ArrayList<Card> getCardDeck(){
        return cardDeck;
    }
}
