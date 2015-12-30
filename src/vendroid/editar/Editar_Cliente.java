//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.editar;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Rotas;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.consultas.Consulta_Rotas_Falsa;
import dipro.vendasandroid.R;
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
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Editar_Cliente extends Activity {

	TextView txvCodigo, txvData, txvCPF, txvRG;
	EditText edtNome, edtCPF, edtRG, edtNascimento, edtEndereco, edtBairro, edtCidade, edtCEP, edtFoneUm, edtFoneDois,
	edtEmail, edtRota;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	ToggleButton tglbtnPessoa;
	Spinner spnUF;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Cliente registro;
	Tabela_Rotas tblRotas;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	int estpessoa = 3;
	Context contexto = this;
	private Object estado;
	private int state;
	private String cliente;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_cliente);
		
		n_acesso = new Tela_Principal();
		
		tblRotas = new Tabela_Rotas(this);
		tblRotas.open();
		
		registro = new Tabela_Cliente(this);
		registro.open();
		
		tblAdministrador  =new Tabela_Administrador(this);
		tblAdministrador.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		AtribuicaoObjetos();
		Mascaras();
		Botoes();
		
		estado = getLastNonConfigurationInstance();
		if (estado != null) {
			SetState((Integer) estado);
			switch (GetState()) {
			case 0:
				//PreencheCampos();
				PreencheTextView();
				BloqueiaCampos();
				SetState(0);
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
			cliente = savedInstanceState.getString("cliente");
			switch (savedInstanceState.getInt("pessoa")) {
			case 0:
				pessoa_fisica();
				tglbtnPessoa.setSelected(true);
				estpessoa = 0;
				break;
			case 1:
				pessoa_juridica();
				tglbtnPessoa.setSelected(false);
				estpessoa = 1;
				break;
			default:
				break;
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
		cliente = extras.getString("id_cliente");
        
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
	
	public void PreencheCampos () {
		Bundle extras = getIntent().getExtras();
		cliente = extras.getString("id_cliente");
        
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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UF,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnUF.setAdapter(adapter);
		
		if (registro.Pessoa(cliente) == 0) {
			tglbtnPessoa.setChecked(true);
			txvCPF.setText("CPF");
			txvRG.setText("RG");
			InputFilter cpf = new InputFilter.LengthFilter(14);
			edtCPF.setFilters((new InputFilter[]{ cpf }));
			edtCPF.setText(registro.CPF(cliente));
			InputFilter rg = new InputFilter.LengthFilter(7);
			edtRG.setFilters((new InputFilter[]{ rg }));
			edtRG.setText(registro.RG(cliente));
			estpessoa = 0;
		} else {
			tglbtnPessoa.setChecked(false);
			txvCPF.setText("CNPJ");
			txvRG.setText("Inscr. Estadual");
			InputFilter cpf = new InputFilter.LengthFilter(18);
			edtCPF.setFilters((new InputFilter[]{ cpf }));
			edtCPF.setText(registro.CNPJ(cliente));
			InputFilter rg = new InputFilter.LengthFilter(15);
			edtRG.setFilters((new InputFilter[]{ rg }));
			edtRG.setText(registro.InscricaoEstadual(cliente));
			estpessoa = 1;
		}
		
		edtNome.setText(registro.Nome(cliente));
		edtEndereco.setText(registro.End(cliente));
		edtBairro.setText(registro.Bairro(cliente));
		edtCidade.setText(registro.Cidade(cliente));
		spnUF.setSelection(adapter.getPosition(registro.Uf(cliente)));				
		edtCEP.setText(registro.cep(cliente));
		edtFoneUm.setText(registro.Foneum(cliente));
		edtFoneDois.setText(registro.Fonedois(cliente));
		edtEmail.setText(registro.Email(cliente));
		edtNascimento.setText(registro.DN(cliente));
		
		if (tblRotas.VerificarRota(registro.Rota(cliente))) {
			edtRota.setText(tblRotas.Rota(registro.Rota(cliente)));
		} else {
			edtRota.setText("Registro Inexistente");
		}
	}
	
	public void LiberaCampos () {
		tglbtnPessoa.setEnabled(true);
		btnSalvar.setEnabled(true);
		edtNome.setEnabled(true);
		edtCPF.setEnabled(true);
		edtRG.setEnabled(true);
		edtNascimento.setEnabled(true);
		edtEndereco.setEnabled(true);
		edtBairro.setEnabled(true);
		edtCidade.setEnabled(true);
		edtCEP.setEnabled(true);
		edtFoneUm.setEnabled(true);
		edtFoneDois.setEnabled(true);
		edtEmail.setEnabled(true);
		edtRota.setEnabled(true);
		spnUF.setEnabled(true);
	}
	
	public void BloqueiaCampos() {
		tglbtnPessoa.setEnabled(false);
		btnSalvar.setEnabled(false);
		edtNome.setEnabled(false);
		edtCPF.setEnabled(false);
		edtRG.setEnabled(false);
		edtNascimento.setEnabled(false);
		edtEndereco.setEnabled(false);
		edtBairro.setEnabled(false);
		edtCidade.setEnabled(false);
		edtCEP.setEnabled(false);
		edtFoneUm.setEnabled(false);
		edtFoneDois.setEnabled(false);
		edtEmail.setEnabled(false);
		edtRota.setEnabled(false);
		spnUF.setEnabled(false);
	}
	
	public void AtribuicaoObjetos() {
		txvCodigo = (TextView) findViewById(R.EditarCliente.txvCodigo);
		txvData = (TextView) findViewById(R.EditarCliente.txvData);
		btnEditar = (Button) findViewById(R.EditarCliente.btnEditar);
		btnSalvar = (Button) findViewById(R.EditarCliente.btnSalvar);
		btnCancelar = (Button) findViewById(R.EditarCliente.btnCancelar);
		btnExcluir = (Button) findViewById(R.EditarCliente.btnExcluir);
		edtNome = (EditText) findViewById(R.EditarCliente.edtNome);
		edtCPF = (EditText) findViewById(R.EditarCliente.edtCPF);
		edtRG = (EditText) findViewById(R.EditarCliente.edtRG);
		edtNascimento = (EditText) findViewById(R.EditarCliente.edtDataNascimento);
		edtEndereco = (EditText) findViewById(R.EditarCliente.edtEndereco);
		edtBairro = (EditText) findViewById(R.EditarCliente.edtBairro);
		edtCidade = (EditText) findViewById(R.EditarCliente.edtCidade);
		edtCEP = (EditText) findViewById(R.EditarCliente.edtCEP);
		edtFoneUm = (EditText) findViewById(R.EditarCliente.edtFoneUm);
		edtFoneDois = (EditText) findViewById(R.EditarCliente.edtFoneDois);
		edtEmail = (EditText) findViewById(R.EditarCliente.edtEmail);
		edtRota= (EditText) findViewById(R.EditarCliente.edtRota);
		edtRota.setKeyListener(null);
		spnUF = (Spinner) findViewById(R.EditarCliente.spnUF);
		tglbtnPessoa = (ToggleButton) findViewById(R.EditarCliente.tglbtnPessoa);
		txvCPF = (TextView) findViewById(R.EditarCliente.txvCPF);
		txvRG = (TextView) findViewById(R.EditarCliente.txvRG);
		
	}
	
	public void Mascaras () {
		
		edtCPF.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
					if (EstaAtualizando) {
						EstaAtualizando = false;
						return;
					}
					boolean TemMascara = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1;
					String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "");
					if (after > before) {
						if (str.length() > 9) {
							str = str.substring(0,3) + '.' + str.substring(3,6) + '.' + str.substring(6,9) + '-' + str.substring(9);
						} else if (str.length() > 6) {
							str = str.substring(0,3) + '.' + str.substring(3,6) + '.' + str.substring(6);
						} else if (str.length() > 3) {
							str = str.substring(0,3) + '.' + str.substring(3);
						}
						EstaAtualizando = true;
						edtCPF.setText(str);
						edtCPF.setSelection(edtCPF.getText().length());
					} else {
						EstaAtualizando = true;
						edtCPF.setText(str);
						edtCPF.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
					}
				} else {
					if (EstaAtualizando) {
						EstaAtualizando = false;
						return;
					}
					boolean TemMascara = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1 || s.toString().indexOf('/') > -1;
					String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "");
					if (after > before) {
						if (str.length() > 12) {
							str = str.substring(0,2) + '.' + str.substring(2,5) + '.' + str.substring(5,8) + '/' + str.substring(8,12) + "-" + str.substring(12);
						} else if (str.length() > 8) {
							str = str.substring(0,2) + '.' + str.substring(2,5) + '.' + str.substring(5,8) + '/' + str.substring(8);
						} else if (str.length() > 5) {
							str = str.substring(0,2) + '.' + str.substring(2,5) + '.' + str.substring(5);
						} else if (str.length() > 2) {
							str = str.substring(0,2) + '.' + str.substring(2);
						}
						
						EstaAtualizando = true;
						edtCPF.setText(str);
						edtCPF.setSelection(edtCPF.getText().length());
					} else {
						EstaAtualizando = true;
						edtCPF.setText(str);
						edtCPF.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
					}
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		edtRG.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void onTextChanged(CharSequence s, int start, int before, int after) {
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Jurídica")) {
					if (EstaAtualizando) {
						EstaAtualizando = false;
						return;
					}
					boolean TemMascara = s.toString().indexOf('.') > -1;
					String str = s.toString().replaceAll("[.]", "");
					if (after > before) {
						if (str.length() > 9) {
							str = str.substring(0,3) + '.' + str.substring(3,6) + '.' + str.substring(6,9) + '.' + str.substring(9);
						} else if (str.length() > 6) {
							str = str.substring(0,3) + '.' + str.substring(3,6) + '.' + str.substring(6);
						} else if (str.length() > 3) {
							str = str.substring(0,3) + '.' + str.substring(3);
						}
						
						EstaAtualizando = true;
						edtRG.setText(str);
						edtRG.setSelection(edtRG.getText().length());
					} else {
						EstaAtualizando = true;
						edtRG.setText(str);
						edtRG.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
					}
				}
			}
			
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			
			public void afterTextChanged(Editable arg0) {}
		});
		
		edtNascimento.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
					if (EstaAtualizando) {
						EstaAtualizando = false;
						return;
					}
					boolean TemMascara = s.toString().indexOf('/') > -1;
					String str = s.toString().replaceAll("[/]", "");
					if (after > before) {
						if (str.length() > 4) {
							str = str.substring(0,2) + '/' + str.substring(2,4) + '/' + str.substring(4);
						} else if (str.length() > 2) {
							str = str.substring(0,2) + '/' + str.substring(2);
						}
						EstaAtualizando = true;
						edtNascimento.setText(str);
						edtNascimento.setSelection(edtNascimento.getText().length());
					} else {
						EstaAtualizando = true;
						edtNascimento.setText(str);
						edtNascimento.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
					}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtFoneDois.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (EstaAtualizando) {
					EstaAtualizando = false;
					return;
				}
				boolean TemMascara = s.toString().indexOf('(') > -1 || s.toString().indexOf(')') > -1 ||s.toString().indexOf('-') > -1;
				String str = s.toString().replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("[-]", "");
				if (after > before) {
					if (str.length() > 6) {
						str = '(' + str.substring(0,2) + ')' + str.substring(2,6) + '-' + str.substring(6);
					} else if (str.length() > 2) {
						str = '(' + str.substring(0,2) + ')' + str.substring(2);
					}
					EstaAtualizando = true;
					edtFoneDois.setText(str);
					edtFoneDois.setSelection(edtFoneDois.getText().length());
				} else {
					EstaAtualizando = true;
					edtFoneDois.setText(str);
					edtFoneDois.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtFoneUm.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (EstaAtualizando) {
					EstaAtualizando = false;
					return;
				}
				boolean TemMascara = s.toString().indexOf('(') > -1 || s.toString().indexOf(')') > -1 || s.toString().indexOf('-') > -1;
				String str = s.toString().replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("[-]", "");
				if (after > before) {
					if (str.length() > 6) {
						str = '(' + str.substring(0,2) + ')' + str.substring(2,6) + '-' + str.substring(6);
					} else if (str.length() > 2) {
						str = '(' + str.substring(0,2) + ')' + str.substring(2);
					}
					EstaAtualizando = true;
					edtFoneUm.setText(str);
					edtFoneUm.setSelection(edtFoneUm.getText().length());
				} else {
					EstaAtualizando = true;
					edtFoneUm.setText(str);
					edtFoneUm.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtCEP.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (EstaAtualizando) {
					EstaAtualizando = false;
					return;
				}
				boolean TemMascara = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1;
				String str = s.toString().replaceAll("[.]", "").replaceAll("[-]", "");
				if (after > before) {
					if (str.length() > 5) {
						str = str.substring(0,2) + '.' + str.substring(2,5) + '-' + str.substring(5);
					} else if (str.length() > 2) {
						str = str.substring(0,2) + '.' + str.substring(2);
					}
					EstaAtualizando = true;
					edtCEP.setText(str);
					edtCEP.setSelection(edtCEP.getText().length());
				} else {
					EstaAtualizando = true;
					edtCEP.setText(str);
					edtCEP.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void pessoa_fisica () {
		txvCPF.setText("CPF");
		txvRG.setText("RG");
		InputFilter cpf = new InputFilter.LengthFilter(14);
		edtCPF.setFilters((new InputFilter[]{ cpf }));
		InputFilter rg = new InputFilter.LengthFilter(7);
		edtRG.setFilters((new InputFilter[]{ rg }));
		estpessoa = 0;
	}
	
	public void pessoa_juridica () {
		txvCPF.setText("CNPJ");
		txvRG.setText("Inscr. Estadual");
		InputFilter cnpj = new InputFilter.LengthFilter(18);
		edtCPF.setFilters((new InputFilter[]{ cnpj }));
		InputFilter estadual = new InputFilter.LengthFilter(15);
		edtRG.setFilters((new InputFilter[]{ estadual }));
		estpessoa = 1;
	}
	
	public void Botoes () {
		
		final Bundle extras = getIntent().getExtras();
		
		
		final Dialog dialog = new Dialog (contexto);
		dialog.setContentView(R.layout.caixa_dialogo);
		dialog.setTitle("Permissão de administrador");
		TextView aviso = (TextView) dialog.findViewById(R.CaixaDialogo.txvAviso);
		aviso.setText("Deseja continuar?");
		final EditText senha = (EditText) dialog.findViewById(R.CaixaDialogo.edtSenha);
		final Button sim = (Button) dialog.findViewById(R.CaixaDialogo.btnSim);
		Button nao = (Button) dialog.findViewById(R.CaixaDialogo.btnNao);
		
		nao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {						
				dialog.dismiss();
			}
		});
		
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if ((edtNome.getText().toString().equalsIgnoreCase("") || 
						edtEndereco.getText().toString().equalsIgnoreCase("") ||
						edtBairro.getText().toString().equalsIgnoreCase("")) || (
								edtFoneDois.getText().toString().equalsIgnoreCase("") &&
								edtFoneUm.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher campos obrigatórios");
					return;
				}
				
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Jurídica")) {
					if ((edtCPF.getText().length() > 0) && (edtCPF.getText().length() < 18)) {
						MostrarCaixaNeutra("CNPJ Inválido", "Favor corrigir o CNPJ");
						return;
					} else if ((edtRG.getText().length() > 0) && (edtRG.getText().length() < 15)) {
						MostrarCaixaNeutra("Inscrição Estadual Inválida", "Favor corrigir a Inscrição Estadual");
						return;
					}
				} else {
					if ((edtCPF.getText().length() > 0) && (edtCPF.getText().length() < 14)) {
						MostrarCaixaNeutra("CPF Inválido", "Favor corrigir CPF");
						return;
					} else if ((edtRG.getText().length() > 0) && (edtRG.getText().length() < 7)) {
						MostrarCaixaNeutra("Data de Nascimento Inválida", "Favor corrigir a data de nascimento");
						return;
					} 
				}
				
				if ((edtNascimento.getText().length() > 0) && (edtNascimento.getText().length() < 10)) {
					MostrarCaixaNeutra("Data de nascimento Inválida", "Favor corrigir a data de nascimento");
					return;
				} else if ((edtCEP.getText().length() > 0) && (edtCEP.getText().length() < 10)) {
					MostrarCaixaNeutra("CEP Inválido", "Favor corrigir o CEP");
					return;
				} else if ((edtFoneUm.getText().length() > 0) && (edtFoneUm.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 1 Inválido", "Favor corrigir o Telefone 1");
					return;
				} else if ((edtFoneDois.getText().length() > 0) && (edtFoneDois.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 2 Inválido", "Favor corrigir o Telefone 2");
					return;
				} else {
					MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (GetState() == 1) {
					edtCPF.setText("");
					edtNascimento.setText("");
					edtFoneUm.setText("");
					edtFoneDois.setText("");
					edtCEP.setText("");
					PreencheCampos();
					BloqueiaCampos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da consulta de clientes?");
				}
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
				}
			}
		});
		
		edtRota.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent consultaRotas = new Intent();
				consultaRotas.setClass(contexto, Consulta_Rotas_Falsa.class);
				startActivityForResult(consultaRotas, 666);
			}
		});
		
		tglbtnPessoa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				edtCPF.setText("");
				edtRG.setText("");
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
					pessoa_fisica();
				} else {
					pessoa_juridica();
				}
			}
		});
		
		btnExcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				
				sim.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
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
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
						
				int id_rota;
		        if (!((edtRota.getText().toString().equalsIgnoreCase("")) || 
		        		(edtRota.getText().toString().equalsIgnoreCase("Registro Inexistente")))) {
		        	id_rota = tblRotas.RetornaID(edtRota.getText().toString());
		        } else {
		        	id_rota = 000000;
		        }
				
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
				        	registro.Atualizar(
									txvCodigo.getText().toString(),
				        			0,
									edtNome.getText().toString(),
									edtCPF.getText().toString().replaceAll("[.-]", ""),
									edtRG.getText().toString(),
									"",
									"",
									edtNascimento.getText().toString().replaceAll("[/]", ""),
									edtEndereco.getText().toString(),
									edtBairro.getText().toString(),
									edtCidade.getText().toString(),
									spnUF.getSelectedItem().toString(),
									edtCEP.getText().toString().replaceAll("[.-]", ""),
									edtFoneUm.getText().toString().replaceAll("[()-]", ""),
									edtFoneDois.getText().toString().replaceAll("[()-]", ""), 
									edtEmail.getText().toString(),
									id_rota);
				        } else {
				        	registro.Atualizar(
				        			txvCodigo.getText().toString(),
				        			1,
									edtNome.getText().toString(),
									"",
									"",
									edtCPF.getText().toString().replaceAll("[./-]", ""),
									edtRG.getText().toString().replaceAll("[.]", ""),
									edtNascimento.getText().toString().replaceAll("[/]", ""),
									edtEndereco.getText().toString(),
									edtBairro.getText().toString(),
									edtCidade.getText().toString(),
									spnUF.getSelectedItem().toString(),
									edtCEP.getText().toString().replaceAll("[.-]", ""),
									edtFoneUm.getText().toString().replaceAll("[()-]", ""),
									edtFoneDois.getText().toString().replaceAll("[()-]", ""), 
									edtEmail.getText().toString(),
									id_rota);
				        }
						
				registro.close();
				tblRotas.close();
				Toast.makeText(getApplicationContext(), "Registro Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.close();
				tblRotas.close();
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
				String route = data.getStringExtra("RotaDescricao");
				edtRota.setText(route);
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
		outState.putString("cliente", cliente);
		outState.putInt("pessoa", estpessoa);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblRotas.close();
		tblAdministrador.close();
		tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
