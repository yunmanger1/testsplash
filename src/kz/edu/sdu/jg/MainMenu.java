package kz.edu.sdu.jg;

import kz.edu.sdu.jg.utils.YellowUtils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenu extends Activity {
   /** Called when the activity is first created. */

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.main);
      //ParseUtils parser = new ParseUtils();
      //YellowUtils parser = new YellowUtils()
      String[] CATEGORIES = null;
      try {
         CATEGORIES = new String[YellowUtils.allCats.size()];
         for (int i = 0; i < CATEGORIES.length; i++) {
            CATEGORIES[i] = YellowUtils.allCats.get(i).name;
         }
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      ListView lv = (ListView) this.findViewById(R.id.listView1);
      lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CATEGORIES));
      lv.setTextFilterEnabled(true);

      /*lv.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
            // When clicked, show a toast with the TextView text
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
         }
      });*/

   }
}
