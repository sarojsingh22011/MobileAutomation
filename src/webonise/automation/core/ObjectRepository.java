package webonise.automation.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ObjectRepository {

	private static Element root = null;
	private static NodeList tcNode = null;
	private static NodeList commonNode = null;
	File file = null;
	private static HashMap<String, String> hmap = new HashMap<String, String>();
	public void initialize(){
		System.out.println("Initializing object repo");
		
		try{
		file = new File(Configuration.objRepo);
		}
		catch(NullPointerException e)
		{
			System.err.println("objectRepository in null.Please define Object Repo file");
		}
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc;
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
			root = doc.getDocumentElement();
			System.out.println(" Root node is :  " + root.getNodeName());
			commonNode=root.getElementsByTagName("common");
			//System.err.println("------------------in common-----------------");
			readCommonNode();
		}
		catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println("Parser error");
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block	
			//System.out.println("objRepo is null");
			System.out.println("Illegal argument in Object Repo file");
		}
	}
	
	public void setTCNode(String tcName){
		  
		  //System.out.println("TC NAME IS " + tcName);
		try{					
		  tcNode = root.getElementsByTagName(tcName);
		}
		catch(NullPointerException e)
		{
			System.err.println("TC or comon not defined.Please check Object Repo file");
		}
	}
	
	public void readCommonNode(){
		  
		  hmap.clear();
		  
		  //System.out.println("tcNode name : " + tcNode.item(0).getNodeName());
		  //for (int j = 1; j < datasetList.getLength(); j = j + 2) {
			
		  for (int temp = 0; temp < commonNode.getLength(); temp++) {
				Node nNode = commonNode.item(temp);	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					for (int k = 1; k < eElement.getChildNodes().getLength(); k=k+2) {
						String key = eElement.getChildNodes().item(k).getNodeName();
						System.err.println("Key is : " + key);
						String value = eElement.getChildNodes().item(k).getTextContent();
						System.err.println("Value is : " + value);

						hmap.put(key, value);
					}
					}
				}
	}
	
	public String getObjID(String varName){
		  if(hmap.containsKey(varName)){
			  return hmap.get(varName);
		  }
		  else		 
		  {
			  return getObj(varName);
		  }
	}
	
	public String getObj(String objName){
		  //System.out.println("VAR NAME IS " + objName);
		NodeList varList,varList2=null;
		Node varNode;
		 try
		 	{
			 	 varList = tcNode.item(0).getChildNodes();  
			 	varList2=commonNode.item(0).getChildNodes();
		  for(int j=0;j<varList.getLength();j++){
			  
			   varNode = varList.item(j);
			  if(varNode.getNodeName() != "#text"){
				  if(varNode.getNodeName().equals(objName)){
					    //System.out.println("Return Value is - " + varNode.getTextContent());
					  	return varNode.getTextContent();
				  }
			  }
			  }
		  for(int j=0;j<varList.getLength();j++){
			  
			   varNode = varList.item(j);
			  if(varNode.getNodeName() != "#text"){
				  if(varNode.getNodeName().equals(objName)){
					    //System.out.println("Return Value is - " + varNode.getTextContent());
					  	return varNode.getTextContent();
				  }
			  }	  
		 	}
		 	}
		 catch(NullPointerException e)
			{
				System.err.println("objectRepository not defined.Please define Object Repo file");
			}
		  System.out.println("Return Value is - Invalid tcName or var name");
		  return "Invalid tcName or tag name in object repo or variable name";
	}
}