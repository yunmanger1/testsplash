package kz.edu.sdu.jg;

import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoriesScreen extends Activity {
   /** Called when the activity is first created. */

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.categories);

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
}
