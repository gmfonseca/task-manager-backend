package br.com.gmfonseca.taskmanager.data.repositories

interface RepositoryFactory<T> {
    operator fun invoke(): T
}
