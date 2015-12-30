package vendroid.sobre;

import dipro.vendasandroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Sobre extends Activity {

	Button btnVoltar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sobre);
		
		btnVoltar = (Button) findViewById(R.sobre.btnVoltar);
		
		btnVoltar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {}
	
	
}
