package no.uio.ifi.cflat.syntax;

/*
 * module Syntax
 */

import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.code.Code;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;
import no.uio.ifi.cflat.scanner.Scanner;
import no.uio.ifi.cflat.scanner.Token;
import static no.uio.ifi.cflat.scanner.Token.*;
import no.uio.ifi.cflat.types.*;

/*
 * Creates a syntax tree by parsing; 
 * prints the parse tree (if requested);
 * checks it;
 * generates executable code. 
 */
public class Syntax {
    static DeclList library;
    static Program program;

    public static void init() {
	//TODO:-- Must be changed in part 1:
    Scanner.readNext();
    Scanner.readNext();
    Scanner.readNext();
    }

    public static void finish() {
	//TODO:-- Must be changed in part 1:
    System.out.println("finish");
    }

    public static void checkProgram() {
	program.check(library);
    }

    public static void genCode() {
	program.genCode(null);
    }

    public static void parseProgram() {
	program = Program.parse();
    }

    public static void printProgram() {
    System.out.println("\nPrinting Program");
	program.printTree();
    System.out.println("Done Printing Program");
    }

    static void error(SyntaxUnit use, String message) {
	Error.error(use.lineNum, message);
    }
}


/*
 * Master class for all syntactic units.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class SyntaxUnit {
    int lineNum;

    SyntaxUnit() {
	lineNum = Scanner.curLine;
    }

    /* The virtual methods: */
    abstract void check(DeclList curDecls);
    abstract void genCode(FuncDecl curFunc);
    abstract void printTree();
}


/*
 * A <program>
 */
class Program extends SyntaxUnit {
    DeclList progDecls;
	
    @Override void check(DeclList curDecls) {
	progDecls.check(curDecls);

	if (! Cflat.noLink) {
	    // Check that 'main' has been declared properly:
	    //-- Must be changed in part 2:
	}
    }
		
    @Override void genCode(FuncDecl curFunc) {
	progDecls.genCode(null);
    }

    static Program parse() {
	Log.enterParser("<program>");

	Program p = new Program();
	p.progDecls = GlobalDeclList.parse();
	if (Scanner.curToken != eofToken)
	    Error.expected("A declaration");

	Log.leaveParser("</program>");
	return p;
    }

    @Override void printTree() {
    System.out.println("Program.printTree");
	progDecls.printTree();
    }
}


