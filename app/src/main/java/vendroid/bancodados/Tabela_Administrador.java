package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Administrador {
	
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
	
	public Tabela_Administrador (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Administrador open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	//Inserir dados no Banco de Dados
	public long Inserir (String Codigo, String Nome, String Senha, String Data) {
		ContentValues dados = new ContentValues();
		dados.put("_id", Codigo);
		dados.put("admi_nome", Nome);
		dados.put("admi_senha", Senha);
		dados.put("admi_datacad", Data);
		return this.mDb.insert("administrador", null, dados);
	}
	
	//Deletar uma entrada
	public boolean Deletar (long IDlinha) {
		return this.mDb.delete("administrador", "_id = " + IDlinha, null) > 0;
	}
	
	//Retorna todos os dados
	public Cursor ObterTodos () {
		return this.mDb.query("administrador", new String[] {"_id", "admi_nome", "admi_senha", "admi_datacad"}, null, null, null, null, null);
	}
	
	//Para atualizar entradas
	public boolean Atualizar (String IDlinha, String Nome, String Senha) {
		ContentValues dados = new ContentValues();
		dados.put("admi_nome", Nome);
		dados.put("admi_senha", Senha);
		return this.mDb.update("administrador", dados, "_id = " + IDlinha, null) > 0;
	}
	
	public Cursor BuscaID (String Administrador) {
		return this.mDb.query("administrador", new String[] {"_id"}, "_id=?", new String[] {Administrador}, null, null, null);
	}
	
	//Funções que retornam dados específicos
	public Cursor ObterAdministrador (String Administrador) {
		return this.mDb.query("administrador", new String[] {"admi_nome"}, "admi_nome=?", new String[] {Administrador}, null, null, null);
	}
	
	public Cursor ObterLinha (String IDlinha) throws SQLException {
		Cursor mCursor = 
				
				this.mDb.query(true, "administrador", new String[] {"admi_nome"}, "admi_nome = " + IDlinha, 
						null, null, null, null, null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public Cursor ObterSenha (String Senha) {
		return this.mDb.query("administrador", new String[] {"admi_senha"}, "admi_senha=?", new String[] {Senha}, null, null, null);
	}
	
	public Cursor ObterData (String Data) {
		return this.mDb.query("administrador", new String[] {"admi_datacad"}, "admi_datacad=?", new String[] {Data}, null, null, null);
	}
	
}
