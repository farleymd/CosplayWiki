package Marty.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by marty.farley on 5/6/2015.
 */
public class newCharacter extends JFrame {
    private WikiDB wikiDB;

    private JTextField nameText;
    private JComboBox genderCombo;
    private JTextField genreText;
    private JTextField universeText;
    private JTextField mediaText;
    private JTextField descriptionText;
    private JButton quitButton;
    private JButton addCharacterButton;
    private JButton clearFieldsButton;
    private JPanel newCharacterPanel;

    final String female = "Female";
    final String male = "Male";
    final String andro = "Androgynous";
    final String unknown = "Unknown";



    public newCharacter(final WikiDB db, final BufferedWriter openBufWriter) throws IOException {
        super("New Character");
        setContentPane(newCharacterPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(500, 500));

        this.wikiDB = db;

        Font font = new Font("Courier", Font.ITALIC,12);

        genderCombo.addItem(female);
        genderCombo.addItem(male);
        genderCombo.addItem(andro);
        genderCombo.addItem(unknown);

        nameText.setFont(font);
        genreText.setFont(font);
        universeText.setFont(font);
        mediaText.setFont(font);
        descriptionText.setFont(font);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new CharacterPage(db, openBufWriter).setVisible(true);
                } catch (IOException io){
                    io.printStackTrace();
                }
            }
        });

        addCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String characterName = nameText.getText();
                String gender = "";
                String genre= genreText.getText();
                String universe = universeText.getText();
                String media = mediaText.getText();
                String description = descriptionText.getText();

                if (characterName.equals("")){
                    JOptionPane.showMessageDialog(null, "Character name is a required field.");
                }

                if (description.equals("")){
                    JOptionPane.showMessageDialog(null, "Description is a required field.");
                }

                if (genderCombo.getSelectedItem().equals(female)){
                    gender = "Female";
                } else if (genderCombo.getSelectedItem().equals(male)){
                    gender = "Male";
                } else if (genderCombo.getSelectedItem().equals(andro)){
                    gender = "Androgynous";
                } else if (genderCombo.getSelectedItem().equals(unknown)){
                    gender = "Unknown";
                }

                if (characterName != null && description != null){
                    //insert character into database

                    wikiDB.insertCharacter(characterName, gender, genre, universe,
                            media, description);

                    int characterID = wikiDB.getCharacterID(characterName);

                    int genreID = wikiDB.getGenreID(genre);
                    int universeID = wikiDB.getUniverseID(universe);
                    int mediaID = wikiDB.getMediaID(media);

                    Character character = new Character(characterID,characterName, gender, genreID, universeID,
                            mediaID, description);

                    addCharactersToFile(character, openBufWriter);

                    JOptionPane.showMessageDialog(null, "Character added.");

                    try {
                        setVisible(false);
                        new CharacterPage(db, openBufWriter).setVisible(true);
                        openBufWriter.close();
                    } catch (IOException io){
                        io.printStackTrace();
                    }
                }


            }
        });

        nameText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                nameText.setText("");

                Font userInput = new Font("Courier", Font.BOLD, 12);

                nameText.setFont(userInput);

            }
        });

        genreText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                genreText.setText("");

                Font userInput = new Font("Courier", Font.BOLD, 12);

                genreText.setFont(userInput);

            }
        });

        universeText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                universeText.setText("");

                Font userInput = new Font("Courier", Font.BOLD,12);

                universeText.setFont(userInput);
            }
        });

        mediaText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                mediaText.setText("");

                Font userInput = new Font("Courier", Font.BOLD, 12);

                mediaText.setFont(userInput);
            }
        });

        descriptionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                descriptionText.setText("");

                Font userInput = new Font("Courier", Font.BOLD,12);

                descriptionText.setFont(userInput);


            }
        });
    }

    private void addCharactersToFile(Character character, BufferedWriter openBufWriter) {
        try{
            //Strings must be inserted into the file, and read from the file, because if the
            //int of the genre, media, or universe doesn't exist, the whole character will be
            //rejected. If the String of the genre, media or universe doesn't exist, the
            //database will insert it automatically.

            String genreName = wikiDB.getGenreName(character.getGenreID());
            String universeName = wikiDB.getUniverseName(character.getUniverseID());
            String mediaTitle= wikiDB.getMediaTile(character.getMediaID());

            openBufWriter.write(character.getCharacterName() + " = ");
            openBufWriter.write(character.getGender() + " = ");
            openBufWriter.write(genreName + " = ");
            openBufWriter.write(universeName + " = ");
            openBufWriter.write(mediaTitle + " = ");
            openBufWriter.write(character.getDescription() + " = ");
            openBufWriter.newLine();

        } catch (IOException io){
            io.printStackTrace();
        }
    }
}
