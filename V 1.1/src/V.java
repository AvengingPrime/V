import java.io.*;

public class V
{
	VCompiler compiler;
	GUI gui;
	
	public V()
	{
		gui = new GUI(this);
		compiler = null;
		run();
	}
	
	public void run()
	{
		while(true)
		{
			if(gui.run)
			{
				System.out.println("WORKS");
				compile(gui.save());
			}
		}
	}
	
	public void compile(File file)
	{
		System.out.println("Working");
		compiler = new VCompiler(file);
	}
	
	public static void main(String[] args)
	{
		V v = new V();
	}
}