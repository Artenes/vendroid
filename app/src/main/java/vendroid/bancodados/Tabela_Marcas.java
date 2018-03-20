package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Marcas {
	
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
	
	public Tabela_Marcas (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Marcas open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (String ID, String Descricao, int Fornecedor, String Date) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("mar_descricao", Descricao);
		dados.put("mar_fornecedor", Fornecedor);
		dados.put("mar_datacad", Date);
		
		return this.mDb.insert("marcas", null, dados);
	}
	
	public boolean Atualizar (String IDlinha, String Descricao, int Fornecedor) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("mar_descricao", Descricao);
		dados.put("mar_fornecedor", Fornecedor);
		
		return this.mDb.update("marcas", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("marcas", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("marcas", new String[] {"_id", "mar_descricao", "mar_fornecedor", "mar_datacad"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String marca) {
		String sql = "_id LIKE '"+ id +"%' AND mar_descricao LIKE '"+marca+ "%'";
		return this.mDb.query("marcas", new String [] {"_id", "mar_descricao"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("marcas", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaIDfalsa (String ID) {
		return this.mDb.query("marcas", new String[] {"mar_descricao"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public String Descricao (String ID) {
		Cursor c = this.mDb.query("marcas", new String[] {"mar_descricao"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("mar_descricao"));
	}
	
	public String Fornecedor (String ID) {
		Cursor c = this.mDb.query("marcas", new String[] {"mar_fornecedor"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("mar_fornecedor"));
	}

	public String Data (String ID) {
		Cursor c = this.mDb.query("marcas", new String[] {"mar_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("mar_datacad")); 
	}
	
	public int RetornaID (String Marca) {
		Cursor c = this.mDb.query("marcas", new String[] {"_id"}, "mar_descricao=?", new String[] {Marca}, null, null, null); 
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}

	public boolean VerificarMarca (String Marca) {
		Cursor c = this.mDb.query("marcas", new String[] {"_id"}, "mar_descricao=?", new String[] {Marca}, null, null, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
	
}
