package Marty.company;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by marty.farley on 5/3/2015.
 */
public class CharacterPage extends JFrame {
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

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";

    public CharacterPage() throws IOException {
        super("Character Page");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(300,500));

        searchDropList.addItem(character);
        searchDropList.addItem(genre);
        searchDropList.addItem(universe);

        genreLabel.setLocation(1,3);
        genreName.setLocation(1,3);


    }
}
