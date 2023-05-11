package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_bill.R
import com.example.kotlin_bill.adapters.BankAdapter
import com.example.kotlin_bill.models.BankModel
import com.google.firebase.database.*

class BankFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var BankList: ArrayList<BankModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        BankList = arrayListOf<BankModel>()

        getBankData()


    }

    private fun getBankData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("BankDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               BankList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(BankModel::class.java)
                        BankList.add(empData!!)
                    }
                    val mAdapter = BankAdapter(BankList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BankAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@BankFetchingActivity, BankDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("bankId", BankList[position].bankId)
                            intent.putExtra("bankName", BankList[position].bankName)
                            intent.putExtra("bankBranch", BankList[position].bankBranch)
                            intent.putExtra("bankAmount", BankList[position].bankAmount)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}