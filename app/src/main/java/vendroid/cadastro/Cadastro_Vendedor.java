package vendroid.cadastro;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Vendedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Cadastro_Vendedor extends Activity {

	Button btnCancelar, btnSalvar;
	EditText edtNome, edtSenha, edtComissao;
	Tabela_Vendedor registro;
	Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_vendedor);
		
		registro = new Tabela_Vendedor(this);
		registro.open();
		
		tblCodigos = new Tabela_Codigos(this);
		tblCodigos.open();
		
		AtribuicaoObjetos();
		
		Botoes();
		
		Mascara();
		
	}
	
	public void AtribuicaoObjetos () {
		btnCancelar = (Button) findViewById(R.id.CadastroVendedor_btnCancelar);
		btnSalvar = (Button) findViewById(R.id.CadastroVendedor_btnSalvar);
		edtComissao = (EditText) findViewById(R.id.CadastroVendedor_EdtComissao);
		edtNome = (EditText) findViewById(R.id.CadastroVendedor_EdtNome);
		edtSenha = (EditText) findViewById(R.id.CadastroVendedor_EdtSenha);
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					String temp = registro.Nome_por_vendedor(edtNome.getText().toString());
					MostrarCaixaNeutra("Atenção", "Descrição já existente no banco de dados");
					return;
				} catch (Exception e) {}
				
				if (edtNome.getText().toString().equalsIgnoreCase("") ||
						edtSenha.getText().toString().equalsIgnoreCase("") ||
						edtComissao.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo inválido", "Favor preencher todos os campos");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de vendedores?");
			}
		});
	}
	
	void Mascara () {
		
			edtComissao.addTextChangedListener(new TextWatcher() {
			
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (!(arg0.length() > 8)) { 
				if(!(arg0.toString().equals(atual))) {
					edtComissao.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
					
					double convertido = 0;
					
					try {
						convertido = Double.parseDouble(stringlimpa);
					} catch (NumberFormatException e) {
						edtComissao.setSelection(arg0.length());
					}
					
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					
					atual = formato;
					edtComissao.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
					edtComissao.setSelection(formato.length());
					edtComissao.addTextChangedListener(this);
				}
				} else {
					return;
				}
			}			
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			public void afterTextChanged(Editable s) {}
		});
		
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Vendedor.this);
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
						edtNome.getText().toString(),
						edtSenha.getText().toString(),
						edtComissao.getText().toString(),
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Vendedor.this);
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
	protected void onDestroy() {
		super.onDestroy();
		tblCodigos.close();
		registro.close();
	}
	
	@Override
	public void onBackPressed() {}
	
}
