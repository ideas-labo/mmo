package org.sas.benchmark.sm.spo.published.adaptive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.femosaa.core.EAConfigure;
import org.femosaa.core.SASAlgorithmAdaptor;
import org.ssase.util.Repository;
import org.ssase.util.Ssascaling;
 
public class AutoRun {

	private static String[] weights = new String[] { "1.0-0.0", "0.0-1.0" };
	private static String[] single_algs = new String[] { "sa", "ga", "hc", "rs" };
	private static String[] multi_algs = new String[] { "nsgaii" };
	private static String[] multi_algs_flash = new String[] { "flash-nsgaii", "flash-rs"  };
	public static String benchmark = "LSTM";
	public static String form = "linear";// linear, sqrt, square
	public static int index = 0;
	public static int pop = 0;
	public static boolean isNegative = false;
	private static double[] w_a = new double[] { 0.01, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0, 10 };

	public static void main(String[] args) {

		String s = args[0];

		if ("trimesh".equals(s)) {
			benchmark = "trimesh";
		} else if ("x264".equals(s)) {
			benchmark = "x264";
		} else if ("storm-wc".equals(s)) {
			benchmark = "storm-wc";
		} else if ("storm-rs".equals(s)) {
			benchmark = "storm-rs";
		} else if ("dnn-sa".equals(s)) {
			benchmark = "dnn-dsr";
		} else if ("dnn-adiac".equals(s)) {
			benchmark = "dnn-coffee";
		} else if ("mariadb".equals(s)) {
			benchmark = "LSTM";
		} else if ("mongodb".equals(s)) {
			benchmark = "LSTM";
		} else if ("vp9".equals(s)) {
			benchmark = "LSTM";
		} else if ("lrzip".equals(s)) {
			benchmark = "LSTM";
		} else if ("llvm".equals(s)) {
			benchmark = "LSTM";
		} else {
			System.out.print("The entered configurable system is not supported at the moment!");
			return;
		}

		if (args.length == 1) {
			Simulator.n = 50;
		} else {

			try {

				int n = Integer.parseInt(args[1]);
				Simulator.n = n;
			} catch (Throwable t) {
				System.out.print("The second argument needs to be an integer (the number of runs)!");
			}

		}

		prepare();

		mo_new();
		mo();
		so();
		
		flash_mo();
		flash_so();

	}

	public static void mo() {

		Parser.selected = benchmark;
		Simulator.setup();
		// SASAlgorithmAdaptor.isSeedSolution =false;
		SASAlgorithmAdaptor.isFuzzy = true;
		SASAlgorithmAdaptor.isBoundNormalizationForTarget = false;
		SASAlgorithmAdaptor.logGenerationOfObjectiveValue = -1;

		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] forms = new String[] { "linear"};
		int[] ii = new int[] { 0, 1 };
		// index = 0;
		// double best = Double.MAX_VALUE;
		// double best_w = 0.0;

		for (String ff : forms) {

			for (int i : ii) {
				index = i;
				for (double w : w_a) {
					form = ff;
					CombineProposition.beta = w;
					run_MOEA(index, w, form, false);
					/*
					 * double a12 = Data.readObjective(index, form);
					 * 
					 * if (a12 > best) { best = a12; best_w = w; }
					 */

					/*
					 * double m = Data.readObjectiveMedian(index, ff);
					 * 
					 * if (m < best) { best = m; best_w = w; }
					 */
					System.out.print("Objective: " + index + ", on form: " + form + ", w: " + w + ", i: " + i);
				}

			}
		}

		SASAlgorithmAdaptor.isFuzzy = false;
		run_MOEA(-1, -1, "none", false);

