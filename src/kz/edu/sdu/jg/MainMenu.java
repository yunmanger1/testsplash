package kz.edu.sdu.jg;

import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainMenu extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      ParseUtils pu = new ParseUtils();
      //YellowUtils parser = new YellowUtils()
      String[] CATEGORIES = null;
      try {
         List<Category> lst = pu.getAlmatyCategories();
         CATEGORIES = new String[lst.size()];
         int k = 0;
         for (Category c : lst) {
            CATEGORIES[k++] = c.name;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      ListView lv = (ListView) this.findViewById(R.id.listView1);
      lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES));
      lv.setTextFilterEnabled(true);

   }

   public void buttonClick(View view) {
      switch (view.getId()) {
         case R.id.button0 :
            break;
         case R.id.button1 :
            startActivity(new Intent("kz.edu.sdu.jg.CategoriesScreen"));
      }
   }
}
