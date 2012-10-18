package JsonReader;

import java.util.ArrayList;
import java.util.List;

public class Journal implements CSVObject{
	      
	    private String ID;
	    private String type;
	    private String label;
	    private String sepDir;
	    private String url;
	    

		 
		  public Journal(){
			  
		  }


		public String getURL() {
			return url;
		}


		public void setURL(String url) {
			this.url = url;
		}


		public String getSepDir() {
			return sepDir;
		}


		public void setSepDir(String sepDir) {
			this.sepDir = sepDir;
		}


		public String getType() {
			return type;
		}


		public void setType(String type) {
			this.type = type;
		}


		public String getID() {
			return ID;
		}


		public void setID(String iD) {
			ID = iD;
		}


		public String getLabel() {
			return label;
		}


		public void setLabel(String label) {
			this.label = label;
		}


		@Override
		public ArrayList<String> getElements() {
			
			ArrayList<String>elements=new ArrayList<String>();
			elements.add(this.getID());
			elements.add(this.getType());
			elements.add(this.getLabel());
			elements.add(this.getSepDir());
			elements.add(this.getURL());
			return elements;
			
		}


		@Override
		public CSVObject getSerializedObject() {
			
			Journal journal=new Journal();
			return journal;
		}
		 
		  
		  
		  
		 
		}

