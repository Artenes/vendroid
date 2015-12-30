package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Rotas;
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

public class Consulta_Rotas extends Activity {

	private static final String campos [] = {"_id","rot_descricao", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastro;
	EditText edtCodigo, edtRota;
	Tabela_Rotas helper;
	Context context = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_rotas);
		
		btnPesquisar = (Button) findViewById(R.ConsultaRotas.btnPesquisar);
		btnVoltar = (Button) findViewById(R.ConsultaRotas.btnVoltar);
		btnCadastro = (Button) findViewById(R.ConsultaRotas.btnCadastro);
		listview = (ListView) findViewById(R.ConsultaRotas.list);
		edtCodigo = (EditText) findViewById(R.ConsultaRotas.edtCodigo);
		edtRota = (EditText) findViewById(R.ConsultaRotas.edtRota);
		helper = new Tabela_Rotas(this);
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
		
		btnCadastro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Dialog dialog = new Dialog (Consulta_Rotas.this);
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
							startActivity(new Intent(Consulta_Rotas.this,vendroid.cadastro.Cadastro_Rotas.class));
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
					startActivity(new Intent(Consulta_Rotas.this,vendroid.cadastro.Cadastro_Rotas.class));
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
				String ID = edtCodigo.getText().toString();
				String Rota = edtRota.getText().toString();
				dataSource = new SimpleCursorAdapter(context, R.layout.consulta_linhas, helper.Busca_Falsa(ID, Rota), campos,
						new int[] {R.ConsultaLinhas.txvCodigo, R.ConsultaLinhas.txvDescricao});
				listview.setAdapter(dataSource);
				if (helper.Busca_Falsa(ID, Rota).getCount() <= 0) {
					Toast.makeText(context, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.ConsultaLinhas.txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String rota = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Rota.class);
				 i.putExtra("id_rota", rota);
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