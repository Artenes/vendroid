package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Forma_Pagamento {
	
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
	
	public Tabela_Forma_Pagamento (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Forma_Pagamento open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (String ID, String Descricao, String QtdParcela, String Diasentreparcela,
			String Acrescimo, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("forpg_descricao", Descricao);
		dados.put("forpg_qtdparcela", QtdParcela);
		dados.put("forpg_diasentreparcela", Diasentreparcela);
		dados.put("forpg_acresc", Acrescimo);
		dados.put("forpg_datacad", Data);
		
		return this.mDb.insert("forma_pag", null, dados);
	}
	
	public boolean Atualizar (String IDlinha, String Descricao, String QtdParcela, String Diasentreparcela,String Acrescimo) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("forpg_descricao", Descricao);
		dados.put("forpg_qtdparcela", QtdParcela);
		dados.put("forpg_diasentreparcela", Diasentreparcela);
		dados.put("forpg_acresc", Acrescimo);
		
		return this.mDb.update("forma_pag", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("forma_pag", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("forma_pag", new String[] {"_id", "forpg_descricao", "forpg_diasentr", "forpg_acresc",
				"forpg_datacad"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String descricao) {
		String sql = "_id LIKE '"+ id +"%' AND forpg_descricao LIKE '"+descricao+"%'";
		return this.mDb.query("forma_pag", new String [] {"_id", "forpg_descricao"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("forma_pag", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaID_falsa (String ID) {
		return this.mDb.query("forma_pag", new String[] {"forpg_descricao"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public String Descricao (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_descricao"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_descricao"));
	}
	
	public String Descricao_por_FormaPagamento (String FormaPagamento) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_descricao"}, "forpg_descricao=?", new String[] {FormaPagamento}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_descricao"));
	}
	
	public int QuantidadeParcelas (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_qtdparcela"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("forpg_qtdparcela"));
	}
	
	public String DiasParcelas (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_diasentreparcela"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_diasentreparcela"));
	}
	
	public String Acressimo (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_acresc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_acresc"));
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_datacad")); 
	}
	
	public String AcressimoID (String ID) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"forpg_acresc"}, "forpg_descricao=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("forpg_acresc"));
	}
		
	public int RetornaID (String FormaPagamento) {
		Cursor c = this.mDb.query("forma_pag", new String[] {"_id"}, "forpg_descricao=?", new String[] {FormaPagamento}, null, null, null);
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}
	
}
