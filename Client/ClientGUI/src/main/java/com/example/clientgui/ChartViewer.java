package com.example.clientgui;

import javafx.scene.paint.Color;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

public class ChartViewer{

    private static final long serialVersionUID = 1L;

    static {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true));
    }

    private DefaultCategoryDataset dataset;
    public ChartViewer(DefaultCategoryDataset dataset) {
        this.dataset = dataset;
    }

    public JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createBarChart(
                "Статистика популярности книг",
                null,                    // x-axis label
                "Количество",                 // y-axis label
                dataset);
        chart.setBackgroundPaint(new ChartColor(255, 255 ,255));
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        ((BarRenderer)plot.getRenderer()).setBarPainter(new StandardBarPainter());

        BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, new ChartColor(0, 0 , 255));
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }
}

