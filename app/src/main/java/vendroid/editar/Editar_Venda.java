//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.editar;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Contas_Receber;
import vendroid.bancodados.Tabela_Forma_Pagamento;
import vendroid.bancodados.Tabela_Itens_Vendas;
import vendroid.bancodados.Tabela_Produtos;
import vendroid.bancodados.Tabela_Usuario_Atual;
import vendroid.bancodados.Tabela_Vendas;
import vendroid.bancodados.Tabela_Vendedor;
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

import dipro.vendasandroid.Tela_Principal;

public class Editar_Venda extends Activity {

	TextView txvCodigo, txvData;
	EditText edtVendedor, edtCliente, edtFormaPagamento, edtDesconto, edtTotalVenda, edtAcressimo;
	Button btnVoltar, btnRelancarVenda, btnExcluir; 
	Tabela_Vendas registro;
	Tabela_Produtos tblProdutos;
	Tabela_Itens_Vendas tblItensVendas;
	Tabela_Contas_Receber tblContasReceber;
	Tabela_Vendedor tblVendedor;
	Tabela_Cliente tblCliente;
	Tabela_Usuario_Atual tblUsuarioAtual;
	Tabela_Forma_Pagamento tblFormaPagamento;
	Tabela_Administrador tblAdministrador;
	Tela_Principal n_acesso;
	
