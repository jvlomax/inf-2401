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

/*
 * Module for reading single characters.
 */
public class CharGenerator {
    public static char curC, nextC;
	
    private static LineNumberReader sourceFile = null;
    private static String sourceLine;
    private static int sourcePos;
    private static int soureLineNum; //this is added by us
	
    public static void init() {
	try {
	    sourceFile = new LineNumberReader(new FileReader(Cflat.sourceName));
	} catch (FileNotFoundException e) {
	    Error.error("Cannot read " + Cflat.sourceName + "!");
	}
	sourceLine = "";  sourcePos = 0;  curC = nextC = ' '; soureLineNum = 0;
	readNext();  readNext();
    }
	
    public static void finish() {
	if (sourceFile != null) {
	    try {
		sourceFile.close();
	    } catch (IOException e) {}
	}
    }
	
    public static boolean isMoreToRead() {
    	if(sourceLine == null){	
			return false;
		}
		return true;
    }
	
    public static int curLineNum() {
    	
    	return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }

    /*
     * This is not part of the pre-code, this is "human" generated
     */
    public static String readNextLine(){
    	try{
			sourceLine = sourceFile.readLine();
			
			curC = 'm';
    		System.out.print("new line: " + sourceLine +  "\n");
			return sourceLine;
		}catch (IOException e){
			Error.error("could not read line");
			return null;
		}
    }
    
    public static void readNext() {
	curC = nextC;
	if (sourceLine == "" || sourcePos == sourceLine.length()){
		sourceLine = readNextLine();
		sourcePos = 0;
		System.out.print("SoureLine: " + sourceLine);

	}
	
	if (! isMoreToRead()){ 
		System.out.print("mordi!!!");
		return;
	}
	nextC = (char) sourceLine.charAt(sourcePos);
	sourcePos ++;
	System.out.print("current c: " + curC);
	
	
		
    }
}
