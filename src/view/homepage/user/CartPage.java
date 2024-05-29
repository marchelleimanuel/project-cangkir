package view.homepage.user;

import java.sql.PreparedStatement;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import util.Connect;
import view.LoginPage;
import view.homepage.Cart;
import view.homepage.Cup;

public class CartPage extends Application{
	
	private Connect connect = Connect.getInstance();
	private Scene sc;
	private Stage stage;
	private BorderPane bp;
	private VBox vb,vb1;
	
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem homeMenu, cartMenu, logOutMenu;
	
	private Label cartOwnerLbl, deleteLbl, courierLbl, courierPriceLbl, totalPriceLbl;
	
	private ComboBox<String> courierCb;
	private CheckBox deliveryCb;
	
	private Button checkoutBtn, deleteBtn;
	
	private int courierPrice = 0;
	private int totalPrice = 0;
	private int deliveryPrice = 0;
	
	public static TableView<Cart> table;
	public static Vector<Cart> cart;
	public static int deliveryInsurance = 0;
	public static String courier = null;
	public static String cupName = null;
	
	public void init() {
		bp = new BorderPane();
		vb = new VBox();
		vb1 = new VBox(20);
		
		sc = new Scene(bp, 800,700);
		
		menuBar = new MenuBar();
		menu = new Menu("Menu");
		homeMenu = new MenuItem("Home");
		cartMenu = new MenuItem("Cart");
		logOutMenu = new MenuItem("Log Out");
		
		table = new TableView<>();
		
		cartOwnerLbl = new Label(LoginPage.username + "'s Cart");
		deleteLbl = new Label("Delete Item");
		courierLbl = new Label("Courier");
		courierPriceLbl = new Label("Courier Price: ");
		totalPriceLbl = new Label("Total Price: ");
		
		courierCb = new ComboBox<>();
		courierCb.setDisable(true);
		deliveryCb = new CheckBox("Use Delivery Insurance");
		deliveryCb.setDisable(true);
		
		checkoutBtn = new Button("Checkout");
		deleteBtn = new Button("Delete Item");
		
		cart = new Vector<>();
		
		
	}
	
	public void setLayout() {
		bp.setTop(menuBar);
		bp.setLeft(vb);
		bp.setRight(vb1);
	}
	
	public void addComponent() {
		menuBar.getMenus().add(menu);
		menu.getItems().addAll(homeMenu, cartMenu, logOutMenu);
		
		vb.getChildren().addAll(cartOwnerLbl, table);
		vb1.getChildren().addAll(deleteLbl, deleteBtn, courierLbl, courierCb, courierPriceLbl, deliveryCb, totalPriceLbl, checkoutBtn);
	
		TableColumn<Cart, String> cupName = new TableColumn<>("Cup Name");
		cupName.setCellValueFactory(new PropertyValueFactory<>("cupName"));
		cupName.setMinWidth((bp.getWidth()/2) / 4);
		
		TableColumn<Cart, Integer> cupPrice = new TableColumn<>("Cup Price");
		cupPrice.setCellValueFactory(new PropertyValueFactory<>("cupPrice"));
		cupPrice.setMinWidth((bp.getWidth()/2) / 4);
		
		TableColumn<Cart, Integer> quantity = new TableColumn<>("Quantity");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantity.setMinWidth((bp.getWidth()/2) / 4);
		
		TableColumn<Cart, Integer> totalPrice = new TableColumn<>("Total");
		totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		totalPrice.setMinWidth((bp.getWidth()/2) / 4);
		
		table.getColumns().addAll(cupName, cupPrice, quantity, totalPrice);
		
		courierCb.getItems().addAll("JNA", "TAKA", "LoinParcel","IRX", "JINJA");
		courierCb.setPromptText("Select Courier");
		
		refreshTable();
	}
	
	public void setAlignment() {
		checkoutBtn.setPadding(new Insets(10, 30, 10, 30));
		deleteBtn.setPadding(new Insets(10, 30, 10, 30));
		vb.setPadding(new Insets(10));
		
		vb.setAlignment(Pos.BOTTOM_LEFT);
		
		BorderPane.setMargin(vb1, new Insets(bp.getHeight()/2.8, 0 , 0 , 0));
		VBox.setMargin(cartOwnerLbl, new Insets(0,0,10,0));

		vb.setMinWidth(bp.getWidth()/2);
		vb1.setMinWidth(bp.getWidth()/2);	
		
	}
	
