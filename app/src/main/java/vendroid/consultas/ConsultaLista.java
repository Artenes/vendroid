package vendroid.consultas;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import dipro.vendasandroid.R;

public class ConsultaLista extends ListActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.consultalist);
		String [] itens = new String [] {"Cliente","Fornecedor", "Marcas", "Grupos", "Produtos", "Rotas", "Vendedores",
				"Formas de Pagamento", "Usuários", "Empresa", "Vendas","Voltar"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itens);
		setListAdapter(adapter);
		
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	switch (position) {
	case 0:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Clientes.class));
		break;
	case 1:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Fornecedor.class));
		break;
	case 2:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Marcas.class));
		break;
	case 3:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Grupos.class));
		break;
	case 4:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Produtos.class));
		break;
	case 5:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Rotas.class));
		break;
	case 6:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Vendedor.class));
		break;
	case 7:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Formas_Pagamento.class));
		break;
	case 8:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Usuarios.class));
		break;
	case 9:
		startActivity(new Intent(this,vendroid.editar.Editar_Empresa.class));
		break;
	case 10:
		startActivity(new Intent(this,vendroid.consultas.Consulta_Vendas.class));
		break;
	case 11:
		finish();
		break;
	}
	
	}
	
	@Override
	public void onBackPressed() {}
	
}
