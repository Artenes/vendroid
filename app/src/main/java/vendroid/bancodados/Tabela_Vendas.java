package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Vendas {
	
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
	
	public Tabela_Vendas (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Vendas open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int Codigo, String Vendedor, String Cliente, String FormaPagamento, String PlanoPagamento, String ValorBruto, String TotalLiquido, String TotalLiquidoDesconto, String Desconto, String Acrescimo,
			String TotalVenda, String Data, String Data_BR) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", Codigo);
		dados.put("vend_vendedor", Vendedor);
		dados.put("vend_cliente", Cliente);
		dados.put("vend_formapag", FormaPagamento);
		dados.put("vend_planopag", PlanoPagamento);
		dados.put("vend_valorbruto", ValorBruto);
		dados.put("vend_totalliquido", TotalLiquido);
		dados.put("vend_totalliquidodesc", TotalLiquidoDesconto);
		dados.put("vend_desconto", Desconto);
		dados.put("vend_acresc", Acrescimo);
		dados.put("vend_totalvenda", TotalVenda);
		dados.put("vend_data_br", Data_BR);
		dados.put("vend_data", Data);
		
		return this.mDb.insert("vendas", null, dados);
	}
	
	public boolean Atualizar (int ID, String Vendedor, String Cliente, String FormaPagamento, String PlanoPagamento, String ValorBruto, String TotalLiquido, String TotalLiquidoDesconto, String Desconto, String Acrescimo,
			String TotalVenda, String Data, String Data_BR) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("vend_vendedor", Vendedor);
		dados.put("vend_cliente", Cliente);
		dados.put("vend_formapag", FormaPagamento);
		dados.put("vend_planopag", PlanoPagamento);
		dados.put("vend_valorbruto", ValorBruto);
		dados.put("vend_totalliquido", TotalLiquido);
		dados.put("vend_totalliquidodesc", TotalLiquidoDesconto);
		dados.put("vend_desconto", Desconto);
		dados.put("vend_acresc", Acrescimo);
		dados.put("vend_totalvenda", TotalVenda);
		dados.put("vend_data_br", Data_BR);
		dados.put("vend_data", Data);
		
		return this.mDb.update("vendas", dados, "_id = " + ID, null) > 0;
		
	}
	
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("vendas", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("vendas", new String[] {"_id", "vend_vendedor", "vend_cliente", "vend_formapag", "vend_desconto", "vend_acresc", "vend_totalvenda"}, 
				null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String codigo, String vendedor, String cliente, String datainicial, String datafinal) {
		String sql = "_id LIKE '"+ codigo +"%' AND vend_cliente LIKE '"+cliente+"%' AND vend_vendedor LIKE '"+vendedor+"%'" +
				" AND (vend_data >= '"+datainicial+"' AND vend_data <= '"+datafinal+"')";
		return this.mDb.query("vendas", new String [] {"_id", "vend_vendedor", "vend_cliente","vend_data_br"}, sql, null, null, null, null);
	}
	
	public Cursor Puta () {
		return this.mDb.query(
				"vendas", 
				new String [] {"_id", "vend_vendedor", "vend_cliente","vend_data"}, 
				null, 
				null, 
				null, 
				null, 
				null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("vendas", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public String Vendedor (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_vendedor"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_vendedor")); 
	}
	
	public String Cliente (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_cliente"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_cliente")); 
	}
	
	public String FormaPagamento (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_formapag"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_formapag")); 
	}
	
	public String PlanoPagamento (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_planopag"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_planopag")); 
	}
	
	public String ValorBruto (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_valorbruto"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_valorbruto")); 
	}
	
	public String TotalLiquido (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_totalliquido"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_totalliquido")); 
	}
	
	public String TotalLiquidoDesconto (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_totalliquidodesc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_totalliquidodesc")); 
	}
	
	public String TotalLiquidoDescontoAcressimo (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_totalliquidodescacresc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_totalliquidodescacresc")); 
	}
	
	public String Desconto (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_desconto"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_desconto")); 
	}
	
	public String Acressimo (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_acresc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_acresc")); 
	}
	
	public String TotalVenda (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_totalvenda"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_totalvenda")); 
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_data"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_data")); 
	}
	
	public String DataBR (String ID) {
		Cursor c = this.mDb.query("vendas", new String[] {"vend_data_br"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("vend_data_br")); 
	}
	
	
}
