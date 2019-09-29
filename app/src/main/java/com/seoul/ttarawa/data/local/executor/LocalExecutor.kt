package com.seoul.ttarawa.data.local.executor

import com.seoul.ttarawa.data.local.LocalDataBase
import com.seoul.ttarawa.data.local.entity.NodeEntity
import com.seoul.ttarawa.data.local.entity.PathAndAllNodes
import com.seoul.ttarawa.data.local.entity.PathEntity
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class LocalExecutor private constructor(
    private val localDataBase: LocalDataBase
) {
    fun insertPath(path: PathEntity, nodes: List<NodeEntity>) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            localDataBase.runInTransaction(
                Callable<Boolean> {
                    localDataBase.getPathDao().insert(path)
                    val pathId = localDataBase.getPathDao().getLastPathId()

                    nodes.forEach {
                        it.pathId = pathId
                    }

                    localDataBase.getNodeDao().insert(*(nodes.toTypedArray()))
                    return@Callable true
                }
            )
        }
    }

    fun getPathAndNodesAll(): List<PathAndAllNodes> {
        val executor = Executors.newSingleThreadExecutor()
        val future =
            executor.submit(Callable<List<PathAndAllNodes>> {
                localDataBase.getPathDao().getPathWithNodesAll()
            })

        return future.get() ?: emptyList()
    }

    fun getPathAndNodes(pathId: Int): PathAndAllNodes? {
        val executor = Executors.newSingleThreadExecutor()
        val future =
            executor.submit(Callable<PathAndAllNodes> {
                localDataBase.getPathDao().getPathWithNodes(pathId)
            })

        return future.get() ?: null
    }

    fun getPath(): List<PathEntity> {
        val executor = Executors.newSingleThreadExecutor()
        val future =
            executor.submit(Callable<List<PathEntity>> {
                localDataBase.getPathDao().getPathList()
            })

        return future.get() ?: emptyList()
    }

    companion object {
        @Volatile
        private var instance: LocalExecutor? = null

        @JvmStatic
        fun getInstance(localDataBase: LocalDataBase): LocalExecutor =
            instance ?: synchronized(this) {
                instance ?: LocalExecutor(
                    localDataBase
                ).also {
                    instance = it
                }
            }
    }
}