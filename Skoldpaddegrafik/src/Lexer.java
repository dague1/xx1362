import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * En klass för att göra lexikal analys, konvertera indataströmmen
 * till en sekvens av tokens.  Den här klassen läser in hela
 * indatasträngen och konverterar den på en gång i konstruktorn.  Man
 * kan tänka sig en variant som läser indataströmmen allt eftersom
 * tokens efterfrågas av parsern, men det blir lite mer komplicerat.
 *
 * Författare: Per Austrin
 */
public class Lexer {
	private String input;
	private List<Token> tokens;
	private int currentToken;
	private int currentRow;


	private static String readInput(InputStream f) throws java.io.IOException {
		//InputStream inputStream       = new FileInputStream("C:/Users/ljung/IdeaProjects/Skoldpaddegrafik/test.in"); // Läser från en testfil. Avkommentera när lokal.
		Reader stdin = new InputStreamReader(f); // Ändra "f" till inputStream för lokal körning.
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
		int read = 0;
		while ((read = stdin.read(input)) != -1) {
			buf.append(input, 0, read);
		}
		return buf.toString();
	}


	public Lexer(InputStream in) throws java.io.IOException {
		String input = Lexer.readInput(in).toUpperCase();
		// Ett regex som beskriver hur ett token kan se ut, plus whitespace (som vi här vill ignorera helt)
		Pattern tokenPattern = Pattern.compile("\\n|\\.|%.*|FORW(\\s|%.*)|BACK(\\s|%.*)|LEFT(\\s|%.*)|RIGHT(\\s|%.*)|UP|DOWN|COLOR(\\s)|\"|REP(\\s|%.*)|#[0-9A-Fa-f]{6}|[0-9]+(\\s|\\.|%.*)|\\s+");
		Matcher m = tokenPattern.matcher(input);
		int inputPos = 0;
		tokens = new ArrayList<Token>();
		currentToken = 0;
		currentRow = 1;

		while (m.find()) {
			if (m.start() != inputPos) {
				tokens.add(new Token(TokenType.Invalid, currentRow));
				return;
			}

			String group = m.group();
			int commentIndex = group.indexOf('%');
            if(commentIndex != -1){
                group = group.substring(0, commentIndex);
            }

			if(group.isEmpty()){
				inputPos = m.end();
				continue;
			}

			if (group.trim().equals("FORW"))
				tokens.add(new Token(TokenType.FORW, currentRow));
			else if (group.trim().equals("BACK"))
				tokens.add(new Token(TokenType.BACK, currentRow));
			else if (group.trim().equals("RIGHT"))
				tokens.add(new Token(TokenType.RIGHT, currentRow));
			else if (group.trim().equals("LEFT"))
				tokens.add(new Token(TokenType.LEFT, currentRow));
			else if (group.equals("UP"))
				tokens.add(new Token(TokenType.UP, currentRow));
			else if (group.equals("DOWN"))
				tokens.add(new Token(TokenType.DOWN, currentRow));
			else if (group.trim().equals("COLOR"))
				tokens.add(new Token(TokenType.COLOR, currentRow));
			else if (group.trim().equals("REP"))
				tokens.add(new Token(TokenType.REP, currentRow));
			else if (group.equals(".") )
				tokens.add(new Token(TokenType.DOT, currentRow));
			else if (group.equals("\""))
				tokens.add(new Token(TokenType.QUOTE, currentRow));
			else if (Character.isDigit(group.charAt(0))){
				String trimmed = m.group().trim();
				tokens.add(new Token(TokenType.NUMBER, currentRow, Integer.parseInt(trimmed.replaceAll("[^0-9]", ""))));
				if (trimmed.charAt(trimmed.length() - 1) == '.')
					tokens.add(new Token(TokenType.DOT, currentRow));
			}
			else if (group.charAt(0) == '#')
				tokens.add(new Token(TokenType.HEX, currentRow, Integer.parseInt(m.group().substring(1), 16)));

			if(group.contains("\n")){
				currentRow += findNumNewLines(group);
			}

			inputPos = m.end();
		}

		if (inputPos != input.length()) {
			tokens.add(new Token(TokenType.Invalid, currentRow));
        }

	}


	public Token peekToken() throws SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens())
			throw new SyntaxError(tokens.get(tokens.size() - 1).getRow());
		return tokens.get(currentToken);
	}


	public Token nextToken() throws SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}
 //
	private int findNumNewLines(String str){
		int num = 0;
		int start = str.indexOf('\n', 0);
		while(start != -1){
			++num;
			++start;
			start = str.indexOf('\n', start);
		}
		return num;
	}
}
