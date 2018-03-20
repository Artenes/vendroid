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

public class Consulta_Rotas_Falsa extends Activity {

	private static final String campos [] = {"_id","rot_descricao", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	EditText edtCodigo, edtRota;
	Tabela_Rotas helper;
	Context context = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_rotas);
		
		btnPesquisar = (Button) findViewById(R.id.ConsultaRotas_btnPesquisar);
		btnVoltar = (Button) findViewById(R.id.ConsultaRotas_btnVoltar);
		btnCadastrar = (Button) findViewById(R.id.ConsultaRotas_btnCadastro);
		listview = (ListView) findViewById(R.id.ConsultaRotas_list);
		edtCodigo = (EditText) findViewById(R.id.ConsultaRotas_edtCodigo);
		edtRota = (EditText) findViewById(R.id.ConsultaRotas_edtRota);
		helper = new Tabela_Rotas(this);
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
				final Dialog dialog = new Dialog (Consulta_Rotas_Falsa.this);
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
							startActivity(new Intent(Consulta_Rotas_Falsa.this,vendroid.cadastro.Cadastro_Rotas.class));
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
					startActivity(new Intent(Consulta_Rotas_Falsa.this,vendroid.cadastro.Cadastro_Rotas.class));
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
				dataSource = new SimpleCursorAdapter(context, R.layout.consulta_linhas, 
						helper.Busca_Falsa(ID, Rota), campos, new int[] {R.id.ConsultaLinhas_txvCodigo,
					R.id.ConsultaLinhas_txvDescricao});
				listview.setAdapter(dataSource);
				if (helper.Busca_Falsa(ID, Rota).getCount() <= 0) {
					Toast.makeText(context, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
				 
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 //C�digo para puxar dados de uma activity para outra
				 final String text = (String) ((TextView)arg1.findViewById(R.id.ConsultaLinhas_txvCodigo)).getText();
				 Cursor cur = helper.Busca(text);
				 cur.moveToFirst();
				 String rota = cur.getString(cur.getColumnIndex("rot_descricao"));
				 Intent returIntent = new Intent();
				 returIntent.putExtra("RotaDescricao", rota);
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