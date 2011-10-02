package kz.edu.sdu.jg;

import kz.edu.sdu.jg.models.Company;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class DetailActivity extends Activity {
   TextView title;
   TextView view;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.details);

      title = (TextView) findViewById(R.id.detailtitle);
      view = (TextView) findViewById(R.id.detailview);

      if (getIntent().getExtras().containsKey("COMPANY")) {
         Company c = (Company) getIntent().getExtras().get("COMPANY");
         title.setText(c.name);
         view.setText(Html.fromHtml(c.content));
      }
   }
}
