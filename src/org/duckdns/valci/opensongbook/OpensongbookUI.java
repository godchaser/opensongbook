package org.duckdns.valci.opensongbook;

import javax.servlet.annotation.WebServlet;

import org.duckdns.valci.opensongbook.data.SongDatabaseConnector;
import org.duckdns.valci.opensongbook.data.SongSQLContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("opensongbook")
public class OpensongbookUI extends UI {

    public Navigator navigator;
    VerticalLayout layout;

    SongDatabaseConnector songDatabaseConnector;
    SongSQLContainer songSQLContainerInstance;

    public static final String SONGBOOKMANAGER = "songbookmanager";
    public static final String SONGEDITOR = "songeditor";

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = OpensongbookUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        // MainWindow mainWindow = new MainWindow();
        // setContent(mainWindow.drawComponents());
        // MainDashboard mainWindow = new MainDashboard();
        // setContent(mainWindow.buildMainView());
        songDatabaseConnector = new SongDatabaseConnector();
        songSQLContainerInstance = new SongSQLContainer();

        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(
                layout);
        navigator = new Navigator(UI.getCurrent(), viewDisplay);
        navigator.addView("", new LoginView());
        navigator.addView(SONGEDITOR, new SongEditorView(
                songSQLContainerInstance));
        navigator.addView(SONGBOOKMANAGER, new SongBookManagerView(
                songSQLContainerInstance));
    }

}