/*
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

abstract class DeclList extends SyntaxUnit {
    Declaration firstDecl = null;
    Declaration lastDecl = null;
    DeclList outerScope;

    DeclList () {
	//TODO:-- Must be changed in part 1:

    }

    @Override void check(DeclList curDecls) {
	outerScope = curDecls;

	Declaration dx = firstDecl;
	while (dx != null) {
	    dx.check(this);  dx = dx.nextDecl;
	}
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
        System.out.println("Declaration.printTree");
        Declaration curDecl = firstDecl;
        if (curDecl == null) {
            System.out.println("DeclList is empty, return from printTree()");
            return;
        }
        curDecl.printTree();
        while(curDecl.nextDecl != null){
            curDecl = curDecl.nextDecl;
            curDecl.printTree();
        }
    }

    void addDecl(Declaration d) {
	//TODO:-- Must be changed in part 1:
        if(firstDecl == null){
            firstDecl = lastDecl = d;
        }else{
            lastDecl = lastDecl.nextDecl = d;
        }
    }

    int dataSize() {
	int res = 0;
	//-- Must be changed in part 2:
	return res;
    }

    Declaration findDecl(String name, SyntaxUnit usedIn) {
	//-- Must be changed in part 2:
	return null;
    }
}


/*
 * A list of global declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class GlobalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static GlobalDeclList parse() {
	GlobalDeclList gdl = new GlobalDeclList();
    gdl.outerScope = gdl;
	while (Token.isTypeName(Scanner.curToken)) {
	   if (Scanner.nextToken == nameToken) {
	       if (Scanner.nextNextToken == leftParToken) {
	           gdl.addDecl(FuncDecl.parse());
	       } else if (Scanner.nextNextToken == leftBracketToken) {
	           gdl.addDecl(GlobalArrayDecl.parse());
	       } else {
		    //TODO:-- Must be changed in part 1:
                gdl.addDecl(GlobalSimpleVarDecl.parse());
           }
	    } else {
		Error.expected("A declaration");
	    }
	}
	return gdl;
    }
}


/*
 * A list of local declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class LocalDeclList extends DeclList {
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static LocalDeclList parse() {
	//TODO:-- Must be changed in part 1:
    LocalDeclList ldl = new LocalDeclList();
    ldl.outerScope = ldl;
    while (Token.isTypeName(Scanner.curToken)) {
       if (Scanner.nextToken == nameToken) {
           if (Scanner.nextNextToken == leftBracketToken) {
               ldl.addDecl(LocalArrayDecl.parse());
           } else {
            //TODO:-- Must be changed in part 1:
               ldl.addDecl(LocalSimpleVarDecl.parse());
           }
        } else {
        Error.expected("A declaration");
        }
    }
    return ldl;
    
    }
}


/*
 * A list of parameter declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class ParamDeclList extends DeclList {
    ParamDecl firstDecl = null;
    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static ParamDeclList parse() {
	    //TODO:-- Must be changed in part 1:
        ParamDeclList paraDeclList = new ParamDeclList();
        //Log.enterParser("<paramDecl list>");

    
        ParamDecl lastParamDecl = null;
        while (Scanner.curToken != rightParToken) {
            if (paraDeclList.firstDecl == null) {
                lastParamDecl = paraDeclList.firstDecl = ParamDecl.parse();
            }else {
                lastParamDecl = lastParamDecl.nextDecl = ParamDecl.parse();
        	}
            System.out.println(Scanner.curToken);
        	if(Scanner.curToken == commaToken){
                
            	Scanner.skip(commaToken);
        	}
        }
      //Log.leaveParser("</paramDecl list>");
	    return paraDeclList;
    }

        
    

    @Override void printTree() {
    //-- Must be changed in part 1:
        System.out.println("ParamDeclList.printTree");
        ParamDecl curParamDecl = firstDecl;
        if (firstDecl == null) {
            System.out.println("ParamDeclList is Empty, return from printTree");
            return;
        }
        curParamDecl.printTree();
        while(curParamDecl.nextDecl != null){
            curParamDecl = curParamDecl.nextDecl;
            curParamDecl.printTree();
        }
    }
}


/*
 * Any kind of declaration.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class Declaration extends SyntaxUnit {
    String name, assemblerName;
    Type type;
    boolean visible = false;
    Declaration nextDecl = null;

    Declaration(String n) {
	name = n;
    }

    abstract int declSize();

    /**
     * checkWhetherArray: Utility method to check whether this Declaration is
     * really an array. The compiler must check that a name is used properly;
     * for instance, using an array name a in "a()" or in "x=a;" is illegal.
     * This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be an
     *      array (i.e., in a construct like "a["), the parser will first 
     *      search for a's declaration d.
     * <li> The parser will call d.checkWhetherArray(this).
     * <li> Every sub-class of Declaration will implement a checkWhetherArray.
     *      If the declaration is indeed an array, checkWhetherArray will do
     *      nothing, but if it is not, the method will give an error message.
     * </ul>
     * Examples
     * <dl>
     *  <dt>GlobalArrayDecl.checkWhetherArray(...)</dt>
     *  <dd>will do nothing, as everything is all right.</dd>
     *  <dt>FuncDecl.checkWhetherArray(...)</dt>
     *  <dd>will give an error message.</dd>
     * </dl>
     */
    abstract void checkWhetherArray(SyntaxUnit use);

    /**
     * checkWhetherFunction: Utility method to check whether this Declaration
     * is really a function.
     * 
     * @param nParamsUsed Number of parameters used in the actual call.
     *                    (The method will give an error message if the
     *                    function was used with too many or too few parameters.)
     * @param use From where is the check performed?
     * @see   checkWhetherArray
     */
    abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);

    /**
     * checkWhetherSimpleVar: Utility method to check whether this
     * Declaration is really a simple variable.
     *
     * @see   checkWhetherArray
     */
    abstract void checkWhetherSimpleVar(SyntaxUnit use);
}


/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
    VarDecl(String n) {
	super(n);
    }

    @Override int declSize() {
	return type.size();
    }
    	
    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
	Syntax.error(use, name + " is a variable and no function!");
    }
	
    
    @Override void printTree() {
    System.out.println("VarDecl.printTree");
	Log.wTree(type.typeName() + " " + name);
	Log.wTreeLn(";");
    }

    //-- Must be changed in part 1+2:
}


/*
 * A global array declaration
 */
class GlobalArrayDecl extends VarDecl {
    GlobalArrayDecl(String n) {
	super(n);
	assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
    }

    @Override void check(DeclList curDecls) {
	visible = true;
	if (((ArrayType)type).nElems < 0)
	    Syntax.error(this, "Arrays cannot have negative size!");
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	/* OK */
    }

    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	Syntax.error(use, name + " is an array and no simple variable!");
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static GlobalArrayDecl parse() {
	Log.enterParser("<var decl>");

	//TODO:-- Must be changed in part 1:
	return null;
    }

    @Override void printTree() {
    System.out.println("GlobalArrayDecl.printTree");
	//TODO:-- Must be changed in part 1:
    }
}


/*
 * A global simple variable declaration
 */
