package no.uio.ifi.cflat.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { 
    addToken, assignToken, 
    commaToken, 
    divideToken, doubleToken,
    elseToken, eofToken, equalToken, 
    forToken, 
    greaterEqualToken, greaterToken, 
    ifToken, intToken, 
    leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, 
    multiplyToken, 
    nameToken, notEqualToken, numberToken, 
    rightBracketToken, rightCurlToken, rightParToken, returnToken, 
    semicolonToken, subtractToken, 
    whileToken;

    public static boolean isFactorOperator(Token t) {
    	System.out.print("isFactorOperator token: " + t);
	return false;
    }

    public static boolean isTermOperator(Token t) {
    	System.out.print("isTermOperator token: " + t);
	return false;
    }

    public static boolean isRelOperator(Token t) {
    	System.out.print("isRelOperator token: " + t);
	return false;
    }

    public static boolean isOperand(Token t) {
    	System.out.print("isOperand token: " + t);
	return false;
    }

    public static boolean isTypeName(Token t) {

    	System.out.print("isTypeName token: " + t);
	   return true;
    }
}
