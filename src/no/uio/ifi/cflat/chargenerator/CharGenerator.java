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

    
    public static String readLine(){
    	try{
			return sourceFile.readLine();
		}catch (IOException e){
			Error.error("could not read line");
			return null;
		}
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
