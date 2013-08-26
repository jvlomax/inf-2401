package no.uio.ifi.cflat.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;

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
	sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
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
		return (sourceLine != null ? true : false);
	
    }
	
    public static int curLineNum() {
    	
    	return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }
	
    public static void readNext() {
	curC = nextC;
	if (! isMoreToRead()) return;
	try{
		nextC = (char) sourceFile.read();
		
	}catch (IOException e){
		Error.error("Could not read character");
		e.printStackTrace();
	}
		
    }
}
