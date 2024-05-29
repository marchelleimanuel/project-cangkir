package main;

import javafx.application.Application;
import javafx.stage.Stage;
import util.Connect;
import view.LoginPage;
import view.RegisterPage;
import view.homepage.admin.CupManagementPage;
import view.homepage.user.CartPage;
import view.homepage.user.CheckOutConfirmationPopUp;
import view.homepage.user.HomePage;

public class Main extends Application{	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage s) throws Exception {
		LoginPage login = new LoginPage();
		login.start(s);

		s.setResizable(false);
	} 
	
}
