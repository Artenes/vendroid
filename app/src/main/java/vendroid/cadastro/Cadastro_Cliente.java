//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Rotas;
import vendroid.consultas.Consulta_Rotas_Falsa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

public class Cadastro_Cliente extends Activity {

	Button btnSalvar, btnCancelar;
	EditText edtNome, edtCPF, edtRG, edtNascimento, edtEndereco, edtBairro, edtCEP, edtCidade,  edtTeleFixo, edtTeleCelular,
	edtEmail, edtRota;
	Spinner spnUF;
	TextView txvCPF, txvRG;
	ToggleButton tglbtnPessoa;
	Tabela_Cliente registro;
	Tabela_Codigos tblCodigos;
	Tabela_Rotas tblRotas;
	int estpessoa = 3;
	int estado_caixa_dialogo = 0;
	//1 = caixa salvar
	//2 = caixa cancelar
	//3 = caixa neutra

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cadastro_cliente);
		registro = new Tabela_Cliente(this);
		tblCodigos = new Tabela_Codigos(this);
		tblRotas = new Tabela_Rotas(this);
		
		registro.open();
		tblCodigos.open();
		tblRotas.open();
		
		AtribuicaoObjetos();//Coloca cada ID em seu respectivo objeto
		
		Mascaras();//Grupo de mascaras para EditText
		
		Botoes();//Atribui função aos botões de salvar e cancelar
		
		edtRota.setKeyListener(null);
		
		if (savedInstanceState != null) {
			switch (savedInstanceState.getInt("pessoa")) {
			case 0:
				pessoa_fisica();
				tglbtnPessoa.setSelected(true);
				break;
			case 1:
				pessoa_juridica();
				tglbtnPessoa.setSelected(false);
				break;
			default:
				break;
			}
		}
		
	}
	
	public void AtribuicaoObjetos() {
		btnSalvar = (Button) findViewById(R.id.CadastroCliente_btnSalvar);
		btnCancelar = (Button) findViewById(R.id.CadastroCliente_btnCancelar);
		edtNome = (EditText) findViewById(R.id.CadastroCliente_EdtNome);
		edtCPF = (EditText) findViewById(R.id.CadastroCliente_EdtCPF);
		edtRG = (EditText) findViewById(R.id.CadastroCliente_EdtRG);
		edtNascimento = (EditText) findViewById(R.id.CadastroCliente_EdtDataNascimento);
		edtEndereco = (EditText) findViewById(R.id.CadastroCliente_EdtEndereco);
		edtBairro = (EditText) findViewById(R.id.CadastroCliente_EdtBairro);
		edtCidade = (EditText) findViewById(R.id.CadastroCliente_EdtCidade);
		edtCEP = (EditText) findViewById(R.id.CadastroCliente_EdtCEP);
		edtTeleFixo = (EditText) findViewById(R.id.CadastroCliente_EdtTelefoneFixo);
		edtTeleCelular = (EditText) findViewById(R.id.CadastroCliente_EdtTelefoneCelular);
		edtEmail = (EditText) findViewById(R.id.CadastroCliente_EdtEmail);
		edtRota= (EditText) findViewById(R.id.CadastroCliente_EdtRota);
		spnUF = (Spinner) findViewById(R.id.CadastroCliente_spnUF);
		tglbtnPessoa = (ToggleButton) findViewById(R.id.CadastroCliente_tglbtnPessoa);
		txvRG = (TextView) findViewById(R.id.CadastroCliente_txvRG);
		txvCPF = (TextView) findViewById(R.id.CadastroCliente_txvCPF);
	}
	
	public void Mascaras () {
	
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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
					return;
				} else {
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
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtTeleCelular.addTextChangedListener(new TextWatcher() {
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
					edtTeleCelular.setText(str);
					edtTeleCelular.setSelection(edtTeleCelular.getText().length());
				} else {
					EstaAtualizando = true;
					edtTeleCelular.setText(str);
					edtTeleCelular.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtTeleFixo.addTextChangedListener(new TextWatcher() {
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
					edtTeleFixo.setText(str);
					edtTeleFixo.setSelection(edtTeleFixo.getText().length());
				} else {
					EstaAtualizando = true;
					edtTeleFixo.setText(str);
					edtTeleFixo.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
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

	public void Botoes () {
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO Fazer a verificação do CPF
				
				try {
					String temp = registro.Nome_por_Cliente(edtNome.getText().toString());
					MostrarCaixaNeutra("Atenção", "Nome já existente no banco de dados");
					return;
				} catch (Exception e) {}
				
				if ((edtNome.getText().toString().equalsIgnoreCase("") || 
						edtEndereco.getText().toString().equalsIgnoreCase("") ||
						edtBairro.getText().toString().equalsIgnoreCase("")) || (
								edtTeleCelular.getText().toString().equalsIgnoreCase("") &&
								edtTeleFixo.getText().toString().equalsIgnoreCase(""))) {
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
						MostrarCaixaNeutra("RG Inválido", "Favor corrigir RG");
						return;
					}
				}
				
				if ((edtNascimento.getText().length() > 0) && (edtNascimento.getText().length() < 10)) {
					MostrarCaixaNeutra("Data de Nascimento Inválida", "Favor corrigir a data de nascimento");
					return;
				}  else if ((edtCEP.getText().length() > 0) && (edtCEP.getText().length() < 10)) {
					MostrarCaixaNeutra("CEP Inválido", "Favor corrigir o CEP");
					return;
				} else if ((edtTeleFixo.getText().length() > 0) && (edtTeleFixo.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 1 Inválido", "Favor corrigir o Telefone 1");
					return;
				} else if ((edtTeleCelular.getText().length() > 0) && (edtTeleCelular.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 2 Inválido", "Favor corrigir o Telefone 2");
					return;
				} else {
					MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de clientes?");
			}
		});
		
		edtRota.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent consultaRotas = new Intent();
				consultaRotas.setClass(Cadastro_Cliente.this, Consulta_Rotas_Falsa.class);
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
		InputFilter cpf = new InputFilter.LengthFilter(18);
		edtCPF.setFilters((new InputFilter[]{ cpf }));
		InputFilter nascimento = new InputFilter.LengthFilter(15);
		edtRG.setFilters((new InputFilter[]{ nascimento }));
		estpessoa = 1;
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
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
		        
		        int id_rota;
		        if (!(edtRota.getText().toString().equalsIgnoreCase(""))) {
		        	id_rota = tblRotas.RetornaID(edtRota.getText().toString());
		        } else {
		        	id_rota = 000000;
		        }
		        
		        
		        if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
		        	registro.Inserir(
							0,
		        			Integer.toString(Codigo),
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
							edtTeleFixo.getText().toString().replaceAll("[()-]", ""),
							edtTeleCelular.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString(),
							id_rota,
							Data);
		        } else {
		        	registro.Inserir(
		        			1,
							Integer.toString(Codigo),
							edtNome.getText().toString(),
							"",
							"",
							edtCPF.getText().toString().replaceAll("[./-]", ""),
							edtRG.getText().toString().replaceAll("[.]", ""),
							edtNascimento.getText().toString().toString().replaceAll("[/]", ""),
							edtEndereco.getText().toString(),
							edtBairro.getText().toString(),
							edtCidade.getText().toString(),
							spnUF.getSelectedItem().toString(),
							edtCEP.getText().toString().replaceAll("[.-]", ""),
							edtTeleFixo.getText().toString().replaceAll("[()-]", ""),
							edtTeleCelular.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString(),
							id_rota,
							Data);
		        }
				
				
				tblCodigos.Inserir(Codigo);
				
				Toast.makeText(getApplicationContext(), "Registro salvo com sucesso", Toast.LENGTH_SHORT).show();
				
				registro.close();
				tblRotas.close();
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
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
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
				String route = data.getStringExtra("RotaDescricao");
				edtRota.setText(route);
				break;
			}
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pessoa", estpessoa);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tblCodigos.close();
		registro.close();
		tblRotas.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
