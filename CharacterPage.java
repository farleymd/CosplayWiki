package Marty.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
    private JPanel imagesPanel;
    private JLabel genderName;

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";

    public CharacterPage(WikiDB db) throws IOException {
        super("Character Page");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(300,500));

        this.wikiDB = db;

        searchDropList.addItem(character);
        searchDropList.addItem(genre);
        searchDropList.addItem(universe);

        genreLabel.setLocation(1,3);
        genreName.setLocation(1,3);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();

                if (searchDropList.getSelectedItem().equals(character)){

                    //can use an arrayList to retrieve details because number of columns
                    //will always be the same
                    ArrayList<Character> characterDetails = wikiDB.searchCharacter(searchString);


                }
            }
        });

    }


}
