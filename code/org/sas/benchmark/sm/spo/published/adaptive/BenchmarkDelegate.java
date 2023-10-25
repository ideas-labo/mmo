package org.sas.benchmark.sm.spo.published.adaptive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.sas.benchmark.sm.spo.Flash;
import org.sas.benchmark.sm.spo.Parser;
import org.ssase.model.Delegate;

import weka.core.DenseInstance;
import weka.core.Instance;

public class BenchmarkDelegate implements Delegate{

	static String prefix = "/Users/" + System.getProperty("user.name") + "/research/monitor/";
	private int obj_index = 0;
	public static Set<String> set = new HashSet<String>();
	
	
	public BenchmarkDelegate(int obj_index) {
		super();
		this.obj_index = obj_index;
	}

	
	

	@Override
	public double predict(double[] xValue) {
		
		if(Simulator.alg.contains("flash")) {
			return predict_flash(xValue);
		}
		
		String v = "";
		for(int i = 0; i < xValue.length; i++) {
			v += v.equals("")? (int)xValue[i] : ":" + (int)xValue[i];
		}
	
		HashMap<String, Double> map = obj_index == 0? Parser.map1 : Parser.map2;

		
		if(map.containsKey(v)) {
			
			
			
			
			double r = map.get(v);
			
			if(!Parser.map1.containsKey(v) || !Parser.map2.containsKey(v)) {
				return Double.MAX_VALUE;
			}
			
			if(Parser.map1.get(v) == 0 || Parser.map2.get(v) == 0) {
				return Double.MAX_VALUE;
			}
			// Only needed for certain benchmarks
			if(obj_index == 0 && !"mariadb".equals(AutoRun.benchmark)
					&& !"vp9".equals(AutoRun.benchmark)
					&& !"lrzip".equals(AutoRun.benchmark)
					&& !"mongodb".equals(AutoRun.benchmark)
					&& !"llvm".equals(AutoRun.benchmark)) {
				r = -1.0*r;
			}
			
			if(!set.contains(v)) {
				try {
					System.out.print("New measurement of configuration " + v + " starts\n");
					Thread.sleep(emulatedTime());
					System.out.print("New measurement completed\n");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				set.add(v);
			} 			
			
			
			return r*100;
		} else {
			return Double.MAX_VALUE;
		}
		
	
	}
	
	public double predict_flash(double[] xValue) {
		
		
		String v = "";
		for(int i = 0; i < xValue.length; i++) {
			v += v.equals("")? (int)xValue[i] : ":" + (int)xValue[i];
		}
		
		String raw = Parser.index_to_raw.get(v);
		if(raw == null) {
			return Double.MAX_VALUE;
		}
		
		String[] split = raw.split(":");
	
		
		final Instance inst = new DenseInstance(split.length + 1);
		for (int i = 0; i < split.length; i++) {
			inst.setValue(i, Double.parseDouble(split[i]));
		}

		
		double r = 0.0;
		double a = 0.0;
		try {
			
			inst.setDataset(Flash.datasets[obj_index]);
			
			 r = obj_index == 0? Flash.models[0].distributionForInstance(inst)[0] : 
				Flash.models[1].distributionForInstance(inst)[0];
			 
			 a = obj_index == 0? Parser.map1.get(v) : 
				 Parser.map2.get(v);
			
			if(obj_index == 0) {
				//r = 1.0/r;
				if(AutoRun.isNegative) {
					 r = -1.0*r;
				} 
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.print(raw + ", predicted=" + r + ", actual=" + a + "\n");
		
		return r;
		
	
	}
	
	
    public double predict2(double[] xValue) {
		
		String v = "";
		for(int i = 0; i < xValue.length; i++) {
			v += v.equals("")? (int)xValue[i] : ":" + (int)xValue[i];
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		// Windows
		processBuilder.command("sudo sh", prefix + "system-interface", v);
		processBuilder.redirectErrorStream(true);
		
		double r = 0.0;

		try {

			Process process = processBuilder.start();
			int exitCode = process.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				r =  Double.parseDouble(line);
				break;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return r;
	}


    private long emulatedTime() {
    	
    	long budget = 7200000;
    	if ("trimesh".equals(AutoRun.benchmark)) {
    		return budget/1000;
    	} else if ("x264".equals(AutoRun.benchmark)) {
    		return budget/2500;
    	} else if ("storm-wc".equals(AutoRun.benchmark)) {
    		return budget/600;
    	} else if ("storm-rs".equals(AutoRun.benchmark)) {
    		return budget/900;
    	} else if ("dnn-sa".equals(AutoRun.benchmark)) {
    		return budget/400;
    	} else if ("dnn-adiac".equals(AutoRun.benchmark)) {
    		return budget/400;
    	} else if ("mariadb".equals(AutoRun.benchmark)) {
    		return budget/400;
    	} else if ("mongodb".equals(AutoRun.benchmark)) {
    		return budget/500;
    	} else if ("vp9".equals(AutoRun.benchmark)) {
    		return budget/700;
    	} else if ("lrzip".equals(AutoRun.benchmark)) {
    		return budget/400;
    	} else if ("llvm".equals(AutoRun.benchmark)) {
    		return budget/600;
    	}
    	
    	return 0;
    }
}