class GlobalSimpleVarDecl extends VarDecl {
    GlobalSimpleVarDecl(String n) {
	super(n);
	assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	/* OK */
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static GlobalSimpleVarDecl parse() {
	Log.enterParser("<var decl>");

	//TODO:-- Must be changed in part 1:
	return null;
    }
}


/*
 * A local array declaration
 */
class LocalArrayDecl extends VarDecl {
    LocalArrayDecl(String n) {
	super(n); 
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static LocalArrayDecl parse() {
	Log.enterParser("<var decl>");

	//TODO:-- Must be changed in part 1:
	return null;
    }

    @Override void printTree() {
        System.out.println("LocalArrayDecl.printTree");
	//TODO:-- Must be changed in part 1:
    }

}


/*
 * A local simple variable declaration
 */
class LocalSimpleVarDecl extends VarDecl {
    LocalSimpleVarDecl(String n) {
	super(n); 
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static LocalSimpleVarDecl parse() {
	Log.enterParser("<var decl>");

	//TODO:-- Must be changed in part 1:
	return null;
    }
}


/*
 * A <param decl>
 */
class ParamDecl extends VarDecl {
    int paramNum = 0;
    ParamDecl nextDecl;

    ParamDecl(String n) {
	super(n);
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static ParamDecl parse() {
    //-- Must be changed in part 1:
	Log.enterParser("<param decl>");
	ParamDecl paramDecl = new ParamDecl(Scanner.nextName);
    paramDecl.type = Types.getType(Scanner.curToken);
    Scanner.readNext();
    //Skip name token, since it has been added already
    Scanner.readNext();
	Log.leaveParser("</param decl>");
	
	return paramDecl;
    }

    // @Override void printTree(){
    //     System.out.println("lOL");
    // }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    //TODO:-- Must be changed in part 1+2:
	ParamDeclList paraDeclList = null;
    FuncBody funcBody = null;
    FuncDecl(String n) {
	// Used for user functions:

	super(n);
	assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
	//TODO:-- Must be changed in part 1:
    }

    @Override int declSize() {
	return 0;
    }

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherArray(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
	//-- Must be changed in part 2:
    }
	
    @Override void checkWhetherSimpleVar(SyntaxUnit use) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static FuncDecl parse() {
        System.out.println("FuncDecl: Parse");
    	//-- Must be changed in part 1:
    	Log.enterParser("<func decl>");
        FuncDecl funcDecl = new FuncDecl(Scanner.nextName);
        funcDecl.type = Types.getType(Scanner.curToken);
        Scanner.readNext();
        funcDecl.name = Scanner.curName;
        Scanner.readNext();
        Scanner.skip(leftParToken);
        funcDecl.paraDeclList = ParamDeclList.parse();
        Scanner.skip(rightParToken);
        System.out.println("Before FuncBody");
        funcDecl.funcBody = FuncBody.parse();
        System.out.println("After FuncBody");
        Log.leaveParser("</func decl>");
        System.out.println("FuncDecl: DoneParse");
	    return funcDecl;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
        System.out.println("FuncDecl.printTree");
        Log.wTree(type.typeName());
        System.out.println("1");
        Log.wTree(" " + name);
        System.out.println("2");
        Log.wTree(" (");
        System.out.println("before paraDeclList.printTree call");
        paraDeclList.printTree();
        Log.wTreeLn(")");
        funcBody.printTree();

        System.out.println("FuncDecl.printTree : Done");
    }
}

class FuncBody extends SyntaxUnit {
    LocalDeclList localDeclList = null;
    StatmList statmList = null;

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
    }

    static FuncBody parse() {
    //TODO:-- Must be changed in part 1:
    Log.enterParser("<func body>");
    System.out.println("inside FuncBody");
    System.out.println("curToken: " + Scanner.curToken);
    FuncBody funcBody = new FuncBody();
    Scanner.skip(leftCurlToken);
    System.out.println("curToken before localDeclList: " + Scanner.curToken);
    funcBody.localDeclList = LocalDeclList.parse();
    System.out.println("curToken before statmList: " + Scanner.curToken);
    funcBody.statmList = StatmList.parse();
    System.out.println("curToken: " + Scanner.curToken);
    Scanner.skip(rightCurlToken);
    Log.leaveParser("</func body>");
    return funcBody;
    }

    @Override void printTree() {
    //TODO:-- Must be changed in part 1:
    System.out.println("FuncBody.printTree");
    Log.wTreeLn("{");
    Log.indentTree();
    System.out.println("LOL 1 ");
    localDeclList.printTree();
    System.out.println("LOL 2 ");
    statmList.printTree();
    Log.outdentTree();
    Log.wTreeLn("}");
    System.out.println("Done with FuncBody.printTree");
    }
}


/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part 1:
    Statement firstStatm = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static StatmList parse() {
	Log.enterParser("<statm list>");

	StatmList sl = new StatmList();
	Statement lastStatm = null;
	while (Scanner.curToken != rightCurlToken) {
	    //-- Must be changed in part 1:
        if (sl.firstStatm == null) {
            lastStatm = sl.firstStatm = Statement.parse();
        }else {
            lastStatm = lastStatm.nextStatm = Statement.parse();
        }
	}

	Log.leaveParser("</statm list>");
	return sl;
    }

