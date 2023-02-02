package com.ntmk.myapp.adapters

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ZListUserItemAdminBinding
import com.ntmk.myapp.model.User
import java.util.*

class ListAdminUserAdapter(var context: Context, var listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListAdminUserAdapter.ListUserAdminViewHolder>() {
    var mContext: Context? = context

    inner class ListUserAdminViewHolder(var v: ZListUserItemAdminBinding) :
        RecyclerView.ViewHolder(v.root) {
        private var txtName: TextView? = null
        private var txtEmail: TextView? = null
        var layout: LinearLayout? = null

        init {
            layout = v.layoutUserAdmin
            txtName = v.txtName
            txtEmail = v.txtEmail
        }

        public fun popupMenus(v: View, user: User) {
            var popupMenu = PopupMenu(mContext, v, Gravity.RIGHT)
//            popupMenu.menu.get(2).
            popupMenu.inflate(R.menu.show_menu_user)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuDetail -> {
                        showDialogDetail(user)
                        true
                    }
                    R.id.menuEdit -> {
                        showDialogEdit(user)
                        true
                    }
                    R.id.menuDelete -> {

                        deleteUser(user)
                        true
                    }

                    else -> true
                }

            }
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }

        private fun showDialogDetail(user: User) {
            val dialog = Dialog(mContext!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_user_admin)
            val window = dialog.window ?: return
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val windowAttribute = window.attributes
            windowAttribute.gravity = Gravity.CENTER

            val txtBirthday = dialog.findViewById<TextView>(R.id.txtBirthday)
            val txtPhone = dialog.findViewById<EditText>(R.id.txtPhone)
            val txtNameAccount = dialog.findViewById<EditText>(R.id.txtNameAccount)
            val txtPass = dialog.findViewById<EditText>(R.id.txtPass)
            val txtName = dialog.findViewById<EditText>(R.id.txtName)
            val txtEmail = dialog.findViewById<EditText>(R.id.txtEmail)
            val txtAddress = dialog.findViewById<EditText>(R.id.txtAddress)
            val btnUpdateProfile = dialog.findViewById<Button>(R.id.btnUpdateProfile)

            btnUpdateProfile.visibility = View.GONE
            txtBirthday.setText(user.birthday)
            txtPhone.setText(user.phone)
            txtNameAccount.setText(user.userName)
            txtPass.setText(user.pass)
            txtName.setText(user.fullName)
            txtEmail.setText(user.email)
            txtAddress.setText(user.address)

            txtBirthday.isFocusable = false
            txtPhone.isFocusable = false
            txtNameAccount.isFocusable = false
            txtPass.isFocusable = false
            txtName.isFocusable = false
            txtEmail.isFocusable = false
            txtAddress.isFocusable = false
//        EditText editText = dialog.findViewById(R.id.layout);
            dialog.show()
        }

        private fun showDialogEdit(user: User) {
            val dialog = Dialog(mContext!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_user_admin)
            val window = dialog.window ?: return
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val windowAttribute = window.attributes
            windowAttribute.gravity = Gravity.CENTER

            val txtBirthday = dialog.findViewById<TextView>(R.id.txtBirthday)
            val txtPhone = dialog.findViewById<EditText>(R.id.txtPhone)
            val txtNameAccount = dialog.findViewById<EditText>(R.id.txtNameAccount)
            val txtPass = dialog.findViewById<EditText>(R.id.txtPass)
            val txtName = dialog.findViewById<EditText>(R.id.txtName)
            val txtEmail = dialog.findViewById<EditText>(R.id.txtEmail)
            val txtAddress = dialog.findViewById<EditText>(R.id.txtAddress)
            val btnUpdateProfile = dialog.findViewById<Button>(R.id.btnUpdateProfile)

            btnUpdateProfile.text = "UPDATE USER"

            txtBirthday.setText(user.birthday)
            txtPhone.setText(user.phone)
            txtNameAccount.setText(user.userName)
            txtPass.setText(user.pass)
            txtName.setText(user.fullName)
            txtEmail.setText(user.email)
            txtAddress.setText(user.address)
            txtEmail.isFocusable = false
            txtAddress.setText(user.address)

            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            txtBirthday.setOnClickListener { view: View? ->
                val datePickerDialog = DatePickerDialog(
                    mContext!!,
                    { datePicker, year, month, day ->
                        var month = month
                        month = month + 1
                        val date = "$day/$month/$year"
                        txtBirthday.text = date
                    }, year, month, day
                )
                datePickerDialog.show()
            }
            btnUpdateProfile.setOnClickListener { view: View? ->
                val birthday = txtBirthday.text.toString()
                val phone = txtPhone.text.toString()
                val nameAccount = txtNameAccount.text.toString()
                val pass = txtPass.text.toString()
                val name = txtName.text.toString()
                val email = txtEmail.text.toString()
                val address = txtAddress.text.toString()
                if (birthday == "" || phone == "" || nameAccount == "" || name == "" || address == "" || email == "" || pass == "") {
                    Toast.makeText(
                        mContext,
                        "Please fill in your personal information",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (pass.length < 6) {
                    Toast.makeText(
                        mContext,
                        "Password length should be 6 characters",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(mContext, "Invalid Email", Toast.LENGTH_SHORT).show()
                } else {
                    val mFirebaseUser = FirebaseAuth.getInstance().currentUser
                    mFirebaseUser!!.updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(nameAccount)
                            .build()
                    )

                    // add user to realtime database
                    val realtimeDatabase =
                        FirebaseDatabase.getInstance().getReference("Users")
                    val mUser = User(
                        user.userId,
                        nameAccount,
                        name,
                        email,
                        pass,
                        phone,
                        address,
                        birthday,
                        user.img

                    )
                    realtimeDatabase.child(user.userId.toString()).setValue(mUser)
                    Toast.makeText(mContext, "Update user success", Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            dialog.show()
        }
        private fun deleteUser(user: User){
            val mDatabaseJobHeart =
                FirebaseDatabase.getInstance().getReference("Users").child(user.userId.toString())
            mDatabaseJobHeart.removeValue()

            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(user.email.toString(), user.pass.toString())
                .addOnCompleteListener(
                    (mContext as Activity?)!!
                ) { task ->
                    if (task.isSuccessful) {
                        val mFirebaseUser = FirebaseAuth.getInstance().currentUser
                        mFirebaseUser!!.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(mContext, "Delete success", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                }

            listUser.remove(user)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserAdminViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListUserItemAdminBinding>(
            infler, R.layout.z_list_user_item_admin, parent, false
        )
        return ListUserAdminViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListUserAdminViewHolder, position: Int) {
        holder.v.user = listUser[position]
        var user = listUser[position]
        holder.layout!!.setOnClickListener { holder.popupMenus(it, user) }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}