package vendroid.consultas;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Produtos;
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

public class Consulta_Produtos extends Activity {

	private static final String campos [] = {"_id","prod_descricao","prod_marca", "prod_grupo", "_id"};
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigo, edtDescricao, edtMarca, edtGrupo;
	Button btnPesquisar, btnVoltar, btnCadastrar;
	ListView listview;
	Tabela_Produtos helper;
	Context contexto = this;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Administrador tblAdministrador;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_produtos);
		
		btnCadastrar = (Button) findViewById(R.id.ConsultaProdutos_btnCadastrar);
		btnPesquisar = (Button) findViewById(R.id.ConsultaProdutos_btnPesquisar);
		btnVoltar = (Button) findViewById(R.id.ConsultaProdutos_btnVoltar);
		edtCodigo = (EditText) findViewById(R.id.ConsultaProdutos_edtCodigo);
		edtDescricao = (EditText) findViewById(R.id.ConsultaProdutos_edtDescricao);
		edtMarca = (EditText) findViewById(R.id.ConsultaProdutos_edtMarca);
		edtGrupo = (EditText) findViewById(R.id.ConsultaProdutos_edtGrupo);
		listview = (ListView) findViewById(R.id.ConsultaProduto_list);
		helper = new Tabela_Produtos(this);
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
				TextView aviso = (TextView) dialog.findViewById(R.id.CaixaDialogo_txvAviso);
				aviso.setText("Deseja continuar?");
				final EditText senha = (EditText) dialog.findViewById(R.id.CaixaDialogo_edtSenha);
				final Button sim = (Button) dialog.findViewById(R.id.CaixaDialogo_btnSim);
				Button nao = (Button) dialog.findViewById(R.id.CaixaDialogo_btnNao);
				
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							startActivity(new Intent(Consulta_Produtos.this,vendroid.cadastro.Cadastro_Produtos.class));
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
					startActivity(new Intent(Consulta_Produtos.this,vendroid.cadastro.Cadastro_Produtos.class));
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
				String id = edtCodigo.getText().toString();
				String descricao = edtDescricao.getText().toString();
				String marca = edtMarca.getText().toString();
				String grupo = edtGrupo.getText().toString();
				dataSource = new SimpleCursorAdapter(contexto, R.layout.consulta_linhas_produtos, 
						helper.BuscaFiltro(id, descricao, marca, grupo), 
						campos, new int[] {R.id.ConsultaLinhasProdutos_txvCodigo, R.id.ConsultaLinhasProdutos_txvProduto,
					R.id.ConsultaLinhasProdutos_txvMarca, R.id.ConsultaLinhasProdutos_txvGrupo});
	            listview.setAdapter(dataSource);
	            if (helper.BuscaFiltro(id, descricao, marca, grupo).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.id.ConsultaLinhasProdutos_txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String cliente = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Produto.class);
				 i.putExtra("id_produto", cliente);
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