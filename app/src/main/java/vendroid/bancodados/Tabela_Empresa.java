package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Empresa {
	
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
	
	public Tabela_Empresa (Context ctx) {
		this.mCtx = ctx;
	}
	
	public SQLiteDatabase Return_Writable () {
		return this.mDbHelper.getWritableDatabase();
	}
	
	public Tabela_Empresa open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	public long Inserir (int ID, String RazaoSocial, String NomeFantasia, String CNPJ, String InscricaoEstadual,
			String Endereco, String Bairro, String Cidade, String UF, String CEP, String Representante, String FoneUm,
			String FoneDois, String Email, String Site, String Data) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("emp_razaosocial", RazaoSocial);
		dados.put("emp_nomefantasia", NomeFantasia);
		dados.put("emp_cnpj", CNPJ);
		dados.put("emp_inscrestadual", InscricaoEstadual);
		dados.put("emp_end", Endereco);
		dados.put("emp_bairro", Bairro);
		dados.put("emp_cidade", Cidade);
		dados.put("emp_uf", UF);
		dados.put("emp_cep", CEP);
		dados.put("emp_representante", Representante);
		dados.put("emp_foneum", FoneUm);
		dados.put("emp_fonedois", FoneDois);
		dados.put("emp_email", Email);
		dados.put("emp_site", Site);
		dados.put("emp_datacad", Data);
		
		return this.mDb.insert("empresa", null, dados);
	}
	
	//A partir daqui começas as funções úteis para brincar com o bando de dados
	public boolean Deletar (String IDlinha) {
		return this.mDb.delete("empresa", "_id = " + IDlinha, null) > 0;
	}
	
	public boolean ObterTodos () {
		Cursor c = this.mDb.query("empresa", new String[] {"_id"}, "_id=?", new String[] {"111111"}, null, null, null);
		if (c.moveToFirst()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Atualizar (String ID, String RazaoSocial, String NomeFantasia, String CNPJ, String InscricaoEstadual,
			String Endereco, String Bairro,String Cidade, String UF, String CEP, String Representante, String FoneUm,
			String FoneDois, String Email, String Site) {
		
		ContentValues dados = new ContentValues();
		
		dados.put("_id", ID);
		dados.put("emp_razaosocial", RazaoSocial);
		dados.put("emp_nomefantasia", NomeFantasia);
		dados.put("emp_cnpj", CNPJ);
		dados.put("emp_inscrestadual", InscricaoEstadual);
		dados.put("emp_end", Endereco);
		dados.put("emp_bairro", Bairro);
		dados.put("emp_cidade", Cidade);
		dados.put("emp_uf", UF);
		dados.put("emp_cep", CEP);
		dados.put("emp_representante", Representante);
		dados.put("emp_foneum", FoneUm);
		dados.put("emp_fonedois", FoneDois);
		dados.put("emp_email", Email);
		dados.put("emp_site", Site);
		
		return this.mDb.update("empresa", dados, "_id = " + ID, null) > 0;
	}

	public String ID (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"_id"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getString(c.getColumnIndex("_id"));
	}

	public String RazaoSocial (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_razaosocial"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_razaosocial")); 
	}
	
	public String NomeFantasia (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_nomefantasia"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_nomefantasia")); 
	}
	
	public String CNPJ (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_cnpj"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_cnpj")); 
	}
	
	public String InscricaoEstadual (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_inscrestadual"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_inscrestadual")); 
	}
	
	public String Endereco (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_end"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_end")); 
	}
	
	public String Bairro (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_bairro"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_bairro")); 
	}
	
	public String Cidade (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_cidade"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_cidade")); 
	}
	
	public String UF (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_uf"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_uf")); 
	}
	
	public String CEP (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_cep"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_cep")); 
	}
	
	public String Representante (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_representante"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_representante")); 
	}
	
	public String FoneUm (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_foneum"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_foneum")); 
	}
	
	public String FoneDois (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_fonedois"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_fonedois")); 
	}
	
	public String Email (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_email"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_email")); 
	}
	
	public String Site (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_site"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_site")); 
	}
	
	public String Data (String ID) {
		Cursor c = this.mDb.query("empresa", new String[] {"emp_datacad"}, "_id=?", new String[] {ID}, null, null, null); 
		c.moveToFirst();
		return c.getString(c.getColumnIndex("emp_datacad")); 
	}
	
}
