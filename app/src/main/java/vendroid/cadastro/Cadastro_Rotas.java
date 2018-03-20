package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Rotas;
import vendroid.bancodados.Tabela_Vendedor;
import vendroid.consultas.Consulta_Vendedor_falsa;

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


public class Cadastro_Rotas extends Activity {

	EditText edtDescricao, edtVendedor;
	Button btnSalvar, btnCancelar;
	Tabela_Rotas registro;
	Tabela_Codigos tblCodigos;
	Tabela_Vendedor tblVendedor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_rotas);
		registro = new Tabela_Rotas(this);
		registro.open();
		
		tblCodigos = new Tabela_Codigos(this);
		tblCodigos.open();
		
		tblVendedor = new Tabela_Vendedor(this);
		tblVendedor.open();
		
		AtribuicaoObjetos();
		
		Botoes();
		
		edtVendedor.setKeyListener(null);
		
	}
	
	public void AtribuicaoObjetos () {
		edtDescricao = (EditText) findViewById(R.id.CadastroRotas_EdtDescricao);
		edtVendedor = (EditText) findViewById(R.id.CadastroRotas_EdtVendedor);
		btnCancelar = (Button) findViewById(R.id.CadastroRotas_btnCancelar);
		btnSalvar = (Button) findViewById(R.id.CadastroRotas_btnSalvar);
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (edtDescricao.getText().toString().equalsIgnoreCase("") 
						|| edtVendedor.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo inválido", "Favor preencher todos os campos");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//InserirNome(GetNumber());
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de rotas?");
				
			}
		});
		
		edtVendedor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent vendedor = new Intent();
				vendedor.setClass(Cadastro_Rotas.this, Consulta_Vendedor_falsa.class);
				startActivityForResult(vendedor, 666);
			}
		});
		
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Rotas.this);
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
						Codigo,
						edtDescricao.getText().toString(),
						tblVendedor.RetornaID(edtVendedor.getText().toString()),
						Data);
				
				tblCodigos.Inserir(Codigo);
				
				Toast.makeText(getApplicationContext(), "Registro salvo com sucesso", Toast.LENGTH_SHORT).show();
				
				tblCodigos.close();
				registro.close();
				tblVendedor.close();
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Rotas.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.close();
				tblCodigos.close();
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
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblCodigos.close();
		tblVendedor.close();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 666: 
			if (resultCode == RESULT_OK) {
				String vendedor = data.getStringExtra("vendedor");
				edtVendedor.setText(vendedor);
				break;
			}
		}
		
	}
	
	
	@Override
	public void onBackPressed() {}
	
}
