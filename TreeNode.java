import java.util.ArrayList;
import java.util.List;

public class TreeNode 
{
	public char letter = 0;
	public Integer count = 0;
	public boolean isLastLetter = false;
	
	List<TreeNode> children = new ArrayList<TreeNode>();

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

		char letter = word.charAt( 0 );
		TreeNode letterNode = null;

		for( TreeNode node : children )
		{
			if( node.letter == letter )
			{
				letterNode = node;
				break;
			}
		}
		
		if( letterNode == null )
		{
			letterNode = new TreeNode( letter );
			children.add( letterNode );
		}
		
		letterNode.count++;
		letterNode.addWord( word.substring( 1 ) );
	}
}
