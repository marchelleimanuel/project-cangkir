package view.homepage.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import util.Connect;
import view.LoginPage;
import view.homepage.Cart;

public class CheckOutConfirmationPopUp extends Application{

	private Connect connect = Connect.getInstance();
	private Stage stage;
	private Scene sc;
	private BorderPane bp;
	private FlowPane fp;
	private VBox vb;
	private Window window;
	private Label confirmationLbl;
	private Button yesBtn, noBtn;
	
	public void init() {
		bp = new BorderPane();
		fp = new FlowPane();
		vb = new VBox(25);
		sc = new Scene(bp, 800,700);
		
		window = new Window("Checkout confirmation");
		
		confirmationLbl = new Label("Are you sure want to purchase?");
		
		yesBtn = new Button("Yes");
		noBtn = new Button("No");
	}
	
	public void setLayout() {
		bp.setCenter(window);
	}
	
	public void addComponent() {
		fp.getChildren().addAll(yesBtn, noBtn);
		vb.getChildren().addAll(confirmationLbl, fp);
		window.getContentPane().getChildren().add(vb);
	}
	
	public void setAlignment() {
		vb.setAlignment(Pos.CENTER);
		fp.setAlignment(Pos.CENTER);
		fp.setHgap(30);
		
		yesBtn.setPadding(new Insets(10, 30, 10, 30));
		noBtn.setPadding(new Insets(10, 30, 10, 30));
	}
	
	public void editComponent() {
		confirmationLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
	}
	
	public void eventHandler() {
		yesBtn.setOnAction(e -> {
			
			String username = LoginPage.username;
			String courier = CartPage.courier;
			int deliveryInsurance = CartPage.deliveryInsurance;
			LocalDate currDate = LocalDate.now();
	        String formattedDate = currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        String transactionId = getTransactionId();
	        
	        connect.addDataToTransactionHeader(transactionId, username, courier, formattedDate, deliveryInsurance);	
	        connect.addDataToTransactionDetails(username, transactionId, CartPage.cart);
	        connect.clearTableCart();
	        
			CartPage c = new CartPage();
			try {
				c.start(stage);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Checkout Information");
				alert.setContentText("Checkout Successful");
				alert.showAndWait();
			} catch (Exception ex) {
				
			}
		});
		
		noBtn.setOnAction(e -> {
			CartPage c = new CartPage();
			try {
				c.start(stage);
			} catch (Exception ex) {
				
			}
		});
	}
	
	public String getTransactionId() {
	    int transaction = countTransaction();
	    int nextTransaction = transaction + 1;
	    String ID = String.format("%03d", nextTransaction);
	    String transactionId = "TR" + ID;
	    
	    return transactionId;
	}
	
	

	public int countTransaction() {
	    String query = "SELECT COUNT(*) FROM transactionheader";
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
	
	public CheckOutConfirmationPopUp() {
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
