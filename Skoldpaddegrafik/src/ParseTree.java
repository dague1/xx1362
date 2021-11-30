// Författare: Per Austrin

import java.util.ArrayList;

// Ett syntaxträd
abstract class ParseTree {
	abstract public void execute(Leona leona);
}

class CommandNode extends ParseTree {
	TokenType type;
	Integer data = null;
	public CommandNode(TokenType type, Integer data) {
			this.type = type;
			this.data = data;
	}

	public CommandNode(TokenType type) {
		this.type = type;
	}

	public void execute(Leona leona) {
		switch (type){
			case FORW:
				leona.Move(data);
				break;
			case BACK:
				leona.Move(-data);
				break;
			case RIGHT:
				leona.Rotate(data);
				break;
			case LEFT:
				leona.Rotate(-data);
				break;
			case COLOR: {
				leona.SetColor(data);
				break;
			}
			case UP:
				leona.SetRecordState(false);
				break;
			case DOWN:
				leona.SetRecordState(true);
				break;
			default:
				break;
		}
	}
}

class CommandSequenceNode extends ParseTree {
	ArrayList<ParseTree> commands = new ArrayList<>();
	private Integer reps;
	public CommandSequenceNode(Integer reps) { this.reps = reps; }

	public CommandSequenceNode(CommandSequenceNode other) {// Copy-constructor
	    commands = new ArrayList<>(other.commands);
	}

	public void addCommand(ParseTree node) {
		commands.add(node);
	}

	public void execute(Leona leona) {
		for(int i = 0; i < reps; ++i){
			for (ParseTree command : commands) {
				command.execute(leona);
			}
		}
	}
}

