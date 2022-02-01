package com.essadrati.lampcontroller

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.essadrati.lampcontroller.databinding.ActivityMainBinding
import com.essadrati.lampcontroller.databinding.UserDialogBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit  var mBinding:ActivityMainBinding
    var isON:Boolean = false
    val lamps:MutableList<String> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.rvPicsList.layoutManager= LinearLayoutManager(this)
        database = FirebaseDatabase.getInstance().reference
       // val sw =mBinding.text
       // val lamps = listOf("l1","l2","l3","l4")
        getAllLamps()

        mBinding.button.setOnClickListener {
            addLamp(this)
        }
       /* sw.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            if (isChecked){
                sw.text = "turn OFF"
                database.child("user").child("lamp1").child("state").setValue(1)
            }else{
                sw.text = "turn ON"
                database.child("user").child("lamp1").child("state").setValue(0)
            }
            Toast.makeText(this@MainActivity, message,
                    Toast.LENGTH_SHORT).show()
        }*/
        val settings = getSharedPreferences("prefs", 0)
       // val prefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val previouslyStarted = settings.getBoolean("previouslyStarted ", false)

        Toast.makeText(this@MainActivity, previouslyStarted.toString(), Toast.LENGTH_SHORT).show()
        if (!previouslyStarted) {
            showDialog(this)
            val edit = settings.edit()
            edit.putBoolean("previouslyStarted ", true)
            edit.apply()
            Toast.makeText(this@MainActivity, "nice", Toast.LENGTH_SHORT).show()

        }
        //Toast.makeText(this, "1 hhhhhh", Toast.LENGTH_SHORT).show()
        val userName = settings.getString("name", "")

        Toast.makeText(this@MainActivity, "hhjj $userName", Toast.LENGTH_SHORT).show()
      //  database.child("user").child("lamp1").child("state").setValue(0)
       // writeNewLamp("15",0)

        val ref =database.child("user").child("lamp1").child("state")
       // ref.addValueEventListener(object : ValueEventListener {//addListenerForSingleValueEvent
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)//value==0 or 1
                /*  if(value==0){//0=OFF
                   // isON=false
                    sw.text = "turn ON"
                }else if(value==1){
                  // isON=true
                    sw.text = "turn OFF"

                }*/
                println(value)
                print("hhhhhhhhhh")
                //Toast.makeText(this@MainActivity, value.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_LONG)
                        .show()
            }
        })
       // Toast.makeText(this@MainActivity, "message1", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this@MainActivity, isON.toString(), Toast.LENGTH_SHORT).show()
       /* sw.setOnClickListener { v ->
           if(sw.text=="turn ON"){
               sw.text="turn OFF"
               database.child("user").child("lamp1").child("state").setValue(1)
           }else{
               sw.text="turn ON"
               database.child("user").child("lamp1").child("state").setValue(0)
           }
          /*  if(isON){
                sw.text = "turn OFF"
                //isON= false
                database.child("user").child("lamp1").child("state").setValue(1)
            }else{
                sw.text = "turn ON"
               // isON= true
                database.child("user").child("lamp1").child("state").setValue(0)
            }*/

        }*/


        Toast.makeText(this, "hhhh", Toast.LENGTH_SHORT).show()
    }

    private fun writeNewLamp(lampId: String, state: Int) {
        val lamp = Lamp(state)

        database.child("lamps").child(lampId).setValue(lamp)
        //update state
      //  database.child("users").child(userId).child("username").setValue(name)
    }
    fun readLamp(){
        val LampListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val lamp = dataSnapshot.getValue<Lamp>()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(LampListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog(context: Context) {
       //val dialog = Dialog(context)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //dialog.setCancelable(false)
        val binding:UserDialogBinding = UserDialogBinding.inflate(layoutInflater)
        //dialog.setContentView(binding.root)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{
            val name= binding.etName.text.toString().trim{it<= ' '}
            //val dateTime = LocalDateTime.now()
           // val date:String =dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            when {
                TextUtils.isEmpty(name) ->{
                    Toast.makeText(this, "enter name", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val settings = getSharedPreferences("prefs", 0)
                    val edit = settings.edit()
                    edit.putString("name", name)
                    edit.apply()
                    setContentView(mBinding.root)
                }
            }
        }

    }
    private fun addLamp(context: Context){
        val binding:UserDialogBinding = UserDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{
            val name= binding.etName.text.toString().trim{it<= ' '}
            when {
                TextUtils.isEmpty(name) ->{
                    Toast.makeText(context, "enter name", Toast.LENGTH_LONG).show()
                }
                else -> {
                    database.child("user").child(name).child("state").setValue(0)
                    setContentView(mBinding.root)
                }
            }
        }
    }
    private fun getAllLamps() {
        val ref =database.child("user")
      //  val lamps = mutableListOf<String>()


        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lamps:MutableList<String> = ArrayList()
                //val value = dataSnapshot.getValue(Int::class.java)//value==0 or 1
                val values = dataSnapshot.children
                for (v: DataSnapshot in values) {
                    // lamps.add(v.toString())
                    lamps.add(v.key.toString())

                }
                val lampAdapter =LampAdapter(this@MainActivity, lamps)
                mBinding.rvPicsList.adapter=lampAdapter
                //Toast.makeText(this@MainActivity, value.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_LONG)
                        .show()
            }
        })


    }

}