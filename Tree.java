import java.util.List;

public class Tree 
{
	public TreeNode root = new TreeNode( '\0' );
	
	public void loadDictionary( List<String> dictionary )
	{
		for( String word : dictionary )
		{
			root.addWord( word );
		}
	}
	
	public void print()
	{
		root.print( "" );
	}
}
