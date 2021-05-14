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
	var status = validateUserForm();
	
	if (status != true)
	{
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
	
	// If valid------------------------
	var type = ($("#hidUserIDSave").val() == "") ? "POST" : "PUT";
	$.ajax(
	{
		url : "PaymentsAPI",
		type : type,
		data : $("#formUser").serialize(),
		dataType : "text",
		
		complete : function(response, status)
		{
			onUserSaveComplete(response.responseText, status);
		}
	});
});

// UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	$("#hidUserIDSave").val($(this).data("userid"));
	$("#cardNumber").val($(this).closest("tr").find('td:eq(2)').text());
	$("#CVV").val($(this).closest("tr").find('td:eq(3)').text());
	$("#expDate").val($(this).closest("tr").find('td:eq(4)').text());
});

function onUserSaveComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divPaymentsGrid").html(resultSet.data);
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
		$("#hidUserIDSave").val("");
		$("#formUser")[0].reset();
}

$(document).on("click", ".btnRemove", function(event)
{
	$.ajax(
	{
		url : "PaymentsAPI",
		type : "DELETE",
		data : "userId=" + $(this).data("userid"),
		dataType : "text",
		complete : function(response, status)
		{
			onUserDeleteComplete(response.responseText, status);
		}
	});
});

function onUserDeleteComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
	{
		$("#alertSuccess").text("Successfully deleted.");
		$("#alertSuccess").show();
		$("#divPaymentsGrid").html(resultSet.data);
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
function validateUserForm()
{
	// CARD NUMBER
	if ($("#cardNumber").val().trim() == "")
	{
		return "Insert card number.";
	}
	// CVV
	if ($("#CVV").val().trim() == "")
	{
		return "Insert CVV.";
	}

	// EXPIRATION DATE-------------------------------
	if ($("#expDate").val().trim() == "")
	{
		return "Insert expiration date.";
	}
	
	// CARD NUMBER IS NUMERICAL VALUE
	var cardNumber = $("#cardNumber").val().trim();
	if (!$.isNumeric(cardNumber))
	{
		return "Insert a numerical value for card number.";
	}
	
	// CVV IS NUMERICAL VALUE
	var cvv = $("#CVV").val().trim();
	if (!$.isNumeric(cvv))
	{
		return "Insert a numerical value for CVV.";
	}
			
	return true;
}