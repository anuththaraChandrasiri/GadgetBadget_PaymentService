<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.Payment"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment service - Researcher payments</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.2.1.min.js"></script>
<script src="Components/payments.js"></script>
</head>
<body>
<div class="container">
		<div class="row">
			<div class="col">
				
				<h1>Researcher payment details management</h1>
				<hr>
				
				<form id="formResearcher" name="formResearcher" method="post" action="ResearcherPayment.jsp">
					Researcher ID:
					<input id="itemCode" name="itemCode" type="text" class="form-control form-control-sm">
					<br> Amount :
					<input id="itemName" name="itemName" type="text" class="form-control form-control-sm">
					<br> Payment status:
					<input id="itemPrice" name="itemPrice" type="text" class="form-control form-control-sm">
					<br>
					<input id="btnSave" name="btnSave" type="button" value="Save" class="btn btn-primary">
					<input type="hidden" id="hidResearcherPaymentIDSave" name="hidResearcherPaymentmIDSave" value="">
				</form>
					
				<div id="alertSuccess" class="alert alert-success"></div>
				<div id="alertError" class="alert alert-danger"></div>
				
				<br>
				<div id="divItemsGrid">
					<%
						Payment paymentObj= new Payment();
						out.print(paymentObj.readResearcherPaymentDetails());
					%>
				</div>
				
			</div>
		</div>
	</div>	

</body>
</html>