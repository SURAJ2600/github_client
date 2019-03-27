

package com.suraj.githubclient.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.android.codelabs.paging.model.Repo
import com.suraj.githubclient.model.PullRepoModel

/**
 * Room data access object for accessing the [Repo] table.
 */
@Dao
interface GitHubClientRepo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repo>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPullRepo(data: List<PullRepoModel>)

    // Do a similar query as the search API:
    // Look for repos that contain the query string in the name or in the description
    // and order those results descending, by the number of stars and then by name
    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE " +
            ":queryString) OR (fullName LIKE :queryString) ORDER BY stars DESC, name ASC")
    fun reposByName(queryString: String): LiveData<List<Repo>>


    // Do a similar query as the search pull request of a repo:
    // Look for pull request of specfic repo
    // and order those results descending, by the number of stars and then by name
    @Query("SELECT * FROM PullRepoModel")
    fun getPullRequestFromRepo(): LiveData<List<PullRepoModel>>

}