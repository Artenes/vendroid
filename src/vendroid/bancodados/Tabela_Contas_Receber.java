package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Contas_Receber {
	
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
	
	public Tabela_Contas_Receber (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Contas_Receber open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int id, String Cliente, String Venda, int FormaPagamento, int DataVencimento, String ValorTotal,
			String NumeroParcela, String ValorParcela, String Data_BR, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", id);
		dados.put("cr_cliente", Cliente);
		dados.put("cr_venda", Venda);
		dados.put("cr_formapag", FormaPagamento);
		dados.put("cr_datavenc", DataVencimento);
		dados.put("cr_valortotal", ValorTotal);
		dados.put("cr_numparcela", NumeroParcela);
		dados.put("cr_valorparc", ValorParcela);
		dados.put("cr_data_br", Data_BR);
		dados.put("cr_data", Data);
		
		return this.mDb.insert("contas_receber", null, dados);
	}
	
	public boolean Atualizar (int IDlinha, String Cliente, String Venda, int FormaPagamento, int DataVencimento, String ValorTotal,
			String NumeroParcela, String ValorParcela, String Data_BR, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("cr_cliente", Cliente);
		dados.put("cr_venda", Venda);
		dados.put("cr_formapag", FormaPagamento);
		dados.put("cr_datavenc", DataVencimento);
		dados.put("cr_valortotal", ValorTotal);
		dados.put("cr_numparcela", NumeroParcela);
		dados.put("cr_valorparc", ValorParcela);
		dados.put("cr_data_br", Data_BR);
		dados.put("cr_data", Data);
		
		return this.mDb.update("contas_receber", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean AtualizarDataVencimento (String IDlinha, String DataVencimento) {
		ContentValues dados = new ContentValues();
		dados.put("cr_datavenc", DataVencimento);
		return this.mDb.update("contas_receber", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean AtualizarValorParcela (String IDlinha, Float Parcela) {
		ContentValues dados = new ContentValues();
		dados.put("cr_valorparc", Parcela);
		return this.mDb.update("contas_receber", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("contas_receber", "_id = " + IDlinha, null) > 0;
	}
	
	public boolean Deletar_Todos (String IDlinha) {
		return this.mDb.delete("contas_receber", "cr_venda =" + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("contas_receber", new String[] {"_id", "cr_cliente", "cr_venda", "cr_formapag", "cr_datavenc", "cr_valortotal", "cr_numparcela", "cr_valorparc"}, 
				null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String venda, String cliente) {
		String sql = "cr_venda LIKE '"+ venda +"%' AND cr_cliente LIKE '"+cliente+"%'";
		return this.mDb.query(
				"contas_receber",
				new String [] {"cr_venda", "cr_cliente", "cr_numparcela", "_id"},
				sql,
				null, null, null,
				"cr_numparcela");
	}
	
	public Cursor InfoConta (String ID) {
		return this.mDb.query(
				"contas_receber",
				new String[] {"_id", "cr_numparcela", "cr_valorparc", "cr_datavenc"},
				"cr_venda=?",
				new String[] {ID},
				null, null,
				"cr_numparcela"
				);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("contas_receber", new String[] {"cr_venda"}, "cr_venda=?", new String[] {ID}, null, null, null);
	}
	
	//Retorna código da conta a receber
	public int ID_conta_receber (String cod_venda) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"_id"}, "cr_venda=?", new String[] {cod_venda}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("_id")); 
	}
	
	//Retorna código do cliente
	public String Cliente (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_cliente"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_cliente")); 
	}
	
	//Retorna código da venda
	public int Venda (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_venda"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("cr_venda")); 
	}
	
	//Retorna código da forma de pagamento
	public int FormaPagamento (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_formapag"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("cr_formapag")); 
	}
	
	//Retorna a data
	public String Data (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_data"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_data")); 
	}
	
	//Retorna a data
	public String DataVencimento (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_datavenc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_datavenc")); 
	}
	
	//Retorna a data
	public String ValorParcela (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_valorparc"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_valorparc")); 
	}
	
	//Retorna a data
	public String ValorTotal (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_valortotal"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_valortotal")); 
	}
	
	public String NumeroParcela (String ID) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"cr_numparcela"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cr_numparcela")); 
	}
	
	public int RetornaID (String Venda) {
		Cursor c = this.mDb.query("contas_receber", new String[] {"_id"}, "cr_venda=?", new String[] {Venda}, null, null, null);
		if (c.moveToFirst()) {
			return c.getInt(c.getColumnIndex("_id"));
		} else {
			return 0;
		}
	}
	
	public Cursor RetornaTodosCodigos (String Venda) {
		return this.mDb.query(
				"contas_receber",
				new String[] {"_id", "cr_qtdparcelas"},
				"cr_venda=?",
				new String[] {Venda},
				null,
				null,
				"cr_qtdparcelas"
				);
	}
	
}
