package project;

import javax.swing.*;
import java.awt.*;

public class Card{
    /**
     * number guarda el valor de la carta
     * 1 = As.
     * 2 - 10 = Numeros de 2 a 10.
     * 11 = Jota.
     * 12 = Reina.
     * 13 = Rey.
     */
    private int number;

    /**
     * isTurned determina si la carta esta o no volteada cara arriba
     * true = Esta volteada cara arriba
     * false = Esta volteada cara abajo
     */
    private boolean isTurned;

    private String figure; //Guarda la figura del que es la carta
    private JToggleButton physicalCard; //Representa el boton interactivo para la carta
    private ImageIcon backSideImage; //Guarda la imagen reescalada de la carta cara abajo
    private ImageIcon frontSideImage; //Guarda la imagen reescalada de la carta cara arriba

    //Genera la carta visualmente en un punto especifico
    //preferentemente en el punto de reparto
    public Card(int number, String figure, String imagePath, int xPosition, int yPosition, int size){
        //Guardar los valores de figura y numero de carta
        this.figure = figure;
        this.number = number;

        //Establecer volteado boca abajo por defecto
        isTurned = false;

        //Generar carta visual
        generateCard(xPosition, yPosition, size, imagePath);
    }

    //Crea la carta visual
    public void generateCard(int xPosition, int yPosition, int size, String imagePath){
        //Dimensiones de la carta
        int width = (691 / 100) * size;
        int height = (1056 / 100) * size;

        //Crear carta
        physicalCard = new JToggleButton("");
        physicalCard.setSelected(false);
        physicalCard.setBackground(Color.BLACK);
        physicalCard.setSize(width, height);
        physicalCard.setLocation(xPosition, yPosition);
        physicalCard.setVisible(true);

        //Crear imagen cara abajo de la carta
        ImageIcon ogImage = new ImageIcon("src/cardImages/Carta Atras.png");
        backSideImage = new ImageIcon(ogImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));

        //Crear imagen cara arriba de la carta
        ImageIcon ogImageII = new ImageIcon(imagePath);
        frontSideImage = new ImageIcon(ogImageII.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));

        //Configurar carta boca abajo (por defecto)
        isTurned = false;
        physicalCard.setIcon(backSideImage);
    }

    //Devuelve la carta fisica, logrando agregarlo a las ventanas de juego
    public JToggleButton getCard(){
        return physicalCard;
    }

    //Voltear carta cara arriba
    public void turnUpCard(){
        //Cambiar imagen de la carta
        physicalCard.setIcon(frontSideImage);

        //Cambiar estado de la carta
        isTurned = true;
    }

    //Voltear carta boca abajo
    public void turnDownCard(){
        //Cambiar imagen de la carta
        physicalCard.setIcon(backSideImage);

        //Cambiar estado de la carta
        isTurned = false;
    }

    //Regresar estado de volteo de la carta
    public boolean getCardState(){
        return isTurned;
    }

    //Mover carta a otra pocision en ventana
    public void relocateCard(int xPosition, int yPosition){
        physicalCard.setLocation(xPosition, yPosition);
    }

    //Obtener ancho de la carta (para facilitar el pocisionaiento de cartas)
    public int getWidth(){
        return physicalCard.getWidth();
    }

    //Obtener alto de la carta (para facilitar el pocisionamiento de cartas)
    public int getHeight(){
        return physicalCard.getHeight();
    }

    //Obtener valor numerico de la carta
    public int getNumber(){
        return number;
    }

    //Obtener figura de la carta
    public String getFigure(){
        return figure;
    }

    @Override
    public String toString(){
        return "Carta " + number + " " + figure;
    }
}
