package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Usuarios;
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

public class Cadastro_Usuario extends Activity {
	
	Button btnCancelar, btnSalvar;
	EditText edtNome, edtSenha;
	Tabela_Usuarios registro;
	Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_usuarios);
		
		registro = new Tabela_Usuarios(this);
		registro.open();
		
		tblCodigos = new Tabela_Codigos(this);
		tblCodigos.open();
		
		AtribuicaoObjetos();
		
		Botoes();
		
	}
	
	public void AtribuicaoObjetos() {
		btnCancelar = (Button) findViewById(R.CadastroUsuarios.btnCancelar);
		btnSalvar = (Button) findViewById(R.CadastroUsuarios.btnSalvar);
		edtNome = (EditText) findViewById(R.CadastroUsuarios.edtNome);
		edtSenha = (EditText) findViewById(R.CadastroUsuarios.edtSenha);		
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (edtNome.getText().toString().equalsIgnoreCase("") ||
						edtSenha.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de usuários?");
			}
		});
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Usuario.this);
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
						Codigo,
						edtNome.getText().toString(),
						edtSenha.getText().toString(),
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Usuario.this);
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
