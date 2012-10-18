package HttpClient;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.swing.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



public class ApacheHttpClientGet implements ActionListener{

	private JFrame frameHttpGet;
	private JPanel panelHttpGet;
	private JLabel labelHttpGet;
	private JTextField textHttpGet;
	
	private JButton buttonHttpGet;
	private static boolean startFlag;
	
	private FileWriter fileWriter;
	public ApacheHttpClientGet(String filename)throws IOException
	{
		startFlag=false;
		panelHttpGet=new JPanel(new FlowLayout(FlowLayout.LEFT));
		labelHttpGet=new JLabel("Web Link:");
		textHttpGet=new JTextField(15);
		buttonHttpGet=new JButton("Extract");
		buttonHttpGet.addActionListener(this);
		panelHttpGet.add(labelHttpGet);
		panelHttpGet.add(textHttpGet);
		panelHttpGet.add(buttonHttpGet);
		
		frameHttpGet=new JFrame();
		frameHttpGet.setLayout(new GridLayout(4,2));
		frameHttpGet.add(panelHttpGet);
		frameHttpGet.setTitle("My frame");
		frameHttpGet.setSize(500,400);
		frameHttpGet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameHttpGet.setVisible(true);
		fileWriter=new FileWriter(filename);
		
	}
	public static void main(String[] args) throws IOException 
	{
		
		ApacheHttpClientGet apacheGet=new ApacheHttpClientGet("Pubs.json");
		while(startFlag!=false)
		apacheGet.httpGetRestful();
	}
	
	
	
	public void httpGetRestful()
	{
		try {

			
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			  //https://inpho.cogs.indiana.edu/journal.json
			//http://inpho2.cogs.indiana.edu/digging/pubs.json
			HttpGet getRequest = new HttpGet("http://inpho2.cogs.indiana.edu/digging/pubs.json"); 
			getRequest.addHeader("accept", "application/json");
		    HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {

				this.fileWriter.append(output);
                
			}
			
			this.fileWriter.flush();
			this.fileWriter.close();
			

			httpClient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
   
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
	}
	
	
	}

