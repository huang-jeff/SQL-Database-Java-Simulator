import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class SQLDatabase 
{
	public static void main(String[] args) 
	{
		boolean terminateDB = false;
		TreeMap<String,String> header = new TreeMap<String, String>();
		ArrayList<TreeMap<String,String>> database = new ArrayList<TreeMap<String, String>>();
		header.put("First", "Smith");
		header.put("Last", "Tom");
		header.put("City", "Seattle");
		header.put("Age", "45");
		header.put("Salary", "$200");
		database.add(header);
		Data.createDatabase("database", database);
		Data.printDatabase(database);
		System.out.println("SQL Database is currently running. To end the program enter: \"exit\"");
		System.out.println("The name of the database is currently: \"database\".");
		while(!terminateDB)
		{
			Query q = Query.readQuery("exit", true);
			if (q == null) 
			{
				System.out.println("SQL Database has been terminated.");
				terminateDB = true;
			}
			else if (q.isSelect())
			{
				System.out.println(select(q, database));
			}
			else if (q.isInsert()) 
			{
				System.out.println(insert(q, database));
			} 
			else if(q.isDelete())
			{
				System.out.println(delete(q, database));
			}
			else if(q.isUpdate())
			{
				System.out.println(update(q, database));
			}
			else
			{
				System.out.println("Function does not exist. Please try again.");
			}
		}
	}

	public static boolean select (Query selectQuery, ArrayList<TreeMap<String,String>> data)
	{
		if(selectQuery.isSelect())
		{
			ArrayList<String> keys = new ArrayList<String>(Arrays.asList(selectQuery.getSelectFields()));
			if(selectQuery.getWhereID() > 0)
			{
				if (keys.contains("*"))
				{
					Data.printDatabase(selectQuery.getWhereID(), Data.focus(selectQuery.getRelationName(), selectQuery.getWhereID()));
					return true;
				}
				else if(selectQuery.getSelectFields() != null && !keys.contains("*"))
				{
					Data.printDatabase(selectQuery.getWhereID(), Data.focusMap(selectQuery.getRelationName(), selectQuery.getWhereID(), selectQuery.getSelectFields()));
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if(keys.contains("*"))
				{
					Data.printDatabase(Data.focus(selectQuery.getRelationName()));
					return true;
				}
				else if(selectQuery.getSelectFields() != null && !keys.contains("*")) 
				{
					Data.printDatabase(Data.focus(selectQuery.getRelationName(), selectQuery.getSelectFields()));
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}

	public static boolean insert(Query insertQuery, ArrayList<TreeMap<String,String>> data) 
	{
		if(insertQuery.isInsert())
		{
			if(Data.getTables().contains(insertQuery.getRelationName()))
			{
				if(insertQuery.getInsertValues().length == Data.getDatabaseFields(insertQuery.getRelationName()).size())
				{
					TreeMap<String,String> insertMap = new TreeMap<String, String>();
					ArrayList<String> keys = new ArrayList<String>();
					for (Object values :Data.getDatabaseFields(insertQuery.getRelationName()).toArray()) 
					{
						keys.add((String) values);
					}
					String[] variables = insertQuery.getInsertValues();
					for (int i = 0; i < keys.size(); i++)
					{
						insertMap.put(keys.get(i),variables[i]);
					}
					Data.addElement(insertQuery.getRelationName(),insertMap);
					return true;
				}
				else
				{
					System.out.println("User did not input the right amount of values.");
					return false;
				}
			}
		}
		return false;
	}

	public static boolean delete(Query deleteQuery, ArrayList<TreeMap<String,String>> data)
	{
		if(deleteQuery.isDelete()){
			if(Data.getTables().contains(deleteQuery.getRelationName())){
				if(deleteQuery.getWhereID() != -1)
				{
					Data.removeElement(deleteQuery.getRelationName(), deleteQuery.getWhereID());
					return true;
				} 
				else
				{
					Data.removeElement(deleteQuery.getRelationName());
					return true;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public static boolean update(Query updateQuery, ArrayList<TreeMap<String,String>> data)
	{
		if(updateQuery.isUpdate())
		{
			if(Data.getTables().contains(updateQuery.getRelationName()))
			{
				if (Data.getDatabaseFields(updateQuery.getRelationName()).contains(updateQuery.getUpdateField()))
				{
					if (updateQuery.getWhereID() == -1)
					{
						for(Integer i :Data.getIndex(updateQuery.getRelationName()))
						{
							Data.updateElements(updateQuery.getRelationName(), i, updateQuery.getUpdateField(), updateQuery.getUpdateValue());
						}
						return true;
					}
					else if (updateQuery.getWhereID() == 0)
					{
						return false;
					}
					else
					{
						Data.updateElements(updateQuery.getRelationName(), updateQuery.getWhereID(), updateQuery.getUpdateField(), updateQuery.getUpdateValue());
						return true;
					}
				}
				else 
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}