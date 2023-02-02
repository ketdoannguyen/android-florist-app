package com.ntmk.myapp.view.home.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityUpdateProfileBinding
import com.ntmk.myapp.model.User
import com.squareup.picasso.Picasso
import java.util.*

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private var user : User = User()
    var progressDialog: ProgressDialog? = null
    var imageUri: Uri? = null
    var storageReference: StorageReference? = null
    private var checkAvartaFolder = true ;
    private final var animal1 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal1.PNG?alt=media&token=667d667a-0623-4a2f-88b6-7b13a5d22ba9"
    private final var animal2 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal2.png?alt=media&token=15601091-ba9f-4da0-9415-f3ee957fc89e"
    private final var animal3 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal3.png?alt=media&token=46da98ba-21db-48e8-9a42-be282d12cd54"
    private final var animal4 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal4.png?alt=media&token=b4123904-4b51-48a0-90af-917a0eca819a"
    private final var animal5 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal5.png?alt=media&token=86633b98-1aaa-47b6-ad39-d18a9baa5742"
    private final var animal6 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal6.png?alt=media&token=930f0ad8-2ab2-4fc3-99d9-635e1abd9c69"
    private final var animal7 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal7.png?alt=media&token=e232a095-1163-465b-8753-770d74042403"
    private final var animal8 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal8.png?alt=media&token=a0fd694f-f09d-4570-931d-06cb3774b5c9"
    private final var animal9 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fanimal9.png?alt=media&token=0f2f5cfb-b97e-4391-8c06-4fa9b93f17c0"

    private final var avarta1 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta1.png?alt=media&token=747f882d-c27e-49ea-949a-396eb1ee760e"
    private final var avarta2 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta2.png?alt=media&token=476087da-3c95-4343-bbf5-933ce464f5fc"
    private final var avarta3 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta3.png?alt=media&token=2330c3db-4b9a-4fcd-a6f0-18f6587e5f5c"
    private final var avarta4 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta4.png?alt=media&token=5b96ba7c-2982-4c2a-a54a-82234cee3d5c"
    private final var avarta5 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta5.png?alt=media&token=0029e6c8-b8fe-4ca1-9721-057372c2550c"
    private final var avarta6 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta6.png?alt=media&token=e9565bc4-e687-4c48-acf8-abc0209fe0ac"
    private final var avarta7 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta7.png?alt=media&token=b4029759-6277-4e26-8655-64b122534e5f"
    private final var avarta8 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta8.png?alt=media&token=a79d4605-d4f0-4c8e-ab4c-63206922f0d3"
    private final var avarta9 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Favarta9.png?alt=media&token=d58f40e7-8566-4f38-95a3-21b464e4678d"
    private final var avarta10 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Fdefault_avarta.png?alt=media&token=c13857a2-581a-47bf-bd43-ad9b9ce19d28"

    private final var icon1 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon1.png?alt=media&token=1e206ed2-49ba-4802-9035-f23db8e8353f"
    private final var icon2 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon2.png?alt=media&token=9d4d9886-2c5f-44b2-91e6-e0e30eba2910"
    private final var icon3 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon3.png?alt=media&token=8024822b-c9f7-4b2c-9bb8-37957b6a1d70"
    private final var icon4 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon4.png?alt=media&token=46584044-9baf-470a-a9d4-c021bda7776d"
    private final var icon5 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon5.png?alt=media&token=99af69ad-7cb3-4d02-8621-19b811c0ebb7"
    private final var icon6 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon6.png?alt=media&token=62ca37a4-d0de-48a9-8a8f-244ff87309a2"
    private final var icon7 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon7.png?alt=media&token=34b3564c-6f96-4bef-bafa-b8de62e978bc"
    private final var icon8 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon8.png?alt=media&token=c8cd437b-e938-411a-9c8d-e49c7cbe99f5"
    private final var icon9 = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/AvartaUser%2FAvailable%2Ficon9.png?alt=media&token=00e9aee9-617a-4e1f-bb28-9fa561aa145a"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        binding.btnBack.setOnClickListener {
            val i = Intent(this, HomeActivity().javaClass)
            i.putExtra("OrderToProfile",true)
            startActivity(i)
        }

        binding.img.setOnClickListener { view -> showDialogChooseAvarta() }

        binding.btnUpdateProfile.setOnClickListener { view -> uploadImage() }

        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        binding.txtBirthday.setOnClickListener(View.OnClickListener { view: View? ->
            val datePickerDialog = DatePickerDialog(this,
                { datePicker, year, month, day ->
                    var month = month
                    month = month + 1
                    val date = "$day/$month/$year"
                    binding.txtBirthday.text = date
                }, year, month, day
            )
            datePickerDialog.show()
        })
    }
    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getInfoUser()
    }
    private fun uploadImage() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Uploading File....")
        progressDialog!!.show()
        val userName: String = binding.txtUserName.text.toString()
        val fullName: String = binding.txtFullName.text.toString()
        val birthday: String = binding.txtBirthday.text.toString()
        val phone: String = binding.txtPhone.text.toString()
        val address: String = binding.txtAddress.text.toString()
        if (userName == "" || fullName == "" || birthday == "" || phone == "" || address == "") {
            Toast.makeText(this, "Fill full the information", Toast.LENGTH_SHORT)
                .show()
        } else {
            user.userName = userName
            user.fullName = fullName
            user.birthday = birthday
            user.phone = phone
            user.address = address
            if (checkAvartaFolder){
                storageReference = FirebaseStorage.getInstance().getReference("AvartaUser").child("Folder").child(user.userId.toString())
                storageReference!!.putFile(imageUri!!)
                    .addOnSuccessListener(OnSuccessListener<Any> { taskSnapshot ->
                        val uriTask: Task<Uri> = storageReference!!.downloadUrl
                        while (!uriTask.isComplete);
                        val uri = uriTask.result
                        user.img = uri.toString()
                        notificationUploadSuccess(userName,uri)
                    }).addOnFailureListener(OnFailureListener {
                        progressDialog!!.dismiss()
                        Toast.makeText(this,"Updated fail",Toast.LENGTH_SHORT).show()
                    })
            }else{
                var uri : Uri = Uri.parse(user.img)
                notificationUploadSuccess(userName,uri)
            }

        }
    }
    private fun notificationUploadSuccess(name : String , uri : Uri){
        val realtimeDatabase = FirebaseDatabase.getInstance().getReference("Users")
        realtimeDatabase.child(user.userId.toString()).setValue(user)

        var mFirebaseuser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = uri
        }

        mFirebaseuser!!.updateProfile(profileUpdates).addOnSuccessListener {
            Toast.makeText(this,"Updated profile",Toast.LENGTH_SHORT).show()
            progressDialog!!.dismiss()
            val i = Intent(this, HomeActivity().javaClass)
            i.putExtra("OrderToProfile",true)
            startActivity(i)
        }
    }
    private fun showDialogChooseAvarta(){
        val v = View.inflate(this, R.layout.dialog_img_user, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(v)
        val dialog = builder.create()

        v.findViewById<View>(R.id.layoutChooseFile).setOnClickListener{
            selectImage()
            dialog.dismiss()
            checkAvartaFolder = true
        }
        v.findViewById<View>(R.id.user_img_animal1).setOnClickListener{
            onClickAvarta(animal1,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal2).setOnClickListener{
            onClickAvarta(animal2,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal3).setOnClickListener{
            onClickAvarta(animal3,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal4).setOnClickListener{
            onClickAvarta(animal4,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal5).setOnClickListener{
            onClickAvarta(animal5,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal6).setOnClickListener{
            onClickAvarta(animal6,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal7).setOnClickListener{
            onClickAvarta(animal7,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal8).setOnClickListener{
            onClickAvarta(animal8,dialog)
        }
        v.findViewById<View>(R.id.user_img_animal9).setOnClickListener{
            onClickAvarta(animal9,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta1).setOnClickListener{
            onClickAvarta(avarta1,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta2).setOnClickListener{
            onClickAvarta(avarta2,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta3).setOnClickListener{
            onClickAvarta(avarta3,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta4).setOnClickListener{
            onClickAvarta(avarta4,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta5).setOnClickListener{
            onClickAvarta(avarta5,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta6).setOnClickListener{
            onClickAvarta(avarta6,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta7).setOnClickListener{
            onClickAvarta(avarta7,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta8).setOnClickListener{
            onClickAvarta(avarta8,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta9).setOnClickListener{
            onClickAvarta(avarta9,dialog)
        }
        v.findViewById<View>(R.id.user_img_avarta10).setOnClickListener{
            onClickAvarta(avarta10,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon1).setOnClickListener{
            onClickAvarta(icon1,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon2).setOnClickListener{
            onClickAvarta(icon2,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon3).setOnClickListener{
            onClickAvarta(icon3,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon4).setOnClickListener{
            onClickAvarta(icon4,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon5).setOnClickListener{
            onClickAvarta(icon5,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon6).setOnClickListener{
            onClickAvarta(icon6,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon7).setOnClickListener{
            onClickAvarta(icon7,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon8).setOnClickListener{
            onClickAvarta(icon8,dialog)
        }
        v.findViewById<View>(R.id.user_img_icon9).setOnClickListener{
            onClickAvarta(icon9,dialog)
        }


        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
    }
    private fun onClickAvarta(img : String,dialog : Dialog){
        user.img = img
        dialog.dismiss()
        Picasso.get()
            .load(user.img)
            .into(binding.img)
        checkAvartaFolder = false
    }
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }
    private fun getInfoUser() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                if (data.exists()) {
                    user = data.getValue(User::class.java) as User
                    println(user.toString())
                    showInfoUser()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
    private fun showInfoUser(){
        println("1"+user.toString())
        binding.txtUserName.setText(user.userName)
        binding.txtFullName.setText(user.fullName)
        binding.txtEmail.setText(user.email)
        binding.txtPhone.setText(user.phone)
        binding.txtBirthday.setText(user.birthday)
        binding.txtAddress.setText(user.address)

        if(user.img != null){
            Picasso.get()
                .load(user.img)
                .into(binding.img)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, Data: Intent?) {
        super.onActivityResult(requestCode, resultCode, Data)
        if (requestCode == 100 && Data != null && Data.data != null) {
            imageUri = Data.data
            println(imageUri)
            binding.img.setImageURI(imageUri)
        }
    }
}

