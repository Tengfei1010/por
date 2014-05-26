/**
 * @author lzongren@gmail.com
 * 
 */
package com.lzongren.por.message;

import java.math.BigInteger;

/**
 * The interface for Message, a message would be divided into N blocks, each
 * block contains S sectors.
 * @author lzongren@gmail.com
 */
public interface Message {
	
	/**
	 * Get a specific block.
	 * @param i Index of the block.
	 * @return Bytes array of the block.
	 */
	public byte[] getBlock(int i);
	
	/**
	 * Get sector in block i with index j.
	 * @param i Index of the block.
	 * @param j Index of the sector.
	 * @return The sector as a BigInteger.
	 */
	public BigInteger getSector(int i, int j);

	/**
	 * Get number of blocks.
	 * @return Number of blocks.
	 */
	public int getN();

	/**
	 * Set number of blocks.
	 * @param n Number of blocks.
	 */
	public void setN(int n);

	/**
	 * Get number of sectors per block.
	 * @return Number of sectors.
	 */
	public int getS();

	/**
	 * Set number of sectors per block.
	 * @param s Number of sectors.
	 */
	public void setS(int s);

}
