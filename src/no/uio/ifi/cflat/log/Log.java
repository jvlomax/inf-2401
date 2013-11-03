package no.uio.ifi.cflat.log;

/*
 * module Log
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.scanner.Scanner;
import no.uio.ifi.cflat.scanner.Token;

/*
 * Produce logging information.
 */
public class Log {
    public static boolean doLogBinding = false, doLogParser = false, 
	doLogScanner = false, doLogTree = false;
	
    private static String logName, curTreeLine = "";
    private static int nLogLines = 0, parseLevel = 0, treeLevel = 0;
	
    public static void init() {
	logName = Cflat.sourceBaseName + ".log";
    }
	
    public static void finish() {
	//-- Must be changed in part 0:
    }

    private static void writeLogLine(String data) {
	try {
	    PrintWriter log = (nLogLines==0 ? new PrintWriter(logName) :
		new PrintWriter(new FileOutputStream(logName,true)));
	    log.println(data);  ++nLogLines;
	    log.close();
	} catch (FileNotFoundException e) {
	    Error.error("Cannot open log file " + logName + "!");
	}
    }

    /*
     * Make a note in the log file that an error has occured.
     *
     * @param message  The error message
     */
    public static void noteError(String message) {
	if (nLogLines > 0) 
	    writeLogLine(message);
    }


    public static void enterParser(String symbol) {
	if (! doLogParser) return;
	Log.parseLevel ++;
	char[] tabs = new char[Log.parseLevel * 2];
	Arrays.fill(tabs, ' ');
	String tabsString = new String(tabs);
	Log.writeLogLine("Parser: " + tabsString +  symbol);

	
	
    }

    public static void leaveParser(String symbol) {
	if (! doLogParser) return;
		Log.parseLevel --;
		char[] tabs = new char[Log.parseLevel * 2];
		Arrays.fill(tabs, ' ');
		String tabsString = new String(tabs);
		Log.writeLogLine("Parser: " + tabsString +  symbol);
	
    }

    /**
     * Make a note in the log file that another source line has been read.
     * This note is only made if the user has requested it.
     *
     * @param lineNum  The line number
     * @param line     The actual line
     */
    public static void noteSourceLine(int lineNum, String line) {
	if (! doLogParser && ! doLogScanner || line == null) return;
		Log.writeLogLine("   " + String.valueOf(lineNum) + ": " + line.trim());
    }
	
    /**
     * Make a note in the log file that another token has been read 
     * by the Scanner module into Scanner.nextNextToken.
     * This note will only be made if the user has requested it.
     */
    public static void noteToken() {
    	if (! doLogScanner) return;
	
		// WARNING: Android convention below
		Token token = Scanner.nextNextToken;
	
		if (token == Token.leftParToken){
			Log.writeLogLine("Scanner: leftParToken");
		}else if(token == Token.rightParToken){
			Log.writeLogLine("Scanner: righParToken");
		}else if(token == Token.leftBracketToken){
			Log.writeLogLine("Scanner: leftBracketToken");
		}else if(token == Token.rightBracketToken){
			Log.writeLogLine("Scanner: rightBracketToken");
		}else if(token == Token.rightCurlToken){
			Log.writeLogLine("Scanner: rightCurlToken");
		}else if(token == Token.leftCurlToken){
			Log.writeLogLine("Scanner: leftCurlToken");
		}else if(token == Token.addToken){
			Log.writeLogLine("Scanner: addToken");
		}else if(token == Token.divideToken){
			Log.writeLogLine("Scanner: divideToken");	
		}else if(token == Token.subtractToken){
			Log.writeLogLine("Scanner: subtractToken");
		}else if(token == Token.multiplyToken){
			Log.writeLogLine("Scanner: multiplyToken");
		}else if(token == Token.nameToken){
			Log.writeLogLine("Scanner: nameToken " + Scanner.nextNextName);
		}else if(token == Token.numberToken){
			Log.writeLogLine("Scanner: numberToken " + Scanner.nextNextNum);
		}else if(token == Token.doubleToken){
			Log.writeLogLine("Scanner: doubleToken");
		}else if(token == Token.intToken){
			Log.writeLogLine("Scanner: intToken");
		}else if(token == Token.commaToken){
			Log.writeLogLine("Scanner: commaToken");
		}else if(token == Token.semicolonToken){
			Log.writeLogLine("Scanner: semicolonToken");
		}else if(token == Token.returnToken){
			Log.writeLogLine("Scanner: returnToken");
		}else if(token == Token.eofToken){
			Log.writeLogLine("Scanner: eofToken");
		}else if(token == Token.assignToken){
			Log.writeLogLine("Scanner: assignToken");
		}else if(token == Token.elseToken){
			Log.writeLogLine("Scanner: elseToken");
		}else if(token == Token.equalToken){
			Log.writeLogLine("Scanner: equalsToken");
		}else if(token == Token.forToken){
			Log.writeLogLine("Scanner: forToken");
		}else if(token == Token.greaterEqualToken){
			Log.writeLogLine("Scanner: greaterEqualToken");
		}else if(token == Token.greaterToken){
			Log.writeLogLine("Scanner: greaterToken");
		}else if(token == Token.ifToken){
			Log.writeLogLine("Scanner: ifToken");
		}else if(token == Token.lessEqualToken){
			Log.writeLogLine("Scanner: lessEqualToken");
		}else if(token == Token.lessToken){
			Log.writeLogLine("Scanner: lessToken");
		}else if(token == Token.notEqualToken){
			Log.writeLogLine("Scanner: notEqualToken");
		}else if(token == Token.whileToken){
			Log.writeLogLine("Scanner: whileToken");
		}
    }

    public static void noteBinding(String name, int lineNum, int useLineNum) {
	if (! doLogBinding) return;
	//-- Must be changed in part 2:
    }


    public static void wTree(String s) {
    System.out.println("wTree");
	if (curTreeLine.length() == 0) {
	    for (int i = 1;  i <= treeLevel;  ++i) curTreeLine += "  ";
	}
	curTreeLine += s;
    }

    public static void wTreeLn() {
   	System.out.println("wTreeLn");
	writeLogLine("Tree:     " + curTreeLine);
	curTreeLine = "";
    }

    public static void wTreeLn(String s) {
    System.out.println("wTreeLn(s)");
	wTree(s);  wTreeLn();
    }

    public static void indentTree() {
	//TODO:-- Must be changed in part 1:
    }

    public static void outdentTree() {
	//TODO:-- Must be changed in part 1:
    }
}
