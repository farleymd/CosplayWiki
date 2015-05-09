package Marty.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by marty.farley on 5/8/2015.
 */
public class GenrePage extends JFrame {
    private WikiDB wikiDB;

    private JTextField searchText;
    private JComboBox searchDropList;
    private JButton searchButton;
    private JButton quitButton;
    private JPanel genrePanel;
    private JScrollPane genreScroll;
    private JList <Character> genreList;

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";
    final String mediaTitle = "Title of Series";

    DefaultListModel<Character> characterListModel;

    public GenrePage(final WikiDB db) throws IOException {
        super("Genre Search");
        setContentPane(genrePanel);
        pack();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(500,500));

        this.wikiDB = db;

        searchDropList.addItem(character);
        searchDropList.addItem(mediaTitle);
        searchDropList.addItem(genre);
        searchDropList.addItem(universe);

        characterListModel = new DefaultListModel<Character>();
        genreList.setModel(characterListModel);
        genreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        searchDropList.setSelectedItem(genre);



        searchDropList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchDropList.getSelectedItem().equals(character)){
                    try {
                        setVisible(false);
                        new CharacterPage(wikiDB).setVisible(true);
                    } catch (IOException io){
                        io.printStackTrace();
                    }
                }
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
}
