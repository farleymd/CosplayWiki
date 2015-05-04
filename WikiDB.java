package Marty.company;

import java.lang.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Date;

/**
 * Created by marty.farley on 5/2/2015.
 */
public class WikiDB {
    private static String protocol="jdbc:derby:";
    private static String dbName = "wikiDB";

    private static final String USER = "username";
    private static final String PASS = "password";

    Statement statement = null;
    Connection conn = null;
    //ResultSet resultSet = null;
    ResultSet secondResultSet = null; //used when an insert/update requires two searches

    PreparedStatement psInsert = null;
    LinkedList<Statement> allStatements = new LinkedList<Statement>();

    boolean dbCreated = false;  //boolean to determine if this is the first time database has been accessed

    public boolean isDbCreated() {
        return dbCreated;
    }


    public void createDB(){
        try {
            conn = DriverManager.getConnection(protocol + dbName +";create=true",USER,PASS);
            statement = conn.createStatement();
            allStatements.add(statement);

            String createCharTableSQL = "CREATE TABLE CosplayCharacter (" +
                    "CharacterID int NOT NULL primary key GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "name varchar(60) not null," +
                    "gender varchar(10)," +
                    "genreID int, " +
                    "universeID int, " +
                    "mediaID int, " +
                    "description varchar(60))";

            String createGenreTableSQL = "CREATE TABLE Genre (GenreID INTEGER NOT NULL GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), genreName varchar(60))";

            String createUniverseTableSQL = "CREATE TABLE Universe (UniverseID int NOT NULL GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), universeName varchar(60))";

            String createMediaTitleTableSQL = "CREATE TABLE Media (MediaID int NOT NULL GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), mediaTitle varchar(60)," +
                    "genreID int," +
                    "universeID int," +
                    "createdBy varchar(60)," +
                    "yearReleased int," +
                    "description varchar(60))";

            String createImageTableSQL = "CREATE TABLE Images (ImageID int NOT NULL GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), author varchar(60) not null," +
                    "dateUploaded date, location varchar(60), characterID int, url varchar(60)," +
                    "tutorialID int)";

            String createTutorialTableSQL = "CREATE TABLE Tutorials (TutorialID int NOT NULL GENERATED ALWAYS " +
                    "AS IDENTITY (START WITH 1, INCREMENT BY 1), author varchar(60) not null," +
                    "dateCreated date, type varchar(60), characterID int, url varchar(60)," +
                    "imageID int)";

            try {
                statement.executeUpdate(createCharTableSQL);

                //Character table print out confirmation
                System.out.println("Character table created");

                statement.executeUpdate(createGenreTableSQL);

                //Genre table print out confirmation
                System.out.println("Genre table created");

                statement.executeUpdate(createUniverseTableSQL);

                //Universe table print out confirmation
                System.out.println("Universe table created");

                statement.executeUpdate(createMediaTitleTableSQL);

                //Media title table print out confirmation
                System.out.println("Media table created");

                statement.executeUpdate(createImageTableSQL);

                //Image table print out confirmation
                System.out.println("Image table created");

                statement.executeUpdate(createTutorialTableSQL);

                //Tutorial table print out confirmation
                System.out.println("Tutorial table created");

            } catch (SQLException se){
                se.printStackTrace();
            }


        } catch (SQLException se){
            se.printStackTrace();
        }

        dbCreated = true;

    }

