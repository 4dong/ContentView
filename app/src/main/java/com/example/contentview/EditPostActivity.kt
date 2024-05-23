package com.example.contentview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditPostActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var postRepository: PostRepository
    private var postId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        postRepository = PostRepository(this)

        postId = intent.getLongExtra("POST_ID", -1)
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
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val contentEditText = findViewById<EditText>(R.id.contentEditText)
        val updateButton = findViewById<Button>(R.id.updateButton)

        post.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
            selectedImageUri = Uri.parse(it.imageUri)
            imageView.setImageURI(selectedImageUri)
        }

        imageView.setOnClickListener {
            openImagePicker()
        }

        updateButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty() && selectedImageUri != null) {
                // Update post logic here
                postRepository.updatePost(postId, title, content, selectedImageUri.toString())
                Toast.makeText(this, "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PostDetailActivity::class.java).apply {
                    putExtra("POST_ID", postId)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "제목, 내용, 이미지를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageURI(selectedImageUri)
        }
    }
}