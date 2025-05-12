package project;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame {
    public static void main(String[] args){
        //Dimensiones de la ventana
        int width = 500;
        int height = 400;

        //Crear ventana de seleccion de jugadores
        JFrame selectPokerWindow = new JFrame("Proyecto Final - Poker");
        selectPokerWindow.setSize(width, height);
        selectPokerWindow.setLayout(null);
        selectPokerWindow.setVisible(true);
        selectPokerWindow.setResizable(false);

        //Titulo de proyecto final
        JLabel projectTitle = new JLabel("Proyecto Final - Poker");
        projectTitle.setFont(new Font("Arial", Font.BOLD, 32));
        projectTitle.setSize(width - 16, projectTitle.getPreferredSize().height);
        projectTitle.setLocation(0, 20);
        projectTitle.setVerticalAlignment(JLabel.CENTER);
        projectTitle.setHorizontalAlignment(JLabel.CENTER);
        selectPokerWindow.add(projectTitle);

        //Instruccion
        JLabel instructions = new JLabel("Selecciona el modo de Poker a jugar");
        instructions.setFont(new Font("Arial", Font.BOLD, 23));
        instructions.setSize(width - 16, instructions.getPreferredSize().height);
        instructions.setLocation(0, 70);
        instructions.setVerticalAlignment(JLabel.CENTER);
        instructions.setHorizontalAlignment(JLabel.CENTER);
        selectPokerWindow.add(instructions);

        //Opcion "5 Card Draw"
        JRadioButton cardDrawOption = new JRadioButton("5 Card Draw", true);
        cardDrawOption.setFont(new Font("Arial", Font.BOLD, 28));
        cardDrawOption.setSize(250, cardDrawOption.getPreferredSize().height);
        cardDrawOption.setLocation(35, 130);
        cardDrawOption.setVerticalAlignment(JLabel.CENTER);
        cardDrawOption.setBackground(Color.WHITE);
        cardDrawOption.setVisible(true);
        selectPokerWindow.add(cardDrawOption);

        //Opcion "Texas Hold'em"
        JRadioButton texasOption = new JRadioButton("Texas Hold'em");
        texasOption.setFont(new Font("Arial", Font.BOLD, 28));
        texasOption.setSize(250, texasOption.getPreferredSize().height);
        texasOption.setLocation(35, 190);
        texasOption.setVerticalAlignment(JLabel.CENTER);
        texasOption.setBackground(Color.WHITE);
        texasOption.setVisible(true);
        selectPokerWindow.add(texasOption);

        //Grupo de botones
        ButtonGroup pokerGroup = new ButtonGroup();
        pokerGroup.add(cardDrawOption);
        pokerGroup.add(texasOption);

        //Boton de aceptacion
        JButton playButton = new JButton("Empezar");
        playButton.setFont(new Font("Arial", Font.BOLD, 28));
        playButton.setSize(200, playButton.getPreferredSize().height);
        playButton.setLocation((width - playButton.getWidth()) / 2, 270);
        playButton.setVerticalAlignment(JLabel.CENTER);
        playButton.setHorizontalAlignment(JLabel.CENTER);
        playButton.setBackground(Color.WHITE);
        playButton.setBorder(new LineBorder(Color.BLACK, 4, true));
        playButton.setVisible(true);
        selectPokerWindow.add(playButton);

        //Inicializar juego elegido
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //Iniciar juego "5 Card Draw"
                if(cardDrawOption.isSelected()){
                    FiveCardDraw cardDrawGame = new FiveCardDraw();
                }

                //Iniciar juego "Texas Hold'em"
                if(texasOption.isSelected()){
                    TexasHoldEm texasGame = new TexasHoldEm();
                }

                selectPokerWindow.dispose();
            }
        });

        //Imagen de carta
        ImageIcon ogImage = new ImageIcon("src/cardImages/Carta 1 Diamante.jpg");
        ImageIcon cardImage = new ImageIcon(ogImage.getImage().getScaledInstance(691 / 10, 1056 / 10, Image.SCALE_SMOOTH));

        JLabel imageCard = new JLabel(cardImage);
        imageCard.setSize(691 / 10, 1056 / 10);
        imageCard.setLocation((width - imageCard.getWidth()) - 120, ((height - imageCard.getHeight()) / 2) - 20);
        imageCard.setVisible(true);
        selectPokerWindow.add(imageCard);

        //Pintar fondo
        Container background = selectPokerWindow.getContentPane();
        background.setBackground(Color.WHITE);
    }
}