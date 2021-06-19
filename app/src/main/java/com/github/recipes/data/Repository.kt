package com.github.recipes.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * inject remote data source inside this repository
 * ActivityRetainedScoped means for binding that should exist for the life of an activity and survive configuration change as well
 * later we inject this Repository inside our view model
 */
@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    // we can access this variable inside our view model later
    val remote = remoteDataSource

    val local = localDataSource
}