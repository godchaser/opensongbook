package com.example.opensongbook.test;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.example.opensongbook.data.LineTester;
import com.example.opensongbook.data.SongClass;
import com.example.opensongbook.data.SongExporter;
import com.example.opensongbook.data.SqliteManager;

public class UnitTests {
	@Test
	public void testDatabaseCreation() throws Exception {
		SqliteManager.createTables();
	}

	@Test
	public void testDatabaseSongInsert() throws Exception {
		SongClass testSong = new SongClass();
		testSong.setTitle("Test song title");
		testSong.setLyrics("Test song lyrics");
		testSong.setAuthor("Test author");
		SongClass provisionedSong = SqliteManager.testDatabaseInsert(
				testSong.getTitle(), testSong.getLyrics(),
				testSong.getAuthor(), "Test User");
		assert (testSong.getTitle().equals(provisionedSong.getTitle())
				&& testSong.getLyrics().equals(provisionedSong.getLyrics()) && testSong
				.getAuthor().equals(provisionedSong.getAuthor()));
	}
	

	@Test
	public void testDatabaseTrigger() throws Exception {

	}

	@Test
	public void testOpensongImport() throws Exception {
		String opensongDirectory = "test_data//opensong_data//";
		SongExporter.exportOpensongFolderToOpenSongbook(opensongDirectory);
	}

	@Test
	// should fail while do not support H, but only B - american
	public void testLineTesterBasicChord1() throws Exception {
		ArrayList<SongClass> songs = SqliteManager.selectAllSongsClasses();
		for (SongClass song : songs) {
			// alternative
			// String[] lines =
			// song.getLyrics().split(System.getProperty("line.separator"));
			// splitting lines by new line
			String[] lines = song.getLyrics().split("[\r\n]+");
			for (int i = 0; i < lines.length; i++) {
				// taking into consideration only lines beggining with . - while
				// it is opensong chord line
				if (lines[i].startsWith(".")) {
					try {
						assert (LineTester.checkChords(lines[i].substring(1)
								.replace("H", "B").replace("/", " ")
								.replace("–", " ")) == true);
					} catch (java.lang.AssertionError e) {
						System.out.println("Pjesma: " + song.getTitle()
								+ "\n chord line: " + lines[i].substring(1));
						throw new Exception("was bound to fail!!!");
					}
				}
			}

		}
	}

}
