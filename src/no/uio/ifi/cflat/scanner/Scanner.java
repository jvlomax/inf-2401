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
	    	Token token;
	    	if(isLetterAZ(CharGenerator.curC)){
	    	
	    	
	    	}else{
		    	switch(CharGenerator.curC){
		    		case '(':
		    			token = Token.leftParToken;
		    			break;
		    		case ')':
		    			token = Token.rightParToken;
		    			break;
		    		case '+':
		    			token = Token.addToken;
		    			break;
		    		case '-':
		    			token = Token.subtractToken;
		    			break;
		    		case '*':
		    			token = Token.multiplyToken;
		    			break;
		    		case '/':
		    			token = Token.divideToken;
		    			break;
		    		case ']':
		    			token = Token.rightBracketToken;
		    			break;
		    		case '[':
		    			token = Token.leftBracketToken;
		    			break;
		    		case '{':
		    			token = Token.leftCurlToken;
		    			break;
		    		case '}':
		    			token = Token.rightCurlToken;
		    			break;
		    		case ',':
		    			token = Token.commaToken;
		    			break;
		    		case ';':
		    			token = Token.semicolonToken;
		    			break;
		    	}
	    		
	    		
	    		/*
	    		 * needs additional checking
	    		 */
//	    		case '<':
//	    			token = Token.lessToken;
//	    			break;
//	    		case '>':
//	    			token = Token.greaterToken;
//		    	case '=':
//		    		token = Token
	    	}	
	    		System.out.print("current C: " + CharGenerator.curC);
	    		
	    		readNext();
	    		
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
