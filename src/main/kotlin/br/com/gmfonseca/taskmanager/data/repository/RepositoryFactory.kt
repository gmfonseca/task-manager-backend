package br.com.gmfonseca.taskmanager.data.repository

interface RepositoryFactory<T> {
    operator fun invoke(): T
}
