package com.suraj.githubclient.ui.ViewPullRequest

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.suraj.githubclient.Extensions.obtainViewModel
import com.suraj.githubclient.R
import com.suraj.githubclient.Utilities.LogsUtils
import com.suraj.githubclient.Utilities.Util
import com.suraj.githubclient.ui.SearchRepo.SearchRepoViewModel
import kotlinx.android.synthetic.main.toolbar_view.*

class ViewRepoPullActvity : AppCompatActivity() {

    private lateinit var viewModel: ViewRepoPullViewModel
    private lateinit var prgressDialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_repo_pull_actvity)
        prgressDialog =Util.ShowProgressView(this)

        viewModel=obtainViewModels()
        initViews()



        viewModel.apply {
            state.observe(this@ViewRepoPullActvity, Observer {


                when(it)
                {
                    ViewRepoPullViewModeltate.SUCESS->{

                        prgressDialog.dismiss()

                    }
                    ViewRepoPullViewModeltate.LOADING->
                    {
                        prgressDialog.show()

                    }
                    ViewRepoPullViewModeltate.ERROR->
                    {
                        prgressDialog.dismiss()

                    }
                    ViewRepoPullViewModeltate.NONE->{
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

        tv_title.text = "Pull Requests"
        img_back.setOnClickListener {
            finish()
        }
        if (intent != null) {
            var data = intent.extras
            viewModel.getPullRequestFromRepo(data.getString("owner_name"),data.getString("repo_name"))
            viewModel.repoResult.observe(this@ViewRepoPullActvity, Observer {

                LogsUtils.makeLogE("searchvlaues",">>>"+it?.data?.value)
            })

        }




    }

    fun obtainViewModels(): ViewRepoPullViewModel = obtainViewModel(ViewRepoPullViewModel::class.java)
}
