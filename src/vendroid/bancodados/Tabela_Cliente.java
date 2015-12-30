package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Cliente {
	
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
	
	public Tabela_Cliente (Context ctx) {
		this.mCtx = ctx;
	}
	
	public SQLiteDatabase Return_Writable () {
		return this.mDbHelper.getWritableDatabase();
	}
	
	public Tabela_Cliente open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("clientes", "_id = " + IDlinha, null) > 0;
	}
	
	public long Inserir (int Pessoa, String ID, String Nome, String CPF, String RG, String CNPJ, String Estadual, String Nascimento, String Endereco, String Bairro,
			String Cidade, String UF, String CEP, String Fixo, String Celular, String Email, int Rota, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("cli_pessoa", Pessoa);
		dados.put("cli_nome", Nome);
		dados.put("cli_cpf", CPF);
		dados.put("cli_rg", RG);
		dados.put("cli_cnpj", CNPJ);
		dados.put("cli_inscrestadual", Estadual);
		dados.put("cli_dn", Nascimento);
		dados.put("cli_end", Endereco);
		dados.put("cli_bairro", Bairro);
		dados.put("cli_cidade", Cidade);
		dados.put("cli_uf", UF);
		dados.put("cli_cep", CEP);
		dados.put("cli_foneum", Fixo);
		dados.put("cli_fonedois", Celular);
		dados.put("cli_email", Email);
		dados.put("cli_rota", Rota);
		dados.put("cli_datacad", Data);
		
		return this.mDb.insert("clientes", null, dados);
	}
	
	public boolean Atualizar (String IDlinha, int Pessoa, String Nome, String CPF, String RG, String CNPJ, String Estadual, String Nascimento, String Endereco, String Bairro,
			String Cidade, String UF, String CEP, String Fixo, String Celular, String Email, int Rota) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("cli_pessoa", Pessoa);
		dados.put("cli_nome", Nome);
		dados.put("cli_cpf", CPF);
		dados.put("cli_rg", RG);
		dados.put("cli_cnpj", CNPJ);
		dados.put("cli_inscrestadual", Estadual);
		dados.put("cli_dn", Nascimento);
		dados.put("cli_end", Endereco);
		dados.put("cli_bairro", Bairro);
		dados.put("cli_cidade", Cidade);
		dados.put("cli_uf", UF);
		dados.put("cli_cep", CEP);
		dados.put("cli_foneum", Fixo);
		dados.put("cli_fonedois", Celular);
		dados.put("cli_email", Email);
		dados.put("cli_rota", Rota);
		
		return this.mDb.update("clientes", dados, "_id = " + IDlinha, null) > 0;
	}
	
	//A partir daqui começas as funções úteis para brincar com o bando de dados
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("clientes", "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor ObterTodos () {
		return this.mDb.query("clientes", new String[] {"_id", "cli_nome", "cli_cpf", "cli_rg", "cli_dn", "cli_end", "cli_bairro",
				"cli_cidade", "cli_uf", "cli_cep", "cli_foneum", "cli_fonedois", "cli_email", "cli_rota", "cli_datacad"}, null, null, null, null, null);
	}
	
	public Cursor BuscaFiltro (String id, String cliente) {
		String sql = "_id LIKE '"+ id +"%' AND cli_nome LIKE '"+cliente+"%'";
		return this.mDb.query("clientes", new String [] {"_id", "cli_nome", "cli_foneum","cli_fonedois"}, sql, null, null, null, null);
	}
	
	public Cursor BuscaID (String ID) {
		return this.mDb.query("clientes", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public Cursor BuscaID_falsa (String ID) {
		return this.mDb.query("clientes", new String[] {"cli_nome"}, "_id=?", new String[] {ID}, null, null, null);
	}
	
	public int Pessoa (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_pessoa"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("cli_pessoa"));
	}
	
	public String Nome (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_nome"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_nome"));
	}
	
	public String Nome_por_Cliente (String Cliente) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_nome"}, "cli_nome=?", new String[] {Cliente}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_nome"));
	}
	
	public String CPF (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_cpf"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_cpf"));
	}
	
	public String RG (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_rg"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_rg"));
	}
	
	public String DN (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_dn"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_dn"));
	}
	
	public String End (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_end"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_end"));
	}
	
	public String Bairro (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_bairro"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_bairro"));
	}
	
	public String Cidade (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_cidade"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_cidade"));
	}
	
	public String Uf (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_uf"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_uf")); 
	}
	
	public String cep (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_cep"}, "_id=?", new String[] {ID}, null, null, null);   
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_cep"));  
	}
	
	
	public String Foneum (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_foneum"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_foneum"));
	}
	
	public String Fonedois (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_fonedois"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_fonedois")); 
	}
	
	public String Email (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_email"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_email"));
	}
	
	public String Rota (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_rota"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_rota"));
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_datacad")); 
	}
	
	public String CNPJ (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_cnpj"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_cnpj")); 
	}
	
	public String InscricaoEstadual (String ID) {
		Cursor c = this.mDb.query("clientes", new String[] {"cli_inscrestadual"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("cli_inscrestadual")); 
	}
	
	public String RetornaID (String Cliente) {
		Cursor c = this.mDb.query("clientes", new String[] {"_id"}, "cli_nome=?", new String[] {Cliente}, null, null, null);
		if (c.moveToFirst()) {
			return c.getString(c.getColumnIndex("_id"));
		} else {
			return "A Vista";
		}
	}
	
}
