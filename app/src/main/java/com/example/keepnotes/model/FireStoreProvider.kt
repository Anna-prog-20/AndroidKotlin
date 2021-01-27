package com.example.keepnotes.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                result.value = NoteResult.Error(e)
            } else if (snapshot != null) {
                val notes = mutableListOf<Note>()

                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                result.value =
                    NoteResult.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
            .set(note)
            .addOnSuccessListener {
                Log.d(TAG, "Заметка $note успешно сохранена")
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                OnFailureListener { exeption ->
                    Log.d(TAG, "Ошибка при сохранении $note, сообщение: ${exeption.message}")
                    result.value = NoteResult.Error(exeption)
                }
            }

        return result
    }

}