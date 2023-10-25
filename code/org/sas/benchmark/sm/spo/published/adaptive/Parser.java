package org.sas.benchmark.sm.spo.published.adaptive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.femosaa.core.EAConfigure;
/**
 * 
 * @author tao
 *
 */
public class Parser {
 
	
	public static String[] p = new String[] {
			
			"-4.0,85.391",
			"-8.0,57.898",
			"-6.0,88.161"
			
			
	};
	
	//public static String[] keepZero = {"BDBCAll","BDBJAll","X264All"};
	// two objectives
	public static LinkedHashMap<String, Double> map1 = new LinkedHashMap<String, Double>();
	public static LinkedHashMap<String, Double> map2 = new LinkedHashMap<String, Double>();
	//public static LinkedHashMap<String, Double> raw_map1 = new LinkedHashMap<String, Double>();
	//public static LinkedHashMap<String, Double> raw_map2 = new LinkedHashMap<String, Double>();
	public static LinkedHashMap<String, String> index_to_raw = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> reverse_map = new LinkedHashMap<String, String>();
	public static List<String> seeds = new ArrayList<String>();
	public static String selected = "SS-K";
	//x264 Best 244.23Worst 821.963
	// sql Best 12.513Worst 16.851
    public static void main( String[] args )
    {
    	
    	read(selected);
    	
    	//output();
    }
    
    
    public static void output(){
    	System.out.print("\n\n\n\n");
    	for (String s : p) {
    		String st = reverse_map.get(s);
    		String v = "";
    		if (st != null) {
    			v = st.split(":")[st.split(":").length-2] + " " +  st.split(":")[st.split(":").length-1];
    		} else {
    			v = "7 8";
    			System.out.print(v + " " + s.split(",")[1] + "\n");
    		}
    		
    	}
    }
    
   

    
    private static String convert(String s) {
    	if("FALSE".equals(s)) {
    		return "False";
    	}
    	
    	if("TRUE".equals(s)) {
    		return "True";
    	}
    	
    	return s;
    }
    
