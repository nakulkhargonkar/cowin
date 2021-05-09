package com.nakul;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;


public class Test1 {
	public static void main(String[] args) throws Exception {
		Test1.m1();
		/*while(true){
			Thread.sleep(5000);
			URL url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=314&date=08-05-2021");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			int responseCode = connection.getResponseCode();
			System.out.println(connection.getResponseMessage());
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			//System.out.println(sb.toString());
			if(sb.length()>15){
				System.out.println("ALERT");
				//
				while(true){
					String gongFile = "C:\\Windows\\Media\\Alarm01.wav";
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File(gongFile)));
					clip.start();
					Thread.sleep(8000);
				}
			}
			else{
				System.out.println("No SLOTS");
			}
		}
		 */	}

	public static void m1()throws Exception {
		//boolean cityflag=true;
		while(true){
			boolean flag=false;
			Thread.sleep(4000);
			URL url=null;
			//if(cityflag){
				url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=97&date=09-05-2021");
//				cityflag=false;
//			}
//			else{
//				url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=363&date=08-05-2021");
//				cityflag=true;
//			}
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			//int responseCode = connection.getResponseCode();
			System.out.println(connection.getResponseMessage());
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject myResponse = new JSONObject(sb.toString());
			Configuration configuration = Configuration.builder()
					.jsonProvider(new JsonOrgJsonProvider())
					.build();

			//read total centers
			JsonPath jsonPath = JsonPath.compile("$.centers");
			JSONArray jsonPathArray = jsonPath.read(myResponse, configuration);
			int center_count = jsonPathArray.length();
			//System.out.println("Total Centers are :"+center_count);
			
			for(int i=0;i<center_count;i++){
				//read total centers
				jsonPath = JsonPath.compile(String.format("$.centers.[%d].sessions",i));
				jsonPathArray = jsonPath.read(myResponse, configuration);
				int session_center_count = jsonPathArray.length();
				for(int j=0;j<session_center_count;j++){
					jsonPath = JsonPath.compile(String.format("$.centers.[%d].sessions.[%d].available_capacity",i,j)); 
					double avSlots=0.0;
					try{
						int avSlots_i=jsonPath.read(myResponse, configuration);
						avSlots=(double)avSlots_i;
					}
					catch(Exception e){
						double avSlots_d=jsonPath.read(myResponse, configuration);
						avSlots=avSlots_d;
					}
					
					jsonPath = JsonPath.compile(String.format("$.centers.[%d].sessions.[%d].available_capacity",i,j)); 
					int minage=jsonPath.read(myResponse, configuration);
	
					jsonPath = JsonPath.compile(String.format("$.centers.[%d].pincode",i)); 
					int pincode=jsonPath.read(myResponse, configuration);
					String pincode_str=pincode+"";
					
					if(avSlots>0 && minage==18 && pincode_str.startsWith("80")){
						System.out.println("=========================================================================");
						System.out.println(avSlots + " Slots Avialable!!!!!!!!! Below are the details");
						jsonPath = JsonPath.compile(String.format("$.centers.[%d].name",i));
						System.out.println("Name of hospital is :"+jsonPath.read(myResponse, configuration));
						jsonPath = JsonPath.compile(String.format("$.centers.[%d].address",i));
						System.out.println("address of hospital is :"+jsonPath.read(myResponse, configuration));
						jsonPath = JsonPath.compile(String.format("$.centers.[%d].pincode",i));
						System.out.println("pincode of hospital is :"+jsonPath.read(myResponse, configuration));	
						System.out.println("=========================================================================");
						flag=true;
					}
				}
			}
			if(flag){
				while(true){
					String gongFile = "C:\\Windows\\Media\\Alarm01.wav";
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File(gongFile)));
					clip.start();
					Thread.sleep(8000);
				}
			}
			System.out.println("Trying Again!!!");
		}

		//System.out.println(sb.toString());
		/*if(sb.length()>15){
				System.out.println("ALERT");
				//
				while(true){
					String gongFile = "C:\\Windows\\Media\\Alarm01.wav";
					Clip clip = AudioSystem.getClip();
			        clip.open(AudioSystem.getAudioInputStream(new File(gongFile)));
			        clip.start();
			        Thread.sleep(8000);
				}
			}
			else{
				System.out.println("No SLOTS");
			}*/
		//}

	}

}