    public void fillDB(){
        ResultSet resultSet = null;

        String prepGenreInsert = "INSERT INTO Genre(genreName) VALUES (?)";
        String prepUniverseInsert = "INSERT INTO Universe(universeName) VALUES (?)";
        String prepMediaInsert = "INSERT INTO Media(mediaTitle) VALUES (?)";

        try {
            psInsert = conn.prepareStatement(prepGenreInsert);
            allStatements.add(psInsert);

            //Set values in the genre table
            psInsert.setString(1,"Television");
            psInsert.execute();
            psInsert.setString(1,"Movies");
            psInsert.execute();
            psInsert.setString(1,"VideoGames");
            psInsert.execute();
            psInsert.setString(1,"Anime");
            psInsert.execute();
            psInsert.setString(1,"Comic Books");
            psInsert.execute();
            psInsert.setString(1,"Literature");
            psInsert.execute();
            psInsert.setString(1,"Historic");
            psInsert.execute();
            psInsert.setString(1,"Other");
            psInsert.execute();

            //Set values in the Universe table
            psInsert = conn.prepareStatement(prepUniverseInsert);
            allStatements.add(psInsert);

            psInsert.setString(1,"Hunger Games");
            psInsert.execute();
            psInsert.setString(1,"Marvel");
            psInsert.execute();
            psInsert.setString(1,"Sailor Moon");
            psInsert.execute();
            psInsert.setString(1,"Game of Thrones");
            psInsert.execute();
            psInsert.setString(1,"Disney");
            psInsert.execute();
            psInsert.setString(1,"Other");
            psInsert.execute();

            psInsert = conn.prepareStatement(prepMediaInsert);
            allStatements.add(psInsert);

            //Set values in the Media table
            psInsert.setString(1,"Frozen");
            psInsert.execute();
            psInsert.setString(1,"The Avengers");
            psInsert.execute();
            psInsert.setString(1,"Buffy the Vampire Slayer");
            psInsert.execute();
            psInsert.setString(1,"Unknown");
            psInsert.execute();


            String fetchAllDataSQL = "SELECT * from Media";

            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()) {
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = resultSet.getString("mediaTitle");

                System.out.println("MediaID:" + mediaID + " Name: " + mediaTitle);
            }


        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public void insertCharacter(String characterName, String gender,  String genre,
                                String universe, String mediaTitle, String description){
        ResultSet resultSet = null;
        int genreID = getGenreID(genre);
        int universeID = getUniverseID(universe);
        int mediaID = getMediaID(mediaTitle);


        String prepCharacterInsert = "INSERT INTO CosplayCharacter(name, gender, genreID," +
                "universeID, mediaID, description) VALUES (?,?,?,?,?,?)";

        try {
            psInsert = conn.prepareStatement(prepCharacterInsert);
            allStatements.add(psInsert);

            psInsert.setString(1,characterName);

            psInsert.setString(2,gender);

            psInsert.setInt(3,genreID);

            psInsert.setInt(4,universeID);
            //TODO if universe specified by user doesn't exist, add it

            psInsert.setInt(5, mediaID);
            //TODO if mediaTitle specified by user doesn't exist, add it

            psInsert.setString(6,description);
            psInsert.executeUpdate();

            String fetchAllDataSQL = "SELECT * from CosplayCharacter";

            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int universeCharID = resultSet.getInt("universeID");
                int mediaCharID = resultSet.getInt("mediaID");

                System.out.println("Character Name : " + name +
                        " UniverseID : " + universeCharID +
                        " MediaID:" + mediaCharID);
            }


        } catch (SQLException se){
            se.printStackTrace();
        }

    }

    public ArrayList searchCharacter(String characterName){
        ResultSet resultSet = null;
        ArrayList<Character> characterDetails = new ArrayList<Character>();


        //TODO CHANGE = TO LIKE
        String fetchAllDataSQL = "SELECT * from CosplayCharacter where name = '" + characterName +"'";
        String universeName = "";


        try{
            resultSet = statement.executeQuery(fetchAllDataSQL);
            while (resultSet.next()) {
                int characterID = resultSet.getInt("characterID");
                String gender = resultSet.getString("gender");
                int genreID = resultSet.getInt("genreID");
                String genreName = getGenreName(genreID);
                    int universeID = resultSet.getInt(5);
                    universeName = getUniverseName(universeID);
                int mediaID = resultSet.getInt("mediaID");
                String mediaTitle = getMediaTile(mediaID);
                String description = resultSet.getString("description");

                characterDetails.add(new Character(characterID, characterName, gender, genreID,
                        universeID, mediaID, description));


                System.out.println("Character Name:" + characterName + " Gender: " + gender +
                " Genre: " + genreName + " Universe: " + universeName + "Title of Series: " +
                mediaTitle + " Character Description: " + description);
            }

        } catch (SQLException se){
            se.printStackTrace();
        }

        return characterDetails;

    }

