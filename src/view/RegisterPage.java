package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.Connect;
import view.homepage.admin.CupManagementPage;
import view.homepage.user.HomePage;

public class RegisterPage extends Application{
	private Connect connect = Connect.getInstance();
	private Stage stage;
	private Scene sc;
	private BorderPane bp;
	private FlowPane fp;
	private VBox vb, vb1;
		
	private Label titleLbl, usernameLbl, emailLbl, passwordLbl, genderLbl;
	private TextField usernameTf, emailTf;
	private PasswordField passwordPf;
	
	private ToggleGroup genderGroup;
	private RadioButton maleRadio, femaleRadio;
	
	private Hyperlink loginLink;
	
	private Button registerBtn;
	
	public void init() {
		fp = new FlowPane();
		bp = new BorderPane();
		vb = new VBox(20);
		vb1 = new VBox();
		
		sc = new Scene(bp, 800, 700);
		
		titleLbl = new Label("Register");
		usernameLbl = new Label("Username");
		passwordLbl = new Label("Password");
		emailLbl = new Label("Email");
		genderLbl = new Label("Gender");
		
		usernameTf = new TextField();
		usernameTf.setPromptText("Input your username here");
		
		emailTf = new TextField();
		emailTf.setPromptText("Input your email here");
		
		passwordPf = new PasswordField();
		passwordPf.setPromptText("Input your password here");
		
		maleRadio = new RadioButton("Male");
		femaleRadio = new RadioButton("Female");
		genderGroup = new ToggleGroup();
		
		loginLink = new Hyperlink("Already have an account? Click here to login!");
		
		registerBtn = new Button("Register");
	}
	
	public void setLayout() {
		bp.setCenter(vb);
	}
	
	public void addComponent() {
		fp.getChildren().addAll(maleRadio, femaleRadio);
		maleRadio.setToggleGroup(genderGroup);
		femaleRadio.setToggleGroup(genderGroup);
		fp.setHgap(20);
		
		vb.getChildren().addAll(titleLbl,vb1, registerBtn, loginLink);
		vb1.getChildren().addAll(usernameLbl, usernameTf, emailLbl, emailTf, passwordLbl, passwordPf, genderLbl, fp);
	}
	
	public void setAlignment() {
		vb.setAlignment(Pos.CENTER);
		vb1.setPadding(new Insets(0, 150, 0, 150));
		
		VBox.setMargin(emailLbl, new Insets(25, 0, 0, 0));
		VBox.setMargin(emailTf, new Insets(0, 0, 25, 0));
		VBox.setMargin(passwordPf, new Insets(0, 0, 25, 0));
		VBox.setMargin(genderLbl, new Insets(0, 0, 20, 0));
		
		registerBtn.setPadding(new Insets(10, 30, 10, 30));
	}
	
	public void editComponent() {
		titleLbl.setFont(Font.font("Calibri",FontWeight.BOLD,35));
		genderLbl.setFont(Font.font("Calibri",FontWeight.BOLD,25));
	}
	
	
	public void eventHandler() {
		loginLink.setOnAction(e -> {
			LoginPage login = new LoginPage();
			try {
				login.start(stage);
			} catch (Exception ex) {
				
			}
		});
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Register Error");
		
		registerBtn.setOnAction(e -> {
			if(e.getSource() == registerBtn) {
				String username = usernameTf.getText();
				String email = emailTf.getText();
				String password = passwordPf.getText();
				boolean male = maleRadio.isSelected();
				boolean female = femaleRadio.isSelected();
				String genderValue = null;
				String role = null;
				boolean isEmailValid = true;
				boolean isUsernameValid = true;
				boolean isPasswordValid = true;
				boolean isGenderValid = true;
				
				try {
					RadioButton value = (RadioButton) genderGroup.getSelectedToggle();
					genderValue = value.getText();	
				} catch(Exception ex){
				}
				
				if(username.contains("admin")) {
					 role = "Admin";
				}
				else {
					role = "User";
				}
				
				if(username.equals("")) {
					alert.setContentText("Please fill out your username");	
					alert.showAndWait();
					isUsernameValid = false;
				}
				else if(connect.isUsernameExist(username)) {
					alert.setContentText("Please choose a different username");
					alert.showAndWait();
					isUsernameValid = false;
				}
				else if(email.equals("")) {
					alert.setContentText("Please fill out your email");
					alert.showAndWait();
					isEmailValid = false;
				}
				else if(!email.endsWith("@gmail.com") || email.startsWith("@gmail.com")) {
					alert.setContentText("Make sure your email ends with @gmail.com");
					alert.showAndWait();
					isEmailValid = false;
				}
				else if(connect.isEmailExist(email)) {
					alert.setContentText("Please choose a different email");
					alert.showAndWait();
					isEmailValid = false;
				}
				else if(password.equals("")) {
					alert.setContentText("Please fill out your password");
					alert.showAndWait();
					isPasswordValid = false;
				}
				else if(password.length() < 8 || password.length() > 15) {
					alert.setContentText("Make sure your password has a length of 8 - 15 characters");
					alert.showAndWait();
					isPasswordValid = false;
				}
				else if(!isAlphanumeric(password)) {
					alert.setContentText("Password must be alphanumeric");
					alert.showAndWait();
					isPasswordValid = false;
				}
				else if((!male && !female)) {
					alert.setContentText("Please fill out your gender");
					alert.showAndWait();
					isGenderValid = false;
				}	 
				
				if(isUsernameValid && isEmailValid && isPasswordValid && isGenderValid) {
					insertDataRegister(username, email, password, genderValue, role);	
					
					Alert success = new Alert(AlertType.INFORMATION);
					success.setHeaderText("Registration");
					success.setContentText("Register Success");
					success.showAndWait();
										
	            	LoginPage login = new LoginPage();
	            	try {
	            		login.start(stage);
					} catch (Exception e2) {
						
					}
					
				}
			}
			
		});
	} 
	
	public void insertDataRegister(String username, String email, String password, String gender, String role) {
	    int users = countUsers();
	    int nextUser = users + 1;
	    String ID = String.format("%03d", nextUser);
	    String userID = "US" + ID;
	    
		String query = "INSERT INTO msuser "
				+ "VALUES ('"+userID+"', '"+ username +"', '"+ email +"', '"+ password +"', '"+ gender +"' , '"+ role +"')";
		
		connect.execUpdate(query);
	}
	
	public int countUsers() {
	    String query = "SELECT COUNT(*) FROM msuser";
	    try {
	        ResultSet resultSet = connect.execQuery(query);
	        if (resultSet.next()) {
	            return resultSet.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0; 
	}
	
	public boolean isAlphanumeric(String password) {
		boolean isAlphabetic = false;
		boolean isNumeric = false;
		boolean isAlphanumeric = false;
		
		for (int i = 0; i < password.length(); i++) {
			if(Character.isAlphabetic(password.charAt(i))) {
				isAlphabetic = true;
			}
			
			if(Character.isDigit(password.charAt(i))) {
				isNumeric = true;
			}
		}
		
		if(isAlphabetic == true && isNumeric == true) {
			isAlphanumeric = true;
			return true;
		}
		
		return false;
	}
	
	public RegisterPage() {
		init();
		setLayout();
		addComponent();
		setAlignment();
		editComponent();
		eventHandler();
	}
	
	@Override
	public void start(Stage s) throws Exception {
		stage = s;
		s.setScene(sc);
		s.show();
	}

}
