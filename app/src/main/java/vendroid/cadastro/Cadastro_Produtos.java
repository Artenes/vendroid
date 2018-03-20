package vendroid.cadastro;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Grupos;
import vendroid.bancodados.Tabela_Marcas;
import vendroid.bancodados.Tabela_Produtos;
import vendroid.consultas.Consulta_Grupos_falsa;
import vendroid.consultas.Consulta_Marcas_falsa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Cadastro_Produtos extends Activity{

	EditText edtDescricao, edtCodigoBarra, edtUnidade, edtEstoqueAtual, edtEstoqueMinimo, edtSituacaoTributaria, 
	edtPrecoCompra, edtPrecoCusto, edtVendaVista, edtVendaPrazo, edtMarca, edtGrupo, edtMargemLucro, edtICMS, edtNCM;
	Button btnSalvar, btnCancelar, btnMargemLucro;
	Spinner spnSituacaoTributaria;
	ToggleButton tglbtnMargemLucro;
	Tabela_Produtos registro;
	Tabela_Marcas tblMarca;
	Tabela_Grupos tblGrupo;
	Tabela_Codigos tblCodigos;
	int lucro = 0;
	Context contexto = this;
	boolean first_access;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_produtos);
		
		registro = new Tabela_Produtos(this);
		tblCodigos = new Tabela_Codigos(this);
		tblMarca = new Tabela_Marcas(this);
		tblGrupo = new Tabela_Grupos(this);
		
		tblCodigos.open();
		registro.open();
		tblMarca.open();
		tblGrupo.open();
		
		
		AtribuicaoObjetos();
		Botoes();
		Mascaras();
		
		if (savedInstanceState != null) {
			lucro = savedInstanceState.getInt("lucro");
			switch (lucro) {
			case 0:
				tglbtnMargemLucro.setSelected(false);
				edtMargemLucro.setEnabled(false);
				btnMargemLucro.setEnabled(false);
				break;
			case 1:
				tglbtnMargemLucro.setSelected(true);
				edtMargemLucro.setEnabled(true);
				btnMargemLucro.setEnabled(true);
				edtVendaVista.setEnabled(false);
				break;
			default:
				break;
			}
		}
		
	}
	
	public void AtribuicaoObjetos() {
		btnSalvar = (Button) findViewById(R.id.CadastroProdutos_btnSalvar);
		btnCancelar = (Button) findViewById(R.id.CadastroProdutos_btnCancelar);
		edtDescricao = (EditText) findViewById(R.id.CadastroProdutos_EdtDescricao);
		edtCodigoBarra = (EditText) findViewById(R.id.CadastroProdutos_EdtCodigoBarras);
		edtUnidade = (EditText) findViewById(R.id.CadastroProdutos_EdtUnidade);
		edtEstoqueAtual = (EditText) findViewById(R.id.CadastroProdutos_EdtEstoqueAtual);
		edtEstoqueMinimo = (EditText) findViewById(R.id.CadastroProdutos_EdtEstoqueMinimo);
		edtPrecoCompra = (EditText) findViewById(R.id.CadastroProdutos_EdtPrecoCompra);
		edtPrecoCusto = (EditText) findViewById(R.id.CadastroProdutos_EdtPrecoCusto);
		edtVendaVista = (EditText) findViewById(R.id.CadastroProdutos_EdtVendaVista);
		edtVendaPrazo = (EditText) findViewById(R.id.CadastroProdutos_EdtVendaPrazo);
		edtMarca = (EditText) findViewById(R.id.CadastroProdutos_EdtMarca);
		edtGrupo = (EditText) findViewById(R.id.CadastroProdutos_EdtGrupo);
		edtMarca.setKeyListener(null);
		edtGrupo.setKeyListener(null);
		tglbtnMargemLucro = (ToggleButton) findViewById(R.id.CadastroProdutos_tglbtnMargemLucro);
		btnMargemLucro = (Button) findViewById(R.id.CadastroProdutos_btnMargemLucro);
		btnMargemLucro.setEnabled(false);
		edtMargemLucro = (EditText) findViewById(R.id.CadastroProdutos_edtMargemLucro);
		edtMargemLucro.setEnabled(false);
		edtICMS = (EditText) findViewById(R.id.CadastroProdutos_edtICMS);
		edtNCM = (EditText) findViewById(R.id.CadastroProdutos_edtNCM);
		spnSituacaoTributaria = (Spinner) findViewById(R.id.CadastroProdutos_spnSituacaoTributaria);
	}
	
	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				try {
					String temp = registro.Descricao_por_Produto(edtDescricao.getText().toString());
					MostrarCaixaNeutra("Atenção", "Descrição já existente no banco de dados");
					return;
				} catch (Exception e) {}
				
				if (	edtDescricao.getText().toString().equalsIgnoreCase("") ||
						edtUnidade.getText().toString().equalsIgnoreCase("") ||
						edtEstoqueAtual.getText().toString().equalsIgnoreCase("") ||
						edtEstoqueMinimo.getText().toString().equalsIgnoreCase("") ||
						edtPrecoCompra.getText().toString().equalsIgnoreCase("") ||
						edtPrecoCusto.getText().toString().equalsIgnoreCase("") ||
						edtVendaVista.getText().toString().equalsIgnoreCase("") ||
						edtVendaPrazo.getText().toString().equalsIgnoreCase("")
					) 
				{
				
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher campos obrigatórios");
					return;
				}
				
				if ((edtNCM.getText().length() > 0) && (edtNCM.getText().length() < 10)) {
					MostrarCaixaNeutra("NCM Inválido", "Favor corrigir o NCM");
					return;
				}
				
				if((Integer.parseInt(edtEstoqueAtual.getText().toString())) < (Integer.parseInt(edtEstoqueMinimo.
						getText().toString()))){			
					MostrarCaixaNeutra("Campo inválido", "Estoque atual menor que Estoque mínimo");
					return;
				}
				
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de produtos?");
			}
		});
		
		tglbtnMargemLucro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (tglbtnMargemLucro.getText().toString().equals("Margem Lucro (ligado)")) {
					edtMargemLucro.setEnabled(true);
					btnMargemLucro.setEnabled(true);
					lucro = 1;
					edtVendaVista.setEnabled(false);
					
				} else {
					edtMargemLucro.setEnabled(false);
					btnMargemLucro.setEnabled(false);
					lucro = 0;
					edtVendaVista.setEnabled(true);
					edtMargemLucro.setText("");
				}
			}
		});
		
		btnMargemLucro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				float porcentagem;
				float custo;
				
				if (edtPrecoCusto.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencha campo do preço de custo");
					return;
				} else {
					custo = Float.parseFloat(edtPrecoCusto.getText().toString().
							replaceAll("[R$.]", "").replaceAll("[,]", "."));
				}
				
				if (custo <= 0) {
					MostrarCaixaNeutra("Campo Inválido", "O valor de custo deve ser maio que zero");
					return;
				}
				
				if (edtMargemLucro.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencha campo de porcentagem");
					return;
				} else {
					porcentagem = Float.parseFloat(edtMargemLucro.getText().toString().replace("%", ""));
				}
				
				if (porcentagem <= 0) {
					MostrarCaixaNeutra("Campo Inválido", "A porcentagem deve ser maior que zero");
					return;
				}
				
				
				
				DecimalFormat df = new DecimalFormat("0.00");
				
				float venda = ((porcentagem/100)*custo)+custo;
				edtVendaVista.setText("R$" + df.format(venda));
			}
		});
		
		edtMarca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent Marcas = new Intent();
				Marcas.setClass(Cadastro_Produtos.this, Consulta_Marcas_falsa.class);
				startActivityForResult(Marcas, 666);
			}
		});
		
		edtGrupo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent grupo = new Intent();
				grupo.setClass(Cadastro_Produtos.this, Consulta_Grupos_falsa.class);
				startActivityForResult(grupo, 999);
			}
		});
		
		spnSituacaoTributaria.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
							int pos, long id) {
				
						Toast.makeText(getApplicationContext(), parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
					}

			public void onNothingSelected(AdapterView<?> arg0) {
						
			}});

		
			}
	
	void Mascaras () {
		
		edtPrecoCompra.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(!(arg0.toString().equals(atual))) {
					edtPrecoCompra.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
					
					double convertido = Double.parseDouble(stringlimpa);
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					
					atual = formato;
					edtPrecoCompra.setText(formato);
					edtPrecoCompra.setSelection(formato.length());
					edtPrecoCompra.addTextChangedListener(this);
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtPrecoCusto.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(!(arg0.toString().equals(atual))) {
					edtPrecoCusto.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
					
					double convertido = Double.parseDouble(stringlimpa);
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					
					atual = formato;
					edtPrecoCusto.setText(formato);
					edtPrecoCusto.setSelection(formato.length());
					edtPrecoCusto.addTextChangedListener(this);
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtVendaPrazo.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(!(arg0.toString().equals(atual))) {
					edtVendaPrazo.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
					
					double convertido = Double.parseDouble(stringlimpa);
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					
					atual = formato;
					edtVendaPrazo.setText(formato);
					edtVendaPrazo.setSelection(formato.length());
					edtVendaPrazo.addTextChangedListener(this);
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtVendaVista.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (lucro == 0) {
					if(!(arg0.toString().equals(atual))) {
						edtVendaVista.removeTextChangedListener(this);
						
						String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
						
						double convertido = Double.parseDouble(stringlimpa);
						String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
						
						atual = formato;
						edtVendaVista.setText(formato);
						edtVendaVista.setSelection(formato.length());
						edtVendaVista.addTextChangedListener(this);
					}
				} else {
					return;
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtMargemLucro.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (lucro == 1) {
					
					if(!(arg0.toString().equals(atual))) {
						edtMargemLucro.removeTextChangedListener(this);
						
						String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
						double convertido = 0;
						
						try {
							convertido = Double.parseDouble(stringlimpa);
						} catch (NumberFormatException e) {
							edtMargemLucro.setSelection(arg0.length());
						}
						String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
						 
						
						atual = formato;
						edtMargemLucro.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
						edtMargemLucro.setSelection(formato.length());
						edtMargemLucro.addTextChangedListener(this);
					}
					
				} else {
					return;
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtICMS.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					
				if(!(arg0.toString().equals(atual))) {
					edtICMS.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
					double convertido = 0;
					
					try {
						convertido = Double.parseDouble(stringlimpa);
					} catch (NumberFormatException e) {
						edtICMS.setSelection(arg0.length());
					}
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					 
					
					atual = formato;
					edtICMS.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
					edtICMS.setSelection(formato.length());
					edtICMS.addTextChangedListener(this);
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {}
		});
		
		
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Produtos.this);
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
						edtMarca.getText().toString(),
						edtGrupo.getText().toString(),
						edtCodigoBarra.getText().toString(),
						edtUnidade.getText().toString(),
						edtEstoqueAtual.getText().toString(),
						edtEstoqueMinimo.getText().toString(),
						spnSituacaoTributaria.getSelectedItem().toString(),
						edtNCM.getText().toString(),
						edtICMS.getText().toString().replaceAll("[%]", ""),
						edtPrecoCompra.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."),
						edtPrecoCusto.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."),
						edtVendaVista.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."),
						edtVendaPrazo.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."),
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Produtos.this);
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
				String marca = data.getStringExtra("Marca");
				edtMarca.setText(marca);
				break;
			}
		
		case 999:
			if (resultCode == RESULT_OK) {
				String grupo = data.getStringExtra("grupo");
				edtGrupo.setText(grupo);
				break;
			}
		}
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("lucro", lucro);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tblCodigos.close();
		registro.close();
		tblGrupo.close();
		tblMarca.close();
	}
	
	
	@Override
	public void onBackPressed() {}
	
}
