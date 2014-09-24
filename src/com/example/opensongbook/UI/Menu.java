package com.example.opensongbook.UI;

import com.example.opensongbook.OpensongbookUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class Menu extends CustomComponent {
	public Menu() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(songEditorButton());
		layout.addComponent(helpButton());
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

	private Button helpButton() {
		Button button = new Button("Help", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(OpensongbookUI.HELPVIEW);
			}
		});
		button.setId("helpButton");
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