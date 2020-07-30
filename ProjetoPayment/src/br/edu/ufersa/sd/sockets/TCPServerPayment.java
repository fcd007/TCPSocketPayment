package br.edu.ufersa.sd.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class TCPServerPayment {
	
	public static void main(String[] args) throws Exception {
		
		
		try(ServerSocket socket = new ServerSocket(3400)) {
		
			//criar servidor para ficar em loop para aguardar os recebimentos de requisições
			while (true) {
				
				//aguarda conexão de clientes
				System.out.println("Servidor: aguardando requisições...");
				Socket clientSocket = socket.accept();
				
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String type = in.readLine(); //ver tipo de requisição solicitada pelo cliente
				
				if (type.equals("PAGAMENTO") ) {
					ProcessPayment(in, out);
				}else {
					//processar outros tipos de requisições
				}
				
			}
		}
	}
	
	private static void ProcessPayment(BufferedReader in, PrintWriter out) {
		
			int qtde;
			try {
				qtde = Integer.parseInt(in.readLine());
				
			}catch (IOException e) {
				out.println("Falha ao ler quantidade parcelas " + e.getMessage());
				return;
			}
			
			for (int i = 0; i < qtde; i++) {
				try {
					String payment = in.readLine();
					String[] tokens = payment.split(";");
					
					String name = tokens[0];
					String numberCard = tokens[1];
					YearMonth dateValidCard = YearMonth.parse(tokens[2], DateTimeFormatter.ofPattern("MM/yyyy"));
					int numParcel = Integer.parseInt(tokens[3]);
					double value = Double.parseDouble(tokens[4]);
					
					System.out.println("Pagamento recebido (" + (i + 1) + ")");
					System.out.println("Nome Completo: " + name);
					System.out.println("Número do cartão: " + numberCard);
					System.out.println("Data validade cartão: " + dateValidCard);
					System.out.println("Número de parcelas : " + numParcel);
					System.out.println("Valor da compra: " + NumberFormat.getCurrencyInstance().format(value));
					
					out.println("OK");
				} catch (IOException e) {
					out.println("Falha" + e.getMessage());
				}
			}
	}
}




