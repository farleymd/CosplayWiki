package Marty.company;

import sun.plugin2.ipc.windows.WindowsEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by marty.farley on 5/3/2015.
 *
 * This is the main GUI display class. This page controls character searches, character display (data and images),
 * and editing of character. The Images are displayed dynamically in a 4 by X grid display. Images can also be added
 * from this page.
 *
 * Known bugs: if searching for a second character, the first character's images are still present, sometimes
 * overlapping the JPanel display of the second character's images.
 *
 * Known buys: new image doesn't display right after being added.
 *
 * Future enhancements: Split images (adding, display) into a different class. Ability to remove image.
 *  Turn image into clickable URL link to open in browser. Display image in separate JFrame.
 *  Warn user if image is not read by the program.
 *
 *  Future enhancement: Add ability to add/pull tutorials related to that character's cosplay.
 *
 *  References: http://stackoverflow.com/questions/30112958/image-isnt-properly-sizing-in-gridbaglayout,
 *  http://stackoverflow.com/questions/30088682/image-icon-not-displaying-in-jpanel,
 *  http://stackoverflow.com/questions/30174681/trying-to-overwrite-replace-jpanels
 *
 */

public class CharacterPage extends JFrame {
    private WikiDB wikiDB;

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
    private JLabel genderName;
    private JButton quitButton;
    private JButton editCharacterButton;
    private JButton addImagesButton;
    private JLabel genderLabel;
    private JPanel imagesPanel;

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";
    final String mediaTitle = "Title of Series";

    FileWriter openWriter = new FileWriter("addCharacters.txt", true);
    final BufferedWriter openBufWriter = new BufferedWriter(openWriter);

    FileWriter imagesWriter = new FileWriter("addCharacterImages.txt", true);
    final BufferedWriter openImageWriter = new BufferedWriter(imagesWriter);


