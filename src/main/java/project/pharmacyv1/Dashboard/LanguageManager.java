// File: project/pharmacyv1/utils/PieChartManager.java
package project.pharmacyv1.Dashboard;

import Config.LanguageSetter;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;

import java.util.Map;

public class LanguageManager {
    private final BorderPane root;
    private final Map<Control,String> sideButtons;
    private final Map<Labeled,String> menuMapping;

    public LanguageManager(BorderPane root,
                           Map<Control,String> sideButtons,
                           Map<Labeled,String> menuMapping) {
        this.root = root;
        this.sideButtons = sideButtons;
        this.menuMapping = menuMapping;
    }

    public void apply(String lang) {
        LanguageSetter LS = LanguageSetter.getInstance();
        root.setNodeOrientation("ar".equals(lang)
                ? NodeOrientation.RIGHT_TO_LEFT
                : NodeOrientation.LEFT_TO_RIGHT
        );

        sideButtons.forEach((ctrl, key) ->
                ctrl.setTooltip(new Tooltip(LS.il8n(key, lang)))
        );
        menuMapping.forEach((lbl, key) ->
                lbl.setText(LS.il8n(key, lang))
        );
    }
}