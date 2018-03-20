package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Itens_Vendas {
	
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
	
	public Tabela_Itens_Vendas (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Itens_Vendas open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int ID, int Venda, String Produto, String Quantidade, String Desconto, String PrecoVenda, String Total) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("itven_venda", Venda);
		dados.put("itven_produto", Produto);
		dados.put("itven_qtd", Quantidade);
		dados.put("itven_desc", Desconto);
		dados.put("itven_precovenda", PrecoVenda);
		dados.put("itven_total", Total);
		
		return this.mDb.insert("itens_vendas", null, dados);
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("itens_vendas", "_id = " + IDlinha, null) > 0;
	}
	
	public boolean DeletarVenda (String Venda) {
		return this.mDb.delete("itens_vendas", "itven_venda = " + Venda, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("itens_vendas", new String[] {"_id", "itven_venda", "itven_produto", "itven_qtd", "itven_desc", "itven_precovenda", "itven_datavend"}, 
				null, null, null, null, null);
	}
	
	public Cursor ObterLinha (long IDlinha) throws SQLException {
		Cursor mCursor = 
				
				this.mDb.query(true, "itens_vendas", new String[] {"_id", "itven_venda", "itven_produto", "itven_qtd", "itven_desc", "itven_precovenda", "itven_datavend"}, "_id = " + IDlinha, 
						null, null, null, null, null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public boolean Atualizar (long IDlinha, String Venda, String Produto, String Quantidade, String Desconto, String PrecoVenda, String Total) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("itven_venda", Venda);
		dados.put("itven_produto", Produto);
		dados.put("itven_qtd", Quantidade);
		dados.put("itven_desc", Desconto);
		dados.put("itven_precovenda", PrecoVenda);
		dados.put("itven_total", Total);
		
		return this.mDb.update("itens_vendas", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ID (String Venda) {
		return this.mDb.query("itens_vendas",
				new String[] {"_id"}, 
				"itven_venda=?",
				new String[] {Venda},
				null,
				null,
				null);
	}
	
	public String Produto (String ID) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_produto"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_produto")); 
	}
	
	public String Produto_por_cod_venda (String cod_venda) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_produto"}, "itven_venda=?", new String[] {cod_venda}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_produto")); 
	}
	
	public String Desconto (String ID) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_desc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_desc")); 
	}
	
	public String Valor (String ID) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_precovenda"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_precovenda")); 
	}
	
	public String Quantidade (String ID) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_qtd"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_qtd")); 
	}
	
	public String Total (String ID) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"itven_total"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("itven_total")); 
	}
	
	public int QunatidadeItensVenda (String Venda) {
		Cursor c = this.mDb.query("itens_vendas", new String[] {"_id"}, "itven_venda=?", new String[] {Venda}, null, null, null); 
		c.moveToFirst();
		return c.getCount(); 
	}
	
}
