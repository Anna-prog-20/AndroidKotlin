package com.example.keepnotes.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private fun getUserNotesCollection() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION).document(firebaseUser.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().addSnapshotListener { snapshot, e ->
                    value = e?.let { exeption ->
                        NoteResult.Error(exeption)
                    }
                        ?: snapshot?.let { query ->
                            val notes = query.documents.map { document ->
                                document.toObject(Note::class.java)
                            }
                            NoteResult.Success(notes)
                        }
                }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(id)
                    .get()
                    .addOnSuccessListener { document ->
                        value = NoteResult.Success(document.toObject(Note::class.java))
                    }.addOnFailureListener { exeption ->
                        value = NoteResult.Error(exeption)
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Заметка $note успешно сохранена")
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        OnFailureListener { exeption ->
                            Log.d(
                                TAG,
                                "Ошибка при сохранении $note, сообщение: ${exeption.message}"
                            )
                            value = NoteResult.Error(exeption)
                        }
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
        }

    override fun deleteNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(note.id)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Заметка $note успешно удалена")
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        OnFailureListener { exeption ->
                            Log.d(TAG, "Ошибка при удалении $note, сообщение: ${exeption.message}")
                            value = NoteResult.Error(exeption)
                        }
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

}