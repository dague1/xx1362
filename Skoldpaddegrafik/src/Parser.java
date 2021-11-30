import java.util.ArrayList;

/**
 * En rekursiv medåknings-parser för Leonaspråket.
 *
 * Författare: Per Austrin
 */
public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public ArrayList<ParseTree> parse() throws SyntaxError {
		ArrayList<ParseTree> commands = new ArrayList<>();
        while(lexer.hasMoreTokens()){
            commands.add(BinTree());
        }
        return commands;
	}

	private ParseTree BinTree() throws SyntaxError {
		Token t = lexer.peekToken();
		switch (t.getType()) {
			case FORW:
			case BACK:
			case RIGHT:
			case LEFT:{
				lexer.nextToken();
				Token number = lexer.nextToken();
				if (number.getType() != TokenType.NUMBER) ThrowError(number.getRow());

				Token param = lexer.nextToken();
				if (param.getType() != TokenType.DOT)
					ThrowError(param.getRow());

				if((Integer)number.getData() == 0)
					ThrowError(number.getRow());

				return new CommandNode(t.getType(), (Integer) number.getData());
			}
			case COLOR: {
				lexer.nextToken();

				Token number = lexer.nextToken();
				if (number.getType() != TokenType.HEX) ThrowError(number.getRow());

				Token param = lexer.nextToken();
				if (param.getType() != TokenType.DOT) ThrowError(param.getRow());

                return new CommandNode(t.getType(), (Integer) number.getData());
			}

			case DOWN:
			case UP: {
				lexer.nextToken();
				if(!lexer.hasMoreTokens())
					ThrowError(t.getRow());
				Token param = lexer.nextToken();
				if (param.getType() != TokenType.DOT) ThrowError(param.getRow());
				return new CommandNode(t.getType());
			}
			case REP:
				lexer.nextToken();

				Token numReps = lexer.nextToken();
				if (numReps.getType() != TokenType.NUMBER) ThrowError(numReps.getRow());

				if((Integer)numReps.getData() == 0)
					ThrowError(numReps.getRow());

				Token param = lexer.peekToken();
				CommandSequenceNode commandSequence = new CommandSequenceNode((Integer)numReps.getData());

				if (param.getType() == TokenType.QUOTE) {
					lexer.nextToken(); //eat QUOTE
                    if (lexer.peekToken().getType() == TokenType.QUOTE)
                        ThrowError(lexer.peekToken().getRow());

					while(lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.QUOTE)  {
						commandSequence.addCommand(BinTree());
					}

                    Token quote = lexer.nextToken();
                    if (quote.getType() != TokenType.QUOTE)
                        ThrowError(quote.getRow());
				}
				else {
					commandSequence.addCommand(BinTree());
				}

				return commandSequence;
			default:
				ThrowError(t.getRow());
		}
		return null;
	}

	void ThrowError(int row) throws SyntaxError {
		throw new SyntaxError(row);
	}

}
