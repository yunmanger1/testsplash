package kz.edu.sdu.jg.utils;

import java.util.ArrayList;
import java.util.List;

import kz.edu.sdu.jg.models.Category;
import kz.edu.sdu.jg.models.Company;

public class YellowUtils {
   public static List<Category> allCats;
   public static List<Company> allData;
   static {
      allCats = new ArrayList<Category>();
      allData = new ArrayList<Company>();
      for (int i = 0; i < 25; i++) {
         Category tmp = new Category();
         tmp.name = String.format("Cat %d", i + 1);
         tmp.url = String.format("/kz/ru/companies/30/%d/", i + 1);
         allCats.add(tmp);
         Company tc = new Company();
         tc.content = String.format("<b>\"ANTEO %d\", ТОРГОВО-ПРОИЗВОДСТВЕНННАЯ КОМПАНИЯ, ТОО</b> <a href=\"/kz/ru/rubrics/608/\" title=\"виды деятельности\" target=\"_top\">...</a><br><br>АЛМАТЫ, УЛ. ПАНФИЛОВА, 20, ОФИС 21,  <br>Тел.: 3286070, 3173901, 2711285<br>Факс: 2711285<br>E-mail: info-anteo@mail.ru<br>URL: <a href=\"http://www.anteo.kz\" target=\"_blank\">http://www.anteo.kz</a><br>", i + 1);
         tc.url = String.format("/kz/ru/rubrics/%d/", i + 1);
         tc.category = tmp;
         allData.add(tc);
      }
   };

   public List<Category> getAllCategories() {
      return allCats;
   }

   public List<Category> getCategoryByLetter(char letter) {
      List<Category> result = new ArrayList<Category>();
      letter = Character.toLowerCase(letter);
      for (Category c : allCats) {
         if (Character.toLowerCase(c.name.charAt(0)) == letter) {
            result.add(c);
         }
      }
      return result;
   }

   public List<Company> getCompaniesByCategory(Category cat) {
      List<Company> result = new ArrayList<Company>();
      for (Company c : allData) {
         if (c.category.equals(cat)) {
            result.add(c);
         }
      }
      return result;
   }

}
