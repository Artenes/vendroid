package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Vendedor {
	
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
	
	public Tabela_Vendedor (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Vendedor open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("vendedor", "_id = " + IDlinha, null) > 0;
	}
	
	public long Inserir (int ID, String Nome, String Senha, String Comissao, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("ven_nome", Nome);
		dados.put("ven_senha", Senha);
		dados.put("ven_comissao", Comissao);
		dados.put("ven_datacad", Data);
		
		return this.mDb.insert("vendedor", null, dados);
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("vendedor", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("vendedor", new String[] {"_id", "ven_nome", "ven_senha", "ven_comissao"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String nome) {
		String sql = "_id LIKE '"+ id +"%' AND ven_nome LIKE '"+nome+"%'";
		return this.mDb.query("vendedor", new String [] {"_id", "ven_nome", "ven_comissao"}, sql, null, null, null, null);
	}
	
	public Cursor ObterNome (String Vendedor) {
		return this.mDb.query("vendedor", new String[] {"ven_nome"}, "ven_nome=?", new String[] {Vendedor}, null, null, null);
	}
	
	public Cursor ObterSenha (String Senha) {
		return this.mDb.query("vendedor", new String[] {"ven_senha"}, "ven_senha=?", new String[] {Senha}, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("vendedor", new String[] {"ven_nome"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaID_falsa (String ID) {
		return this.mDb.query("vendedor", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public String Nome (String ID) {
		Cursor c = this.mDb.query("vendedor", new String[] {"ven_nome"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("ven_nome"));
	}
	
	public String Nome_por_vendedor (String vendedor) {
		Cursor c = this.mDb.query("vendedor", new String[] {"ven_nome"}, "ven_nome=?", new String[] {vendedor}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("ven_nome"));
	}
	
	public String Senha (String ID) {
		Cursor c = this.mDb.query("vendedor", new String[] {"ven_senha"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("ven_senha"));
	}
	
	public String Comissao (String ID) {
		Cursor c = this.mDb.query("vendedor", new String[] {"ven_comissao"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("ven_comissao"));
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("vendedor", new String[] {"ven_datacad"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("ven_datacad"));
	}
	
	public int RetornaID (String Vendedor) {
		Cursor c = this.mDb.query("vendedor", new String[] {"_id"}, "ven_nome=?", new String[] {Vendedor}, null, null, null);
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}
	
	public boolean Atualizar (String IDlinha, String Nome, String Senha, String Comissao) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("ven_nome", Nome);
		dados.put("ven_senha", Senha);
		dados.put("ven_comissao", Comissao);
		
		return this.mDb.update("vendedor", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean VerificarVendedor (String ID) {
		Cursor c = this.mDb.query("vendedor", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
	
}
