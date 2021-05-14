<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.Payment"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment service - Order payments</title>
<link rel="stylesheet" href="Views/bootstrap.min.css">
<script src="Components/jquery-3.2.1.min.js"></script>
<script src="Components/payments.js"></script>
</head>
</head>
<body>
<div class="container">
		<div class="row">
			<div class="col">
				
				<h1>Order payment management</h1>
				<hr>
				
				<form id="formOrder" name="formOrder" method="post" action="OrderPayment.jsp">
					Project ID :
					<input id="projectId" name="projectId" type="text" class="form-control form-control-sm">
					<br> Order ID :
					<input id="orderId" name="orderId" type="text" class="form-control form-control-sm">
					<br> Researcher ID :
					<input id="researcherId" name="researcherId" type="text" class="form-control form-control-sm">
					<br> Amount :
					<input id="amount" name="amount" type="text" class="form-control form-control-sm">
					<br>
					<br> Payment status :
					<input id="paymentStatus" name="paymentStatus" type="text" class="form-control form-control-sm">
					<br>
					<input id="btnSave" name="btnSave" type="button" value="Save" class="btn btn-primary">
					<input type="hidden" id="hidOrderPaymentIDSave" name="hidOrderPaymentSave" value="">
				</form>
					
				<div id="alertSuccess" class="alert alert-success"></div>
				<div id="alertError" class="alert alert-danger"></div>
				
				<br>
				<div id="divItemsGrid">
					<%
						Payment paymentObj= new Payment();
						out.print(paymentObj.readOrderPaymentDetails());
					%>
				</div>
				
			</div>
		</div>
	</div>	

</body>
</html>