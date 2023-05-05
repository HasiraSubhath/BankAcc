package com.example.kotlin_bill.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlin_bill.models.BankModel
import com.example.kotlin_bill.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BankInsertionActivity : AppCompatActivity() {
    //initializing variables

    private lateinit var etBankName: EditText
    private lateinit var etBankBranch: EditText
    private lateinit var etBankAmount: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_insertion)

        etBankName = findViewById(R.id.etBankName)
        etBankBranch = findViewById(R.id.etBankBranch)
        etBankAmount = findViewById(R.id.etBankAmount)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("BankDB")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }

    }

    private fun saveEmployeeData() {

        //Geting Values
        val bankName = etBankName.text.toString()
        val bankBranch = etBankBranch.text.toString()
        val bankAmount = etBankAmount.text.toString()

        //validation
        if (bankName.isEmpty()) {
            etBankName.error = "Please enter name"
        }
        if (bankBranch.isEmpty()) {
            etBankBranch.error = "Please enter age"
        }
        if (bankAmount.isEmpty()) {
            etBankAmount.error = "Please enter salary"
        }

        //genrate unique ID
        val bankId = dbRef.push().key!!

        val employee = BankModel(bankId, bankName, bankBranch, bankAmount)

        dbRef.child(bankId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this,"data insert successfully",Toast.LENGTH_SHORT).show()

                //clear data after insert
                etBankName.text.clear()
                etBankBranch.text.clear()
                etBankAmount.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }

}