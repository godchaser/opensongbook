package com.example.opensongbook;

import com.example.opensongbook.UI.Menu;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class HelpView extends VerticalLayout implements View {
    public HelpView() {
        setSizeFull();
        setSpacing(true);
        addComponent(new Menu());
        addComponent(headingLabel());
        addComponent(someText());
    }

    private Label headingLabel() {
        return new Label("Help");
    }

    private Label someText() {
        Label label = new Label("this is example help");
        label.setContentMode(ContentMode.HTML);
        return label;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}