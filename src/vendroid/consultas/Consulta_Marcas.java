package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Marcas;
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

public class Consulta_Marcas extends Activity {

	private static final String campos [] = {"_id","mar_descricao", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigo, edtDescricao, edtFornecedor;
	Button btnPesquisar, btnVoltar, btnCadastrar;
	ListView listview;
	Tabela_Marcas helper;
	Context contexto = this;
	
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_marcas);
		
		btnCadastrar = (Button) findViewById(R.ConsultaMarcas.btnCadastrar);
		edtCodigo = (EditText) findViewById(R.ConsultaMarcas.edtCodigo);
		edtDescricao = (EditText) findViewById(R.ConsultaMarcas.edtDescricao);
		edtFornecedor = (EditText) findViewById(R.ConsultaMarcas.edtFornecedor);
		btnPesquisar = (Button) findViewById(R.ConsultaMarcas.btnPesquisar);
		btnVoltar = (Button) findViewById(R.ConsultaMarcas.btnVoltar);
		listview = (ListView) findViewById(R.ConsultaMarcas.list);
		helper = new Tabela_Marcas(this);
		helper.open();
		
		change = (SimpleCursorAdapter)getLastNonConfigurationInstance();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
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
							startActivity(new Intent(Consulta_Marcas.this,vendroid.cadastro.Cadastro_Marcas.class));
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
					startActivity(new Intent(Consulta_Marcas.this,vendroid.cadastro.Cadastro_Marcas.class));
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
				String descricao = edtDescricao.getText().toString();
				dataSource = new SimpleCursorAdapter(contexto, R.layout.consulta_linhas,
						helper.BuscaFiltro(id, descricao), campos, 
						new int[] {R.ConsultaLinhas.txvCodigo, R.ConsultaLinhas.txvDescricao});
            listview.setAdapter(dataSource);
            if (helper.BuscaFiltro(id, descricao).getCount() <= 0) {
				Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
			}
			}
		});
		
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.ConsultaLinhas.txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String marca = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Marca.class);
				 i.putExtra("id_marca", marca);
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