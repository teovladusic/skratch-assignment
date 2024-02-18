package com.teovladusic.friends_map_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.CameraChangedCallback
import com.mapbox.maps.CameraState
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.expressions.dsl.generated.toNumber
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.ProjectionName
import com.mapbox.maps.extension.style.projection.generated.Projection
import com.mapbox.maps.extension.style.projection.generated.setProjection
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.toCameraOptions
import com.mapbox.maps.viewannotation.annotatedLayerFeature
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.teovladusic.core.common.extension.findActivity

class FriendsMapView(
    context: Context,
    attrs: AttributeSet?
) : MapView(context, attrs) {

    private var clickListener: FriendsMapViewClickListener? = null

    constructor(
        context: Context,
        cameraState: CameraState,
        clickListener: FriendsMapViewClickListener,
        cameraChangedCallback: CameraChangedCallback
    ) : this(context, null) {
        mapboxMap.setCamera(cameraState.toCameraOptions())
        mapboxMap.subscribeCameraChanged(cameraChangedCallback)
        this.clickListener = clickListener
    }

    companion object {
        private const val SOURCE_ID = "friends-source"
        private const val UNCLUSTERED_LAYER_ID = "friend-points"
        private const val CLUSTERED_LAYER_ID = "cluster-points"
        private const val CLUSTER_MAX_ZOOM = 20L
        private const val CLUSTER_RADIUS = 75L
        private const val POINTS_COUNT_TEXT_SIZE = 17.0
        private const val CLUSTERED_SYMBOL_LAYER_IMAGE_ID = "circle-white"
    }

    private var features = emptyList<Feature>()

    init {
        configureMapElements()
        mapboxMap.loadStyle(Style.OUTDOORS) { style ->
            style.setProjection(Projection(ProjectionName.MERCATOR))
            style.addSymbolLayerIfNeeded()

            if (features.isNotEmpty()) {
                val handler = Handler(Looper.getMainLooper())

                // Delay drawing annotations (can lead to UI freezing) to let map load and have
                // smoother navigation
                handler.postDelayed({
                    style.addUpdateGeoJsonSource(features)
                    addViewAnnotations(features)
                }, 1_000)
            }
        }
    }

    private fun configureMapElements() {
        logo.enabled = false
        attribution.enabled = false
        scalebar.enabled = false
        compass.enabled = false
    }

    private fun Style.addUpdateGeoJsonSource(features: List<Feature>) {
        val source = getSource(SOURCE_ID)
        if (source == null) {
            val geoJsonSource = GeoJsonSource.Builder(SOURCE_ID)
                .featureCollection(FeatureCollection.fromFeatures(features))
                .cluster(true)
                .clusterMaxZoom(CLUSTER_MAX_ZOOM)
                .clusterRadius(CLUSTER_RADIUS)
                .build()

            addSource(geoJsonSource)
        } else {
            updateGeoJSONSourceFeatures(SOURCE_ID, "", features)
        }
    }

    private fun Style.addSymbolLayerIfNeeded() {
        if (getLayer(UNCLUSTERED_LAYER_ID) != null) return

        val unclusteredSymbolLayer = SymbolLayer(UNCLUSTERED_LAYER_ID, SOURCE_ID)
        unclusteredSymbolLayer.textField(".") // Must have value to be shown
        unclusteredSymbolLayer.textSize(0.1)
        addLayer(unclusteredSymbolLayer)

        val clusteredSymbolLayer = SymbolLayer(CLUSTERED_LAYER_ID, SOURCE_ID)
        clusteredSymbolLayer.textField(Expression.toString(get("point_count")))
        clusteredSymbolLayer.textSize(POINTS_COUNT_TEXT_SIZE)

        getClusteredLayerIcon()?.let {
            addImage(CLUSTERED_SYMBOL_LAYER_IMAGE_ID, it)
            clusteredSymbolLayer.iconImage(CLUSTERED_SYMBOL_LAYER_IMAGE_ID)
        }

        val pointCount = toNumber { get("point_count") }
        // Add a filter to the cluster layer that hides the icons based on "point_count"
        clusteredSymbolLayer.filter(
            Expression.all(
                Expression.has("point_count"),
                Expression.gt(pointCount, Expression.literal(1))
            )
        )

        addLayer(clusteredSymbolLayer)
    }

    private fun getClusteredLayerIcon(): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_circle) ?: return null

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun updateFeatures(newFeatures: List<Feature>) {
        if (newFeatures != features) {
            features = newFeatures

            mapboxMap.style?.let { style ->
                style.clear()
                style.addUpdateGeoJsonSource(newFeatures)
                style.addSymbolLayerIfNeeded()
                addViewAnnotations(newFeatures)
            }
        }
    }

    private fun Style.clear() {
        removeStyleLayer(UNCLUSTERED_LAYER_ID)
        removeStyleLayer(CLUSTERED_LAYER_ID)
        removeStyleSource(SOURCE_ID)
        viewAnnotationManager.removeAllViewAnnotations()
    }

    private fun addViewAnnotations(features: List<Feature>) {
        features.forEach { feature ->
            viewAnnotationManager.addViewAnnotation(
                inflateView(feature),
                options = viewAnnotationOptions {
                    annotatedLayerFeature(UNCLUSTERED_LAYER_ID) {
                        featureId(feature.id())
                    }
                }
            )
        }
    }

    private fun inflateView(feature: Feature): View {
        val activity = context.findActivity()

        val view = activity.layoutInflater.inflate(R.layout.view_annotation, this, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val text = view.findViewById<TextView>(R.id.text)
        val image = view.findViewById<ImageView>(R.id.image)

        text.text = feature.getStringProperty("name")

        Glide.with(activity.baseContext)
            .load(feature.getStringProperty("image"))
            .centerCrop()
            .into(image)

        clickListener?.let { clickListener ->
            val id = feature.id() ?: return@let
            view.setOnClickListener {
                clickListener.onFriendClick(id)
            }
        }

        return view
    }
}

interface FriendsMapViewClickListener {
    fun onFriendClick(id: String)
}