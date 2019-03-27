
package com.suraj.githubclient.db

import android.arch.lifecycle.LiveData
import android.util.Log
import com.example.android.codelabs.paging.model.Repo
import com.suraj.githubclient.model.PullRepoModel
import java.util.concurrent.Executor


class GithubLocalCache(
    private val repoDao: GitHubClientRepo,
    private val ioExecutor: Executor
) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(repos: List<Repo>, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     * @param name repository name
     */
    fun reposByName(name: String): LiveData<List<Repo>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }

    /**
     * Insert a list of pull request data as [PullRepoModel] in the database, on a background thread.
     */
    fun insertPullRepo(repos: List<PullRepoModel>, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insertPullRepo(repos)
            insertFinished()
        }
    }



    /**
     * Request a LiveData<List<Repo>> from the Dao, based on sort DESC or AESC
     *
     */
    fun pullDataFromRepo(): LiveData<List<PullRepoModel>> {
        // appending '%' so we can allow other characters to be before and after the query string
        return repoDao.getPullRequestFromRepo()
    }


}