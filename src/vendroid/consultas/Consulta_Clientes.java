package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Usuario_Atual;
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

public class Consulta_Clientes extends Activity {

	private static final String campos [] = {"_id","cli_nome","_id"};
	
	private SimpleCursorAdapter dataSource, change;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	EditText edtCodigo, edtCliente;
	Tabela_Cliente helper;
	Tabela_Administrador tblAdministrador;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_clientes);
		
		edtCodigo = (EditText) findViewById(R.ConsultaClientes.edtCodigo);
		edtCliente = (EditText) findViewById(R.ConsultaClientes.edtCliente);
		btnPesquisar = (Button) findViewById(R.ConsultaClientes.btnPesquisar);
		btnCadastrar = (Button) findViewById(R.ConsultaClientes.btnCadastrar);
		btnVoltar = (Button) findViewById(R.ConsultaClientes.btnVoltar);
		listview = (ListView) findViewById(R.ConsultaClientes.list);
		helper = new Tabela_Cliente(this);
		helper.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
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
				TextView aviso = (TextView) dialog.findViewById(R.CaixaDialogo.txvAviso);
				aviso.setText("Deseja continuar?");
				final EditText senha = (EditText) dialog.findViewById(R.CaixaDialogo.edtSenha);
				final Button sim = (Button) dialog.findViewById(R.CaixaDialogo.btnSim);
				Button nao = (Button) dialog.findViewById(R.CaixaDialogo.btnNao);
				
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							startActivity(new Intent(Consulta_Clientes.this,vendroid.cadastro.Cadastro_Cliente.class));
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
				
				if (tblUsuarioAtual.NivelAcesso("1") == 3) {
					dialog.show();
				} else {
					startActivity(new Intent(Consulta_Clientes.this,vendroid.cadastro.Cadastro_Cliente.class));
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
			public void onClick(View arg0) {
				String Codigo = edtCodigo.getText().toString();
				String Cliente = edtCliente.getText().toString();
				dataSource = new SimpleCursorAdapter(contexto, R.layout.consulta_linhas, 
						helper.BuscaFiltro(Codigo, Cliente), campos, new int[] {R.ConsultaLinhas.txvCodigo,
            		R.ConsultaLinhas.txvDescricao});
				listview.setAdapter(dataSource);
				if (helper.BuscaFiltro(Codigo, Cliente).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.ConsultaLinhas.txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String cliente = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Cliente.class);
				 i.putExtra("id_cliente", cliente);
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
        tblAdministrador.close();
        tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}