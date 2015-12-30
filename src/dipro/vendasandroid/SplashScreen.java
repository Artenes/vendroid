package dipro.vendasandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity implements Runnable {
	
	
	
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.splash);
Handler h = new Handler();
h.postDelayed(this, 1000);//aqui � definido o delay do handler em milisegundos
}

public void run(){
	startActivity(new Intent(this, LogIn.class));
	finish();//aqui � chamado o m�todo finish pra finalizar a activity atual no caso SplashScreen
}

@Override
public void onBackPressed() {}


}