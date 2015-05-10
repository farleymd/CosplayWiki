package Marty.company;

import sun.plugin2.ipc.windows.WindowsEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by marty.farley on 5/3/2015.
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

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";
    final String mediaTitle = "Title of Series";

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
                        characterNameText = characterNameText.toUpperCase();

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

                    BufferedImage img1 = null;
                    JPanel imagesPanel = new JPanel();
                    imagesPanel.setLayout(new GridBagLayout());

                    for (int x = 0; x < characterImages.size(); x++){
                        String imageURL = characterImages.get(x);

                        //TODO MAKE IMAGE METHOD

                        try
                        {
                            URL url1 = new URL(imageURL);

                            URLConnection conn1 = url1.openConnection();
                            conn1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                            InputStream in1 = conn1.getInputStream();

                            img1 = ImageIO.read(in1);
                        } catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                        }


                    }

                    int w;
                    int h;
                    if (img1.getWidth() > img1.getHeight()) {
                        //if the source was wider than it was tall, make the width 100,
                        w = 100;
                        //and scale the height accordingly
                        h = (int) ((double)img1.getHeight() / img1.getWidth() * 100.0);
                    } else {
                        //otherwise, vice versa (and if w == h, then they are both 100)
                        h = 100;
                        w = (int) ((double)img1.getWidth() / img1.getHeight() * 100.0);
                    }
                    BufferedImage img2 = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = img2.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(img1,0,0,w,h,null);
                    g2.dispose();

                    JLabel cIcon = new JLabel(new ImageIcon(img2));


                    rootPanel.add(cIcon,c);
                    rootPanel.repaint();



                } else if (searchDropList.getSelectedItem().equals(genre)){

                }
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 6;

                setContentPane(rootPanel);
                pack();
                setVisible(true);

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

                JOptionPane.showConfirmDialog(null, myPanel, "Please enter following fields",
                        JOptionPane.OK_CANCEL_OPTION);

                String characterNameText = characterName.getText();
                int characterID = 0;

                ArrayList<Character> characterDetails = wikiDB.searchCharacter(characterNameText);

                for (int i = 0; i < characterDetails.size(); i++){
                    characterID = characterDetails.get(i).getCharacterID();
                }

                wikiDB.insertImage(characterID, author.getText(), imageURL.getText());

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

        setContentPane(rootPanel);
        pack();
        setVisible(true);
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
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

    }


}
