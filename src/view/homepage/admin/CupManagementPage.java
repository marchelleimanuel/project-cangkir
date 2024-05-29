package view.homepage.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.Connect;
import view.LoginPage;
import view.homepage.Cup;
import view.homepage.user.HomePage;

public class CupManagementPage extends Application{

	private Connect connect = Connect.getInstance();
	private Scene sc;
	private Stage stage;
	private BorderPane bp;
	private VBox vb,vb1;
	
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem cupManagementMenu, logOutMenu;
	
	private TableView<Cup> table;
	private Vector<Cup> cups;
	
	private Label tableLbl, cupNameLbl, priceLbl;
	private TextField cupNameTf, cupPriceTf;
	
	private Button addCupBtn, updatePriceBtn, removeCupBtn;
	
	public void init() {
		bp = new BorderPane();
		vb = new VBox();
		vb1 = new VBox(20);
		
		sc = new Scene(bp, 800,700);
		
		menuBar = new MenuBar();
		menu = new Menu("Menu");
		cupManagementMenu = new MenuItem("Cup Management");
		logOutMenu = new MenuItem("Log Out");
		
		table = new TableView<>();
		cups = new Vector<>();
		
		tableLbl = new Label("Cup Management");
		cupNameLbl = new Label("Cup Name");
		priceLbl = new Label("Cup Price");
		
		cupNameTf = new TextField();
		cupPriceTf = new TextField();
		
		addCupBtn = new Button("Add Cup");
		updatePriceBtn = new Button("Update Price");
		removeCupBtn = new Button("Remove Cup");
	}
	 
	public void setLayout() {
		bp.setTop(menuBar);
		bp.setLeft(vb);
		bp.setRight(vb1);
		
		
		
	}
	
