package vendroid.bancodados;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDados_Acesso{
	//Tabela vendedor - ok
	private static final String VENDEDOR = 
			"CREATE TABLE IF NOT EXISTS vendedor " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "ven_nome VARCHAR (35) NOT NULL, "
			+ "ven_senha VARCHAR (35) NOT NULL, "
			+ "ven_comissao VARCHAR (35) NOT NULL, "
			+ "ven_datacad DATE);";
	
	//Tabela rotas - ok
	private static final String ROTAS = 
			"CREATE TABLE IF NOT EXISTS rotas " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "rot_descricao VARCHAR (120) NOT NULL, "
			+ "rot_vendedor NUMERIC (6) NOT NULL, " //Chave estrangeira
			+ "rot_datacad DATE);";
	
	//Tabela clientes - ok
	private static final String CLIENTES = 
			"CREATE TABLE IF NOT EXISTS clientes " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "cli_pessoa NUMERIC (2) NOT NULL, "
			+ "cli_nome VARCHAR (80) NOT NULL, "
			+ "cli_cpf VARCHAR (35), "
			+ "cli_cnpj VARCHAR (18), "
			+ "cli_inscrestadual VARCHAR (15), "
			+ "cli_rg VARCHAR (35), "
			+ "cli_dn DATE, "
			+ "cli_end VARCHAR (80) NOT NULL,"
			+ "cli_bairro VARCHAR (65) NOT NULL, "
			+ "cli_cidade VARCHAR (65), "
			+ "cli_uf VARCHAR (65), "
			+ "cli_cep VARCHAR (35), "
			+ "cli_foneum VARCHAR (35) NOT NULL, "
			+ "cli_fonedois VARCHAR (35), "
			+ "cli_email VARCHAR (65), "
			+ "cli_rota NUMERIC (6), " //Chave estrangeira
			+ "cli_datacad DATE);";
	
	//tabela fornecedor - ok
	private static final String FORNECEDOR = 
			"CREATE TABLE IF NOT EXISTS fornecedor " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "for_pessoa NUMERIC (2) NOT NULL, "
			+ "for_nome VARCHAR (80) NOT NULL, "
			+ "for_cpf VARCHAR (35), "
			+ "for_rg VARCHAR (35), "
			+ "for_cnpj VARCHAR (18), "
			+ "for_inscrestadual VARCHAR (15), "
			+ "for_end VARCHAR (80) NOT NULL,"
			+ "for_bairro VARCHAR (65) NOT NULL, "
			+ "for_cidade VARCHAR (65), "
			+ "for_uf VARCHAR (65), "
			+ "for_cep VARCHAR (35), "
			+ "for_foneum VARCHAR (35) NOT NULL, "
			+ "for_fonedois VARCHAR (35), "
			+ "for_email VARCHAR (65), "
			+ "for_datacad DATE);";
	
	//Tabela marcas - ok
	private static final String MARCAS = 
			"CREATE TABLE IF NOT EXISTS marcas " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "mar_descricao VARCHAR (35) NOT NULL, "
			+ "mar_fornecedor NUMERIC (6) NOT NULL, " //Chave estrangeira	
			+ "mar_datacad DATE);";
	
	//Tabela grupos - ok
	private static final String GRUPOS = 
			"CREATE TABLE IF NOT EXISTS grupos " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "grup_descricao VARCHAR (35) NOT NULL, "
			+ "grup_datacad DATE);";
	
	//Tabela produtos - ok
	private static final String PRODUTOS = 
			"CREATE TABLE IF NOT EXISTS produtos " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "prod_descricao VARCHAR (120) NOT NULL, "
			+ "prod_marca VARCHAR (35) NOT NULL, " //Chave estrangeira
			+ "prod_grupo VARCHAR (35) NOT NULL, " //Chave estrangeira
			+ "prod_codbarra NUMERIC, "
			+ "prod_unidade VARCHAR (03) NOT NULL, "
			+ "prod_estoqueatual NUMERIC NOT NULL, "
			+ "prod_estoquemin NUMERIC, "
			+ "prod_sittributaria VARCHAR (35) NOT NULL, "
			+ "prod_ncm NUMERIC NOT NULL, "
			+ "prod_icms NUMERIC NOT NULL, "
			+ "prod_precocompra VARCHAR (35) NOT NULL, "
			+ "prod_precocusto VARCHAR (35) NOT NULL, "
			+ "prod_vendavista VARCHAR (35) NOT NULL, "
			+ "prod_vendaprazo VARCHAR (35) NOT NULL, "			
			+ "prod_datacad DATE);";
	
	//Tabela forma pagamento - ok
	private static final String FORMA_PAG = 
			"CREATE TABLE IF NOT EXISTS forma_pag " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "forpg_descricao VARCHAR (35) NOT NULL, "
			+ "forpg_qtdparcela NUMERIC NOT NULL, "
			+ "forpg_diasentreparcela NUMERIC NOT NULL, "
			+ "forpg_acresc NUMERIC NOT NULL, "
			+ "forpg_datacad DATE);";
	
	//Tabela usuario - ok
	private static final String USUARIO = 
			"CREATE TABLE IF NOT EXISTS usuario " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "us_nome VARCHAR (35) NOT NULL, "
			+ "us_senha VARCHAR (35) NOT NULL, "
			+ "us_datacad DATE);";
	
	//Tabela administrador - ok
	private static final String ADMINISTRADOR = 
			"CREATE TABLE IF NOT EXISTS administrador " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "admi_nome VARCHAR (35) NOT NULL, "
			+ "admi_senha VARCHAR (35) NOT NULL, "
			+ "admi_datacad DATE);";
	
	//Tabela vendas - ok
	private static final String VENDAS = 
			"CREATE TABLE IF NOT EXISTS vendas " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "vend_vendedor VARCHAR (120) NOT NULL, " //Chave estrangeira
			+ "vend_cliente VARCHAR (120) NOT NULL, " //Chave estrangeira
			+ "vend_formapag VARCHAR (120) NOT NULL, " //Chave estrangeira
			+ "vend_planopag VARCHAR (120) NOT NULL, " 
			+ "vend_valorbruto NUMERIC NOT NULL,"
			+ "vend_totalliquido NUMERIC NOT NULL,"
			+ "vend_totalliquidodesc NUMERIC NOT NULL,"
			+ "vend_desconto NUMERIC NOT NULL, "
			+ "vend_acresc NUMERIC NOT NULL, "
			+ "vend_totalvenda NUMERIC NOT NULL, "
			+ "vend_data_br VARCHAR (10) NOT NULL, " //Esta coluna me ajuda na hora de exibir a data no padrão BR durante a pesquisa
			+ "vend_data DATE NOT NULL);";
	
	//Tabela itens vendas - ok
	private static final String ITENS_VENDAS = 
			"CREATE TABLE IF NOT EXISTS itens_vendas " 
			+ "( _id INTEGER (6) NOT NULL, "
			+ "itven_venda INTEGER (6) NOT NULL, " 
			+ "itven_produto INTEGER (6) NOT NULL, " 
			+ "itven_qtd NUMERIC, "
			+ "itven_desc NUMERIC, "
			+ "itven_precovenda NUMERIC, "
			+ "itven_total NUMERIC, "
			+ "itven_datavend DATE);";
	
	//Tabela contas receber - ok
	private static final String CONTAS_RECEBER = 
			"CREATE TABLE IF NOT EXISTS contas_receber " 
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "cr_cliente VARCHAR (120) NOT NULL, " //Chave Estrangeira 
			+ "cr_venda NUMERIC NOT NULL, " //Chave Estrangeira
			+ "cr_formapag NUMERIC NOT NULL, " //Chave Estrangeira
			+ "cr_datavenc VARCHAR (10), "
			+ "cr_valortotal VARCHAR (35), "
			+ "cr_numparcela VARCHAR (10), "
			+ "cr_valorparc VARCHAR (35), "
			+ "cr_data_br VARCHAR (10) NOT NULL, " //Esta coluna me ajuda na hora de exibir a data no padrão BR durante a pesquisa
			+ "cr_data DATE);";

	//Tabela coidogs - ok
	private static final String CODIGOS =
			"CREATE TABLE IF NOT EXISTS codigos "
			+ "(codigo INTEGER NOT NULL);";
	
	//Tabela coidogs - ok
	private static final String USUARIO_ATUAL =
				"CREATE TABLE IF NOT EXISTS usuario_atual "
				+ "(_id INTEGER (1) NOT NULL,"
				+ "usuario INTEGER (1) NOT NULL);";
	
	//Tabela empresas - ok
	private static final String EMPRESA = 
			"CREATE TABLE IF NOT EXISTS empresa "
			+ "( _id INTEGER PRIMARY KEY NOT NULL, "
			+ "emp_razaosocial VARCHAR (120) NOT NULL, "
			+ "emp_nomefantasia VARCHAR (120), "
			+ "emp_cnpj VARCHAR (18) NOT NULL, "
			+ "emp_inscrestadual VARCHAR (15) NOT NULL, "
			+ "emp_end VARCHAR (120) NOT NULL, "
			+ "emp_bairro VARCHAR (90) NOT NULL, "
			+ "emp_cidade VARCHAR (90), "
			+ "emp_uf VARCHAR (90) NOT NULL, "
			+ "emp_cep VARCHAR (10), "
			+ "emp_representante VARCHAR (90) NOT NULL, "
			+ "emp_foneum VARCHAR (13) NOT NULL, "
			+ "emp_fonedois VARCHAR (13), "
			+ "emp_email VARCHAR (90), "
			+ "emp_site VARCHAR (90), "
			+ "emp_datacad DATE);";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	@SuppressWarnings("unused")
	private SQLiteDatabase db;
	
	public BancoDados_Acesso (Context ctx) {
		this.context = ctx;
		this.DBHelper = new DatabaseHelper(this.context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper (Context context) {
			super (context, "vendroid.db", null, 1);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CLIENTES);
			db.execSQL(FORNECEDOR);
			db.execSQL(MARCAS);
			db.execSQL(GRUPOS);
			db.execSQL(PRODUTOS);
			db.execSQL(ROTAS);
			db.execSQL(VENDEDOR);
			db.execSQL(FORMA_PAG);
			db.execSQL(USUARIO);
			db.execSQL(ADMINISTRADOR);
			db.execSQL(VENDAS);
			db.execSQL(ITENS_VENDAS);
			db.execSQL(CONTAS_RECEBER);
			db.execSQL(CODIGOS);
			db.execSQL(EMPRESA);
			db.execSQL(USUARIO_ATUAL);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
		
	}
	
	public BancoDados_Acesso open() throws SQLException {
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		this.DBHelper.close();
	}
	
}
