package com.example.opensongbook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.opensongbook.data.ChordTransposer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SongEditorController {
    private SongEditorView songEditorView;

    public SongEditorController(SongEditorView songEditorView) {
        this.songEditorView = songEditorView;
        // TODO Auto-generated constructor stub
    }

    private SongEditorModel model = new SongEditorModel();

    // this is listener for all button features
    ClickListener buttonListener = new Button.ClickListener() {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            case ("transposeButton"):
                model.chordTranspose(songEditorView.getSelect, );
                break;
            }
        }
    };

}
