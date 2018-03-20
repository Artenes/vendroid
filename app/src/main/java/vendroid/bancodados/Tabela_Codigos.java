package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Codigos {

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
	
	public Tabela_Codigos (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Codigos open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	//Inserir dados no Banco de Dados
	public long Inserir (int Codigo) {
		ContentValues dados = new ContentValues();
		dados.put("codigo", Codigo);
		return this.mDb.insert("codigos", null, dados);
	}
	
	//Deletar uma entrada
	public boolean Deletar (String Codigo) {
		return this.mDb.delete("codigos", "codigo = " + Codigo, null) > 0;
	}
	
	//Retorna todos os dados
	public Cursor ObterTodos () {
		return this.mDb.query("codigos", new String[] {"codigo"}, null, null, null, null, null);
	}
	
	//Para atualizar entradas
	public boolean Atualizar (String VelhoCodigo, String NovoCodigo) {
		ContentValues dados = new ContentValues();
		dados.put("codigo", NovoCodigo);
		return this.mDb.update("codigos", dados, "codigo = " + VelhoCodigo, null) > 0;
	}
	
	//Funções que retornam dados específicos
	public Cursor ObterCodigo (String Codigo) {
		return this.mDb.query("codigos", new String[] {"codigo"}, "codigo=?", new String[] {Codigo}, null, null, null);
	}
	
}
