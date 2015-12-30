package vendroid.editar;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Fornecedor;
import vendroid.bancodados.Tabela_Usuario_Atual;
import dipro.vendasandroid.R;
import dipro.vendasandroid.Tela_Principal;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Editar_Fornecedor extends Activity {

	TextView txvCodigo, txvData, txvCPF, txvRG;
	EditText edtNome, edtCPF, edtRG, edtEndereco, edtBairro, edtCidade, edtCEP, edtFoneUm, edtFoneDois, edtEmail;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	ToggleButton tglbtnPessoa;
	Spinner spnUF;
	Tabela_Fornecedor registro;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	String id;
	private Object estado = null;
	private int state;
	int pessoa = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_fornecedor);
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		registro = new Tabela_Fornecedor(this);
		registro.open();
		
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
				btnExcluir.setEnabled(true);
				break;
				
			case 1:
				PreencheTextView();
				LiberaCampos();
				btnEditar.setEnabled(false);
				btnExcluir.setEnabled(false);
				SetState(1);
				break;
			}
		} else {
			PreencheCampos();
			BloqueiaCampos();
			SetState(0);
		}
		
		if (savedInstanceState != null) {
				pessoa = savedInstanceState.getInt("pessoa");
				switch (pessoa) {
				case 0:
					pessoa_fisica();
					tglbtnPessoa.setChecked(true);
					break;
				case 1:
					tglbtnPessoa.setChecked(false);
					pessoa_juridica();
					break;
				default:
					break;
				}
		}
		
		if (savedInstanceState != null) {
			id = savedInstanceState.getString("id");
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
		id = extras.getString("id_fornecedor");
        
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
        
		txvCodigo.setText(id);
	}
	
	void LiberaCampos () {
		btnSalvar.setEnabled(true);
		edtNome.setEnabled(true);
		edtCPF.setEnabled(true);
		edtRG.setEnabled(true);
		edtEndereco.setEnabled(true);
		edtBairro.setEnabled(true);
		edtCidade.setEnabled(true);
		edtCEP.setEnabled(true);
		edtFoneUm.setEnabled(true);
		edtFoneDois.setEnabled(true);
		edtEmail.setEnabled(true);
		spnUF.setEnabled(true);
		tglbtnPessoa.setEnabled(true);
	} 
	
	void BloqueiaCampos() {
		btnSalvar.setEnabled(false);
		edtNome.setEnabled(false);
		edtCPF.setEnabled(false);
		edtRG.setEnabled(false);
		edtEndereco.setEnabled(false);
		edtBairro.setEnabled(false);
		edtCidade.setEnabled(false);
		edtCEP.setEnabled(false);
		edtFoneUm.setEnabled(false);
		edtFoneDois.setEnabled(false);
		edtEmail.setEnabled(false);
		spnUF.setEnabled(false);
		tglbtnPessoa.setEnabled(false);
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.EditarFornecedor.txvData);
		txvCodigo = (TextView) findViewById(R.EditarFornecedor.txvCodigo);
		txvCPF = (TextView) findViewById(R.EditarFornecedor.txvCPF);
		txvRG = (TextView) findViewById(R.EditarFornecedor.txvRG);
		btnEditar = (Button) findViewById(R.EditarFornecedor.btnEditar);
		btnSalvar = (Button) findViewById(R.EditarFornecedor.btnSalvar);
		btnCancelar = (Button) findViewById(R.EditarFornecedor.btnCancelar);
		btnExcluir = (Button) findViewById(R.EditarFornecedor.btnExcluir);
		edtNome = (EditText) findViewById(R.EditarFornecedor.edtNome);
		edtCPF = (EditText) findViewById(R.EditarFornecedor.edtCPF);
		edtRG = (EditText) findViewById(R.EditarFornecedor.edtRG);
		edtEndereco = (EditText) findViewById(R.EditarFornecedor.edtEndereco);
		edtBairro = (EditText) findViewById(R.EditarFornecedor.edtBairro);
		edtCidade = (EditText) findViewById(R.EditarFornecedor.edtCidade);
		edtCEP = (EditText) findViewById(R.EditarFornecedor.edtCEP);
		edtFoneUm = (EditText) findViewById(R.EditarFornecedor.edtFoneUm);
		edtFoneDois = (EditText) findViewById(R.EditarFornecedor.edtFoneDois);
		edtEmail = (EditText) findViewById(R.EditarFornecedor.edtEmail);
		spnUF = (Spinner) findViewById(R.EditarFornecedor.spnUF);
		tglbtnPessoa = (ToggleButton) findViewById(R.EditarFornecedor.tglbtnPessoa);
	}
	
	
	void PreencheCampos () {
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_fornecedor");
		
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
		
        if (registro.Pessoa(id) == 0) {
        	tglbtnPessoa.setChecked(true);
			
        	InputFilter rg = new InputFilter.LengthFilter(7);
			edtRG.setFilters((new InputFilter[]{ rg }));
			
			InputFilter cpf = new InputFilter.LengthFilter(14);
			edtCPF.setFilters((new InputFilter[]{ cpf }));
        	
        	txvCPF.setText("CPF");
			txvRG.setText("RG");
			
			edtCPF.setText(registro.CPF(id));
			edtRG.setText(registro.RG(id));
			
			pessoa = 0;
        } else {
        	tglbtnPessoa.setChecked(false);
        	InputFilter cnpj = new InputFilter.LengthFilter(18);
			edtCPF.setFilters((new InputFilter[]{ cnpj }));
			
			InputFilter estadual = new InputFilter.LengthFilter(15);
			edtRG.setFilters((new InputFilter[]{ estadual }));
			
        	txvCPF.setText("CNPJ");
        	txvRG.setText("Inscr. Estadual");
        	
			edtCPF.setText(registro.CNPJ(id));
			edtRG.setText(registro.InscricaoEstadual(id));
			
        	pessoa = 1;
        }
        
        txvCodigo.setText(id);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UF, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnUF.setAdapter(adapter);
		edtNome.setText(registro.Nome(id));
		edtEndereco.setText(registro.End(id));
		edtBairro.setText(registro.Bairro(id));
		edtCidade.setText(registro.Cidade(id));
		spnUF.setSelection(adapter.getPosition(registro.Uf(id)));
		edtFoneUm.setText(registro.Foneum(id));
		edtFoneDois.setText(registro.Fonedois(id));
		edtEmail.setText(registro.Email(id));
		edtCEP.setText(registro.cep(id));
	}
	
	public void pessoa_fisica () {
		txvCPF.setText("CPF");
		txvRG.setText("RG");
		InputFilter cpf = new InputFilter.LengthFilter(14);
		edtCPF.setFilters((new InputFilter[]{ cpf }));
		InputFilter rg = new InputFilter.LengthFilter(7);
		edtRG.setFilters((new InputFilter[]{ rg }));
		pessoa = 0;
	}
	
	public void pessoa_juridica () {
		txvCPF.setText("CNPJ");
		txvRG.setText("Inscr. Estadual");
		InputFilter cnpj = new InputFilter.LengthFilter(18);
		edtCPF.setFilters((new InputFilter[]{ cnpj }));
		InputFilter estadual = new InputFilter.LengthFilter(15);
		edtRG.setFilters((new InputFilter[]{ estadual }));
		pessoa = 1;
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
		
		
		edtFoneUm.addTextChangedListener(new TextWatcher() {
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
	
		edtFoneDois.addTextChangedListener(new TextWatcher() {
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

		
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((edtNome.getText().toString().equalsIgnoreCase("") || 
						edtEndereco.getText().toString().equalsIgnoreCase("") ||
						edtBairro.getText().toString().equalsIgnoreCase("")) || (
								edtFoneDois.getText().toString().equalsIgnoreCase("") &&
								edtFoneUm.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos obrigatórios");
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
				}
				
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Jurídica")) {
					if ((edtCPF.getText().length() > 0) && (edtCPF.getText().length() < 18)) {
						MostrarCaixaNeutra("CNPJ Inválido", "Favor corrigir CNPJ");
						return;
					} else if ((edtRG.getText().length() > 0) && (edtRG.getText().length() < 15)) {
						MostrarCaixaNeutra("Inscrição Estadual Inválido", "Favor corrigir Inscrição Estadual");
						return;
					} 
				} else { 
					if ((edtCPF.getText().length() > 0) && (edtCPF.getText().length() < 14)) {
						MostrarCaixaNeutra("CPF Inválido", "Favor corrigir CPF");
						return;
					} else if ((edtRG.getText().length() > 0) && (edtRG.getText().length() < 7)) {
						MostrarCaixaNeutra("RG Inválido", "Favor corrigir RG");
						return;
					}
				}
				
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
		
			}
				
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (GetState() == 1) {
					edtCPF.setText("");
					edtRG.setText("");
					edtFoneDois.setText("");
					edtFoneUm.setText("");
					edtCEP.setText("");
					BloqueiaCampos();
					PreencheCampos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da edição de fornecedores?");
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
		
		tglbtnPessoa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				edtCPF.setText("");
				edtRG.setText("");
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
					txvCPF.setText("CPF");
					txvRG.setText("RG");
					InputFilter cpf = new InputFilter.LengthFilter(14);
					edtCPF.setFilters((new InputFilter[]{ cpf }));
					InputFilter rg = new InputFilter.LengthFilter(7);
					edtRG.setFilters((new InputFilter[]{ rg }));
					pessoa = 0;
				} else {
					txvCPF.setText("CNPJ");
					txvRG.setText("Inscr. Estadual");
					InputFilter cnpj = new InputFilter.LengthFilter(18);
					edtCPF.setFilters((new InputFilter[]{ cnpj }));
					InputFilter estadual = new InputFilter.LengthFilter(15);
					edtRG.setFilters((new InputFilter[]{ estadual }));
					pessoa = 1;
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
				
				if (pessoa == 0) {
					registro.Atualizar(
							txvCodigo.getText().toString(),
							0,
							edtNome.getText().toString(),
							edtCPF.getText().toString().replaceAll("[.-]", ""), 
							edtRG.getText().toString(),
							"",
							"",
							edtEndereco.getText().toString(),
							edtBairro.getText().toString(),
							edtCidade.getText().toString(), 
							spnUF.getSelectedItem().toString(),
							edtCEP.getText().toString().replaceAll("[.-]", ""), 
							edtFoneUm.getText().toString().replaceAll("[()-]", ""), 
							edtFoneDois.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString());
				} else {
					registro.Atualizar(
							txvCodigo.getText().toString(),
							1,
							edtNome.getText().toString(),
							"",
							"",
							edtCPF.getText().toString().replaceAll("[./-]", ""),
							edtRG.getText().toString().replaceAll("[.]", ""),
							edtEndereco.getText().toString(),
							edtBairro.getText().toString(),
							edtCidade.getText().toString(), 
							spnUF.getSelectedItem().toString(),
							edtCEP.getText().toString().replaceAll("[.-]", ""), 
							edtFoneUm.getText().toString().replaceAll("[()-]", ""), 
							edtFoneDois.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString());
				}
				
				registro.close();
				Toast.makeText(getApplicationContext(), "Registro Salvo com Sucesso", Toast.LENGTH_SHORT).show();
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
				registro.Deletar(id);
				Toast.makeText(getApplicationContext(), "Registro Excluido com Sucesso", Toast.LENGTH_SHORT).show();
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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
	public Object onRetainNonConfigurationInstance() {
		return GetState();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("id", id);
		outState.putInt("pessoa", pessoa);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblAdministrador.close();
		tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
