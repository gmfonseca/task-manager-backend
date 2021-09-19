package br.com.gmfonseca.taskmanager.utils.ext

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import java.util.UUID

suspend fun MultiPartData.mapNameToBytes(): Pair<String, ByteArray> {
    var fileName = UUID.randomUUID().toString()
    lateinit var fileBytes: ByteArray

    forEachPart { part ->
        if (part is PartData.FileItem) {
            if (!fileName.contains(".")) {
                part.originalFileName
                    ?.substringAfterLast(".")
                    ?.also { fileName += ".$it" }
            }

            fileBytes = part.streamProvider().readBytes()
        }
    }

    return fileName to fileBytes
}
