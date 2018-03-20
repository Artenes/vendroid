package dipro.vendasandroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.BancoDados_Acesso;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Usuario_Atual;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Cadastro_Administrador extends Activity{

	EditText edtAdministrador, edtSenha;
	Button btnFinalizar, btnCancelar;
	Tabela_Administrador registro;
	Tabela_Codigos tblCodigos;
	Tabela_Usuario_Atual tblUsuarioAtual;
	BancoDados_Acesso bdAcesso;
	Cursor cursor;
	Context contesto = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bdAcesso = new BancoDados_Acesso(this);
		bdAcesso.open();
		tblCodigos = new Tabela_Codigos(this);
		tblCodigos.open();
		registro = new Tabela_Administrador(this);
		registro.open();
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		cursor = registro.ObterTodos();
		if (cursor.getCount() > 0) {
			registro.close();
			bdAcesso.close();
			startActivity(new Intent(this,dipro.vendasandroid.SplashScreen.class));
			finish();
		}
		
		setContentView(R.layout.cadastro_administrador);
		edtAdministrador = (EditText) findViewById(R.id.CadastroAdministrador_edtAdministrador);
		edtSenha = (EditText) findViewById(R.id.CadastroAdministrador_edtSenha);
		btnFinalizar = (Button) findViewById(R.id.CadastroAdministrador_btnFinalizar);
		btnCancelar = (Button) findViewById(R.id.CadastroAdministrador_btnCancelar);
		
		
		btnFinalizar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (edtAdministrador.getText().toString().equalsIgnoreCase("") ||
						edtSenha.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Preencha todos os campos");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar Operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar operação", "Deseja cancelar a operação?");
			}
		});
		
		if (tblUsuarioAtual.ObterTodos().getCount() <= 0) {
			tblUsuarioAtual.Inserir(1, 1);
		}
		
	}
	
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}

	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
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
						edtAdministrador.getText().toString(),
						edtSenha.getText().toString(),
						Data);
				tblCodigos.Inserir(Codigo);
				registro.close();
				tblCodigos.close();
				bdAcesso.close();
				startActivity(new Intent(contesto,dipro.vendasandroid.SplashScreen.class));
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblCodigos.close();
		bdAcesso.close();
		cursor.close();
		tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
