package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Vendedor;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Consulta_Vendedor_falsa extends Activity {

	private static final String campos [] = {"_id","ven_nome", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigo, edtVendedor;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	Tabela_Vendedor helper;
	Context contexto = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_vendedor);
		
		edtCodigo = (EditText) findViewById(R.ConsultaVendedores.edtCodigo);
		edtVendedor = (EditText) findViewById(R.ConsultaVendedores.edtVendedor);
		btnVoltar = (Button) findViewById(R.ConsultaVendedores.btnVoltar);
		btnPesquisar = (Button) findViewById(R.ConsultaVendedores.btnPesquisar);
		btnCadastrar = (Button) findViewById(R.ConsultaVendedores.btnCadastro);
		listview = (ListView) findViewById(R.ConsultaVendedores.list);
		helper = new Tabela_Vendedor(this);
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
							startActivity(new Intent(Consulta_Vendedor_falsa.this,vendroid.cadastro.Cadastro_Vendedor.class));
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
					startActivity(new Intent(Consulta_Vendedor_falsa.this,vendroid.cadastro.Cadastro_Vendedor.class));
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
				String ID = edtCodigo.getText().toString();
				String Vendedor = edtVendedor.getText().toString();
				dataSource = new SimpleCursorAdapter(contexto, R.layout.consulta_linhas, helper.BuscaFiltro(ID, Vendedor), 
						campos, new int[] {R.ConsultaLinhas.txvCodigo, R.ConsultaLinhas.txvDescricao});
				listview.setAdapter(dataSource);
				if (helper.BuscaFiltro(ID, Vendedor).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.ConsultaLinhas.txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String vendedor = cur.getString(cur.getColumnIndex("ven_nome"));
				 Intent returIntent = new Intent();
				 returIntent.putExtra("vendedor", vendedor);
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
        tblUsuarioAtual.close();
        tblAdministrador.close();
	}
	
	@Override
	public void onBackPressed() {}
	
}