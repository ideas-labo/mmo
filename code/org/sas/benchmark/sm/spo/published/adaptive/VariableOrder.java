package org.sas.benchmark.sm.spo.published.adaptive;

import java.util.ArrayList;
import java.util.List;


public class VariableOrder {

	private static List<String> x264 = new ArrayList<String>();
	private static List<String> trimesh = new ArrayList<String>();
	private static List<String> storm_wc = new ArrayList<String>();
	private static List<String> storm_rs = new ArrayList<String>();
	private static List<String> ShapesAll = new ArrayList<String>();
	private static List<String> Adiac = new ArrayList<String>();
	private static List<String> vp9 = new ArrayList<String>();
	private static List<String> mariadb = new ArrayList<String>();
	private static List<String> llvm = new ArrayList<String>();
	private static List<String> mongodb = new ArrayList<String>();
	private static List<String> lrzip = new ArrayList<String>();
	
	
	static {
		// The excluded ones are still here but they do not affect the order
		String[] array = new String[]{
				"no_mbtree",
				"no_asm",
				"no_cabac",
				"no_scenecut",
				"aq_strength",
				"bframes",
				"qcomp",
				"qp",
				"ref",
				"rc_lookahead",
				"b_bias",
				"threads",
				"keyint",
				"crf",
				"scenecut",
				"seek",
				"ipratio"			
		};
		
		attach(x264, array);
		
	
		array = new String[]{
				"F",
				"smoother",
				"colorGS",
				"relaxParameter",
				"V",
				"Jacobi",
				"line",
				"zebraLine",
				"cycle",
				"alpha",
				"beta",
				"preSmoothing",
				"postSmoothing"
				
		};
		
		attach(trimesh, array);
		
		array = new String[]{
				"spouts",
				"max_spout",
				"spout_wait",
				"spliters",
				"counters",
				"netty_min_wait"
				
		};
		
		attach(storm_wc, array);
		
		
		array = new String[]{
				"spouts",
				"max_spout",
				"sorters",
				"emit_freq",
				"chunk_size",
				"message_size"
				
		};
		
		attach(storm_rs, array);
		
	
	    
	    
		array = new String[]{
				"﻿vm_type",
				"a",
				"b",
				"c",
				"d",
				"e",
				"f",
				"g",
				"h",
				"i",
				"j",
				"k",
				"l"
			};
			
	    attach(ShapesAll, array);
	 
	    
	    array = new String[]{
				"﻿vm_type",
				"a",
				"b",
				"c",
				"d",
				"e",
				"f",
				"g",
				"h",
				"i",
				"j",
				"k",
				"l"
			};
			
	    attach(Adiac, array);
	    
	
	    
	    
	    array = new String[]{
	    		"ee_instrument",
	    		"simplifycfg",
	    		"ipsccp",
	    		"called_value_propagation",
	    		"basicaa",
	    		"instcombine",
	    		"inline",
	    		"mergereturn",
	    		"jump_threading",
	    		"pgo_memop_opt",
	    		"tailcallelim",
	    		"licm",
	    		"sink",
	    		"gvn",
	    		"sccp",
	    		"adce"
			};
			
	    attach(llvm, array);
	    
	    array = new String[]{
	    		"journal",
	    		"nojournal",
	    		"journalCompression",
	    		"journalCompressionSnappy",
	    		"journalCompressionZlib",
	    		"ssl",
	    		"networkCompression",
	    		"networkCompressionSnappy",
	    		"networkCompressionZlib",
	    		"wireObjectCheck",
	    		"dataCompression",
	    		"dataCompressionSnappy",
	    		"dataCompressionZlib",
	    		"indexPrefixCompression",
	    		"journalCommitInterval",
	    		"cacheSize"
			};
			
	    attach(mongodb, array);
	    
	    array = new String[]{
	    		"encryption",
	    		"compression",
	    		"compressionBzip2",
	    		"compressionGzip",
	    		"compressionLzo",
	    		"compressionZpaq",
	    		"compressionLzma",
	    		"unlimitedWindowSize",
	    		"disableCompressibilityTesting",
	    		"level",
	    		"processorCount",
	    		"maxRam"
			};
			
	    attach(lrzip, array);
	    
	    array = new String[]{
	    		"twoPass",
	    		"quality",
	    		"bestQuality",
	    		"goodQuality",
	    		"rtQuality",
	    		"constantBitrate",
	    		"autoAltRef",
	    		"noAltRef",
	    		"allowResize",
	    		"columnTiling",
	    		"rowTiling",
	    		"threads",
	    		"arnrMaxFrames",
	    		"arnrStrength"
	 			};
	 			
	 	attach(vp9, array);
	 	    
	 	array = new String[]{
	 			"delayedInnodbLogFlush",
	 			"delayedInnodbLogWrite",
	 			"innodbFlushMethod",
	 			"fsyncFlush",
	 			"dsyncFlush",
	 			"directFlush",
	 			"binaryLog",
	 			"innodbDoubleWrite",
	 			"tempTableSize",
	 			"innodbBufferPoolSize",
	 			"innodbLogBufferSize"	
				};
				
		attach(mariadb, array);
	  
	    
	 
	    
	}
	
	
	
	
	public static List<String> getList(){
		

		if("x264".equals(Parser.selected)) {
			return x264;
		
		} else if("trimesh".equals(Parser.selected)) {
			return trimesh;
		} else if("storm-wc".equals(Parser.selected)) {
			return storm_wc;
		} else if("storm-rs".equals(Parser.selected)) {
			return storm_rs;
		
		} else if("dnn-sa".equals(Parser.selected)) {
			return ShapesAll;
		
		} else if("dnn-adiac".equals(Parser.selected)) {
			return Adiac;
		
		} else if("llvm".equals(Parser.selected)) {
			return llvm;
		
		} else if("mongodb".equals(Parser.selected)) {
			return mongodb;
		} else if("lrzip".equals(Parser.selected)) {
			return lrzip;
		} else if("vp9".equals(Parser.selected)) {
			return vp9;
		} else if("mariadb".equals(Parser.selected)) {
			return mariadb;
		}
		
		
		
		return null;
	}
	
	private static void attach(List<String> list, String[] array){
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		
	}
	
	public static void main(String[] arg) {
		for (int i = 0; i < x264.size(); i++) {
			//System.out.print(" <feature name=\""+X264.get(i)+"\" type=\"categorical\" optional=\"true\"/>\n");
			System.out.print("<item name=\""+x264.get(i)+"\" provision=\"0\" constraint=\"-1\" differences=\"1\" pre_to_max=\"0.7\" pre_of_max=\"0.1\" min=\"0\" max=\"1\" price_per_unit=\"0.5\"  />\n");
		}
	}
}
