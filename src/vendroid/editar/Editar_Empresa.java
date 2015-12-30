package vendroid.editar;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Empresa;
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
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Editar_Empresa extends Activity {

	
	EditText edtRazaoSocial, edtNomeFantasia, edtCNPJ, edtInscricaoEstadual, edtEndereco, edtBairro, edtCidade, edtCEP,
	edtRepresentante, edtFoneUm, edtFoneDois, edtEmail, edtSite;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	Spinner spnUF;
	Tabela_Empresa registro;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	private Object estado;
	private int state;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_empresa);
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		registro = new Tabela_Empresa(this);
		registro.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		if (registro.ObterTodos()) {
			AtribuicaoObjetos();
			Mascaras();
			Botoes();	
		} else {
			Toast.makeText(getApplicationContext(), "Nenhum Registro Encontado", Toast.LENGTH_SHORT).show();
			finish();
		}
		
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
				LiberaCampos();
				btnEditar.setEnabled(false);
				btnExcluir.setEnabled(false);
				SetState(1);
				break;
			}
		} else if (registro.ObterTodos()){
			PreencheCampos();
			BloqueiaCampos();
			SetState(0);
		}
	}
	
	public void PreencheCampos () {
		id = "111111";
        
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UF,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnUF.setAdapter(adapter);
		edtRazaoSocial.setText(registro.RazaoSocial(id));
		edtNomeFantasia.setText(registro.NomeFantasia(id));
		edtSite.setText(registro.Site(id));
		edtEmail.setText(registro.Email(id));
		edtRepresentante.setText(registro.Representante(id));
		edtEndereco.setText(registro.Endereco(id));
		edtBairro.setText(registro.Bairro(id));
		edtCidade.setText(registro.Cidade(id));
		spnUF.setSelection(adapter.getPosition(registro.UF(id)));
		edtCNPJ.setText(registro.CNPJ(id));
		edtInscricaoEstadual.setText(registro.InscricaoEstadual(id));
		edtCEP.setText(registro.CEP(id));
		edtFoneUm.setText(registro.FoneUm(id));
		edtFoneDois.setText(registro.FoneDois(id));
	}
	
	public void LiberaCampos () {
		btnSalvar.setEnabled(true);
		edtRazaoSocial.setEnabled(true);
		edtNomeFantasia.setEnabled(true);
		edtCNPJ.setEnabled(true);
		edtInscricaoEstadual.setEnabled(true);
		edtEndereco.setEnabled(true);
		edtBairro.setEnabled(true);
		edtCidade.setEnabled(true);
		edtCEP.setEnabled(true);
		edtRepresentante.setEnabled(true);
		edtFoneUm.setEnabled(true);
		edtFoneDois.setEnabled(true);
		edtEmail.setEnabled(true);
		edtSite.setEnabled(true);
		spnUF.setEnabled(true);
	}
	
	public void BloqueiaCampos() {
		SetState(1);
		btnSalvar.setEnabled(false);
		edtRazaoSocial.setEnabled(false);
		edtNomeFantasia.setEnabled(false);
		edtCNPJ.setEnabled(false);
		edtInscricaoEstadual.setEnabled(false);
		edtEndereco.setEnabled(false);
		edtBairro.setEnabled(false);
		edtCidade.setEnabled(false);
		edtCEP.setEnabled(false);
		edtRepresentante.setEnabled(false);
		edtFoneUm.setEnabled(false);
		edtFoneDois.setEnabled(false);
		edtEmail.setEnabled(false);
		edtSite.setEnabled(false);
		spnUF.setEnabled(false);
	}
	
	public void AtribuicaoObjetos() {
		btnEditar = (Button) findViewById(R.EditarEmpresa.btnEditar);
		btnSalvar = (Button) findViewById(R.EditarEmpresa.btnSalvar);
		btnCancelar = (Button) findViewById(R.EditarEmpresa.btnCancelar);
		edtRazaoSocial = (EditText) findViewById(R.EditarEmpresa.edtRazaoSocial);
		edtNomeFantasia = (EditText) findViewById(R.EditarEmpresa.edtNomeFantasia);
		edtCNPJ = (EditText) findViewById(R.EditarEmpresa.edtCNPJ);
		edtInscricaoEstadual = (EditText) findViewById(R.EditarEmpresa.edtInscricaoEstadual);
		edtEndereco = (EditText) findViewById(R.EditarEmpresa.edtEndereco);
		edtBairro = (EditText) findViewById(R.EditarEmpresa.edtBairro);
		edtCidade = (EditText) findViewById(R.EditarEmpresa.edtCidade);
		edtCEP = (EditText) findViewById(R.EditarEmpresa.edtCEP);
		edtRepresentante = (EditText) findViewById(R.EditarEmpresa.edtRepresentante);
		edtFoneUm = (EditText) findViewById(R.EditarEmpresa.edtTelefoneContatoUm);
		edtFoneDois = (EditText) findViewById(R.EditarEmpresa.edtTelefoneContatoDois);
		edtEmail = (EditText) findViewById(R.EditarEmpresa.edtEmail);
		edtSite= (EditText) findViewById(R.EditarEmpresa.edtSite);
		spnUF = (Spinner) findViewById(R.EditarEmpresa.spnUF);
		btnExcluir = (Button) findViewById(R.EditarEmpresa.btnExcluir);
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
			public void onClick(View arg0) {						
				dialog.dismiss();
			}
		});
		
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
				} else {
					MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
				}	
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (GetState() == 1) {
					BloqueiaCampos();
					AtribuicaoObjetos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da consulta da empresa?");
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
					public void onClick(View arg0) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							MostrarCaixaDeletar("Excluir Registro", "Deseja excluir este registro?");
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
					MostrarCaixaDeletar("Excluir Registro", "Deseja excluir este registro?");
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
				registro.Atualizar(
						
						id,
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
						edtFoneDois.getText().toString().replaceAll("[()-]", ""), 
						edtEmail.getText().toString(),
						edtSite.getText().toString());
				
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

	public void MostrarCaixaDeletar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.Deletar("111111");
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
	
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}
	
	public void SetState (int estado) {
		state = estado;
	}
	
	public int GetState () {
		return state;
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return GetState();
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
