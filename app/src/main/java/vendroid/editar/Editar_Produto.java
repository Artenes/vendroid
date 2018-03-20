package vendroid.editar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Grupos;
import vendroid.bancodados.Tabela_Marcas;
import vendroid.bancodados.Tabela_Produtos;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.consultas.Consulta_Grupos_falsa;
import vendroid.consultas.Consulta_Marcas_falsa;
import dipro.vendasandroid.Tela_Principal;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Editar_Produto extends Activity {

	TextView txvCodigo, txvData;
	EditText edtDescricao, edtMarca, edtGrupo, edtCodigoBarra, edtUnidade, edtEstoqueAtual, edtEstoqueMinimo, 
	edtPrecoCompra, edtPrecoCusto, edtVendaVista, edtPrecoPrazo, edtMargemLucro, edtICMS, edtNCM;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir, btnMargemLucro;
	Spinner spnSituacaoTributaria;
	ToggleButton tglbtnMargemLucro;
	Tabela_Produtos registro;
	Tabela_Marcas tblMarca;
	Tabela_Grupos tblGrupo;
	Tabela_Administrador tblAdministrador;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tela_Principal n_acesso;
	Context contexto = this;
	private Object estado;
	private int state;
	private String cliente;
	int lucro = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_produto);
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		registro = new Tabela_Produtos(this);
		registro.open();
		
		tblMarca = new Tabela_Marcas(this);
		tblMarca.open();
		
		tblGrupo = new Tabela_Grupos(this);
		tblGrupo.open();
		
		AtribuicaoObjetos();
		Botoes();
		Mascaras();
		
		estado = getLastNonConfigurationInstance();
		if (estado != null) {
			SetState((Integer) estado);
			switch (GetState()) {
			case 0:
				PreencheCampos();
				BloqueiaCampos();
				SetState(0);
				btnExcluir.setEnabled(true);
				break;
				
			case 1:
				PreencheTextView();
				LiberaCampos();
				btnEditar.setEnabled(false);
				btnExcluir.setEnabled(false);
				SetState(1);
			}
		} else {
			PreencheCampos();
			BloqueiaCampos();
			SetState(0);
		}
		
		if (savedInstanceState != null) {
			cliente = savedInstanceState.getString("id");
			
			if (GetState() == 1) {
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
	}
	
	public void SetState (int estado) {
		state = estado;
	}
	
	public int GetState () {
		return state;
	}
	
	public void PreencheTextView () {
		Bundle extras = getIntent().getExtras();
		cliente = extras.getString("id_produto");
        
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(registro.Data(cliente));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String newFormat = formatter.format(testDate);
        txvData.setText(newFormat);
        
		txvCodigo.setText(cliente);
	}

	void LiberaCampos() {
		btnSalvar.setEnabled(true);
		edtDescricao.setEnabled(true);
		edtMarca.setEnabled(true);
		edtGrupo.setEnabled(true);
		edtCodigoBarra.setEnabled(true);
		edtUnidade.setEnabled(true);
		edtEstoqueAtual.setEnabled(true);
		edtEstoqueMinimo.setEnabled(true);
		spnSituacaoTributaria.setEnabled(true);
		edtPrecoCompra.setEnabled(true);
		edtPrecoCusto.setEnabled(true);
		edtVendaVista.setEnabled(true);
		edtPrecoPrazo.setEnabled(true);
		edtNCM.setEnabled(true);
		edtICMS.setEnabled(true);
		tglbtnMargemLucro.setEnabled(true);
		edtMargemLucro.setEnabled(true);
		btnMargemLucro.setEnabled(true);
	}
	
	void BloqueiaCampos () {
		btnSalvar.setEnabled(false);
		edtDescricao.setEnabled(false);
		edtMarca.setEnabled(false);
		edtGrupo.setEnabled(false);
		edtCodigoBarra.setEnabled(false);
		edtUnidade.setEnabled(false);
		edtEstoqueAtual.setEnabled(false);
		edtEstoqueMinimo.setEnabled(false);
		spnSituacaoTributaria.setEnabled(false);
		edtPrecoCompra.setEnabled(false);
		edtPrecoCusto.setEnabled(false);
		edtVendaVista.setEnabled(false);
		edtPrecoPrazo.setEnabled(false);
		edtNCM.setEnabled(false);
		edtICMS.setEnabled(false);
		tglbtnMargemLucro.setEnabled(false);
		edtMargemLucro.setEnabled(false);
		btnMargemLucro.setEnabled(false);
	}
	
	void PreencheCampos() {
		Bundle extras = getIntent().getExtras();
		cliente = extras.getString("id_produto");
		txvCodigo.setText(cliente);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(registro.Data(cliente));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String newFormat = formatter.format(testDate);
        txvData.setText(newFormat);
		
        if (tblMarca.VerificarMarca(registro.Marca(cliente))) {
        	edtMarca.setText(registro.Marca(cliente));
        } else {
        	edtMarca.setText("Registro inexistente");
        }
        
        if (tblGrupo.VerificarGrupo(registro.Grupo(cliente))) {
        	edtGrupo.setText(registro.Grupo(cliente));
        } else {
        	edtGrupo.setText("Registro inexistente");
        }
        
        edtNCM.setText(registro.NCM(cliente));
        edtICMS.setText(registro.ICMS(cliente));
		edtDescricao.setText(registro.Descricao(cliente));
		edtCodigoBarra.setText(registro.CodigoBarra(cliente));
		edtUnidade.setText(registro.Unidade(cliente));
		edtEstoqueAtual.setText(registro.EstoqueAtual(cliente));
		edtEstoqueMinimo.setText(registro.EstoqueMinimo(cliente));
		edtPrecoCompra.setText(registro.PrecoCompra(cliente));
		edtPrecoCusto.setText(registro.Precocusto(cliente));
		edtVendaVista.setText(registro.PrecoVista(cliente));
		edtPrecoPrazo.setText(registro.PrecoPrazo(cliente));
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.id.EditarProduto_txvData);
		txvCodigo = (TextView) findViewById(R.id.EditarProduto_txvCodigo);
		btnEditar = (Button) findViewById(R.id.EditarProduto_btnEditar);
		btnSalvar = (Button) findViewById(R.id.EditarProduto_btnSalvar);
		btnCancelar = (Button) findViewById(R.id.EditarProduto_btnCancelar);
		btnExcluir = (Button) findViewById(R.id.EditarProduto_btnExcluir);
		edtDescricao = (EditText) findViewById(R.id.EditarProduto_edtDescricao);
		edtMarca = (EditText) findViewById(R.id.EditarProduto_edtMarca);
		edtGrupo = (EditText) findViewById(R.id.EditarProduto_edtGrupo);
		edtCodigoBarra = (EditText) findViewById(R.id.EditarProduto_edtCodigoBarra);
		edtUnidade = (EditText) findViewById(R.id.EditarProduto_edtUnidade);
		edtEstoqueAtual = (EditText) findViewById(R.id.EditarProduto_edtEstoqueAtual);
		edtEstoqueMinimo = (EditText) findViewById(R.id.EditarProduto_edtEstoqueMinimo);
		spnSituacaoTributaria = (Spinner) findViewById(R.id.EditarProduto_spnSituacaoTributaria);
		edtPrecoCompra = (EditText) findViewById(R.id.EditarProduto_edtPrecoCompra);
		edtPrecoCusto = (EditText) findViewById(R.id.EditarProduto_edtPrecoCusto);
		edtVendaVista = (EditText) findViewById(R.id.EditarProduto_edtPrecoVista);
		edtPrecoPrazo= (EditText) findViewById(R.id.EditarProduto_edtPrecoPrazo);
		tglbtnMargemLucro = (ToggleButton) findViewById(R.id.EditarProduto_tglbtnMargemLucro);
		edtMargemLucro = (EditText) findViewById(R.id.EditarProduto_edtMargemLucro);
		btnMargemLucro = (Button) findViewById(R.id.EditarProduto_btnMargemLucro);
		edtICMS = (EditText) findViewById(R.id.EditarProduto_edtICMS);
		edtNCM = (EditText) findViewById(R.id.EditarProduto_edtNCM);
		edtMarca.setKeyListener(null);
		edtGrupo.setKeyListener(null);
	}
	
	public void Botoes () {
		
		final Dialog dialog = new Dialog (contexto);
		dialog.setContentView(R.layout.caixa_dialogo);
		dialog.setTitle("Permiss�o de administrador");
		TextView aviso = (TextView) dialog.findViewById(R.id.CaixaDialogo_txvAviso);
		aviso.setText("Deseja continuar?");
		final EditText senha = (EditText) dialog.findViewById(R.id.CaixaDialogo_edtSenha);
		final Button sim = (Button) dialog.findViewById(R.id.CaixaDialogo_btnSim);
		Button nao = (Button) dialog.findViewById(R.id.CaixaDialogo_btnNao);

		nao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (	edtDescricao.getText().toString().equalsIgnoreCase("") ||
						edtUnidade.getText().toString().equalsIgnoreCase("") ||
						edtEstoqueAtual.getText().toString().equalsIgnoreCase("") ||
						edtEstoqueMinimo.getText().toString().equalsIgnoreCase("") ||
						edtPrecoCompra.getText().toString().equalsIgnoreCase("") ||
						edtPrecoCusto.getText().toString().equalsIgnoreCase("") ||
						edtVendaVista.getText().toString().equalsIgnoreCase("") ||
						edtPrecoPrazo.getText().toString().equalsIgnoreCase("")
					) 
				{
					MostrarCaixaNeutra("Campo Inv�lido", "Favor preencher campos obrigat�rios");
					return;
				}
				
				if((Integer.parseInt(edtEstoqueAtual.getText().toString())) < (Integer.parseInt(edtEstoqueMinimo.
						getText().toString()))){			
					MostrarCaixaNeutra("Campo inv�lido", "Estoque atual menor que Estoque m�nimo");
					return;
				}
				
				MostrarCaixaSalvar("Salvar Registro", "Finalizar opera��o?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (GetState() == 1) {
					BloqueiaCampos();
					PreencheCampos();
					AtribuicaoObjetos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da edi��o de produtos?");
				}
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
					edtMargemLucro.setText("");
					if (state == 1) {
						edtVendaVista.setEnabled(true);
					} else {
						edtVendaVista.setEnabled(false);
					}
				}
			}
		});
		
		btnMargemLucro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				float porcentagem;
				float custo;
				
				if (edtPrecoCusto.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inv�lido", "Favor preencha campo do pre�o de custo");
					return;
				} else {
					custo = Float.parseFloat(edtPrecoCusto.getText().toString().
							replaceAll("[R$.]", "").replaceAll("[,]", "."));
				}
				
				if (custo <= 0) {
					MostrarCaixaNeutra("Campo Inv�lido", "O valor de custo deve ser maio que zero");
					return;
				}
				
				if (edtMargemLucro.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inv�lido", "Favor preencha campo de porcentagem");
					return;
				} else {
					porcentagem = Float.parseFloat(edtMargemLucro.getText().toString().replaceAll("[%]", "").replaceAll("[,]", "."));
				}
				
				if (porcentagem <= 0) {
					MostrarCaixaNeutra("Campo Inv�lido", "A porcentagem deve ser maior que zero");
					return;
				}
								
				DecimalFormat df = new DecimalFormat("0.00");
				
				float venda = ((porcentagem/100)*custo)+custo;
				edtVendaVista.setText("R$" + df.format(venda));
				
				
			}
		});
		
		
		btnEditar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							SetState(1);
							LiberaCampos();
							btnEditar.setEnabled(false);
							btnExcluir.setEnabled(false);
							if (tglbtnMargemLucro.getText().toString().equals("Margem Lucro (desligado)")) {
								edtMargemLucro.setEnabled(false);
								btnMargemLucro.setEnabled(false);
							} else {
								edtMargemLucro.setEnabled(true);
								btnMargemLucro.setEnabled(true);
							}
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (tblUsuarioAtual.NivelAcesso("1") == 3 || tblUsuarioAtual.NivelAcesso("1") == 2) {
					dialog.show();
				} else {
					SetState(1);
					LiberaCampos();
					btnEditar.setEnabled(false);
					btnExcluir.setEnabled(false);
					if (tglbtnMargemLucro.getText().toString().equals("Margem Lucro (desligado)")) {
						edtMargemLucro.setEnabled(false);
						btnMargemLucro.setEnabled(false);
					} else {
						edtMargemLucro.setEnabled(true);
						btnMargemLucro.setEnabled(true);
					}
				}
			}
		});
		
		edtMarca.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent consultaRotas = new Intent();
				consultaRotas.setClass(contexto, Consulta_Marcas_falsa.class);
				startActivityForResult(consultaRotas, 666);
			}
		});
		
		edtGrupo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent consultaRotas = new Intent();
				consultaRotas.setClass(contexto, Consulta_Grupos_falsa.class);
				startActivityForResult(consultaRotas, 999);
			}
		});
		
		btnExcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							MostrarCaixaExcluir("Excluir Registro", "Deseja excluir este registro?");
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (tblUsuarioAtual.NivelAcesso("1") == 3 || tblUsuarioAtual.NivelAcesso("1") == 2) {
					dialog.show();
				} else {
					MostrarCaixaExcluir("Excluir Registro", "Deseja excluir este registro?");
				}
			}
		});
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
		
		edtPrecoPrazo.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(!(arg0.toString().equals(atual))) {
					edtPrecoPrazo.removeTextChangedListener(this);
					
					String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
					
					double convertido = Double.parseDouble(stringlimpa);
					String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
					
					atual = formato;
					edtPrecoPrazo.setText(formato);
					edtPrecoPrazo.setSelection(formato.length());
					edtPrecoPrazo.addTextChangedListener(this);
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
		
		edtICMS.addTextChangedListener(new TextWatcher() {
			private String atual = "";
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					if(!(arg0.toString().equals(atual))) {
						edtICMS.removeTextChangedListener(this);
						
						String stringlimpa;
						if (arg0.toString().equalsIgnoreCase("%")) {
							stringlimpa = "0.0";
						} else {
							stringlimpa = arg0.toString().replaceAll("[%]", "");
						}
						double convertido = Double.parseDouble(stringlimpa);
						String formato = NumberFormat.getPercentInstance(Locale.US).format((convertido/100));
						
						atual = formato;
						edtICMS.setText(formato);
						edtICMS.setSelection(formato.length());
						edtICMS.addTextChangedListener(this);
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
						
						double convertido = Double.parseDouble(stringlimpa);
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
		
	}

	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				int id_marca, id_grupo;
				
		        if (!((edtMarca.getText().toString().equalsIgnoreCase("")) || 
		        		(edtMarca.getText().toString().equalsIgnoreCase("Registro Inexistente")))) {
		        	id_marca = tblMarca.RetornaID(edtMarca.getText().toString());
		        } else {
		        	id_marca = 000000;
		        }
		        
		        if (!((edtGrupo.getText().toString().equalsIgnoreCase("")) || 
		        		(edtGrupo.getText().toString().equalsIgnoreCase("Registro Inexistente")))) {
		        	id_grupo = tblGrupo.RetornaId(edtGrupo.getText().toString());
		        } else {
		        	id_grupo = 000000;
		        }
				
				registro.Atualizar(
						txvCodigo.getText().toString(), 
						edtDescricao.getText().toString(),
						Integer.toBinaryString(id_marca),
						Integer.toBinaryString(id_grupo), 
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
						edtPrecoPrazo.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", ".")); 
				registro.close();
				Toast.makeText(getApplicationContext(), "Registro Salvo com Sucesso", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		Alert.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Alert.show();
	}
	
	public void MostrarCaixaExcluir (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.Deletar(cliente);
				Toast.makeText(getApplicationContext(), "Registro Excluido com Sucesso", Toast.LENGTH_SHORT).show();
				registro.close();
				tblGrupo.close();
				tblMarca.close();
				finish();
			}
		});
		Alert.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Alert.show();
	}
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.close();
				finish();
			}
		});
		Alert.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
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
				String route = data.getStringExtra("Marca");
				edtMarca.setText(route);
				break;
			}
		case 999:
			if (resultCode == RESULT_OK) {
				String route = data.getStringExtra("grupo");
				edtGrupo.setText(route);
				break;
			}
		}
		
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return GetState();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("id", cliente);
		outState.putInt("lucro", lucro);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblGrupo.close();
		tblMarca.close();
		tblAdministrador.close();
		tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
