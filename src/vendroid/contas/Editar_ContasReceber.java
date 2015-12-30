//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.contas;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Contas_Receber;
import vendroid.bancodados.Tabela_Forma_Pagamento;
import vendroid.bancodados.Tabela_Vendas;
import vendroid.cadastro.Cadastro_Vendedor;
import dipro.vendasandroid.R;
import dipro.vendasandroid.Tela_Principal;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Editar_ContasReceber extends Activity {

	
	private SimpleCursorAdapter dataSource, change;
	TextView txvCodigo, txvData;
	EditText edtCliente, edtCodigoVenda, edtFormaPagamento, edtDataVencimento, edtValorTotal, edtNumeroParcela, edtValorParcela;
	Button btnVoltar, btnExcluir, btnEditarData, btnBaixa, btnRefatorar;
	ListView lstviewParcelas;
	Tabela_Contas_Receber registro;
	Tabela_Cliente tblCliente;
	Tabela_Forma_Pagamento tblFormaPagamento;
	Tabela_Administrador tblAdministrador;
	Tabela_Vendas tblVendas;
	Tela_Principal n_acesso;
	Context contexto = this;
	String id = "";
	int dia, mes, ano;
	private int ESTA_EDITANDO;
	private Object state;
	String data;
	static final int DATA = 111;
	
	private static final String campos [] = {"cr_numparcela","cr_valorparc", "cr_datavenc", "_id"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_contas_receber);
		
		tblVendas = new Tabela_Vendas(this);
		tblVendas.open();
		
		registro = new Tabela_Contas_Receber(this);
		registro.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		tblCliente = new Tabela_Cliente(this);
		tblCliente.open();
		
		tblFormaPagamento = new Tabela_Forma_Pagamento(this);
		tblFormaPagamento.open();
		
		//state = savedInstanceState.getInt("esta_editando");
		
		AtribuicaoObjetos();
		Botoes();
		BloqueiaCampos();
		PreencherCampos();
		Lista();
		
		if (state == null) {
			//LiberaCampos();
			//btnEditar.setEnabled(false);
			ESTA_EDITANDO = 0;
		} 
		
		
		
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	public void BloqueiaCampos () {
		//btnSalvar.setEnabled(false);
		edtCliente.setEnabled(false);
		edtCodigoVenda.setEnabled(false);
		edtDataVencimento.setEnabled(false);
		edtFormaPagamento.setEnabled(false);
		edtNumeroParcela.setEnabled(false);
		edtValorParcela.setEnabled(false);
		edtValorTotal.setEnabled(false);
	}
	
	public void LiberaCampos () {
		edtCliente.setEnabled(true);
		edtCodigoVenda.setEnabled(true);
		edtDataVencimento.setEnabled(true);
		edtFormaPagamento.setEnabled(true);
		edtNumeroParcela.setEnabled(true);
		edtValorParcela.setEnabled(true);
		edtValorTotal.setEnabled(true);
	}
	
	public void PreencherCampos () {
		
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_conta");
		txvCodigo.setText(id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(registro.Data(id));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String newFormat = formatter.format(testDate);
		txvData.setText(newFormat);
		
		try {
			edtCliente.setText(tblCliente.Nome_por_Cliente(registro.Cliente(id)));
		} catch (Exception e) {
			edtCliente.setText("Registro inexistente");
		}
		
		try {
			String temp = tblVendas.Cliente(Integer.toString(registro.Venda(id)));
			edtCodigoVenda.setText(Integer.toString(registro.Venda(id)));
		} catch (Exception e) {
			edtCodigoVenda.setText("Registro inexistente");
		}
		
		try {
			edtFormaPagamento.setText(tblFormaPagamento.Descricao(Integer.toString(registro.FormaPagamento(id))));
		} catch (Exception e) {
			edtFormaPagamento.setText("Registro inexistente");
		}
        
        edtDataVencimento.setText(registro.DataVencimento(id));
        edtNumeroParcela.setText(registro.NumeroParcela(id));
        
        if (!registro.ValorParcela(id).contains(".")) {
        	edtValorParcela.setText("R$" + registro.ValorParcela(id) + ".00");
        } else {
        	edtValorParcela.setText("R$" + registro.ValorParcela(id));
        }
        
        if (!registro.ValorTotal(id).contains(".")) {
        	edtValorTotal.setText("R$" + registro.ValorTotal(id) + ".00");
        } else {
        	edtValorTotal.setText("R$" + registro.ValorTotal(id));
        }
        
        
       
        dataSource = new SimpleCursorAdapter(
        		contexto,
        		R.layout.editar_contas_linhas,
        		registro.InfoConta(Integer.toString(registro.Venda(id))),
				campos,
				new int[] {R.EditarContasLinhas.txvNumeroParcela, R.EditarContasLinhas.txvValorParcela, R.EditarContasLinhas.txvDataVencimento, R.EditarContasLinhas.txvCodigo}
        		);
		lstviewParcelas.setAdapter(dataSource);
		setListViewHeightBasedOnChildren(lstviewParcelas);
	}
	

	public void Lista () {
		lstviewParcelas.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String codigo = (String) ((TextView)arg1.findViewById(R.EditarContasLinhas.txvCodigo)).getText();
				 	id = codigo;
				 	txvCodigo.setText(id);
				 	try {
						edtCliente.setText(tblCliente.Nome_por_Cliente(registro.Cliente(id)));
					} catch (Exception e) {
						edtCliente.setText("Registro inexistente");
					}
				 	try {
						String temp = tblVendas.Cliente(Integer.toString(registro.Venda(id)));
						edtCodigoVenda.setText(Integer.toString(registro.Venda(id)));
					} catch (Exception e) {
						edtCodigoVenda.setText("Registro inexistente");
					}
					
			        edtDataVencimento.setText(registro.DataVencimento(id));
			        try {
						edtFormaPagamento.setText(tblFormaPagamento.Descricao(Integer.toString(registro.FormaPagamento(id))));
					} catch (Exception e) {
						edtFormaPagamento.setText("Registro inexistente");
					}
			        edtNumeroParcela.setText(registro.NumeroParcela(id));
			        if (!registro.ValorParcela(id).contains(".")) {
			        	edtValorParcela.setText("R$" + registro.ValorParcela(id) + ".00");
			        } else {
			        	edtValorParcela.setText("R$" + registro.ValorParcela(id));
			        }
			        
			        if (!registro.ValorTotal(id).contains(".")) {
			        	edtValorTotal.setText("R$" + registro.ValorTotal(id) + ".00");
			        } else {
			        	edtValorTotal.setText("R$" + registro.ValorTotal(id));
			        }
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			        Date testDate = null;
			        try {
			            testDate = sdf.parse(registro.Data(id));
			        }catch(Exception ex){
			            ex.printStackTrace();
			        }
			        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
			        String newFormat = formatter.format(testDate);
			        txvData.setText(newFormat);
			 }
		});
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.EditarContasReceber.txvData);
		txvCodigo = (TextView) findViewById(R.EditarContasReceber.txvCodigo);
		btnVoltar = (Button) findViewById(R.EditarContasReceber.btnVoltar);
		btnBaixa = (Button) findViewById(R.EditarContasReceber.btnBaixa);
		btnEditarData = (Button) findViewById(R.EditarContasReceber.btnEditarData);
		btnExcluir = (Button) findViewById(R.EditarContasReceber.btnExcluir);
		btnRefatorar = (Button) findViewById(R.EditarContasReceber.btnRefatorar);
		edtCliente = (EditText) findViewById(R.EditarContasReceber.edtCliente);
		edtCodigoVenda = (EditText) findViewById(R.EditarContasReceber.edtCodigoVenda);
		edtDataVencimento = (EditText) findViewById(R.EditarContasReceber.edtDataVencimento);
		edtFormaPagamento = (EditText) findViewById(R.EditarContasReceber.edtFormaPagamento);
		edtNumeroParcela = (EditText) findViewById(R.EditarContasReceber.edtQtdParcelas);
		edtValorParcela = (EditText) findViewById(R.EditarContasReceber.edtValorParcela);
		edtValorTotal = (EditText) findViewById(R.EditarContasReceber.edtValorTotal);
		lstviewParcelas = (ListView) findViewById(R.EditarContasReceber.lstviewParcelas);
	}
	
	public void Botoes () {
		
		final Dialog dialog = new Dialog (contexto);
		dialog.setContentView(R.layout.caixa_dialogo);
		dialog.setTitle("Permissão de administrador");
		TextView aviso = (TextView) dialog.findViewById(R.CaixaDialogo.txvAviso);
		aviso.setText("Deseja continuar?");
		final EditText senha = (EditText) dialog.findViewById(R.CaixaDialogo.edtSenha);
		final Button sim = (Button) dialog.findViewById(R.CaixaDialogo.btnSim);
		Button nao = (Button) dialog.findViewById(R.CaixaDialogo.btnNao);

		nao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		btnExcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
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
				
				if (n_acesso.NivelAcesso() == 3 || n_acesso.NivelAcesso() == 1) {
					dialog.show();
				} else {
					MostrarCaixaExcluir("Excluir Registro", "Deseja excluir este registro?");
				}
			}
		});
		
		btnEditarData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							MostrarCaixaEditarData("Editar Data de Vencimento", "Deseja editar a data de vencimento?");
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (n_acesso.NivelAcesso() == 3 || n_acesso.NivelAcesso() == 1) {
					dialog.show();
				} else {
					MostrarCaixaEditarData("Editar Data de Vencimento", "Deseja editar a data de vencimento?");
				}
				
			}
		});
		
		btnBaixa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							MostrarCaixaBaixa("Baixa de Conta a Receber", "Deseja dar baixa neste registro?");
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (n_acesso.NivelAcesso() == 3 || n_acesso.NivelAcesso() == 1) {
					dialog.show();
				} else {
					MostrarCaixaBaixa("Baixa de Conta a Receber", "Deseja dar baixa neste registro?");
				}
				
			}
		});
		
		btnRefatorar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				final DecimalFormat df = new DecimalFormat("###.##");
				
				final Dialog dialogo = new Dialog (contexto);
				dialogo.setContentView(R.layout.caixa_dialogo_venda);
				dialogo.setTitle("                    Refatorar                  ");
				final EditText edtdescontogeral = (EditText) dialogo.findViewById(R.CaixaDialogoVenda.edtDescontoGeral);
				final EditText edttotal = (EditText) dialogo.findViewById(R.CaixaDialogoVenda.edtTotal);
				edttotal.setEnabled(false);
				final EditText edttroco = (EditText) dialogo.findViewById(R.CaixaDialogoVenda.edtTroco);
				edttroco.setEnabled(false);
				final EditText edtpago = (EditText) dialogo.findViewById(R.CaixaDialogoVenda.edtValorPago);
				Button btnfinalizar = (Button) dialogo.findViewById(R.CaixaDialogoVenda.btnFinalizar);
				Button btncancelar = (Button) dialogo.findViewById(R.CaixaDialogoVenda.btnCancelar);
				TextView txvdescontogeral = (TextView) dialogo.findViewById(R.CaixaDialogoVenda.txvDescontoGeral);
				TextView txvtroco = (TextView) dialogo.findViewById(R.CaixaDialogoVenda.txvTroco);
				TextView txvtotal = (TextView) dialogo.findViewById(R.CaixaDialogoVenda.txvTotal);
				txvdescontogeral.setVisibility(4);
				txvtroco.setVisibility(4);
				edtdescontogeral.setVisibility(4);
				edttroco.setVisibility(4);
				
				txvtotal.setText("Valor da Parcela");
				edttotal.setText(edtValorParcela.getText().toString());
				
				edtpago.addTextChangedListener(new TextWatcher() {
					private String atual = "";
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						
						//Máscara de dinheiro
						if(!(arg0.toString().equals(atual))) {
							edtpago.removeTextChangedListener(this);
							
							String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
							
							double convertido = Double.parseDouble(stringlimpa);
							String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
							
							atual = formato;
							edtpago.setText(formato);
							edtpago.setSelection(formato.length());
							edtpago.addTextChangedListener(this);
						}
						
						//Cálculo para definir o troco
						
						if (edtpago.getText().toString().equalsIgnoreCase("")) {
							edttroco.setText("0.0");
							return;
						} else {
							//Primeiro caso - se o valor dado for menor que o valor a ser pago, o troco não é exibido
							if (Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", ".")) <
									Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", ""))) {
								edttroco.setText("0.0");
								return;
							} else {
							//Segundo caso - se não o valor será calculado e troco será mostrado 
							edttroco.setText(df.format(Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", ".")) -
									Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", ""))));
							if (!(edttroco.getText().toString().contains("."))) {
									edttroco.setText(edttroco.getText().toString() + ".00");
								}
							}
						}
					}
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
					public void afterTextChanged(Editable arg0) {}
				});
			
				btnfinalizar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (edtpago.getText().toString().equalsIgnoreCase("")) {
							MostrarCaixaNeutra("Atenção", "Valor pago não possui nenhum valor!");
							return;
						}
						
						if (
								Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", "")) 
								< 
								Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."))
							) {
							MostrarCaixaNeutra("Atenção", "Valor pago maior que a parcela - Para realizar baixas, clique no botão 'Baixa' no menu anterior");
							return;
						} else if (
								Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", "")) 
								== 
								Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."))) 
						{
							MostrarCaixaNeutra("Atenção", "Valor pago igual a parcela - Para realizar baixas, clique no botão 'Baixa' no menu anterior");
							return;
						}
						
						registro.AtualizarValorParcela(
								txvCodigo.getText().toString(),
								Float.parseFloat(registro.ValorParcela(id)) - Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", "."))
								);
						
						Toast.makeText(getApplicationContext(), "Fatoração efetuada com sucesso", Toast.LENGTH_SHORT).show();
						dialogo.dismiss();
						finish();
					}
				});
				
				btncancelar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {					
						dialogo.dismiss();
					}
				});
				
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							dialogo.show();
							senha.setText("");
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (n_acesso.NivelAcesso() == 3 || n_acesso.NivelAcesso() == 1) {
					dialog.show();
				} else {
					dialogo.show();
				}
			}
		});
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				MostrarCaixaCancelar("Cancelar Registro", "Sair da consulta de contas a receber?");
			}
		});
	
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATA:
		   return new DatePickerDialog(this, datePickerListener, 
                         ano, mes, dia);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		ano = selectedYear;
		mes = selectedMonth;
		dia = selectedDay;
		
		data = dia+"/"+(mes+1)+"/"+ano;
		
		registro.AtualizarDataVencimento(id, data);
		
		Toast.makeText(getApplicationContext(), "Data editada com sucesso", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	};

	public void MostrarCaixaExcluir (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.Deletar(Long.parseLong(txvCodigo.getText().toString()));
				Toast.makeText(getApplicationContext(), "Registro excluido com sucesso", Toast.LENGTH_SHORT).show();
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
	
	public void MostrarCaixaEditarData (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final Calendar c = Calendar.getInstance();
				ano = c.get(Calendar.YEAR);
				mes = c.get(Calendar.MONTH);
				dia = c.get(Calendar.DAY_OF_MONTH);
				showDialog(DATA);
				new DatePickerDialog(contexto, datePickerListener, ano, mes, dia);
			}
		});
		Alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Alert.show();
	}
	
	public void MostrarCaixaBaixa (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.Deletar(Long.parseLong(id));
				Toast.makeText(getApplicationContext(), "Baixa efetuada com sucesso", Toast.LENGTH_SHORT).show();
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
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Editar_ContasReceber.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return dataSource;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("esta_editando", ESTA_EDITANDO);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblCliente.close();
		tblFormaPagamento.close();
		tblAdministrador.close();
		tblVendas.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}