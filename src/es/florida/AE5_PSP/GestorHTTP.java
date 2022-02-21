package es.florida.AE5_PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GestorHTTP implements HttpHandler {
	
	int temperaturaActual=15;
	int temperaturaTermostato=15;
	
	public void handle(HttpExchange httpExchange) throws IOException {
		String requestParamValue=null;
		if("GET".equals(httpExchange.getRequestMethod())) {
		requestParamValue = handleGetRequest(httpExchange);
		handleGETResponse(httpExchange,requestParamValue);
		} else if ("POST".equals(httpExchange.getRequestMethod())) {
		requestParamValue = handlePostRequest(httpExchange);
		try {
			handlePOSTResponse(httpExchange,requestParamValue);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		}
	
	public int RegularTemperatura() {
		Scanner teclado = new Scanner(System.in);
		System.out.print("La temperatura del Termostato es de 15º Deseas subir o bajar la temperatura? (s/b)");
		String temp = teclado.next();
		if(temp=="s") {
			temperaturaTermostato++;
		}else {
			temperaturaTermostato=temperaturaTermostato-1;
		}
		return temperaturaTermostato;
	}
	
	private void handleGETResponse(HttpExchange httpExchange, String temperaturaActual) throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();
		temperaturaActual="15";
		int temp = Integer.parseInt(temperaturaActual);
		String htmlResponse = "<html><body>Temperatura actual "+ temp + "</body></html>";
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
		}
	
	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
		}
	
	
	private String handlePostRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo POST :" + httpExchange.getRequestURI().toString());
		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		}catch( IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
		}
	
	private void handlePOSTResponse(HttpExchange httpExchange, String tempTermostato) throws IOException, InterruptedException {
		OutputStream outputStream = httpExchange.getResponseBody();
		tempTermostato="20";
		String htmlResponse = "Temperatura que debe alcanzar el Termostato " + tempTermostato + " -> Se procesara por parte del servidor";
		int setTemperatura=Integer.parseInt(tempTermostato);
		if(temperaturaTermostato != setTemperatura) {
			RegularTemperatura();
			Thread.sleep(5);
		}
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
		}
	
	
}
