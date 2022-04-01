@file:Suppress("KDocUnresolvedReference")

package com.itsaky.androidide.filepicker

import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Point
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.webkit.MimeTypeMap
import com.itsaky.androidide.R
import com.itsaky.androidide.utils.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * A document provider for the Storage Access Framework which exposes the files in the $HOME/
 * directory to other apps.
 *
 *
 * Note that this replaces providing an activity matching the ACTION_GET_CONTENT intent:
 *
 *
 * "A document provider and ACTION_GET_CONTENT should be considered mutually exclusive. If you
 * support both of them simultaneously, your app will appear twice in the system picker UI, offering
 * two different ways of accessing your stored data. This would be confusing for users." -
 * http://developer.android.com/guide/topics/providers/document-provider.html#43
 *
 * @see [TermuxDocumentsProvider](https://github.com/termux/termux-app/blob/master/app/src/main/java/com/termux/filepicker/TermuxDocumentsProvider.java)
 */
class DocumentsProvider : DocumentsProvider() {
    override fun queryRoots(projection: Array<String>): Cursor {
        val result = MatrixCursor(projection)
        val applicationName = context!!.getString(R.string.app_name)
        val row = result.newRow()
        row.add(DocumentsContract.Root.COLUMN_ROOT_ID, getDocIdForFile(BASE_DIR))
        row.add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, getDocIdForFile(BASE_DIR))
        row.add(DocumentsContract.Root.COLUMN_SUMMARY, null)
        row.add(
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.FLAG_SUPPORTS_CREATE
                    or DocumentsContract.Root.FLAG_SUPPORTS_SEARCH
                    or DocumentsContract.Root.FLAG_SUPPORTS_IS_CHILD)
        row.add(DocumentsContract.Root.COLUMN_TITLE, applicationName)
        row.add(DocumentsContract.Root.COLUMN_MIME_TYPES, ALL_MIME_TYPES)
        row.add(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES, BASE_DIR.freeSpace)
        row.add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.ic_launcher)
        return result
    }
    
    @Throws(FileNotFoundException::class)
    override fun queryDocument(documentId: String, projection: Array<String>): Cursor {
        val result = MatrixCursor(projection)
        includeFile(result, documentId, null)
        return result
    }
    
    @Throws(FileNotFoundException::class)
    override fun queryChildDocuments(
        parentDocumentId: String, projection: Array<String>, sortOrder: String): Cursor {
        val result = MatrixCursor(projection)
        val parent = getFileForDocId(parentDocumentId)
        for (file in parent.listFiles()) {
            includeFile(result, null, file)
        }
        return result
    }
    
    @Throws(FileNotFoundException::class)
    override fun openDocument(
        documentId: String, mode: String, signal: CancellationSignal?): ParcelFileDescriptor {
        val file = getFileForDocId(documentId)
        val accessMode = ParcelFileDescriptor.parseMode(mode)
        return ParcelFileDescriptor.open(file, accessMode)
    }
    
    @Throws(FileNotFoundException::class)
    override fun openDocumentThumbnail(
        documentId: String, sizeHint: Point, signal: CancellationSignal): AssetFileDescriptor {
        val file = getFileForDocId(documentId)
        val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        return AssetFileDescriptor(pfd, 0, file.length())
    }
    
    override fun onCreate(): Boolean {
        return true
    }
    
    @Throws(FileNotFoundException::class)
    override fun createDocument(parentDocumentId: String, mimeType: String, displayName: String): String {
        var newFile = File(parentDocumentId, displayName)
        var noConflictId = 2
        while (newFile.exists()) {
            newFile = File(parentDocumentId, displayName + " (" + noConflictId++ + ")")
        }
        try {
            val succeeded: Boolean = if (DocumentsContract.Document.MIME_TYPE_DIR == mimeType) {
                newFile.mkdir()
            } else {
                newFile.createNewFile()
            }
            
            if (!succeeded) {
                throw FileNotFoundException(
                    "Failed to create document with id " + newFile.path)
            }
        } catch (e: IOException) {
            throw FileNotFoundException(
                "Failed to create document with id " + newFile.path)
        }
        return newFile.path
    }
    
    @Throws(FileNotFoundException::class)
    override fun deleteDocument(documentId: String) {
        val file = getFileForDocId(documentId)
        if (!file.delete()) {
            throw FileNotFoundException("Failed to delete document with id $documentId")
        }
    }
    
    @Throws(FileNotFoundException::class)
    override fun getDocumentType(documentId: String): String {
        val file = getFileForDocId(documentId)
        return getMimeType(file)
    }
    
    @Throws(FileNotFoundException::class)
    override fun querySearchDocuments(rootId: String, query: String, projection: Array<String>): Cursor {
        val result = MatrixCursor(projection)
        val parent = getFileForDocId(rootId)
        
        // This example implementation searches file names for the query and doesn't rank search
        // results, so we can stop as soon as we find a sufficient number of matches.  Other
        // implementations might rank results and use other data about files, rather than the file
        // name, to produce a match.
        val pending = LinkedList<File>()
        pending.add(parent)
        val maxSearchResults = 50
        while (!pending.isEmpty() && result.count < maxSearchResults) {
            val file = pending.removeFirst()
            // Avoid directories outside the $HOME directory linked with symlinks (to avoid e.g.
            // search
            // through the whole SD card).
            val isInsideHome: Boolean = try {
                file.canonicalPath.startsWith(Environment.ROOT.absolutePath)
            } catch (e: IOException) {
                true
            }
            
            if (isInsideHome) {
                if (file.isDirectory) {
                    Collections.addAll(pending, *file.listFiles())
                } else {
                    if (file.name.lowercase(Locale.ROOT).contains(query)) {
                        includeFile(result, null, file)
                    }
                }
            }
        }
        return result
    }
    
    override fun isChildDocument(parentDocumentId: String, documentId: String): Boolean {
        return documentId.startsWith(parentDocumentId)
    }
    
    /**
     * Add a representation of a file to a cursor.
     *
     * @param result the cursor to modify
     * @param docId the document ID representing the desired file (may be null if given file)
     * @param file the File object representing the desired file (may be null if given docID)
     */
    @Suppress("NAME_SHADOWING")
    @Throws(FileNotFoundException::class)
    private fun includeFile(result: MatrixCursor, docId: String?, file: File?) {
        var docId = docId
        var file = file
        if (docId == null) {
            docId = getDocIdForFile(file)
        } else {
            file = getFileForDocId(docId)
        }
        var flags = 0
        if (file!!.isDirectory) {
            if (file.canWrite()) flags = flags or DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE
        } else if (file.canWrite()) {
            flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_WRITE
        }
        if (file.parentFile.canWrite()) flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_DELETE
        val displayName = file.name
        val mimeType = getMimeType(file)
        if (mimeType.startsWith("image/")) flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL
        val row = result.newRow()
        row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, docId)
        row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, displayName)
        row.add(DocumentsContract.Document.COLUMN_SIZE, file.length())
        row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType)
        row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
        row.add(DocumentsContract.Document.COLUMN_FLAGS, flags)
        row.add(DocumentsContract.Document.COLUMN_ICON, R.mipmap.ic_launcher)
    }
    
    companion object {
        private const val ALL_MIME_TYPES = "*/*"
        private val BASE_DIR = Environment.ROOT
        
        /**
         * Get the document id given a file. This document id must be consistent across time as other
         * applications may save the ID and use it to reference documents later.
         *
         *
         * The reverse of @{link #getFileForDocId}.
         */
        private fun getDocIdForFile(file: File?): String {
            return file!!.absolutePath
        }
        
        /** Get the file given a document id (the reverse of [.getDocIdForFile]).  */
        @Throws(FileNotFoundException::class)
        private fun getFileForDocId(docId: String): File {
            val f = File(docId)
            if (!f.exists()) throw FileNotFoundException(f.absolutePath + " not found")
            return f
        }
        
        private fun getMimeType(file: File?): String {
            return if (file!!.isDirectory) {
                DocumentsContract.Document.MIME_TYPE_DIR
            } else {
                val name = file.name
                val lastDot = name.lastIndexOf('.')
                if (lastDot >= 0) {
                    val extension = name.substring(lastDot + 1).lowercase(Locale.ROOT)
                    val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                    if (mime != null) return mime
                }
                "application/octet-stream"
            }
        }
    }
}