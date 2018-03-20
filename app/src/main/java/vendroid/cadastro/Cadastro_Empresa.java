package vendroid.cadastro;

import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Empresa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;

public class Cadastro_Empresa extends Activity {

	Button btnSalvar, btnCancelar;
	EditText edtRazaoSocial, edtNomeFantasia, edtCNPJ, edtInscricaoEstadual, edtEndereco, edtBairro, edtCidade, edtCEP,
	edtRepresentante, edtFoneUm,edtFonedois, edtEmail, edtSite;
	Spinner spnUF;
	Tabela_Empresa registro;
	Tabela_Codigos tblCodigos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cadastro_empresa);
		registro = new Tabela_Empresa(this);
		tblCodigos = new Tabela_Codigos(this);
		
		registro.open();
		tblCodigos.open();
		
		if (registro.ObterTodos()) {
			registro.close();
			tblCodigos.close();
			Toast.makeText(getApplicationContext(), "A empresa já está cadastrada", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		AtribuicaoObjetos();//Coloca cada ID em seu respectivo objeto
		
		Mascaras();//Grupo de mascaras para EditText
		
		Botoes();//Atribui função aos botões de salvar e cancelar
		
	}
	
	public void AtribuicaoObjetos() {
		btnSalvar = (Button) findViewById(R.id.CadastroEmpresa_btnSalvar);
		btnCancelar = (Button) findViewById(R.id.CadastroEmpresa_btnCancelar);
		edtRazaoSocial = (EditText) findViewById(R.id.CadastroEmpresa_edtRazaoSocial);
		edtNomeFantasia = (EditText) findViewById(R.id.CadastroEmpresa_edtNomeFantasia);
		edtCNPJ = (EditText) findViewById(R.id.CadastroEmpresa_edtCNPJ);
		edtInscricaoEstadual = (EditText) findViewById(R.id.CadastroEmpresa_edtInscricaoEstadual);
		edtEndereco = (EditText) findViewById(R.id.CadastroEmpresa_edtEndereco);
		edtBairro = (EditText) findViewById(R.id.CadastroEmpresa_edtBairro);
		edtCidade = (EditText) findViewById(R.id.CadastroEmpresa_edtCidade);
		edtCEP = (EditText) findViewById(R.id.CadastroEmpresa_edtCEP);
		edtRepresentante = (EditText) findViewById(R.id.CadastroEmpresa_edtRepresentante);
		edtFoneUm = (EditText) findViewById(R.id.CadastroEmpresa_edtTelefoneContatoUm);
		edtFonedois = (EditText) findViewById(R.id.CadastroEmpresa_edtTelefoneContatoDois);
		edtEmail= (EditText) findViewById(R.id.CadastroEmpresa_edtEmail);
		edtSite= (EditText) findViewById(R.id.CadastroEmpresa_edtSite);
		spnUF = (Spinner) findViewById(R.id.CadastroEmpresa_spnUF);
	}
	
	public void Mascaras () {
	
		edtCNPJ.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
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
					edtCNPJ.setText(str);
					edtCNPJ.setSelection(edtCNPJ.getText().length());
				} else {
					EstaAtualizando = true;
					edtCNPJ.setText(str);
					edtCNPJ.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
				}
			}
			public void afterTextChanged(Editable s) {
			}
		});
	
		edtInscricaoEstadual.addTextChangedListener(new TextWatcher() {
			boolean EstaAtualizando;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int after) {	
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
					edtInscricaoEstadual.setText(str);
					edtInscricaoEstadual.setSelection(edtInscricaoEstadual.getText().length());
				} else {
					EstaAtualizando = true;
					edtInscricaoEstadual.setText(str);
					edtInscricaoEstadual.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
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
	
		edtFonedois.addTextChangedListener(new TextWatcher() {
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
					edtFonedois.setText(str);
					edtFonedois.setSelection(edtFonedois.getText().length());
				} else {
					EstaAtualizando = true;
					edtFonedois.setText(str);
					edtFonedois.setSelection(Math.max(0, Math.min(TemMascara ? start - before : start, str.length())));
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
				if ((
						edtRazaoSocial.getText().toString().equalsIgnoreCase("") ||
						edtCNPJ.getText().toString().equalsIgnoreCase("") ||
						edtInscricaoEstadual.getText().toString().equalsIgnoreCase("") ||
						edtEndereco.getText().toString().equalsIgnoreCase("") ||
						edtBairro.getText().toString().equalsIgnoreCase("") || 
						edtFoneUm.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher campos obrigatórios");
					return;
				} else if ((edtCNPJ.getText().length() > 0) && (edtCNPJ.getText().length() < 18)) {
					MostrarCaixaNeutra("CNPJ Inválido", "Favor corrigir");
					return;
				} else if ((edtInscricaoEstadual.getText().length() > 0) && (edtInscricaoEstadual.getText().length() < 15)) {
					MostrarCaixaNeutra("Inscrição Estadual Inválida", "Favor corrigir");
					return;
				} else if ((edtCEP.getText().length() > 0) && (edtCEP.getText().length() < 10)) {
					MostrarCaixaNeutra("CEP Inválido", "Favor corrigir o CEP");
					return;
				} else if ((edtFoneUm.getText().length() > 0) && (edtFoneUm.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 1 Inválido", "Favor corrigir o Telefone 1");
					return;
				} if ((edtFonedois.getText().length() > 0) && (edtFonedois.getText().length() < 13)) {
					MostrarCaixaNeutra("Telefone 2 Inválido", "Favor corrigir o Telefone 2");
					return;
				} else {
					MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MostrarCaixaCancelar("Cancelar Registro", "Sair do cadastro da empresa?");
			}
		});
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Empresa.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				int Codigo = 111111;
				
				SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		        String Data = sdfDateTime.format(new Date(System.currentTimeMillis()));

				registro.Inserir(
						Codigo,
						edtRazaoSocial.getText().toString(),
						edtNomeFantasia.getText().toString(),
						edtCNPJ.getText().toString().replaceAll("[./-]", ""), 
						edtInscricaoEstadual.getText().toString().replaceAll("[.]", ""),
						edtEndereco.getText().toString(),
						edtBairro.getText().toString(),
						edtCidade.getText().toString(),
						spnUF.getSelectedItem().toString(),
						edtCEP.getText().toString().replaceAll("[.-]", ""),
						edtRepresentante.getText().toString(),
						edtFoneUm.getText().toString().replaceAll("[()-]", ""),
						edtFonedois.getText().toString().replaceAll("[()-]", ""), 
						edtEmail.getText().toString(),
						edtSite.getText().toString(),
						Data);
				
				tblCodigos.Inserir(Codigo);
				
				Toast.makeText(getApplicationContext(), "Registro salvo com sucesso", Toast.LENGTH_SHORT).show();
				
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
	
	public void MostrarCaixaCancelar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Cadastro_Empresa.this);
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
