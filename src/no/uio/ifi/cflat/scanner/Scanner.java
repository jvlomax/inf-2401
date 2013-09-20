package no.uio.ifi.cflat.scanner;

/*
 * module Scanner
 */

import static no.uio.ifi.cflat.scanner.Token.eofToken;

import java.io.IOException;

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
		// -- Must be changed in part 0:
	}

	public static void readNext() {
		curToken = nextToken;
		nextToken = nextNextToken;
		curName = nextName;
		nextName = nextNextName;
		curNum = nextNum;
		nextNum = nextNextNum;
		curLine = nextLine;
		nextLine = nextNextLine;

		nextNextToken = null;
		while (nextNextToken == null) {
			nextNextLine = CharGenerator.curLineNum();
			try {

				if (!CharGenerator.isMoreToRead()) {
					

					nextNextToken = eofToken;
				} else {

					Token token = null;
					if (CharGenerator.curC == '#') {
						// print line to log and skip to next line
						CharGenerator.skipLine();
						CharGenerator.readNext();
					}else if(CharGenerator.curC == '/' && CharGenerator.nextC == '*'){
						CharGenerator.readNext(); CharGenerator.readNext();
						while(!(CharGenerator.curC == '*' && CharGenerator.nextC =='/')){
							CharGenerator.readNext();
						}
						CharGenerator.readNext();
					} else if (isValidNameChar(CharGenerator.curC)) {
						
						
						String word = "" + CharGenerator.curC;
						while (isValidNameChar(CharGenerator.nextC)) {
							CharGenerator.readNext();
							word += CharGenerator.curC;
						}
						
						word.trim();
						switch (word) {

						case "double":
							token = Token.doubleToken;
							break;
						case "int":
							token = Token.intToken;
							break;
						case "if":
							token = Token.ifToken;
							break;
						case "else":
							token = Token.elseToken;
							break;
						case "for":
							token = Token.forToken;
							break;
						case "while":
							token = Token.whileToken;
							break;
						case "return":
							token = Token.returnToken;
							break;
						default:
							if (isStringNumber(word)) {
								token = Token.numberToken;
								nextNextNum = Integer.valueOf(word);
							} else {
								token = Token.nameToken;
								nextNextName = word;
							}
							break;
						}
					} else {
						
						switch (CharGenerator.curC) {
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
						case '<':
							if (CharGenerator.nextC == '=') {
								CharGenerator.readNext();
								token = Token.lessEqualToken;
							} else {
								token = Token.lessToken;
							}
							break;
						case '>':
							token = Token.greaterToken;
						case '=':
							if (CharGenerator.nextC == '=') {
								CharGenerator.readNext();
								token = Token.equalToken;
							} else if (CharGenerator.nextC == '>') {
								token = Token.greaterEqualToken;
							} else {
								token = Token.assignToken;
							}
							break;
						case '\'':
							
							CharGenerator.readNext();
							if (CharGenerator.nextC == '\'') {
								
								token = Token.numberToken;
								nextNextNum = (int) CharGenerator.curC;
								CharGenerator.readNext();
							}
							break;
						case '!':
							if (CharGenerator.nextC == '=') {
								CharGenerator.readNext();
								token = Token.notEqualToken;
							}
							break;
						}
					}
					nextNextToken = token;
					
					CharGenerator.readNext();
				}
			} catch (IOException e) {
				Log.noteError("Error reading the file");
				e.printStackTrace();
			}
		}
		Log.noteToken();
		// Error.error(nextNextLine,
		// "Illegal symbol: '" + CharGenerator.curC + "'!");
	}

	/*
	 * Check to see if a character is a [a-zA-Z] letter
	 * 
	 * @param c char to check
	 * 
	 * @return true if char is an AZ letter
	 */
	private static boolean isLetterAZ(char c) {
		return Character.isLetter(c);

	}

	/*
	 * Check to see if a character is whitespace. This includes tab, carriage
	 * return, line feed, etc.
	 * 
	 * @param c char to check
	 * 
	 * @return true if char is a witespace character
	 */
	private static boolean isWhiteSpace(char c) {
		return (Character.isWhitespace(c));
	}

	/*
	 * Check to see if a character is a valid character to use in a Cb name
	 * token
	 * 
	 * @param c char to check
	 * 
	 * @return true if char is a valid in a Cb name
	 */
	private static boolean isValidNameChar(char c) {
		return (Character.isLetter(c) || Character.isDigit(c) || c == '_' ? true
				: false);
	}

	/*
	 * Check to see if a string only consists of number
	 * 
	 * @param s String to check
	 * 
	 * @return True if the string only consists of numbers
	 */
	private static boolean isStringNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
		check(t);
		readNext();
	}

	public static void skip(Token t1, Token t2) {
		check(t1, t2);
		readNext();
	}
}
