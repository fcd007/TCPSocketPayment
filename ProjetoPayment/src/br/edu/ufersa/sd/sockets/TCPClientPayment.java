package br.edu.ufersa.sd.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class TCPClientPayment {

	public static void main(String[] args) throws Exception{
		
		try(Socket socket = new Socket("localhost", 3400)){
			System.out.println("Cliente: Conectado ao servidor...");
			
			//vai fazer buffer das informações.
			PrintWriter  out = new PrintWriter(socket.getOutputStream(), true); //envio automático
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//quantidade de pagamento
			int qtde = 10;
			
			//protocolo usado para pagamento
			out.println("PAGAMENTO"); //recurso utilizado
			out.println(qtde); //quantidade de pagamento
			
			for (int i = 0; i < qtde; i++) {
				
				//formato de dados entrada cliente para servidor
				String status = sendPayment("Jose da silva", "2415871256451", YearMonth.of(2020, 7), 1, 502.00, in, out);
				
				if (status == null) {
					System.out.println("Pagamento: realizado com sucesso...");
				} else {
					System.out.println("Erro: " + status);
				}
			}
			
		}
	}
	
	/*Estrutura de pagamento
	 * String name
	 * String numberCard
	 * YearMonth dateValidCard
	 * int numParcel
	 * double value
	 * */
	private static String sendPayment(String name, String numberCard, YearMonth dateValidCard, int numParcel, double value, BufferedReader in, PrintWriter out) throws IOException {
		//formatando a string de retorno dos dados
		out.println(String.format("%s;%s;%s;%d;%s", name, numberCard, dateValidCard.format(DateTimeFormatter.ofPattern("MM/yyyy")), numParcel, String.valueOf(value)));
		
		String status = in.readLine();
		
		if (status.equals("OK")) {
			return null;
		}else {
			return status;
		}
	}
}
