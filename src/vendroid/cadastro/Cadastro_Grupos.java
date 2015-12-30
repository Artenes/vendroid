package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Grupos;
import dipro.vendasandroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Cadastro_Grupos extends Activity {

EditText edtDescricao; 
Button btnSalvar, btnCancelar;
Tabela_Grupos registro;
Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_grupos);
		registro = new Tabela_Grupos(this);
		tblCodigos = new Tabela_Codigos(this);
		
		registro.open();
		tblCodigos.open();
		
		AtribuicaoObjetos();//Coloca cada ID em seu respectivo objeto
		
		Botoes();//Atribui função aos botões de salvar e cancelar
		
	}
	
	public void AtribuicaoObjetos () {
		btnSalvar = (Button) findViewById(R.CadastroGrupos.btnSalvar);
		btnCancelar = (Button) findViewById(R.CadastroGrupos.btnCancelar);
		edtDescricao = (EditText) findViewById(R.CadastroGrupos.edtDescricao);
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (edtDescricao.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo inválido", "Aqui só tem um campo...");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de grupos?");
			}
		});
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Grupos.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				int Codigo = (int) Math.ceil(100000 + Math.random() * 899999);
				Cursor cur = tblCodigos.ObterCodigo(Integer.toString(Codigo));
				
				while (cur.moveToFirst() != false) {
					Codigo = (int) Math.ceil(100000 + Math.random() * 899999);
					cur = tblCodigos.ObterCodigo(Integer.toString(Codigo));
				}

				SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		        String Data = sdfDateTime.format(new Date(System.currentTimeMillis()));
				
				registro.Inserir(
						Integer.toString(Codigo),
						edtDescricao.getText().toString(),
						Data);
				
				tblCodigos.Inserir(Codigo);
				
				Toast.makeText(getApplicationContext(), "Registro salvo com sucesso", Toast.LENGTH_SHORT).show();
				
				tblCodigos.close();
				registro.close();
				finish();
			}
		});
		Alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Alert.show();
	}
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Grupos.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				tblCodigos.close();
				registro.close();
				finish();
			}
		});
		Alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Alert.show();
	}
	
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}
	
	@Override
		protected void onDestroy() {
			super.onDestroy();
			tblCodigos.close();
			registro.close();
		}
	
	@Override
	public void onBackPressed() {}
	
}