    public static void read(String name){
    	
    	double max_1 = Double.NEGATIVE_INFINITY;
    	double min_1 = Double.POSITIVE_INFINITY;
    	double max_2 = Double.NEGATIVE_INFINITY;
    	double min_2 = Double.POSITIVE_INFINITY;
    	
    	Set<String> set_str = new HashSet<String>();
    	// We only need to get rid of the mandatory one or those that do not change at all.
    	ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
    	String[] names = null;
    	double time = 0.0;
    	try {
    		InputStream in = Parser.class.getResourceAsStream("/"+name+".csv"); 
    		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	    String line = null; 
			
		
			int o = 0;
			while ((line = reader.readLine()) != null) {
				
				if(line.contains("$") || o==0) {
					String[] dd = line.split(",");
					names = dd;
					for(String s : dd) {
						System.out.print("\"" + s + "\",\n");
							
					}
					o++;
					continue;
				}
				String r = "";
				String[] data = line.split(",");
				
				for(int i = 0; i < data.length - 2; i++) {
					///r += r.equals("")? data[i] : ":" + data[i];
					if(list.size() <= i) {
						list.add(new ArrayList<Double>());
					}
					
					ArrayList<Double> subList = list.get(i);
					if(!subList.contains(Double.parseDouble(data[i]))) {
						subList.add(Double.parseDouble(data[i]));
					}
					
				}
				
				
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashSet<Integer> set = new HashSet<Integer>();
		
		
		for(int i = 0; i < list.size(); i++) {
			ArrayList<Double> subList = list.get(i);
			// means it cannot be changed and has no variability
			if (subList.size() == 1) {
				set.add(i);
			} else {
				double[] d = new double[subList.size()];
				for(int j = 0; j < subList.size(); j++) {
					d[j] = subList.get(j);
				}
				
				
				Arrays.sort(d);
				
				subList.clear();
				for(int j = 0; j < d.length; j++) {
					subList.add((Double)d[j]);
					System.out.print("Oringal index: " + i + "=" + d[j] + "\n");
				}
				
				
			}
		}
		
		for(int i = 0; i < list.size(); i++) {
			if(!set.contains(i)) {
			System.out.print("<item name=\""+ names[i] +"\" provision=\"0\" constraint=\"-1\" differences=\"1\" pre_to_max=\"0.7\" pre_of_max=\"0.1\" min=\"0\" max=\""+(list.get(i).size()-1)+"\" price_per_unit=\"0.5\"  />\n");
			}
		
		}
		
		for(int i = 0; i < list.size(); i++) {
			if(!set.contains(i)) {
			if(list.get(i).size() <= 2) {
				System.out.print("<feature name=\""+names[i]+"\" type=\"categorical\" optional=\"true\"/>\n");
			} else {
				System.out.print("<feature name=\""+names[i]+"\" type=\"numeric\" range=\"0 "+(list.get(i).size()-1)+"\" gap=\"1\" />\n");
			}
			}
		}
		
		System.out.print("Unchanged ones: " + set.toString() + "\n");
		//if (1==1)return;
    	
		double[] v1_d = new double[] {Double.MAX_VALUE,Double.MIN_VALUE};
		double[] v2_d = new double[] {Double.MAX_VALUE,Double.MIN_VALUE};
    	try {
    		InputStream in = Parser.class.getResourceAsStream("/"+name+".csv"); 
    		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    		//BufferedReader reader = new BufferedReader(new FileReader("/Users/"+System.getProperty("user.name")+"/research/experiments-data/public-data/performance/flash-data/Flash-MultiConfig/Data/"+name+".csv"));
			String line = null; 
			int o = 0;
			
			while ((line = reader.readLine()) != null) {
				
				if(line.contains("$") || o==0) {
					o++;
					continue;
				}
				String r = "";
				String raw = "";
				String[] data = line.split(",");
				
				for(int i = 0; i < data.length - 2; i++) {
					
					if(!set.contains(i)) {
						ArrayList<Double> subList = list.get(i);
						int v = subList.indexOf(Double.parseDouble(data[i]));
						r += r.equals("")? v : ":" + v;
						
						double raw_value = Double.parseDouble(data[i]);
						raw += raw.equals("")? raw_value : ":" + raw_value;
					}
					
					
					
				}
				
				
				if(map1.containsKey(r)) {
					//System.out.print(line + " : " + r+ ", current "  +map1.get(r) +" duplicate\n");
				}
				seeds.add(r);
				
				double v1 = "nan".equals(data[data.length-2]) ? 0.0 : Double.valueOf(data[data.length-2]);
				double v2 = "nan".equals(data[data.length-1]) ? 0.0 : Double.valueOf(data[data.length-1]);
				
				if(v1 < 0) {
					v1 = Math.abs(v1);
				}
				
				if(v2 < 0) {
					v2 = Math.abs(v2);
				}
						
				map1.put(r, v1);
				map2.put(r, v2);
				/*if(v1 == 9645.6) {
					System.out.print(r + "=" + v1);
				}*/
				//raw_map1.put(raw, v1);
				//raw_map2.put(raw, v2);
				index_to_raw.put(r,raw);
				//reverse_map.put((-1*v1)+","+v2, r);
				
				if(v1 < v1_d[0] && v1 != 0) {
					v1_d[0] = v1;
				}
				
				if(v1 > v1_d[1] && v1 != 0) {
					v1_d[1] = v1;
				}
				
				if(v2 < v2_d[0]) {
					v2_d[0] = v2;
				}
				
				if(v2 > v2_d[1]) {
					v2_d[1] = v2;
				}
		
				if (v1 > max_1 && v1 != 0) {
					max_1 = v1;
				}
				
				if (v1 < min_1&& v1 != 0) {
					min_1 = v1;
				}
				
				if (v2 > max_2&& v2 != 0) {
					max_2 = v2;
				}
				
				if (v2 < min_2&& v2 != 0) {
					min_2 = v2;
				}
				//System.out.print(/*line + " : " + */r + "=" + map1.get(r)+ " and " + map2.get(r) +"\n");
				System.out.print("("+map1.get(r)+ "," + map2.get(r) +")\n");
				set_str.add("("+(Math.round(map1.get(r) * 1000.0) / 1000.0)+ "," + (Math.round(map2.get(r) * 100.0) / 100.0) +")\n");
				if(!"nan".equals(data[data.length-1]))
				  time += Double.valueOf(data[data.length-1]);
			}
			
			System.out.print(map1.size() + "\n");
			System.out.print("Mean runtime: " + time/map1.size() + "\n");
			
			System.out.print(v1_d[0] + " , " + v1_d[1] + "\n");
			System.out.print(1.0/v1_d[1] + " , " + 1.0/v1_d[0] + "\n");
			System.out.print(v2_d[0] + " , " + v2_d[1] + "\n");
			//getSeeds();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    	System.out.print("\n\n\n" + set_str.size() + "\n\n\n");
    	
    	for (String s : set_str) {
    		System.out.print(s);
    	}
		
    	System.out.print("min_1 " +min_1 +"\n");
    	System.out.print("max_1 " +max_1  +"\n");
    	System.out.print("min_2 " +min_2 +"\n");
    	System.out.print("max_2 " +max_2 +"\n");
    	
    	double[] values = new double[map1.size()];
    	int k = 0;
    	for (String s : map1.keySet()) {
    		values[k] = map1.get(s);
    		k++;
    	}
    	
    	//distribution(values, max_1, min_1, true);
    	System.out.print("----------\n");
    	
    	double[] values1 = new double[map2.size()];
    	 k = 0;
    	for (String s : map2.keySet()) {
    		values1[k] = map2.get(s);
    		k++;
    	}
    	
    	//distribution(values1, max_2, min_2, false);
    	
    	/*for (String s : raw_map1.keySet()) {
    		System.out.print(s + "\n");
    	}*/
	}
	
    public static void validateUnchanged(){
    	
    	
    }
    
	public static void validate(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/Users/"+System.getProperty("user.name")+"/research/experiments-data/fuzzy-requirement/single-objective-dataset/"+selected+".csv"));
			String line = null; 
			
			int[] store = null;
			int total = 0;
			while ((line = reader.readLine()) != null) {
				
				if(line.startsWith("$")) {
					String[] d = line.split(",");
					for (int i = 0; i < d.length; i++) {
						//System.out.print("\""+d[i].substring(1) + "\",\n");
					}
					
					continue;
				}
				
				String[] data = line.split(",");
				
				if(store == null) {
					store = new int[data.length - 1];
					for(int i = 0; i < store.length; i++) {
						store[i] = 0;
					}
				}
				
				for(int i = 0; i < store.length; i++) {
					
					if(data[i].equals("1")) {
						store[i] += 1;
					} 
				}
				
				total++;
		
			}
			
			String r = "";
			for(int i = 0; i < store.length; i++) {
				
				if(store[i] == total) { 
					r += i + ",";
				}
			}
			
			System.out.print(r);
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static List<String> getSeeds(){
		
		int no = EAConfigure.getInstance().pop_size;
		List<String> list = new ArrayList<String>();
		
		/*for (int i = 0; i < seeds.size(); i++ ) {
			   System.out.print(i+"\n");
			   list.add(seeds.get(i));	
			}*/
		
		
		
		int gap = seeds.size() / no;
		
		for (int i = 0; i < seeds.size(); i=i+gap ) {
		   System.out.print(i+"\n");
		   list.add(seeds.get(i));	
		}
		
		if (list.size() < no) {
			list.add(seeds.get(seeds.size()-1));
		}
		
		if (list.size() > no) {
			list.remove(list.size()-1);
		}
		
		for (int i = 0; i < list.size(); i++ ) {
			System.out.print(list.get(i) + "\n");
		}
		System.out.print(list.size());
		return list;
		
	}
	
	static void distribution (double[] values, double max, double min, boolean neg) {
		
		//double upper = neg? 0 - min : max;
		//double lower = neg? 0 - max : min;
		
		int sep_num = 100;
		
		double sep = (max - min)/sep_num;
		
		int[] new_v = new int[sep_num+1];
		
		for (int i = 0; i < sep_num+1; i++) {
			new_v [i] = 0;
			
		}
		
		for (int i = 0; i < values.length; i++) {
			
			int n = (int) ((values[i]-min) / sep);
			//System.out.print(sep + " : " + n + " : " + values[i] + "\n");
			new_v[n] = new_v[n] + 1;
		}
		
		if (neg) {
			int k = 0;
			for (int i = new_v.length - 1; i >0; i--) {
				//System.out.print("("+k+","+new_v[i]+")\n");
				System.out.print("(-"+(sep*i)+","+new_v[i]+")\n");
				k++;
			}
			System.out.print("--\n");
			for (int i = new_v.length - 1; i >0; i--) {
				//System.out.print("("+k+","+new_v[i]+")\n");
				System.out.print("(-"+(sep*i)+",0)\n");
				
			}
		} else {
			for (int i = 0; i < new_v.length; i++) {
				//System.out.print("("+i+","+new_v[i]+")\n");
				System.out.print("("+(sep*(i+1))+","+new_v[i]+")\n");
			}
			System.out.print("--\n");
			for (int i = 0; i < new_v.length; i++) {
				//System.out.print("("+i+","+new_v[i]+")\n");
				System.out.print("("+(sep*(i+1))+",0)\n");
			}
		}
		
		
	}
	
	private static void normalize(){
		double max =  17.894581279143072;
		double v = 4.1823277703510335;
		double min = 0;
		
		v = (v - min) / (max - min);
		
		System.out.print((0.3 * v) + 1.2);
		
		/**
		 *17.894581279143072
10.953841910378587
4.819035135705402
4.1823277703510335
1.0097075186941624
		 */
	}
	
	/**
	 * apache=0.08888888888888889;0.36666666666666664;0.6444444444444445;
	 * bdbc=0.011525532255482631;0.11996467982050739;0.37815312640389964;
	 * bdbj=0.025053422739665463;0.15032053643799279;0.5187532237860143;
	 * llvm=0.290950744558992;0.43413516609392905;0.7205040091638032;
	 * x264=0.26962281884538364;0.6158034940015544;0.9619841691577251;
	 * sql=0.11226371599815588;0.45804518211157225;0.6885661595205165;
	 */
	private static void run_normalize(){
		String[] a = new String[]{"13.0", "14.5", "15.5"};
		double max = 16.851;
		
		double min = 12.513;
		
		for (String s : a) {
			
			double v = Double.parseDouble(s);
			v = (v - min) / (max - min);
			
			System.out.print(v+";");
		}
		
	}
}
