package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		  try {

			  /*
			   * Url Name Sample:
			   * 
			   * http://api.goeuro.com/api/v2/position/suggest/en/Berlin
			   * 
			   */
			  
			  if(args!=null && args.length >= 1 ){
				  
				  for (String city : args) {
						
					  	JSONArray obj = processJsonforCity(city);
						
						List<HashMap<String, String>> listOfMaps = processJSONArray(obj);
						
						FileWriter fw = getFileWriter(city);
						
						writeOutput(listOfMaps, fw);
				}
				    
				  
			  }else{
				  System.out.println("Invalid or Less Number of Arguments.");
			  }
			  	
						

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param listOfMaps
	 * @param fw
	 * @throws IOException
	 */
	private static void writeOutput(List<HashMap<String, String>> listOfMaps, FileWriter fw) throws IOException {
		StringBuffer output=new StringBuffer();
		
		System.out.println("Output from Server .... \n");
		
		output.append("_id,").append("name,").append("type,").append("latitude,").append("longitude").append("\n");
		
		for (HashMap<String,String> hashMap : listOfMaps) {
		
			output.append(hashMap.get("_id"));
			output.append(",");
			output.append(hashMap.get("name"));
			output.append(",");
			output.append(hashMap.get("type"));
			output.append(",");
			output.append(hashMap.get("latitude"));
			output.append(",");
			output.append(hashMap.get("longitude"));
			output.append("\n");
		}
		
		fw.write(output.toString());
		
		fw.close();
	}

	/**
	 * @param obj
	 * @return
	 */
	private static List<HashMap<String, String>> processJSONArray(JSONArray obj) {
		HashMap<String,String> map=null;
		
		List<HashMap<String,String>> listOfMaps=new ArrayList<HashMap<String,String>>();
		
		for (Object o : obj)
		  {
			
			JSONObject location = (JSONObject) o;

			map =new HashMap<String,String>();
			
		    map.put("_id" ,Long.toString((Long)location.get("_id")));
		    System.out.println(Long.toString((Long)location.get("_id")));
		    

		    map.put("name" ,(String) location.get("name"));
		    System.out.println((String) location.get("name"));
		    
		    map.put("type" ,(String) location.get("type"));
		    System.out.println((String) location.get("type"));
		    
		    map.put("latitude" ,(Double.toString((Double)((JSONObject) location.get("geo_position")).get("latitude"))));
		    //System.out.println((String) location.get("latitude"));
		    
		    map.put("longitude" ,(Double.toString((Double)((JSONObject) location.get("geo_position")).get("longitude"))));
		    
		    listOfMaps.add(map);
		    
		  }
		return listOfMaps;
	}

	/**
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private static FileWriter getFileWriter(String city) throws URISyntaxException, IOException {
				
		File file = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+city+".csv");
		
		System.out.println("Get File @ -->"+System.getProperty("user.dir")+System.getProperty("file.separator")+city+".csv");
		
		FileWriter fw=new FileWriter(file);
		return fw;
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws RuntimeException
	 * @throws ParseException
	 */
	private static JSONArray processJsonforCity(String city)
			throws MalformedURLException, IOException, ProtocolException, RuntimeException, ParseException {
		
		//URL url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/Berlin");
		
		URL url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/"+city);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		
		JSONParser parser = new JSONParser();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		JSONArray obj = (JSONArray) parser.parse(br);
		
		conn.disconnect();
		
		return obj;
	}

}
