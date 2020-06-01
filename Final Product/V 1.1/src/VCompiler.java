import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class VCompiler
{
	HashMap<String, Variable> vars;
	String totalCode;
	Scanner console;
	PrintWriter write;
	
	public VCompiler(File file)
	{
		console = null;
		write = null;
		try
		{
//			file = new File("Hello");
			this.console = new Scanner(file);
			this.write = new PrintWriter(new File(file.getName() +  " Output.txt"));
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		vars = new HashMap<>();
		System.out.println("Welcome to V, the language of the future");
		interpret();
		
		write.close();
	}	
	
	public void interpret()
	{
		while(console.hasNextLine())
		{
			String line = console.nextLine();
			try
			{
				if(line.equals("end"))
					break;
				
				determine(line);
			}
			catch(Exception e)
			{
				System.out.println("There was an error on this line of code: " + line);
				e.printStackTrace();
			}
		}
	}
	
	private void determine(String code)
	{
		boolean b = true;
		Pattern initialisation = Pattern.compile("(integer|decimal|Characters|binary|character) [a-zA-Z\\d]+ is .+");
		Pattern assignment = Pattern.compile("[a-zA-Z\\d]+ is .+");
		Pattern conditional = Pattern.compile("if [\\w\\d]+ (equals|<|<=|>=|does not equals) [\\w\\d]+");
		Pattern forLoop = Pattern.compile("for \\d+ times");
		Pattern fromLoop = Pattern.compile("from \\w+ is \\d+ to \\d+");
		Pattern whileLoop = Pattern.compile("while \\w+ (is|>|<|<=|>=) [\"\\w\\d\"]+");
		Pattern print = Pattern.compile("print\\s?(\\s?.*)");
		Pattern operations = Pattern.compile("[A-Za-z\\d]+ (\\+|-|\\*|/|and|or|not|xor|) .*");
		
		Scanner line = new Scanner(code);
		
//		System.out.println(code);
		
		if(initialisation.matches(initialisation.pattern(), code))
		{
			String type = line.next();
			String name = line.next();
			line.next(); String value = line.nextLine();
			
//			System.out.println(value);
			
//			if(value.contains("\""))
//				value.removeAll(1, value.length() - 2);
			
//			System.out.println(value);
			
			vars.put(name, new Variable(type, name, getValue(value)));
			
//			System.out.println(vars.get(name));
		}
		else if(assignment.matches(assignment.pattern(), code))
		{
//			System.out.println("assignment");
//			Scanner console1 = new Scanner(code);
//			String type = console.next();
			String name = line.next();
			line.next(); String value = line.nextLine();
			
//			System.out.println(name + " " + value);
			
			vars.put(name, new Variable(vars.get(name).t, name, getValue(value)));
		}
		else if(conditional.matches(conditional.pattern(), code))
		{
			line.next();
			String value = line.nextLine();
			String next = console.nextLine();
			String code2 = "";
			while(!next.equals("end if"))
			{
//				System.out.println(next);
				code2 += next + "\n";
				next = console.nextLine();
			}
			if(getValue(value).equals("true"))
			{
				determine(code2);
			}
		}
		else if(forLoop.matches(forLoop.pattern(), code))
		{
			String n = code.split(" ")[1];
			int i = Integer.parseInt(n);
			String next = console.nextLine();
			String code2 = "";
			while(!next.equals("end loop"))
			{
//				System.out.println(next);
				code2 += next + "\n";
				next = console.nextLine();
			}
			
//			System.out.println(code2);
			forLoop(code2, i);
		}
		else if(fromLoop.matches(fromLoop.pattern(), code))
		{
			line.next();
			String name = line.next();
			line.next();
			int from = Integer.parseInt(line.next());
			line.next();
			int to = Integer.parseInt(line.next());
			vars.put(name, new Variable("integer", name, from + ""));
			String next = console.nextLine();
			String code2 = "";
			while(!next.equals("end loop"))
			{
//				System.out.println(next);
				code2 += next + "\n";
				next = console.nextLine();
			}
//			System.out.println(code2);
//			System.out.println();
			fromLoop(code2, to, name, from);
		}
		else if(whileLoop.matches(whileLoop.pattern(), code))
		{
//			System.out.println(code);
			line.next();
			String value = line.nextLine();
//			System.out.println(value);
			String next = console.nextLine();
			String code2 = "";
			ArrayList<String> code2s = new ArrayList<>();
			while(!next.equals("end loop"))
			{
//				System.out.println(next);
				code2 += next + "\n";
				code2s.add(next);
				next = console.nextLine();
			}
//			System.out.println(code2);
			while(getValue(value).equals("true"))
			{
//				System.out.println("while");
//				System.out.println(vars.get("a").val);
				
				for(String l : code2s)
					determine(l);
			}
		}
		else if(print.matches(print.pattern(), code))
		{
//			System.out.println("print");
			line.next();
			String str = line.nextLine().replace(" )", "");
//			System.out.println(str);
			write.print(getValue(str) + "\n");
//			System.out.println(getValue(str) + "\n");
		}
//		else if(operations.matches(operations.pattern(), code))
//		{
//			
//		}
	}
	
	public String getValue(String code)
	{
		String[] arr = code.split(" ");
		String s = "";
		
//		System.out.println(code);
		
		for(String str : arr)
		{
			if(vars.containsKey(str))
			{
//				if(vars.get(str).javaType.contentEquals("String"))
//				{
//					s += "\"" + vars.get(str).val + "\"" + " ";
//				}
//				else if(vars.get(str).javaType.contentEquals("Character"))
//				{
//					s += "\'" + vars.get(str).val + "\'" + " ";
//				}
//				else 
				if(vars.get(str).javaType.contentEquals("Double"))
				{
					s += Double.parseDouble(vars.get(str).val) + " ";
				}
				else
				{
					s += vars.get(str).val + " ";
				}
			}
			else if(str.contains("\""))
			{
				str.replaceAll("[\s\"]", "");
				s += str + " ";
			}
			else
			{
				s += str + " ";
			}
		}
		
		LinkedList<String> q = new LinkedList<>();
		arr = s.split(" ");
		for(String str : arr)
		{
			if(str.equals("") || str.equals(" ") || str.equals("\n"))
			{
				continue;
			}
			String str2 = str.replaceAll("[\\s\n]", "");
			q.add(str2);
		}
		
//		System.out.println(q);
		
		while(q.size() > 1)
		{
			String val = q.pollFirst();
			String operation = q.pollFirst();
			
			if(operation.equals("not"))
			{
				operation += " equals";
			}
			
			String operand = q.pollFirst();
			
//			System.out.println(val + " " + operation + " " + operand);
			
			if(val == null || operation == null || operand == null)
			{
				break;
			}
			
			if(operation.equals("+") || operation.equals("*") || operation.equals("/") || operation.equals("-") || operation.equals("and") || operation.equals("or") || operation.equals("xor"))
			{
				q.addFirst(performOperation(val, operation, operand));
			}
			else
			{
				q.addFirst("" + performBoolean(val, operation, operand));
			}
			
//			System.out.println(q);
		}
//		if()
//		{
//			
//		}
		
		return q.pollFirst();
	}
	
	public Boolean performBoolean(String val, String operation, String operand)
	{
//		System.out.println("boolean");
		Pattern string = Pattern.compile("\".*\"");
		Pattern character = Pattern.compile("\'.\'");
		Pattern integer = Pattern.compile("\\d+");
		Pattern doub = Pattern.compile("\\d+\\.\\d*");
		Pattern bool = Pattern.compile("(true|false)");
		
//		String temp = val;
//		val = operand;
//		operand = temp;
		
//		System.out.println(val + " " + operation + " " + operand);
		
		if(operation.equals("equals"))
		{
			return val.equals(operand);
		}
		else if(operation.equals("not equals"))
		{
			return(!val.equals(operand));
		}
		else if(operation.equals("<"))
		{
			return(doub.matches(doub.pattern(), val) || doub.matches(doub.pattern(), operand))? Double.parseDouble(val) < Double.parseDouble(operand) :  Integer.parseInt(val) < Integer.parseInt(operand);
		}
		else if(operation.equals(">"))
		{
//			System.out.println(">");
			return(doub.matches(doub.pattern(), val) || doub.matches(doub.pattern(), operand))? Double.parseDouble(val) > Double.parseDouble(operand) :  Integer.parseInt(val) > Integer.parseInt(operand);
		}
		else if(operation.equals("<="))
		{
			return(doub.matches(doub.pattern(), val) || doub.matches(doub.pattern(), operand))? Double.parseDouble(val) <= Double.parseDouble(operand) :  Integer.parseInt(val) <= Integer.parseInt(operand);
		}
		else if(operation.equals(">="))
		{
			return(doub.matches(doub.pattern(), val) || doub.matches(doub.pattern(), operand))? Double.parseDouble(val) >= Double.parseDouble(operand) :  Integer.parseInt(val) >= Integer.parseInt(operand);
		}
		
		
		return false;
	}
	
	public String performOperation(String val, String operation, String operand)
	{
//		System.out.println(val + " " + operation + " " + operand);
		Pattern string = Pattern.compile("[^(true|false)\\.\\d]+");
		Pattern character = Pattern.compile("\'.\'");
		Pattern integer = Pattern.compile("\\d+");
		Pattern doub = Pattern.compile("\\d+\\.\\d*");
		Pattern bool = Pattern.compile("(true|false)");
		
//		String temp = val;
//		val = operand;
//		operand = temp;
		
		if(Pattern.matches(string.pattern(), val) || Pattern.matches(string.pattern(), operand))
		{
			if(operation.equals("+"))
			{
				return val + operand;
			}
			else if(operation.equals("*"))
			{
				int num = Integer.parseInt(operand);
				
				String str = "";
				
				for(int i = 0; i < num; i++)
				{
					str += val;
				}
			}
		}
		else if(string.matches(doub.pattern(), val) || string.matches(doub.pattern(), operand))
		{
			if(operation.equals("+"))
			{
				return Double.parseDouble(val) + Double.parseDouble(operand) + "";
			}
			else if(operation.equals("-"))
			{
				return Double.parseDouble(val) - Double.parseDouble(operand) + "";
			}
			else if(operation.equals("*"))
			{
				return Double.parseDouble(val) * Double.parseDouble(operand) + "";
			}
			else if(operation.equals("/"))
			{
				return Double.parseDouble(val) / Double.parseDouble(operand) + "";
			}
		}
		else if(string.matches(integer.pattern(), val) && string.matches(integer.pattern(), operand))
		{
			if(operation.equals("+"))
			{
				return Integer.parseInt(val) + Integer.parseInt(operand) + "";
			}
			else if(operation.equals("-"))
			{
				return Integer.parseInt(val) - Integer.parseInt(operand) + "";
			}
			else if(operation.equals("*"))
			{
				return Integer.parseInt(val) * Integer.parseInt(operand) + "";
			}
			else if(operation.equals("/"))
			{
				return Integer.parseInt(val) / Integer.parseInt(operand) + "";
			}
		}
		else if(string.matches(bool.pattern(), val) && string.matches(bool.pattern(), operand))
		{
			if(operation.equals("and"))
			{
				return (Boolean.parseBoolean(val) && Boolean.parseBoolean(operand)) + "";
			}
			else if(operation.equals("or"))
			{
				return (Boolean.parseBoolean(val) || Boolean.parseBoolean(operand)) + "";
			}
			else if(operation.equals("xor"))
			{
				return (Boolean.parseBoolean(val) ^ Boolean.parseBoolean(operand)) + "";
			}
		}
		
		return null;
	}
	
	public void forLoop(String code, int times)
	{
		String[] s = code.split("\n");
		for(int i = 0; i < times; i++)
		{
			for(String str : s)
			{
				if(str.equals(" ") || str.equals(""))
					continue;
//				System.out.println(str);
				determine(str);
			}
		}
	}
	
	public void fromLoop(String code, int to, String var, int from)
	{
		if(to < from)
		{
//			System.out.println("if");
			String[] s = code.split("\n");
			while(Integer.parseInt(vars.get(var).val) > to)
			{
				for(String str : s)
				{
					if(str.equals(" ") || str.equals(""))
						continue;
//					System.out.println(str);
					determine(str);
				}
			}
			
			vars.get(var).val = Integer.parseInt(vars.get(var).val) - 1 + "";
		}
		else if(from < to)
		{
//			System.out.println("else if");
			String[] s = code.split("\n");
//			System.out.println(Arrays.toString(s));
			while(Integer.parseInt(vars.get(var).val) < to)
			{
				for(String str : s)
				{
					if(str.equals(" ") || str.equals(""))
						continue;
//					System.out.println(str);
					determine(str);	
				}
				
//				System.out.println(vars.get(var).val);
				vars.get(var).val = Integer.parseInt(vars.get(var).val) + 1 + "";
			}
		}
	}
	
//	public void initialise(String code)
//	{
//		String[] strs = code.split(" is ");
//		
//	}
	
	public boolean end(String code)
	{
		return code.contains("end");
	}
	
	public static void main(String[] args) 
	{
		new VCompiler(null);
//		new V();
//		System.out.println(Pattern.matches("\".*\"", "5"));
	}
}
