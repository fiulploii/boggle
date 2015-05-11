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
	
	void solve( TreeNode subtree, List<Die> neighbours, String prefix )
	{
		for( Die die : neighbours )
		{
			TreeNode nextTree = subtree.children.get( die.getChar() );
			
			if( nextTree == null )
			{
				return;
			}
			
			if( nextTree.isLastLetter == true )
			{
			}
		}
	}
	
	List<String> getWords()
	{
		return words;
	}
}
