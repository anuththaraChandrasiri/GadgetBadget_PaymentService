$(document).ready(function()
{
	if ($("#alertSuccess").text().trim() == "")
	{
		$("#alertSuccess").hide();
	}
	$("#alertError").hide();
});

// SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();
	
	// Form validation-------------------
	var status = validateOrderForm();
	
	if (status != true)
	{
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	
	// If valid------------------------
	var type = ($("#hidOrderPaymentIDSave").val() == "") ? "POST" : "PUT";
	$.ajax(
	{
		url : "OrderPaymentsAPI",
		type : type,
		data : $("#formOrder").serialize(),
		dataType : "text",
		
		complete : function(response, status)
		{
			onOrderSaveComplete(response.responseText, status);
		}
	});
});

// UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	$("#hidOrderPaymentIDSave").val($(this).data("oid"));
	$("#projectId").val($(this).closest("tr").find('td:eq(0)').text());
	$("#orderId").val($(this).closest("tr").find('td:eq(1)').text());
	$("#researcherId").val($(this).closest("tr").find('td:eq(2)').text());
	$("#amount").val($(this).closest("tr").find('td:eq(3)').text());
	$("#paymentStatus").val($(this).closest("tr").find('td:eq(4)').text());
});

function onOrderSaveComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divOrderPaymentsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
		} else if (status == "error")
		{
			$("#alertError").text("Error while saving.");
			$("#alertError").show();
		} else
		{
			$("#alertError").text("Unknown error while saving..");
			$("#alertError").show();
		}
		$("#hidOrderPaymentIDSave").val("");
		$("#formOrder")[0].reset();
}

$(document).on("click", ".btnRemove", function(event)
{
	$.ajax(
	{
		url : "OrderPaymentsAPI",
		type : "DELETE",
		data : "oPaymentId=" + $(this).data("oid"),
		dataType : "text",
		complete : function(response, status)
		{
			onOrderDeleteComplete(response.responseText, status);
		}
	});
});

function onOrderDeleteComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
	{
		$("#alertSuccess").text("Successfully deleted.");
		$("#alertSuccess").show();
		$("#divOrderPaymentsGrid").html(resultSet.data);
	} else if (resultSet.status.trim() == "error")
	{
		$("#alertError").text(resultSet.data);
		$("#alertError").show();
	}
	} else if (status == "error")
	{
		$("#alertError").text("Error while deleting.");
		$("#alertError").show();
	} else
	{
		$("#alertError").text("Unknown error while deleting..");
		$("#alertError").show();
	}
}

// CLIENT-MODEL================================================================
function validateOrderForm()
{
	// PROJECT ID
	if ($("#projectId").val().trim() == "")
	{
		return "Insert the project ID.";
	}
	// ORDER ID
	if ($("#orderId").val().trim() == "")
	{
		return "Insert the order ID.";
	}

	// RESEARCHER ID
	if ($("#researcherId").val().trim() == "")
	{
		return "Insert the researcher ID.";
	}
	
	// AMOUNT
	if ($("#amount").val().trim() == "")
	{
		return "Insert the amount.";
	}
	
	// PAYMENT STATUS
	if ($("#paymentStatus").val().trim() == "")
	{
		return "Insert the payment status.";
	}
	
	// IS NUMERICAL VALUE
	var amount = $("#amount").val().trim();
	if (!$.isNumeric(amount))
	{
		return "Insert a numerical value for the amount.";
	}
		
	// convert to decimal price
	$("#amount").val(parseFloat(amount).toFixed(2));
				
	return true;
}