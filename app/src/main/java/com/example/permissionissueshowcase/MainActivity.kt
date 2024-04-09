package com.example.permissionissueshowcase

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.permissionissueshowcase.ui.theme.PermissionIssueShowcaseTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            var files by remember { mutableStateOf<List<Uri>?>(null) }

            val permissionState = rememberPermissionState(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            val launcher =
                rememberLauncherForActivityResult(contract = PermissableFolder()) { potentialUri ->
                    potentialUri?.let { uri ->
                        Log.i("MainActivity", "trying to take persistable permission for URI: $uri")
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                        Log.i("MainActivity", "persistable permission granted for URI: $uri")
                        files = readFiles(context, uri)
                    }
                }

            PermissionIssueShowcaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        // Checks to see if we have permission or requests it.
                        if (permissionState.status.isGranted) {
                            Text("WRITE_EXTERNAL_STORAGE permission granted")

                            if (files == null) {
                                OutlinedButton(onClick = { launcher.launch(null) }) {
                                    Text(text = "Trigger OPEN_DOCUMENT_TREE Intent")
                                }
                            } else {
                                files?.forEach { uri ->
                                    Text(text = uri.toString())
                                }
                            }

                            // Request permission logic.
                        } else {
                            Button(onClick = { permissionState.launchPermissionRequest() }) {
                                Text("Request WRITE_EXTERNAL_STORAGE permission")
                            }
                        }
                    }
                }
            }
        }
    }

    // https://stackoverflow.com/questions/62353325/how-to-get-access-to-a-directory-and-its-files-from-tree-uri-path-with-saf
    private fun readFiles(context: Context, uri: Uri): List<Uri> {
        val uriList: MutableList<Uri> = ArrayList()

        // the uri returned by Intent.ACTION_OPEN_DOCUMENT_TREE
        // the uri from which we query the files
        val uriFolder = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        var cursor: Cursor? = null
        try {
            // let's query the files
            cursor = context.contentResolver.query(
                uriFolder, arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
                null, null, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // build the uri for the file
                    val uriFile =
                        DocumentsContract.buildDocumentUriUsingTree(uri, cursor.getString(0))
                    //add to the list
                    uriList.add(uriFile)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // TODO: handle error
        } finally {
            cursor?.close()
        }

        //return the list
        return uriList
    }

    private class PermissableFolder : ActivityResultContracts.OpenDocumentTree() {
        override fun createIntent(context: Context, input: Uri?): Intent {
            return super.createIntent(context, input).apply {
                setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!", modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        PermissionIssueShowcaseTheme {
            Greeting("Android")
        }
    }
}