    @Override void printTree() {
    System.out.println("StatmList.printTree");
	//-- Must be changed in part 1:
    Statement curStatm = firstStatm;
    if (curStatm == null) {
        System.out.println("StatmList is empty, return from printTree");
    }
    curStatm.printTree();
    while(curStatm.nextStatm != null){
        curStatm = curStatm.nextStatm;
        curStatm.printTree();
    }
    }
}



/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {
    Statement nextStatm = null;

    static Statement parse() {
	Log.enterParser("<statement>");

	Statement s = null;
	if (Scanner.curToken==nameToken && 
	    Scanner.nextToken==leftParToken) {
	    //TODO:-- Must be changed in part 1:
        System.out.println("Calling CallStatm");
        s = CallStatm.parse();
	} else if (Scanner.curToken == nameToken) {
	    //-- Must be changed in part 1:
		s = AssignStatm.parse();
	} else if (Scanner.curToken == forToken) {
	    //-- Must be changed in part 1:
		s = ForStatm.parse();
	} else if (Scanner.curToken == ifToken) {
	    s = IfStatm.parse();
	} else if (Scanner.curToken == returnToken) {
	    //-- Must be changed in part 1:
		s = ReturnStatm.parse();
	} else if (Scanner.curToken == whileToken) {
	    s = WhileStatm.parse();
	} else if (Scanner.curToken == semicolonToken) {
	    s = EmptyStatm.parse();
	} else {
	    Error.expected("A statement");
	}
	Log.leaveParser("</statement>");
	return s;
    }
}


class AssignStatm extends Statement{
    Assignment a = null;

	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
	static AssignStatm parse(){
        Log.enterParser("<assign-statm>");
        AssignStatm as = new AssignStatm();
        as.a = Assignment.parse();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</assign-statm");
		return as;
	}

	@Override
	void printTree(){
        System.out.println("AssignStatm.printTree");
		// TODO Auto-generated method stub
        a.printTree(); Log.wTreeLn(";");
	}
	
}

class CallStatm extends Statement{
    FunctionCall fc = null;

	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}

	static CallStatm parse(){
		Log.enterParser("<call-statm>");
        CallStatm cs = new CallStatm();
        cs.fc = FunctionCall.parse();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</call-statm>");
        return cs;
	}

	
	@Override
	void printTree(){
        System.out.println("CallStatm.printTree");
		// TODO Auto-generated method stub
        fc.printTree();
        Log.wTreeLn(";");
	}
}

/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
    //TODO:-- Must be changed in part 1+2:
	
    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static EmptyStatm parse() {
	//-- Must be changed in part 1:
    	Log.enterParser("<empty-statm>");
    	Scanner.skip(semicolonToken);
    	Log.leaveParser("</empty-statm>");
    	
	return null;
    }

    @Override void printTree() {
    System.out.println("EmptyStatm.printTree");
	//TODO:-- Must be changed in part 1:
    }
}
	

/*
 * A <for-statm>.
 */
//TODO:-- Must be changed in part 1+2:

class ForStatm extends Statement {
	ForControl fc;
	StatmList sl;
	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
	
	static ForStatm parse(){
		Log.enterParser("<for-statm>");
		ForStatm f = new ForStatm();
		Scanner.skip(leftParToken);
		f.fc = ForControl.parse();
		Scanner.skip(rightParToken);
		Scanner.skip(leftBracketToken);
		f.sl = StatmList.parse();
		Scanner.skip(rightBracketToken);
		Log.leaveParser("</for-statm>");
		return null;
	}

	@Override
	void printTree(){
        System.out.println("ForStatm.printTree");
		// TODO Auto-generated method stub
		Log.wTree("for ("); fc.printTree(); Log.wTreeLn(") {");
		Log.indentTree();
		sl.printTree();
		Log.outdentTree();
		Log.wTreeLn("}");
	}
	
	
}


class ForControl extends Statement{
	Assignment as;
	Expression e;
	Assignment as2;
	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}

	static ForControl parse(){
		Log.enterParser("<for-control>");
		ForControl fc = new ForControl();
		fc.as = Assignment.parse();
		Scanner.skip(semicolonToken);
		fc.e = Expression.parse();
		Scanner.skip(semicolonToken);
		fc.as2 = Assignment.parse();
		
		
		Log.leaveParser("</for-control>");
		return fc;
	}
	
	@Override
	void printTree(){
	   System.out.println("ForControl.printTree");
		as.printTree(); Log.wTree(";"); e.printTree(); Log.wTree(";"); as2.printTree();
		
	}
	
	
	
}

