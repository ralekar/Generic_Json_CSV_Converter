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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import au.com.bytecode.opencsv.CSVWriter;

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
	private CSVWriter csvJsonWriter;
	
	public JsonRead(String jsonSource, boolean sourceFromFile) throws IOException{
		
		this.jsonSource = jsonSource;
		this.sourceFromFile = sourceFromFile;
		this.filename="FINALJOURNAL.csv";
		this.createCSVFile();
		primaryKey="url";
		arrayKey="";
		arrayValue="";
		superKey="";
		firstRowFlag=false;
		mapPrimitive=new LinkedHashMap<String,String>();
		
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		
		try{
			JsonRead jsonParserDemo =new JsonRead("FinalJournal.json",true);
			
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
						arrayValue+=data.toString().trim()+"|";
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
					mapPrimitive.put(incomingString,jsonValue.trim());
					firstRowFlag=true;

				}
				if(!incomingString.equalsIgnoreCase(superKey+"_"+primaryKey))
				{
					String value=mapPrimitive.get(incomingString);
					value+="|"+jsonValue.trim();
					mapPrimitive.put(incomingString, value.trim());
				}
			}
			else
			{
				
				mapPrimitive.put(superKey+"_"+jsonKey, jsonValue.trim());
			}

		
	}	
		
	public void csvFileWriter(Map<String,String>mapJsonPrimitive) throws IOException
	{
		
	  if(!firstRowFlag)
	  {
		 String [] columns=new String[mapJsonPrimitive.size()+1];
		 int index=0;
		 for(Entry<String,String>pair:mapJsonPrimitive.entrySet())
		 {
			 
			 String col=pair.getKey().trim();
			 columns[index]=col;
			 index++;
			 
		 }
		 this.csvJsonWriter.writeNext(columns);
	  }
	  else
	  {
		  String[] listJsonValues=new String[mapJsonPrimitive.entrySet().size()+1];
			int index=0;
			for(Entry<String,String>pair:mapJsonPrimitive.entrySet())
			 {
				String newPair=pair.getValue().trim();
			    listJsonValues[index]=newPair;
			    index++;
			    
				
			 }
			this.csvJsonWriter.writeNext(listJsonValues);
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
		    this.csvJsonWriter=new CSVWriter(fileWriter);
			
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