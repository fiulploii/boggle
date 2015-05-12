import java.util.HashMap;

public class TreeNode 
{
	public char 	letter 				= '\0';
	public int 		count 				= 0;
	public boolean 	isLastLetterOfWord 	= false;
	
	public HashMap<Character,TreeNode> children = new HashMap<Character,TreeNode>();

	public TreeNode( char letter )
	{
		this.letter = letter;
	}
	
	public void addWord( String word ) 
	{
		if( word.length() == 0 )
		{
			isLastLetterOfWord = true;
			return;
		}

		char 		letter 		= word.charAt( 0 );
		TreeNode 	letterNode 	= children.get( letter );

		if( letterNode == null )
		{
			letterNode = new TreeNode( letter );
			children.put( letter, letterNode );
		}
		
		letterNode.count++;
		letterNode.addWord( word.substring( 1 ) );
	}
	
	public void print( String prefix )
	{
		String horizontal = "";
		
		if( letter != '\0' )
		{
			System.out.println( prefix + letter + "              " + count + " " + isLastLetterOfWord );
			
			if( prefix.length() == 0 )
			{
				horizontal = "|-";
			}
			else
			{
				horizontal = "--";
			}
		}
		
		for( TreeNode node : children.values() )
		{
			node.print( prefix + horizontal );
		}
	}
}
