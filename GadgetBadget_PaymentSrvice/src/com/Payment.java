package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Payment {
	
	//A common method to connect to the database
	private Connection connect() {
						
		Connection con = null;
						
		try	{
		
			Class.forName("com.mysql.jdbc.Driver");
						
			//Providing correct details to connect : DBServer/DBName, user name, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/payment_service", "root", "");
			
			System.out.println("Successfully connected!");
					
		}catch (Exception e){
							
			e.printStackTrace();
		}
						
		return con;		
	}
			
	//User payment details methods----------------------------------------------------------------------------------------------------------
		
	//Method to read Payment details
	public String readPaymentDetails() {
						
		String output = "";
						
		try {
						
			Connection con = connect();
						
			if(con == null) 
				return "Error while connecting to the database for reading.";
							
			//Preparing the HTML table, which is to be displayed
			output = "<table border = '1'>"
					+ "<tr><th>User ID</th>"
					+ "<th>Name</th>"
					+ "<th>Card number</th>"
					+ "<th>CVV</th>"
					+ "<th>Expiration date</th>"
					+ "<th>Update</th><tr>";
					
			String query = "select userId, firstName, lastName, cardNumber, CVV, expDate from user";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
							
			//Iterate through the rows in the result set
			while(rs.next()) {
							
				Integer userID = rs.getInt("userId");
				String Fname = rs.getString("firstName");
				String Lname = rs.getString("lastName");
				String cardNumber = rs.getString("cardNumber");
				Integer CVV = rs.getInt("CVV");
				String expDate = rs.getString("expDate");
				String name = Fname + " " + Lname ;
							
				// Add into the HTML table
				output += "<tr><td>" + userID + "</td>";
				output += "<td>" + name + "</td>";
				output += "<td>" + cardNumber + "</td>";
				output += "<td>" + CVV + "</td>";
				output += "<td>" + expDate + "</td>";
				
				// button
				output += "<td><input name='btnUpdate' type='button' value='Update' "
				+ "class='btnUpdate btn btn-secondary' data-userid='" + userID + "'></td>";
			}	
							
		con.close();
							
		//Completing the HTML table
		output += "</table>";					
						
		} catch (SQLException e) {
							
			output += "Error while reading the payment details.";
			System.err.println(e.getMessage());
		}		
						
		return output ;		
	}
			
	//Method to read a specific user's payment details
	public String readUserPaymentDetails(int userId) {
						
		String output = "";
							
		try {
							
			Connection con = connect();
								
			if(con == null) 
				return "Error while connecting to the database for reading.";
								
			//Preparing the HTML table, which is to be displayed
			output = "<table border = '1'>"
					+ "<tr><th>User ID</th>"
					+ "<th>Name</th>"
					+ "<th>Card number</th>"
					+ "<th>CVV</th>"
					+ "<th>Expiration date</th>"
					+ "<th>Update</th>"
					+ "<th>Confirm</th><tr>";
						
			String query = "select userId, firstName, lastName, cardNumber, CVV, expDate from user where userId = " + userId;
				
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);	
								
			if(rs.next()) {
								
				Integer userID = rs.getInt("userId");
				String Fname = rs.getString("firstName");
				String Lname = rs.getString("lastName");
				String cardNumber = rs.getString("cardNumber");
				Integer CVV = rs.getInt("CVV");
				String expDate = rs.getString("expDate");
				String name = Fname + " " + Lname ;
												
				// Add into the HTML table
				output += "<tr><td>" + userID + "</td>";
				output += "<td>" + name + "</td>";
				output += "<td>" + cardNumber + "</td>";
				output += "<td>" + CVV + "</td>";
				output += "<td>" + expDate + "</td>";
								
				//Displaying the buttons
				//Displaying the buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' "
				+ "class='btnUpdate btn btn-secondary' data-userid='" + userID + "'></td>"
				+ "<input name = 'btnConfirm' type = 'button' value = 'Confirm'></td>";
							
			}	
								
		con.close();
							
		//Completing the HTML table
		output += "</table>";					
								
		} catch (SQLException e) {
								
			output += "Error while reading the user payment details.";
			System.err.println(e.getMessage());
		}		
							
		return output ;		
	}
		
	//Method to update a specific user's payment details
	public String updateUserPaymentDetailsRecord(String userId, String cardNumber, String CVV, String expDate) {
				
		String output = "";
			
		//Creating a Payment type object
		Payment payment = new Payment();
			
		//Assigning the output string value of the readUserPaymentDetails(String userId) to the result string variable 
		String result = payment.readUserPaymentDetails(Integer.parseInt(userId));
					
		//Checking whether the entered userId is existing in the database (Update, Delete buttons will be created only if there is a user to display)
		if(result.contains("Update")) {			
				
			try {
						
				Connection con = connect();
						
				if (con == null) 
					return "Error while connecting to the database for updating."; 
						
				// Creating a prepared statement
				String query = "update user set cardNumber = ?, CVV = ?, expDate = STR_TO_DATE(?,'%Y/%m/%d') WHERE userId = ?";
				
				PreparedStatement preparedStmt = con.prepareStatement(query);
						
				// Binding values
				preparedStmt.setString(1, cardNumber);
				preparedStmt.setInt(2, Integer.parseInt(CVV));
				preparedStmt.setString(3, expDate);
				preparedStmt.setInt(4, Integer.parseInt(userId));
							
				//Executing the statement
				preparedStmt.execute();
				con.close();
				
				String newUserPaymentDetailsRecords = readPaymentDetails();			
				
				output = "{\"status\":\"success\", \"data\": \"" + newUserPaymentDetailsRecords + "\"}";
			}
			catch (Exception e)	{

				output = "{\"status\":\"error\", \"data\": \"Updating the user payment details record and got an error.\"}";
				System.err.println(e.getMessage());
			}			
				
		}
			
		//Printing an error message since the entered userId does not exist in the database
		else
			output = "Error. Entered User ID does not exist.";
				
		return output;
	}
		
	//Order payment details methods----------------------------------------------------------------------------------------------------------

	//Method to read Order payment details
	public String readOrderPaymentDetails() {
				
		String output = "";
			
		try {
				
			Connection con = connect();
					
			if(con == null) 
				return "Error while connecting to the database for reading.";
					
			//Preparing the HTML table, which is to be displayed
			output = "<table border = '1'>"
					+ "<tr><th>Project ID</th>"
					+ "<th>Order ID</th>"
					+ "<th>Researcher ID</th>"
					+ "<th>Amount</th>"
					+ "<th>Payment status</th>"
					+ "<th>Update</th>"
					+ "<th>Remove</th><tr>";
					
			String query = "select * from order_payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
					
			//Iterate through the rows in the result set
			while(rs.next()) {
						
				Integer oPaymentId = rs.getInt("oPaymentId");
				Integer projectID = rs.getInt("pId");
				Integer orderID = rs.getInt("orderId");
				Integer researcherID = rs.getInt("researcherId");
				Float orderAmount = (float) rs.getInt("amount");
				String paymentStatus = rs.getString("paymentStatus");
						
				// Add into the HTML table
				output += "<tr><td>" + projectID + "</td>";
				output += "<td>" + orderID + "</td>";
				output += "<td>" + researcherID + "</td>";
				output += "<td>" + orderAmount + "</td>";
				output += "<td>" + paymentStatus + "</td>";
						
				// buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' "
				+ "class='btnUpdate btn btn-secondary' data-oid='" + oPaymentId + "'></td>"
				+ "<td><input name='btnRemove' type='button' value='Remove' "
				+ "class='btnRemove btn btn-danger' data-oid='" + oPaymentId + "'></td></tr>";				
			}	
						
			con.close();
						
			//Completing the HTML table
			output += "</table>";					
					
		} catch (SQLException e) {
					
			output += "Error while reading the order payment details.";
			System.err.println(e.getMessage());
		}		
				
		return output ;		
	}
		
	//Method to insert an Order payment details record
	public String insertOrderPaymentRecord(int pId, int orderId, int researcherId, float amount, String paymentStatus){
			
		String output = "";
			
		try{
				
			Connection con = connect();
				
			if (con == null)
				{return "Error while connecting to the database for inserting."; }
				
			// create a prepared statement
			String query = " insert into order_payment (`pId`, `orderId`, `researcherId`, `amount`, `paymentStatus`) values (?, ?, ?, ?, ?)";
				
			PreparedStatement preparedStmt = con.prepareStatement(query);
				
			// binding values
			preparedStmt.setInt(1, pId);
			preparedStmt.setInt(2, orderId);
			preparedStmt.setInt(3, researcherId);
			preparedStmt.setFloat(4, amount);
			preparedStmt.setString(5, paymentStatus);
		
			// execute the statement
			preparedStmt.execute();
				
			con.close();
				
			String newOrderPaymentDetailsRecords = readOrderPaymentDetails();			
			
			output = "{\"status\":\"success\", \"data\": \"" + newOrderPaymentDetailsRecords + "\"}";
			
		}
		catch (Exception e){
				
			output = "{\"status\":\"error\", \"data\": \"Error while inserting the item.\"}";
			System.err.println(e.getMessage());
		}
			
		return output;
	}
	
	//Method to update a specific order payment detail record
	public String updateOrderPaymentDetailsRecord(int orderPaymentId, int pId, int orderId, int researcherId, float amount, String paymentStatus) {
					
		String output = "";
						
		try {
							
			Connection con = connect();
							
			if (con == null) 
				return "Error while connecting to the database for updating."; 
							
			// Creating a prepared statement
			String query = "update order_payment set pId = ?, orderId = ?, researcherId = ?, amount = ?, paymentStatus = ? WHERE oPaymentId = ?";
				
			PreparedStatement preparedStmt = con.prepareStatement(query);
							
			// Binding values
			preparedStmt.setInt(1, pId);
			preparedStmt.setInt(2, orderId);
			preparedStmt.setInt(3, researcherId);
			preparedStmt.setFloat(4, amount);
			preparedStmt.setString(5, paymentStatus);
			preparedStmt.setInt(6, orderPaymentId);
												
			//Executing the statement
			preparedStmt.execute();
			con.close();
					
			String newOrderPaymentDetailsRecords = readOrderPaymentDetails();			
					
			output = "{\"status\":\"success\", \"data\": \"" + newOrderPaymentDetailsRecords + "\"}";
				
		}
		catch (Exception e)	{

			output = "{\"status\":\"error\", \"data\": \"Updating the order payment details record and got an error.\"}";
			System.err.println(e.getMessage());
		}			
		
		return output;
	}			
		
	//Method to delete an Order payment details record
	public String deleteOrderPaymentRecord(String orderPaymentID){
			
		String output = "";
		int orderPaymentId = Integer.parseInt(orderPaymentID);
			
		try	{
				
			Connection con = connect();
				
			if (con == null){
					
				return "Error while connecting to the database for deleting.";
			}
				
			// Creating a prepared statement
			String query = "delete from order_payment where oPaymentId = ?";
		
			PreparedStatement preparedStmt = con.prepareStatement(query);
				
			// Binding values
			preparedStmt.setInt(1, orderPaymentId);
				
			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String newOrderPaymentDetailsRecords = readOrderPaymentDetails();			
			
			output = "{\"status\":\"success\", \"data\": \"" + newOrderPaymentDetailsRecords + "\"}";
			
		}
		catch (Exception e)
		{
			output = "Error while deleting an order payment record.";
			System.err.println(e.getMessage());
		}
		return output;
	}
		
	//Fund payment details methods------------------------------------------------------------------------------------------------------------
		
	//Method to read Fund payment details
	public String readFundPaymentDetails() {
						
		String output = "";
		String success = "Success";
						
		try {
						
			Connection con = connect();
							
			if(con == null) 
				return "Error while connecting to the database for reading.";
							
			//Preparing the HTML table, which is to be displayed
			output = "<table border = '1'>"
					+ "<tr><th>Fund payment ID</th>"
					+ "<th>Project ID</th>"
					+ "<th>Fund ID</th>"
					+ "<th>Researcher ID</th>"
					+ "<th>Amount</th>"
					+ "<th>Payment status</th>"
					+ "<th>Action</th><tr>";
						
			String query = "select * from fund_payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
						
			//Iterate through the rows in the result set
			while(rs.next()) {
							
				Integer fundPaymentID = rs.getInt("fPaymentId");
				Integer projectID = rs.getInt("pId");
				Integer fundID = rs.getInt("fundId");
				Integer researcherID = rs.getInt("researcherId");
				Float fundAmount = (float) rs.getInt("amount");
				String paymentStatus = rs.getString("paymentStatus");
							
				// Add into the HTML table
				output += "<tr><td>" +  fundPaymentID + "</td>";
				output += "<td>" + projectID + "</td>";
				output += "<td>" + fundID + "</td>";
				output += "<td>" + researcherID + "</td>";
				output += "<td>" + fundAmount + "</td>";
				output += "<td>" + paymentStatus + "</td>";
							
				//Displaying the relevant button
				if(paymentStatus.equalsIgnoreCase(success)) {
					
					output += "<td><input name='btnPay' type='button' value='Pay       ' "
							+ "class='btnUpdate btn btn-secondary' data-fundPaymentID='" + fundPaymentID + "'></td>";
								
				}
				else {
					
					output += "<td><input name='btnRemove' type='button' value='Remove' "
							+ "class='btnRemove btn btn-danger' data-fundPaymentID='" + fundPaymentID + "'></td></tr>";
						
				}
			}	
							
			con.close();
							
			//Completing the HTML table
			output += "</table>";					
						
		} catch (SQLException e) {
						
			output += "Error while reading the fund payment details.";
			System.err.println(e.getMessage());
		}		
						
		return output ;		
	}
			
	//Method to insert a Fund payment details record
	public String insertFundPaymentRecord(int pId, int fundId, int researcherId, float amount, String paymentStatus){
				
		String output = "";
				
		try{
					
			Connection con = connect();
					
			if (con == null)
				{return "Error while connecting to the database for inserting."; }
					
			// create a prepared statement
			String query = " insert into fund_payment (`pId`, `fundId`, `researcherId`, `amount`, `paymentStatus`) values (?, ?, ?, ?, ?)";
					
			PreparedStatement preparedStmt = con.prepareStatement(query);
					
			// binding values
			preparedStmt.setInt(1, pId);
			preparedStmt.setInt(2, fundId);
			preparedStmt.setInt(3, researcherId);
			preparedStmt.setFloat(4, amount);
			preparedStmt.setString(5, paymentStatus);
			
			// execute the statement
			preparedStmt.execute();
					
			con.close();
					
			output = "Fund payment record inserted successfully";
		}
		catch (Exception e){
					
			output = "Error while inserting the fund payment record.";
			System.err.println(e.getMessage());
		}
				
		return output;
	}
			
	//Method to delete a Fund payment details record
	public String deleteFundPaymentRecord(int fundPaymentId){
				
		String output = "";
				
		try	{
					
			Connection con = connect();
					
			if (con == null)					
				return "Error while connecting to the database for deleting.";
							
			// Creating a prepared statement
			String query = "delete from fund_payment where fPaymentId = ?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
					
			// Binding values
			preparedStmt.setInt(1, fundPaymentId);
					
			// execute the statement
			preparedStmt.execute();
			con.close();
					
			output = "Fund payment record deleted successfully";
		}
		catch (Exception e)
		{
			output = "Error while deleting a fund payment record.";
			System.err.println(e.getMessage());
		}
		return output;
	}
			
	//Researcher payment details methods----------------------------------------------------------------------------------------------------------
		
	//Method to read Researcher payment details
	public String readResearcherPaymentDetails() {
						
		String output = "";
		String success = "Success";
						
		try {
						
			Connection con = connect();
							
			if(con == null) 
				return "Error while connecting to the database for reading.";
							
			//Preparing the HTML table, which is to be displayed
			output = "<table border = '1'>"
					+ "<tr><th>Researcher payment ID</th>"
					+ "<th>Researcher ID</th>"
					+ "<th>Amount</th>"
					+ "<th>Payment status</th>"
					+ "<th>Action</th><tr>";
						
			String query = "select * from researcher_payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
				
			//Iterate through the rows in the result set
			while(rs.next()) {
							
				Integer researcherPaymentID = rs.getInt("rPaymentId");
				Integer researcherID = rs.getInt("researcherId");
				Float rPaymentAmount = (float) rs.getInt("amount");
				String paymentStatus = rs.getString("paymentStatus");
								
				// Add into the HTML table
				output += "<tr><td>" +  researcherPaymentID + "</td>";
				output += "<td>" + researcherID + "</td>";
				output += "<td>" + rPaymentAmount + "</td>";
				output += "<td>" + paymentStatus + "</td>";
							
				//Displaying the relevant button
				if(paymentStatus.equalsIgnoreCase(success)) {
								
					output += "<td>None</td>";
									
				}
				else {
								
					output += "<td><input name='btnRemove' type='button' value='Remove' "
							+ "class='btnRemove btn btn-danger' data-researcherPaymentID='" + researcherPaymentID + "'></td></tr>";
							
				}
			}	
								
			con.close();
								
			//Completing the HTML table
			output += "</table>";					
							
		} catch (SQLException e) {
							
			output += "Error while reading the researcher payment details.";
			System.err.println(e.getMessage());
		}		
						
		return output ;		
	}	
			
	//Method to insert a Researcher payment details record
	public String inserteResearcherPaymentRecord(int researcherId, float amount, String paymentStatus){
				
		String output = "";
					
		try{
					
			Connection con = connect();
					
			if (con == null)
				return "Error while connecting to the database for inserting."; 
					
			// create a prepared statement
			String query = " insert into researcher_payment (`researcherId`,`amount`, `paymentStatus`) values (?, ?, ?)";
				
			PreparedStatement preparedStmt = con.prepareStatement(query);
					
			// binding values
			preparedStmt.setInt(1, researcherId);
			preparedStmt.setFloat(2, (float) (amount*0.9));
			preparedStmt.setString(3, paymentStatus);
			
			// execute the statement
			preparedStmt.execute();
					
			con.close();
					
			output = "Researcher payment record inserted successfully";
		}
		catch (Exception e){
					
			output = "Error while inserting the researcher payment record.";
			System.err.println(e.getMessage());
		}
				
		return output;
	}
			
	//Method to delete a Researcher payment details record
	public String deleteResearcherPaymentRecord(int researcherPaymentId){
				
		String output = "";
				
		try	{
					
			Connection con = connect();
					
			if (con == null)				
				return "Error while connecting to the database for deleting.";
					
			// Creating a prepared statement
			String query = "delete from researcher_payment where rPaymentId = ?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
					
			// Binding values
			preparedStmt.setInt(1, researcherPaymentId);
					
			// execute the statement
			preparedStmt.execute();
			con.close();
					
			output = "Researcher payment record deleted successfully";
		}
		catch (Exception e)
		{
			output = "Error while deleting a researcher payment record.";
			System.err.println(e.getMessage());
		}
		return output;
	}	

}
