package vendroid.editar;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Vendedor;
import dipro.vendasandroid.R;
import dipro.vendasandroid.Tela_Principal;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Editar_Vendedor extends Activity {

	TextView txvCodigo, txvData;
	EditText edtNome, edtSenha, edtComissao;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	Tabela_Vendedor registro;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	String id;
	private Object estado;
	private int state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_vendedor);
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		registro = new Tabela_Vendedor(this);
		registro.open();
		
		AtribuicaoObjetos();
		Botoes();
		
		estado = getLastNonConfigurationInstance();
		if (estado != null) {
			SetState((Integer) estado);
			switch (GetState()) {
			case 0:
				//PreencherCampos();
				PreencheTextView();
				BloqueiaCampos();
				SetState(0);
				//btnExcluir.setEnabled(true);
				break;
				
			case 1:
				PreencheTextView();
				LiberaCampos();
				btnEditar.setEnabled(false);
				btnExcluir.setEnabled(false);
				SetState(1);
			}
		} else {
			PreencherCampos();
			BloqueiaCampos();
			SetState(0);
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
		id = extras.getString("id_vendedor");
        
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

	void LiberaCampos() {
		btnSalvar.setEnabled(true);
		edtNome.setEnabled(true);
		edtSenha.setEnabled(true);
		edtComissao.setEnabled(true);
	}
	
	void BloqueiaCampos() {
		btnSalvar.setEnabled(false);
		edtNome.setEnabled(false);
		edtSenha.setEnabled(false);
		edtComissao.setEnabled(false);
	}
	
	void PreencherCampos() {
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_vendedor");
		
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
		edtNome.setText(registro.Nome(id));
		edtSenha.setText(registro.Senha(id));
		edtComissao.setText(registro.Comissao(id));
	}
	
	public void AtribuicaoObjetos() {
		txvCodigo = (TextView) findViewById(R.EditarVendedor.txvCodigo);
		txvData = (TextView) findViewById(R.EditarVendedor.txvData);
		btnEditar = (Button) findViewById(R.EditarVendedor.btnEditar);
		btnSalvar = (Button) findViewById(R.EditarVendedor.btnSalvar);
		btnSalvar.setEnabled(false);
		btnCancelar = (Button) findViewById(R.EditarVendedor.btnCancelar);
		edtNome = (EditText) findViewById(R.EditarVendedor.edtNome);
		edtNome.setEnabled(false);
		edtSenha = (EditText) findViewById(R.EditarVendedor.edtSenha);
		edtSenha.setEnabled(false);
		edtComissao = (EditText) findViewById(R.EditarVendedor.edtComissao);
		edtComissao.setEnabled(false);
		btnExcluir = (Button) findViewById(R.EditarVendedor.btnExcluir);
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
						edtSenha.getText().toString().equalsIgnoreCase("") ||
						edtComissao.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher campos obrigatórios");
					return;
				}
				MostrarCaixaSalvar("Salvar Registro", "Finalizar operação?");
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (GetState() == 1) {
					BloqueiaCampos();
					PreencherCampos();
					AtribuicaoObjetos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da edição de vendedores?");
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
	}
	
	public void MostrarCaixaSalvar (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				registro.Atualizar(txvCodigo.getText().toString(),
						edtNome.getText().toString(),
						edtSenha.getText().toString(), 
						edtComissao.getText().toString());
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
				Toast.makeText(getApplicationContext(), "Registro Excluir com Sucesso", Toast.LENGTH_SHORT).show();
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