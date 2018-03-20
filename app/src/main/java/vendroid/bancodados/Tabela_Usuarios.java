package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Usuarios {
	
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
	
	public Tabela_Usuarios (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Usuarios open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int ID, String Nome, String Senha, String Data) {
		ContentValues dados = new ContentValues();
		dados.put("_id", ID);
		dados.put("us_nome", Nome);
		dados.put("us_senha", Senha);
		dados.put("us_datacad", Data);
		
		return this.mDb.insert("usuario", null, dados);
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("usuario", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("usuario", new String[] {"_id", "us_nome", "us_senha", "us_datacad"}, null, null, null, null, null);
	}
	
	public boolean Atualizar (String IDlinha, String Nome, String Senha) {
		ContentValues dados = new ContentValues();
		dados.put("us_nome", Nome);
		dados.put("us_senha", Senha);
		return this.mDb.update("usuario", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor BuscaFiltro (String id, String usuario) {
		String sql = "_id LIKE '"+ id +"%' AND us_nome LIKE '"+usuario+ "%'";
		return this.mDb.query("usuario", new String [] {"_id", "us_nome"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("usuario", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor ObterUsuario (String Usuario) {
		return this.mDb.query("usuario", new String[] {"us_nome"}, "us_nome=?", new String[] {Usuario}, null, null, null);
	}
	
	public Cursor ObterSenha (String Senha) {
		return this.mDb.query("usuario", new String[] {"us_senha"}, "us_senha=?", new String[] {Senha}, null, null, null);
	}
	
	public String Usuario (String ID) {
		Cursor c = this.mDb.query("usuario", new String[] {"us_nome"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("us_nome")); 
	}
	
	public String Senha (String ID) {
		Cursor c = this.mDb.query("usuario", new String[] {"us_senha"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("us_senha")); 
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("usuario", new String[] {"us_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("us_datacad")); 
	}
	
}
