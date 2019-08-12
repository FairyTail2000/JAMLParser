package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JAMLParser {
	
	private List<String> unparsed = new ArrayList<String>();
	private List<String> parsed = new ArrayList<String>();
	private Map<String, String> DefinitionMap = new HashMap<String, String>();
	private File targetFile = null;
	private List<String> targetList = null;
	
	
	public JAMLParser (File target) {
		targetFile = target;
		
		//if (!target.getName().endsWith("jaml")) {
		//	System.err.println("Please use jaml extension");
		//}
		try {
			unwrapFile();
			parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public JAMLParser(List<String> target) {
		targetList = target;
		parse();
	}
	
	private void unwrapFile() throws FileNotFoundException, IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(targetFile), StandardCharsets.UTF_8);
		try (BufferedReader reader = new BufferedReader(isr)) {
			String read = "";
			while ((read = reader.readLine()) != null) {
				unparsed.add(read);
			}
			
			boolean inside = false;
			String buf = "";
			for (String p : unparsed) {
				if (p.startsWith("{")) {
					inside = true;
					continue;
				} else if (p.startsWith("}")) {
					inside = false;
					parsed.add(buf);
					buf = "";
					continue;
				}
				if (inside) {
					
					buf += p;
				}
			}
			targetList = parsed;
			parsed = null;
			unparsed = null;
		} catch (Exception e) {
		} finally {
			isr.close();
		}
	}
	
	private void parse () {
		
		for (String p : targetList) {
			String name = "";
			String beschreibung = "";
			char[] arr = p.toCharArray();
			boolean beschreibungsStart = false;
			int start = 0;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == ':' && !beschreibungsStart) {
					name = p.substring(0, i);
					continue;
				}
				
				if (arr[i] == '"' && !beschreibungsStart) {
					beschreibungsStart = true;
					start = i;
					continue;
				} else if (arr[i] == '"' && beschreibungsStart) {
					beschreibung = p.substring(start + 1, i);
				}
			}
			 DefinitionMap.put(name, beschreibung);
		}
		
		
	}
	
	public Set<String> getKeySet () {
		return DefinitionMap.keySet();
	}
	
	public Map<String, String> getMap () {
		return DefinitionMap;
	}
	
	public String getValue (String key) {
		return DefinitionMap.get(key);
	}
	
	public boolean containsKey (String key) {
		return DefinitionMap.containsKey(key);
	}
	
	public void setTarget (File target) {
		targetFile = target;
		reInitialize();
		try {
			unwrapFile();
			parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setTarget (List<String> target) {
		targetList = target;
		reInitialize();
		parse();
	}
	
	public void reInitialize () {
		unparsed = new ArrayList<String>();
		parsed = new ArrayList<String>();
		DefinitionMap = new HashMap<String, String>();
	}
	
	private String return_able = "";
	@Override
	public String toString() {
		return_able += "Current Content:\n";
		this.DefinitionMap.forEach((String Key, String Value) -> return_able += "Name: " + Key + ", Value: " + Value + "\n");
		return return_able;
	}
	
}
