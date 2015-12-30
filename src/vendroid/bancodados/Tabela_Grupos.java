package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Grupos {
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, "vendroid.db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
		
	}
	
	public Tabela_Grupos (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Grupos open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (String ID, String Descricao, String Date) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("grup_descricao", Descricao);
		dados.put("grup_datacad", Date);
		
		return this.mDb.insert("grupos", null, dados);
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("grupos", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("grupos", new String[] {"_id", "grup_descricao", "grup_datacad"}, null, null, null, null,null);
	}
	
	public Cursor BuscaFiltro (String id, String grupo) {
		String sql = "_id LIKE '"+ id +"%' AND grup_descricao LIKE '"+grupo+"%'";
		return this.mDb.query("grupos", new String [] {"_id", "grup_descricao"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("grupos", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaIDfalsa (String ID) {
		return this.mDb.query("grupos", new String[] {"grup_descricao"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public String Grupo (String ID) {
		Cursor c = this.mDb.query("grupos", new String[] {"grup_descricao"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("grup_descricao"));
	}

	public String Data (String ID) {
		Cursor c = this.mDb.query("grupos", new String[] {"grup_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("grup_datacad")); 
	}
	
	public int RetornaId (String Grupo) {
		Cursor c = this.mDb.query("grupos", new String[] {"_id"}, "grup_descricao=?", new String[] {Grupo}, null, null, null); 
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}
	
	public boolean Atualizar (String IDlinha, String Descricao) {
		ContentValues dados = new ContentValues();
		dados.put("grup_descricao", Descricao);
		return this.mDb.update("grupos", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean VerificarGrupo (String grupo) {
		Cursor c = this.mDb.query("grupos", new String[] {"_id"}, "grup_descricao=?", new String[] {grupo}, null, null, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
}
