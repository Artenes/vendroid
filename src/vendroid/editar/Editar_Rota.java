package vendroid.editar;

import java.text.SimpleDateFormat;
import java.util.Date;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Rotas;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Vendedor;
import vendroid.consultas.Consulta_Vendedor_falsa;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Editar_Rota extends Activity {

	TextView txvCodigo, txvData;
	EditText edtDescricao, edtVendedor;
	Button btnSalvar, btnCancelar, btnEditar, btnExcluir;
	Tabela_Rotas registro;
	Tabela_Vendedor tblVendedor;
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
		setContentView(R.layout.editar_rotas);
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		registro = new Tabela_Rotas(this);
		registro.open();
		
		tblVendedor = new Tabela_Vendedor(this);
		tblVendedor.open();
		
		AtribuicaoObjetos();
		Botoes();
		
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
		id = extras.getString("id_rota");
        
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
		edtDescricao.setEnabled(true);
		edtVendedor.setEnabled(true);
	}
	
	void BloqueiaCampos() {
		btnSalvar.setEnabled(false);
		edtDescricao.setEnabled(false);
		edtVendedor.setEnabled(false);
	}
	
	void PreencheCampos() {
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_rota");
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
		
        //Toast.makeText(getApplicationContext(), "codigo " + registro.Vendedor(id), Toast.LENGTH_SHORT).show();
        
        if (tblVendedor.VerificarVendedor(registro.Vendedor(id))) {
        	edtVendedor.setText(tblVendedor.Nome(registro.Vendedor(id)));
        } else {
        	edtVendedor.setText("Registro inexistente");
        }
        
		edtDescricao.setText(registro.Rota(id));
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.EditarRotas.txvData);
		txvCodigo = (TextView) findViewById(R.EditarRotas.txvCodigo);
		btnEditar = (Button) findViewById(R.EditarRotas.btnEditar);
		btnSalvar = (Button) findViewById(R.EditarRotas.btnSalvar);
		btnCancelar = (Button) findViewById(R.EditarRotas.btnCancelar);
		btnExcluir = (Button) findViewById(R.EditarRotas.btnExcluir);
		edtDescricao = (EditText) findViewById(R.EditarRotas.edtDescricao);
		edtVendedor = (EditText) findViewById(R.EditarRotas.edtVendedor);
		edtVendedor.setKeyListener(null);
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
				if ((edtDescricao.getText().toString().equalsIgnoreCase("")) || 
						edtVendedor.getText().toString().equalsIgnoreCase("")) {
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
					PreencheCampos();
					AtribuicaoObjetos();
					btnEditar.setEnabled(true);
					btnExcluir.setEnabled(true);
					SetState(0);
				} else {
					MostrarCaixaCancelar("Cancelar Registro", "Sair da edição de rotas?");
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
		
		edtVendedor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent consultaRotas = new Intent();
				consultaRotas.setClass(contexto, Consulta_Vendedor_falsa.class);
				startActivityForResult(consultaRotas, 666);
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
				
				int id_vendedor;
				
		        if (!((edtVendedor.getText().toString().equalsIgnoreCase("")) || 
		        		(edtVendedor.getText().toString().equalsIgnoreCase("Registro Inexistente")))) {
		        	id_vendedor = tblVendedor.RetornaID(edtVendedor.getText().toString());
		        } else {
		        	id_vendedor = 000000;
		        }
				
				registro.Atualizar(
						txvCodigo.getText().toString(), 
						edtDescricao.getText().toString(), 
						id_vendedor);
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
				tblVendedor.close();
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
				tblVendedor.close();
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
				String vendedor = data.getStringExtra("vendedor");
				edtVendedor.setText(vendedor);
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
		outState.putString("id", id);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblVendedor.close();
		tblAdministrador.close();
		tblUsuarioAtual.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}
