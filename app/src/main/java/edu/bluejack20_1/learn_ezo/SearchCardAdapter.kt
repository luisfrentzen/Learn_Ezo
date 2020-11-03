package edu.bluejack20_1.learn_ezo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.search_card.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchCardAdapter(val result : ArrayList<Player>, var user : Player, var following_list : ArrayList<String>) : RecyclerView.Adapter<SearchCardAdapter.ViewHolder>() {

    lateinit var ctx : Context

    val db = FirebaseDatabase.getInstance()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nametv = itemView.nameTv
        val leveltv = itemView.levelTv
        val btn = itemView.follow_btn
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        ctx = context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.search_card, parent, false)

        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val u = result.get(position)
        holder.nametv.text = u.name
        val level = "Level " + (u.exp / 25)
        holder.leveltv.text = level

        Log.d("follow", following_list.toString())

        if (following_list.contains(u.id.toString())){
            holder.btn.text = ctx.resources.getString(ctx.resources.getIdentifier("followed", "string", ctx.packageName))
            holder.btn.backgroundTintList = ctx.getColorStateList(ctx.resources.getIdentifier("lightGray", "color", ctx.packageName))
        }
        else{
            holder.btn.text = ctx.resources.getString(ctx.resources.getIdentifier("follow", "string", ctx.packageName))
            holder.btn.backgroundTintList = ctx.getColorStateList(ctx.resources.getIdentifier("bluesky", "color", ctx.packageName))
        }

        holder.btn.setOnClickListener{
            if(holder.btn.text.equals(ctx.resources.getString(ctx.resources.getIdentifier("follow", "string", ctx.packageName))))
            {
                holder.btn.text = ctx.resources.getString(ctx.resources.getIdentifier("followed", "string", ctx.packageName))
                holder.btn.backgroundTintList = ctx.getColorStateList(ctx.resources.getIdentifier("lightGray", "color", ctx.packageName))

                val ref = db.getReference("users").child(user.id.toString()).child("following-list").child(u.id.toString())
                ref.setValue(1)

                following_list.add(u.id.toString())

                val reffollower = db.getReference("users").child(u.id.toString()).child("follower-list").child(user.id.toString())
                reffollower.setValue(1)

//                val databaseNotif = FirebaseDatabase.getInstance().getReference("notifications")
//
//                val curNotif = databaseNotif.child(UUID.randomUUID().toString())
//                curNotif.child("userid").setValue(user.id.toString())
//                curNotif.child("type").setValue("follow")
//
//                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                val date = parser.format(Calendar.getInstance().time)
//                curNotif.child("timestamp").setValue(date)

                val databaseA : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                user.id.toString()).child("achievements").child("4")

                databaseA.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val temp_snapshot = snapshot.value.toString()

                        if(temp_snapshot == "null"){
                            databaseA.child("currentProgress").setValue(1)
                            databaseA.child("isComplete").setValue(0)
                        }else if(snapshot.child("isComplete").value != 1){
                            if(snapshot.child("currentProgress").value.toString().toInt() < 3){
                                databaseA.child("currentProgress").setValue(snapshot.child("currentProgress").value.toString().toInt() + 1)
                            }

                            else{
                                databaseA.child("isComplete").setValue(1)
                            }
                        }

                    }

                })

                val databaseB : DatabaseReference = FirebaseDatabase.getInstance().getReference("accomplishment").child(
                    u.id.toString()).child("achievements").child("6")

                databaseB.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val temp_snapshot = snapshot.value.toString()

                        if(temp_snapshot == "null"){
                            databaseB.child("currentProgress").setValue(1)
                            databaseB.child("isComplete").setValue(0)
                        }else if(snapshot.child("isComplete").value != 1){
                            if(snapshot.child("currentProgress").value.toString().toInt() < 3){
                                databaseB.child("currentProgress").setValue(snapshot.child("currentProgress").value.toString().toInt() + 1)
                            }

                            if(snapshot.child("currentProgress").value.toString().toInt() == 3){
                                databaseB.child("isComplete").setValue(1)
                            }
                        }

                    }

                })

            }
            else {
                holder.btn.text = ctx.resources.getString(ctx.resources.getIdentifier("follow", "string", ctx.packageName))
                holder.btn.backgroundTintList = ctx.getColorStateList(ctx.resources.getIdentifier("bluesky", "color", ctx.packageName))

                following_list.remove(u.id.toString())

                val ref = db.getReference("users").child(user.id.toString()).child("following-list").child(u.id.toString())
                ref.removeValue()

                val reffollower = db.getReference("users").child(u.id.toString()).child("follower-list").child(user.id.toString())
                reffollower.removeValue()
            }
        }
    }
}