package webonise.automation.core;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestData {
		private static Element root = null;
		private static NodeList tcNode = null;
		private static NodeList commonNode = null;
		public static HashMap<String, String> hmap = new HashMap<String, String>();
		
		public void initialize(String filename){
			System.out.println("Initializing Test data");
			File file = null;
			try{
			file = new File(filename);
			System.out.println("filename="+filename);
			}
			catch(NullPointerException e)
			{
				System.err.println("test data is null.Please define test data file");
			}
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document doc;
				doc = db.parse(file);
				doc.getDocumentElement().normalize();
				root = doc.getDocumentElement();
			}
			catch (SAXException | IOException e) {
				e.printStackTrace();
			}
			catch (ParserConfigurationException e) {
				System.out.println("Parser error");
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) {
				System.out.println("Illegal argument in testedata file");
			}
		}
		public void setTCNode(String tcName){
			  System.out.println("TC NAME IS " + tcName);
			  try{					
			  tcNode = root.getElementsByTagName(tcName);
			  commonNode=root.getElementsByTagName("common");
			  System.out.println("TC Node is "+ tcNode.toString());
			  }
			  catch(NullPointerException e)
				{
					System.err.println("Testcase tag is missing or Common tag is missing.Please define testcase or common tag Test Data file");
				}
		}
		public Object[][] getTCData(){
			
			//System.out.println("in GetTCData");
			Integer num = null;
			try{
			 num = tcNode.item(0).getChildNodes().item(1).getChildNodes().getLength();
			
			num = (num/2);
			//System.out.println("Number is : " + num);
			}
			  catch(NullPointerException e)
				{
					System.err.println(" datasets not defined.Please define datasets Test Data file");
				}
			Object[][] ob =  new Object[num][2];
			
			for(int i=0;i<num;i++){
				ob[i][0]=i;
				ob[i][1]=true;
			}
			
			return ob;
			
			//return new Object[][] {{2, true}, {6, false}, {19, true}, {22, false}, {23, true}};
			
		}
		
		public void updateTCData(int iteration){
			  
			  System.out.println("Iteration : " + iteration);
			  
			  hmap.clear();
			  
			  //System.out.println("tcNode name : " + tcNode.item(0).getNodeName());
			  
			  NodeList datasetList = tcNode.item(0).getChildNodes().item(1).getChildNodes();
			  
			  //for (int j = 1; j < datasetList.getLength(); j = j + 2) {
				
			  	  Node datasetNode = datasetList.item((iteration*2)+1);
				  if (datasetNode.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) datasetNode;
						for (int k = 1; k < ele.getChildNodes().getLength(); k = k + 2) {
							String key = ele.getChildNodes().item(k).getNodeName();
							//System.out.println("Key is : " + key);
							String value = ele.getChildNodes().item(k).getTextContent();
							//System.out.println("Value is : " + value);
							hmap.put(key, value);
						}
					}
				  
				  if(commonNode.getLength()!=0) {
					   datasetList = commonNode.item(0).getChildNodes().item(1).getChildNodes();
					   datasetNode = datasetList.item((iteration*2)+1);
					   if (datasetNode.getNodeType() == Node.ELEMENT_NODE) {
					    Element ele = (Element) datasetNode;
					    for (int k = 1; k < ele.getChildNodes().getLength(); k = k + 2) {
					     String key = ele.getChildNodes().item(k).getNodeName();
					     String value = ele.getChildNodes().item(k).getTextContent();
					     hmap.put(key, value);
					    }
					   }
					   
					  }
		}
		
		public String getVal(String value){
			  //System.out.println("VAR NAME IS " + objName);
			NodeList varList,varList2=null;
			Node varNode;
			 try
			 	{
				 	// varList = tcNode.item(0).getChildNodes();
				 	varList2=commonNode.item(0).getChildNodes();
				 	  for(int j=0;j<varList2.getLength();j++){
						  
						   varNode = varList2.item(j);
						  if(varNode.getNodeName() != "#text"){
							  if(varNode.getNodeName().equals(value)){
								    //System.out.println("Return Value is - " + varNode.getTextContent());
								  	return varNode.getTextContent();
							  }
						  }
					    }
			 	}
			 catch(NullPointerException e)
				{
					System.err.println("Common tag missing.Please define Testdata file");
				}
			return "Tag missing in test data";
		}
		
		public String getValue(String varName){
			  if(hmap.containsKey(varName)){
				  return hmap.get(varName);
			  }
			  else				 
			  {
				  return getVal(varName);
			  }
		}
		
		@SuppressWarnings("rawtypes")
		public Map getHashMap()
		{
			return hmap;
		}
}