	public void addComponent() {
		menuBar.getMenus().add(menu);
		menu.getItems().addAll(cupManagementMenu,logOutMenu);
		
		vb.getChildren().addAll(tableLbl, table);
		vb1.getChildren().addAll(cupNameLbl, cupNameTf, priceLbl, cupPriceTf, addCupBtn, updatePriceBtn, removeCupBtn);
	
		TableColumn<Cup, String> cupName = new TableColumn<>("Cup Name");
		cupName.setCellValueFactory(new PropertyValueFactory<>("name"));
		cupName.setMinWidth((bp.getWidth()/2) / 2);
		
		TableColumn<Cup, Integer> cupPrice = new TableColumn<>("Cup Price");
		cupPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		cupPrice.setMinWidth((bp.getWidth()/2) / 2);
		
		table.getColumns().addAll(cupName, cupPrice);
		
		cupNameTf.setPromptText("Input cup name here");
		cupPriceTf.setPromptText("Input cup price here");
		
		try {
			refreshTable();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setAlignment() {
		addCupBtn.setPadding(new Insets(15, 70,15, 70));
		updatePriceBtn.setPadding(new Insets(15, 60,15, 60));
		removeCupBtn.setPadding(new Insets(15, 60,15, 60));
		vb.setPadding(new Insets(10));
		vb1.setPadding(new Insets(40, 10 ,10, 10));
		
		vb.setAlignment(Pos.BOTTOM_LEFT);
		
		BorderPane.setMargin(vb1, new Insets(bp.getHeight()/3, 0 , 0 , 0));
		VBox.setMargin(tableLbl, new Insets(0,0,10,0));

		vb.setMinWidth(bp.getWidth()/2);
		vb1.setMinWidth(bp.getWidth()/2);	
		
		cupNameTf.setMaxWidth(bp.getWidth()/3);
		cupPriceTf.setMaxWidth(bp.getWidth()/3);
		
		
	}
	
	public void editComponent() {
		tableLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
	}
	
	
	public void eventHandler() {
		addCupBtn.setOnAction(e -> {
		    Alert alert = new Alert(AlertType.ERROR);
		    alert.setHeaderText("Cup Management");

		    String cupID = getCupID();
		    String cupName = cupNameTf.getText();
		    String cupPriceText = cupPriceTf.getText();
		    int cupPrice = 0;

		    boolean isCupNameValid = false;
		    boolean isPriceValid = false;

		    if (cupName.isEmpty()) {
		        alert.setContentText("Please fill out the cup name");
		        alert.showAndWait();
		        isCupNameValid = false;
		    } 
		    else if (cupPriceText.isEmpty()) {
		        alert.setContentText("Please enter the price");
		        alert.showAndWait();
		        isPriceValid = false;
		    }
		    else if (connect.isCupNameExist(cupName)) {
		        alert.setContentText("Cup already exists");
		        alert.showAndWait();
		        isCupNameValid = false;
		    }
		    else {
				try {
				    cupPrice = Integer.parseInt(cupPriceText);
				
				    if (cupPrice < 5000 || cupPrice > 1000000) {
				        alert.setContentText("Cup price must be between 5000 and 1000000 inclusive");
					    alert.showAndWait();
					    isPriceValid = false;
					    return;
				    }
				    isPriceValid = true;
				    isCupNameValid = true;
			    } catch (NumberFormatException ex) {
			        alert.setContentText("Invalid price format. Please enter a valid integer.");
			        alert.showAndWait();
			        isPriceValid = false;
			        return;
			    }
		    }

		    if (isCupNameValid == true && isPriceValid == true) {
		        Alert success = new Alert(AlertType.INFORMATION);
		        success.setHeaderText("Cup Management");
		        success.setContentText("Cup Successfully Added");
		        success.showAndWait();
		        table.getItems().add(new Cup(cupName, cupPrice));
		        connect.addCup(cupID, cupName, cupPrice);
		        refreshTable();
		        resetTextField();
		    }
		});
		
		removeCupBtn.setOnAction(e -> {
			if(table.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Cup Management");
				alert.setContentText("Please select a cup from the table to be deleted");	
				alert.showAndWait();
			}
			else {
				Cup selectedCup = table.getSelectionModel().getSelectedItem();
				String cupName = selectedCup.getName();
				String query = "DELETE FROM mscup WHERE CupName = ?";
				PreparedStatement ps = connect.prepareStatement(query);
				try {
					ps.setString(1, cupName);
		            ps.execute();
		            
		            table.getItems().remove(selectedCup);
				} catch (Exception e2) {
					e2.printStackTrace();
				}				
				
				refreshTable();
				resetTextField();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Cup Management");
				alert.setContentText("Cup Successfully Deleted");	
				alert.showAndWait();
			}
		});
		
		updatePriceBtn.setOnAction(e -> {
		    if (table.getSelectionModel().isEmpty()) {
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setHeaderText("Cup Management");
		        alert.setContentText("Please select a cup from the table to be updated");
		        alert.showAndWait();
		    } else {
		        String newPriceText = cupPriceTf.getText();
		        if (newPriceText.isEmpty()) {
		            Alert alert = new Alert(AlertType.ERROR);
		            alert.setHeaderText("Cup Management");
		            alert.setContentText("Please enter the new price");
		            alert.showAndWait();
		            return;
		        }

		        int newPrice;
		        try {
		            newPrice = Integer.parseInt(newPriceText);
		            if(newPrice < 5000 || newPrice > 1000000) {
		            	Alert alert = new Alert(AlertType.ERROR);
			            alert.setHeaderText("Cup Management");
			            alert.setContentText("Cup price must be 5000 - 1000000");
			            alert.showAndWait();
			            return;
		            }
		        } catch (NumberFormatException ex) {
		            Alert alert = new Alert(AlertType.ERROR);
		            alert.setHeaderText("Cup Management");
		            alert.setContentText("Invalid price format.");
		            alert.showAndWait();
		            return; 
		        }

		        Cup selectedCup = table.getSelectionModel().getSelectedItem();
		        String cupName = selectedCup.getName();

		        selectedCup.setPrice(newPrice);
		        table.refresh(); 

		        String query = "UPDATE mscup SET CupPrice = ? WHERE CupName = ?";
		        PreparedStatement ps = connect.prepareStatement(query);
		        try {
		            ps.setInt(1, newPrice);
		            ps.setString(2, cupName);
		            ps.executeUpdate();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }

		        resetTextField();

		        Alert success = new Alert(AlertType.INFORMATION);
		        success.setHeaderText("Cup Management");
		        success.setContentText("Cup Successfully Updated");
		        success.showAndWait();
		    }
		});
		
		table.setOnMouseClicked(e -> {
			TableSelectionModel<Cup> tableSelectionModel = table.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			Cup cup = tableSelectionModel.getSelectedItem();
			
			if(tableSelectionModel.getSelectedItem() == null) {
				Alert alert = new Alert(AlertType.ERROR);
		        alert.setHeaderText("Cup Management");
		        alert.setContentText("Please select a cup from the table to be updated");
		        alert.showAndWait();
			}
			else {
				cupNameTf.setText(cup.getName());
				String cupPrice = Integer.toString(cup.getPrice());
				cupPriceTf.setText(cupPrice);				
			}
		});
		
		
		logOutMenu.setOnAction(e -> {
			LoginPage logOut = new LoginPage();
			try {
				logOut.start(stage);
			} catch (Exception e2) {
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
	
	public int countCup() {
	    String query = "SELECT COUNT(*) FROM mscup";
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
	
	public String getCupID() {
	    String availableID = findAvailableCupID();
	    if (availableID != null) {
	        return availableID;
	    } else {
	        int cup = countCup();
	        int nextCup = cup + 1;
	        return "CU" + String.format("%03d", nextCup);
	    }
	}

	private String findAvailableCupID() {
	    String query = "SELECT CupID FROM mscup";
	    try {
	        ResultSet resultSet = connect.execQuery(query);
	        String lastCupID = null;

	        while (resultSet.next()) {
	            String currentCupID = resultSet.getString("CupID");

	            if (lastCupID != null) {
	                int lastNumber = Integer.parseInt(lastCupID.substring(2));
	                int currentNumber = Integer.parseInt(currentCupID.substring(2));

	                if (currentNumber - lastNumber > 1) {
	                    return "CU" + String.format("%03d", lastNumber + 1);
	                }
	            }

	            lastCupID = currentCupID;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public void resetTextField() {
		cupNameTf.setText("");
		cupPriceTf.setText("");
		
		table.getSelectionModel().clearSelection();
	}
	
	public CupManagementPage() {
		table = HomePage.table;
		cups = HomePage.cups;
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
