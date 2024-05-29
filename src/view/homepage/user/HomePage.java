package view.homepage.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;
import util.Connect;
import view.LoginPage;
import view.homepage.Cart;
import view.homepage.Cup;
import view.homepage.user.CartPage;

public class HomePage extends Application{

	private Connect connect = Connect.getInstance();
	private Scene sc;
	private Stage stage;
	private BorderPane bp;
	private VBox vb,vb1;
	
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem homeMenu, cartMenu, logOutMenu;
	
	private Label tableLbl, cupNameLbl, priceLbl;
	private Spinner<Integer> amount; 
	
	private Button addToCartBtn;
	
	public static TableView<Cup> table;
	public static Vector<Cup> cups;
	public static String cupName;
	public static int cupPrice;
	public static int quantity;
	
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
		
		tableLbl = new Label("Cup List");
		cupNameLbl = new Label("Cup Name: ");
		priceLbl = new Label("Price: ");
		
		amount = new Spinner<>(1, 20, 1);
		
		addToCartBtn = new Button("Add to Cart");
		
		cups = new Vector<>();
	}
	
	public void setLayout() {
		bp.setTop(menuBar);
		bp.setLeft(vb);
		bp.setRight(vb1);
		
		
		
	}
	
	public void addComponent() {
		menuBar.getMenus().add(menu);
		menu.getItems().addAll(homeMenu, cartMenu, logOutMenu);
		
		vb.getChildren().addAll(tableLbl, table);
		vb1.getChildren().addAll(cupNameLbl, amount, priceLbl, addToCartBtn);
			
		TableColumn<Cup, String> cupName = new TableColumn<>("Cup Name");
		cupName.setCellValueFactory(new PropertyValueFactory<>("name"));
		cupName.setMinWidth((bp.getWidth()/2) / 2);
		
		TableColumn<Cup, Integer> cupPrice = new TableColumn<>("Cup Price");
		cupPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		cupPrice.setMinWidth((bp.getWidth()/2) / 2);
		
		table.getColumns().addAll(cupName, cupPrice);
		
		refreshTable();
	}
	
	public void setAlignment() {
		addToCartBtn.setPadding(new Insets(10, 30, 10, 30));
		vb.setPadding(new Insets(10));
		
		vb.setAlignment(Pos.BOTTOM_LEFT);
		
		BorderPane.setMargin(vb1, new Insets(bp.getHeight()/2, 0 , 0 , 0));
		VBox.setMargin(tableLbl, new Insets(0,0,10,0));

		vb.setMinWidth(bp.getWidth()/2);
		vb1.setMinWidth(bp.getWidth()/2);	
		
	}
	
	public void editComponent() {
		tableLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		cupNameLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		priceLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
	}
	
	
	public void eventHandler() {
		
		addToCartBtn.setOnAction(e -> {
			if(e.getSource() == addToCartBtn) {
				if(table.getSelectionModel().isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cart Error");
					alert.setContentText("Please select a cup to be added");	
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					TableSelectionModel<Cup> tableSelectionModel = table.getSelectionModel();
					tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
					Cup cup = tableSelectionModel.getSelectedItem();
					
					
					String name = cup.getName();
					String username = LoginPage.username;
					int price = cup.getPrice();
					int quantity = amount.getValue();
					int total = price * quantity;
					
					HomePage.cupName = cup.getName();
					HomePage.cupPrice = cup.getPrice();
					HomePage.quantity = amount.getValue();
					
	            	connect.addDataUserHomePage(username, name, quantity);
					
	            	refreshTable();
		            
					alert.setHeaderText("Cart Info");
					alert.setContentText("Item Successfully added to cart!");	
					alert.showAndWait();			
				}
				
			}
			
			
		});
		
		homeMenu.setOnAction(e -> {
			HomePage home = new HomePage();
			try {
				home.start(stage);
			} catch(Exception ex) {
				
			}
		});
		
		cartMenu.setOnAction(e -> {
			CartPage cart = new CartPage();
			try {
				cart.start(stage);
			} catch(Exception ex) {
				
			}
			
		});
		
		logOutMenu.setOnAction(e -> {
			LoginPage logOut = new LoginPage();
			try {
				logOut.start(stage);
			} catch (Exception e2) {
			}
		});
		
		
		table.setOnMouseClicked(e -> {
			amount.getValueFactory().setValue(1);
			TableSelectionModel<Cup> tableSelectionModel = table.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			Cup cup = tableSelectionModel.getSelectedItem();
			if(cup != null) {
				int price = cup.getPrice() * amount.getValue();
				
				cupNameLbl.setText("Cup Name: " + cup.getName());
				priceLbl.setText("Price: " + price);
			}
		});
		
		amount.valueProperty().addListener((observable, oldValue, newValue) -> {
		    Cup cup = table.getSelectionModel().getSelectedItem();
		    
		    if(cup != null) {
		    	int newPrice = cup.getPrice() * newValue.intValue();
		    	priceLbl.setText("Price: " + newPrice);		    	
		    }
		});
		
		
	}
	
	
	public void getData() {
		cups.removeAllElements();
		
		String query = "SELECT * FROM mscup";
		connect.rs = connect.execQuery(query);
		
		try {
			while(connect.rs.next()) {
				String name = connect.rs.getString("CupName");
				int price = connect.rs.getInt("CupPrice");
				cups.add(new Cup(name,price));	
			}
			
		} catch (Exception e) {
					}
		
	}
	
	public void refreshTable() {
		getData(); 
		ObservableList<Cup> regObs = FXCollections.observableArrayList(cups);
		table.setItems(regObs);		
	}
	
	public HomePage() {
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
