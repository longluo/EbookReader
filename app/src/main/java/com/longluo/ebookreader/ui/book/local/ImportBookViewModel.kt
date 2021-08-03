package com.longluo.ebookreader.ui.book.local

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.longluo.ebookreader.base.BaseViewModel
import com.longluo.ebookreader.model.localBook.LocalBook
import com.longluo.ebookreader.utils.DocItem
import com.longluo.ebookreader.utils.DocumentUtils
import com.longluo.ebookreader.utils.isContentScheme
import java.io.File
import java.util.*


class ImportBookViewModel(application: Application) : BaseViewModel(application) {

    fun addToBookshelf(uriList: HashSet<String>, finally: () -> Unit) {
        execute {
            uriList.forEach {
                LocalBook.importFile(Uri.parse(it))
            }
        }.onFinally {
            finally.invoke()
        }
    }

    fun deleteDoc(uriList: HashSet<String>, finally: () -> Unit) {
        execute {
            uriList.forEach {
                val uri = Uri.parse(it)
                if (uri.isContentScheme()) {
                    DocumentFile.fromSingleUri(context, uri)?.delete()
                } else {
                    uri.path?.let { path ->
                        File(path).delete()
                    }
                }
            }
        }.onFinally {
            finally.invoke()
        }
    }

    fun scanDoc(
        documentFile: DocumentFile,
        isRoot: Boolean,
        find: (docItem: DocItem) -> Unit,
        finally: (() -> Unit)? = null
    ) {
        val docList = DocumentUtils.listFiles(context, documentFile.uri)
        docList.forEach { docItem ->
            if (docItem.isDir) {
                DocumentFile.fromSingleUri(context, docItem.uri)?.let {
                    scanDoc(it, false, find)
                }
            } else if (docItem.name.endsWith(".txt", true)
                || docItem.name.endsWith(".epub", true)
            ) {
                find(docItem)
            }
        }
        if (isRoot) {
            finally?.invoke()
        }
    }

    fun scanFile(
        file: File,
        isRoot: Boolean,
        find: (docItem: DocItem) -> Unit,
        finally: (() -> Unit)? = null
    ) {
        file.listFiles()?.forEach {
            if (it.isDirectory) {
                scanFile(it, false, find)
            } else if (it.name.endsWith(".txt", true)
                || it.name.endsWith(".epub", true)
            ) {
                find(
                    DocItem(
                        it.name,
                        it.extension,
                        it.length(),
                        Date(it.lastModified()),
                        Uri.parse(it.absolutePath)
                    )
                )
            }
        }
        if (isRoot) {
            finally?.invoke()
        }
    }

}