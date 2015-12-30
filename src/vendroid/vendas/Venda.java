//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();

package vendroid.vendas;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import vendroid.bancodados.Tabela_Administrador;
import vendroid.bancodados.Tabela_Cliente;
import vendroid.bancodados.Tabela_Codigos;
import vendroid.bancodados.Tabela_Contas_Receber;
import vendroid.bancodados.Tabela_Forma_Pagamento;
import vendroid.bancodados.Tabela_Itens_Vendas;
import vendroid.bancodados.Tabela_Produtos;
import vendroid.bancodados.Tabela_Vendas;
import vendroid.bancodados.Tabela_Vendedor;
import vendroid.consultas.Consulta_Clientes_Falsa;
import vendroid.consultas.Consulta_Formas_Pagamento_Falsa;
import vendroid.consultas.Consulta_Produtos_Falsa;
import vendroid.consultas.Consulta_Vendedor_falsa;
import dipro.vendasandroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Venda extends Activity {
	
	//Tabelas usadas na classe
	Tabela_Produtos tblProdutos;
	Tabela_Vendas tblVendas;
	Tabela_Codigos tblCodigos;
	Tabela_Forma_Pagamento tblFormaPagamento;
	Tabela_Contas_Receber tblContasReceber;
	Tabela_Administrador tblAdministrador;
	Tabela_Itens_Vendas tblItensVendas;
	Tabela_Vendedor tblVendedor;
	Tabela_Cliente tblCliente;
	//Tabelas usadas na classe
	
	//Views usadas nesta classe
	EditText edtVendedor, edtFormaPagamento, edtCliente, edtCodigo, edtProduto, edtPreco, edtQuantidade, edtDesconto,
	edtTotal, edtValorBruto, edtDescontoGeral, edtTotalLiquido, senha;
	Button btnLancarProduto, btnExcluirProduto, btnFinalizarVenda, btnCancelarVenda;
	Spinner spnPlanoPagamento;
	ListView listview;
	//Views usadas nesta classe
	
	//Variável para pegar contexto da classe
	Context contexto = this;
	
	//A solução aqui é usar variáveis globais para controlar os valores que estão correndo pela classe
	//Essas variáveis ajudam a fazer os cálculos durante o lançamento de um produto
	float preco_do_produto = 0;
	int quantidade_do_produto = 0;
	float desconto_do_produto = 0;
	float total_do_produto = 0;
	float total_do_produto_temporario = 0;
	
	//Essas carregam os valores finais da venda
	float valor_bruto_da_venda = 0;
	float desconto_geral_da_venda = 0;
	float total_liquido_da_venda = 0; 
	float total_liquido_final = 0; //Este apresenta o valor final somente com desconto
	float novo_total_da_venda = 0; //Este apresenta o valor total já com desconto e acréssimo
	
	//Essas servem para guardar alguns valores usados mais de uma vez
	int codigo_venda = 0;
	String data_venda = "", data_venda_br = "";
	String id_venda = "";//Este é usado para o caso da revenda
	boolean revenda = false;
	
	//Máscara para deixar números com duas casas decimais ou para serem arredondados por completo
	final DecimalFormat df = new DecimalFormat("###.##");
	
	boolean estado_desconto_geral = false;//Define se o desconto geral está liberado ou não
	
	private int on_product = 0; //Definie se um produto está sendo lançado (1) ou não (0) 
	private float resultado = 0; //Ajuda no calculo do total de um produto
	private float produto_bruto = 0; //Ajuda no calculo do valor bruto geral da compara
	static int ESTADO_INICIAL = 1; //Ajuda a definir a liberação ou bloqueio de campos durante rotação de tela
	static int ESTADO_FINAL = 2; //Ajuda a definir a liberação ou bloqueio de campos durante rotação de tela
	int estado_atual = ESTADO_INICIAL; //Ajuda a definir a liberação ou bloqueio de campos durante rotação de tela
	private int indice_produto; //Ajuda a definir qual produto está sendo editado na listview
	private float valor_bruto_indice_produto;
	private float valor_liquido_indice_produto;
	boolean esta_editando;
	private int desconto [] = new int [1000];
	
	private SimpleAdapter adapter;
	private ArrayList<HashMap<String, String>> list;
	private ArrayList<HashMap<String, String>> lista  = new ArrayList<HashMap<String,String>>();
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle State) {
		super.onCreate(State);
		setContentView(R.layout.vendas);
		
		tblProdutos = new Tabela_Produtos(this);
		tblProdutos.open();
		
		tblVendas = new Tabela_Vendas(this);
		tblVendas.open();
		
		tblFormaPagamento = new Tabela_Forma_Pagamento(this);
		tblFormaPagamento.open();
		
		tblContasReceber = new Tabela_Contas_Receber(this);
		tblContasReceber.open();
		
		tblCodigos = new Tabela_Codigos(this);
		tblCodigos.open();
		
		tblAdministrador = new Tabela_Administrador(this);
		tblAdministrador.open();
		
		tblItensVendas = new Tabela_Itens_Vendas(this);
		tblItensVendas.open();
		
		tblVendedor = new Tabela_Vendedor(this);
		tblVendedor.open();
		
		tblCliente = new Tabela_Cliente(this);
		tblCliente.open();
		
		AtribuicaoObjetos();
		BloqueioInicial();
		
		//Código para atribuir os itens para a listview quando a tela mudar de sentido
		lista = (ArrayList<HashMap<String,String>>) getLastNonConfigurationInstance();
		list = new ArrayList<HashMap<String,String>>();
		
		if (lista != null) {
			list = lista;
		}
		
		adapter = new SimpleAdapter(contexto, list,
				R.layout.vendas_linhas,
				new String[] {"produto", "desconto", "valor", "qtd", "total"},
				new int[] {R.VendaLinha.txvProduto, R.VendaLinha.txvDesconto, R.VendaLinha.txvValor,
							R.VendaLinha.txvQtd, R.VendaLinha.txvTotal});
		listview.setAdapter(adapter);
		//Código para atribuir os itens para a listview quando a tela mudar de sentido
		
		//Isso irá verificar se a tela mudou de sentido, caso mude, as variaveis terão quer receber seus valores de anteriormente
		//Pois sabemos que quando a tela vira, tudo é reinicializado
		if (State != null) {
			
			switch (State.getInt("puta")) {
			case 1:
				SetProduct(0);
				break;
				
			case 2:
				LiberacaoInicial();
				DefineEstado(ESTADO_FINAL);
				break;
			}
			
			switch (State.getInt("produto")) {
			case 0:
				SetProduct(0);
				break;
			case 1:
				btnLancarProduto.setEnabled(true);
				edtQuantidade.setEnabled(true);
				edtDesconto.setEnabled(true);
				SetProduct(1);
			}
			
			esta_editando = State.getBoolean("esta_editando");
			valor_bruto_indice_produto = State.getFloat("valor_bruto_indice_produto");
			valor_liquido_indice_produto = State.getFloat("valor_liquido_indice_produto");
			indice_produto = State.getInt("indice_produto");
			resultado = State.getFloat("resultado");
			produto_bruto = State.getFloat("produto_bruto");
			preco_do_produto = State.getFloat("preco_do_produto");
			quantidade_do_produto = State.getInt("quantidade_do_produto");
			desconto_do_produto = State.getFloat("desconto_do_produto");
			total_do_produto = State.getFloat("total_do_produto");
			total_do_produto_temporario = State.getFloat("total_do_produto_temporario");
			valor_bruto_da_venda = State.getFloat("valor_bruto_da_venda");
			desconto_geral_da_venda = State.getFloat("desconto_geral_da_venda");
			total_liquido_da_venda = State.getFloat("total_liquido_da_venda");
			total_liquido_final = State.getFloat("total_liquido_final");
			novo_total_da_venda = State.getFloat("novo_total_da_venda");
			codigo_venda = State.getInt("codigo_venda");
			data_venda = State.getString("data_venda");
			id_venda = State.getString("id_venda");
			revenda = State.getBoolean("revenda");
			estado_desconto_geral = State.getBoolean("estado_desconto_geral");
			desconto = State.getIntArray("desconto");
			
		}
		
		
		Botoes();
		CalculoProdutoTotal();
		ListaProdutos();
		
		//Isso irá verificar se a venda que está sendo efetuada é uma revenda ou não
		Bundle extras = getIntent().getExtras();	
		if ((extras != null) && !(revenda)) {
			id_venda = extras.getString("CodigoVenda");
			revenda = true;
			ReVenda();
		}		
	}
	
	//Esta ajuda a definir aquilo que chamo de "estado";
	private void DefineEstado (int estado) {
		estado_atual = estado;
	}
	private int PegaEstado () {
		return estado_atual;
	}
	
	//Serve para retornar os valores que existem dentro da listview com os produtos
	private ArrayList<HashMap<String, String>> GetArray () {
		return list;
	}
	
	//Serve para definir se um produto está sendo lançado ou não
	void SetProduct (int prod) {
		on_product = prod;
	}
	int GetProduct () {
		return on_product;
	}
	
	//Este é um pequeno truque da net para fazer um listview rolar dentro de uma scroll view
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	//Essas são três funções para retornar valores de variáveis e objetos para reinicia-las após um virada de tela
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1: 
			if (resultCode == RESULT_OK) {
				String vendedor = data.getStringExtra("vendedor");
				edtVendedor.setText(vendedor);
				if (!(edtVendedor.getText().toString().equalsIgnoreCase(""))) {
					edtFormaPagamento.setEnabled(true);
					edtCliente.setEnabled(true);
					spnPlanoPagamento.setEnabled(true);
					edtCodigo.setEnabled(true);
					edtProduto.setEnabled(true);
					btnFinalizarVenda.setEnabled(true);
				} 
				DefineEstado(ESTADO_FINAL);
				break;
			}
			
		case 2:
			if (resultCode == RESULT_OK) {
				String formapagamento = data.getStringExtra("formapagamento");
				edtFormaPagamento.setText(formapagamento);
				break;
			}
			
		case 3:
			if (resultCode == RESULT_OK) {
				String cliente = data.getStringExtra("cliente");
				edtCliente.setText(cliente);
				break;
			}
			
		case 4:
			if (resultCode == RESULT_OK) {
				esta_editando = false;
				SetProduct(1);
				String preco;
				edtQuantidade.setEnabled(true);
				edtDesconto.setEnabled(true);
				btnLancarProduto.setEnabled(true);
				String id = data.getStringExtra("produto_codigo");
				edtCodigo.setText(id);
				edtProduto.setText(tblProdutos.Descricao(id));
				if (edtFormaPagamento.getText().toString().equalsIgnoreCase("A Vista")) {
					edtPreco.setText(tblProdutos.PrecoVista(id).replaceAll("[R$.]", "").replaceAll("[,]", "."));
					preco = tblProdutos.PrecoVista(id);
					edtTotal.setText(preco.replaceAll("[R$.]", "").replaceAll("[,]", "."));
				} else {
					preco = tblProdutos.PrecoPrazo(id);
					edtPreco.setText(tblProdutos.PrecoPrazo(id).replaceAll("[R$.]", "").replaceAll("[,]", "."));
					edtTotal.setText(preco.replaceAll("[R$.]", "").replaceAll("[,]", "."));
				}
				edtDesconto.setText("0");
				edtQuantidade.setText("1");
				resultado = Float.parseFloat(preco.replaceAll("[R$.]", "").replaceAll("[,]", "."));
				break;
			}
		default:
			SetProduct(0);
		}
	}	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return GetArray();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (PegaEstado() == ESTADO_INICIAL) {
			SetProduct(0);
		}
		outState.putInt("puta", PegaEstado());
		outState.putInt("produto", on_product);
		outState.putFloat("resultado", resultado);
		outState.putFloat("produto_bruto", produto_bruto);
		outState.putInt("indice_produto", indice_produto);
		outState.putFloat("valor_bruto_indice_produto", valor_bruto_indice_produto);
		outState.putFloat("valor_liquido_indice_produto", valor_liquido_indice_produto);
		outState.putBoolean("esta_editando", esta_editando);
		outState.putFloat("preco_do_produto", preco_do_produto);
		outState.putInt("quantidade_do_produto", quantidade_do_produto);
		outState.putFloat("desconto_do_produto", desconto_do_produto);
		outState.putFloat("total_do_produto", total_do_produto);
		outState.putFloat("total_do_produto_temporario", total_do_produto_temporario);
		outState.putFloat("valor_bruto_da_venda", valor_bruto_da_venda);
		outState.putFloat("desconto_geral_da_venda", desconto_geral_da_venda);
		outState.putFloat("total_liquido_da_venda", total_liquido_da_venda);
		outState.putFloat("total_liquido_final", total_liquido_final);
		outState.putFloat("novo_total_da_venda", novo_total_da_venda);
		outState.putInt("codigo_venda", codigo_venda);
		outState.putString("data_venda", data_venda);
		outState.putString("id_venda", id_venda);
		outState.putBoolean("revenda", revenda);
		outState.putBoolean("estado_desconto_geral", estado_desconto_geral);
		outState.putIntArray("desconto", desconto);
		
	}
	
	//Bloqueia os campos principais
	private void BloqueioInicial () {
			
			edtFormaPagamento.setEnabled(false);
			edtCliente.setEnabled(false);
			spnPlanoPagamento.setEnabled(false);
			edtCodigo.setEnabled(false);
			edtProduto.setEnabled(false);
			edtQuantidade.setEnabled(false);
			edtDesconto.setEnabled(false);
			btnLancarProduto.setEnabled(false);
			btnExcluirProduto.setEnabled(false);			
			btnFinalizarVenda.setEnabled(false);
			
		}
		
	//Libera os campos principais
	private void LiberacaoInicial () {
			
			
			edtFormaPagamento.setEnabled(true);
			edtCliente.setEnabled(true);
			spnPlanoPagamento.setEnabled(true);
			edtCodigo.setEnabled(true);
			edtProduto.setEnabled(true);
			btnFinalizarVenda.setEnabled(true);
			
		}
		
	//Atribui os objetos às suas respectivas views
	private void AtribuicaoObjetos () {
			edtValorBruto = (EditText) findViewById(R.Venda.edtValorBruto);
			edtValorBruto.setEnabled(false);
			edtVendedor = (EditText) findViewById(R.Venda.edtVendedor);
			edtVendedor.setKeyListener(null);
			edtFormaPagamento = (EditText) findViewById(R.Venda.edtFormaPagamento);
			edtFormaPagamento.setKeyListener(null);
			edtCliente = (EditText) findViewById(R.Venda.edtCliente);
			edtCliente.setKeyListener(null);
			edtCodigo = (EditText) findViewById(R.Venda.edtCodigo);
			edtCodigo.setKeyListener(null);
			edtProduto = (EditText) findViewById(R.Venda.edtProduto);
			edtProduto.setKeyListener(null);
			edtPreco = (EditText) findViewById(R.Venda.edtPreco);
			edtPreco.setEnabled(false);
			edtQuantidade = (EditText) findViewById(R.Venda.edtQuantidade);
			edtDesconto = (EditText) findViewById(R.Venda.edtDesconto);
			edtTotal = (EditText) findViewById(R.Venda.edtTotal);
			edtTotal.setEnabled(false);
			edtTotalLiquido = (EditText) findViewById(R.Venda.edtTotalLiquido);
			edtTotalLiquido.setEnabled(false);
			btnLancarProduto = (Button) findViewById(R.Venda.btnLancarProduto);
			btnExcluirProduto = (Button) findViewById(R.Venda.btnExcluirProduto);
			btnFinalizarVenda = (Button) findViewById(R.Venda.btnFinalizarVenda);
			btnCancelarVenda = (Button) findViewById(R.Venda.btnCancelarVenda);
			spnPlanoPagamento = (Spinner) findViewById(R.Venda.spnPlanoPagamento);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.PlanoPagamento, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnPlanoPagamento.setAdapter(adapter);
			spnPlanoPagamento.setSelection(adapter.getPosition("Dinheiro"));
			listview = (ListView) findViewById(R.Venda.listview);
		}
	
	//Três funções que servem para controlar a funcionalidade de caixas de diálogo
	public void MostrarCaixaNeutra (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setNeutralButton("OK", null);
		Alert.show();
	}
	public void MostrarCaixaExcluir (String titulo, String mensagem) {
		AlertDialog.Builder Alert = new AlertDialog.Builder(Venda.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				desconto[indice_produto] = 0;
				list.remove(indice_produto);
				listview.refreshDrawableState();
				adapter.notifyDataSetChanged();
				
				setListViewHeightBasedOnChildren(listview);
				
				edtCodigo.setText("");
				edtProduto.setText("");
				edtPreco.setText("");
				edtQuantidade.setText("");
				edtQuantidade.setEnabled(false);
				edtDesconto.setText("");
				edtDesconto.setEnabled(false);
				edtTotal.setText("");
				btnLancarProduto.setEnabled(false);
				btnExcluirProduto.setEnabled(false);
				
				float valor_bruto = Float.parseFloat(edtValorBruto.getText().toString());
				float valor_liquido = Float.parseFloat(edtTotalLiquido.getText().toString());
				
				edtValorBruto.setText(df.format(valor_bruto - valor_bruto_indice_produto).replaceAll("[,]", "."));
				edtTotalLiquido.setText(df.format(valor_liquido - valor_liquido_indice_produto).replaceAll("[,]", "."));
				
				if (!(edtValorBruto.getText().toString().contains("."))) {
					edtValorBruto.setText(edtValorBruto.getText().toString() + ".00");
				}
				if (!(edtTotalLiquido.getText().toString().contains("."))) {
					edtTotalLiquido.setText(edtTotalLiquido.getText().toString() + ".00");
				}
				
				valor_bruto_da_venda = (valor_bruto - valor_bruto_indice_produto);
				total_liquido_da_venda = (valor_liquido - valor_liquido_indice_produto);
				
				esta_editando = false;
				
				int indice_vetor = 0;
				int x = 0;
				int y = 1;
				
				//Rearanja os valores dentro do vetor - ou seja: o espaço deixado vazio pelo valor deletado terá de ser ocupado
				//pelo valor seguinte assim gerando um evento onde todos os valores em seguida devem ser puxados um posição a menos no vetor
				do {
					desconto [indice_produto + x] = desconto [indice_produto + y]; 
					x++;
					y++;
					indice_vetor++;
				} while (desconto[indice_vetor] != 0 && indice_vetor <= 995);
				
				for (int i = 0; i <= 997; i++ ) {
					if (desconto[i] == 1) {
						estado_desconto_geral = false;
						break;
					} else {
						estado_desconto_geral = true;
					}
				}
				
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
		AlertDialog.Builder Alert = new AlertDialog.Builder(Venda.this);
		Alert.setTitle(titulo);
		Alert.setMessage(mensagem);
		Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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
	
	//Função usada para calcular valores de edittexts que estão com textwatcher - serve para fazer mudanças de valores dinâmicamente
	private void CalculoProdutoTotal () {
		
		edtQuantidade.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
				boolean produto_vazio = edtProduto.getText().toString().equalsIgnoreCase("");
				boolean quantidade_vazia = edtQuantidade.getText().toString().equalsIgnoreCase("");
				boolean desconto_vazio = edtDesconto.getText().toString().equalsIgnoreCase("");
				
				if (produto_vazio) {
					return; //caso não tenha nenhum produto, não vaz nada, pois se vizer da erro
				} else {
					
					if (quantidade_vazia){
						preco_do_produto = Float.parseFloat(edtPreco.getText().toString());
						quantidade_do_produto = 1;
						total_do_produto = preco_do_produto * quantidade_do_produto; 
						edtTotal.setText(df.format(total_do_produto).replaceAll("[,]", "."));
						valor_bruto_da_venda = total_do_produto;
						
						if (desconto_vazio || (desconto_do_produto <= 0.0)) {
							return;
						} else {
							desconto_do_produto = Float.parseFloat(edtDesconto.getText().toString().replaceAll("[%]", ""))/100;	
							edtTotal.setText(df.format(total_do_produto - (desconto_do_produto * total_do_produto)).replaceAll("[,]", "."));
							if (!edtTotal.getText().toString().contains(".")) {
								edtTotal.setText(edtTotal.getText().toString() + ".00");
							}
						}
					
						return;
						
					} else {
						preco_do_produto = Float.parseFloat(edtPreco.getText().toString());
						quantidade_do_produto = Integer.parseInt(edtQuantidade.getText().toString());
						total_do_produto = preco_do_produto * quantidade_do_produto;
						edtTotal.setText(df.format(total_do_produto).replaceAll("[,]", "."));
						if (!edtTotal.getText().toString().contains(".")) {
							edtTotal.setText(edtTotal.getText().toString() + ".00");
						}
						valor_bruto_da_venda = total_do_produto;
						
						if (desconto_vazio || (desconto_do_produto <= 0.0)) {
							return;
						} else {
							desconto_do_produto = Float.parseFloat(edtDesconto.getText().toString().replaceAll("[%]", ""))/100;
							edtTotal.setText(df.format(total_do_produto - (desconto_do_produto * total_do_produto)).replaceAll("[,]", "."));
							if (!edtTotal.getText().toString().contains(".")) {
								edtTotal.setText(edtTotal.getText().toString() + ".00");
							}
						}
					}
				}
				
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} public void afterTextChanged(Editable arg0) {}
		});
		
		edtDesconto.addTextChangedListener(new TextWatcher() {
			
			private String atual = "";
			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
				//Mascara de porcentagem
				if (!(arg0.length() > 8)) {
					if (!(arg0.toString().equalsIgnoreCase(""))) {
						if(!(arg0.toString().equals(atual))) {
							edtDesconto.removeTextChangedListener(this);
							String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
							double convertido = 0;
							try {
								convertido = Double.parseDouble(stringlimpa);
							} catch (NumberFormatException e) {
								edtDesconto.setSelection(arg0.length());
							}
							String formato = NumberFormat.getCurrencyInstance().format((convertido/100));			
							atual = formato;
							edtDesconto.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
							edtDesconto.setSelection(formato.length());
							edtDesconto.addTextChangedListener(this);
						}
						} else {
							return;
							}
					} else {
						return;
					}
				//Mascara de porcentagem				
				
				boolean produto_vazio = edtProduto.getText().toString().equalsIgnoreCase("");
				boolean desconto_vazio = edtDesconto.getText().toString().equalsIgnoreCase("");
				
				//Calculo
				if (produto_vazio) {
					
					return;
					
				} else {
					
					if (desconto_vazio || ((Float.parseFloat(edtDesconto.getText().toString().replaceAll("[%]", ""))) <= (0.0))){
						total_do_produto_temporario = total_do_produto;
						edtTotal.setText(df.format(total_do_produto_temporario).replaceAll("[,]", "."));
						if (!edtTotal.getText().toString().contains(".")) {
							edtTotal.setText(edtTotal.getText().toString() + ".00");
						}
						return;
					} else { 
						desconto_do_produto = Float.parseFloat(edtDesconto.getText().toString().replaceAll("[%]", ""))/100;
						total_do_produto_temporario = total_do_produto - (desconto_do_produto * total_do_produto);
						edtTotal.setText(df.format(total_do_produto_temporario).replaceAll("[,]", "."));
						if (!edtTotal.getText().toString().contains(".")) {
							edtTotal.setText(edtTotal.getText().toString() + ".00");
						}
					}
				}
				
				if (Float.parseFloat(arg0.toString().replaceAll("[%]", "")) >= 100) {
					edtDesconto.setText("0");
				}
				//Calculo
				
				
			}
			
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} public void afterTextChanged(Editable arg0) {}
		});
	}

	//Função para lançar um produto na venda	
	private void LancarProduto () {
		
		String produto = edtProduto.getText().toString();
		String desconto = edtDesconto.getText().toString();
		String valor = edtPreco.getText().toString();
		String qtd = edtQuantidade.getText().toString();
		String total = edtTotal.getText().toString();
		
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("produto", produto);
		hMap.put("valor", valor);
		hMap.put("desconto", desconto);
		hMap.put("qtd", qtd);
		hMap.put("total", total);
		list.add(hMap);
		listview.refreshDrawableState();
		adapter.notifyDataSetChanged();
		
		edtCodigo.setText("");
		edtProduto.setText("");
		edtPreco.setText("");
		edtQuantidade.setText("");
		edtQuantidade.setEnabled(false);
		edtDesconto.setText("");
		edtDesconto.setEnabled(false);
		edtTotal.setText("");
		btnLancarProduto.setEnabled(false);
		btnExcluirProduto.setEnabled(false);
		
		if (edtValorBruto.getText().toString().equalsIgnoreCase("") ||
				edtTotalLiquido.getText().toString().equalsIgnoreCase(""))
		{
			edtValorBruto.setText("0.0");
			edtTotalLiquido.setText("0.0");
		}
		
		float valor_bruto;
		float total_liquido;
		float novo_total_liquido;
		float novo_total_bruto;
		
		
		
			valor_bruto = Float.parseFloat(edtValorBruto.getText().toString());
			total_liquido = Float.parseFloat(edtTotalLiquido.getText().toString());
		
			novo_total_bruto = valor_bruto + ((Float.parseFloat(valor)) * (Float.parseFloat(qtd))); 
			novo_total_liquido = total_liquido + Float.parseFloat(total);
				
			edtValorBruto.setText(df.format(novo_total_bruto).replaceAll("[,]", "."));
			edtTotalLiquido.setText(df.format(novo_total_liquido).replaceAll("[,]", "."));
			
			if (!(edtValorBruto.getText().toString().contains("."))) {
				edtValorBruto.setText(edtValorBruto.getText().toString() + ".00");
			}
			if (!(edtTotalLiquido.getText().toString().contains("."))) {
				edtTotalLiquido.setText(edtTotalLiquido.getText().toString() + ".00");
			}
			
			btnFinalizarVenda.setEnabled(true);
			SetProduct(0);
			esta_editando = false;
			
			valor_bruto_da_venda = novo_total_bruto;
			total_liquido_da_venda = novo_total_liquido;
			
			quantidade_do_produto = 0;
			desconto_do_produto = 0;
			total_do_produto = 0;
			
		}
	
	//Função para pegar um produto da listview para edita-lo ou exlui-lo
	private void ListaProdutos () {
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 
				 SetProduct(1); //Defino que estamos no estado de edição de produto
				 esta_editando = true;
				 indice_produto = arg2;
				 
				 Cursor codigo = tblProdutos.BuscapProduto(((TextView)arg1.findViewById(R.VendaLinha.txvProduto)).getText().toString());; 
				 codigo.moveToFirst();
				 
				 final String cod = codigo.getString(codigo.getColumnIndex("_id"));
				 final String produto = (String) ((TextView)arg1.findViewById(R.VendaLinha.txvProduto)).getText();
				 final String preco = (String) ((TextView)arg1.findViewById(R.VendaLinha.txvValor)).getText();
				 final String qtd = (String) ((TextView)arg1.findViewById(R.VendaLinha.txvQtd)).getText();
				 final String desc = (String) ((TextView)arg1.findViewById(R.VendaLinha.txvDesconto)).getText();
				 final String total = (String) ((TextView)arg1.findViewById(R.VendaLinha.txvTotal)).getText();
				 
				 valor_bruto_indice_produto = Float.parseFloat(preco)*Float.parseFloat(qtd);
				 valor_liquido_indice_produto = Float.parseFloat(total);
				 quantidade_do_produto = Integer.parseInt(qtd);
				 desconto_do_produto = Float.parseFloat(desc.replaceAll("%", ""));
				 total_do_produto = Float.parseFloat(total);
				 
				 edtCodigo.setText(cod);
				 edtProduto.setText(produto);
				 edtPreco.setText(preco);
				 edtQuantidade.setText(qtd);
				 edtDesconto.setText(desc);
				 edtTotal.setText(total);
				 
				 btnLancarProduto.setEnabled(true);
				 btnExcluirProduto.setEnabled(true);
				 edtQuantidade.setEnabled(true);
				 edtDesconto.setEnabled(true);
				 
				 //Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
				 
			 }
		});
	}
	
	//Função que guarda todos os botões na hora da venda
	private void Botoes () {
		
		edtCliente.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				edtCliente.setText("A Vista");
				return true;
			}
		});
		
		edtFormaPagamento.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View arg0) {
				AlertDialog.Builder Alert = new AlertDialog.Builder(Venda.this);
				Alert.setTitle("Atenção");
				Alert.setMessage("Mudar a Forma de Pagamento irá excluir todos os produtos já lançados. Deseja continuar?");
				Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						for (int u = 0; u <= list.size(); u++) {
							list.remove(u);
							listview.refreshDrawableState();
							adapter.notifyDataSetChanged();
						}
						valor_bruto_da_venda = 0;
						total_liquido_da_venda = 0;
						quantidade_do_produto = 0;
						desconto_do_produto = 0;
						edtValorBruto.setText("0.00");
						edtTotalLiquido.setText("0.00");
						edtFormaPagamento.setText("A Vista");
						
					}
				});
				Alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				if (list.size() <= 0) {
					edtFormaPagamento.setText("A Vista");
				} else {
					Alert.show();
				}
				
				return true;
			}
		});
		
		edtFormaPagamento.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				AlertDialog.Builder Alert = new AlertDialog.Builder(Venda.this);
				Alert.setTitle("Atenção");
				Alert.setMessage("Mudar a Forma de Pagamento irá excluir todos os produtos já lançados. Deseja continuar?");
				Alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						for (int u = 0; u <= list.size(); u++) {
							list.remove(u);
							listview.refreshDrawableState();
							adapter.notifyDataSetChanged();
						}
						valor_bruto_da_venda = 0;
						total_liquido_da_venda = 0;
						quantidade_do_produto = 0;
						desconto_do_produto = 0;
						edtValorBruto.setText("0.00");
						edtTotalLiquido.setText("0.00");
						Intent vendedor = new Intent();
						vendedor.setClass(contexto, Consulta_Formas_Pagamento_Falsa.class);
						startActivityForResult(vendedor, 2);
					}
				});
				Alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				if (list.size() <= 0) {
					Intent vendedor = new Intent();
					vendedor.setClass(contexto, Consulta_Formas_Pagamento_Falsa.class);
					startActivityForResult(vendedor, 2);
				} else {
					Alert.show();
				}
			}
		});
		
		edtVendedor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent vendedor = new Intent();
				vendedor.setClass(contexto, Consulta_Vendedor_falsa.class);
				startActivityForResult(vendedor, 1);
			}
		});
		
		edtCliente.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent vendedor = new Intent();
				vendedor.setClass(contexto, Consulta_Clientes_Falsa.class);
				startActivityForResult(vendedor, 3);
			}
		});
		
		edtCodigo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent vendedor = new Intent();
				vendedor.setClass(contexto, Consulta_Produtos_Falsa.class);
				startActivityForResult(vendedor, 4);
			}
		});
		
		edtProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent vendedor = new Intent();
				vendedor.setClass(contexto, Consulta_Produtos_Falsa.class);
				startActivityForResult(vendedor, 4);
			}
		});
		
		btnLancarProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				//Verifica se o campo de quantidade ou o de desconto estão vazios
				if (edtQuantidade.getText().toString().equalsIgnoreCase("")) {
					MostrarCaixaNeutra("Campo Inválido", "Favor, defina a quantidade");
					return;
				}else if (edtDesconto.getText().toString().equalsIgnoreCase("") || Float.parseFloat(edtDesconto.getText().toString().replace("%", "")) <= 0) {
					edtDesconto.setText("0");
				}
				
				//Código pra quando um produto esta sendo editado e vai ser lançado
				if (esta_editando) {
					list.remove(indice_produto);
					desconto[indice_produto] = 0;
					edtValorBruto.setText(Float.toString(Float.parseFloat(edtValorBruto.getText().toString()) - valor_bruto_indice_produto));
					valor_bruto_da_venda = ((Float.parseFloat(edtValorBruto.getText().toString())) - (valor_bruto_indice_produto));
					edtTotalLiquido.setText(Float.toString(Float.parseFloat(edtTotalLiquido.getText().toString()) -valor_liquido_indice_produto));
					total_liquido_da_venda = ((Float.parseFloat(edtTotalLiquido.getText().toString())) - (valor_liquido_indice_produto));
					
					if (!(edtValorBruto.getText().toString().contains("."))) {
						edtValorBruto.setText(edtValorBruto.getText().toString() + ".00");
					}
					if (!(edtTotalLiquido.getText().toString().contains("."))) {
						edtTotalLiquido.setText(edtTotalLiquido.getText().toString() + ".00");
					}
					
					int indice_vetor = 0;
					int x = 0;
					int y = 1;
					do {
						desconto [indice_produto + x] = desconto [indice_produto + y]; 
						x++;
						y++;
						indice_vetor++;
					} while (desconto[indice_vetor] != 0 && indice_vetor <= 995);
				}
				
				
				
				desconto_do_produto = Float.parseFloat(edtDesconto.getText().toString().replaceAll("%", ""));
				
				//Verifica se a quantidade é maior que existe no estoque
				if (Float.parseFloat(edtQuantidade.getText().toString()) > Float.parseFloat(tblProdutos.EstoqueAtual(edtCodigo.getText().toString()))) {
					
					final Dialog dialog = new Dialog (contexto);
					dialog.setContentView(R.layout.caixa_dialogo);
					dialog.setTitle("Quantidade maior que estoque");
					TextView aviso = (TextView) dialog.findViewById(R.CaixaDialogo.txvAviso);
					aviso.setText("Deseja continuar?");
					senha = (EditText) dialog.findViewById(R.CaixaDialogo.edtSenha);
					Button sim = (Button) dialog.findViewById(R.CaixaDialogo.btnSim);
					Button nao = (Button) dialog.findViewById(R.CaixaDialogo.btnNao);
					
					nao.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {						
							dialog.dismiss();
						}
					});
					
					sim.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {
							Cursor password = tblAdministrador.ObterSenha(senha.getText().toString());
							if (password.moveToFirst()) {
								
								//Essa porra de código não faz sentido
								/*if (Float.parseFloat(edtDesconto.getText().toString().replaceAll("[%]", "")) > 0) {
									desconto[desconto.length + 1] = 1;
								} else {
									desconto[desconto.length + 1] = 0;
								}*/
								
								
								
								if (desconto_do_produto > 0) {
									for (int i = 0; i <= 995; i++) {
										if (desconto[i] == 0) {
											desconto[i] = 1; //este é quando tem desconto
											break;
										}
									}
								} else {
									for (int i = 0; i <= 995; i++) {
										if (desconto[i] == 0) {
											desconto[i] = 2; //este é quando não tem desconto
											break;
										}
									}
								}
								
								LancarProduto();
								
								dialog.dismiss();
								
							} else {
								Toast.makeText(getApplicationContext(), "Senha Incorreta", Toast.LENGTH_SHORT).show();
								return;
							}
						}
					});
					
					dialog.show();
					
				} else {
					
					if (desconto_do_produto > 0) {
						for (int i = 0; i <= 995; i++) {
							if (desconto[i] == 0) {
								desconto[i] = 1; //este é quando tem desconto
								break;
							}
						}
					} else {
						for (int i = 0; i <= 995; i++) {
							if (desconto[i] == 0) {
								desconto[i] = 2; //este é quando não tem desconto
								break;
							}
						}
					}
					
					LancarProduto();
				}
				
				for (int i = 0; i <= 995; i++ ) {
					if (desconto[i] == 1) {
						estado_desconto_geral = false;
						break;
					} else {
						estado_desconto_geral = true;
					}
				}
				
				setListViewHeightBasedOnChildren(listview);
			
				//Toast.makeText(getApplicationContext(), "desconto length: " + desconto.length, Toast.LENGTH_SHORT).show();
				
			}
			
		});
		
		btnExcluirProduto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				MostrarCaixaExcluir("Excluir item", "Deseja excluit este item?");
			}
		});
		
		btnCancelarVenda.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				MostrarCaixaCancelar("Cancelar Venda", "Deseja cancelar esta venda?");
			}
		});
	
		btnFinalizarVenda.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				if ((edtFormaPagamento.getText().toString().equalsIgnoreCase("A Vista")) ||
						(edtCliente.getText().toString().equalsIgnoreCase("A Vista"))) {
					if (!((edtFormaPagamento.getText().toString().equalsIgnoreCase("A Vista")) &&
								(edtCliente.getText().toString().equalsIgnoreCase("A Vista")))) {
						MostrarCaixaNeutra("Atenção", "Caso o cliente ou a forma de pagamento for a vista, o outro também deve ser.");
						return;
					}
				}
				//Verifica se ao menos um produto foi lançado
				if (list.size() <= 0) {
					MostrarCaixaNeutra("Atenção", "Lance ao menos um produto antes de finalizar a venda");
					return;
				}
				
				codigo_venda = (int) Math.ceil(100000 + Math.random() * 899999);
				Cursor cv = tblCodigos.ObterCodigo(Integer.toString(codigo_venda));
				
				while (cv.moveToFirst() != false) {
					codigo_venda = (int) Math.ceil(100000 + Math.random() * 899999);
					cv = tblCodigos.ObterCodigo(Integer.toString(codigo_venda));
				}
				
				SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		        data_venda = sdfDateTime.format(new Date(System.currentTimeMillis()));
				
		        SimpleDateFormat sdfDateTime_BR = new SimpleDateFormat("dd/MM/yyyy");
		        data_venda_br = sdfDateTime_BR.format(new Date(System.currentTimeMillis()));
		        
				tblCodigos.Inserir(codigo_venda);
				
				//Caixa de diálogo finalizar venda
				final Dialog dialog = new Dialog (contexto);
				dialog.setContentView(R.layout.caixa_dialogo_venda);
				dialog.setTitle("               Concluir Venda               ");
				final EditText edtdescontogeral = (EditText) dialog.findViewById(R.CaixaDialogoVenda.edtDescontoGeral);
				final EditText edttotal = (EditText) dialog.findViewById(R.CaixaDialogoVenda.edtTotal);
				edttotal.setEnabled(false);
				final EditText edttroco = (EditText) dialog.findViewById(R.CaixaDialogoVenda.edtTroco);
				edttroco.setEnabled(false);
				final EditText edtpago = (EditText) dialog.findViewById(R.CaixaDialogoVenda.edtValorPago);
				Button btnfinalizar = (Button) dialog.findViewById(R.CaixaDialogoVenda.btnFinalizar);
				Button btncancelar = (Button) dialog.findViewById(R.CaixaDialogoVenda.btnCancelar);
				
				if (estado_desconto_geral) {
					edtdescontogeral.setEnabled(true);
				} else {
					edtdescontogeral.setEnabled(false);
				}
				
				edttotal.setText("R$"+edtTotalLiquido.getText().toString());
				
				edtdescontogeral.addTextChangedListener(new TextWatcher() {
					
					private String atual = "";
					float valor_com_desconto = 0;
					
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						
						//Mascara de porcentagem
						if (!(arg0.length() > 8)) {
							if (!(arg0.toString().equalsIgnoreCase(""))) {
								if(!(arg0.toString().equals(atual))) {
									edtdescontogeral.removeTextChangedListener(this);
									String stringlimpa = arg0.toString().replaceAll("[.,R$%]", "");
									double convertido = 0;
									try {
										convertido = Double.parseDouble(stringlimpa);
									} catch (NumberFormatException e) {
										edtdescontogeral.setSelection(arg0.length());
									}
									String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
									atual = formato;
									edtdescontogeral.setText(formato.replaceAll("[R$]", " ").replaceAll("[,]", ".")+"%");
									edtdescontogeral.setSelection(formato.length());
									edtdescontogeral.addTextChangedListener(this);
								}
								} else {
									return;
									}
							} else {
								return;
							}
						//Mascara de porcentagem
						
						boolean desconto_geral_vazio = edtdescontogeral.getText().toString().equalsIgnoreCase(""); 
						
						edtpago.setText("0000");
						
						if ( desconto_geral_vazio || (Float.parseFloat(edtdescontogeral.getText().toString().replaceAll("[%]", "")) <= 0.0)){
							valor_com_desconto = total_liquido_da_venda;
							edttotal.setText("R$" + df.format(valor_com_desconto).replaceAll("[,]", "."));
							if (!edttotal.getText().toString().contains(".")) {
								edttotal.setText(edttotal.getText().toString() + ".00");
							} 
							return;
								
						} else {
								
							desconto_geral_da_venda = Float.parseFloat(edtdescontogeral.getText().toString().replaceAll("[%]", ""))/100;
							valor_com_desconto = total_liquido_da_venda - (desconto_geral_da_venda * total_liquido_da_venda);
							edttotal.setText("R$" + df.format(valor_com_desconto).replaceAll("[,]", "."));
							if (!edttotal.getText().toString().contains(".")) {
								edttotal.setText(edttotal.getText().toString() + ".00");
							} 
						}
						
					}
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {} public void afterTextChanged(Editable arg0) {}
				});

				edtpago.addTextChangedListener(new TextWatcher() {
					private String atual = "";
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						
						//Máscara de dinheiro
						if(!(arg0.toString().equals(atual))) {
							edtpago.removeTextChangedListener(this);
							
							String stringlimpa = arg0.toString().replaceAll("[.,R$]", "");
							
							double convertido = Double.parseDouble(stringlimpa);
							String formato = NumberFormat.getCurrencyInstance().format((convertido/100));
							
							atual = formato;
							edtpago.setText(formato);
							edtpago.setSelection(formato.length());
							edtpago.addTextChangedListener(this);
						}
						
						//Cálculo para definir o troco
						
						if (edtpago.getText().toString().equalsIgnoreCase("")) {
							edttroco.setText("R$0.0");
							return;
						} else { 
							//Primeiro caso - se o valor dado for menor que o valor a ser pago, o troco não é exibido
							if (Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", ".")) <
									Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", ""))) {
								edttroco.setText("R$0.0");
								return;
							} else {   
							//Segundo caso - se não o valor será calculado e troco será mostrado 
							edttroco.setText("R$" + df.format(Float.parseFloat(edtpago.getText().toString().replaceAll("[R$.]", "").replaceAll("[,]", ".")) -
									Float.parseFloat(edttotal.getText().toString().replaceAll("[R$]", ""))));
							edttroco.setText(edttroco.getText().toString().replaceAll("[,]", "."));
							if (!(edttroco.getText().toString().contains("."))) {
									edttroco.setText(edttroco.getText().toString() + ".00");
								}
							}
						}
					}
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
					public void afterTextChanged(Editable arg0) {}
				});
				
				btnfinalizar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {  
						total_liquido_final = Float.parseFloat(edttotal.getText().toString().replaceAll("[,]", ".").replaceAll("[R$]", ""));
						CadastroVenda();
						CadastroProdutos();
						if (!(edtFormaPagamento.getText().toString().equalsIgnoreCase("A Vista"))) {
							CadastroConta();
						}
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "Venda efetuada com sucesso", Toast.LENGTH_SHORT).show();
						finish();
					}
				});
				
				btncancelar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
				
				dialog.show();
				//Caixa de diálogo finalizar venda				
				
			}
		});
		
	}

	
	//Função usada para preencher os devidos campos no começo de uma revenda
	private void ReVenda () {
		
		//Aqui liberamos os campos inciais e definimos que estes campos devem ficar liberados
		LiberacaoInicial();
		DefineEstado(ESTADO_FINAL);
		
		//Este código serve para preencher os campos com suas devidas informações
		try {
			edtVendedor.setText(tblVendedor.Nome_por_vendedor(tblVendas.Vendedor(id_venda)));
		} catch (Exception e1) {
			edtVendedor.setText("Registro inexistente");
		}
		
		if (tblVendas.FormaPagamento(id_venda).equalsIgnoreCase("A Vista")) {
			edtFormaPagamento.setText(tblVendas.FormaPagamento(id_venda));
		} else {
			try {
				edtFormaPagamento.setText(tblFormaPagamento.Descricao_por_FormaPagamento(tblVendas.FormaPagamento(id_venda)));
			} catch (Exception e) {
				edtFormaPagamento.setText("Registro inexistente");
			}
		}
		
		if (tblVendas.Cliente(id_venda).equalsIgnoreCase("A Vista")) {
			edtCliente.setText(tblVendas.Cliente(id_venda));
		} else {
			try {
				edtCliente.setText(tblCliente.Nome_por_Cliente(tblVendas.Cliente(id_venda)));
			} catch (Exception e) {
				edtCliente.setText("Registro inexistente");
			}
		}

		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this,
				R.array.PlanoPagamento,
				android.R.layout.simple_spinner_item
				);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPlanoPagamento.setAdapter(adapter);
		spnPlanoPagamento.setSelection(adapter.getPosition(tblVendas.PlanoPagamento(id_venda)));
		if (!tblVendas.ValorBruto(id_venda).contains(".")) {
			edtValorBruto.setText(tblVendas.ValorBruto(id_venda) + ".00");
		} else {
			edtValorBruto.setText(tblVendas.ValorBruto(id_venda));
		}
		
		if (!tblVendas.TotalLiquido(id_venda).contains(".")) {
			edtTotalLiquido.setText(tblVendas.TotalLiquido(id_venda) + ".00");
		} else {
			edtTotalLiquido.setText(tblVendas.TotalLiquido(id_venda));
		}
		
		//Este é um contador para controlar a posição no Cursor de dados
		int contagem = 1;
		
		//Este cursor puxa o código de todos os produtos lançados que possuam o mesmo código de uma venda
		Cursor codigo_produto = tblItensVendas.ID(id_venda);
		codigo_produto.moveToFirst();
		
		//Strings que usamos para guardar os valores para então colocar na listview
		String temp_produto = "";
		String temp_valor = "";
		String temp_desconto = "";
		String temp_qtd = "";
		String temp_total = "";
		
		//Este while serve para repetir a inserção de produtos a quantidade de vezes de produtos lançados nesta venda
		while (contagem <= tblItensVendas.QunatidadeItensVenda(id_venda)) {
						
			//Aqui coloco nas string os valores, baseado no cursor que me retorna o código do item da venda
			try {
				temp_produto = tblProdutos.Descricao(tblItensVendas.Produto(codigo_produto.getString(0)));
			} catch (Exception e) {
				temp_produto = "Registro inexistente";
			}
			
			if (!tblItensVendas.Valor(codigo_produto.getString(0)).contains(".")) {
				temp_valor = tblItensVendas.Valor(codigo_produto.getString(0)) + ".00";
			} else {
				temp_valor = tblItensVendas.Valor(codigo_produto.getString(0));
			}
			temp_desconto = tblItensVendas.Desconto(codigo_produto.getString(0)) + "%";
			temp_qtd = tblItensVendas.Quantidade(codigo_produto.getString(0));
			if (!tblItensVendas.Total(codigo_produto.getString(0)).contains(".")) {
				temp_total = tblItensVendas.Total(codigo_produto.getString(0)) + ".00";
			} else {
				temp_total = tblItensVendas.Total(codigo_produto.getString(0));
			}
			
			//Então aqui alimentamos o vetor de desconto, que nos ajuda a verificar se o desconto geral vai ser liberado ou não
			if (Float.parseFloat(temp_desconto.replaceAll("%", "")) >= 0) {
				desconto[contagem - 1] = 1;
				estado_desconto_geral = false;
			} else {
				desconto[contagem - 1] = 2;
			}
			
			//Aqui colocamos os valores na listview
			HashMap<String, String> hMap = new HashMap<String, String>();
			hMap.put("produto", temp_produto);
			hMap.put("valor", temp_valor);
			hMap.put("desconto", temp_desconto);
			hMap.put("qtd", temp_qtd);
			hMap.put("total", temp_total);
			list.add(hMap);
			listview.refreshDrawableState();
			adapter.notifyDataSetChanged();
			
			//incrementagem do contador
			contagem++;
			codigo_produto.moveToNext();
		}
		
		//Valor_bruto e total_liquido recebem seus respectivos valores
		valor_bruto_da_venda = Float.parseFloat(edtValorBruto.getText().toString());
		total_liquido_da_venda = Float.parseFloat(edtTotalLiquido.getText().toString());
		
		//Só reinicio esta variaveis só pro precaução
		preco_do_produto = 0;
		quantidade_do_produto = 0;
		desconto_do_produto = 0;
		total_do_produto = 0;
		total_do_produto_temporario = 0;
		desconto_geral_da_venda = 0;
		total_liquido_final = 0; 
		novo_total_da_venda = 0;
	}
	
	//Funções para cadastrar a venda e etc após a finalização
	private void CadastroVenda () {
		
		String Acrescimo;
		float acressimo_na_venda = 0;
		
		//BEGIN cadastro da venda
		if (edtFormaPagamento.getText().toString().equalsIgnoreCase("A Vista")) {
			Acrescimo = "0";
		} else {
			Acrescimo = tblFormaPagamento.AcressimoID(edtFormaPagamento.getText().toString()).replaceAll("[%]", "");
			acressimo_na_venda = Float.parseFloat(Acrescimo);
		}
		
		if (acressimo_na_venda > 0) {
			novo_total_da_venda = ((acressimo_na_venda/100) * total_liquido_final) + total_liquido_final;
		} else {
			novo_total_da_venda = total_liquido_final;
		}
		
        float dsc = 0;
        if (desconto_geral_da_venda > 0) {
        	dsc = desconto_geral_da_venda*total_liquido_da_venda;
        }
         
        if (revenda) {
        	tblVendas.Atualizar(
        		Integer.parseInt(id_venda),
        		edtVendedor.getText().toString(),
        		edtCliente.getText().toString(),
        		edtFormaPagamento.getText().toString(),
				spnPlanoPagamento.getSelectedItem().toString(),
				Float.toString(valor_bruto_da_venda),
				Float.toString(total_liquido_da_venda),
				Float.toString(total_liquido_final),
				Float.toString(dsc),
				Acrescimo,
				Float.toString(novo_total_da_venda),
    			data_venda,
    			data_venda_br
        	);
        } else {
        	tblVendas.Inserir(
    			codigo_venda,
    			edtVendedor.getText().toString(),
    			edtCliente.getText().toString(),
    			edtFormaPagamento.getText().toString(),
    			spnPlanoPagamento.getSelectedItem().toString(),
    			Float.toString(valor_bruto_da_venda),
    			Float.toString(total_liquido_da_venda),
    			Float.toString(total_liquido_final),
    			Float.toString(dsc),
    			Acrescimo,
    			Float.toString(novo_total_da_venda),
    			data_venda,
    			data_venda_br
    		);
        }
		//END cadastro da venda
	}
	private void CadastroConta () {
		//BEGIN cadastro da conta
		
			
			float valor_parcela = novo_total_da_venda / tblFormaPagamento.QuantidadeParcelas(Integer.toString(tblFormaPagamento.RetornaID(edtFormaPagamento.getText().toString()))); 
			String vlrparcela = "";
			
			if (df.format(valor_parcela).contains(",")) {
				vlrparcela = df.format(valor_parcela).replaceAll("[,]", ".");
			}
			
			if (!vlrparcela.contains(".")) {
				vlrparcela = df.format(valor_parcela) + ".00";
			} 
			
			int contador = 1;
			int qtd_parcelas = tblFormaPagamento.QuantidadeParcelas(Integer.toString(tblFormaPagamento.RetornaID(edtFormaPagamento.getText().toString())));
			

			if (revenda) {
				Cursor codigo_conta = tblContasReceber.RetornaTodosCodigos(id_venda);
				codigo_conta.moveToFirst();
				
				while (contador <= qtd_parcelas) {
					tblContasReceber.Atualizar(
							codigo_conta.getInt(0), 
							edtCliente.getText().toString(),
							id_venda, 
							tblFormaPagamento.RetornaID(edtFormaPagamento.getText().toString()),
							0, 
							Float.toString(novo_total_da_venda),
							contador + " de " + qtd_parcelas,
							vlrparcela,
							data_venda_br,
							data_venda
							);
					codigo_conta.moveToNext();
					contador++;
				}
				
			} else {
			
				while (contador <= qtd_parcelas) {
					
					int codigo_pagamento = (int) Math.ceil(100000 + Math.random() * 899999);
					Cursor cp = tblCodigos.ObterCodigo(Integer.toString(codigo_pagamento));
					
					while (cp.moveToFirst() != false) {
						codigo_pagamento = (int) Math.ceil(100000 + Math.random() * 899999);
						cp = tblCodigos.ObterCodigo(Integer.toString(codigo_pagamento));
					}
					
					tblCodigos.Inserir(codigo_pagamento);
					
					tblContasReceber.Inserir(
							codigo_pagamento,
							edtCliente.getText().toString(),
							Integer.toString(codigo_venda),
							tblFormaPagamento.RetornaID(edtFormaPagamento.getText().toString()),
							0, //TODO este é o vencimento do pagamento - temos que calcular isso
							Float.toString(novo_total_da_venda),
							contador + " de " + qtd_parcelas,
							vlrparcela,  
							data_venda_br,
							data_venda);
					
					contador++;
				}
				
			
			}
			
		Toast.makeText(getApplicationContext(), "parcela: " + valor_parcela, Toast.LENGTH_SHORT).show();
		
		//END cadastro da conta
		
	}
	private void CadastroProdutos () {
					
		int index = 0;
		int codigo_desta_venda = 0;
		if (revenda) {
			int contador = 1;
			int qtd_itens = tblItensVendas.QunatidadeItensVenda(id_venda);
			Cursor codigo_produto = tblItensVendas.ID(id_venda);
			codigo_produto.moveToFirst();
			while (contador <= qtd_itens) {
				tblProdutos.AtualizarEstoque(
						Integer.parseInt(tblItensVendas.Quantidade(codigo_produto.getString(contador - 1))) + 
						Integer.parseInt(tblProdutos.EstoqueAtual(tblItensVendas.Produto(codigo_produto.getString(contador - 1)))),
						tblItensVendas.Produto(codigo_produto.getString(contador - 1)));
				tblItensVendas.Deletar(codigo_produto.getLong(contador - 1));
				contador++;
			}
			codigo_desta_venda = Integer.parseInt(id_venda);
		} else {
			codigo_desta_venda = codigo_venda;
		}
		 
		while (index < list.size()) {
			
			int codigo_produto = (int) Math.ceil(100000 + Math.random() * 899999);
			
			Cursor cv = tblCodigos.ObterCodigo(Integer.toString(codigo_produto));
			while (cv.moveToFirst() != false) {
				codigo_produto = (int) Math.ceil(100000 + Math.random() * 899999);
				cv = tblCodigos.ObterCodigo(Integer.toString(codigo_produto));
			}
			
			tblCodigos.Inserir(codigo_produto);
			
			tblItensVendas.Inserir(
					codigo_produto, 
					codigo_desta_venda, 
					tblProdutos.Codigo_por_Produto(list.get(index).get("produto").toString()), 
					list.get(index).get("qtd").toString(), 
					list.get(index).get("desconto").toString().replaceAll("[%]", ""), 
					list.get(index).get("valor").toString(),
					list.get(index).get("total").toString());

			//Este código serve para subtrair a quantidade do produto nesta venda, do estoque atual do produto
			tblProdutos.AtualizarEstoque(
					tblProdutos.EstoqueAtual_PorNome(list.get(index).get("produto").toString()) - Integer.parseInt(list.get(index).get("qtd").toString()),
					list.get(index).get("produto").toString()
			);
			
			index++;
		}
		
	}
	
	//Essa função fecha todas as tabelas após a activity ser destruida
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		tblProdutos.close();
		tblVendas.close();
		tblCodigos.close();
		tblFormaPagamento.close();
		tblContasReceber.close();
		tblAdministrador.close();
		tblItensVendas.close();
		tblVendedor.close();
		tblCliente.close();
	}

	@Override
	public void onBackPressed() {}
	
	
}