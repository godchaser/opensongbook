package org.duckdns.valci.opensongbook;

import org.duckdns.valci.opensongbook.data.SongSQLContainer;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.StringLengthValidator;
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
    OpenSongBookController controller;

    ListSelect selectChordTransposition;

    FieldGroup editorFields;
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

    int songEditorRows = 30;

    public SongEditorView() {
        this.controller = new OpenSongBookController(this);
        //this.controller = OpenSongBookController.getInstance(this);
        createSongEditorComponents();
    }

    private void createSongEditorComponents() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        // setSizeUndefined();
        // setHeight("100%");
        addComponent(new NavigationMenu());

        horizontalSongFieldLayout = new HorizontalLayout();

        // ***********EDITOR FIELDS*************************
        editorFields = new FieldGroup();
        editorFields.setBuffered(true);

        // ***********SONG FIELDS COMPONENTS*****************

        songNameField = new TextField();
        songNameField.setCaption("Name");
        // songTextInput.setSizeFull();
        songNameField.setId("songNameField");
        // songNameField.setWidth("15%");
        songNameField.addValidator(new StringLengthValidator("The song name must be 1-32 letters (was {0})", 1, 30000,
                false));
        songNameField.setValidationVisible(false);

        editorFields.bind(songNameField, SongSQLContainer.propertyIds.SONGTITLE.toString());

        songAuthorField = new TextField();
        songAuthorField.setCaption("Author");
        // songTextInput.setSizeFull();
        songAuthorField.setId("songAuthorField");
        // songAuthorField.setWidth("15%");
        // songAuthorField.addValidator(new
        // StringLengthValidator("The song author name must be 1-32 letters (was {0})",
        // 1, 32, true));
        editorFields.bind(songAuthorField, SongSQLContainer.propertyIds.SONGAUTHOR.toString());

        horizontalSongFieldLayout.addComponent(songNameField);
        horizontalSongFieldLayout.addComponent(songAuthorField);
        horizontalSongFieldLayout.setSpacing(true);
        // horizontalSongFieldLayout.setSizeFull();
        addComponent(horizontalSongFieldLayout);

        // ***********SONG EDITOR LAYOUT COMPONENTS*****************

        horizontalSongTextLayout = new HorizontalLayout();
        horizontalSongTextLayout.setSpacing(true);
        horizontalSongTextLayout.setHeight("80%");
        horizontalSongTextLayout.setWidth("100%");

        verticalSongTextSidebarLayout = new VerticalLayout();
        verticalSongTextSidebarLayout.setSpacing(true);

        // ***********SONG BUTTONS COMPONENTS*****************

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
        transposeButton.addClickListener(controller.getButtonListener());

        newSongButton = new Button("New song");
        newSongButton.setId("newSongButton");
        newSongButton.addClickListener(controller.getButtonListener());

        saveSongButton = new Button("Save song");
        saveSongButton.setId("saveSongButton");
        saveSongButton.addClickListener(controller.getButtonListener());

        deleteSongButton = new Button("Delete song");
        deleteSongButton.setId("deleteSongButton");
        deleteSongButton.addClickListener(controller.getButtonListener());

        verticalSongTextSidebarLayout.addComponent(selectChordTransposition);
        verticalSongTextSidebarLayout.addComponent(transposeButton);
        verticalSongTextSidebarLayout.addComponent(newSongButton);
        verticalSongTextSidebarLayout.addComponent(saveSongButton);
        verticalSongTextSidebarLayout.addComponent(deleteSongButton);

        // ***********SEARCH COMPONENTS*****************

        // I need to make instance of input component while I need reference for
        // update from search box
        songTextInput = new TextArea();

        searchSongsField = new TextField("Search");
        searchSongsField.setId("searchSongsField");
        searchSongsField.setTextChangeEventMode(TextChangeEventMode.LAZY);
        searchSongsField.setTextChangeTimeout(200);
        searchSongsField.addTextChangeListener(controller.getSearchFieldTextChangeListener());
        searchSongsField.setSizeFull();
        // TODO: currently disable because in multiple view it affects all users
        searchSongsField.setEnabled(false);

        songListTable = new Table(null, controller.getSQLContainer());
        songListTable.setId("songListTable");
        songListTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        songListTable.setSelectable(true);
        songListTable.setEditable(false);
        songListTable.setSizeFull();
        // songListTable.setNullSelectionAllowed(false);
        songListTable.setVisibleColumns(new Object[] { songListTable.getVisibleColumns()[1] });

        songListTable.addValueChangeListener(this.controller.getTableValueChangeListener(songListTable, songTextInput,
                songNameField, songAuthorField));

        songSearchLayout = new VerticalLayout();
        songSearchLayout.addComponent(searchSongsField);
        songSearchLayout.addComponent(songListTable);
        horizontalSongTextLayout.addComponent(songSearchLayout);

        // ***********SONG TEXT INPUT COMPONENTS*****************
        songTextInput.setCaption("Editor");
        songTextInput.setId("songTextInput");
        songTextInput.setStyleName("monoSpaceTextArea");
        songTextInput.setSizeFull();
        songTextInput.setRows(songEditorRows);
        songTextInput.addValidator(new StringLengthValidator("The song lyrics must have at least 1 letters (was {0})",
                1, 4000, false));
        songTextInput.setValidationVisible(false);
        editorFields.bind(songTextInput, SongSQLContainer.propertyIds.SONGLYRICS.toString());

        // ***********SONG EDITOR LAYOUT *****************

        horizontalSongTextLayout.addComponent(songTextInput);
        horizontalSongTextLayout.addComponent(verticalSongTextSidebarLayout);
        horizontalSongTextLayout.setExpandRatio(songSearchLayout, 1);
        horizontalSongTextLayout.setExpandRatio(songTextInput, 2);
        songTextInput.setSizeFull();
        horizontalSongTextLayout.setExpandRatio(verticalSongTextSidebarLayout, 1);
        addComponent(horizontalSongTextLayout);

        // ***********SONG FOOTBAR COMPONENTS*****************

        footbarLayout = new HorizontalLayout();
        footbarLayout.setSpacing(true);
        exportSongButton = new Button("Export song to docx");
        exportSongButton.setId("exportSongButton");
        exportSongButton.addClickListener(controller.getButtonListener());
        footbarLayout.addComponent(exportSongButton);

        downloadExportedSongDocxLink = new Link();
        downloadExportedSongDocxLink.setCaption("exported song (docx)");
        downloadExportedSongDocxLink.setId("downloadExportedSongDocxLink");
        addComponent(footbarLayout);
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

    public TextField getSearchSongsField() {
        return searchSongsField;
    }

    public Table getSongListTable() {
        return songListTable;
    }

    public Button getNewSongButton() {
        return newSongButton;
    }

    public Object getSelectedSong() {
        // TODO Auto-generated method stub
        Object selectedSong = songListTable.getValue();
        return selectedSong;
    }

    public Object[] getProgressComponents() {
        // TODO Auto-generated method stub
        return null;
    }

    public FieldGroup getEditorFields() {
        // TODO Auto-generated method stub
        return editorFields;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}
