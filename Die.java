import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Die
{
	public 			int 				id 					= 0;
	public 			int 				x 					= 0;
	public 			int 				y 					= 0;
	public 			boolean 			usedInWord 			= false;
	public 			ArrayList<String>	wordsStartingHere 	= new ArrayList<String>();
	public 			char 				rolledFace			= '\0';

	private 		String				rolledFaceString	= "";
	private final 	long 				randomSeed			= 0x8888;
	private 		Random 				random 				= new Random( randomSeed );
	private			String 				faces				= null;
	
	
	Die( String faces, int id )
	{
		this.faces 	= faces;
		this.id 	= id;
		
		roll();
	}

	public String toString()
	{
		return rolledFaceString;
	}
	
	public char getChar()
	{
		return rolledFace;
	}
	
	public void roll()
	{
		rolledFace 			= faces.charAt( random.nextInt( 6 ) );
		rolledFaceString 	= String.valueOf( rolledFace );
	}
	
	public void solve( TreeNode subtree, Board board, String prefix )
	{
		if( usedInWord || subtree == null )
		{
			return;
		}

		usedInWord = true;
		prefix += this.toString();

		if( subtree.isLastLetter == true )
		{
			wordsStartingHere.add( prefix );
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
		
		usedInWord = false;
	}
	
	public List<String> getWords()
	{
		return wordsStartingHere;
	}
	
	public void clearWords()
	{
		wordsStartingHere.clear();
	}
}
