/**
 * 
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * @author ya
 * 
 */
public class Creature {

	/**
	 * 
	 */
	public String folder = "G:/world/";

	private File file;
	private Monitor m;

	/**
	 * @param DNA
	 * @param dir
	 */
	public Creature(String DNA, String dir) {
		file = new File(DNA);
		folder = dir;
		if (file.exists())
			start();
	}

	/**
	 */
	private void start() {
		try {
			String x = file.getCanonicalPath();
			System.out.println(x);
			m = new Monitor(Runtime.getRuntime().exec(file.getCanonicalPath(),
					new String[] {}, new File(folder)));
			m.start();
		} catch (Exception e) {
			file.delete();
		}
	}

	class Monitor extends Thread {
		private BufferedReader reader;
		private Process process;

		public Monitor(Process process) {
			this.process = process;
			this.reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
		}

		@Override
		public void run() {
			for (int j = 0; j < 100; j++) {
				try {
					if (process.exitValue() == 0) {
						String msg;
						for (int i = 0; i < 7
								&& (msg = reader.readLine()) != null; i++) {
							if (msg.equals("copy")) {
								final String name = reader.readLine();
								born(name);
							}
						}
						file.delete();
						break;
					} else {

						file.delete();
						break;
					}
				} catch (Exception e) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		private void born(String name) throws IOException {

			File file2 = new File(folder + name);
			if (!file2.exists())
				return;
			FileInputStream is = new FileInputStream(file);
			FileInputStream is2 = new FileInputStream(file2);

			File baby = null;
			String babyname = null;
			Random r = new Random();
			while (baby == null || baby.exists()) {
				babyname = Math.abs(new Random().nextInt()) + ".exe";
				baby = new File(folder + babyname);
			}
			System.out.println("born: " + babyname + " by " + name + "&"
					+ file.getName());
			baby.createNewFile();
			FileOutputStream os = new FileOutputStream(baby);

			int b = 0, b1 = 0;
			while (true) {
				if (b == -1 && b1 == -1)
					break;

				if (b != -1)
					b = is.read();
				if (b1 != -1)
					b1 = is2.read();

				if (r.nextDouble() < 1e-5) {
					b = r.nextInt();
					b1 = r.nextInt();
				}

				if (r.nextDouble() < 1e-5) {
					os.write(r.nextInt());
				}
				if (r.nextDouble() < 1e-5) {
					continue;
				}

				if (r.nextBoolean()) {
					if (b != -1)
						os.write(b);
				} else {
					if (b1 != -1)
						os.write(b1);
				}
			}

			os.flush();
			os.close();
			new Creature(folder + babyname, folder);
			is.close();
			is2.close();
		}
	}
}
