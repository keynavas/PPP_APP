package com.essadrati.lampcontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.essadrati.lampcontroller.databinding.ActivityLampBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LampActivity : AppCompatActivity() {
    private lateinit  var mBinding:ActivityLampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLampBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val sw =mBinding.text

        val database = FirebaseDatabase.getInstance().reference
        val lamp =intent.getStringExtra("lamp")

        var ref =database.child("user").child(lamp!!).child("state")
        Toast.makeText(this,ref.toString(),Toast.LENGTH_SHORT).show()
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)//value==0 or 1
                  if(value==0){//0=OFF
                    sw.text = "turn ON"
                     sw.background=getDrawable(R.color.dark_chibi_color)
                }else if(value==1){
                    sw.text = "turn OFF"
                      sw.background=getDrawable(R.color.chibi_color)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LampActivity, "Error fetching data", Toast.LENGTH_LONG)
                        .show()
            }

        })
        sw.setOnClickListener { v ->
            if(sw.text=="turn ON"){
                sw.text="turn OFF"
                sw.background=getDrawable(R.color.chibi_color)
                database.child("user").child(lamp).child("state").setValue(1)
            }else{
                sw.text="turn ON"
                sw.background=getDrawable(R.color.dark_chibi_color)
                database.child("user").child(lamp).child("state").setValue(0)
            }


        }
    }
}