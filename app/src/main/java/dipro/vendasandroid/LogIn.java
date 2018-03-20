package dipro.vendasandroid;

import vendroid.bancodados.BancoDados_Acesso;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Usuarios;
import vendroid.bancodados.Tabela_Vendedor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity{
	
	EditText edtUsuario, edtSenha;
	Button btnEntrar, btnCancelar;
	Context contesto = this;
	BancoDados_Acesso bdAcesso;
	Tabela_Usuarios tblUsuario;
	Tabela_Administrador tblAdministrador;
	Tabela_Vendedor tblVendedor;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Cursor cUsu_nome, cUsu_senha, cAdmi_nome, cAdmi_senha, cVend_nome, cVend_senha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
				
		bdAcesso = new BancoDados_Acesso(this);
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuario = new Tabela_Usuarios(this);
		tblAdministrador = new Tabela_Administrador(this);
		tblVendedor = new Tabela_Vendedor(this);
		bdAcesso.open();
		tblUsuarioAtual.open();
		tblUsuario.open();
		tblAdministrador.open();
		tblVendedor.open();
		
		edtSenha = (EditText) findViewById(R.id.login_edtSenha);
		edtUsuario = (EditText) findViewById(R.id.login_edtUsuario);
		btnCancelar = (Button) findViewById(R.id.login_btnCancelar);
		btnEntrar = (Button) findViewById(R.id.login_btnEntrar);
		
		
		
		btnEntrar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String usuario = edtUsuario.getText().toString();
				String senha = edtSenha.getText().toString();
				cUsu_nome = tblUsuario.ObterUsuario(usuario);
				cUsu_senha = tblUsuario.ObterSenha(senha);
				cAdmi_nome = tblAdministrador.ObterAdministrador(usuario);
				cAdmi_senha = tblAdministrador.ObterSenha(senha);
				cVend_nome = tblVendedor.ObterNome(usuario);
				cVend_senha = tblVendedor.ObterSenha(senha);
				
				if ((cUsu_nome.moveToFirst()) && (cUsu_senha.moveToFirst())) {
					tblUsuarioAtual.Atualizar("1", "3");
					Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
					intent.putExtra("UsuarioAtual", usuario);
					Toast.makeText(getApplicationContext(), "Seja bem-vindo(a) " + usuario, Toast.LENGTH_LONG).show();
					tblUsuario.close();
					tblAdministrador.close();
					tblVendedor.close();
					bdAcesso.close();
					startActivity(intent);
					finish();
				} else if ((cAdmi_nome.moveToFirst()) && (cAdmi_senha.moveToFirst())) {
					tblUsuarioAtual.Atualizar("1", "1");
					Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
					intent.putExtra("UsuarioAtual", usuario);
					Toast.makeText(getApplicationContext(), "Seja bem-vindo(a) " + usuario, Toast.LENGTH_LONG).show();
					tblUsuario.close();
					tblAdministrador.close();
					tblVendedor.close();
					bdAcesso.close();
					startActivity(intent);
					finish();
				} else if ((cVend_nome.moveToFirst()) && (cVend_senha.moveToFirst())) {
					tblUsuarioAtual.Atualizar("1", "2");
					Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
					intent.putExtra("UsuarioAtual", usuario);
					Toast.makeText(getApplicationContext(), "Seja bem-vindo(a) " + usuario, Toast.LENGTH_LONG).show();
					tblUsuario.close();
					tblAdministrador.close();
					tblVendedor.close();
					bdAcesso.close();
					startActivity(intent);
					finish();
				} else {
					MostrarCaixaNeutra("Erro", "Campos Inválidos");
					return;
				}
				
				
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar login", "Sair da aplicação?");
			}
		});
		
	}
	
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}

	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				tblUsuario.close();
				tblAdministrador.close();
				tblVendedor.close();
				bdAcesso.close();
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
		tblUsuarioAtual.close();
		tblUsuario.close();
		tblAdministrador.close();
		tblVendedor.close();
		bdAcesso.close();
	}
	
	@Override
	public void onBackPressed() {}
	
}
