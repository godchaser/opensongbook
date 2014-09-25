package com.example.opensongbook;

import com.example.opensongbook.UI.Menu;
import com.example.opensongbook.data.SongSQLContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class SongBookManagerView extends VerticalLayout implements View {
    public SongBookManagerView(SongSQLContainer songSQLContainerInstance) {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new Menu());
        TwinColSelect songBookManagerTwinColSelect = new TwinColSelect();
        addComponent(songBookManagerTwinColSelect);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}