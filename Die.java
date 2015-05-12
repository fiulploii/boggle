import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Die
{
	int id = 0;
	String faces;
	int rolledFace = 0;
	int x = 0;
	int y = 0;
	final long randomSeed = 0x8888;
	Random random = new Random( randomSeed );
	
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
	
	void roll()
	{
		this.rolledFace = random.nextInt( 6 ); 
	}
	
	void solve( TreeNode subtree, Board board, String prefix )
	{
		if( used || subtree == null )
		{
			return;
		}

		used = true;
		prefix += this.toString();

		if( subtree.isLastLetter == true )
		{
			words.add( prefix );
		}
		
		List<Die> neighbours = board.getNeighbours( x, y );
		
		for( Die die : neighbours )
		{
			TreeNode nextTree = subtree.children.get( die.getChar() );

			if( nextTree == null )
			{
				continue;
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
