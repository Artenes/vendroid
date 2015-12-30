package dipro.vendasandroid;

import vendroid.bancodados.BancoDados_Acesso;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Usuarios;
import vendroid.bancodados.Tabela_Vendedor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Tela_Principal extends Activity {
	
	TextView txvUsuario;
	ImageView Cadastro, Vendas, Consulta, Contas, Sobre, Sair;
	BancoDados_Acesso registro;
	Tabela_Administrador tblAdministrador;
	Tabela_Vendedor tblVendedor;
	Tabela_Usuarios tblUsuarios;
	private int User = 0;
	//1 é usuário
	//2 é administrador
	//3 é vendedor
	static int ANTES_SPLASH = 1;
	static int APOS_SPLASH = 2;
	int estado = ANTES_SPLASH;
	private Cursor Usu, Admi, Vend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_principal);
		
		txvUsuario = (TextView) findViewById(R.TelaPrincipal.txvUsuario);
		Cadastro = (ImageView) findViewById(R.TelaPrincipal.Cadastro);
		Vendas = (ImageView) findViewById(R.TelaPrincipal.Vendas);
		Consulta = (ImageView) findViewById(R.TelaPrincipal.Consulta);
		Contas = (ImageView) findViewById(R.TelaPrincipal.Contas);
		Sobre = (ImageView) findViewById(R.TelaPrincipal.Sobre);
		Sair = (ImageView) findViewById(R.TelaPrincipal.Sair);
		
		registro = new BancoDados_Acesso(this);
		tblAdministrador = new Tabela_Administrador(this);
		tblUsuarios = new Tabela_Usuarios(this);
		tblVendedor = new Tabela_Vendedor(this);
		
		registro.open();
		tblAdministrador.open();
		tblUsuarios.open();
		tblVendedor.open();
		
		Bundle extras = getIntent().getExtras();
		String usuario = extras.getString("UsuarioAtual");
		
		Usu = tblUsuarios.ObterUsuario(usuario);
		Admi = tblAdministrador.ObterAdministrador(usuario);
		Vend = tblVendedor.ObterNome(usuario);
		
		if (Usu.moveToFirst()) {
			txvUsuario.setText(usuario);
			SetUser(1);
		} else if (Admi.moveToFirst()) {
			txvUsuario.setText(usuario);
			SetUser(2);
		} else if (Vend.moveToFirst()) {
			txvUsuario.setText(usuario);
			SetUser(3);
		}
		
		switch (GetUser()) {
		case 1:
			Cadastro.setImageResource(R.drawable.ncadastroblack);
			Cadastro.setEnabled(false);
			Vendas.setImageResource(R.drawable.nvendasblack);
			Vendas.setEnabled(false);
			Contas.setImageResource(R.drawable.ncontasblack);
			Contas.setEnabled(false);
			break;
		case 2:
			break;
		case 3:
			Cadastro.setImageResource(R.drawable.ncadastroblack);
			Cadastro.setEnabled(false);
			break;
		}
		
		Cadastro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Tela_Principal.this,vendroid.cadastro.Cadastro_Lista.class));
			}
		});
		
		Consulta.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Tela_Principal.this,vendroid.consultas.ConsultaLista.class));
			}
		});
		
		Vendas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Tela_Principal.this,vendroid.vendas.Venda.class));
			}
		});
		
		Contas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Tela_Principal.this,vendroid.contas.ContasReceber.class));
			}
		});
		
		Sobre.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Tela_Principal.this,vendroid.sobre.Sobre.class));
			}
		});
		
		Sair.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Encerrar aplicativo", "Deseja encerrar a aplicação?");
			}
		});
	
		
		
}
	
	private void SetUser (int Usuario) {
		User = Usuario;
	}
	
	private int GetUser () {
		return User;
	}
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.close();
				tblAdministrador.close();
				tblUsuarios.close();
				tblVendedor.close();
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

	public int NivelAcesso () {
		return GetUser();
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu){
	    	boolean result = super.onCreateOptionsMenu(menu);
	    	super.onCreateOptionsMenu(menu);
	    	menu.addSubMenu(0, 1, 0, "Atualizar Banco de Dados");
	    	//menu.addSubMenu(0, 2, 0, "Enviar Dados");
	    	/*menu.addSubMenu(0, 3, 0, "Sair").setIcon(R.drawable.ic_launcher);
	    	SubMenu submenu = menu.addSubMenu("Pokémon");
	    	submenu.add(0,4,0,"Blaizikem");
	    	submenu.add(0,5,0,"Swampert");
	    	submenu.add(0,6,0,"Sceptile");*/
	    	return result;
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item){
	    	switch (item.getItemId()){
	    	case 1:
	    		 Intent i = new Intent(getApplicationContext(), dipro.vendasandroid.Atualizar_Banco_Dados.class);
				 startActivity(i);
	    	break;
	    	}
	    	
	    	return super.onOptionsItemSelected(item);
	    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblAdministrador.close();
		tblUsuarios.close();
		tblVendedor.close();
		Usu.close();
		Admi.close();
		Vend.close();
	}
	
	@Override
	public void onBackPressed() {}
	
}
