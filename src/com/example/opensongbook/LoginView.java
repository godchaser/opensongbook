package com.example.opensongbook;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout implements View {
	public LoginView() {
		setSizeFull();
		setSpacing(true);
		Label label = new Label("Enter your information below to log in.");
		TextField username = new TextField("Username");
		username.setId("usernameTextBoxLoginView");
		TextField password = new TextField("Password");
		password.setId("passwordTextBoxLoginView");
		addComponent(label);
		addComponent(username);
		addComponent(password);
		addComponent(loginButton());
	}


	private Button loginButton() {
		Button button = new Button("Log In", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(OpensongbookUI.SONGEDITOR);
			}
		});
		button.setId("loginButton");
		return button;
	}


    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
        
    }
}
