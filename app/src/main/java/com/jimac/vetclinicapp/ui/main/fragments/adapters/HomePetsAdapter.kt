package com.jimac.vetclinicapp.ui.main.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jimac.vetclinicapp.R
import com.jimac.vetclinicapp.data.models.Pet
import com.jimac.vetclinicapp.utils.ImageUtils
import de.hdodenhof.circleimageview.CircleImageView

class HomePetsAdapter(private val pets: List<Pet>) : RecyclerView.Adapter<HomePetsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var petNameTextView: TextView = itemView.findViewById(R.id.textView_homePet_name)
        var parentView: LinearLayout = itemView.findViewById(R.id.linearLayout_homePet_parent)
        var petImageImageView: CircleImageView = itemView.findViewById(R.id.imageView_homePet_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_homepet, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = pets[position]

        holder.petNameTextView.text = pet.name
        holder.petImageImageView.setImageBitmap(ImageUtils.convertBase64ToBitmap(pet.petImageUrl))

        holder.parentView.setOnClickListener { }
    }

    override fun getItemCount(): Int {
        return pets.size
    }
}