/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    Expression e;
    StatmList sl;
    ElsePart ep = null;
	//:-- Must be changed in part 1+2:
	
    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static IfStatm parse() {
	//:-- Must be changed in part 1:
    	Log.enterParser("<if-statm>");
    	IfStatm is = new IfStatm(); 
    	Scanner.skip(leftParToken);
    	is.e = Expression.parse();
    	Scanner.skip(rightParToken);
    	Scanner.skip(leftBracketToken);
    	is.sl = StatmList.parse();
    	Scanner.skip(rightBracketToken);
    	// might be next token, but should be cur token
    	if (Scanner.curToken == elseToken){
    		is.ep = ElsePart.parse();
    		
    	}
    	
    	Log.leaveParser("</if-statm>");
    	
    	return is;
    }

    @Override void printTree() {
        System.out.println("IfStatm.printTree");
	//TODO:-- Must be changed in part 1:
    	Log.wTree("if (");  e.printTree();  Log.wTreeLn(") {");
    	Log.indentTree();
    	sl.printTree();
    	Log.outdentTree();
    	Log.wTreeLn("}");
    	if (ep != null){
    		ep.printTree();
    	}
    	
    }
}

class ElsePart extends Statement{
	StatmList sl;


	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}

	static ElsePart parse(){
		Log.enterParser("<else-part>");
		ElsePart ep = new ElsePart();
		Scanner.skip(leftBracketToken);
		ep.sl = StatmList.parse();
		Scanner.skip(rightBracketToken);
		Log.leaveParser("</else-part>");
		return ep;
		
	}
	
	@Override
	void printTree(){
        System.out.println("ElsePart.printTree");
		// TODO Auto-generated method stub
		Log.wTree("else { ");
		Log.indentTree();
		sl.printTree();
		Log.outdentTree();
		Log.wTreeLn("}");
	}
	
}

/*
 * A <return-statm>.
 */
//-- Must be changed in part 1+2:
class ReturnStatm extends Statement {
	Expression retVal;
	
	@Override
	void check(DeclList curDecls){
		retVal.check(curDecls);
	}
	
	@Override 
	void genCode(FuncDecl curFunc){
		//TODO: fill in
	}
	
	
	static ReturnStatm parse() {
		Log.enterParser("<return-statm>");
		ReturnStatm rs = new ReturnStatm();
		Scanner.readNext();
		rs.retVal = Expression.parse();
		System.out.println(Scanner.curName);
		Scanner.skip(semicolonToken);
		Log.leaveParser("</return-statm>");
		return rs;
	}
	
	@Override
	void printTree() {
		System.out.println("ReturnStatm.printTree");
		Log.wTree("return ");
		retVal.printTree();
		Log.wTree(";");
	}
}

/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {
    Expression test;
    StatmList body;

    @Override void check(DeclList curDecls) {
	test.check(curDecls);
	body.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
	String testLabel = Code.getLocalLabel(), 
	       endLabel  = Code.getLocalLabel();

	Code.genInstr(testLabel, "", "", "Start while-statement");
	test.genCode(curFunc);
	test.valType.genJumpIfZero(endLabel);
	body.genCode(curFunc);
	Code.genInstr("", "jmp", testLabel, "");
	Code.genInstr(endLabel, "", "", "End while-statement");
    }

    static WhileStatm parse() {
	Log.enterParser("<while-statm>");

	WhileStatm ws = new WhileStatm();
	Scanner.readNext();
	Scanner.skip(leftParToken);
	ws.test = Expression.parse();
	Scanner.skip(rightParToken);
	Scanner.skip(leftCurlToken);
	ws.body = StatmList.parse();
	Scanner.skip(rightCurlToken);

	Log.leaveParser("</while-statm>");
	return ws;
    }

    @Override void printTree() {
    System.out.println("WhileStatm.printTree");
	Log.wTree("while (");  test.printTree();  Log.wTreeLn(") {");
	Log.indentTree();
	body.printTree();
	Log.outdentTree();
	Log.wTreeLn("}");
    }
}


//TODO:-- Must be changed in part 1+2:


/*
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
    Expression firstExpr = null;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static ExprList parse() {
    //TODO:-- Must be changed in part 1:

	Log.enterParser("<expr list>");
    System.out.println("curToken inside ExprList: " + Scanner.curToken);

	
    ExprList exprList = new ExprList();
    Expression lastExpr = null;
    while (Scanner.curToken != rightParToken) {
        //-- Must be changed in part 1:
        System.out.println("while cur token: " + Scanner.curToken);
        if (exprList.firstExpr == null) {
            lastExpr = exprList.firstExpr = Expression.parse();
        }else {
            lastExpr = lastExpr.nextExpr = Expression.parse();
        }
        if (Scanner.curToken == commaToken) {
            Scanner.readNext();
            
        }
    }


    System.out.println("After exprList: " + Scanner.curToken);

    Log.leaveParser("</expr list>");
	return exprList;
    }

    @Override void printTree() {
        System.out.println("ExprList.printTree");
	//TODO:-- Must be changed in part 1:
        Expression curExpre = firstExpr;
        if (curExpre == null) {
            System.out.println("ExprList is empty, returning from printTree");
            return;
        }
        curExpre.printTree();
        while(curExpre.nextExpr != null){
            curExpre = curExpre.nextExpr;
            curExpre.printTree();
        }
    }
    //TODO:-- Must be changed in part 1:
}




/*
 * An <Assignment> Added in part 1.
 */

