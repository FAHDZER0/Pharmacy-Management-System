package project.pharmacyv1.Dashboard;

import java.text.NumberFormat;
import java.text.ParseException;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class PieChartManager {
    private final Label purchaseLabel, profitLabel, totalSalesLabel;
    private final PieChart purchaseChart, profitChart;

    public PieChartManager(Label purchaseLabel, Label profitLabel, Label totalSalesLabel,
                           PieChart purchaseChart, PieChart profitChart) {
        this.purchaseLabel = purchaseLabel;
        this.profitLabel = profitLabel;
        this.totalSalesLabel = totalSalesLabel;
        this.purchaseChart = purchaseChart;
        this.profitChart = profitChart;
    }

    public void populateCharts() {
        try {
            NumberFormat fmt = NumberFormat.getNumberInstance();
            double purchase = fmt.parse(purchaseLabel.getText()).doubleValue();
            double profit  = fmt.parse(profitLabel.getText()).doubleValue();
            double total   = fmt.parse(totalSalesLabel.getText()).doubleValue();

            double purchasePct = purchase / total * 100;
            double profitPct   = profit   / total * 100;

            purchaseChart.getData().setAll(
                    new PieChart.Data("Purchased", purchasePct),
                    new PieChart.Data("Other",     100 - purchasePct)
            );
            profitChart.getData().setAll(
                    new PieChart.Data("Profit",    profitPct),
                    new PieChart.Data("Other",     100 - profitPct)
            );
        } catch (ParseException e) {
            e.printStackTrace();  // you could also log or show an alert
        }
    }
}
