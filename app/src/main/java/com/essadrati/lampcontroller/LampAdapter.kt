package com.essadrati.lampcontroller

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.essadrati.lampcontroller.databinding.DeleteLampBinding
import com.essadrati.lampcontroller.databinding.LampItemBinding
import com.google.firebase.database.FirebaseDatabase

class LampAdapter( private  val context:Context,
                   private val lamps:MutableList<String>): RecyclerView.Adapter<LampAdapter.ViewHolder>(){





    class ViewHolder(view:LampItemBinding):RecyclerView.ViewHolder(view.root) {
        //val hh=view.hh
        val name = view.name
        val btn_more = view.moreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding:LampItemBinding= LampItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lamp =lamps[position]
        holder.name.text = lamp
        holder.name.setOnClickListener { v->
            val intent = Intent(context,LampActivity::class.java)
            intent.putExtra("lamp",lamp)
            context.startActivity(intent)

        }
        holder.btn_more.setOnClickListener {
            val  dialog = Dialog(context)
            val mBinding:DeleteLampBinding= DeleteLampBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(mBinding.root)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            //dialog.window?.setBackgroundDrawableResource(R.color.white_black)
            val database = FirebaseDatabase.getInstance().reference
            val ref =database.child("user").child(lamp)
            mBinding.deletePostedPic.setOnClickListener {
                ref.removeValue()
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    override fun getItemCount(): Int =lamps.size
}