class Assignment extends SyntaxUnit {
    Variable variable = null;
    Expression exp = null;

    @Override void check(DeclList curDecls) {
    //-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
    //-- Must be changed in part 2:
    }


    static Assignment parse() {
    Log.enterParser("<assignment>");

    Assignment a = new Assignment();
    Scanner.readNext();
    a.variable = Variable.parse();
    Scanner.skip(assignToken);
    a.exp = Expression.parse();
    Scanner.readNext();

    Log.leaveParser("</assignment>");

    return a;
    }

    @Override void printTree() {
        System.out.println("Assignment.printTree");
        variable.printTree();  Log.wTree("=");  exp.printTree();
    }
}

/*
 * An <expression>
 */
class Expression extends Operand {
    Expression nextExpr = null;
    Term firstTerm, secondTerm = null;
    Operator relOp = null;
    boolean innerExpr = false;

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static Expression parse() {
	Log.enterParser("<expression>");
    System.out.println("Expression curToken: " + Scanner.curToken);
	Expression e = new Expression();
	e.firstTerm = Term.parse();
    
	if (Token.isRelOperator(Scanner.curToken)) {
	    e.relOp = RelOperator.parse();
	    e.secondTerm = Term.parse();
	}
    System.out.println("Done with expression");
	Log.leaveParser("</expression>");
	return e;
    }

    @Override void printTree() {
        System.out.println("Expression.printTree");
        
        firstTerm.printTree();
        System.out.println("omg");
        if (relOp != null) {
            relOp.printTree();
        }
        if (secondTerm != null) {
            secondTerm.printTree();
        }
	//TODO:-- Must be changed in part 1:
    }
}

class FactorList extends SyntaxUnit {
	Factor firstFactor;
	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
	
	static FactorList parse(){
		System.out.println("start of factor list parse");
		FactorList fl = new FactorList();
		Factor lastFactor = null;
	
		if(Scanner.curToken == leftParToken){
			while(Scanner.curToken != rightParToken){
				if(fl.firstFactor == null){		
					System.out.println(Scanner.nextToken);
					lastFactor = fl.firstFactor = Factor.parse();				
				}else{			
					lastFactor = lastFactor.nextFactor = Factor.parse();			
				}
			
			}
		}
		fl.firstFactor = Factor.parse();
		
		System.out.println(Scanner.curToken);
		System.out.println(Scanner.nextToken);
		Scanner.readNext();
		while(Token.isOperand(Scanner.nextToken)){
			
			if(fl.firstFactor == null){		
				System.out.println(Scanner.nextToken);
				lastFactor = fl.firstFactor = Factor.parse();				
			}else{			
				lastFactor = lastFactor.nextFactor = Factor.parse();			
			}
			Scanner.readNext();
		
			
			
			if(Scanner.curToken != rightParToken)
				Scanner.readNext();
		}
		
		
		return fl;
	}

	@Override
	void printTree(){
		System.out.println("factory list print teree");
		if(firstFactor == null){
			System.out.print("factor list is empty");
			return;
		}
		
		Factor curFactor = firstFactor;
		curFactor.printTree();
        while(curFactor.nextFactor != null){
            curFactor = curFactor.nextFactor;
            curFactor.printTree();
        }
	}
}
class Factor extends SyntaxUnit {
	
	Factor nextFactor = null;
	OperandList op;
	TermOperator term = null;
	
	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
	static Factor parse(){
		Log.enterParser("<factor>");
		Factor f = new Factor();
		f.op = OperandList.parse();	

		Log.leaveParser("</factor>");
		if(Token.isTermOperator(Scanner.nextToken)){
			System.out.println("TERM OPERATOR");
			f.term = TermOperator.parse();
		}
		return f;
			
	}

	@Override
	void printTree(){
        System.out.println("Factor.printTree");
		// TODO Auto-generated method stub
		op.printTree();
		
	}
	
	
}
/*
 * A <term>
 */
class Term extends SyntaxUnit {
    //TODO:-- Must be changed in part 1+2:
    FactorList fl;
	
	
    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static Term parse() {
	//TODO:-- Must be changed in part 1:̈́
    	Log.enterParser("<term>");
    	
    	Term t = new Term();
    	t.fl = FactorList.parse();
    
    	
    	Log.leaveParser("</term>");
    	
    	
    	return t;
    }

