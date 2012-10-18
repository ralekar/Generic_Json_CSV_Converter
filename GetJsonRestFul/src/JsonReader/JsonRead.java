package JsonReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonRead {

    private CSVWriter csvWriter;
    private String jsonSource;
	private boolean sourceFromFile;
	private FileWriter fileWriter;
    private static String arrayKey;
    private static String arrayValue;
    private String superKey;
	private static String primaryKey;
    private Map<String,String> mapPrimitive;
	private static boolean firstRowFlag;
	
	private String filename;
	
	
	public JsonRead(String jsonSource, boolean sourceFromFile) throws IOException{
		
		this.jsonSource = jsonSource;
		this.sourceFromFile = sourceFromFile;
		this.filename="rrr.csv";
		this.createCSVFile();
		primaryKey="volume";
		arrayKey="";
		arrayValue="";
		superKey="";
		firstRowFlag=false;
		mapPrimitive=new HashMap<String,String>();
		
	}
	
	public static void main(String[] args) throws IOException
	{
		
		try{
			JsonRead jsonParserDemo =new JsonRead("temppubs.json",true);
			@SuppressWarnings("unused")
			Gson jsonGson = new Gson();
			JsonReader jsonReader = jsonParserDemo.getJsonReader();
		    JsonParser jsonParser = new JsonParser();
			JsonObject dataObject = jsonParser.parse(jsonReader).getAsJsonObject();
			jsonParserDemo.jsonFileReaderRecursion(dataObject);
			jsonParserDemo.csvFileWriter(jsonParserDemo.mapPrimitive);
			jsonParserDemo.closeFileWriter();
		}
		catch(Exception e)
		{
			System.out.println("ERROR ::: MALFORMED JSON FILE!!!!!!!!!!!!!!!!");
			System.exit(0);
		}
	    
	 }

	public void jsonFileReaderRecursion(JsonObject dataObject) throws IOException
	{

		for(Entry<String,JsonElement>dataEntry:dataObject.entrySet())
		{
			if(dataEntry.getValue() instanceof JsonObject)
			{
				superKey=dataEntry.getKey().toString(); 
			    jsonFileReaderRecursion(dataObject.getAsJsonObject(dataEntry.getKey().toString()));
				
			}

			else if(dataEntry.getValue() instanceof JsonArray)
			{
				arrayKey=dataEntry.getKey().toString();
				arrayValue="";
				
				JsonArray dataArray=dataEntry.getValue().getAsJsonArray();
				
				for(JsonElement data:dataArray)
				{
					if(!data.isJsonPrimitive())
					{
						jsonFileReaderRecursion(data.getAsJsonObject());
					}
					else
					{
						arrayValue+=data.toString()+"|";
					}

				}
				
				jsonToCsvFormat(superKey,arrayKey,arrayValue);
				
				
            }
			else if(dataEntry.getValue() instanceof JsonPrimitive)
			{
				jsonToCsvFormat(superKey,dataEntry.getKey().toString(),dataEntry.getValue().toString());
				
			}
		}
		
		
	}
	
	public void jsonToCsvFormat(String superKey,String jsonKey,String jsonValue) throws IOException
	{
		    
			if(mapPrimitive.containsKey(superKey+"_"+jsonKey))
			{
				String incomingString=superKey+"_"+jsonKey;
				if(incomingString.equalsIgnoreCase(superKey+"_"+primaryKey))
				{
                    csvFileWriter(mapPrimitive);	
					mapPrimitive.clear();
					mapPrimitive.put(incomingString,jsonValue);
					firstRowFlag=true;

				}
				if(!incomingString.equalsIgnoreCase(superKey+"_"+primaryKey))
				{
					String value=mapPrimitive.get(incomingString);
					value+="|"+jsonValue;
					mapPrimitive.put(incomingString, value);
				}
			}
			else
			{
				
				mapPrimitive.put(superKey+"_"+jsonKey, jsonValue);
			}

		  
	}	
		
	public void csvFileWriter(Map<String,String>mapJsonPrimitive) throws IOException
	{
		
	  if(!firstRowFlag)
	  {
		 for(Entry<String,String>pair:mapJsonPrimitive.entrySet())
		 {
			 this.fileWriter.append(pair.getKey()+" ,");
			 
		 }
		 this.fileWriter.append("\n");
		 for(Entry<String,String>pair:mapJsonPrimitive.entrySet())
		 {
			  String value;
			  if(pair.getValue().isEmpty()||pair.getValue().equalsIgnoreCase("\"\""))
				 value="null";
			  else
				  value=pair.getValue();
			  
			  this.fileWriter.append(value+" ,");
			 
		 }
		 this.fileWriter.append("\n");
		 
	  }
	  else
	  {
		
		  for(Entry<String,String>pair:mapJsonPrimitive.entrySet())
			 {
			  String value;
			  if(pair.getValue().isEmpty()||pair.getValue().equalsIgnoreCase("\"\""))
				  value="null";
			  else
				  value=pair.getValue();
			  
			  this.fileWriter.append(value+" ,");
				 
			 }
			 this.fileWriter.append("\n");
			
	  }
	  
	}
	
	
	private void closeFileWriter() throws IOException
	{
		this.fileWriter.flush();
		this.fileWriter.close();
	}
	
	
	private void createCSVFile()
	{
		try {
			
			this.fileWriter=new FileWriter(this.filename);
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	
   private JsonReader getJsonReader () throws FileNotFoundException, UnsupportedEncodingException{
		JsonReader reader = null;
		if (sourceFromFile){
			reader = new JsonReader(
					new InputStreamReader(new FileInputStream(this.jsonSource),"UTF-8"));
		}
		return reader;
	}
}