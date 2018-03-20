package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Fornecedor;
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

public class Consulta_Fornecedor_falsa extends Activity {

	private SimpleCursorAdapter dataSource, change;
	
	private static final String campos [] = {"_id","for_nome","_id"};
	
	EditText edtFornecedor, edtCodigo;
	ListView listview;
	Button btnVoltar, btnPesquisar, btnCadastrar;
	Tabela_Fornecedor helper;
	Context contesto = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_fornecedor);
		
		
		btnCadastrar = (Button) findViewById(R.id.ConsultaFornecedor_btnCadastrar);
		edtCodigo = (EditText) findViewById(R.id.ConsultaFornecedor_edtCodigo);
		edtFornecedor = (EditText) findViewById(R.id.ConsultaFornecedor_edtFornecedor);
		btnPesquisar = (Button) findViewById(R.id.ConsultaFornecedor_btnPesquisar);
		btnVoltar = (Button) findViewById(R.id.ConsultaFornecedor_btnVoltar);
		listview = (ListView) findViewById(R.id.ConsultaFornecedor_list);
		helper = new Tabela_Fornecedor(this);
		
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
				final Dialog dialog = new Dialog (Consulta_Fornecedor_falsa.this);
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
							startActivity(new Intent(Consulta_Fornecedor_falsa.this,vendroid.cadastro.Cadastro_Fornecedor.class));
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
					startActivity(new Intent(Consulta_Fornecedor_falsa.this,vendroid.cadastro.Cadastro_Fornecedor.class));
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
				String Fornecedor = edtFornecedor.getText().toString();
				dataSource = new SimpleCursorAdapter(contesto, R.layout.consulta_linhas, helper.BuscaFiltro(ID,Fornecedor),
						campos, new int[] {R.id.ConsultaLinhas_txvCodigo,
					R.id.ConsultaLinhas_txvDescricao});
            listview.setAdapter(dataSource);
            if (helper.BuscaFiltro(ID, Fornecedor).getCount() <= 0) {
				Toast.makeText(contesto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
			}
			}
		});
		
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.id.ConsultaLinhas_txvCodigo)).getText();
				 Cursor cur = helper.BuscaIDfalsa(text);
				 cur.moveToFirst();
				 String Fornecedor = cur.getString(cur.getColumnIndex("for_nome"));
				 Intent returIntent = new Intent();
				 returIntent.putExtra("FornecedorNome", Fornecedor);
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

