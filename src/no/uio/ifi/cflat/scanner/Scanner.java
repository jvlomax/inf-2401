package no.uio.ifi.cflat.scanner;

/*
 * module Scanner
 */

import static no.uio.ifi.cflat.scanner.Token.eofToken;
import no.uio.ifi.cflat.chargenerator.CharGenerator;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
    public static Token curToken, nextToken, nextNextToken;
    public static String curName, nextName, nextNextName;
    public static int curNum, nextNum, nextNextNum;
    public static int curLine, nextLine, nextNextLine;
	
    public static void init() {
	System.out.print("Scanner init\n");
    }
	
    public static void finish() {
	//-- Must be changed in part 0:
    }
	
    public static void readNext() {
	curToken = nextToken;  nextToken = nextNextToken;
	curName = nextName;  nextName = nextNextName;
	curNum = nextNum;  nextNum = nextNextNum;
	curLine = nextLine;  nextLine = nextNextLine;

	nextNextToken = null;
	while (nextNextToken == null) {
	    nextNextLine = CharGenerator.curLineNum();
	    
	    if (! CharGenerator.isMoreToRead()) {
	    	nextNextToken = eofToken;
		
	    } else 
	    //-- Must be changed in part 0:
	    {
//		Error.error(nextNextLine,
//			    "Illegal symbol: '" + CharGenerator.curC + "'!");
	    	if(CharGenerator.curC == '#'){
	    		String line = CharGenerator.readNextLine();
	    		
	    		Log.noteSourceLine(curLine, line);
	    		nextLine ++;
	    		
	    	}else{
	    		String word = "";
	    		while(CharGenerator.curC != ' '){
	    			CharGenerator.readNext();
	    			word = word + CharGenerator.curC;
	    		}
	    		//CharGenerator.readNextLine();
	    		System.out.print("word: " + word);
	    	}
	    }
	}
	Log.noteToken();
    }
	
    private static boolean isLetterAZ(char c) {
		return Character.isLetter(c);
			
    }
	
    public static void check(Token t) {
	if (curToken != t)
	    Error.expected("A " + t);
    }
	
    public static void check(Token t1, Token t2) {
	if (curToken != t1 && curToken != t2)
	    Error.expected("A " + t1 + " or a " + t2);
    }
	
    public static void skip(Token t) {
    	check(t);  readNext();
    }
	
    public static void skip(Token t1, Token t2) {
    	check(t1,t2);  readNext();
    }
}