    @Override void printTree() {
        
    	System.out.println("Term.printTree");
    	fl.printTree();
    	//-- Must be changed in part 1+2:
    }
}

//TODO:-- Must be changed in part 1+2:

/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {
    Operator nextOp = null;
    Type opType;
    Token opToken;

    @Override void check(DeclList curDecls) {}
}


//TODO:-- Must be changed in part 1+2:


/*
 * A relational operator (==, !=, <, <=, > or >=).
 */

class RelOperator extends Operator {
    @Override void genCode(FuncDecl curFunc) {
	if (opType == Types.doubleType) {
	    Code.genInstr("", "fldl", "(%esp)", "");
	    Code.genInstr("", "addl", "$8,%esp", "");
	    Code.genInstr("", "fsubp", "", "");
	    Code.genInstr("", "fstps", Code.tmpLabel, "");
	    Code.genInstr("", "cmpl", "$0,"+Code.tmpLabel, "");
	} else {
	    Code.genInstr("", "popl", "%ecx", "");
	    Code.genInstr("", "cmpl", "%eax,%ecx", "");
	}
	Code.genInstr("", "movl", "$0,%eax", "");
	switch (opToken) {
	case equalToken:        
	    Code.genInstr("", "sete", "%al", "Test ==");  break;
	case notEqualToken:
	    Code.genInstr("", "setne", "%al", "Test !=");  break;
	case lessToken:
	    Code.genInstr("", "setl", "%al", "Test <");  break;
	case lessEqualToken:
	    Code.genInstr("", "setle", "%al", "Test <=");  break;
	case greaterToken:
	    Code.genInstr("", "setg", "%al", "Test >");  break;
	case greaterEqualToken:
	    Code.genInstr("", "setge", "%al", "Test >=");  break;
	}
    }

    static RelOperator parse() {
	Log.enterParser("<rel operator>");

	RelOperator ro = new RelOperator();
	ro.opToken = Scanner.curToken;
	Scanner.readNext();

	Log.leaveParser("</rel operator>");
	return ro;
    }

    @Override void printTree() {
    System.out.println("RelOperator.printTree");
	String op = "?";
	switch (opToken) {
		case equalToken:        op = "==";  break;
		case notEqualToken:     op = "!=";  break;
		case lessToken:         op = "<";   break;
		case lessEqualToken:    op = "<=";  break;
		case greaterToken:      op = ">";   break;
		case greaterEqualToken: op = ">=";  break;
	}
	Log.wTree(" " + op + " ");
    }
}

class TermOperator extends Operator {
	static TermOperator parse() {
	Log.enterParser("<term operator>");

	TermOperator to = new TermOperator();
	to.opToken = Scanner.curToken;
	Scanner.readNext();

	Log.leaveParser("</term operator>");
	return to;
    }

    @Override void printTree() {
    System.out.println("RelOperator.printTree");
	String op = "?";
	switch (opToken) {
	case addToken:        	op = "+";  break;
	case subtractToken:     op = "-";  break;
		
	}
	Log.wTree(" " + op + " ");
    }

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
}

class FactorOperator extends Operator {
	static FactorOperator parse() {
	Log.enterParser("<factor operator>");

	FactorOperator fo = new FactorOperator();
	fo.opToken = Scanner.curToken;
	Scanner.readNext();

	Log.leaveParser("</factor operator>");
	return fo;
    }

    @Override void printTree() {
    System.out.println("RelOperator.printTree");
	String op = "?";
	switch (opToken) {
	case multiplyToken:   op = "*";  break;
	case divideToken:     op = "/";  break;
		
	}
	Log.wTree(" " + op + " ");
    }

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}
}

class OperandList extends SyntaxUnit{
	Operand firstOperand;
	@Override
	void check(DeclList curDecls){
		// TODO Auto-generated method stub
		
	}

	@Override
	void genCode(FuncDecl curFunc){
		// TODO Auto-generated method stub
		
	}

	static OperandList parse(){
		OperandList opl = new OperandList();
		Operand lastOperand = null;
		System.out.println("Time to go home");
		opl.firstOperand = Operand.parse();
		System.out.println("really HERE!");
		System.out.println(Scanner.curToken);
		System.out.println(Scanner.nextToken);
		//Scanner.readNext();
		while(Token.isOperand(Scanner.curToken)){
			System.out.print("WHILWHIWLHILW");
			if(opl.firstOperand == null){
				lastOperand = opl.firstOperand = Operand.parse();
			}else{
				lastOperand = lastOperand.nextOperand = Operand.parse();
			}
			Scanner.readNext();
			if(Scanner.curToken != rightParToken)
				Scanner.readNext();
			
		}
		return opl;
	}
	@Override
	void printTree(){
		System.out.println("Opernald list preint");
		if(firstOperand == null){
			System.out.print("opernad list is empty");
			return;
		}
		
		Operand curOperand = firstOperand;
		curOperand.printTree();
        while(curOperand.nextOperand != null){
            curOperand = curOperand.nextOperand;
            curOperand.printTree();
		
        }
		
	}
	
	
	
}

