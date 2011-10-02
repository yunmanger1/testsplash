package kz.edu.sdu.jg;

import java.util.HashMap;
import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class CategoryActivity extends Activity {
   /** Called when the activity is first created. */
   static HashMap<Character, List<Category>> data;
   static String[] CATEGORIES = null;
   static char lastChar;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.categories);
      data = new HashMap<Character, List<Category>>();
      try {
         ParseUtils pu = new ParseUtils();
         data.put('a', pu.getAlmatyCategoriesByLetter('a'));
         List<Category> lst = data.get('a');
         lastChar = 'a';
         CATEGORIES = new String[lst.size()];
         int k = 0;
         for (Category cat : lst) {
            CATEGORIES[k++] = cat.name;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      ListView lv = (ListView) this.findViewById(R.id.listView1);
      lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES));
      lv.setTextFilterEnabled(true);
      lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

         @Override
         public void onItemSelected(AdapterView parent, View child, int pos, long id) {
            Intent intent = new Intent(CategoryActivity.this, CompanyActivity.class);
            intent.putExtra("cat_id", data.get('c').get(pos).id);
            startActivityForResult(intent, 777);
         }

         @Override
         public void onNothingSelected(AdapterView parent) {

         }

      });

   }

   public void buttonClick(View view) {
      changeCategory(((Button) view).getText().charAt(0));
      ListView lv = (ListView) this.findViewById(R.id.listView1);
      lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES));
   }

   public static void changeCategory(char c) {
      try {
         ParseUtils pu = new ParseUtils();
         data.put(c, pu.getAlmatyCategoriesByLetter(c));
         List<Category> lst = data.get(c);
         CATEGORIES = new String[lst.size()];
         int k = 0;
         for (Category cat : lst) {
            CATEGORIES[k++] = cat.name;
         }
         lastChar = c;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
