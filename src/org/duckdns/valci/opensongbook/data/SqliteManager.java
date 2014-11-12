package org.duckdns.valci.opensongbook.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteManager {

    // SELECT title FROM songs WHERE lyrics MATCH 'Isu*'
    static String dbPath = "test_data//opensongbookdb.sqlite";

    static String normalizeString(String input) {
        return input.replaceAll("'", "''");
    }

    public static void importSongToDB(SongClass song) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate
            statement.executeUpdate("insert into songs values(null,'" + normalizeString(song.getTitle()) + "','"
                    + normalizeString(song.getLyrics()) + "','" + normalizeString(song.getAuthor()) + "','" + "unknown"
                    + "'," + "datetime(), 0)");

            System.out.println("song : " + song.getTitle() + " succesfully imported");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    public static void testDatabaseTriggerInsert(String songTitle, String songLyrics, String songAuthor,
            String modifiedBy) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            // datetime()
            // ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate
            statement.executeUpdate("insert into songs values(null, " + "'songTitle',  " + "'songLyrics', "
                    + "'songAuthor', " + "'modifiedBy', " + "datetime())");
            statement
                    .executeUpdate("update songs set songLyrics='songLyrics2', modifiedDate=datetime() where songTitle='songTitle'");

            ResultSet rs = statement.executeQuery("select * from songs");
            System.out.println("songs: ");
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
                        + rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
            }

            rs.close();
            ResultSet rs2 = statement.executeQuery("select * from song_revisions");
            System.out.println("song_revisions: ");
            while (rs2.next()) {
                System.out.println(rs2.getString(1) + "\t" + rs2.getString(2) + "\t" + rs2.getString(3) + "\t"
                        + rs2.getString(4) + "\t" + rs2.getString(5) + "\t" + rs2.getString(6));
            }

            rs.close();
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    public static SongClass testDatabaseInsert(String songTitle, String songLyrics, String songAuthor, String modifiedBy)
            throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        SongClass song = new SongClass();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            // datetime()
            // ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate
            statement.executeUpdate("insert into songs values(null, '" + songTitle + "','" + songLyrics + "','"
                    + songAuthor + "','" + modifiedBy + "'," + "datetime()," + "0)");

            ResultSet rs = statement.executeQuery("select * from songs");
            System.out.println("songs: ");
            while (rs.next()) {
                song.setTitle(rs.getString(2));
                song.setLyrics(rs.getString(3));
                song.setAuthor(rs.getString(4));
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
                        + rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
            }

            rs.close();
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return song;
    }

    public static void createTables() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // dropping any existing table
            statement.executeUpdate("drop table if exists songs");
            statement.executeUpdate("drop table if exists song_revisions");

            // creating main table for storing songs
            statement.executeUpdate("create table songs(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "songTitle text, " + "songLyrics text, " + "songAuthor text, " + "modifiedBy text, "
                    + "modifiedDate text, " + "version INTEGER DEFAULT 0 NOT NULL);");
            // creating revision table for storing song revisions
            statement.executeUpdate("create table song_revisions(" + "lastRevisionID integer, " + "songTitle text, "
                    + "songLyrics text, " + "songAuthor text, " + "modifiedBy text, " + "modifiedDate text);");

            // creating trigger rule for updating song revisions after song is
            // updated
            statement.executeUpdate("create trigger revision_update_trigger " + "after update on songs " + "begin "
                    + "insert into song_revisions values (" + "NEW.ID," + "NEW.songTitle," + "NEW.songLyrics,"
                    + "NEW.songAuthor," + "NEW.modifiedBy," + "NEW.modifiedDate);" + "end;");
            statement.executeUpdate("create trigger revision_insert_trigger " + "after insert on songs " + "begin "
                    + "insert into song_revisions values (" + "NEW.ID," + "NEW.songTitle," + "NEW.songLyrics,"
                    + "NEW.songAuthor," + "NEW.modifiedBy," + "NEW.modifiedDate);" + "end;");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    public static void selectAllSongs() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from songs");
            System.out.println("songs: ");
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
                        + rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
            }

            rs.close();

            ResultSet rs2 = statement.executeQuery("select * from song_revisions");
            System.out.println("song_revisions: ");
            while (rs2.next()) {
                System.out.println(rs2.getString(1) + "\t" + rs2.getString(2) + "\t" + rs2.getString(3) + "\t"
                        + rs2.getString(4) + "\t" + rs2.getString(5) + "\t" + rs2.getString(6));
            }

            rs2.close();
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }

    public static ArrayList<SongClass> selectAllSongsClasses() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ArrayList<SongClass> songsArrayList = new ArrayList<SongClass>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            // ID, songTitle, songLyrics, songAuthor, modifiedBy, modifiedDate
            ResultSet rs = statement.executeQuery("select * from songs");
            // System.out.println("songs: ");

            while (rs.next()) {
                SongClass song = new SongClass();
                song.setTitle(rs.getString(2));
                song.setLyrics(rs.getString(3));
                song.setAuthor(rs.getString(4));
                songsArrayList.add(song);
            }

            rs.close();

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return songsArrayList;
    }

    public static void main(String[] args) {
    }

}
