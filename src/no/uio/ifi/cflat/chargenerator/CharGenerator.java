package no.uio.ifi.cflat.chargenerator;

/*
 * module CharGenerator
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;

/*
 * Module for reading single characters.
 */
public class CharGenerator {
    public static char curC, nextC;
	
    private static LineNumberReader sourceFile = null;
    private static String sourceLine;
    private static int sourcePos;
	
    public static void init() {
	try {
	    sourceFile = new LineNumberReader(new FileReader(Cflat.sourceName));
	} catch (FileNotFoundException e) {
	    Error.error("Cannot read " + Cflat.sourceName + "!");
	}
	try{
		sourceLine = sourceFile.readLine();
	}catch (IOException e){
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	sourcePos = 0;  curC = nextC = ' '; 
	readNext();  readNext();
    }
	
    public static void finish() {
	if (sourceFile != null) {
	    try {
		sourceFile.close();
	    } catch (IOException e) {}
	}
    }
	
    public static boolean isMoreToRead() throws IOException {
    	if(sourcePos < sourceLine.length() || sourceFile.ready()){
			return true;
		}
		return false;
    }
	
    public static int curLineNum() {
    	
    	return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }

    
    public static void readNext() {
	curC = nextC;
	
	/*
	 * Read the next line unless we are the end of it
	 */
		if(sourcePos < sourceLine.length()){
			nextC = sourceLine.charAt(sourcePos);
			sourcePos += 1;
		}else{
			System.out.print("New line\n");
			try{
				sourceLine = sourceFile.readLine();
				sourceLine += "  ";
				Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);
				
			}catch (IOException e){
				// TODO Auto-generated catch block
				// MONGO
				e.printStackTrace();
			}	
			if (sourceLine == null){
				//nextC = '?';
				return;
			}
			sourcePos = 0;
			nextC = sourceLine.charAt(sourcePos);
			sourcePos += 1;
			System.out.printf("curC: %c nextC: %c\n", curC, nextC);
		}
    }
  
    
    public static void skipLine(){
    	
    	Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);
    	sourcePos = 0;
    	try{
			sourceLine = sourceFile.readLine();
		}catch (IOException e){
			// TODO Auto-generated catch block
			// MERE MONGO
			
			e.printStackTrace();
		}
    }
}





