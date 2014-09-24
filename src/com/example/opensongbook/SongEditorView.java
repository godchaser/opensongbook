package com.example.opensongbook;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.opensongbook.UI.Menu;
import com.example.opensongbook.data.ChordTransposer;
import com.example.opensongbook.data.DocumentWriter;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.ColumnProperty;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SongEditorView extends VerticalLayout implements View {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private SongEditorController controller = new SongEditorController(this);
	
	private SongSQLContainer songSQLContainerInstance = new SongSQLContainer();
	SQLContainer songContainer = songSQLContainerInstance.getSongContainer();

	public SongEditorView() {
		setSizeFull();
		setSpacing(true);

		setMargin(true);
		// setContent(layout);

		addComponent(new Menu());

		HorizontalLayout horizontalSongFieldLayout = new HorizontalLayout();

		final TextField songNameField = new TextField();
		songNameField.setCaption("Name");
		// songTextInput.setSizeFull();
		songNameField.setId("songNameField");
		// addComponent(songNameField);

		final TextField songAuthorField = new TextField();
		songAuthorField.setCaption("Author");
		// songTextInput.setSizeFull();
		songAuthorField.setId("songAuthorField");
		// addComponent(songAuthorField);

		horizontalSongFieldLayout.addComponent(songNameField);
		horizontalSongFieldLayout.addComponent(songAuthorField);
		horizontalSongFieldLayout.setSizeUndefined();
		horizontalSongFieldLayout.setSpacing(true);
		addComponent(horizontalSongFieldLayout);

		HorizontalLayout horizontalSongTextLayout = new HorizontalLayout();
		horizontalSongTextLayout.setSpacing(true);

		final TextArea songTextInput = new TextArea();
		// songTextInput.setSizeFull();
		songTextInput.setCaption("Editor");
		songTextInput.setWidth("600px");
		songTextInput.setHeight("400px");
		songTextInput.setId("songTextInput");
		// addComponent(songTextInput);
		createSongSearch(horizontalSongTextLayout, songTextInput,
				songNameField, songAuthorField);

		horizontalSongTextLayout.addComponent(songTextInput);

		VerticalLayout verticalSongTextSidebarLayout = new VerticalLayout();
		verticalSongTextSidebarLayout.setSpacing(true);
		// Create the selection component
		final ListSelect selectChordTransposition = new ListSelect(
				"Chord transpose");

		for (int i = -7; i < 7; i++) {
			selectChordTransposition.addItem(i);
		}

		selectChordTransposition.setId("selectChordTransposition");
		selectChordTransposition.setNullSelectionAllowed(false);
		// Show 5 items and a scrollbar if there are more
		selectChordTransposition.setRows(3);
		// selectChordTransposition.setTabIndex(7);
		// addComponent(selectChordTransposition);

		Button transposeButton = new Button("Transpose");
		transposeButton.setId("transposeButton");
		transposeButton.addClickListener(controller.buttonListener);
		 /*
        transposeButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                String[] songList = songTextInput.getValue().split("[\r\n]+");
                StringBuilder updatedSong = new StringBuilder();
                for (String songLine : songList) {
                    // this is a chord line
                    if (songLine.startsWith(".")) {
                        Pattern p = Pattern.compile("[\\w'\\S]+");
                        Matcher m = p.matcher(songLine);
                        StringBuffer chordLineBuilder = new StringBuffer(
                                songLine);
                        // go through each chord
                        while (m.find()) {
                            String rootChord = songLine.substring(m.start(),
                                    m.end());
                            String transposed = (".".equals(rootChord)) ? "."
                                    : ChordTransposer.improvedTransposeChord(
                                            rootChord,
                                            (int) selectChordTransposition
                                                    .getValue());
                            ;
                            // String transposed =
                            // ChordTransposer.improvedTransposeChord(rootChord,
                            // (int) selectChordTransposition.getValue());
                            chordLineBuilder.replace(m.start(), m.end(),
                                    transposed);
                        }
                        updatedSong.append(chordLineBuilder.toString()
                                + newline);
                    } else {
                        updatedSong.append(songLine + newline);
                    }
                }
                songTextInput.setValue(updatedSong.toString());
            }
        });
        */
		// addComponent(transposeButton);

		Button newSongButton = new Button("New song");
		newSongButton.setId("newSongButton");

		Button saveSongButton = new Button("Save song");
		saveSongButton.setId("saveSongButton");

		Button deleteSongButton = new Button("Delete song");
		deleteSongButton.setId("deleteSongButton");

		verticalSongTextSidebarLayout.addComponent(selectChordTransposition);
		verticalSongTextSidebarLayout.addComponent(transposeButton);
		verticalSongTextSidebarLayout.addComponent(newSongButton);
		verticalSongTextSidebarLayout.addComponent(saveSongButton);
		verticalSongTextSidebarLayout.addComponent(deleteSongButton);

		horizontalSongTextLayout.addComponent(verticalSongTextSidebarLayout);
		addComponent(horizontalSongTextLayout);
       
		final HorizontalLayout footbarLayout = new HorizontalLayout();
		footbarLayout.setSpacing(true);
		Button exportSongButton = new Button("Export song to docx");
		exportSongButton.setId("exportSongButton");
		footbarLayout.addComponent(exportSongButton);

		exportSongButton.addClickListener(new Button.ClickListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String[] songList = songTextInput.getValue().split("[\r\n]+");
				String songName = songNameField.getValue();
				ArrayList<String> songArray = new ArrayList<String>();
				for (String songLine : songList) {
					songArray.add(songLine);
				}
				DocumentWriter doc = new DocumentWriter();
				try {
					FileResource generatedFile = doc.newWordDoc(
							"testOutputSong", songName, songArray);
					Link downloadExportedSongDocxLink = new Link(
							"Link to the generated docx", generatedFile);
					downloadExportedSongDocxLink
							.setCaption("Song in docx format");
					downloadExportedSongDocxLink
							.setId("downloadExportedSongDocxLink");
					footbarLayout.addComponent(downloadExportedSongDocxLink);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		addComponent(footbarLayout);
	}

	public void createSongSearch(HorizontalLayout horizontalSongTextLayout,
			final TextArea songTextInput, final TextField songNameField,
			final TextField songAuthorField) {

		TextField searchSongsField = new TextField("Search");
		searchSongsField.setId("searchSongsField");
		searchSongsField.setTextChangeEventMode(TextChangeEventMode.LAZY);
		searchSongsField.setTextChangeTimeout(200);
		searchSongsField.addTextChangeListener(new TextChangeListener() {

			/**
             * 
             */
			private static final long serialVersionUID = 1L;

			public void textChange(TextChangeEvent event) {
				songContainer.removeAllContainerFilters();
				songContainer.addContainerFilter(
						SongSQLContainer.propertyIds.songLyrics.toString(),
						event.getText(), true, false);
			}
		});

		final Table songListTable = new Table(null, songContainer);
		songListTable.setId("songListTable");
		songListTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		songListTable.setSelectable(true);
		songListTable.setEditable(false);
		songListTable.setNullSelectionAllowed(false);
		songListTable.setVisibleColumns(new Object[] { songListTable
				.getVisibleColumns()[1] });
		// songListTable.setPageLength(20);
		// songListTable.sort(SongSQLContainer.propertyIds.songTitle, true);
		// table.addItemClickListener(new Listener());

		// listen for valueChange, a.k.a 'select' and update the label
		songListTable.addValueChangeListener(new Table.ValueChangeListener() {
			/**
             * 
             */
			private static final long serialVersionUID = 1L;

			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				// in multiselect mode, a Set of itemIds is returned,
				// in singleselect mode the itemId is returned directly
				// TODO: all this should be logged
				System.out.println("songListTable " + songListTable.getValue());
				Item it = songContainer.getItem(songListTable.getValue());
				// System.out.println("item: " + it);
				String lyrics = (String) it.getItemProperty(
						SongSQLContainer.propertyIds.songLyrics.toString())
						.getValue();
				String songTitle = (String) it.getItemProperty(
						SongSQLContainer.propertyIds.songTitle.toString())
						.getValue();
				String songAuthor = (String) it.getItemProperty(
						SongSQLContainer.propertyIds.songAuthor.toString())
						.getValue();
				// System.out.println("lyrics  " +lyrics);
				songTextInput.setValue(lyrics);
				songNameField.setValue(songTitle);
				songAuthorField.setValue(songAuthor);
			}
		});
		songListTable.addItemClickListener(new ItemClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (!event.isDoubleClick())
					return;
			}
		});
		searchSongsField.setWidth("150px");
		songListTable.setWidth("150px");

		VerticalLayout songSearchLayout = new VerticalLayout();
		songSearchLayout.addComponent(searchSongsField);
		songSearchLayout.addComponent(songListTable);
		horizontalSongTextLayout.addComponent(songSearchLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
