import java.util.HashMap;

public class TreeNode 
{
	public char letter = 0;
	public Integer count = 0;
	public boolean isLastLetter = false;
	
	HashMap<Character,TreeNode> children = new HashMap<Character,TreeNode>();

	public TreeNode( char letter )
	{
		this.letter = letter;
	}
	
	public void addWord( String word ) 
	{
		if( word.length() == 0 )
		{
			isLastLetter = true;
			return;
		}

		Character letter = Character.valueOf( word.charAt( 0 ) );
		TreeNode letterNode = null;

		letterNode = children.get( letter ) ;
		
		if( letterNode == null )
		{
			letterNode = new TreeNode( letter );
			children.put( letter, letterNode );
		}
		
		letterNode.count++;
		letterNode.addWord( word.substring( 1 ) );
	}
}