    public int getGenreID(String genre){
        ResultSet resultSet = null;
        int genreID = 0;

        try{
            String searchGenre = "select * from Genre where genreName = '" + genre + "'";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                genreID = resultSet.getInt("genreID");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                String otherSearch ="select * from Genre where genreName = 'Other'";
                PreparedStatement otherDecision = conn.prepareStatement(otherSearch);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    genreID = secondResultSet.getInt("genreID");
                }

            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        return genreID;
    }

    public String getGenreName(int genreID){
        ResultSet resultSet = null;
        String genreName = "";

        try{
            String searchGenre = "select * from Genre where genreID = " + genreID + "" ;
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                genreName = resultSet.getString("genreName");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                genreName = "Other";

            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        return genreName;
    }


    public int getUniverseID(String universe){
        ResultSet resultSet = null;
        int universeID = 0;

        try{
            String searchGenre = "select * from Universe where universeName = '" + universe + "'";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                universeID = resultSet.getInt("universeID");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                String otherSearch ="select * from Universe where universeName = 'Other'";
                PreparedStatement otherDecision = conn.prepareStatement(otherSearch);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    universeID = secondResultSet.getInt("universeID");
                }
            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        return universeID;
    }

    public String getUniverseName(int universeID){
        ResultSet resultSet = null;
        String universeName = "";

        try{
            String searchGenre = "select * from Universe where universeID = " + universeID + "";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the universe name based on the ID
                universeName = resultSet.getString("universeName");
            } else{
                //if that universe doesn't exist, set the universe to Other
                universeName = "Unknown";

            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        return universeName;

    }

    public int getMediaID(String mediaTitle){
        ResultSet resultSet = null;
        int mediaID = 0;

        try{
            String searchGenre = "select * from Media where mediaTitle = '" + mediaTitle + "'";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the genreID of the genre entered by the user for this character
                mediaID = resultSet.getInt("mediaID");
            } else{
                //if that genre doesn't exist, set the genre to Other's genreID
                String otherSearch ="select * from Media where mediaTitle = 'Unknown'";
                PreparedStatement otherDecision = conn.prepareStatement(otherSearch);
                secondResultSet = otherDecision.executeQuery();

                if (secondResultSet.next()){
                    mediaID = secondResultSet.getInt("mediaID");
                }
            }
        } catch (SQLException se){
            se.printStackTrace();
        }


        return mediaID;
    }

    public String getMediaTile(int mediaID){
        ResultSet resultSet = null;
        String mediaTitle = "";

        try{
            String searchGenre = "select * from Media where mediaID = " + mediaID + "";
            PreparedStatement recordSearch = conn.prepareStatement(searchGenre);
            resultSet = recordSearch.executeQuery();

            if (resultSet.next()) {
                //get the universe name based on the ID
                mediaTitle = resultSet.getString("mediaTitle");
            } else{
                //if that universe doesn't exist, set the universe to Other
                mediaTitle = "Unknown";

            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        return mediaTitle;
    }

    public void deleteDB(){
        String deleteCharSQL = "DROP TABLE CosplayCharacter";
        String deleteGenreSQL = "DROP TABLE Genre";
        String deleteImagesSQL = "DROP TABLE Images";
        String deleteUniverseSQL = "DROP TABLE Universe";
        String deleteTutorialsSQL = "DROP TABLE Tutorials";
        try {
            statement.executeUpdate(deleteCharSQL);
            statement.executeUpdate(deleteGenreSQL);
            statement.executeUpdate(deleteImagesSQL);
            statement.executeUpdate(deleteUniverseSQL);
            statement.executeUpdate(deleteTutorialsSQL);
        } catch (SQLException se){
            se.printStackTrace();
        }

    }
}
