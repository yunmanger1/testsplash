package kz.edu.sdu.jg;

import java.io.IOException;
import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.models.Company;
import kz.edu.sdu.jg.utils.ParseUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CompanyActivity extends Activity {
   static Category cat = null;
   static List<Company> lst = null;
   static String[] COMPANIES = null;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.companies);
      ParseUtils pu = new ParseUtils();
      cat = (Category) getIntent().getExtras().get("CATEGORY");
      if (cat != null) {
         try {
            lst = pu.getCategoryDetail(cat);
            COMPANIES = new String[lst.size()];
            int k = 0;
            for (Company cat : lst) {
               COMPANIES[k++] = cat.name;
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
         ListView lv = (ListView) this.findViewById(R.id.listView2);
         lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, COMPANIES));
         lv.setTextFilterEnabled(true);

         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int pos, long arg3) {
               Intent intent = new Intent(CompanyActivity.this, DetailActivity.class);
               intent.putExtra("COMPANY", lst.get(pos));
               startActivity(intent);

            }

         });
      }
   }
}