		/*
		 * form = "linear"; CombineProposition.beta = 10; run_MOEA(form); form = "sqrt";
		 * CombineProposition.beta = 0.8; run_MOEA(form); form = "square";
		 * CombineProposition.beta = 0.01; run_MOEA(form);
		 */
	}
	
	public static void flash_mo() {

		Parser.selected = benchmark;
		Simulator.setup();
		// SASAlgorithmAdaptor.isSeedSolution =false;
		SASAlgorithmAdaptor.isFuzzy = true;
		SASAlgorithmAdaptor.isBoundNormalizationForTarget = true;
		SASAlgorithmAdaptor.logGenerationOfObjectiveValue = -1;

		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] forms = new String[] { "linear" };
		int[] ii = new int[] { 0, 1 };
		// index = 0;
		// double best = Double.MAX_VALUE;
		// double best_w = 0.0;
		
		double[] local = new double[] {1.0};

		for (String ff : forms) {

			for (int i : ii) {
				index = i;
				for (double w : local) {
					form = ff;
					CombineProposition.beta = w;
					run_Flash(index, w, form, "flash-nsgaii");
					/*
					 * double a12 = Data.readObjective(index, form);
					 * 
					 * if (a12 > best) { best = a12; best_w = w; }
					 */

					/*
					 * double m = Data.readObjectiveMedian(index, ff);
					 * 
					 * if (m < best) { best = m; best_w = w; }
					 */
					System.out.print("Objective: " + index + ", on form: " + form + ", w: " + w + ", i: " + i);
				}

			}
		}
	}
	
	public static void mo_new() {

		Parser.selected = benchmark;
		Simulator.setup();
		// SASAlgorithmAdaptor.isSeedSolution =false;
		SASAlgorithmAdaptor.isFuzzy = true;
		SASAlgorithmAdaptor.isBoundNormalizationForTarget = true;
		SASAlgorithmAdaptor.logGenerationOfObjectiveValue = -1;

		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] forms = new String[] { "linear" };
		int[] ii = new int[] { 0, 1 };
		// index = 0;
		// double best = Double.MAX_VALUE;
		// double best_w = 0.0;
		
		double[] local = new double[] {1.0};

		for (String ff : forms) {

			for (int i : ii) {
				index = i;
				for (double w : local) {
					form = ff;
					CombineProposition.beta = w;
					run_MOEA(index, w, form, true);
					/*
					 * double a12 = Data.readObjective(index, form);
					 * 
					 * if (a12 > best) { best = a12; best_w = w; }
					 */

					/*
					 * double m = Data.readObjectiveMedian(index, ff);
					 * 
					 * if (m < best) { best = m; best_w = w; }
					 */
					System.out.print("Objective: " + index + ", on form: " + form + ", w: " + w + ", i: " + i);
				}

			}
		}

	}

	public static void so() {
		// Parser.selected = benchmark;
		// Simulator.setup();
		// SASAlgorithmAdaptor.isSeedSolution = false;
		SASAlgorithmAdaptor.isFuzzy = false;
		SASAlgorithmAdaptor.isBoundNormalizationForTarget = false;
		SASAlgorithmAdaptor.logGenerationOfObjectiveValue = -1;
		SASAlgorithmAdaptor.isWeightedSumNormalized = false;

		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String p : weights) {

			String[] s = p.split("-");
			double[] w = new double[s.length];
			for (int i = 0; i < s.length; i++) {
				w[i] = Double.parseDouble(s[i]);
			}

			for (String alg : single_algs) {

				/*
				 * if("ga".equals(alg) && (benchmark.equals("SS-M") ||
				 * benchmark.equals("SS-N"))) { SASAlgorithmAdaptor.isSeedSolution = true; }
				 * else { SASAlgorithmAdaptor.isSeedSolution = true; }
				 */

				Simulator.alg = alg;
				Simulator.weights = w;

				Simulator.main_test();

				File source = new File(System.getProperty("user.dir") + "/results/temp");
				File r = new File(
						System.getProperty("user.dir") + "/results/" + p + "/" + benchmark + "/" + alg + "/" + "/sas");
				File dest = new File(
						System.getProperty("user.dir") + "/results/" + p + "/" + benchmark + "/" + alg + "/" + "/sas");

				if (r.exists()) {
					System.out.print("Remove " + r + "\n");
					try {
						delete(r);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (!dest.exists()) {
					dest.mkdirs();
				}

				try {
					copyFolder(source, dest);
					if (source.exists()) {
						System.out.print("Remove " + source + "\n");
						delete(source);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.print("End of " + "/Users/" + System.getProperty("user.dir") + "/results/" + p + "/"
						+ benchmark + "/" + alg + "/" + "\n");
				// try {
				// Thread.sleep((long)2000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}
		}

	}
	
	public static void flash_so() {
		// Parser.selected = benchmark;
		// Simulator.setup();
		// SASAlgorithmAdaptor.isSeedSolution = false;
		SASAlgorithmAdaptor.isFuzzy = false;
		SASAlgorithmAdaptor.isBoundNormalizationForTarget = false;
		SASAlgorithmAdaptor.logGenerationOfObjectiveValue = -1;
		SASAlgorithmAdaptor.isWeightedSumNormalized = false;

		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String p : weights) {

			String[] s = p.split("-");
			double[] w = new double[s.length];
			for (int i = 0; i < s.length; i++) {
				w[i] = Double.parseDouble(s[i]);
			}

			

				/*
				 * if("ga".equals(alg) && (benchmark.equals("SS-M") ||
				 * benchmark.equals("SS-N"))) { SASAlgorithmAdaptor.isSeedSolution = true; }
				 * else { SASAlgorithmAdaptor.isSeedSolution = true; }
				 */

				Simulator.alg = "flash-rs";
				Simulator.weights = w;

				Simulator.main_test();

				File source = new File(System.getProperty("user.dir") + "/results/temp");
				File r = new File(
						System.getProperty("user.dir") + "/results/" + p + "/" + benchmark + "/" + Simulator.alg + "/" + "/sas");
				File dest = new File(
						System.getProperty("user.dir") + "/results/" + p + "/" + benchmark + "/" + Simulator.alg + "/" + "/sas");

				if (r.exists()) {
					System.out.print("Remove " + r + "\n");
					try {
						delete(r);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (!dest.exists()) {
					dest.mkdirs();
				}

				try {
					copyFolder(source, dest);
					if (source.exists()) {
						System.out.print("Remove " + source + "\n");
						delete(source);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.print("End of " + "/Users/" + System.getProperty("user.dir") + "/results/" + p + "/"
						+ benchmark + "/" + Simulator.alg + "/" + "\n");
				// try {
				// Thread.sleep((long)2000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			
		}

	}
	
	public static void run_Flash(int i, double w, String form, String alg) {
		
			Simulator.alg = alg;

			Simulator.main_test();

			File source = new File(System.getProperty("user.dir") + "/results/temp");
			File r = new File(System.getProperty("user.dir") + "/results/" + benchmark + "/" + form + "/" + alg + "/"
					+ i + "/" + w + "/" + "/sas");
			File dest = new File(System.getProperty("user.dir") + "/results/" + benchmark + "/" + form + "/" + alg + "/"
					+ i + "/" + w + "/" + "/sas");

			if (r.exists()) {
				System.out.print("Remove " + r + "\n");
				try {
					delete(r);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!dest.exists()) {
				dest.mkdirs();
			}

			try {
				copyFolder(source, dest);
				if (source.exists()) {
					System.out.print("Remove " + source + "\n");
					delete(source);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("End of " + "/Users/" + System.getProperty("user.dir") + "/results" + "/" + benchmark + "/"
					+ form + "/" + alg + "/" + i + "/" + w + "/" + "\n");

		
		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void run_MOEA(int i, double w, String form, boolean main) {
		for (String alg : multi_algs) {
			Simulator.alg = alg;

			Simulator.main_test();

			File source = new File(System.getProperty("user.dir") + "/results/temp");
			File r = new File(System.getProperty("user.dir") + "/results/" + benchmark + "/" + form + "/" + alg + "/"
					+ i + "/" + (main? "1.0-mmo" : w) + "/" + "/sas");
			File dest = new File(System.getProperty("user.dir") + "/results/" + benchmark + "/" + form + "/" + alg + "/"
					+ i + "/" + (main? "1.0-mmo" : w) + "/" + "/sas");

			if (r.exists()) {
				System.out.print("Remove " + r + "\n");
				try {
					delete(r);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!dest.exists()) {
				dest.mkdirs();
			}

			try {
				copyFolder(source, dest);
				if (source.exists()) {
					System.out.print("Remove " + source + "\n");
					delete(source);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.print("End of " + "/Users/" + System.getProperty("user.dir") + "/results" + "/" + benchmark + "/"
					+ form + "/" + alg + "/" + i + "/" + (main? "1.0-mmo" : w) + "/" + "\n");

		}
		File f = new File(System.getProperty("user.dir") + "/results/temp");

		try {
			if (f.exists()) {
				delete(f);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void prepare() {

		org.ssase.util.Logger.prefix = System.getProperty("user.dir") + "/results/temp/";
		org.femosaa.util.Logger.prefix = System.getProperty("user.dir") + "/results/temp/";
		Repository.mac_path = System.getProperty("user.dir") + "/weight";
		Repository.other_path = System.getProperty("user.dir") + "/weight";
		if ("trimesh".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 1000;
			Ssascaling.dom0 = "benchmark/flash/dom0_ssm.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_ssm.xml";
			SASAlgorithmAdaptor.isSeedSolution = true;
		} else if ("x264".equals(AutoRun.benchmark)) {
			pop = 50;
			EAConfigure.getInstance().measurement = 2500;
			Ssascaling.dom0 = "benchmark/flash/dom0_ssn.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_ssn.xml";
			SASAlgorithmAdaptor.isSeedSolution = true;
		} else if ("mariadb".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 400;
			Ssascaling.dom0 = "benchmark/flash/dom0_mariadb.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_mariadb.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("storm-wc".equals(AutoRun.benchmark)) {
			pop = 50;
			EAConfigure.getInstance().measurement = 600;
			Ssascaling.dom0 = "benchmark/flash/dom0_ssk.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_ssk.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("vp9".equals(AutoRun.benchmark)) {
			pop = 30;
			EAConfigure.getInstance().measurement = 700;
			Ssascaling.dom0 = "benchmark/flash/dom0_vp9.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_vp9.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("storm-rs".equals(AutoRun.benchmark)) {
			pop = 50;
			EAConfigure.getInstance().measurement = 900;
			Ssascaling.dom0 = "benchmark/flash/dom0_ssj.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_ssj.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("lrzip".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 400;
			Ssascaling.dom0 = "benchmark/flash/dom0_lrzip.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_lrzip.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("mongodb".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 500;
			Ssascaling.dom0 = "benchmark/flash/dom0_mongodb.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_mongodb.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("dnn-sa".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 400;
			Ssascaling.dom0 = "benchmark/flash/dom0_dnn_shapesall.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_dnn_shapesall.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("dnn-adiac".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 400;
			Ssascaling.dom0 = "benchmark/flash/dom0_dnn_adiac.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_dnn_adiac.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} else if ("llvm".equals(AutoRun.benchmark)) {
			pop = 20;
			EAConfigure.getInstance().measurement = 600;
			Ssascaling.dom0 = "benchmark/flash/dom0_llvm.xml";
			Ssascaling.feature_model = "benchmark/flash/feature_model_llvm.xml";
			SASAlgorithmAdaptor.isSeedSolution = false;
		} 
		
		
		if(!"mariadb".equals(AutoRun.benchmark)
				&& !"vp9".equals(AutoRun.benchmark)
				&& !"lrzip".equals(AutoRun.benchmark)
				&& !"mongodb".equals(AutoRun.benchmark)
				&& !"llvm".equals(AutoRun.benchmark)) {
			isNegative = true;
		}
	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to " + dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}

	}

	public static void delete(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				// System.out.println("Directory is deleted : "
				// + file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					// System.out.println("Directory is deleted : "
					// + file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			// System.out.println("File is deleted : " +
			// file.getAbsolutePath());
		}
	}
}
