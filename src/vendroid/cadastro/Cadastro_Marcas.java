package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Fornecedor;
import vendroid.bancodados.Tabela_Marcas;
import vendroid.consultas.Consulta_Fornecedor_falsa;
import dipro.vendasandroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Cadastro_Marcas extends Activity {
EditText edtDescricao, edtFornecedor;
Button btnSalvar, btnCancelar;
Tabela_Marcas registro;
Tabela_Fornecedor tblFornecedor;
Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_marcas);
		
		tblFornecedor = new Tabela_Fornecedor(this);
		tblCodigos = new Tabela_Codigos(this);
		registro = new Tabela_Marcas(this);
		
		tblFornecedor.open();
		registro.open();
		tblCodigos.open();
		
		AtribuicaObjetos();//Coloca cada ID em seu respectivo objeto
		
		Botoes();//Atribui função aos botões de salvar e cancelar
		
		
	}
	
	public void AtribuicaObjetos () {
		btnSalvar = (Button) findViewById(R.CadastroMarcas.btnSalvar);
		btnCancelar = (Button) findViewById(R.CadastroMarcas.btnCancelar);
		edtDescricao = (EditText) findViewById(R.CadastroMarcas.edtDescricao);
		edtFornecedor = (EditText) findViewById(R.CadastroMarcas.edtFornecedor);
		edtFornecedor.setKeyListener(null);
	}
	
	public void Botoes() {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (edtDescricao.getText().toString().equalsIgnoreCase("") ||
						edtFornecedor.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de marcas?");
			}
		});
		
		edtFornecedor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent consultaFornecedor = new Intent();
				consultaFornecedor.setClass(Cadastro_Marcas.this, Consulta_Fornecedor_falsa.class);
				startActivityForResult(consultaFornecedor, 666);
			}
		});
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Marcas.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				int Codigo = (int) Math.ceil(100000 + Math.random() * 899999);
				Cursor cur = tblCodigos.ObterCodigo(Integer.toString(Codigo));
				
				while (cur.moveToFirst() != false) {
					Codigo = (int) Math.ceil(100000 + Math.random() * 899999);
					cur = tblCodigos.ObterCodigo(Integer.toString(Codigo));
				}

				SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		        String Data = sdfDateTime.format(new Date(System.currentTimeMillis()));
				
				registro.Inserir(
						Integer.toString(Codigo),
						edtDescricao.getText().toString(),
						tblFornecedor.RetornaID(edtFornecedor.getText().toString()),
						Data);
				
				tblCodigos.Inserir(Codigo);
				
				Toast.makeText(getApplicationContext(), "Registro salvo com sucesso", Toast.LENGTH_SHORT).show();
				
				tblCodigos.close();
				registro.close();
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
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Marcas.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				tblCodigos.close();
				registro.close();
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
	
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 666: 
			if (resultCode == RESULT_OK) {
				String fornecedor = data.getStringExtra("FornecedorNome");
				edtFornecedor.setText(fornecedor);
				break;
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		tblCodigos.close();
		registro.close();
		tblFornecedor.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
