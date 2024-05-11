package Config;

import java.util.ResourceBundle;

public class LanguageSetter {

    public String il8n(String key , String language) {
        if(language.equals("en"))
        {
            ResourceBundle bundle = ResourceBundle.getBundle("Config/resource_bundle_en_US");
            return bundle.getString(key);
        }
        else if(language.equals("ar"))
        {
            ResourceBundle bundle = ResourceBundle.getBundle("Config/resource_bundle_ar_EG");
            return bundle.getString(key);
        }
        return key;
    }
}