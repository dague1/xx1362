// Författare: Per Austrin

// De olika token-typer vi har i grammatiken
enum TokenType { //  An enum is a special "class" that represents a group of constants (unchangeable variables, like final variables). Not objects; enums.
	FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, HEX, REP, DOT, QUOTE, NUMBER, Invalid // Invalid - ERROR, HEX? NUMBER - DECIMAL
}

// Klass för att representera en token
// I praktiken vill man nog även spara info om vilken rad/position i
// indata som varje token kommer ifrån, för att kunna ge bättre
// felmeddelanden
class Token {
	private TokenType type;
	private Object data;
	private int row;
	
	public Token(TokenType type, int row) {
		this.type = type;
		this.data = null;
		this.row = row;
	}

	public Token(TokenType type, int row, Object data) {
		this.type = type;
		this.data = data;
		this.row = row;
	}

	public TokenType getType() { return type; }
	public Object getData() { return data; }
	public int getRow() { return row; }

}
