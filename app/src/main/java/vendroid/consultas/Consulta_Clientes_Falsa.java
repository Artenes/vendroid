package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.editar.Editar_Cliente;
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

public class Consulta_Clientes_Falsa extends Activity {

	private static final String campos [] = {"_id","cli_nome","_id"};
	
	private SimpleCursorAdapter dataSource, change;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	EditText edtCodigo, edtCliente;
	Tabela_Cliente helper;
	Tabela_Administrador tblAdministrador;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	Editar_Cliente edt;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_clientes);
		
		edt = new Editar_Cliente();
		
		btnCadastrar = (Button) findViewById(R.id.ConsultaClientes_btnCadastrar);
		edtCodigo = (EditText) findViewById(R.id.ConsultaClientes_edtCodigo);
		edtCliente = (EditText) findViewById(R.id.ConsultaClientes_edtCliente);
		btnPesquisar = (Button) findViewById(R.id.ConsultaClientes_btnPesquisar);
		btnVoltar = (Button) findViewById(R.id.ConsultaClientes_btnVoltar);
		listview = (ListView) findViewById(R.id.ConsultaClientes_list);
		
		helper = new Tabela_Cliente(this);
		helper.open();
		
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
				dialog.setTitle("Permiss�o de administrador");
				TextView aviso = (TextView) dialog.findViewById(R.id.CaixaDialogo_txvAviso);
				aviso.setText("Deseja continuar?");
				final EditText senha = (EditText) dialog.findViewById(R.id.CaixaDialogo_edtSenha);
				final Button sim = (Button) dialog.findViewById(R.id.CaixaDialogo_btnSim);
				Button nao = (Button) dialog.findViewById(R.id.CaixaDialogo_btnNao);
				
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							startActivity(new Intent(Consulta_Clientes_Falsa.this,vendroid.cadastro.Cadastro_Cliente.class));
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
					startActivity(new Intent(Consulta_Clientes_Falsa.this,vendroid.cadastro.Cadastro_Cliente.class));
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
						helper.BuscaFiltro(Codigo, Cliente), campos, new int[] {R.id.ConsultaLinhas_txvCodigo,
            		R.id.ConsultaLinhas_txvDescricao});
				listview.setAdapter(dataSource);
				if (helper.BuscaFiltro(Codigo, Cliente).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.id.ConsultaLinhas_txvCodigo)).getText();
				 Cursor cur = helper.BuscaID_falsa(text);
				 cur.moveToFirst();
				 String cliente = cur.getString(cur.getColumnIndex("cli_nome"));
				 Intent returIntent = new Intent();
				 returIntent.putExtra("cliente", cliente);
				 setResult(RESULT_OK, returIntent);
				 helper.close();
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
