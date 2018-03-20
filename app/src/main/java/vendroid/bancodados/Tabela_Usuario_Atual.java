package vendroid.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tabela_Usuario_Atual {
	
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
	
	public Tabela_Usuario_Atual (Context ctx) {
		this.mCtx = ctx;
	}
	
	public Tabela_Usuario_Atual open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.mDbHelper.close();
	}
	
	//Inserir dados no Banco de Dados
	public long Inserir (int id, int usuario) {
		ContentValues dados = new ContentValues();
		dados.put("_id", id);
		dados.put("usuario", usuario);
		return this.mDb.insert("usuario_atual", null, dados);
	}
	
	//Deletar uma entrada
	public boolean Deletar (String id) {
		return this.mDb.delete("usuario_atual", "_id = " + id, null) > 0;
	}
	
	//Retorna todos os dados
	public Cursor ObterTodos () {
		return this.mDb.query("usuario_atual", new String[] {"usuario"}, null, null, null, null, null);
	}
	
	public int NivelAcesso (String ID) {
		Cursor c = this.mDb.query("usuario_atual", new String[] {"usuario"}, "_id=?", new String[] {ID}, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("usuario"));
	}
	
	//Para atualizar entradas
	public boolean Atualizar (String id, String usuario) {
		ContentValues dados = new ContentValues();
		dados.put("usuario", usuario);
		return this.mDb.update("usuario_atual", dados, "_id = " + id, null) > 0;
	}
}