	Context contexto = this;
	String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_vendas);
		
		tblUsuarioAtual = new Tabela_Usuario_Atual(this);
		tblUsuarioAtual.open();
		
		tblVendedor = new Tabela_Vendedor(this);
		tblVendedor.open();
		
		tblCliente = new Tabela_Cliente(this);
		tblCliente.open();
		
		tblFormaPagamento = new Tabela_Forma_Pagamento(this);
		tblFormaPagamento.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		n_acesso = new Tela_Principal();
		
		registro = new Tabela_Vendas(this);
		registro.open();
		
		tblProdutos = new Tabela_Produtos(this);
		tblProdutos.open();
		
		tblItensVendas = new Tabela_Itens_Vendas(this);
		tblItensVendas.open();
		
		tblContasReceber = new Tabela_Contas_Receber(this);
		tblContasReceber.open();
		
		
		AtribuicaoObjetos();
		Botoes();
		PreencherCampos();
		BloqueiaCampos();
	}
	
	public void BloqueiaCampos () {
		edtVendedor.setEnabled(false);
		edtCliente.setEnabled(false); 
		edtFormaPagamento.setEnabled(false);
		edtDesconto.setEnabled(false); 
		edtTotalVenda.setEnabled(false); 
		edtAcressimo.setEnabled(false);
	}
	
	public void LiberaCampos () {
		edtVendedor.setEnabled(true);
		edtCliente.setEnabled(true); 
		edtFormaPagamento.setEnabled(true);
		edtDesconto.setEnabled(true); 
		edtTotalVenda.setEnabled(true); 
		edtAcressimo.setEnabled(true);
	}
	
	public void PreencherCampos () {
		
		Bundle extras = getIntent().getExtras();
		id = extras.getString("id_venda");
		txvCodigo.setText(id);
		
        txvData.setText(registro.DataBR(id));
		
        String temporario;
        try {
			edtVendedor.setText(tblVendedor.Nome_por_vendedor(registro.Vendedor(id)));
		} catch (Exception e) {
			edtVendedor.setText("Registro inexistente");
		}
        
        if (registro.Cliente(id).equalsIgnoreCase("A Vista")) {
        	edtCliente.setText("A Vista");
        } else {
        	try {
    			edtCliente.setText(tblCliente.Nome_por_Cliente(registro.Cliente(id)));
    		} catch (Exception e) {
    			edtCliente.setText("Registro inexistente");
    		} 
        }
		
		if (registro.FormaPagamento(id).equalsIgnoreCase("A Vista")) {
			edtFormaPagamento.setText("A Vista");
		} else {
			try {
				edtFormaPagamento.setText(tblFormaPagamento.Descricao_por_FormaPagamento(registro.FormaPagamento(id)));
			} catch (Exception e) {
				edtFormaPagamento.setText("Registro inexistente");
			}
		}
		
		
        temporario = registro.Desconto(id);
		if (temporario.contains(".")) {
			edtDesconto.setText("R$" + registro.Desconto(id));
		} else {
			edtDesconto.setText("R$" + registro.Desconto(id) + ".00");
		}
		
		//Toast.makeText(getApplicationContext(), "acressimo: " + registro.Acressimo(id), Toast.LENGTH_SHORT).show();
		temporario = registro.Acressimo(id);
		if (temporario.contains(".")) {
			edtAcressimo.setText(registro.Acressimo(id) + "%");
		} else {
			edtAcressimo.setText(registro.Acressimo(id) + ".00" + "%");
		}
		
		temporario = registro.TotalVenda(id);
		if (temporario.contains(".")) {
			edtTotalVenda.setText("R$" + registro.TotalVenda(id));
		} else {
			edtTotalVenda.setText("R$" + registro.TotalVenda(id) + ".00");
		}
	}
	
	public void AtribuicaoObjetos() {
		txvData = (TextView) findViewById(R.id.EditarVenda_txvData);
		txvCodigo = (TextView) findViewById(R.id.EditarVenda_txvCodigo);
		btnVoltar = (Button) findViewById(R.id.EditarVenda_btnVoltar);
		btnRelancarVenda = (Button) findViewById(R.id.EditarVenda_btnRelancarVenda);
		btnExcluir = (Button) findViewById(R.id.EditarVenda_btnExcluir);
		edtVendedor = (EditText) findViewById(R.id.EditarVenda_edtVendedor);
		edtCliente = (EditText) findViewById(R.id.EditarVenda_edtCliente);
		edtFormaPagamento = (EditText) findViewById(R.id.EditarVenda_edtFormaPagamento);
		edtDesconto = (EditText) findViewById(R.id.EditarVenda_edtDesconto);
		edtTotalVenda = (EditText) findViewById(R.id.EditarVenda_edtTotalVenda);
		edtAcressimo = (EditText) findViewById(R.id.EditarVenda_edtAcressimo);
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
		
		btnRelancarVenda.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sim.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
						if (password.moveToFirst()) {
							Intent i = new Intent(getApplicationContext(), vendroid.vendas.Venda.class);
							i.putExtra("CodigoVenda", txvCodigo.getText().toString());
							registro.close();
							startActivity(i);
							senha.setText("");
							dialog.dismiss();
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
				
				if (tblUsuarioAtual.NivelAcesso("1") == 3 || tblUsuarioAtual.NivelAcesso("1") == 2) {
					dialog.show();
				} else {
					Intent i = new Intent(getApplicationContext(), vendroid.vendas.Venda.class);
					i.putExtra("CodigoVenda", txvCodigo.getText().toString());
					registro.close();
					startActivity(i);
					finish();
				} 
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
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				MostrarCaixaCancelar("Cancelar Registro?", "Deseja sair da edição de vendas?");
			}
		});
		
		
	}
	
	public void MostrarCaixaExcluir (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				/*Primeiro verifico se há algum registro no BD com o id da venda
				 * pois se fizesse direto, daria erro caso não existisse um resgistro para ser deletado
				 * */
				Cursor conta_receber = tblContasReceber.BuscaID(txvCodigo.getText().toString());
				if (conta_receber.moveToFirst()) {
					tblContasReceber.Deletar_Todos(txvCodigo.getText().toString());
				}
				
				int contador = 1;
				//Faço isso pois acho que toda vez que o loop for refeito, a quantidade de itens será atualizada
				//Já que estou deletando um registro por vez ao final de cada loop
				//Eu tava fazendo tipo assim:
				//while (contador <= tblItensVendas.QunatidadeItensVenda(txvCodigo.getText().toString())) {...}
				int qtd_itens = tblItensVendas.QunatidadeItensVenda(txvCodigo.getText().toString());
				
				//Aqui pego todos os produtos que possuam o código da venda 
				Cursor codigo_produto = tblItensVendas.ID(txvCodigo.getText().toString());
				codigo_produto.moveToFirst();
				
				while (contador <= qtd_itens) {
					//Primeiro atualizo o estoque, somando a quantidade vendida com a quantidade que está no estoque
					tblProdutos.AtualizarEstoque(
							Integer.parseInt(tblItensVendas.Quantidade(codigo_produto.getString(contador - 1))) + 
							Integer.parseInt(tblProdutos.EstoqueAtual(tblItensVendas.Produto(codigo_produto.getString(contador - 1)))),
							tblItensVendas.Produto(codigo_produto.getString(contador - 1)));
					
					//Então deleto o produto da tabela Itens Vendas
					tblItensVendas.Deletar(codigo_produto.getLong(contador - 1));
					
					contador++;
				}
				
				//Por fim deleto o próprio registro da venda e encerro a Activity
				registro.Deletar(txvCodigo.getText().toString());
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		registro.close();
		tblContasReceber.close();
		tblItensVendas.close();
		tblProdutos.close();
		tblAdministrador.close();
		tblFormaPagamento.close();
		tblVendedor.close();
		tblUsuarioAtual.close();
	}
	
	@Override
	public void onBackPressed() {}
	
}
