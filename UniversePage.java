package Marty.company;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marty.farley on 5/11/2015.
 */
public class UniversePage extends JFrame {
    private WikiDB wikiDB;

    private JPanel universePanel;
    private JTextField searchText;
    private JComboBox searchDropList;
    private JButton searchButton;
    private JLabel universeNameLabel;
    private JTree universeTree;
    private JScrollPane universeScroll;
    private JList universeList;
    private JButton viewCharacterButton;
    private JButton quitButton;

    final String character = "Character";
    final String genre = "Genre";
    final String universe = "Universe";
    final String mediaTitle = "Title of Series";

    DefaultListModel<Character> universeListModel;

    public UniversePage (final WikiDB db) throws IOException{
        super("Universe Search");
        setContentPane(universePanel);
        pack();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(500,500));

        this.wikiDB = db;

        searchDropList.addItem(character);
        searchDropList.addItem(mediaTitle);
        searchDropList.addItem(genre);
        searchDropList.addItem(universe);

        universeListModel = new DefaultListModel<Character>();
        universeList.setModel(universeListModel);
        universeList.setSelectionMode((ListSelectionModel.SINGLE_SELECTION));

        universeNameLabel.setText("");

        searchDropList.setSelectedItem(universe);
        searchDropList.requestFocus();

        universeTree.setModel(null);
        universeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        viewCharacterButton.setVisible(false);

        Font font = new Font("Courier", Font.ITALIC,12);

        searchText.setFont(font);

        searchText.setText("Search for a universe, EX Marvel or Disney.");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();

                ArrayList<Integer> universeCharacterIDs = wikiDB.searchUniverse(searchString);
                HashMap<String, String> hash = new HashMap<String, String>();
                HashMap<String, String> allHash = new HashMap<String, String>();
                HashMap<String, String> mediaHash = new HashMap<String, String>();
                String genreText = "";


                for (int i = 0; i < universeCharacterIDs.size(); i++) {
                    int characterID = universeCharacterIDs.get(i);

                    Character universeCharacter = wikiDB.returnCharacter(characterID);

                    int genreIDInt = universeCharacter.getGenreID();
                    genreText = wikiDB.getGenreName(genreIDInt);

                    int universeIDInt = universeCharacter.getUniverseID();
                    String universeText = wikiDB.getUniverseName(universeIDInt);

                    int mediaIDInt = universeCharacter.getMediaID();
                    String mediaText = wikiDB.getMediaTile(mediaIDInt);

                    UniversePage.this.universeListModel.addElement(universeCharacter);

                    hash.put(mediaText, universeText);
                    allHash.put(universeText, genreText);
                    mediaHash.put(universeText, mediaText);

                }

                designAUniverseTree(genreText,hash, allHash, mediaHash);


            }
        });

        universeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                viewCharacterButton.setVisible(true);
            }
        });

        viewCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Marty.company.Character selectedCharacter = (Character) UniversePage.this.universeList.getSelectedValue();

                try{
                    setVisible(false);
                    CharacterPage characterPage = new CharacterPage(wikiDB);
                    characterPage.showCharacter(selectedCharacter);
                    characterPage.setVisible(true);

                } catch (IOException io){
                    io.printStackTrace();
                }


            }
        });

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

    private void designAUniverseTree(String genreName, HashMap hash, HashMap hashAll, HashMap mediaHash) {

        //TODO FIX MEDIA DUPLICATION ISSUE
        DefaultMutableTreeNode universeMainTree = new DefaultMutableTreeNode("Universe");

        DefaultMutableTreeNode genreNode = new DefaultMutableTreeNode(genreName);
        DefaultMutableTreeNode mediaTitleNode = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode universeTitleNode = new DefaultMutableTreeNode("");
        Map<String, DefaultMutableTreeNode> mediaToNode = new HashMap<String, DefaultMutableTreeNode>();
        Map<String, DefaultMutableTreeNode> categoryToNode = new HashMap<String, DefaultMutableTreeNode>();
        Map<String, DefaultMutableTreeNode> genreToNode = new HashMap<String, DefaultMutableTreeNode>();


        ArrayList<Map.Entry<String, String>> copy2 = new ArrayList<Map.Entry<String, String>>();
        copy2.addAll(hashAll.entrySet());

        ArrayList<Map.Entry<String, String>> copy = new ArrayList<Map.Entry<String, String>>();
        copy.addAll(hash.entrySet());

        ArrayList<Map.Entry<String, String>> copy3 = new ArrayList<Map.Entry<String, String>>();
        copy3.addAll(mediaHash.entrySet());

        for (Map.Entry<String, String> e : copy){
            universeTitleNode = categoryToNode.get(e.getValue());
            if (universeTitleNode == null){
                universeTitleNode = new DefaultMutableTreeNode(e.getValue());
                categoryToNode.put(e.getValue(), universeTitleNode);
            }
            universeMainTree.add(universeTitleNode);
            mediaTitleNode = new DefaultMutableTreeNode(e.getKey());
            universeTitleNode.add(mediaTitleNode);


        }



        universeTree.setModel(new DefaultTreeModel(universeMainTree));
    }
}
