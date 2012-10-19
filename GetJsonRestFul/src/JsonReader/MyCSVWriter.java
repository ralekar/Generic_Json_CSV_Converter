package JsonReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MyCSVWriter {

	private String fileName;
	private List<Object>jsonList;
	private FileWriter fileWriter;



	public FileWriter getFileWriter() {
		return fileWriter;
	}



	public void setFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public List<Object> getJsonList() {
		return jsonList;
	}



	public void setJsonList(List<Object> jsonList) {
		this.jsonList = jsonList;
	}



	public MyCSVWriter(String filename){

		try {
			this.setFileName(filename);
			fileWriter=new FileWriter(getFileName());
			this.setFileWriter(fileWriter);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}




	public void CSVFileWriter() throws IOException
	{

		for(Object fields:this.getJsonList().get(0).getClass().getDeclaredFields())
		{

			this.getFileWriter().append((fields.toString().substring(fields.toString().lastIndexOf('.') + 1))+";");

		}

		for(Object jsonField:this.getJsonList())
		{
			CSVObject csvObject=(CSVObject)jsonField;
			this.getFileWriter().append("\n");
			for(String element:csvObject.getElements())
			{
				if(element != null && !element.isEmpty())
					this.getFileWriter().append(element.trim()+";");
				else
					this.getFileWriter().append(element+";");

			}
     }

		this.getFileWriter().flush();
		this.getFileWriter().close();
   }



}
