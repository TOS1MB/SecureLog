package secureLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.MDC;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class SecureLogger {

	public String secret;
	public String loggingPath;
	public String FQCN = "secureLog.SecureLogger";
	public BufferedReader reader;
	public PrintWriter writer;

	public SecureLogger(String secret, String loggingPath) {
		this.secret = secret;
		this.loggingPath = loggingPath;
	}

	public enum LogLevel {
		TRACE, DEBUG, INFO, WARN, ERROR, FATAL
	};


	public void secureLog(LogLevel logLevel, String userId, String activity, String activityDetail) {

		switch (logLevel) {
		case TRACE:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.TRACE, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		case DEBUG:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.DEBUG, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		case INFO:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.INFO, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		case WARN:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.WARN, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		case ERROR:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.ERROR, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		case FATAL:
			MDC.put("userId", userId);
			MDC.put("activity", activity);
			MDC.put("activityDetail", activityDetail);
			LogManager.getLogger().log(Level.FATAL, activityDetail);
			readFileAndInsertHMAC();
			MDC.clear();
			break;
		default:
			System.err.println("No such Log Level");
			break;
		}

	}

	public void readFileAndInsertHMAC() {
		try {
			File file = new File(loggingPath);
			//File temp = File.createTempFile("file", ".log", file.getParentFile());
			String charset = "UTF-8";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			//writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
			String line, lastLine = "";
			//List<String> lines = new ArrayList<String>();
			int lineCounter = 0;

			/*
			 * Reading log file and inserting hmac
			 */
			
			while((line = reader.readLine()) != null) {
				lastLine = line;
				lineCounter++;
			}


			String hmac = Crypt.generateHmac(secret, lastLine, lineCounter); // generating hmac for entry
																				
			//Writing the HMAC to file and separating line for new Log-Entry
			Files.write(Paths.get(loggingPath), (hmac + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//writer.close();

		}
	}

	public boolean verifyLog() {
		try {
			File file = new File(loggingPath);
			String charset = "UTF-8";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			String line;
			int lineCounter = 0;

			while ((line = reader.readLine()) != null) {

				lineCounter++;

				if (line.isEmpty()) { // checking if line is empty
					return false;
				}

				String HMACtoVerifiy = line.substring(line.lastIndexOf("=") + 1);// extracting hmac from line
																					
				
				String[] parts = line.split("="); // separating line into values
				
				try {//if HMAC is not in the right place or there isn't even one
					
					String HMACfromLine = parts[4]; // extracting hmac-value to delete it from line for computing the hmac of the original log
					String logWithoutHMAC = line.replace(HMACfromLine, "");
					
					
					String actualHMAC = Crypt.generateHmac(secret, logWithoutHMAC, lineCounter);
					

					if (HMACtoVerifiy.equals(actualHMAC)) {
						continue;
					} else
						return false;
				} catch (Exception e) {
					return false;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
 
}
