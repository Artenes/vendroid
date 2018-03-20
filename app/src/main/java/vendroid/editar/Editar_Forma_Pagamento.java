package vendroid.editar;

import java.text.SimpleDateFormat;
import java.util.Date;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Forma_Pagamento;
import vendroid.bancodados.Tabela_Usuario_Atual;
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

import dipro.vendasandroid.Tela_Principal;

public class Editar_Forma_Pagamento extends Activity {

	TextView txvCodigo, txvData;
	EditText edtDescricao, edtQuantidadeParcelas, edtDiasParcelas, edtAcressimo;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	Tabela_Forma_Pagamento registro;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Context contexto = this;
	String id;
	private int state;
	private Object estado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_formaspagamento);
		
		registro = new Tabela_Forma_Pagamento(this);
		registro.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		n_acesso = new Tela_Principal();
		
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
		id = extras.getString("id_pagamento");
        
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
	
	public void BloqueiaCampos () {
		btnSalvar.setEnabled(false);
		edtDescricao.setEnabled(false);
		edtQuantidadeParcelas.setEnabled(false);
		edtDiasParcelas.setEnabled(false);
		edtAcressimo.setEnabled(false);
	}
	
	public void LiberaCampos () {
		btnSalvar.setEnabled(true);
		edtDescricao.setEnabled(true);
		edtQuantidadeParcelas.setEnabled(true);
		edtDiasParcelas.setEnabled(true);
		edtAcressimo.setEnabled(true);
	}
	
	public void PreencherCampos () {
		
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_pagamento");
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
		
		
		
		edtDescricao.setText(registro.Descricao(id));
		edtQuantidadeParcelas.setText(Integer.toString(registro.QuantidadeParcelas(id)));
		edtDiasParcelas.setText(registro.DiasParcelas(id));
		edtAcressimo.setText(registro.Acressimo(id));
		
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.id.EditarFormasPagamento_txvData);
		txvCodigo = (TextView) findViewById(R.id.EditarFormasPagamento_txvCodigo);
		btnEditar = (Button) findViewById(R.id.EditarFormasPagamento_btnEditar);
		btnSalvar = (Button) findViewById(R.id.EditarFormasPagamento_btnSalvar);
		btnCancelar = (Button) findViewById(R.id.EditarFormasPagamento_btnCancelar);
		btnExcluir = (Button) findViewById(R.id.EditarFormasPagamento_btnExcluir);
		edtDescricao = (EditText) findViewById(R.id.EditarFormasPagamento_EdtDescricao);
		edtQuantidadeParcelas = (EditText) findViewById(R.id.EditarFormasPagamento_EdtQuantidadeParcela);
		edtDiasParcelas = (EditText) findViewById(R.id.EditarFormasPagamento_EdtDiasEntreParcela);
		edtAcressimo = (EditText) findViewById(R.id.EditarFormasPagamento_EdtAcressimo);
	}
	
	public void Botoes () {
		
		final Dialog dialog = new Dialog (contexto);
		dialog.setContentView(R.layout.caixa_dialogo);
		dialog.setTitle("Permissão de administrador");
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
				if ((
						edtDescricao.getText().toString().equalsIgnoreCase("") ||
						edtQuantidadeParcelas.getText().toString().equalsIgnoreCase("") ||
						edtDiasParcelas.getText().toString().equalsIgnoreCase("") ||
						edtAcressimo.getText().toString().equalsIgnoreCase(""))) {
					MostrarCaixaNeutra("Campo Inválido", "Favor preencher todos os campos");
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
					MostrarCaixaCancelar("Cancelar Registro", "Sair da edição de formas de pagamento?");
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
				registro.Atualizar(
						txvCodigo.getText().toString(),
						edtDescricao.getText().toString(),
						edtQuantidadeParcelas.getText().toString(),
						edtDiasParcelas.getText().toString(),
						edtAcressimo.getText().toString().replaceAll("[%]", ""));
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
				registro.Deletar(Long.parseLong(id));
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
