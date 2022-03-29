package com.example.callcalendertest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callcalendertest.databinding.ItemCalllogLayoutBinding

data class Call(val id: String?, val name: String?, val phone: String?, val type: String?, val date: String?)

class CallLogAdapter(val list:List<Call>) : RecyclerView.Adapter<CallLogAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calllog_layout, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val phone = list[position]
        holder.setPhone(phone)
    }

    @SuppressLint("MissingPermission")
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCall: Call? = null

        fun setPhone(call: Call) {
            val textName = itemView.findViewById<TextView>(R.id.textName)
            val textPhone = itemView.findViewById<TextView>(R.id.textPhone)
            val textType = itemView.findViewById<TextView>(R.id.textType)
            mCall = call
            textName.text = call.name?:"알 수 없음"
            textPhone.text = call.phone?:"없는 번호입니다."
            when(call.type) {
                "1" -> textType.text = "수신"
                "2" -> textType.text = "발신"
                "3" -> textType.text = "부재중"
            }

        }
    }
}