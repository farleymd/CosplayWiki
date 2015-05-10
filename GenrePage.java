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

        genreTree.setModel(null);
        genreTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchText.getText();

                ArrayList<Integer> genreCharacterIDs = wikiDB.searchGenre(searchString);
                HashMap<String, String> hash = new HashMap<String, String>();
                String genreText = "";


                for (int i = 0; i < genreCharacterIDs.size(); i++) {
                    int characterID = genreCharacterIDs.get(i);

                    Character genreCharacter = wikiDB.returnCharacter(characterID);

                        int genreIDInt = genreCharacter.getGenreID();
                        genreText = wikiDB.getGenreName(genreIDInt);
                    genreNameLabel.setText(genreText);

                        int universeIDInt = genreCharacter.getUniverseID();
                        String universeText = wikiDB.getUniverseName(universeIDInt);

                        int mediaIDInt = genreCharacter.getMediaID();
                        String mediaText = wikiDB.getMediaTile(mediaIDInt);

                        GenrePage.this.characterListModel.addElement(genreCharacter);

                    hash.put(mediaText, universeText);

                        }

                designAUniverseTree(genreText,hash);

                    }

        });

        genreTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) genreTree.getLastSelectedPathComponent();
                String genreName = genreNameLabel.getText();


                if (node==null) {
                    return;
                }

                String nodeName = node.toString();
                String nodeParent = node.getParent().toString();

                if (nodeParent.equals(genreName)){
                    int universeID = wikiDB.getUniverseID(nodeName);
                    displayUniverse(nodeName);
                } else {
                    int mediaID = wikiDB.getMediaID(nodeName);
                    displayMedia(nodeName);
                }
            }
        });



        genreList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Character selectedCharacter = GenrePage.this.genreList.getSelectedValue();
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

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wikiDB.deleteDB();
                wikiDB.closeDB();
                System.exit(0);
            }
        });

    }


    private void designAUniverseTree(String genreName, HashMap hash){
        DefaultMutableTreeNode genreMainTree = new DefaultMutableTreeNode(genreName);
        DefaultMutableTreeNode mediaTitleNode = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode universeTitleNode = new DefaultMutableTreeNode("");
        Map<String, DefaultMutableTreeNode> categoryToNode = new HashMap<String, DefaultMutableTreeNode>();


        ArrayList<Map.Entry<String,String>> copy = new ArrayList<Map.Entry<String, String>>();
        copy.addAll(hash.entrySet());

        for (Map.Entry<String,String> e : copy){
            universeTitleNode = categoryToNode.get(e.getValue());
            if (universeTitleNode == null ){
                universeTitleNode = new DefaultMutableTreeNode(e.getValue());
                categoryToNode.put(e.getValue(), universeTitleNode);
                genreMainTree.add(universeTitleNode);
            }
            mediaTitleNode = new DefaultMutableTreeNode(e.getKey());
            universeTitleNode.add(mediaTitleNode);
        }

        genreTree.setModel(new DefaultTreeModel(genreMainTree));
    }

    private void displayUniverse(String universeName) {
        ArrayList<Integer> universeCharacterIDs = wikiDB.searchUniverse(universeName);

        GenrePage.this.characterListModel.removeAllElements();

        for (int i = 0; i < universeCharacterIDs.size(); i++) {
            int characterID = universeCharacterIDs.get(i);

            Character genreCharacter = wikiDB.returnCharacter(characterID);

            characterID = genreCharacter.getCharacterID();

            String characterName = genreCharacter.getCharacterName();


            String genderText = genreCharacter.getGender();

            int genreIDInt = genreCharacter.getGenreID();

            int universeIDInt = genreCharacter.getUniverseID();
            String universeText = wikiDB.getUniverseName(universeIDInt);

            int mediaIDInt = genreCharacter.getMediaID();
            String mediaText = wikiDB.getMediaTile(mediaIDInt);

            String descriptionText = genreCharacter.getDescription();


            GenrePage.this.characterListModel.addElement(genreCharacter);

        }
    }

    private void displayMedia (String mediaName) {
        ArrayList<Integer> mediaCharacterIDs = wikiDB.searchMediaTitle(mediaName);

        GenrePage.this.characterListModel.removeAllElements();

        for (int i = 0; i < mediaCharacterIDs.size(); i++) {
            int characterID = mediaCharacterIDs.get(i);

            Character genreCharacter = wikiDB.returnCharacter(characterID);

            characterID = genreCharacter.getCharacterID();

            String characterName = genreCharacter.getCharacterName();


            String genderText = genreCharacter.getGender();

            int genreIDInt = genreCharacter.getGenreID();

            int universeIDInt = genreCharacter.getUniverseID();
            String universeText = wikiDB.getUniverseName(universeIDInt);

            int mediaIDInt = genreCharacter.getMediaID();
            String mediaText = wikiDB.getMediaTile(mediaIDInt);

            String descriptionText = genreCharacter.getDescription();


            GenrePage.this.characterListModel.addElement(genreCharacter);
        }
    }
}
