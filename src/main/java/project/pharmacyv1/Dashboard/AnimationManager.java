package project.pharmacyv1.Dashboard;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationManager {
    private final Timeline timeline;

    public AnimationManager(Node purchaseLabel, Node profitLabel,
                            Node purchaseChart, Node profitChart) {
        FadeTransition plIn  = fade(purchaseLabel, 0, 1);
        FadeTransition plOut = fade(purchaseLabel, 1, 0);
        FadeTransition prIn  = fade(profitLabel,   0, 1);
        FadeTransition prOut = fade(profitLabel,   1, 0);
        FadeTransition pcIn  = fade(purchaseChart, 0, 1);
        FadeTransition pcOut = fade(purchaseChart, 1, 0);
        FadeTransition pfIn  = fade(profitChart,   0, 1);
        FadeTransition pfOut = fade(profitChart,   1, 0);

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> { pcIn.play(); pfIn.play(); plOut.play(); prOut.play(); }),
                new KeyFrame(Duration.seconds(5), e -> { pcOut.play(); pfOut.play(); plIn.play(); prIn.play(); }),
                new KeyFrame(Duration.seconds(9)) // pause
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private FadeTransition fade(Node node, double from, double to) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), node);
        ft.setFromValue(from);
        ft.setToValue(to);
        return ft;
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
