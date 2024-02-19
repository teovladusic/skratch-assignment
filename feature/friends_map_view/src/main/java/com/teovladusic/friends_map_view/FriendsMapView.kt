package com.teovladusic.friends_map_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsMapView(
    context: Context,
    attrs: AttributeSet?
) : MapView(context, attrs) {

    private var clickListener: FriendsMapViewClickListener? = null
    private var coroutineScope: CoroutineScope? = null
    private val asyncInflater = AsyncLayoutInflater(context)

    constructor(
        context: Context,
        cameraState: CameraState,
        clickListener: FriendsMapViewClickListener,
        cameraChangedCallback: CameraChangedCallback,
        coroutineScope: CoroutineScope
    ) : this(context, null) {
        mapboxMap.setCamera(cameraState.toCameraOptions())
        mapboxMap.subscribeCameraChanged(cameraChangedCallback)
        this.clickListener = clickListener
        this.coroutineScope = coroutineScope
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

            style.clear()
            style.addUpdateGeoJsonSource(features)
            style.addSymbolLayerIfNeeded()
            addViewAnnotations(features)
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
        if (getLayer(CLUSTERED_LAYER_ID) != null) return

        val unclusteredSymbolLayer = SymbolLayer(UNCLUSTERED_LAYER_ID, SOURCE_ID)
        unclusteredSymbolLayer.textField(".") // Must have value to be shown
        unclusteredSymbolLayer.textSize(0.1)
        unclusteredSymbolLayer.filter(Expression.not(Expression.has("point_count")))
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
        coroutineScope?.launch(Dispatchers.Default) {
            features.forEach { feature ->
                viewAnnotationManager.addViewAnnotation(
                    R.layout.view_annotation,
                    options = viewAnnotationOptions {
                        annotatedLayerFeature(UNCLUSTERED_LAYER_ID) {
                            featureId(feature.id())
                        }
                    },
                    asyncInflater = asyncInflater,
                    asyncInflateCallback = { view ->
                        val textView = view.findViewById<TextView>(R.id.text)
                        val imageView = view.findViewById<ImageView>(R.id.image)

                        textView.text = feature.getStringProperty("name")

                        Glide.with(context)
                            .load(feature.getStringProperty("image"))
                            .centerCrop()
                            .into(imageView)

                        clickListener?.let { clickListener ->
                            val id = feature.id() ?: return@let
                            view.setOnClickListener {
                                clickListener.onFriendClick(id)
                            }
                        }
                    }
                )
            }
        }
    }
}

interface FriendsMapViewClickListener {
    fun onFriendClick(id: String)
}