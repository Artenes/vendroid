package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Fornecedor {
	
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
	
	public Tabela_Fornecedor (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Fornecedor open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (String ID, int pessoa, String Nome, String CPF, String RG, String CNPJ, String Estadual, String Endereco, String Bairro,
			String Cidade, String UF, String CEP, String Fixo, String Celular, String Email, String Date) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("for_pessoa", pessoa);
		dados.put("_id", ID);
		dados.put("for_nome", Nome);
		dados.put("for_cpf", CPF);
		dados.put("for_rg", RG);
		dados.put("for_cnpj", CNPJ);
		dados.put("for_inscrestadual", Estadual);
		dados.put("for_end", Endereco);
		dados.put("for_bairro", Bairro);
		dados.put("for_cidade", Cidade);
		dados.put("for_uf", UF);
		dados.put("for_cep", CEP);
		dados.put("for_foneum", Fixo);
		dados.put("for_fonedois", Celular);
		dados.put("for_email", Email);
		dados.put("for_datacad", Date);
		
		return this.mDb.insert("fornecedor", null, dados);
	}
	
	public boolean Atualizar (String IDlinha, int pessoa, String Nome, String CPF, String RG, String CNPJ, String Estadual, String Endereco, String Bairro,
			String Cidade, String UF, String CEP, String Fixo, String Celular, String Email) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("for_pessoa", pessoa);
		dados.put("for_nome", Nome);
		dados.put("for_cpf", CPF);
		dados.put("for_rg", RG);
		dados.put("for_cnpj", CNPJ);
		dados.put("for_inscrestadual", Estadual);
		dados.put("for_end", Endereco);
		dados.put("for_bairro", Bairro);
		dados.put("for_cidade", Cidade);
		dados.put("for_uf", UF);
		dados.put("for_cep", CEP);
		dados.put("for_foneum", Fixo);
		dados.put("for_fonedois", Celular);
		dados.put("for_email", Email);
		
		return this.mDb.update("fornecedor", dados, "_id = " + IDlinha, null) > 0;
	}
	
	//Funções úteis para pegar alguns dados da tabela
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("fornecedor", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("fornecedor", new String[] {"_id", "for_nome", "for_cpf", "for_rg", "for_end", "for_bairro",
				"for_cidade", "for_uf", "for_cep", "for_foneum", "for_fonedois", "for_email", "for_datacad"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String nome) {
		String sql = "_id LIKE '"+ id +"%' AND for_nome LIKE '"+nome+"%'";
		return this.mDb.query("fornecedor", new String [] {"_id","for_nome","for_end", "for_bairro", "for_foneum",
				"for_fonedois"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("fornecedor", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaIDfalsa (String ID) {
		return this.mDb.query("fornecedor", new String[] {"for_nome"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public int Pessoa (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_pessoa"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("for_pessoa"));
	}
	
	public String CNPJ (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_cnpj"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_cnpj"));
	}
	
	public String InscricaoEstadual (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_inscrestadual"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_inscrestadual"));
	}
	
	public String Nome (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_nome"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_nome"));
	}
	
	public String CPF (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_cpf"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_cpf"));
	}
	
	public String RG (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_rg"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_rg"));
	}
	
	public String End (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_end"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_end"));
	}
	
	public String Bairro (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_bairro"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_bairro"));
	}
	
	public String Cidade (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_cidade"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_cidade"));
	}
	
	public String Uf (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_uf"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_uf")); 
	}
	
	public String cep (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_cep"}, "_id=?", new String[] {ID}, null, null, null);   
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_cep"));  
	}
	
	
	public String Foneum (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_foneum"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_foneum"));
	}
	
	public String Fonedois (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_fonedois"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_fonedois")); 
	}
	
	public String Email (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_email"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_email"));
	}

	public String Data (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"for_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("for_datacad")); 
	}
	
	public int RetornaID  (String Fornecedor) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"_id"}, "for_nome=?", new String[] {Fornecedor}, null, null, null); 
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("_id")); 
	}
	
	public boolean VerificarFornecedor (String ID) {
		Cursor c = this.mDb.query("fornecedor", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null); 
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
}
