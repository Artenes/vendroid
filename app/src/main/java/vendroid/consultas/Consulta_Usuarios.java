package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Usuarios;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Consulta_Usuarios extends Activity {

	private static final String campos [] = {"_id","us_nome", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigo, edtUsuario;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	ListView listview;
	Tabela_Usuarios helper;
	Context contexto = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_usuarios);
		
		helper = new Tabela_Usuarios(this);
		helper.open();
		
		btnCadastrar = (Button) findViewById(R.id.ConsultaUsuarios_btnCadastrar);
		edtCodigo = (EditText) findViewById(R.id.ConsultaUsuarios_edtCodigo);
		edtUsuario = (EditText) findViewById(R.id.ConsultaUsuarios_edtUsuario);
		btnPesquisar = (Button) findViewById(R.id.ConsultaUsuarios_btnPesquisar);
		btnVoltar = (Button) findViewById(R.id.ConsultaUsuarios_btnVoltar);
		listview = (ListView) findViewById(R.id.ConsultaUsuarios_list);
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		change = (SimpleCursorAdapter)getLastNonConfigurationInstance();
		
		if (change != null) {
			dataSource = change;
			listview.setAdapter(dataSource);
		}
		
		btnCadastrar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Dialog dialog = new Dialog (contexto);
				dialog.setContentView(R.layout.caixa_dialogo);
				dialog.setTitle("Permissão de administrador");
				TextView aviso = (TextView) dialog.findViewById(R.id.CaixaDialogo_txvAviso);
				aviso.setText("Deseja continuar?");
				final EditText senha = (EditText) dialog.findViewById(R.id.CaixaDialogo_edtSenha);
				final Button sim = (Button) dialog.findViewById(R.id.CaixaDialogo_btnSim);
				Button nao = (Button) dialog.findViewById(R.id.CaixaDialogo_btnNao);
				
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							startActivity(new Intent(Consulta_Usuarios.this,vendroid.cadastro.Cadastro_Usuario.class));
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				nao.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {						
						dialog.dismiss();
					}
				});
				
				if (tblUsuarioAtual.NivelAcesso("1") == 3 || tblUsuarioAtual.NivelAcesso("1") == 2) {
					dialog.show();
				} else {
					startActivity(new Intent(Consulta_Usuarios.this,vendroid.cadastro.Cadastro_Usuario.class));
				}

				
			}
		});
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				helper.close();
				finish();
			}
		});
		 
		btnPesquisar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String id = edtCodigo.getText().toString();
				String usuario = edtUsuario.getText().toString();
				dataSource = new SimpleCursorAdapter(contexto, R.layout.consulta_linhas,
						helper.BuscaFiltro(id, usuario), campos, 
						new int[] {R.id.ConsultaLinhas_txvCodigo, R.id.ConsultaLinhas_txvDescricao});
            listview.setAdapter(dataSource);
            if (helper.BuscaFiltro(id, usuario).getCount() <= 0) {
				Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
			}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.id.ConsultaLinhas_txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String usuario = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Usuario.class);
				 i.putExtra("usuario", usuario);
				 helper.close();
				 startActivity(i);
				 finish();
			}
		});
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return dataSource;
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();        
        helper.close();        

    	tblUsuarioAtual.close();
            tblAdministrador.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
