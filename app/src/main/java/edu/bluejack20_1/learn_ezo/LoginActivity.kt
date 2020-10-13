package edu.bluejack20_1.learn_ezo

import android.content.Context
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import edu.bluejack20_1.learn_ezo.R


class   LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN : Int = 1
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    lateinit var firebaseAuth : FirebaseAuth

    lateinit var googleButton : Button

    var userLogged : Player? = null

    var databaseU : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSignIn()
        setContentView(R.layout.activity_login)

        googleButton = findViewById(R.id.sign_in_button)

        firebaseAuth = Firebase.auth

        googleButton.setOnClickListener{
            signInWithGoogle()
        }
    }

    private fun storeUserData(id: String, name: String, ctx: Context){
        val p : Player = Player(id, name)



        databaseU.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    if (data.key == p.id){

                        userLogged = data.getValue(Player::class.java) as Player

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseU.child(id).setValue(p)

        Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                storeUserData(account.id.toString(), account.displayName.toString(), this)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google authentication failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    startActivity(NavBottom.getLaunchIntent(this, userLogged as Player))
                } else {
                    Toast.makeText(this, "Firebase authentication failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun configureSignIn(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signInWithGoogle(){
        val signInIntent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()



        val user = FirebaseAuth.getInstance().currentUser


        if (user != null) {
            val acct = GoogleSignIn.getLastSignedInAccount(this)

            if (acct != null) {

                databaseU.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(data in snapshot.children){
                            if (data.key == acct.id){

                                val u = data.getValue(Player::class.java) as Player

                                startNavBottomActivity(u)

                                return
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        }
    }

    fun startNavBottomActivity(u : Player) {
        startActivity(NavBottom.getLaunchIntent(this, u))
        finish()
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}