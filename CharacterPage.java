package Marty.company;

import sun.plugin2.ipc.windows.WindowsEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by marty.farley on 5/3/2015.
 */
public class CharacterPage extends JFrame {
    private WikiDB wikiDB;
    private newCharacter newCharacter;
    private CharacterPage characterPage;

    private JPanel rootPanel;
    private JButton searchButton;
    private JComboBox searchDropList;
    private JTextField searchText;
    private JLabel characterName;
    private JLabel genreName;
    private JLabel genreLabel;
    private JLabel universeLabel;
    private JLabel universeName;
    private JLabel titleLabel;
    private JLabel titleName;
    private JTextPane characterDescription;
    private JLabel imageLabel;
    private JPanel imagesPanel;
    private JLabel genderName;
    private JButton imageAdd;
    private JButton quitButton;
    private JButton editCharacterButton;

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";
    final String mediaTitle = "Title of Series";

    public CharacterPage(WikiDB db) throws IOException {
        super("Character Page");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(500,500));
        imagesPanel.setLayout(new BorderLayout());

        this.wikiDB = db;


        searchDropList.addItem(character);
        searchDropList.addItem(mediaTitle);
        searchDropList.addItem(genre);
        searchDropList.addItem(universe);

        genreLabel.setLocation(1,3);
        genreName.setLocation(1,3);

       genderName.setText("");
        universeName.setText("");
        titleName.setText("");
        genreName.setText("");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();
                int characterID = 0;
                String characterNameText = "";
                String genderText = "";
                int genreIDInt = 0;
                String genreText="";
                int universeIDInt = 0;
                String universeText = "";
                int mediaIDInt = 0;
                String mediaText = "";
                String descriptionText = "";


                if (searchDropList.getSelectedItem().equals(character)){

                    //can use an arrayList to retrieve details because number of columns
                    //will always be the same
                    ArrayList<Character> characterDetails = wikiDB.searchCharacter(searchString);

                    if (characterDetails.size() ==0){

                        int reply = JOptionPane.showConfirmDialog(null, "That character cannot be found." +
                                "Would you like to add them?", "Title", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            try {
                                setVisible(false);
                                new newCharacter(wikiDB).setVisible(true);
                            } catch (IOException io){
                                io.printStackTrace();
                            }

                        }
                        else {
                            JOptionPane.showMessageDialog(null, "GOODBYE");
                        }
                    }

                    for (int i = 0; i < characterDetails.size(); i++){
                        characterID = characterDetails.get(i).getCharacterID();

                        characterNameText =characterDetails.get(i).getCharacterName();
                        genderText = characterDetails.get(i).getGender();

                        genreIDInt = characterDetails.get(i).getGenreID();
                        genreText = wikiDB.getGenreName(genreIDInt);

                        universeIDInt = characterDetails.get(i).getUniverseID();
                        universeText = wikiDB.getUniverseName(universeIDInt);

                        mediaIDInt = characterDetails.get(i).getMediaID();
                        mediaText = wikiDB.getMediaTile(mediaIDInt);

                        descriptionText = characterDetails.get(i).getDescription();

                    }

                    characterName.setText(characterNameText);
                    genderName.setText(genderText);
                    genreName.setText(genreText);
                    universeName.setText(universeText);
                    titleName.setText(mediaText);
                    characterDescription.setText(descriptionText);

                    ArrayList<String> characterImages = wikiDB.searchImages(characterID);

                    for (int x = 0; x < characterImages.size(); x++){
                        String imageURL = characterImages.get(x);
                        Graphics g = getGraphics();


                       try {
                           URL url = new URL(imageURL);

                           //icon.paintIcon(imagesPanel,g,300,100);


                           JLabel wIcon = new JLabel(new ImageIcon(url));

                           imagesPanel.setVisible(true);
                           imagesPanel.add(wIcon);



                       } catch (MalformedURLException mue){
                           mue.printStackTrace();
                       }





                    }


                }
            }
        });



        imageAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //use image's url to display the picture

                JPanel myPanel = new JPanel();
                JTextField author = new JTextField(5);
                JTextField dateUploaded = new JTextField(10);
                JTextField imageURL = new JTextField(15);

                myPanel.add(new JLabel("Author: "));
                myPanel.add(author);

                myPanel.add(new JLabel("Date Uploaded"));
                myPanel.add(dateUploaded);

                myPanel.add(new JLabel("Image URL:"));
                myPanel.add(imageURL);
                myPanel.add(Box.createHorizontalStrut(15));

                JOptionPane.showConfirmDialog(null, myPanel, "Please enter x and Y value",
                        JOptionPane.OK_CANCEL_OPTION);



                String characterNameText = characterName.getText();
                int characterID = 0;

                ArrayList<Character> characterDetails = wikiDB.searchCharacter(characterNameText);

                for (int i = 0; i < characterDetails.size(); i++){
                    characterID = characterDetails.get(i).getCharacterID();
                }

            }
        });

        searchDropList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = new Font("Courier", Font.ITALIC,12);

                searchText.setFont(font);

                if (searchDropList.getSelectedItem().equals(character)){
                    searchText.setText("Search for a character by name.");
                } else if (searchDropList.getSelectedItem().equals(genre)){
                    searchText.setText("Search for genre, EX television, movies, comic.");
                } else if (searchDropList.getSelectedItem().equals(universe)){
                    searchText.setText("Search for universe, EX Marvel, Harry Potter.");
                } else if (searchDropList.getSelectedItem().equals(mediaTitle)){
                    searchText.setText("Search for title of series, EX The Avengers.");
                }
            }
        });


        searchText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                searchText.setText("");

                Font userInput = new Font("Courier", Font.BOLD,12);

                searchText.setFont(userInput);


            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wikiDB.deleteDB();
                wikiDB.closeDB();
                System.exit(0);
            }
        });
    }

    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        if (path != null) {
            return new ImageIcon(path, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


}
