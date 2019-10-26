package dev.gumil.places.presentation.list

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import dev.gumil.places.R
import dev.gumil.places.domain.Place

internal interface ViewItem<M> {

    val layout: Int

    fun bind(view: View, item: M)
}

internal class PlaceViewItem : ViewItem<Place> {

    override val layout: Int = R.layout.item_place

    override fun bind(view: View, item: Place) {
        val name = view.findViewById<TextView>(R.id.item_name)
        val rating = view.findViewById<TextView>(R.id.item_text_rating)
        val ratingBar = view.findViewById<RatingBar>(R.id.item_ratingBar)
        val open = view.findViewById<TextView>(R.id.item_open)
        val distance = view.findViewById<TextView>(R.id.item_distance)

        name.text = item.name
        rating.text = String.format("%.1f", item.rating)
        ratingBar.rating = item.rating.toFloat()
        formatOpen(open, item.isOpen)
        distance.text = "4.4km"
    }

    private fun formatOpen(textView: TextView, isOpen: Boolean) {
        val textColor = if (isOpen) R.color.open else R.color.closed
        val text = if (isOpen) R.string.open else R.string.closed
        textView.setTextColor(ContextCompat.getColor(textView.context, textColor))
        textView.setText(text)
    }

}

internal class FooterViewItem : ViewItem<Nothing> {
    override val layout: Int = R.layout.item_progress

    override fun bind(view: View, item: Nothing) {
        //no bindings
    }
}
