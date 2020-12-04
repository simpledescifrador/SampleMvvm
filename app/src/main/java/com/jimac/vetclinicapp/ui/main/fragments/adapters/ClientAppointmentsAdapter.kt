package com.jimac.vetclinicapp.ui.main.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jimac.vetclinicapp.R
import com.jimac.vetclinicapp.data.models.Appointment
import com.jimac.vetclinicapp.utils.AppUtils
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.Calendar

class ClientAppointmentsAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<ClientAppointmentsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var parentView: CardView = itemView.findViewById(R.id.cardView_itemAppointment_parent)
        var appointmentDateMonth: TextView = itemView.findViewById(R.id.textView_itemAppointment_dateMonth)
        var appointmentDateDay: TextView = itemView.findViewById(R.id.textView_itemAppointment_dateDay)
        var appointmentStartTime: TextView = itemView.findViewById(R.id.textView_itemAppointment_startTime)
        var serviceTitle: TextView = itemView.findViewById(R.id.textView_itemAppointment_serviceTitle)
        var serviceDuration: TextView = itemView.findViewById(R.id.textView_itemAppointment_serviceDuration)
        var petName: TextView = itemView.findViewById(R.id.textView_itemAppointment_petName)
        var appointmentNumber: TextView =
            itemView.findViewById(R.id.textView_itemAppointment_appointmentNumber)
        var appointmentStatus: TextView =
            itemView.findViewById(R.id.textView_itemAppointment_appointmentStatus)
        var appointmentStatusIcon: ImageView =
            itemView.findViewById(R.id.imageView_itemAppointment_statusIcon)
        var appointmentDateCreated: TextView = itemView.findViewById(R.id.textView_itemAppointment_dateCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]

        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(appointment.appointmentDate)
        cal.time = date
        //Set Month
        val month: Int = cal.get(Calendar.MONTH) + 1
        holder.appointmentDateMonth.text = AppUtils.getMonthName(month.toString())
        //Set Date
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        holder.appointmentDateDay.text = day.toString()
        //Set Time
        holder.appointmentStartTime.text = AppUtils.convertTime(appointment.startTime)
        //Set Service Title
        holder.serviceTitle.text = appointment.appointmentService?.title
        //Set Service Duration
        holder.serviceDuration.text = "Duration: ${appointment.appointmentService?.duration.toString()} mins"
        //Set Pet Name
        holder.petName.text = "Pet Name: ${appointment.pet?.name}"
        //Set Appointment Number
        holder.appointmentNumber.text = "No: ${appointment.appointmentNumber}"

        holder.appointmentStatus.text = appointment.appointmentStatus?.status

        val prettyTime = PrettyTime()
        val dateCreated = AppUtils.getDateFromString(appointment.appointmentDateCreated, "yyyy-MM-dd hh:mm:ss")
        holder.appointmentDateCreated.text = prettyTime.format(dateCreated)

        when (appointment.appointmentStatus?.status) {
            "NEW" -> holder.appointmentStatusIcon.setImageResource(R.drawable.ic_new_24)
            "CONFIRMED" -> holder.appointmentStatusIcon.setImageResource(R.drawable.ic_thumb_up_24)
        }

        holder.parentView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }
}