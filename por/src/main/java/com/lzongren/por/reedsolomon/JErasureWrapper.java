/**
 * @author lzongren@gmail.com
 */
package com.lzongren.por.reedsolomon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_postdam.hpi.jerasure.Decoder;
import de.uni_postdam.hpi.jerasure.Encoder;
import de.uni_postdam.hpi.utils.FileUtils;

/**
 * @author lzongren@gmail.com
 *
 */
public class JErasureWrapper {

	private final static Logger LOGGER = 
			LogManager.getLogger(JErasureWrapper.class.getName());
	
	private int k;
	
	private int m;
	
	private int w = 8;
	
	/**
	 * Construct JErasureWrapper.
	 * @param k Number of original devices (Original size).
	 * @param m Number of additional devices (Additional size).
	 * @param w Size of Galois Field.
	 */
	public JErasureWrapper(int k, int m, int w) {
		this.k = k;
		this.m = m;
		this.w = w;
		LOGGER.info(String.format("Constructing JErasureWrapper with RS(%d,%d)" +
				" in GF(2^%d)", k, m, w));
	}
	
	/**
	 * Construct JErasureWrapper in Galois Field 2^8.
	 * @param k Number of original devices (Original size).
	 * @param m Number of additional devices (Additional size).
	 */
	public JErasureWrapper(int k, int m) {
		this(k, m, 8);
	}
	
	/**
	 * Using Reed-Solomon to encode a file. Returns a collection of files, each
	 * one is a part of Reed-Solomon share (E.g., using RS(9,3) means there are
	 * total 12 shares, the return is 12 shares of file).
	 * @param dataFile The file contains information to process.
	 * @return A collection of shares after Reed-Solomon encoding.
	 */
	public File[] encodeToFiles(File dataFile) {
		File[] files = new File[k+m];
		int counter = 0;
		
		Encoder encoder = new Encoder(k, m, w);
		encoder.encode(dataFile);
		
		File[] kParts = FileUtils.collectFiles(dataFile.getAbsolutePath(), "k", k);
		File[] mParts = FileUtils.collectFiles(dataFile.getAbsolutePath(), "m", m);
		
		for (File onePart: kParts) {
			files[counter++] = onePart;
		}
		for (File onePart: mParts) {
			files[counter++] = onePart;
		}
		
		LOGGER.info("RS encode file " + dataFile + " into " + files.length + 
				" parts");
		return files;
	}
	
	/**
	 * Using Reed-Solomon to encode a file. Returns a collection of strings, each
	 * one is a part of Reed-Solomon share (E.g., using RS(9,3) means there are
	 * total 12 shares, the return is 12 shares of file).
	 * @param dataFile The file contains information to process.
	 * @return A collection of shares after Reed-Solomon encoding.
	 */
	public String[] encodeToStrings(String filePath) {
		File dataFile = new File(filePath);
		String[] blocks = new String[k+m];
		int counter = 0;
		
		Encoder encoder = new Encoder(k, m, w);
		encoder.encode(dataFile);
		
		try {
			File[] kParts = FileUtils.collectFiles(dataFile.getAbsolutePath(), "k", k);
			File[] mParts = FileUtils.collectFiles(dataFile.getAbsolutePath(), "m", m);
			
			for (File onePart: kParts) {
				blocks[counter++] = org.apache.commons.io.FileUtils.readFileToString(onePart);
			}
			for (File onePart: mParts) {
				blocks[counter++] = org.apache.commons.io.FileUtils.readFileToString(onePart);
			}
			
		} catch (IOException e) {
			LOGGER.error("IOException while RS-encoding file " + filePath);
			e.printStackTrace();
		}
		
		LOGGER.info("RS encode file " + dataFile + " into " + blocks.length + 
				" parts");
		return blocks;
	}
	
	/**
	 * Reed-Solomon decode the file with parts. (JErasure would look for the
	 * encoded parts according to the original file and do the reconstruction).
	 * @param filePath Path to the original file.
	 * @param originalFileSize Size of the original file.
	 */
	public void decode(String filePath, long originalFileSize) {
		File dataFile = new File(filePath);

		Decoder decoder = new Decoder(dataFile, k, m, w);
		decoder.decode(originalFileSize);
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		
	}

}
