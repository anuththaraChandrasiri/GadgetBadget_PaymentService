<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.Payment"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment service - User payments</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.2.1.min.js"></script>
<script src="Components/payments.js"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col">
				
				<h1>User payment details management</h1>
				<hr>
				
				<form id="formUser" name="formUser" method="post" action="UserPayment.jsp">
					Card number :
					<input id="cardNumber" name="cardNumber" type="text" class="form-control form-control-sm">
					<br> CVV :
					<input id="cvv" name="cvv" type="text" class="form-control form-control-sm">
					<br> Expiring date :
					<input id="expDate" name="expDate" type="text" class="form-control form-control-sm">
					<br>
					<input id="btnSave" name="btnSave" type="button" value="Save" class="btn btn-primary">
					<input type="hidden" id="hidUserIDSave" name="hidUserIDSave" value="">
				</form>
					
				<div id="alertSuccess" class="alert alert-success"></div>
				<div id="alertError" class="alert alert-danger"></div>
				
				<br>
				<div id="divItemsGrid">
					<%
						Payment paymentObj= new Payment();
						out.print(paymentObj.readPaymentDetails());
					%>
				</div>
				
			</div>
		</div>
	</div>	
</body>
</html>