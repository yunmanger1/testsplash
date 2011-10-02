package kz.edu.sdu.jg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

   }

   public void buttonClick(View view) {
      switch (view.getId()) {
         case R.id.button0 :
            break;
         case R.id.button1 :
            Intent catIntent = new Intent(MainMenu.this, CategoryActivity.class);
            MainMenu.this.startActivity(catIntent);
      }
   }
}
