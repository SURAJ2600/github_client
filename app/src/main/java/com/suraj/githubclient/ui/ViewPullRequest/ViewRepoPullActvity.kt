package com.suraj.githubclient.ui.ViewPullRequest

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide

import com.suraj.githubclient.Extensions.obtainViewModel
import com.suraj.githubclient.R
import com.suraj.githubclient.Utilities.Util
import com.suraj.githubclient.model.PullRepoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_repo_pull_actvity.*
import kotlinx.android.synthetic.main.toolbar_view.*

class ViewRepoPullActvity : AppCompatActivity() {

    private lateinit var viewModel: ViewRepoPullViewModel
    private lateinit var prgressDialog: Dialog
    private val adapter = ViewPullRequestAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_repo_pull_actvity)
        prgressDialog = Util.ShowProgressView(this)

        viewModel = obtainViewModels()
        initViews()



        viewModel.apply {
            state.observe(this@ViewRepoPullActvity, Observer {


                when (it) {
                    ViewRepoPullViewModeltate.SUCESS -> {

                        prgressDialog.dismiss()

                    }
                    ViewRepoPullViewModeltate.LOADING -> {
                        prgressDialog.show()

                    }
                    ViewRepoPullViewModeltate.ERROR -> {
                        prgressDialog.dismiss()

                    }
                    ViewRepoPullViewModeltate.NONE -> {
                        prgressDialog.dismiss()

                    }


                }
            })


        }


    }


    /**
     *
     * @ Initialize the views from layout,Android trigger actions for click events
     */
    fun initViews() {


        //Title toolbar text and wiring up the back button action

        tv_title.text = "Pull Requests"
        img_back.setOnClickListener {
            finish()
        }

        //Wire up the recycler list view


        pull_list.apply {
            val decoration = DividerItemDecoration(this@ViewRepoPullActvity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        setupScrollListener()
        initAdapter()


        //Check the intent value and if not null pass the data to viewmodel

        if (intent != null) {
            var data = intent.extras
            viewModel.setOwnerAndRepoName(data.getString("owner_name"), data.getString("repo_name"))



            Glide
                .with(this)
                .load(data.getString("user_avatar"))
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(profile_image)
            tv_user_name.text ="User :"+ data.getString("owner_name")
            tv_reponame.text = "Repository : "+data.getString("repo_name")


        }


        /**
         *method to listen scroll changes of recylerview to load more data
         */

    }

    /**
     *
     * initialize the adapter and wire it to the  [repo_list]
     */

    private fun initAdapter() {
        pull_list.adapter = adapter
        viewModel.repos.observe(this, Observer<List<PullRepoModel>> {
            Log.d("Activity", "list: ${it?.size}")
            showVisibileViews(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })


    }

    @UiThread
    private fun showVisibileViews(status: Boolean) {

        if (status) {
            emptyListpull.visibility = View.VISIBLE
            pull_list.visibility = View.GONE
        } else {
            emptyListpull.visibility = View.GONE
            pull_list.visibility = View.VISIBLE
        }


    }

    private fun setupScrollListener() {

        val layoutManager = pull_list.layoutManager as LinearLayoutManager
        pull_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }


    fun obtainViewModels(): ViewRepoPullViewModel = obtainViewModel(ViewRepoPullViewModel::class.java)
}