	public void editComponent() {
		cartOwnerLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		deleteLbl.setFont(Font.font(null, FontWeight.BOLD, 25));
		courierLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		courierPriceLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		totalPriceLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
	}
	
	
	public void eventHandler() {
		
		if(!table.getItems().isEmpty()) {
			courierCb.setDisable(false);
			deliveryCb.setDisable(false);
		}
		
		homeMenu.setOnAction(e -> {
			HomePage user = new HomePage();
			try {
				user.start(stage);
			} catch(Exception ex) {
				
			}
		});
		
		checkoutBtn.setOnAction(e -> {
			if(table.getItems().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Your cart is empty!");	
				alert.showAndWait();
			}
			else {
				if(courierCb.getValue() != null) {
					CheckOutConfirmationPopUp pop = new CheckOutConfirmationPopUp();
					try {
						pop.start(stage);
					} catch(Exception ex) {
						
					}
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Error");
					alert.setContentText("Choose courier!");	
					alert.showAndWait();
				}
				
			}
		});
		
		deleteBtn.setOnAction(e -> {
			if(table.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Deletion Error");
				alert.setContentText("Please select the item you want to delete");	
				alert.showAndWait();
			}
			else {
				Cart selectedCart = table.getSelectionModel().getSelectedItem();
				String username = LoginPage.username; 
				String query = "DELETE FROM cart WHERE UserID = (SELECT UserID FROM msuser WHERE Username = ?) AND CupID IN (SELECT CupID FROM mscup WHERE CupName = ?) AND Quantity = ?";
				PreparedStatement ps = connect.prepareStatement(query);
				try {
					ps.setString(1, username);
		            ps.setString(2, selectedCart.getCupName());
		            ps.setInt(3, selectedCart.getQuantity());
		            ps.execute();
		            
		            table.getItems().remove(selectedCart);
				} catch (Exception e2) {
					e2.printStackTrace();
				}				
				
				totalPrice = 0;
				refreshTable();
				
				if(table.getItems().size() < 1) {
					courierCb.setDisable(true);
					deliveryCb.setDisable(true);
				}

				totalPriceLbl.setText("Total Price: " + totalPrice);
				courierPriceLbl.setText("Courier: ");
				CartPage cart = new CartPage();
				try {
					cart.start(stage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Deletion Information");
				alert.setContentText("Cart Deleted Successfully");	
				alert.showAndWait();
			}
			
		});
		
		logOutMenu.setOnAction(e -> {
			LoginPage logOut = new LoginPage();
			try {
				logOut.start(stage);
			} catch (Exception e2) {
			}
		});
		
		courierCb.valueProperty().addListener((observable, oldValue, newValue) -> {

		    if(observable.getValue().equals("JNA")) {
		    	courier = "JNA";
		    	courierPrice = 20000; 
		    }
		    else if(observable.getValue().equals("TAKA")) {
		    	courier = "TAKA";
		    	courierPrice = 19000;
		    }
		    else if(observable.getValue().equals("LoinParcel")) {
		    	courier = "LoinParcel";
		    	courierPrice = 22000;
		    }
		    else if(observable.getValue().equals("IRX")) {
		    	courier = "IRX";
		    	courierPrice = 30000;
		    }
		    else if(observable.getValue().equals("JINJA")) {
		    	courier = "JINJA";
		    	courierPrice = 150000;
		    }
		    int currTotalPrice = totalPrice;
	        totalPrice = totalPrice + courierPrice + deliveryPrice;
	        courierPriceLbl.setText("Courier: " + courierPrice);
	        totalPriceLbl.setText("Total Price: " + totalPrice);
	        totalPrice = currTotalPrice;
		});
		
		deliveryCb.selectedProperty().addListener((observable, oldValue, newValue) -> {
		    if(deliveryCb.isSelected()) {
		    	deliveryPrice = 2000;
		    	deliveryInsurance = 1;
		    }
		    else {
		    	deliveryPrice = 0;
		    	deliveryInsurance = 0;
		    }

		    int currTotalPrice = totalPrice;
	        totalPrice = totalPrice + courierPrice + deliveryPrice;
	        totalPriceLbl.setText("Total Price: " + totalPrice);
	        totalPrice = currTotalPrice;
		});

	}
		
	public void getData() {
	    cart.removeAllElements();

	    String username = LoginPage.username;
	    
	    String query = "SELECT CupName, CupPrice, Quantity, mc.CupPrice * c.Quantity AS Total " +
	    			   "FROM cart c " +
	    			   "JOIN mscup mc ON c.CupID = mc.CupID " +
	    			   "JOIN msuser mu ON c.UserID = mu.UserID " +
	    			   "WHERE mu.Username = ? ";

	    PreparedStatement ps = connect.prepareStatement(query);
	    try {
	    	ps.setString(1, username);
	        connect.rs = ps.executeQuery(); 
	        
	        while (connect.rs.next()) {
	            String cupName = connect.rs.getString("CupName");
	            int cupPrice = connect.rs.getInt("CupPrice");
	            int quantity = connect.rs.getInt("Quantity");
	            int total = connect.rs.getInt("Total");
	            totalPrice += total;
	            cart.add(new Cart(cupName, cupPrice, quantity, total));

	        }
	        totalPriceLbl.setText("Total Price: " + totalPrice);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void refreshTable() {
		getData();
		ObservableList<Cart> regObs = FXCollections.observableArrayList(cart);
		table.setItems(regObs);		
		
	}
	
	
	public CartPage() {
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
