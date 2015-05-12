package Marty.company;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.*;

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
    private JLabel genreNameLabel;
    private JTree genreTree;
    private JButton viewCharacterButton;

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

        genreNameLabel.setText("");

        searchDropList.setSelectedItem(genre);
        searchDropList.requestFocus();

        genreTree.setModel(null);
        genreTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        viewCharacterButton.setVisible(false);

        Font font = new Font("Courier", Font.ITALIC,12);

        searchText.setFont(font);

        searchText.setText("Search for a genre, EX television, movies, comics.");


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();

                ArrayList<Integer> genreCharacterIDs = wikiDB.searchGenre(searchString);
                HashMap<String, String> hash = new HashMap<String, String>();
                HashMap<String, String> allHash = new HashMap<String, String>();
                HashMap<String, String> mediaHash = new HashMap<String, String>();
                String genreText = "";


                for (int i = 0; i < genreCharacterIDs.size(); i++) {
                    int characterID = genreCharacterIDs.get(i);

                    Character genreCharacter = wikiDB.returnCharacter(characterID);

                        int genreIDInt = genreCharacter.getGenreID();
                        genreText = wikiDB.getGenreName(genreIDInt);
                       
                        int universeIDInt = genreCharacter.getUniverseID();
                        String universeText = wikiDB.getUniverseName(universeIDInt);

                        int mediaIDInt = genreCharacter.getMediaID();
                        String mediaText = wikiDB.getMediaTile(mediaIDInt);

                        GenrePage.this.characterListModel.addElement(genreCharacter);

                    hash.put(mediaText, universeText);
                    allHash.put(universeText, genreText);
                    mediaHash.put(universeText, mediaText);

                        }

                designAUniverseTree(genreText,hash, allHash, mediaHash);

                    }

        });

        genreTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) genreTree.getLastSelectedPathComponent();
                if (node==null) {
                    return;
                }

                String nodeName = node.toString();
                int nodeChildCount = node.getChildCount();

                if (nodeChildCount == 0){
                    displayMedia(nodeName);
                } else {
                    displayUniverse(nodeName);
                }
            }
        });



        genreList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                viewCharacterButton.setVisible(true);
            }
        });

        viewCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Character selectedCharacter = GenrePage.this.genreList.getSelectedValue();
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
        DefaultMutableTreeNode genreMainTree = new DefaultMutableTreeNode("Genre");

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


        for (Map.Entry<String, String> u : copy2) {
            genreNode = genreToNode.get(u.getValue());
            if (genreNode == null) {
                genreNode = new DefaultMutableTreeNode(u.getValue());
                genreToNode.put(u.getValue(), genreNode);
                genreMainTree.add(genreNode);
            }
            universeTitleNode = new DefaultMutableTreeNode(u.getKey());
            categoryToNode.put(u.getKey(), universeTitleNode);
            genreNode.add(universeTitleNode);

            for (Map.Entry<String, String> e : copy) {
                universeTitleNode = categoryToNode.get(e.getValue());
                if (universeTitleNode == null) {
                    universeTitleNode = new DefaultMutableTreeNode(e.getValue());
                    categoryToNode.put(e.getValue(), universeTitleNode);
                }
                mediaTitleNode = new DefaultMutableTreeNode(e.getKey());
                mediaToNode.put(e.getKey(), mediaTitleNode);
                universeTitleNode.add(mediaTitleNode);

                for (Map.Entry<String, String> m : copy3) {
                    mediaTitleNode = mediaToNode.get(m.getValue());
                    if (mediaTitleNode == null){
                        mediaTitleNode = new DefaultMutableTreeNode(m.getValue());
                        mediaToNode.put(m.getValue(), mediaTitleNode);
                        universeTitleNode.add(mediaTitleNode);
                    }
                }
            }

        }
        genreTree.setModel(new DefaultTreeModel(genreMainTree));
    }

    private void displayUniverse(String universeName) {
        ArrayList<Integer> universeCharacterIDs = wikiDB.searchUniverse(universeName);

        GenrePage.this.characterListModel.removeAllElements();

        for (int i = 0; i < universeCharacterIDs.size(); i++) {
            int characterID = universeCharacterIDs.get(i);

            Character genreCharacter = wikiDB.returnCharacter(characterID);

            GenrePage.this.characterListModel.addElement(genreCharacter);

        }
    }

    private void displayMedia (String mediaName) {
        ArrayList<Integer> mediaCharacterIDs = wikiDB.searchMediaTitle(mediaName);

        GenrePage.this.characterListModel.removeAllElements();

        for (int i = 0; i < mediaCharacterIDs.size(); i++) {
            int characterID = mediaCharacterIDs.get(i);

            Character genreCharacter = wikiDB.returnCharacter(characterID);

            GenrePage.this.characterListModel.addElement(genreCharacter);
        }
    }
}
