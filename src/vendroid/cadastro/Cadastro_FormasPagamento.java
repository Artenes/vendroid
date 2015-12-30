package vendroid.cadastro;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Forma_Pagamento;
import dipro.vendasandroid.R;
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

public class Cadastro_FormasPagamento extends Activity {

	Button btnSalvar, btnCancelar;
	EditText edtDescricao, edtQuantidadeParcela, edtDiasEntreParcela, edtAcressimo;
	Tabela_Forma_Pagamento registro;
	Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_formaspagamento);
		
		tblCodigos = new Tabela_Codigos(this);
		registro = new Tabela_Forma_Pagamento(this);
		
		registro.open();
		tblCodigos.open();
		
		AtribuicaoObjetos();
		
		Botoes();
		
		Mascara();
	}
	
	public void AtribuicaoObjetos() {
		btnSalvar = (Button) findViewById(R.CadastroFormasPagamento.btnSalvar);
		btnCancelar = (Button) findViewById(R.CadastroFormasPagamento.btnCancelar);
		edtAcressimo = (EditText) findViewById(R.CadastroFormasPagamento.EdtAcressimo);
		edtQuantidadeParcela = (EditText) findViewById(R.CadastroFormasPagamento.EdtQuantidadeParcela);
		edtDescricao = (EditText) findViewById(R.CadastroFormasPagamento.EdtDescricao);
		edtDiasEntreParcela = (EditText) findViewById(R.CadastroFormasPagamento.EdtDiasEntreParcela);
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				try {
					String temp = registro.Descricao_por_FormaPagamento(edtDescricao.getText().toString());
					MostrarCaixaNeutra("Atenção", "Descrição já existente no banco de dados");
					return;
				} catch (Exception e) {}
				
				if (edtAcressimo.getText().toString().equalsIgnoreCase("") || 
						edtDescricao.getText().toString().equalsIgnoreCase("") ||
						edtQuantidadeParcela.getText().toString().equalsIgnoreCase("") ||
						edtDiasEntreParcela.getText().toString().equalsIgnoreCase("") ||
						edtAcressimo.getText().toString().equalsIgnoreCase("")) {
					
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos");
					
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de formas de pagamento?");
			}
		});
	}

		void Mascara () {
			
			edtAcressimo.addTextChangedListener(new TextWatcher() {
				
				private String atual = "";
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					if (!(arg0.length() > 8)) { 
					if(!(arg0.toString().equals(atual))) {
						edtAcressimo.removeTextChangedListener(this);
						
						String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
						
						double convertido = 0;
						
						try {
							convertido = Double.parseDouble(stringlimpa);
						} catch (NumberFormatException e) {
							edtAcressimo.setSelection(arg0.length());
						}
						
						String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
						
						
						atual = formato;
						edtAcressimo.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
						edtAcressimo.setSelection(formato.length());
						edtAcressimo.addTextChangedListener(this);
					}
					} else {
						return;
					}
				}			
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
				
				public void afterTextChanged(Editable s) {}
			});	}
	
	
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_FormasPagamento.this);
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
						edtQuantidadeParcela.getText().toString(),
						edtDiasEntreParcela.getText().toString(),
						edtAcressimo.getText().toString().replaceAll("[%]", ""),
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_FormasPagamento.this);
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
		registro.close();
		tblCodigos.close();
	}
	
	@Override
	public void onBackPressed() {}
	
	
}
