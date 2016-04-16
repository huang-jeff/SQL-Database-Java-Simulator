import java.util.*;

public class Data 
{
	private static TreeMap<String, ArrayList<TreeMap<String, String>>> data = new TreeMap<String, ArrayList<TreeMap<String, String>>>();

	public static ArrayList<TreeMap<String, String>> focus(String name, String[] keys)
	{
		ArrayList<TreeMap<String,String>> temp = new ArrayList<TreeMap<String, String>>();
		for(int i = 0; i < data.get(name).size(); i++) 
		{
			TreeMap<String, String> temp2 = new TreeMap<String, String>();
			ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(keys));
			ArrayList<String> fields = new ArrayList<String>();
			if (data.get(name).size() < i) 
			{
				return null;
			} 
			else 
			{
				for (String key : keys) 
				{
					fields.add(data.get(name).get(i).get(key));
				}
			}
			for (int j = 0; j < keysList.size(); j++)
			{
				temp2.put((String) keysList.get(j), (String) fields.get(j));
			}
			temp.add(temp2);
		}
		return temp;
	}
	
	public static ArrayList<TreeMap<String, String>> focus(String name)
	{
		return data.get(name);
	}
	
	public static SortedMap<String, String> focus(String name, int index) 
	{
		if (data.size() < index-1)
		{
			return null;
		} 
		else 
		{
			return data.get(name).get(index - 1);
		}
	}

	public static SortedMap<String, String> focusMap(String name, int index, String[] keys) 
	{
		TreeMap<String, String> temp = new TreeMap<String, String>();
		ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(keys));
		ArrayList<String> fields = new ArrayList<String>();
		if (data.get(name).size() <= index - 1)
		{
			return null;
		} 
		else
		{
			for (String key : keys) 
			{
				fields.add(data.get(name).get(index - 1).get(key));
			}
		}
		for (int i = 0; i < keysList.size(); i++) 
		{
			temp.put((String) keysList.get(i), (String) fields.get(i));
		}
		return temp;
	}

	public static void addElement(String name, TreeMap<String, String> map)
	{
		Set<String> keys = getDatabaseFields(name);
		TreeMap<String, String> insertIndex = map;
		for (String key : map.keySet()) 
		{
			if (!(keys.contains(key)))
			{
				return;
			}
		}
		for (String key : keys)
		{
			if (!(map.keySet().contains(key)))
			{
				insertIndex.put(key, null);
			}
		}
		data.get(name).add(insertIndex);
	}

	public static void removeElement(String name, int index) 
	{
		if (data.get(name).size() > index - 1) 
		{
			deleteElement(name,index);
		}
	}

	public static void removeElement(String name)
	{
		for (int i = 0; i < data.get(name).size(); i++)
		{
			deleteElement(name,i);
		}
	}
	
	private static void deleteElement(String name, int index)
	{
        for (String keys : data.get(name).get(index).keySet())
        {
            data.get(name).get(index).put(keys,null);
        }
    }

	public static void updateElements(String name, int index, String columnName, String value)
	{
		if (data.get(name).size() > index - 1) 
		{
			if (data.get(name).get(0).containsKey(columnName))
			{
				data.get(name).get(index - 1).put(columnName, value);
			}
		}
	}

	public static Set<String> getDatabaseFields(String name)
	{
		return data.get(name).get(0).keySet();
	}
	
	public static ArrayList<Integer> getIndex(String name)
	{
        ArrayList<TreeMap<String,String>> temp1 = data.get(name);
        ArrayList<Integer> temp2 = new ArrayList<Integer>();
        for(int i = 0; i < temp1.size(); i++)
        {
            if(!(temp1.get(i).equals(null)))
            {
            	temp2.add(i+1);
            }
        }return temp2;
    }

	public static void createDatabase(String name, ArrayList<TreeMap<String, String>> database)
	{
		Data.data.put(name, database);
	}

	public static void printDatabase(int index, SortedMap<String, String> database)
	{
        if(database != null) 
        {
            if(database.keySet() != null) 
            {
                System.out.println(); 
                System.out.print("| ID ");
                for (String keys : database.keySet()) 
                {
                    System.out.print("| " + keys);
                }
                System.out.println("|");
                System.out.print("| " + index + " ");
                for (String keys : database.values())
                {
                    System.out.print("| " + keys + " ");
                }
                System.out.println(" |");
            }
            else
            {
                System.out.println("Index does not exist in the database.");
            }
        }
        else
        {
            System.out.println("Index does not exist in the database.");
        }
    }

	public static void printDatabase(ArrayList<TreeMap<String,String>> database) 
	{
		if (database != null) 
		{
			System.out.print("| ID ");
			for (String keys : database.get(database.size()-1).keySet())
			{
				System.out.print("| " + keys + " ");
			}
			System.out.println(" |");
			printDivider();
			for (int i = 1; i <= database.size(); i++)
			{
				boolean flag = true;
				System.out.print("| " + i + " ");
				for (String s : database.get(i - 1).values()) 
				{
					if(s != null)
					{
						System.out.print(" | " + s);
					}
					else if(flag)
					{
						System.out.print(" | ");
						flag = false;
					}
					else
					{
						System.out.print("          ");
					}

				}
				System.out.println(" |");
				printDivider();
			}
		}
		else
		{
			System.out.println("Database is currently empty.");
		}
	}

	public static Set<String> getTables() 
	{
		return data.keySet();
	}

	public static void printDivider()
	{
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
	}
}