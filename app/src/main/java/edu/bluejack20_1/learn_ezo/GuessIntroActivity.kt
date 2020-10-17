package edu.bluejack20_1.learn_ezo

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class GuessIntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_intro)

        val imgurl = "Image_pool/にんじん.jpg"

        val stref : StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://tpamobile-5d381.appspot.com/Image_pool/にんじん.jpg")
        val links = ArrayList<String>()

//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%84%E3%81%99.png?alt=media&token=c1d81e7d-2f9b-48a3-9a4b-a01c2f326cc2")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%88%E3%82%93%E3%81%B4%E3%81%A4.jpg?alt=media&token=37d7237c-f043-4be3-8dd3-dcec035aaed7")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%94%E3%81%BF%E3%81%B0%E3%81%93.png?alt=media&token=aac617a0-7fe0-4c8e-8a6e-3dafc6c07c87")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%8B%E3%81%B0%E3%82%93.jpg?alt=media&token=4ce6856c-26f6-49f8-b875-17587a63c212")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%8A%E3%81%A1%E3%82%83.png?alt=media&token=21bb6240-0c14-4d04-b337-bf884764f8b1")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%A4%E3%81%8F%E3%81%88.jpg?alt=media&token=e71b323d-e85a-48e6-ae15-4c1ef4dce73b")
//        links.add("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%A4%E3%81%8F%E3%81%88.jpg?alt=media&token=e71b323d-e85a-48e6-ae15-4c1ef4dce73b")
//
        val imView = findViewById<ImageView>(R.id.iv_obj)

        Glide.with(this )
            .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/tpamobile-5d381.appspot.com/o/Image_pool%2F%E3%81%AB%E3%82%93%E3%81%98%E3%82%93.jpg?alt=media&token=b96a67d3-051d-4c63-b6b3-47694843f322"))
            .into(imView)
    }
}