package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Fornecedor;
import dipro.vendasandroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;

public class Cadastro_Fornecedor extends Activity{
Button btnSalvar, btnCancelar;
EditText edtNome, edtCPF, edtRG, edtEndereco, edtBairro, edtCidade, edtCEP, edtTelFixo, edtTelCelular, edtEmail;
Spinner spnUF;
ToggleButton tglbtnPessoa;
TextView txvCPF, txvRG;
Tabela_Fornecedor registro;
Tabela_Codigos tblCodigos;
int pessoa = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro_fornecedor);
		registro = new Tabela_Fornecedor(this);
		tblCodigos = new Tabela_Codigos(this);
		
		registro.open();
		tblCodigos.open();
		
		AtribuicaoObjetos();//Coloca cada ID em seu respectivo objeto
		
		Mascaras();//Grupo de mascaras para EditText
		
		Botoes();//Atribui função aos botões de salvar e cancelar
		
		if (savedInstanceState != null) {
			pessoa = savedInstanceState.getInt("pessoa");
			switch (pessoa) {
			case 0:
				tglbtnPessoa.setChecked(true);
				txvCPF.setText("CPF");
				txvRG.setText("RG");
				pessoa = 0;
				break;
			case 1:
				tglbtnPessoa.setChecked(false);
				txvCPF.setText("CNPJ");
				txvRG.setText("Inscr. Estadual");
				pessoa = 1;
				break;
			default:
				break;
			}
		}
		
	}
	
	public void AtribuicaoObjetos () {
		edtNome = (EditText) findViewById(R.CadastroFornecedor.EdtNome);
		edtCPF = (EditText) findViewById(R.CadastroFornecedor.EdtCPF);
		edtRG = (EditText) findViewById(R.CadastroFornecedor.EdtRG);
		edtEndereco = (EditText) findViewById(R.CadastroFornecedor.EdtEndereco);
		edtBairro = (EditText) findViewById(R.CadastroFornecedor.EdtBairro);
		edtCidade = (EditText) findViewById(R.CadastroFornecedor.EdtCidade);
		edtCEP = (EditText) findViewById(R.CadastroFornecedor.EdtCEP);
		edtTelFixo = (EditText) findViewById(R.CadastroFornecedor.EdtTelefoneFixo);
		edtTelCelular = (EditText) findViewById(R.CadastroFornecedor.EdtTelefoneCelular);
		edtEmail = (EditText) findViewById(R.CadastroFornecedor.EdtEmail);
		btnSalvar = (Button) findViewById(R.CadastroFornecedor.btnSalvar);
		btnCancelar = (Button) findViewById(R.CadastroFornecedor.btnCancelar);
		spnUF = (Spinner) findViewById(R.CadastroFornecedor.spnUF);
		txvCPF = (TextView) findViewById(R.CadastroFornecedor.txvCPF);
		txvRG = (TextView) findViewById(R.CadastroFornecedor.txvRG);
		tglbtnPessoa = (ToggleButton) findViewById(R.CadastroFornecedor.tglbtnPessoa);
	}
	
	public void Mascaras () {
		edtCPF.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
				if (pessoa == 0) {
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
		
		edtTelFixo.addTextChangedListener(new TextWatcher() {
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
					edtTelFixo.setText(str);
					edtTelFixo.setSelection(edtTelFixo.getText().length());
				} else {
					EstaAtualizando = true;
					edtTelFixo.setText(str);
					edtTelFixo.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		edtTelCelular.addTextChangedListener(new TextWatcher() {
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
					edtTelCelular.setText(str);
					edtTelCelular.setSelection(edtTelCelular.getText().length());
				} else {
					EstaAtualizando = true;
					edtTelCelular.setText(str);
					edtTelCelular.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtRG.addTextChangedListener(new TextWatcher() {
			
				boolean EstaAtualizando;
				public void onTextChanged(CharSequence s, int start, int before, int after) {
				if (pessoa == 1) {
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
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((edtNome.getText().toString().equalsIgnoreCase("") || 
						edtEndereco.getText().toString().equalsIgnoreCase("") ||
						edtBairro.getText().toString().equalsIgnoreCase("")) || (
								edtTelCelular.getText().toString().equalsIgnoreCase("") &&
								edtTelFixo.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos obrigatórios");
					return;
				} else if ((edtCEP.getText().length() > 0) && (edtCEP.getText().length() < 10)) {
					MostrarCaixaNeutra("CEP Inválido", "Favor corrigir o CEP");
					return;
				} else if ((edtTelFixo.getText().length() > 0) && (edtTelFixo.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 1 Inválido", "Favor corrigir o Telefone 1");
					return;
				} else if ((edtTelCelular.getText().length() > 0) && (edtTelCelular.getText().length() < 13)) {
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
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro de fornecedores?");
			}
		});
		
		tglbtnPessoa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (tglbtnPessoa.getText().toString().equalsIgnoreCase("Pessoa Física")) {
					InputFilter cpf = new InputFilter.LengthFilter(14);
					edtCPF.setFilters((new InputFilter[]{ cpf }));
					InputFilter rg = new InputFilter.LengthFilter(7);
					edtRG.setFilters((new InputFilter[]{ rg }));
					edtCPF.setText("");
					txvCPF.setText("CPF");
					edtRG.setText("");
					txvRG.setText("RG");
					pessoa = 0;
				} else {
					InputFilter cnpj = new InputFilter.LengthFilter(18);
					edtCPF.setFilters((new InputFilter[]{ cnpj }));
					InputFilter inscricao = new InputFilter.LengthFilter(15);
					edtRG.setFilters((new InputFilter[]{ inscricao }));
					edtCPF.setText("");
					edtRG.setText("");
					txvCPF.setText("CNPJ");
					txvRG.setText("Insc. Estadual");
					pessoa = 1;
				}
			}
		});
		
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Fornecedor.this);
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
				
		        if (pessoa == 0) {
		        	registro.Inserir(
							Integer.toString(Codigo),
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
							edtTelFixo.getText().toString().replaceAll("[()-]", ""), 
							edtTelCelular.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString(),
							Data);
		        } else {
		        	registro.Inserir(
							Integer.toString(Codigo),
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
							edtTelFixo.getText().toString().replaceAll("[()-]", ""), 
							edtTelCelular.getText().toString().replaceAll("[()-]", ""), 
							edtEmail.getText().toString(),
							Data);

		        }
		        
								
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Fornecedor.this);
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pessoa", pessoa);
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

