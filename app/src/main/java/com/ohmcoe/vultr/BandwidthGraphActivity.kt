package com.ohmcoe.vultr

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout

import org.achartengine.ChartFactory
import org.achartengine.chart.BarChart
import org.achartengine.model.XYMultipleSeriesDataset
import org.achartengine.model.XYSeries
import org.achartengine.renderer.XYMultipleSeriesRenderer
import org.achartengine.renderer.XYSeriesRenderer

class BandwidthGraphActivity : AppCompatActivity() {

    internal var dates: Array<String>? = null
    internal var inbound: DoubleArray? = null
    internal var outbound: DoubleArray? = null
    internal var x: IntArray? = null
    internal var bandwidthGraph: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_bandwidth_graph)

        bandwidthGraph = findViewById(R.id.bandwidthGraph) as LinearLayout

        val intent = intent
        x = intent.getIntArrayExtra("x")
        dates = intent.getStringArrayExtra("dates")
        inbound = intent.getDoubleArrayExtra("inbound")
        outbound = intent.getDoubleArrayExtra("outbound")

        draw()
    }

    protected fun draw() {
        val inboundSeries = XYSeries("Inbound")
        val outboundSeries = XYSeries("Outbound")

        for (i in x!!.indices) {
            inboundSeries.add(i.toDouble(), inbound!!.get(i))
            outboundSeries.add(i.toDouble(), outbound!!.get(i))
        }

        //Dataset
        val dataset = XYMultipleSeriesDataset()
        dataset.addSeries(inboundSeries)
        dataset.addSeries(outboundSeries)

        //set color
        val inboundRender = XYSeriesRenderer()
        inboundRender.setColor(Color.BLUE)
        inboundRender.setFillPoints(true)
        inboundRender.setLineWidth(2f)
        inboundRender.setDisplayChartValues(true)

        val outBoundRender = XYSeriesRenderer()
        outBoundRender.setColor(Color.GRAY)
        outBoundRender.setFillPoints(true)
        outBoundRender.setLineWidth(2f)
        outBoundRender.setDisplayChartValues(true)

        //multi render
        val multipleSeriesRenderer = XYMultipleSeriesRenderer()
        multipleSeriesRenderer.orientation = XYMultipleSeriesRenderer.Orientation.HORIZONTAL
        multipleSeriesRenderer.xLabels = 0
        multipleSeriesRenderer.chartTitle = "Bandwidth"
        multipleSeriesRenderer.xTitle = "Date"
        multipleSeriesRenderer.yTitle = "GB"
        multipleSeriesRenderer.isFitLegend = true
        multipleSeriesRenderer.labelsTextSize = 18f
        multipleSeriesRenderer.axisTitleTextSize = 24f
        multipleSeriesRenderer.chartTitleTextSize = 28f

        //space between two bar
        multipleSeriesRenderer.setBarSpacing(0.5)

        //enable zoom
        multipleSeriesRenderer.setZoomEnabled(true)

        //background
        multipleSeriesRenderer.setApplyBackgroundColor(true)
        multipleSeriesRenderer.setBackgroundColor(Color.TRANSPARENT)

        for (i in x!!.indices) {
            multipleSeriesRenderer.addXTextLabel(i.toDouble(), dates!!.get(i))
        }


        multipleSeriesRenderer.addSeriesRenderer(inboundRender)
        multipleSeriesRenderer.addSeriesRenderer(outBoundRender)

        bandwidthGraph!!.removeAllViews()
        val mChart = ChartFactory.getBarChartView(this, dataset, multipleSeriesRenderer, BarChart.Type.DEFAULT)
        bandwidthGraph!!.addView(mChart)
    }
}
