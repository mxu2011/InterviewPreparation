package com.Maxeler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class VM {
	private static String filePath = new File("").getAbsolutePath();
	private static final String inputPath = filePath.concat(new String("/src/com/input/task1.bin"));
	private static final String outputPath = filePath.concat("/src/com/output/output.txt");
	private static final int lineSize = 8;
	
	/** Run the example. 
	 * @throws UnsupportedEncodingException */
	public static void main(String[] Args) throws UnsupportedEncodingException {
		VM test = new VM();
	    //read in the bytes
		byte[] fileContents = test.read(inputPath);
		load(fileContents);
	    //write it back out to a different file name
	    //test.write(fileContents, outputPath);
	}
	
	/** Read the given binary file, and return its contents as a byte array.*/ 
	byte[] read(String aInputFileName){
		log("Reading in binary file named : " + aInputFileName);
		File file = new File(aInputFileName);
		log("File size: " + file.length());
		byte[] result = new byte[(int)file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
		        input = new BufferedInputStream(new FileInputStream(file));
		        while (totalBytesRead < result.length){
		        	int bytesRemaining = result.length - totalBytesRead;
		        	//input.read() returns -1, 0, or more :
		        	int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
		        	if (bytesRead > 0) {
		        		totalBytesRead = totalBytesRead + bytesRead;
		        	}
		        }
		        /*
		         the above style is a bit tricky: it places bytes into the 'result' array; 
		         'result' is an output parameter;
		         the while loop usually has a single iteration only.
		        */
		        log("Num bytes read: " + totalBytesRead);
		     }
		     finally {
		        log("Closing input stream.");
		        input.close();
		     }
		}
		catch (FileNotFoundException ex) {
			log("File not found.");
		}
		catch (IOException ex) {
		    log(ex);
		}
		return result;
	}
	  
	/**
	 Write a byte array to the given file. 
	 Writing binary data is significantly simpler than reading it. 
	*/
	void write(byte[] aInput, String aOutputFileName){
		log("Writing binary file..." + aOutputFileName);
	    try {
	    	OutputStream output = null;
	    	try {
	    		output = new BufferedOutputStream(new FileOutputStream(aOutputFileName));
	    		output.write(aInput);
	    	}
	    	finally {
	    		output.close();
	    	}
	    }
	    catch(FileNotFoundException ex){
	      log("File not found.");
	    }
	    catch(IOException ex){
	      log(ex);
	    }
	    log("Complete writing!");
	}
	  
	 private static void log(Object aThing){
		 System.out.println(String.valueOf(aThing));
	 }
	 
	 private static int getNumber(byte[] image, int offset) 
			 throws UnsupportedEncodingException {
		 int d = 0;
		 // lineSize == 8
		 // item in index 8 is new line sign '\n'
		 for (int i = 0; i < lineSize; i++) {
			 String digit = new String(new byte[] {image[i + offset * (lineSize + 1)]}, "UTF-8");
			 int dTmp = Integer.parseInt(digit, 16);
			 if (dTmp > 0) {
				 for (int j = 0; j < (lineSize - 1 - i); j++) {
					 dTmp *= 16; // convert to decimal number
				 }
			 }
			 d += dTmp;
		 }
		 return d;
	 }
	 
	 private static void storeInstruction(byte[] image, int[] data, int imageSize) 
			 throws UnsupportedEncodingException {
		 log("byte array size: " + image.length);
		 for (int i = 0; i < imageSize; i++) {  
			 data[i] = getNumber(image, i + 2); // instruction start from line in index 2
		 }
	 }

	 private static int f(int[] data, int sp, int v) {
		 sp = sp - 1;
		 data[sp] = v;
		 return sp;
	 }
	 
	 private static int g(int[] data, int sp) {
			int v = data[sp];
			sp = sp + 1;
			return v;
	 }
	 
	 private static void executeInstruction(int[] data) {
		 int sp = data.length;
		 for (int ip = 0; ip < data.length; ip++) {
			 int curInstruction = data[ip];
			 ip = ip + 1;
		   	 // decode instruction
			 int binop = 1 << 31;
			 binop &= data[ip];
			 int operation = ((1 << 7) - 1) << 24;
			 operation &= data[ip];
			 int optionalData = (1 << 24) - 1;
			 optionalData &= data[ip];
			 // perform action based on operation
			 if (binop == 0) {
				 int addr; 
				 switch (operation) {
				 	case 0: // pop 
				 		sp = sp + 1;
				 		break;
				 		
				 	case 1: // "push <const>"
				 		sp = f(data, sp, optionalData);
				 		break;

				 	case 2: // "push ip"
				 		sp = f(data, sp, ip);
				 		break;
				 		
				 	case 3: // "push sp"
				 		sp = f(data, sp, sp);
				 		break;

				 	case 4: // "load"
				 		addr = g(data, sp);
				 		sp++; // sp is a local variable and update after g() operation every time
				 		sp = f(data, sp, data[addr]);
				 		break;

				 	case 5: // "store"
				 		int st_data = g(data, sp);
				 		sp++;
				 		addr = g(data, sp);
				 		sp++;
				 		data[addr] = st_data;
				 		break;

				 	case 6: // "jmp"
				 		int cond = g(data, sp);
				 		sp++;
				 		addr = g(data, sp);
				 		sp++;
				 		if (cond != 0) { // if cond is not equal to zero then set ip = addr
				 			ip = addr;
				 		}
				 		break;

				 	case 7: // "not"
				 		if (g(data, sp) == 0)  { // if g() equals 0 then f(1) else f(0)
				 			sp = f(data, sp, 1);
				 		} else {
				 			sp = f(data, sp, 0);
				 		}
				 		break;
				 		
				 	case 8: // "putc"
				 		// output exactly one byte = (g() & 0xff) to stdout
				 		log(g(data, sp) & 0xff);
				 		sp++;
				 		// Note: Output from the supplied VM images will always be ASCII text when
				 		// functioning correctly and will use '\n' (= 0x0A) to indicate new-line.
				 		break;

				 	case 9: // "getc"
					int x = 0;
						try { 
							x = System.in.read(); // read exactly one byte from stdin
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
				 		x = // cast x to 32bits
				 		sp = f(data, sp, x & 0xff); // x & 0xff, hexa to dec
				 		break;

				 	case 10: // (0x0A) - halt
				 		return; // Stop execution

				 	default:
				 		log("Invalid operation!");
				 		break;
				 }
			 } else {
				 
			 }
		 }
	 }
	 
	 private static void load(byte[] image) throws UnsupportedEncodingException {		 
		 int dataSize = getNumber(image, 0); log("data size: " + dataSize);
		 int imageSize = getNumber(image, 1); log("image size: " + imageSize);
		 int[] data = new int[dataSize];
		 storeInstruction(image, data, imageSize);
		 log("4th instruction: " + data[3] + ", hexadecimal: " + 
				 Integer.toHexString(data[3]));
		 
		 
		 String decoded = "";
		 for (int i = 0; i < lineSize; i++) { 
			 decoded += new String(new byte[] {image[i + 5 * (lineSize + 1)]}, "UTF-8");
		 }
		 log("decoded: " + decoded);
		 
		 log("origin2: " + image[0]);
		 String decoded2 = "";
		 for (int i = 0; i < 1; i++) { 
			 decoded2 += new String(new byte[] {image[i]}, "UTF-8");
		 }
		 log("decoded2: " + decoded2);
	 }
}
