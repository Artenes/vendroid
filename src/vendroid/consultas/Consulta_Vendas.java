//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.consultas;

import java.util.Calendar;

import dipro.vendasandroid.R;
import vendroid.bancodados.Tabela_Vendas;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Consulta_Vendas extends Activity {

	private static final String campos [] = {"_id","vend_vendedor","vend_cliente", "vend_data_br", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigo, edtVendedor, edtCliente, edtDataInicial, edtDataFinal;
	Button btnVoltar, btnPesquisar;
	ListView listview;
	Tabela_Vendas helper;
	int dia, mes, ano;
	Context contexto = this;
	String DataInicial = "";
	String DataFinal = "";
	String state [] = {""};
	static final int DATA_INICIAL = 111;
	static final int DATA_FINAL = 222;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_vendas);
		
		helper = new Tabela_Vendas(this);
		helper.open();
		
		state = (String[]) getLastNonConfigurationInstance();
		
		/*if (state.length != 0) {
			Datas = state;
			DataInicial = Datas[0];
			DataFinal = Datas[1];
		}*/
		
		edtCodigo = (EditText) findViewById(R.ConsultaVendas.edtCodigo);
		edtCliente = (EditText) findViewById(R.ConsultaVendas.edtCliente);
		edtDataFinal = (EditText) findViewById(R.ConsultaVendas.edtDataFinal);
		edtDataFinal.setKeyListener(null);
		edtDataInicial = (EditText) findViewById(R.ConsultaVendas.edtDataInicial);
		edtDataInicial.setKeyListener(null);
		edtVendedor = (EditText) findViewById(R.ConsultaVendas.edtVendedor);
		btnVoltar = (Button) findViewById(R.ConsultaVendas.btnVoltar);
		btnPesquisar = (Button) findViewById(R.ConsultaVendas.btnPesquisar);
		listview = (ListView) findViewById(R.ConsultaVendas.list);
		
		
		change = (SimpleCursorAdapter)getLastNonConfigurationInstance();
		
		if (change != null) {
			dataSource = change;
			listview.setAdapter(dataSource);
		}
		
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				helper.close();
				finish();
			}
		});
		
		edtDataInicial.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final Calendar c = Calendar.getInstance();
				ano = c.get(Calendar.YEAR);
				mes = c.get(Calendar.MONTH);
				dia = c.get(Calendar.DAY_OF_MONTH);
				showDialog(DATA_INICIAL);
				new DatePickerDialog(contexto, datePickerListener, ano, mes, dia);
			}
		});
		
		edtDataFinal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final Calendar c = Calendar.getInstance();
				ano = c.get(Calendar.YEAR);
				mes = c.get(Calendar.MONTH);
				dia = c.get(Calendar.DAY_OF_MONTH);
				showDialog(DATA_FINAL);
				new DatePickerDialog(contexto, datePickerListener2, ano, mes, dia);
			}
		});
		
		btnPesquisar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				if (edtDataInicial.getText().toString().equalsIgnoreCase("") &&
						edtDataFinal.getText().toString().equalsIgnoreCase("")) {
					DataInicial = "1990-01-01";
					DataFinal = "2100-01-01";
				} else if (edtDataInicial.getText().toString().equalsIgnoreCase("")) {
					DataInicial = "1990-01-01";
				} else if (edtDataFinal.getText().toString().equalsIgnoreCase("")) {
					DataFinal = "2100-01-01";
				} 	
				
			
				
				String Codigo = edtCodigo.getText().toString();
				String Cliente = edtCliente.getText().toString();
				String Vendedor = edtVendedor.getText().toString();
				dataSource = new SimpleCursorAdapter(
						contexto, 
						R.layout.consulta_linhas_vendas, 
						helper.BuscaFiltro(Codigo, Vendedor, Cliente, DataInicial, DataFinal), 
						campos, 
						new int[] { R.ConsultaLinhasVendas.txvCodigo,
					R.ConsultaLinhasVendas.txvVendedor,
					R.ConsultaLinhasVendas.txvCliente,
					R.ConsultaLinhasVendas.txvData});
				listview.setAdapter(dataSource);
				
				if (helper.BuscaFiltro(Codigo, Vendedor, Cliente, DataInicial, DataFinal).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		 
		listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.ConsultaLinhasVendas.txvCodigo)).getText();
				 Cursor cur = helper.BuscaID(text);
				 cur.moveToFirst();
				 String venda = cur.getString(cur.getColumnIndex("_id"));
				 Intent i = new Intent(getApplicationContext(), vendroid.editar.Editar_Venda.class);
				 i.putExtra("id_venda", venda);
				 helper.close();
				 startActivity(i);
				 finish();
			}
		});
		
		
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATA_INICIAL:
		   return new DatePickerDialog(this, datePickerListener, 
                         ano, mes, dia);
		case DATA_FINAL:
			return new DatePickerDialog(this, datePickerListener2, 
                    ano, mes, dia);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
		ano = selectedYear;
		mes = selectedMonth;
		dia = selectedDay;
		
		if ((mes < 10) && (dia < 10)) {
			DataInicial = ano+"-"+"0"+(mes+1)+"-"+"0"+dia;
			
		} else if((mes < 10) && (dia >= 10)){
			DataInicial = ano+"-"+"0"+(mes+1)+"-"+dia;
			
		} else if ((mes >= 10) && (dia < 10)) {
			DataInicial = ano+"-"+(mes+1)+"-"+"0"+dia;
			
		} else {
			DataInicial = ano+"-"+(mes+1)+"-"+dia;
		}
		
	edtDataInicial.setText(new StringBuilder().append(dia)
	   .append("/").append(mes + 1).append("/").append(ano)
	   .append(" "));	
	}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			ano = selectedYear;
			mes = selectedMonth;
			dia = selectedDay;
		
			if ((mes < 10) && (dia < 10)) {
				DataFinal = ano+"-"+"0"+(mes+1)+"-"+"0"+dia;
				
			} else if((mes < 10) && (dia >= 10)){
				DataFinal = ano+"-"+"0"+(mes+1)+"-"+dia;
				
			} else if ((mes >= 10) && (dia < 10)) {
				DataFinal = ano+"-"+(mes+1)+"-"+"0"+dia;
				
			} else {
				DataFinal = ano+"-"+(mes+1)+"-"+dia;
			}
		
		edtDataFinal.setText(new StringBuilder().append(dia)
		   .append("/").append(mes + 1).append("/").append(ano)
		   .append(" "));	
		}
		};
	
		@Override
		public Object onRetainNonConfigurationInstance() {
			return dataSource;
		}
		
	@Override
    protected void onDestroy() {
        super.onDestroy();        
        helper.close();        
	}

	@Override
	public void onBackPressed() {}
	
	
	}

