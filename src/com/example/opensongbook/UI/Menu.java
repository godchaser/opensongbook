package com.example.opensongbook.UI;

import com.example.opensongbook.OpensongbookUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class Menu extends CustomComponent {
	public Menu() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(songEditorButton());
		layout.addComponent(songBookManagerButton());
		layout.addComponent(logoutButton());
		layout.setSizeUndefined();
		layout.setSpacing(true);
		setSizeUndefined();
		setCompositionRoot(layout);
	}

	private Button songEditorButton() {
		Button button = new Button("Song Editor", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(OpensongbookUI.SONGEDITOR);
			}
		});
		button.setId("songEditorButton");
		return button;
	}

	private Button songBookManagerButton() {
		Button button = new Button("Songbook Manager", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(OpensongbookUI.SONGBOOKMANAGER);
			}
		});
		button.setId("songBookManagerButton");
		return button;
	}

	private Button logoutButton() {
		Button button = new Button("Logout", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getSession().close();
				getUI().getPage().setLocation(getLogoutPath());
			}
		});
        button.setId("logoutButton");
		return button;
	}

	private String getLogoutPath() {
		return getUI().getPage().getLocation().getPath();
	}
}