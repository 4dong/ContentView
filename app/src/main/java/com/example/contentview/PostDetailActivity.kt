package com.example.contentview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PostDetailActivity : AppCompatActivity() {
    private lateinit var postRepository: PostRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        postRepository = PostRepository(this)

        val postId = intent.getLongExtra("POST_ID", -1)
        if (postId == -1L) {
            Toast.makeText(this, "잘못된 게시물 ID입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val post = postRepository.getPost(postId)
        if (post == null) {
            Toast.makeText(this, "게시물을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val contentTextView = findViewById<TextView>(R.id.contentTextView)

        post?.let {
            titleTextView.text = it.title
            contentTextView.text = it.content
            imageView.setImageURI(Uri.parse(it.imageUri))
        }

        val editButton = findViewById<Button>(R.id.editButton)
        val backButton = findViewById<Button>(R.id.back_main)
        editButton.setOnClickListener{
            val intent = Intent(this, EditPostActivity::class.java).apply {
                putExtra("POST_ID", postId)
            }
            startActivity(intent)
        }
        backButton.setOnClickListener{
            val intent = Intent(this, NewActivity::class.java).apply {
                putExtra("POST_ID", postId)
            }
            startActivity(intent)
        }
    }
}
