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
    program.printTree();
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
	program.printTree();
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
        Declaration curDecl = firstDecl;
        while(curDecl.nextDecl != null){
            curDecl.printTree();
            curDecl = curDecl.nextDecl;
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

	return null;
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
        Log.enterParser("<paramDecl list>");

    
        ParamDecl lastParamDecl = null;
        while (Scanner.curToken != rightParToken) {
            if (paraDeclList.firstDecl == null) {
                lastParamDecl = paraDeclList.firstDecl = ParamDecl.parse();
            }else {
                lastParamDecl = lastParamDecl.next = ParamDecl.parse();
            if(Scanner.curToken == commaToken){
                Scanner.skip(commaToken);
            }
        }
    }

        Log.leaveParser("</paramDecl list>");
	    return paraDeclList;
    }

    @Override void printTree() {
    //-- Must be changed in part 1:
        ParamDecl curParamDecl = firstDecl;
        while(curParamDecl.next != null){
            curParamDecl.printTree();
            curParamDecl = curParamDecl.next;
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
    Type type = null;
    Name name = null;
    ParamDecl next = null;

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
    paramDecl.name = Name.parse();
	Log.leaveParser("</param decl>");
	
	return paramDecl;
    }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    //TODO:-- Must be changed in part 1+2:
	Type type = null;
	Name name = null;
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
        funcDecl.name = Name.parse();
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
        Log.wTree(type.typeName()); 
        name.printTree();
        Log.wTree("(");
        paraDeclList.printTree();
        Log.wTree(")");
        funcBody.printTree();
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
    //funcBody.localDeclList = LocalDeclList.parse();
    System.out.println("curToken before statmList: " + Scanner.curToken);
    funcBody.statmList = StatmList.parse();
    System.out.println("curToken: " + Scanner.curToken);
    Scanner.skip(rightCurlToken);
    Log.leaveParser("</func body>");
    return funcBody;
    }

    @Override void printTree() {
    //TODO:-- Must be changed in part 1:
    Log.wTreeLn("{");
    Log.indentTree();
    localDeclList.printTree();
    statmList.printTree();
    Log.outdentTree();
    Log.wTreeLn("}");
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
	//-- Must be changed in part 1:
    Statement curStatm = firstStatm;
    while(curStatm.nextStatm != null){
        curStatm.printTree();
        curStatm = curStatm.nextStatm;
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
		Scanner.skip(semicolonToken);
		Log.leaveParser("</return-statm>");
		return rs;
	}
	
	@Override
	void printTree() {
		
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
        if (exprList.firstExpr == null) {
            lastExpr = exprList.firstExpr = Expression.parse();
        }else {
            lastExpr = lastExpr.nextExpr = Expression.parse();
        }
        Scanner.readNext();
    }


    Log.leaveParser("</expr list>");
	return exprList;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
        Expression curExpre = firstExpr;
        while(curExpre.nextExpr != null){
            curExpre.printTree();
            curExpre = curExpre.nextExpr;
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

	Expression e = new Expression();
	e.firstTerm = Term.parse();
	if (Token.isRelOperator(Scanner.curToken)) {
	    e.relOp = RelOperator.parse();
	    e.secondTerm = Term.parse();
	}

	Log.leaveParser("</expression>");
	return e;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
    }
}


/*
 * A <term>
 */
class Term extends SyntaxUnit {
    //TODO:-- Must be changed in part 1+2:

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
    	
    	Log.leaveParser("</term>");
    	
    	
	return null;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1+2:
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


/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operand nextOperand = null;
    Type valType;

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
	return o;
    }
}


/*
 * A <function call>.
 */
class FunctionCall extends Operand {
    Name name;
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
    	Log.enterParser("<Function call>");
        FunctionCall fc = new FunctionCall();
        System.out.println("CurToken Inside FunctionCall: " + Scanner.curToken + "name: " + Scanner.curName);
        fc.name = Name.parse();
        Scanner.skip(leftParToken);
        fc.expList = ExprList.parse();
        Scanner.skip(rightParToken);    	
    	Log.leaveParser("</function call>");
	   return fc;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
        name.printTree(); 
        Log.wTree("("); 
        expList.printTree();
        Log.wTreeLn(")");
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
	Log.wTree("" + numVal);
    }
}

/*
 * A <name>.
 */
class Name extends Operand {
    String nameVal;

   @Override void check(DeclList curDecls) {
       //-- Must be changed in part 2:
    }
    
    @Override void genCode(FuncDecl curFunc) {
     
    }

    static Name parse() {
    //TODO:-- Must be changed in part 1:
        Name name = new Name();
        Log.enterParser("<name>");
        name.nameVal = Scanner.curName;
        Scanner.readNext();
        Log.leaveParser("</name>");
    return name;
    }

     @Override void printTree() {
    Log.wTree("" + nameVal);
    }
}


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
	Log.enterParser("<variable>");
	
	
	
	Variable v = new Variable();
	Scanner.readNext();
	v.varName = Scanner.curToken.toString();
	Scanner.readNext();
	
	
	
	WhileStatm ws = new WhileStatm();
	Scanner.readNext();
	Scanner.skip(leftParToken);
	ws.test = Expression.parse();
	Scanner.skip(rightParToken);
	Scanner.skip(leftCurlToken);
	ws.body = StatmList.parse();
	Scanner.skip(rightCurlToken);
	
	
	Log.leaveParser("</variable>");
	return null;
    }

    @Override void printTree() {
	//TODO:-- Must be changed in part 1:
    }
}
