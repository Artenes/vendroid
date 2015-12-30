package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Produtos {
	
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
	
	public Tabela_Produtos (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Produtos open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (String Id, String Descricao, String Marca, String Grupo, String CodBarra, String Unidade, String EstoqueAtual,
			String EstoqueMin, String SitTributaria, String ncm, String icms, String PrecoCompra, String PrecoCusto, String Vendavista, String VendaPrazo, String Date) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", Id);
		dados.put("prod_descricao", Descricao);
		dados.put("prod_marca", Marca);
		dados.put("prod_grupo", Grupo);
		dados.put("prod_codbarra", CodBarra);
		dados.put("prod_unidade", Unidade);
		dados.put("prod_estoqueatual", EstoqueAtual);
		dados.put("prod_estoquemin", EstoqueMin);
		dados.put("prod_sittributaria", SitTributaria);
		dados.put("prod_ncm", ncm);
		dados.put("prod_icms", icms);
		dados.put("prod_precocompra", PrecoCompra);
		dados.put("prod_precocusto", PrecoCusto);
		dados.put("prod_vendavista", Vendavista);
		dados.put("prod_vendaprazo", VendaPrazo);
		dados.put("prod_datacad", Date);
		
		return this.mDb.insert("produtos", null, dados);
	}

	public boolean Atualizar (String IDlinha, String Descricao, String Marca, String Grupo, String CodBarra, String Unidade, String EstoqueAtual,
			String EstoqueMin, String SitTributaria, String ncm, String icms, String PrecoCompra, String PrecoCusto, String Vendavista, String VendaPrazo) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("prod_descricao", Descricao);
		dados.put("prod_marca", Marca);
		dados.put("prod_grupo", Grupo);
		dados.put("prod_codbarra", CodBarra);
		dados.put("prod_unidade", Unidade);
		dados.put("prod_estoqueatual", EstoqueAtual);
		dados.put("prod_estoquemin", EstoqueMin);
		dados.put("prod_sittributaria", SitTributaria);
		dados.put("prod_ncm", ncm);
		dados.put("prod_icms", icms);
		dados.put("prod_precocompra", PrecoCompra);
		dados.put("prod_precocusto", PrecoCusto);
		dados.put("prod_vendavista", Vendavista);
		dados.put("prod_vendaprazo", VendaPrazo);
		
		return this.mDb.update("produtos", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("produtos", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("produtos", new String[] {"_id", "prod_descricao", "prod_marca", "prod_grupo", "prod_codbarra", "prod_unidade", "prod_estoqueatual",
				"prod_estoquemin", "prod_sittributaria", "prod_precocompra", "prod_precocusto", "prod_vendavista", "prod_vendaprazo", "prod_datacad"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String descricao, String marca, String grupo) {
		String sql = "_id LIKE '"+ id +"%' AND prod_descricao LIKE '"+descricao+"%' AND prod_marca LIKE '"+marca+"%' AND prod_grupo LIKE '"+grupo+"%'";
		return this.mDb.query("produtos", new String [] {"_id", "prod_descricao", "prod_marca","prod_grupo"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("produtos", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaID_falsa (String ID) {
		return this.mDb.query("produtos", new String[] {"prod_descricao"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscapProduto (String produto) {
		return this.mDb.query("produtos", new String[] {"_id"}, "prod_descricao=?", new String[] {produto}, null, null, null);
	}
		
	public String Descricao (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_descricao"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_descricao"));
	}
	
	public String Descricao_por_Produto (String Produto) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_descricao"}, "prod_descricao=?", new String[] {Produto}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_descricao"));
	}
	
	public String Marca (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_marca"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_marca"));
	}
	
	public String Grupo (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_grupo"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_grupo"));
	}
	
	public String CodigoBarra (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_codbarra"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_codbarra"));
	}
	
	public String Unidade (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_unidade"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_unidade"));
	}
	
	public String EstoqueAtual (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_estoqueatual"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_estoqueatual"));
	}
	
	public String EstoqueMinimo (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_estoquemin"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_estoquemin"));
	}
	
	public String SituacaoTributaria (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_sittributaria"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_sittributaria")); 
	}
	
	public String NCM (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_ncm"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_ncm")); 
	}
	
	public String ICMS (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_icms"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_icms")); 
	}
	
	public String PrecoCompra (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_precocompra"}, "_id=?", new String[] {ID}, null, null, null);   
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_precocompra"));  
	}
	
	public String Precocusto (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_precocusto"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_precocusto"));
	}
	
	public String PrecoVista (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_vendavista"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_vendavista")); 
	}
	
	public String PrecoPrazo (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_vendaprazo"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_vendaprazo"));
	}

	public String Data (String ID) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("prod_datacad")); 
	}
	
	//atencao Este pedaço de código faz com que seja necessário que dois produtos jamais tenham o mesmo nome
	public int EstoqueAtual_PorNome (String Produto) {
		Cursor c = this.mDb.query("produtos", new String[] {"prod_estoqueatual"}, "prod_descricao=?", new String[] {Produto}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("prod_estoqueatual"));
	}
	
	//atencao Este pedaço de código faz com que seja necessário que dois produtos jamais tenham o mesmo nome
	public boolean AtualizarEstoque(int Quantidade, String id_produto) {
		ContentValues dados = new ContentValues();
		dados.put("prod_estoqueatual", Quantidade);
		return this.mDb.update("produtos", dados, "_id=?", new String[] {id_produto}) > 0;
	}
	
	public String Codigo_por_Produto (String Produto) {
		Cursor c = this.mDb.query("produtos", new String[] {"_id"}, "prod_descricao=?", new String[] {Produto}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("_id")); 
	}
	
}
