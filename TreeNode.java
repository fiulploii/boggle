import java.util.List;

public class TreeNode 
{
	public char letter = 0;
	public Integer count = 0;
	public boolean isLastLetter = false;
	
	List<TreeNode> children;

	public TreeNode( char letter )
	{
		this.letter = letter;
		this.count++;
	}
	
	public void addWord( String word ) 
	{
		char letter = word.charAt( 0 );
		boolean isNewLetter = true;

		if( word.length() == 0 )
		{
			isLastLetter = true;
			return;
		}
		
		for( TreeNode node : children )
		{
			if( node.letter == letter )
			{
				node.count++;
				node.addWord( word.substring( 1 ) );
				isNewLetter = false;
			}
		}
		
		if( isNewLetter )
		{
			children.add( new TreeNode( letter ) );
		}
	}
}