/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operand nextOperand = null;
    Type valType;
    FactorOperator fo = null;
    static Operand parse() {
	Log.enterParser("<operand>");

	Operand o = null;
	if (Scanner.curToken == numberToken) {
	    o = Number.parse();
	} else if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
	    o = FunctionCall.parse();
	} else if (Scanner.curToken == nameToken) {
	    o = Variable.parse();
	} else if (Scanner.curToken == leftParToken) {
	    Scanner.readNext();
	    o = Expression.parse();  
	    ((Expression)o).innerExpr = true;
	    Scanner.skip(rightParToken);
	} else {
	    Error.expected("An operand");
	}
	

	Log.leaveParser("</operand>");
	if(Token.isFactorOperator(Scanner.nextToken)){
		System.out.println("Factor OPERATOR");
		if(o == null){
			System.out.println("is null");
		}
		o.fo = FactorOperator.parse();
		System.out.print("After factor operator");
		
	}
	Scanner.readNext();
	return o;
    }
}


/*
 * A <function call>.
 */
class FunctionCall extends Operand {
    String name;
    ExprList expList;
    //TODO:-- Must be changed in part 1+2:

    @Override void check(DeclList curDecls) {
	//-- Must be changed in part 2:
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static FunctionCall parse() {    	
	//TODO:-- Must be changed in part 1:
    	Log.enterParser("<function call>");
        FunctionCall fc = new FunctionCall();
        System.out.println("CurToken Inside FunctionCall: " + Scanner.curToken + "name: " + Scanner.curName);
        fc.name = Scanner.curName;
        Scanner.readNext();
        Scanner.skip(leftParToken);
        fc.expList = ExprList.parse();
        Scanner.skip(rightParToken);    	
    	Log.leaveParser("</function call>");
	   return fc;
    }

    @Override void printTree() {
        System.out.println("FunctionCall.printTree");
	//TODO:-- Must be changed in part 1:
        Log.wTree(name); 
        Log.wTree("("); 
        expList.printTree();
        Log.wTree(")");
    }
    //TODO:-- Must be changed in part 1+2:
}


/*
 * A <number>.
 */
class Number extends Operand {
    int numVal;

   @Override void check(DeclList curDecls) {
       //-- Must be changed in part 2:
    }
	
    @Override void genCode(FuncDecl curFunc) {
	Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal); 
    }

    static Number parse() {
	//TODO:-- Must be changed in part 1:
        Number number = new Number();
    	Log.enterParser("<number>");
        number.numVal = Scanner.curNum;
        Scanner.readNext();
    	Log.leaveParser("</number>");
	return number;
    }

     @Override void printTree() {
    System.out.println("Number.printTree");
	Log.wTree("" + numVal);
    }
}

// /*
//  * A <name>.
//  */
// class Name extends Operand {
//     String nameVal;

//    @Override void check(DeclList curDecls) {
//        //-- Must be changed in part 2:
//     }
    
//     @Override void genCode(FuncDecl curFunc) {
     
//     }

//     static Name parse() {
//     //TODO:-- Must be changed in part 1:
//         Name name = new Name();
//         //Log.enterParser("<name>");
//         name.nameVal = Scanner.curName;
//         Scanner.readNext();
//         //Log.leaveParser("</name>");
//     return name;
//     }

//      @Override void printTree() {
//     Log.wTree("" + nameVal);
//     }
// }


/*
 * A <variable>.
 */

class Variable extends Operand {
    String varName;
    VarDecl declRef = null;
    Expression index = null;

    @Override void check(DeclList curDecls) {
	Declaration d = curDecls.findDecl(varName,this);
	if (index == null) {
	    d.checkWhetherSimpleVar(this);
	    valType = d.type;
	} else {
	    d.checkWhetherArray(this);
	    index.check(curDecls);
	    index.valType.checkType(lineNum, Types.intType, "Array index");
	    valType = ((ArrayType)d.type).elemType;
	}
	declRef = (VarDecl)d;
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- Must be changed in part 2:
    }

    static Variable parse() {
	Log.enterParser("<variable>");
	//TODO:-- Must be changed in part 1:
	
	
	
	
	Variable v = new Variable();
	
	v.varName = Scanner.curToken.name();
	if(Scanner.nextToken == leftBracketToken){
		Scanner.skip(leftBracketToken);
		v.index = Expression.parse();
		Scanner.skip(rightBracketToken);
	}
	
	
	
	
	
	
	Log.leaveParser("</variable>");
	return v;
    }

    @Override void printTree() {
        System.out.println("Variable.printTree");
	//TODO:-- Must be changed in part 1:
    }
}
