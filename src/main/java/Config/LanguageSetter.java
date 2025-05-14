package Config;

import java.util.ResourceBundle;

public class LanguageSetter {
    // 1. Volatile instance for safe publication
    private static volatile LanguageSetter instance;

    // 2. Private constructor prevents external instantiation
    private LanguageSetter() { }

    // 3. Double‑checked locking for lazy, thread‑safe singleton
    public static LanguageSetter getInstance() {
        if (instance == null) {
            synchronized (LanguageSetter.class) {
                if (instance == null) {
                    instance = new LanguageSetter();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the localized value for the given key in the specified language.
     * Falls back to the key itself if no bundle or key is found.
     *
     * @param key       the resource key
     * @param language  two‑letter code ("en", "ar", etc.)
     * @return localized string or key if missing
     */
    public String il8n(String key, String language) {
        String bundleName;
        switch (language) {
            case "en":
                bundleName = "Config/resource_bundle_en_US";
                break;
            case "ar":
                bundleName = "Config/resource_bundle_ar_EG";
                break;
            default:
                // unknown language: return key directly
                return key;
        }

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
            return bundle.getString(key);
        } catch (Exception e) {
            // missing bundle or key: log and return key
            System.err.println("Missing resource for key '" + key + "' in bundle '" + bundleName + "'");
            return key;
        }
    }
}