    public CharacterPage(WikiDB db) throws IOException {
        super("Character Page");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(new Dimension(1000,1000));

        rootPanel.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        rootPanel.setSize(new Dimension(500,500));

        buildUI(c);

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

        Font font = new Font("Courier", Font.ITALIC,12);

        searchText.setFont(font);

        characterName.setText("");

        editCharacterButton.setVisible(false);
        addImagesButton.setVisible(false);

        setContentPane(rootPanel);
        pack();
        setVisible(true);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();
                int characterID = 0;
                String characterNameText = "";
                String genderText = "";
                int genreIDInt = 0;
                String genreText = "";
                int universeIDInt = 0;
                String universeText = "";
                int mediaIDInt = 0;
                String mediaText = "";
                String descriptionText = "";


                if (searchDropList.getSelectedItem().equals(character)) {

                    //can use an arrayList to retrieve details because number of columns
                    //will always be the same
                    ArrayList<Character> characterDetails = wikiDB.searchCharacter(searchString);


                    //If the character doesn't exist, ask the user if they'd like the add the
                    //character to the wiki
                    if (characterDetails.size() == 0) {

                        int reply = JOptionPane.showConfirmDialog(null, "That character cannot be found." +
                                "Would you like to add them?", "New Character?", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            try {
                                setVisible(false);
                                new newCharacter(wikiDB).setVisible(true);
                            } catch (IOException io) {
                                io.printStackTrace();
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "GOODBYE");
                        }
                    } else {

                        editCharacterButton.setVisible(true);
                        addImagesButton.setVisible(true);

                        for (int i = 0; i < characterDetails.size(); i++) {
                            characterID = characterDetails.get(i).getCharacterID();

                            characterNameText = characterDetails.get(i).getCharacterName();
                            characterNameText = characterNameText.toUpperCase();

                            genderText = characterDetails.get(i).getGender();

                            genreIDInt = characterDetails.get(i).getGenreID();
                            genreText = wikiDB.getGenreName(genreIDInt);

                            universeIDInt = characterDetails.get(i).getUniverseID();
                            universeText = wikiDB.getUniverseName(universeIDInt);

                            mediaIDInt = characterDetails.get(i).getMediaID();
                            mediaText = wikiDB.getMediaTile(mediaIDInt);

                            descriptionText = characterDetails.get(i).getDescription();

                            Character genreCharacter = wikiDB.returnCharacter(characterID);

                        }

                        characterName.setText(characterNameText);
                        genderName.setText(genderText);
                        genreName.setText(genreText);
                        universeName.setText(universeText);
                        titleName.setText(mediaText);
                        characterDescription.setText(descriptionText);

                        //TODO FIX DUPLICATING IMAGES ISSUE


                        ArrayList<String> characterImages = wikiDB.searchImages(characterID);

                        JPanel imagesPanel = displayImages(characterImages, c);


                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 0;
                        c.gridy = 6;

                        rootPanel.add(imagesPanel, c);
                        rootPanel.repaint();

                        setContentPane(rootPanel);
                        pack();
                        setVisible(true);
                        //System.out.println("Repainting.");
                    }
                }
            }
        });

        searchDropList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchDropList.getSelectedItem().equals(genre)){
                    try {
                        setVisible(false);
                        new GenrePage(wikiDB).setVisible(true);
                    } catch (IOException io){
                        io.printStackTrace();
                    }
                } else if (searchDropList.getSelectedItem().equals(universe)){
                    try {
                        setVisible(false);
                        new UniversePage(wikiDB).setVisible(true);
                    } catch (IOException io){
                        io.printStackTrace();
                    }
                } else if (searchDropList.getSelectedItem().equals(mediaTitle)){
                    //TODO ADD MEDIA TITLE PAGE
                }
            }
        });



        addImagesButton.addActionListener(new ActionListener() {
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

                int reply = JOptionPane.showConfirmDialog(null, myPanel, "Please enter following fields",
                        JOptionPane.OK_CANCEL_OPTION);

                if (reply == JOptionPane.OK_OPTION){
                    if (!author.getText().equals(null) && !imageURL.getText().equals(null)){
                        String characterNameText = characterName.getText();
                        int characterID = 0;

                        ArrayList<Character> characterDetails = wikiDB.searchCharacter(characterNameText);

                        for (int i = 0; i < characterDetails.size(); i++){
                            characterID = characterDetails.get(i).getCharacterID();
                            characterNameText = characterDetails.get(i).getCharacterName();
                        }

                        wikiDB.insertImage(characterID, author.getText(), imageURL.getText());

                        addImagesToFile(characterNameText, author.getText(), imageURL.getText(), openImageWriter);

                        ArrayList<String> characterImages = wikiDB.searchImages(characterID);

                        //TODO DISPLAY IMAGE AFTER ADDING

                        final GridBagConstraints c = new GridBagConstraints();
                        JPanel imagesPanel = displayImages(characterImages, c);

                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 0;
                        c.gridy = 6;

                        rootPanel.add(imagesPanel, c);
                        rootPanel.repaint();

                        setContentPane(rootPanel);
                        pack();
                        setVisible(true);

                    }
                }
            }
        });


        searchText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                //Reset the font from italic to normal when clicking in the search text
                super.focusGained(e);
                searchText.setText("");

                Font userInput = new Font("Courier", Font.BOLD,12);

                searchText.setFont(userInput);


            }
        });

        editCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();

                ArrayList<Character> characterDetails = wikiDB.searchCharacter(searchString);

                Object[] editOptions = {"Character Name", "Gender", "Genre", "Universe", "Title of Series", "Description"};
                String s = (String)JOptionPane.showInputDialog(null, "Which option would you like to edit?", "Edit Character",
                        JOptionPane.PLAIN_MESSAGE, null, editOptions, "Character Name");

                int characterID = characterDetails.get(0).getCharacterID();

                if ((s != null) && (s.length() > 0)) {
                    if (s.equals("Character Name")){
                        JPanel myPanel = new JPanel();
                        JTextField characterName = new JTextField(15);

                        myPanel.add(new JLabel("New Name: "));
                        myPanel.add(characterName);

                        int reply = JOptionPane.showConfirmDialog(null, myPanel, "Edit character name", JOptionPane.OK_CANCEL_OPTION);

                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterName(characterID, characterName.getText());
                        }


                    } else if (s.equals("Gender")){
                        JPanel genderPanel = new JPanel();
                        JTextField newGender = new JTextField(15);

                        genderPanel.add(new JLabel("New Gender: "));
                        genderPanel.add(newGender);

                        int reply = JOptionPane.showConfirmDialog(null, genderPanel, "Edit gender", JOptionPane.OK_CANCEL_OPTION);

                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterGender(characterID, newGender.getText());
                        }

                    } else if (s.equals("Genre")){
                        JPanel myPanel = new JPanel();
                        JTextField newGenre = new JTextField(15);

                        myPanel.add(new JLabel("New Genre: "));
                        myPanel.add(newGenre);

                        int reply = JOptionPane.showConfirmDialog(null, myPanel, "Edit genre", JOptionPane.OK_CANCEL_OPTION);

                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterGenre(characterID, newGenre.getText());
                        }

                    } else if (s.equals("Universe")){
                        JPanel myPanel = new JPanel();
                        JTextField newUniverse = new JTextField(15);

                        myPanel.add(new JLabel("New Universe: "));
                        myPanel.add(newUniverse);

                        int reply = JOptionPane.showConfirmDialog(null, myPanel, "Edit universe", JOptionPane.OK_CANCEL_OPTION);
                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterUniverse(characterID, newUniverse.getText());
                        }

                    } else if (s.equals("Title of Series")){
                        JPanel myPanel = new JPanel();
                        JTextField newTitle = new JTextField(15);

                        myPanel.add(new JLabel("New Title of Series: "));
                        myPanel.add(newTitle);

                        int reply = JOptionPane.showConfirmDialog(null, myPanel, "Edit title", JOptionPane.OK_CANCEL_OPTION);

                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterMedia(characterID, newTitle.getText());
                        }

                    } else if (s.equals("Description")){
                        JPanel myPanel = new JPanel();
                        JTextField newDesc = new JTextField(15);

                        myPanel.add(new JLabel("New Description: "));
                        myPanel.add(newDesc);

                        int reply = JOptionPane.showConfirmDialog(null, myPanel, "Edit description", JOptionPane.OK_CANCEL_OPTION);
                        if (reply == JOptionPane.OK_OPTION){
                            wikiDB.editCharacterDesc(characterID, newDesc.getText());
                        }
                    }
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    openBufWriter.close();
                    openImageWriter.close();
                } catch (IOException io){

                }

                wikiDB.deleteDB();
                wikiDB.closeDB();

                System.exit(0);
            }
        });


    }


    protected void buildUI(GridBagConstraints c){
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        rootPanel.add(searchText,c);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 2;
        c.gridy = 0;
        rootPanel.add(searchButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        rootPanel.add(searchDropList,c);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 1;
        rootPanel.add(characterName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        rootPanel.add(genderLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        rootPanel.add(genderName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        rootPanel.add(genreLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 2;
        rootPanel.add(genreName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        rootPanel.add(universeLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 3;
        rootPanel.add(universeName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        rootPanel.add(titleLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 4;
        rootPanel.add(titleName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        rootPanel.add(characterDescription,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        rootPanel.add(imageLabel,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 5;
        rootPanel.add(editCharacterButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 6;
        rootPanel.add(addImagesButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 6;
        rootPanel.add(quitButton,c);



    }

    public void showCharacter(Character character){
        //Method used by the Genre, Universe and Media GUI classes to show
        //selected character in the CharacterPage GUI

        int characterID = character.getCharacterID();

        String characterNameText =character.getCharacterName();

        String genderText = character.getGender();

        int genreIDInt = character.getGenreID();
        String genreText = wikiDB.getGenreName(genreIDInt);

        int universeIDInt = character.getUniverseID();
        String universeText = wikiDB.getUniverseName(universeIDInt);

        int mediaIDInt = character.getMediaID();
        String mediaText = wikiDB.getMediaTile(mediaIDInt);

        String descriptionText = character.getDescription();


    characterName.setText(characterNameText);
    genderName.setText(genderText);
    genreName.setText(genreText);
    universeName.setText(universeText);
    titleName.setText(mediaText);
    characterDescription.setText(descriptionText);

        ArrayList<String> characterImages = wikiDB.searchImages(characterID);

        final GridBagConstraints c = new GridBagConstraints();
        JPanel imagesPanel = displayImages(characterImages, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;

        rootPanel.add(imagesPanel, c);
        rootPanel.repaint();

        setContentPane(rootPanel);
        pack();
        setVisible(true);


    }

    private JPanel displayImages(ArrayList<String> characterImages, GridBagConstraints c){
        //Method used by Genre, Universe and Media GUI classes when displaying a
        //selected character.
        BufferedImage img1 = null;
        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridBagLayout());

        for (int x = 0; x < characterImages.size(); x++) {

            String imageURL = characterImages.get(x);

            try {
                URL url1 = new URL(imageURL);

                URLConnection conn1 = url1.openConnection();
                conn1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                InputStream in1 = conn1.getInputStream();

                img1 = ImageIO.read(in1);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }


            if (img1 != null) {

                //target size is no bigger than 100 x 100
                int w;
                int h;
                if (img1.getWidth() > img1.getHeight()) {
                    //if the source was wider than it was tall, make the width 100,
                    w = 100;
                    //and scale the height accordingly
                    h = (int) ((double) img1.getHeight() / img1.getWidth() * 100.0);
                } else {
                    //otherwise, vice versa (and if w == h, then they are both 100)
                    h = 100;
                    int myH = img1.getHeight();
                    int myW = img1.getWidth();
                    w = (int) ((double) img1.getWidth() / img1.getHeight() * 100.0);

                    w= w*3;
                    h = h*3;
                }

                BufferedImage img2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img2.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(img1, 0, 0, w, h, null);
                g2.dispose();

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = x%4;
                c.gridy = x/4;

                Border b1 = new BevelBorder(
                        BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.DARK_GRAY);
                Border b2 = new LineBorder(Color.GRAY, 12);
                Border bTemp = new CompoundBorder(b1,b2);


                JLabel cIcon = new JLabel(new ImageIcon(img2));

                cIcon.setBorder(bTemp);
                imagesPanel.add(cIcon, c);

            }
        }

        return imagesPanel;

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

    private void addImagesToFile(String characterName, String author, String url, BufferedWriter openImageWriter){
        try{
            //Character name must be used, even though characterID is used to store the data in the table,
            //because characterID is potentially reassigned from the txt file. Character name is
            //easier to link as name is the same.
            //Txt containing character must be loaded before image txt.

            openImageWriter.write(characterName + " = ");
            openImageWriter.write(author + " = ");
            openImageWriter.write(url + " = ");
            openImageWriter.newLine();

        } catch (IOException io){
            io.printStackTrace();
        }

    }

}
