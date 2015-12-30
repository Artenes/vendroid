package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Rotas {
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, "vendroid.db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
		
	}
	
	public Tabela_Rotas (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Rotas open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int ID, String Descricao, int Vendedor, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("rot_descricao", Descricao);
		dados.put("rot_vendedor", Vendedor);
		dados.put("rot_datacad", Data);
		
		return this.mDb.insert("rotas", null, dados);
	}
	
	public boolean Atualizar (String IDlinha, String Descricao, int Vendedor) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("rot_descricao", Descricao);
		dados.put("rot_vendedor", Vendedor);
		
		return this.mDb.update("rotas", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("rotas", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("rotas", new String[] {"_id", "rot_descricao", "rot_vendedor"}, null, null, null, null, null);
	}
	
	public Cursor Busca (String rota) {
		return this.mDb.query("rotas", new String[] {"rot_descricao"}, "_id=?", new String[] {rota}, null, null, null);
	}
	
	public Cursor Busca_Falsa (String id, String rota) {
		String sql = "_id LIKE '"+ id +"%' AND rot_descricao LIKE '"+rota+"%'";
		return this.mDb.query("rotas", new String [] {"_id", "rot_descricao", "rot_vendedor"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("rotas", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}

	public String Rota (String ID) {
		Cursor c = this.mDb.query("rotas", new String[] {"rot_descricao"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("rot_descricao")); 
	}
	
	public String Vendedor (String ID) {
		Cursor c = this.mDb.query("rotas", new String[] {"rot_vendedor"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("rot_vendedor")); 
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("rotas", new String[] {"rot_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("rot_datacad")); 
	}
	
	public int RetornaID (String Rota) {
		Cursor c = this.mDb.query("rotas", new String[] {"_id"}, "rot_descricao=?", new String[] {Rota}, null, null, null); 
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}
	
	public boolean VerificarRota (String ID) {
		Cursor c = this.mDb.query("rotas", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
	
}
