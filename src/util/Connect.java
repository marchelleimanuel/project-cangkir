package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import view.LoginPage;
import view.homepage.Cart;
import view.homepage.user.CartPage;

public class Connect {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "cangkir";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	private Connection con;
	private PreparedStatement ps;
	private Statement st;
	private static Connect connect;
	
	public static Connect getInstance() {
		if(connect == null) return new Connect();
		return connect;
	}
 	
	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void execUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValidAccount(String username, String password) {
	    try {
	        String query = "SELECT * FROM msuser WHERE Username = ? AND UserPassword = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, password);

	        ResultSet result = preparedStatement.executeQuery();

	        return result.next(); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean isUsernameExist(String username) {
	    try {
	        String query = "SELECT * FROM msuser WHERE Username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);

	        ResultSet result = preparedStatement.executeQuery();

	        return result.next(); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean isEmailExist(String email) {
	    try {
	        String query = "SELECT * FROM msuser WHERE UserEmail = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, email);

	        ResultSet result = preparedStatement.executeQuery();

	        return result.next(); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public String checkRole(String username) {
	    try {
	        String query = "SELECT UserRole FROM msuser WHERE Username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);

	        ResultSet result = preparedStatement.executeQuery();

	        if(result.next()) {
	        	return result.getString("UserRole"); 
	        }
	        else {
	        	return null;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	    
	}

	public void addDataUserHomePage(String pengguna, String namaCup, int quantity) {
	    String username = pengguna;
	    String cupName = namaCup;

	    String getUserID = "SELECT userID FROM msuser WHERE Username = ?";
	    String getCupID = "SELECT cupID FROM mscup WHERE CupName = ?";
	    
	    try {
	        PreparedStatement getUserIDStatement = con.prepareStatement(getUserID);
	        getUserIDStatement.setString(1, username);
	        ResultSet userResult = getUserIDStatement.executeQuery();

	        PreparedStatement getCupIDStatement = con.prepareStatement(getCupID);
	        getCupIDStatement.setString(1, cupName);
	        ResultSet cupResult = getCupIDStatement.executeQuery();

	        String userID = null;
	        String cupID = null;

	        if (userResult.next()) {
	            userID = userResult.getString("userID");
	        }

	        if (cupResult.next()) {
	            cupID = cupResult.getString("cupID");
	        }
	        String checkCartItemQuery = "SELECT * FROM cart WHERE userID = ? AND cupID = ?";
	        PreparedStatement checkCartItemStatement = con.prepareStatement(checkCartItemQuery);
	        checkCartItemStatement.setString(1, userID);
	        checkCartItemStatement.setString(2, cupID);
	        ResultSet cartResult = checkCartItemStatement.executeQuery();

	        if (cartResult.next()) {
	            int existingQuantity = cartResult.getInt("quantity");
	            int newQuantity = existingQuantity + quantity;
	            String updateCartItemQuery = "UPDATE cart SET quantity = ? WHERE userID = ? AND cupID = ?";
	            PreparedStatement updateCartItemStatement = con.prepareStatement(updateCartItemQuery);
	            updateCartItemStatement.setInt(1, newQuantity);
	            updateCartItemStatement.setString(2, userID);
	            updateCartItemStatement.setString(3, cupID);
	            updateCartItemStatement.executeUpdate();
	        } else {
	            String insertCartItemQuery = "INSERT INTO cart (userID, cupID, quantity) VALUES (?, ?, ?)";
	            PreparedStatement insertCartItemStatement = con.prepareStatement(insertCartItemQuery);
	            insertCartItemStatement.setString(1, userID);
	            insertCartItemStatement.setString(2, cupID);
	            insertCartItemStatement.setInt(3, quantity);
	            insertCartItemStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public PreparedStatement prepareStatement(String query) {
		try {
			ps = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	public void clearTableCart() {
		CartPage.table.getItems().clear();
		
		String username = LoginPage.username;
		String getUserID = "SELECT UserID FROM msuser WHERE Username = ?";
		
		String query = "DELETE FROM cart WHERE UserID = ?";
		
		try {
			PreparedStatement preparedStatement = con.prepareStatement(getUserID);
			preparedStatement.setString(1, username);
			ResultSet userIDResult = preparedStatement.executeQuery();
			
			String userId = null;
			
			if(userIDResult.next()) {
				userId = userIDResult.getString("UserID");
			}
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, userId);
			preparedStatement.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addCup(String cupID, String cupName, int cupPrice) {
		
		String cupId = cupID;
		
		String query = "INSERT INTO mscup (CupID, CupName, CupPrice) VALUES (?, ?, ?)";
		
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, cupId);
			preparedStatement.setString(2, cupName);
			preparedStatement.setInt(3, cupPrice);
			preparedStatement.executeUpdate();

		} catch(SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public boolean isCupNameExist(String cupName) {
	    try {
	        String query = "SELECT * FROM mscup WHERE CupName = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, cupName);

	        ResultSet result = preparedStatement.executeQuery();

	        return result.next(); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	

	public void addDataToTransactionHeader(String transactionId, String pengguna, String courier, String currDate, int deliveryInsurance) {
		String transactionID = transactionId;
	    String username = pengguna;
	    String courierName = courier;
	    String transactionDate = currDate;
	    int useDeliveryInsurance = deliveryInsurance;
	    

	    String getUserID = "SELECT userID FROM msuser WHERE Username = ?";
	    String getCourierID = "SELECT courierID FROM mscourier WHERE CourierName = ?";
	    
	    
	    try {
	        PreparedStatement getUserIDStatement = con.prepareStatement(getUserID);
	        getUserIDStatement.setString(1, username);
	        ResultSet userResult = getUserIDStatement.executeQuery();

	        PreparedStatement getCourierIDStatement = con.prepareStatement(getCourierID);
	        getCourierIDStatement.setString(1, courierName);
	        ResultSet courierResult = getCourierIDStatement.executeQuery();
	        
	        String userID = null;
	        String courierID = null;

	        if (userResult.next()) {
	            userID = userResult.getString("userID");
//	            System.out.println(userID);
	        }

	        if (courierResult.next()) {
	            courierID = courierResult.getString("courierID");
//	            System.out.println(courierID);
	        }
	        
	        String query = "INSERT INTO transactionheader (TransactionID, UserID, CourierID, TransactionDate, UseDeliveryInsurance) " +
	                "VALUES (?, ?, ?, ?, ?)";
	        
		     PreparedStatement preparedStatement = con.prepareStatement(query);
		     preparedStatement.setString(1, transactionID);
		     preparedStatement.setString(2, userID);
		     preparedStatement.setString(3, courierID);
		     preparedStatement.setString(4, transactionDate);
		     preparedStatement.setInt(5, useDeliveryInsurance);
	
		     preparedStatement.executeUpdate();
		 } catch (SQLException e) {
		     e.printStackTrace();
		 }    
	}
	
	public void addDataToTransactionDetails(String username, String transactionDetailsID, Vector<Cart> items) {
        for (Cart item : items) {
            String cupName = item.getCupName();
            String getCupID = "SELECT CupID FROM mscup WHERE CupName = ?";
            String getQuantity = "SELECT Quantity FROM cart c " +
                    "JOIN msuser mu ON c.UserID = mu.UserID " +
                    "WHERE CupID = ? AND Username = ?";
            try {
                PreparedStatement getCupIDStatement = con.prepareStatement(getCupID);
                getCupIDStatement.setString(1, cupName);
                ResultSet cupIDResult = getCupIDStatement.executeQuery();

                String cupID = null;

                if (cupIDResult.next()) {
                    cupID = cupIDResult.getString("CupID");
                }

                PreparedStatement getQuantityStatement = con.prepareStatement(getQuantity);
                getQuantityStatement.setString(1, cupID);
                getQuantityStatement.setString(2, username);
                ResultSet quantityResult = getQuantityStatement.executeQuery();

                int quantity = 0;
                if (quantityResult.next()) {
                    quantity = quantityResult.getInt("Quantity");
                }

                String query2 = "INSERT INTO transactiondetail " +
                        "(TransactionID, CupID, Quantity) " +
                        "VALUES (?, ?, ?)";
                
                PreparedStatement preparedStatement = con.prepareStatement(query2);
                preparedStatement.setString(1, transactionDetailsID);
                preparedStatement.setString(2, cupID);
                preparedStatement.setInt(3, quantity);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	







	



	    

}
