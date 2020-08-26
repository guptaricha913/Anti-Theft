package com.example.anti_theft;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SaveNo extends SQLiteOpenHelper 
{		
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Save_Sim_Serial_No";
	private static final String TABLE_SRNO = "Sim_Serial"; 
	private static final String SR_NO = "srno";
	public SaveNo(Context context, String name,CursorFactory factory, int version) 
	{
			super(context,DATABASE_NAME,null,DATABASE_VERSION);			
	}
@Override
public void onCreate(SQLiteDatabase db) {
	String CREATE_CONFIG_TABLE = "CREATE TABLE " + TABLE_SRNO + "("+SR_NO+ " TEXT PRIMARY KEY);";		
	db.execSQL(CREATE_CONFIG_TABLE);		
}
public Cursor getConfigurations()
{	
	SQLiteDatabase db=this.getReadableDatabase();
	Cursor cursor=db.rawQuery("select *from "+TABLE_SRNO+";", null);		
	return cursor;
}	
public String insert(String srno)
{
	String msg="";
	SQLiteDatabase db=this.getWritableDatabase();
	try
	{		
		String s="insert into "+TABLE_SRNO+ " values('"+srno+"');";
		db.execSQL(s);
		msg="data";
	}
	catch(Exception e)
	{
		msg="Serial number exists in database";
	}
	return msg;
}	
public String delete(String srno)
{
	String msg="";
	SQLiteDatabase db=this.getWritableDatabase();
	try
	{		
		String s="delete from "+TABLE_SRNO+ " where "+SR_NO+"='"+srno+"';";
		db.execSQL(s);
		msg="Deleted";
	}
	catch(Exception e)
	{
		msg="Deletion failed "+e ;
	}
	return msg;
}	
@Override
public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) 
{
	db.execSQL("DROP TABLE IF EXISTS "+TABLE_SRNO);
    onCreate(db);		
}
}
