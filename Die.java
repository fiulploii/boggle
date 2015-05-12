import java.util.ArrayList;
import java.util.List;

public class Die
{
	int id = 0;
	String faces;
	int rolledFace = 0;
	int x = 0;
	int y = 0;
	
	boolean used = false;
	ArrayList<String> words = new ArrayList<String>();
	
	Die( String face, Integer id )
	{
		this.faces = face;
		this.id = id;
	}

	public String toString()
	{
		return faces.substring( rolledFace, rolledFace + 1 );
	}
	
	char getChar()
	{
		return faces.charAt( rolledFace );
	}
	
	void roll( int face )
	{
		this.rolledFace = face;
	}
	
	void solve( TreeNode subtree, Board board, String prefix )
	{
		if( used )
		{
			return;
		}
		else
		{
			used = true;
		}
		
		prefix += this.toString();
		
		List<Die> neighbours = board.getNeighbours( x, y );
		
		for( Die die : neighbours )
		{
			TreeNode nextTree = subtree.children.get( die.getChar() );

			if( nextTree == null )
			{
				continue;
			}
			
			if( nextTree.isLastLetter == true )
			{
				words.add( prefix + nextTree.letter );
			}
			
			die.solve( nextTree, board, prefix );
		}
		
		used = false;
	}
	
	List<String> getWords()
	{
		return words;
	}
}
