package com.ohmcoe.vultr;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class BandwidthGraphActivity extends AppCompatActivity {

    String[] dates;
    double[] inbound;
    double[] outbound;
    int[] x;
    LinearLayout bandwidthGraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bandwidth_graph);

        bandwidthGraph = (LinearLayout)findViewById(R.id.bandwidthGraph);

        Intent intent = getIntent();
        x = intent.getIntArrayExtra("x");
        dates = intent.getStringArrayExtra("dates");
        inbound = intent.getDoubleArrayExtra("inbound");
        outbound = intent.getDoubleArrayExtra("outbound");


        draw();
    }

    protected void draw()
    {
        XYSeries inboundSeries = new XYSeries("Inbound");
        XYSeries outboundSeries = new XYSeries("Outbound");

        for (int i = 0; i < x.length; i++) {
            inboundSeries.add(i, inbound[i]);
            outboundSeries.add(i, outbound[i]);
        }

        //Dataset
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(inboundSeries);
        dataset.addSeries(outboundSeries);

        //set color
        XYSeriesRenderer inboundRender = new XYSeriesRenderer();
        inboundRender.setColor(Color.BLUE);
        inboundRender.setFillPoints(true);
        inboundRender.setLineWidth(2);
        inboundRender.setDisplayChartValues(true);

        XYSeriesRenderer outBoundRender = new XYSeriesRenderer();
        outBoundRender.setColor(Color.GRAY);
        outBoundRender.setFillPoints(true);
        outBoundRender.setLineWidth(2);
        outBoundRender.setDisplayChartValues(true);

        //multi render
        XYMultipleSeriesRenderer multipleSeriesRenderer = new XYMultipleSeriesRenderer();
        multipleSeriesRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multipleSeriesRenderer.setXLabels(0);
        multipleSeriesRenderer.setChartTitle("Bandwidth");
        multipleSeriesRenderer.setXTitle("Date");
        multipleSeriesRenderer.setYTitle("GB");

        //space between two bar
        multipleSeriesRenderer.setBarSpacing(0.5);

        //enable zoom
        multipleSeriesRenderer.setZoomEnabled(true);

        //background
        multipleSeriesRenderer.setBackgroundColor(Color.TRANSPARENT);

        for (int i = 0; i < x.length; i++)
        {
            multipleSeriesRenderer.addXTextLabel(i, dates[i]);
        }


        multipleSeriesRenderer.addSeriesRenderer(inboundRender);
        multipleSeriesRenderer.addSeriesRenderer(outBoundRender);

        bandwidthGraph.removeAllViews();
        View mChart = ChartFactory.getBarChartView(this, dataset, multipleSeriesRenderer, BarChart.Type.DEFAULT);
        bandwidthGraph.addView(mChart);
    }
}
