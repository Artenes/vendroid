//Toast.makeText(getApplicationContext(), "id: " + arg2, Toast.LENGTH_SHORT).show();
package vendroid.contas;

import vendroid.bancodados.Tabela_Contas_Receber;
import dipro.vendasandroid.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContasReceber extends Activity{
	
private static final String campos [] = {"cr_venda","cr_cliente", "cr_numparcela", "_id"};
	
	private SimpleCursorAdapter dataSource, change;
	EditText edtCodigoVenda, edtCliente;
	ListView listview;
	Button btnVoltar, btnPesquisar;
	Tabela_Contas_Receber helper;
	Context contexto = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consulta_contas_receber);
		
		helper = new Tabela_Contas_Receber(this);
		helper.open();
		
		edtCodigoVenda = (EditText) findViewById(R.ContasReceber.edtCodigoVenda);
		edtCliente = (EditText) findViewById(R.ContasReceber.edtCliente);
		btnPesquisar = (Button) findViewById(R.ContasReceber.btnPesquisar);
		btnVoltar = (Button) findViewById(R.ContasReceber.btnVoltar);
		listview = (ListView) findViewById(R.ContasReceber.listview);
		
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
		 
		btnPesquisar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String venda = edtCodigoVenda.getText().toString();
				String cliente = edtCliente.getText().toString();
				dataSource = new SimpleCursorAdapter(
						contexto, 
						R.layout.editar_contas_linhas,
						helper.BuscaFiltro(venda, cliente),
						campos, 
						new int[] {R.EditarContasLinhas.txvNumeroParcela, R.EditarContasLinhas.txvValorParcela, R.EditarContasLinhas.txvDataVencimento, R.EditarContasLinhas.txvCodigo}
						);
				listview.setAdapter(dataSource);
				if (helper.BuscaFiltro(venda, cliente).getCount() <= 0) {
					Toast.makeText(contexto, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		 listview.setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				 final String text = (String) ((TextView)arg1.findViewById(R.EditarContasLinhas.txvCodigo)).getText();
				 Intent i = new Intent(getApplicationContext(), vendroid.contas.Editar_ContasReceber.class);
				 i.putExtra("id_conta", text);
				 helper.close();
				 startActivity(i);
				 finish();
			}
		});
	}
	
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