package com.example.opensongbook;


import com.example.opensongbook.UI.Menu;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
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
    SongEditorController controller;

    ListSelect selectChordTransposition;
    TextArea songTextInput;

    TextField songNameField;
    TextField songAuthorField;

    Link downloadExportedSongDocxLink;

    HorizontalLayout footbarLayout;
    HorizontalLayout horizontalSongFieldLayout;
    HorizontalLayout horizontalSongTextLayout;

    VerticalLayout verticalSongTextSidebarLayout;
    VerticalLayout songSearchLayout;

    Button transposeButton;
    Button newSongButton;
    Button saveSongButton;
    Button deleteSongButton;
    Button exportSongButton;

    TextField searchSongsField;
    Table songListTable;

    public SongEditorView(SongSQLContainer songSQLContainerInstance) {
        this.controller = new SongEditorController(this,
                songSQLContainerInstance);
        createSongEditorComponents();
    }

    private void createSongEditorComponents() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);

        addComponent(new Menu());

        // this are song editor components
        horizontalSongFieldLayout = new HorizontalLayout();

        songNameField = new TextField();
        songNameField.setCaption("Name");
        // songTextInput.setSizeFull();
        songNameField.setId("songNameField");

        songAuthorField = new TextField();
        songAuthorField.setCaption("Author");
        // songTextInput.setSizeFull();
        songAuthorField.setId("songAuthorField");

        horizontalSongFieldLayout.addComponent(songNameField);
        horizontalSongFieldLayout.addComponent(songAuthorField);
        horizontalSongFieldLayout.setSizeUndefined();
        horizontalSongFieldLayout.setSpacing(true);
        addComponent(horizontalSongFieldLayout);

        horizontalSongTextLayout = new HorizontalLayout();
        horizontalSongTextLayout.setSpacing(true);

        songTextInput = new TextArea();
        // songTextInput.setSizeFull();
        songTextInput.setCaption("Editor");
        songTextInput.setWidth("600px");
        songTextInput.setHeight("400px");
        songTextInput.setId("songTextInput");

        verticalSongTextSidebarLayout = new VerticalLayout();
        verticalSongTextSidebarLayout.setSpacing(true);

        selectChordTransposition = new ListSelect("Chord transpose");
        for (int i = -7; i < 7; i++) {
            selectChordTransposition.addItem(i);
        }
        selectChordTransposition.setId("selectChordTransposition");
        selectChordTransposition.setNullSelectionAllowed(false);
        // Show 5 items and a scrollbar if there are more
        selectChordTransposition.setRows(3);
        selectChordTransposition.select(0);
        selectChordTransposition.setValue(0);

        transposeButton = new Button("Transpose");
        transposeButton.setId("transposeButton");
        transposeButton.addClickListener(controller.buttonListener);

        newSongButton = new Button("New song");
        newSongButton.setId("newSongButton");
        newSongButton.addClickListener(controller.buttonListener);

        saveSongButton = new Button("Save song");
        saveSongButton.setId("saveSongButton");
        saveSongButton.addClickListener(controller.buttonListener);

        deleteSongButton = new Button("Delete song");
        deleteSongButton.setId("deleteSongButton");
        deleteSongButton.addClickListener(controller.buttonListener);

        verticalSongTextSidebarLayout.addComponent(selectChordTransposition);
        verticalSongTextSidebarLayout.addComponent(transposeButton);
        verticalSongTextSidebarLayout.addComponent(newSongButton);
        verticalSongTextSidebarLayout.addComponent(saveSongButton);
        verticalSongTextSidebarLayout.addComponent(deleteSongButton);

        createSongSearchComponents(horizontalSongTextLayout, songTextInput,
                songNameField, songAuthorField);
        horizontalSongTextLayout.addComponent(songTextInput);
        horizontalSongTextLayout.addComponent(verticalSongTextSidebarLayout);
        addComponent(horizontalSongTextLayout);

        // this are footbar components
        footbarLayout = new HorizontalLayout();
        footbarLayout.setSpacing(true);
        exportSongButton = new Button("Export song to docx");
        exportSongButton.setId("exportSongButton");
        exportSongButton.addClickListener(controller.buttonListener);
        footbarLayout.addComponent(exportSongButton);

        downloadExportedSongDocxLink = new Link();
        downloadExportedSongDocxLink.setCaption("exported song (docx)");
        downloadExportedSongDocxLink.setId("downloadExportedSongDocxLink");
        addComponent(footbarLayout);
    }

    public void createSongSearchComponents(
            HorizontalLayout horizontalSongTextLayout, TextArea songTextInput,
            TextField songNameField, TextField songAuthorField) {

        searchSongsField = new TextField("Search");
        searchSongsField.setId("searchSongsField");
        searchSongsField.setTextChangeEventMode(TextChangeEventMode.LAZY);
        searchSongsField.setTextChangeTimeout(200);
        searchSongsField.addTextChangeListener(controller
                .getSearchFieldTextChangeListener());

        songListTable = new Table(null, controller.getSQLContainer());
        songListTable.setId("songListTable");
        songListTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        songListTable.setSelectable(true);
        songListTable.setEditable(false);
        songListTable.setNullSelectionAllowed(false);
        songListTable.setVisibleColumns(new Object[] { songListTable
                .getVisibleColumns()[1] });

        songListTable.addValueChangeListener(this.controller
                .getTableValueChangeListener(songListTable, songTextInput,
                        songNameField, songAuthorField));

        searchSongsField.setWidth("150px");
        songListTable.setWidth("150px");

        songSearchLayout = new VerticalLayout();
        songSearchLayout.addComponent(searchSongsField);
        songSearchLayout.addComponent(songListTable);
        horizontalSongTextLayout.addComponent(songSearchLayout);
    }

    public HorizontalLayout getFootbarLayout() {
        return footbarLayout;
    }

    public TextField getSongNameField() {
        return songNameField;
    }

    public TextField getSongAuthorField() {
        return songAuthorField;
    }

    public TextArea getSongTextInput() {
        return songTextInput;
    }

    public ListSelect getSelectChordTransposition() {
        return selectChordTransposition;
    }

    public Link getDownloadExportedSongDocxLink() {
        return downloadExportedSongDocxLink;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
