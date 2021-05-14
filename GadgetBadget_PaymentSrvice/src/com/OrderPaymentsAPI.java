package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OrderPaymentsAPI
 */
@WebServlet("/OrderPaymentsAPI")
public class OrderPaymentsAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Payment paymentObj = new Payment();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderPaymentsAPI() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String output = paymentObj.insertOrderPaymentRecord(Integer.parseInt(request.getParameter("projectId")),
															Integer.parseInt(request.getParameter("orderId")),
															Integer.parseInt(request.getParameter("researcherId")),
															Float.parseFloat(request.getParameter("amount")),
															request.getParameter("paymentStatus"));
			
		response.getWriter().write(output);;
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Map paras = getParasMap(request);
		
		String output = paymentObj.updateOrderPaymentDetailsRecord(Integer.parseInt(paras.get("hidOrderPaymentIDSave").toString()),
																   Integer.parseInt(paras.get("projectId").toString()),
																   Integer.parseInt(paras.get("orderId").toString()),
																   Integer.parseInt(paras.get("researcherId").toString()),
																   Float.parseFloat(paras.get("amount").toString()),
																   paras.get("paymentStatus").toString());
		
		response.getWriter().write(output);
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map paras = getParasMap(request);
		
		String output = paymentObj.deleteOrderPaymentRecord(paras.get("oPaymentId").toString());
		
		response.getWriter().write(output);		
		
	}
	
	// Convert request parameters to a Map
	private static Map getParasMap(HttpServletRequest request) {
			
		Map<String, String> map = new HashMap<String, String>();
			
		try
		{
			Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
				
			String queryString = scanner.hasNext() ?
			scanner.useDelimiter("\\A").next() : "";
				
			scanner.close();
				
			String[] params = queryString.split("&");
				
			for (String param : params) {
				String[] p = param.split("=");
				map.put(p[0], p[1]);
			}
		}
		catch (Exception e)
		{
		}
		return map;
	}

}
