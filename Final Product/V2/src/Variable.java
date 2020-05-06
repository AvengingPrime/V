import java.util.HashMap;

public class Variable
{
	Type type;
	String t;
	String javaType;
	String name;
//	T value;
	String val;
	
	public static enum Type
	{
		nothing, integer, decimal, binary, character, Characters;
		
		public static Type getType(String type)
		{
			switch(type)
			{
				case "integer" :
					return integer;
				case "decimal" : 
					return decimal;
				case "binary" : 
					return binary;
				case "character" :
					return character;
				case "Characters" :
					return Characters;
				default :
					return nothing;
			}
		}
		
		public static String getClass(Type type)
		{
			switch(type)
			{
				case integer :
					return "Integer";
				case decimal : 
					return "Double";
				case binary : 
					return "Boolean";
				case character :
					return "Character";
				case Characters :
					return "String";
				default : 
					return null;
			}
		}
	}
	
	public Variable(String type, String name, String value)
	{
		this.type = Type.getType(type);
		this.t = type;
		this.javaType = Type.getClass(this.type) + "";
		this.name = name;
//		T = Type.getClass(this.type);
//		this.value = value;
		this.val = value + "";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.name.equals((String) obj);
	}
	
//	public void operation(String code)
//	{
//		
//	}
	
	public static void main(String[] args) {
		HashMap<String, Variable> nums = new HashMap<>();
		
//		nums.put("nums", new Variable<>("fasdk", "nsjab", 5));
		
//		nums.get(key)
	}
}