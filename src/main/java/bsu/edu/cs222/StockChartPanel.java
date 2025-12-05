package bsu.edu.cs222;

import javax.swing.*;
import java.awt.*;
import java.util.List;


  //Very simple line chart panel for a 5-day price history.
 //Uses only core Swing / AWT, no external libraries.

public class StockChartPanel extends JPanel {

    private final PriceHistory history;
    private double minPrice;
    private double maxPrice;

    public StockChartPanel(PriceHistory history) {
        this.history = history;
        computeRange();
        setBackground(Color.WHITE);
    }

    private void computeRange() {
        List<DailyPrice> days = history.getDays();
        minPrice = Double.POSITIVE_INFINITY;
        maxPrice = Double.NEGATIVE_INFINITY;
        for (DailyPrice day : days) {
            double p = day.getClose().doubleValue();
            if (p < minPrice) minPrice = p;
            if (p > maxPrice) maxPrice = p;
        }
        if (minPrice == maxPrice) {
            minPrice -= 1.0;
            maxPrice += 1.0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (history.getDays().isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int leftMargin = 50;
        int rightMargin = 20;
        int topMargin = 20;
        int bottomMargin = 40;

        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;

        // axes
        g2.setColor(Color.BLACK);
        int xAxisY = height - bottomMargin;
        int yAxisX = leftMargin;
        g2.drawLine(yAxisX, topMargin, yAxisX, xAxisY);
        g2.drawLine(yAxisX, xAxisY, width - rightMargin, xAxisY);

        List<DailyPrice> days = history.getDays();
        int n = days.size();

        // map prices to Y
        double priceRange = maxPrice - minPrice;

        // we draw oldest on the left, newest on the right
        // days list is newest-first in our parser, so iterate backwards
        int prevX = -1;
        int prevY = -1;

        for (int i = 0; i < n; i++) {
            DailyPrice day = days.get(n - 1 - i); // reverse order
            double price = day.getClose().doubleValue();

            double normalized = (price - minPrice) / priceRange; // 0..1
            int x = yAxisX + (int) (chartWidth * (i / (double) (n - 1)));
            int y = topMargin + (int) ((1.0 - normalized) * chartHeight);

            // draw point
            g2.setColor(new Color(0, 102, 204));
            g2.fillOval(x - 3, y - 3, 6, 6);

            // connect line
            if (prevX != -1) {
                g2.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }

        // labels for min/max
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(String.format("%.2f", maxPrice), 5, topMargin + 10);
        g2.drawString(String.format("%.2f", minPrice), 5, topMargin + chartHeight);

        // title
        g2.drawString("5-day closing prices for " + history.getSymbol(),
                leftMargin, topMargin - 5);

        g2.dispose();
    }
}
