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
		try {
			sourceLine = sourceFile.readLine();
			sourceLine += "  ";
			System.out.print("Init: " + sourceLine);
			//Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sourcePos = 0;
		curC = nextC = ' ';
		readNext();
		readNext();
	}

	public static void finish() {
		if (sourceFile != null) {
			try {
				sourceFile.close();
			} catch (IOException e) {

			}
		}
	}

	/*
	 * Check to see if there is more to read in the file or if we have reached
	 * the end of the file
	 * 
	 * @return True if there is more to read, otherwise false
	 */
	public static boolean isMoreToRead() throws IOException {
		if (sourcePos < sourceLine.length() || sourceFile.ready()) {
			return true;
		}
		return false;
	}

	/*
	 * @return The current line numbder
	 */
	public static int curLineNum() {

		return (sourceFile == null ? 0 : sourceFile.getLineNumber());
	}

	/*
	 * Put the next character and move it into curC. Also read the next
	 * character and assign it to nextC. If we have arraived at the end of the line,
	 * we will move to the next line and read it from the begining
	 */
	public static void readNext() {
		curC = nextC;
		if(sourcePos == 0){
			System.out.print("TEST: " + sourceLine + "\n");
			Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);
		}
		if (sourcePos < sourceLine.length()) {
			nextC = sourceLine.charAt(sourcePos);
			sourcePos += 1;
		} else {
			System.out.print("New line\n");
			try {
				sourceLine = sourceFile.readLine();
				sourceLine += "  ";
				System.out.print("New Line: " + sourceLine);
				Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);

			} catch (IOException e) {
				Log.noteError("Could not read from file");
				e.printStackTrace();
				return;
			}
			if (sourceLine == null) {
				return;
			}
			sourcePos = 0;
			nextC = sourceLine.charAt(sourcePos);
			sourcePos += 1;
		}
	}

	/*
	 * Skip the current line and start at he beginin of the next. Does not
	 * change curC or nextC
	 */
	public static void skipLine() {
		//Log.noteSourceLine(sourceFile.getLineNumber(), sourceLine);
		sourcePos = 0;
		try {
			sourceLine = sourceFile.readLine();
		} catch (IOException e) {
			Log.noteError("Could not read from file");
			e.printStackTrace();
			return;
		}
	}
}
